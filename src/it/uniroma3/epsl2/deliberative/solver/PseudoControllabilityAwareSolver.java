package it.uniroma3.epsl2.deliberative.solver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.deliberative.search.SearchStrategy;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyFactory;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.PlanControllabilityType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.tn.stnu.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityAwareSolver extends Solver {

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
		this.blacklist = sf.create(SearchStrategyType.DFD);
	}
	
	/**
	 * 
	 */
	@Override
	public Plan solve() 
			throws NoSolutionFoundException 
	{
		// prepare the search
		boolean exit = false;								// process end flag
		long start = System.currentTimeMillis();			// execution start time
		this.stepCounter = 0;								// solving step counter

		SearchSpaceNode extracted = null;			// current extracted node
		SearchSpaceNode last = null;				// root node
		
		// the solution plan
		Plan plan = null;
		
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
				
				// check if last is root node
				if (this.stepCounter > 1) 
				{
					// extract next node from the fringe
					extracted = this.strategy.dequeue();
					// propagate node
					this.logger.debug("Propagating node:\n" + extracted + "\nof " + this.strategy.getFringeSize() + " available in the fringe");
					// context switch
					this.contextSwitch(last, extracted);
					// updated last propagated node
					last = extracted;
				} 
				else 
				{
					// create root node
					extracted = new SearchSpaceNode();
					// set extracted as the root node
					last = extracted;
				}
				
				try 
				{
					// consistency check
					this.pdb.check();
					this.logger.debug("Plan refinement successfully done...");
				}
				catch (PseudoControllabilityCheckException ex) 
				{
					// check solving modality
					if (pseudocontrollability) {
						// add node to the priority queue
						this.blacklist.enqueue(extracted);
						// skip
						skip = true;
						// warning
						this.logger.warning("Controllability issues during plan refinement:\n- " + ex.getPseudoControllabilityIssues());
					}
				}
				
				// check solving flags
				if (!skip) {
					
					// looks for flaws
					this.logger.debug("Looking for flaws on the current refined plan...");
	 				// choose the best flaws to solve
					List<Flaw> flaws = new ArrayList<>(this.heuristic.choose());
					// create a branch for each "equivalent" flaw to solve next
					for (Flaw flaw : flaws)
					{
						// create a branch for each possible solution of the flaw
						for (FlawSolution flawSolution : flaw.getSolutions()) 
						{
							// expand the search tree
							this.logger.debug("Expanding the search tree with flaw to solve:\n" + flaw + "\n- #solutions= " + flaw.getSolutions().size());
							// create operator
							Operator op = new Operator(flawSolution);
							// create child node
							SearchSpaceNode child = new SearchSpaceNode(extracted, op);
							// enqueue node
							this.strategy.enqueue(child);
							// expand the search space
							this.logger.debug("Child-node= " + child + ":\n- operator= " + op + "\n- flaw= " + flaw + "\n- solution= " + flawSolution);
						}
					}
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
					this.logger.debug("No more node in the fringe but still flaws to solve [" + this.blacklist.getFringeSize() +  "] ...");
					// change solving modality
					pseudocontrollability = false;
					// enqueue not pseudo-controllable node
					this.strategy.enqueue(this.blacklist.dequeue());
					// notify changing modality
					this.logger.warning("Entering in no pseudo-controllability mode");
				}
				catch (EmptyFringeException exx) 
				{
					/*
					 * TODO : RETRACT INITIAL PROBLEM
					 */
					
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
					plan = this.pdb.getPlan();
					plan.setControllability(PlanControllabilityType.PSEUDO_CONTROLLABLE);
					plan.setSolvingTime(this.time);
					// pseudo-controllable solution found
					this.logger.info("Pseudo-controllable solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
				}
				else 
				{
					// set plan
					plan = this.pdb.getPlan();
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
