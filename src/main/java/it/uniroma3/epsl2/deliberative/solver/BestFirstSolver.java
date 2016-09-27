package it.uniroma3.epsl2.deliberative.solver;

import java.util.Comparator;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.tn.stnu.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class BestFirstSolver extends Solver implements Comparator<SearchSpaceNode> 
{
	private long time;
	private long stepCounter;
	
	/**
	 * 
	 */
	protected BestFirstSolver() {
		super(SolverType.BEST_FIRST);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// compare depths
		return o1.getDepth() >= o2.getDepth() ? -1 : 1;
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

		SearchSpaceNode extracted = null;			// current extracted node
		SearchSpaceNode last = null;				// root node
		
		// the solution plan
		SolutionPlan plan = null;
		// starts solution search
		while (!exit) 
		{
			try 
			{
				// new solving step
				this.stepCounter++;
				this.logger.debug("Solving step " + this.stepCounter);
				
				// check if last is root node
				if (this.stepCounter > 1) {
					// extract next node from the fringe
					extracted = this.strategy.dequeue();
					// propagate node
					this.logger.debug("Propagating node:\n" + extracted + "\nof " + this.strategy.getFringeSize() + " available in the fringe");
					
					// context switch
					this.contextSwitch(last, extracted);
					// updated last propagated node
					last = extracted;
				} 
				else {
					// create root node
					extracted = new SearchSpaceNode();
					// set extracted as the root node
					last = extracted;
				}
				
				try {
					// consistency check
					this.pdb.check();
					this.logger.debug("Plan refinement successfully done... looking for flaws on the current refined plan...");
				}
				catch (PseudoControllabilityCheckException ex) {
					// ignoring pseudo-controllability issues
					this.logger.debug("BestFirstSolver ignores pseudo-controllability issues of the plan.... continue the search");
				}
				
 				// get the "best" flaws to solve first
				Set<Flaw> flaws = this.heuristic.choose();
				// take into account all the "equivalent" flaws as possible plan refinements
				for (Flaw flaw : flaws) 
				{ 
					// create a branch for each possible solution of a flaw
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
						this.logger.debug("Child-node= " + child + ":\n- operator= " + op + "\n- flaw= " + flaw + "\n- solution= " + flawSolution);
					}
				}
				
			}
			catch (PlanRefinementException | ConsistencyCheckException | UnsolvableFlawFoundException ex) {
				// unsolvable flaw found
				this.logger.error("Error during plan refinement:\n " + ex.getMessage());
			}
			catch (EmptyFringeException ex) {
				
				// impossible to find a solution
				this.logger.debug("No more node in the fringe... ");
				
				/*
				 * TODO : RETRACT INITIAL PROBLEM
				 */
					
				// get solving time
				this.time = System.currentTimeMillis() - start;
				// no solution found
				throw new NoSolutionFoundException("No solution found after " + this.time  + " msecs and " + this.stepCounter  + " solving steps");
			}
			catch (NoFlawFoundException ex) {
				// get solving time
				this.time = System.currentTimeMillis() - start;
				// solution found 
				exit = true;
				this.logger.info("Solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
			}
			
		} // end while

		
		// get the resulting solution plan
		plan = this.pdb.getSolutionPlan();
		// set solving time
		plan.setSolvingTime(this.time);
		// get plan
		return plan;
	}
}
