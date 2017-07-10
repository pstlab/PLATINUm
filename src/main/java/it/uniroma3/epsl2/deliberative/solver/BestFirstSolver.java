package it.uniroma3.epsl2.deliberative.solver;

import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristic;
import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.deliberative.strategy.SearchStrategy;
import it.uniroma3.epsl2.deliberative.strategy.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.strategy.ex.EmptyFringeException;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.FlawSelectionHeuristicModule;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.SearchStrategyModule;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class BestFirstSolver extends Solver 
{
	@SearchStrategyModule(strategy= SearchStrategyType.DFS)
	private SearchStrategy strategy;
	
	@FlawSelectionHeuristicModule(heuristics= FlawSelectionHeuristicType.HFS)
	private FlawSelectionHeuristic heuristic;
	
	/**
	 * 
	 */
	protected BestFirstSolver() {
		super(SolverType.BEST_FIRST.getLabel());
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
		
		// starts solution search
		while (!exit) 
		{
			try 
			{
				// new solving step
				this.stepCounter++;
				this.logger.debug("Solving step " + this.stepCounter);
				
				// extract next node from the fringe
				extracted = this.strategy.dequeue();
				// propagate node
				this.logger.debug("Propagating node:\n" + extracted + "\nof " + this.strategy.getFringeSize() + " available in the fringe");
				
				// context switch
				this.contextSwitch(last, extracted);
				// updated last propagated node
				last = extracted;
				
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
					for (SearchSpaceNode child : this.expand(extracted, flaw)) {
						// enqueue node
						this.strategy.enqueue(child);
						this.logger.debug("Expanding the search space with:\n- node= " + child + "\n");
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
