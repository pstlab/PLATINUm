package it.istc.pst.platinum.deliberative.solver;

import java.util.Set;

import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.PlanRefinementException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class BestFirstSolver extends PlannerSolver 
{
	/**
	 * 
	 * @param timeout
	 */
	protected BestFirstSolver(long timeout) {
		super(PlannerSolverType.BEST_FIRST.getLabel(), timeout);
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
		SearchSpaceNode root = this.createSearchSpaceNode();
		this.fringe.enqueue(root);
		
		// starts solution search
		while (!exit) 
		{
			try 
			{
				// new solving step
				this.stepCounter++;
				// get time passed from the start 
				long now = System.currentTimeMillis() - start;
				// check timeout
				if (this.timeout > 0 && now > this.timeout) 
				{
					// set solving time
					this.time = System.currentTimeMillis() - start;
					// backtrack from the last propagated node
					this.backtrack(last);
					// timeout exception
					throw new NoSolutionFoundException("Timeout: no solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
				}
				
				
				logger.debug("Solving step " + this.stepCounter);
				
				// extract next node from the fringe
				extracted = this.fringe.dequeue();
				// propagate node
				logger.debug("Propagating node:\n" + extracted + "\nof " + this.fringe.getFringeSize() + " available in the fringe");
				
				// context switch
				this.contextSwitch(last, extracted);
				// updated last propagated node
				last = extracted;
				
				try {
					// consistency check
					this.pdb.verify();
					logger.debug("Plan refinement successfully done... looking for flaws on the current refined plan...");
				}
				catch (PseudoControllabilityCheckException ex) {
					// ignoring pseudo-controllability issues
					logger.debug("BestFirstSolver ignores pseudo-controllability issues of the plan.... continue the search");
				}
				
 				// get the "best" flaws to solve first
				Set<Flaw> flaws = this.heuristic.choose();
				// take into account all the "equivalent" flaws as possible plan refinements
				for (Flaw flaw : flaws) 
				{ 
					// create a branch for each possible solution of a flaw
					for (SearchSpaceNode child : this.expand(extracted, flaw)) {
						// enqueue node
						this.fringe.enqueue(child);
						logger.debug("Expanding the search space with:\n- node= " + child + "\n");
					}
				}
				
			}
			catch (PlanRefinementException | ConsistencyCheckException | UnsolvableFlawException ex) {
				// unsolvable flaw found
				logger.error("Error during plan refinement:\n " + ex.getMessage());
			}
			catch (EmptyFringeException ex) {
				
				// impossible to find a solution
				logger.debug("No more node in the fringe... ");
				
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
				logger.info("Solution found after " + this.time + " msecs and " + this.stepCounter + " solving steps");
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
