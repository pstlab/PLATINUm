package it.uniroma3.epsl2.deliberative.solver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.deliberative.search.SearchStrategy;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyFactory;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.PlanControllabilityType;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityAwareSolver extends Solver 
{
	private long time;
	private long stepCounter;
	private SearchStrategy blacklist;
	
	/**
	 * 
	 */
	protected PseudoControllabilityAwareSolver() {
		super(SolverType.PSEUDO_CONTROLLABILITY_AWARE);
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		SearchStrategyFactory sf = new SearchStrategyFactory();
		this.blacklist = sf.create(SearchStrategyType.DFS);
	}
	
	/**
	 * 
	 */
	@Override
	public SolutionPlan solve() 
			throws NoSolutionFoundException 
	{
		// prepare the search
		boolean exit = false;								// process end flag
		long start = System.currentTimeMillis();			// execution start time
		this.stepCounter = 0;								// solving step counter

		// the solution plan
		SolutionPlan plan = null;
		SearchSpaceNode extracted = null;			// current extracted node
		SearchSpaceNode last = null;				// root node
		// create root node and add to the fringe
		SearchSpaceNode root = new SearchSpaceNode();
		this.strategy.enqueue(root);
		
		// start with pseudo-controllability mode
		boolean pseudocontrollability = true;
		// starts solution search
		while (!exit) 
		{
			try 
			{
				// new solving step
				this.stepCounter++;
				this.logger.debug("Solving step " + this.stepCounter);
				// skip flag
				boolean skip = false;
				
				// extract next node from the fringe
				extracted = this.strategy.dequeue();
				// propagate node
				this.logger.debug("Propagating node:\n" + extracted + "\nof " + this.strategy.getFringeSize() + " available in the fringe");
				// context switch
				this.contextSwitch(last, extracted);
				// updated last propagated node
				last = extracted;
				
				try 
				{
					// consistency check
					this.pdb.check();
					this.logger.debug("Plan refinement successfully done...");
				}
				catch (PseudoControllabilityCheckException ex) 
				{
					// check solving modality
					if (pseudocontrollability) 
					{
						// add node to the black list
						this.blacklist.enqueue(extracted);
						// skip
						skip = true;
						// warning
						this.logger.warning("Controllability issues during plan refinement:\n- " + ex.getPseudoControllabilityIssues());
					}
				}
				
				// check solving flags
				if (!skip) 
				{
					// looks for flaws
					String info = "[step= " + this.stepCounter + "] Looking for flaws on the current plan...\n";
	 				// choose the best flaws to solve
					List<Flaw> flaws = new ArrayList<>(this.heuristic.choose());
					info += "Selected Flaw(s) to solve:\n";
					for (Flaw flaw : flaws) {
						info += "- " + flaw + "\n";
					}
					// print info
					this.logger.info(info);
					
					// create a branch for each "equivalent" flaw to solve next
					for (Flaw flaw : flaws)
					{
						// create a branch for each possible solution of the flaw
						for (FlawSolution flawSolution : flaw.getSolutions()) 
						{
							try
							{
								// try apply flaw solution
								this.pdb.propagete(flawSolution);
								
								// compute the resulting makespan
								double makespan = this.pdb.computeMakespan();
								// get resulting agenda
								Agenda agenda = this.pdb.getAgenda();
								
								// create operator
								Operator op = new Operator(flawSolution);
								// create child node
								SearchSpaceNode child = new SearchSpaceNode(extracted, op);
								// set the makespan
								child.setMakespan(makespan);
								// set agenda 
								child.setAgenda(agenda);
								// enqueue node
								this.strategy.enqueue(child);
								// expand the search space
								this.logger.debug("Search tree expansion:\nChild-node= " + child + ":\n- operator= " + op + "\n- flaw= " + flaw + "\n- solution= " + flawSolution);
								
								// retract propagated flaw solution
								this.pdb.retract(flawSolution);
							}
							catch (FlawSolutionApplicationException ex) 
							{
								// unfeasible solution found
								this.logger.warning("Unfeasible flaw solution found.\n- solution" + flawSolution + "\n Skip resulting child node");
							}
						}
					}
				}
				else {
					// skipping current plan
					this.logger.info("Skipping current plan for controllability issues...");
				}
			}
			catch (PlanRefinementException | ConsistencyCheckException | UnsolvableFlawFoundException ex) {
				// unsolvable flaw found
				this.logger.error("Error during plan refinement:\n " + ex.getMessage());
			}
			catch (EmptyFringeException ex) 
			{
				try 
				{
					// no pseudo-controllable solution try with not pseudo-controllable ones
					pseudocontrollability = false;
					// enqueue not pseudo-controllable node
					this.strategy.enqueue(this.blacklist.dequeue());
					// notify changing modality
					this.logger.warning("No more node in the fringe but still flaws to solve [" + this.blacklist.getFringeSize() +  "] ...\nEntering in no pseudo-controllability mode\n");
				}
				catch (EmptyFringeException exx) 
				{
					// get solving time
					this.time = System.currentTimeMillis() - start;
					// no solution found
					throw new NoSolutionFoundException("No solution found after " + this.time  + " msecs and " + this.stepCounter  + " solving steps");
				}
			}
			catch (NoFlawFoundException ex) 
			{
				// get solving time
				this.time = System.currentTimeMillis() - start;
				// solution found
				exit = true;
				// check solving modality
				if (pseudocontrollability) 
				{
					// set plan
					plan = this.pdb.getSolutionPlan();
					plan.setControllability(PlanControllabilityType.PSEUDO_CONTROLLABLE);
					plan.setSolvingTime(this.time);
					// pseudo-controllable solution found
					this.logger.info("Pseudo-controllable solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
				}
				else 
				{
					// set plan
					plan = this.pdb.getSolutionPlan();
					plan.setControllability(PlanControllabilityType.NOT_PSEUDO_CONTROLLABLE);
					plan.setSolvingTime(this.time);
					// not pseudo-controllable solution found
					this.logger.info("Not pseudo-controllable solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
				}
			}
			
		} // end while

		// get solution plan
		return plan;
	}
}	
