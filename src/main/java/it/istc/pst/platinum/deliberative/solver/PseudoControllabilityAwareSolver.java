package it.istc.pst.platinum.deliberative.solver;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.deliberative.heuristic.FlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.heuristic.FlawSelectionHeuristicType;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategy;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategyType;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.domain.component.PlanElementStatus;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.FlawSelectionHeuristicModule;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.SearchStrategyModule;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.PlanRefinementException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.plan.PlanControllabilityType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityAwareSolver extends Solver 
{
	@SearchStrategyModule(strategy= SearchStrategyType.ASTAR)
	private SearchStrategy fringe;
	
	@FlawSelectionHeuristicModule(heuristics= FlawSelectionHeuristicType.SEARCH_AND_BUILD)
	private FlawSelectionHeuristic heuristic;
	
	/**
	 * 
	 */
	protected PseudoControllabilityAwareSolver() {
		super(SolverType.PSEUDO_CONTROLLABILITY_AWARE.getLabel());
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// create the root node
		SearchSpaceNode root = this.createSearchSpaceNode();
		// enqueue the root node
		this.fringe.enqueue(root);
	}
	
	/**
	 * 
	 */
	@Override
	public SolutionPlan solve() 
			throws NoSolutionFoundException 
	{
		// set solving start time
		long start = System.currentTimeMillis();
		// initialize solving step counter
		this.stepCounter = 0;

		// initialize the solution plan
		SolutionPlan plan = null;
		// last extracted node
		SearchSpaceNode last = null, node = null;
		// search condition
		boolean search = true;
		// search a solution
		while (search) 
		{
			try 
			{
				// update step counter
				this.stepCounter++;
				// extract a node from the fringe
				node = this.fringe.dequeue();
				this.logger.info("Solving step: " + this.stepCounter +"\n"
						+ "- Extracted node: " + node + "\n"
						+ "- Applied operator: " + node.getGenerator() + "\n");
				
				// propagate extracted node
				this.contextSwitch(last, node);
				// updated last propagated node
				last = node;
				// check consistency of the resulting partial plan
				this.pdb.check();
				
				// print information concerning current partial plan	
				this.logger.info("Partial plan after propagation of operator: "  + node.getGenerator() + "\n"
							+ "- plan:\n"
							+ "---- decisions= " + this.pdb.getPlan().getDecisions() + "\n"
							+ "---- relations= " + this.pdb.getPlan().getRelations() + "\n\n"
							+ "- pending plan (agenda):\n"
							+ "---- decisions= " + this.pdb.getPlan(PlanElementStatus.PENDING).getDecisions() + "\n"
							+ "---- relations= " + this.pdb.getPlan(PlanElementStatus.PENDING).getRelations() + "\n\n"
							+ "- silent plan:\n"
							+ "---- decisions= " + this.pdb.getPlan(PlanElementStatus.SILENT).getDecisions() + "\n"
							+ "---- relations= " + this.pdb.getPlan(PlanElementStatus.SILENT).getRelations() + "\n\n");
				
 				// choose the best flaws to solve
				List<Flaw> flaws = new ArrayList<>(this.heuristic.choose());
				// create a branch for each "equivalent" flaw to solve next
				for (Flaw flaw : flaws)
				{
					// expand the search space with the available solutions of the flaw
					for (SearchSpaceNode child : this.expand(last, flaw)) {
						// add the node to the fringe
						this.fringe.enqueue(child);
						// expand the search space
						this.logger.info("Search tree expansion:\n- node: " + child + "\n"
								+ "- generator: " + child.getGenerator() + "\n");
					}
				}
			}
			catch (PlanRefinementException ex) {
				// error while refining the current plan
				this.logger.warning("Error while refining the current plan\n"
						+ "- operator: " + node.getGenerator() + "\n"
						+ "- message: " + ex.getMessage() + "\n");
			}
			catch (UnsolvableFlawFoundException | ConsistencyCheckException  ex) {
				// not feasible partial plan
				this.logger.warning("Not feasible partial plan found\n"
						+ "- oeprator: " + node.getGenerator() + "\n"
						+ "- plan:\n"
							+ "---- decisions= " + this.pdb.getPlan().getDecisions() + "\n"
							+ "---- relations= " + this.pdb.getPlan().getRelations() + "\n"
						+ "- agenda:\n"
							+ "---- decisions= " + this.pdb.getPlan(PlanElementStatus.PENDING).getDecisions() + "\n"
							+ "---- relations= " + this.pdb.getPlan(PlanElementStatus.PENDING).getRelations() + "\n\n");
			}
			catch (NoFlawFoundException ex)
			{
				// solution found stop search
				search = false;
				// set solving time
				this.time = System.currentTimeMillis() - start;
				// set plan
				plan = this.pdb.getSolutionPlan();
				plan.setControllability(PlanControllabilityType.PSEUDO_CONTROLLABLE);
				plan.setSolvingTime(this.time);
				// pseudo-controllable solution found
				this.logger.info("Pseudo-controllable solution found after " + (this.time / 1000) + " (secs) and " + this.stepCounter + " solving steps\n");
			}
			catch (EmptyFringeException ex) 
			{
				// no solution found stop search
				search = false;
				// set solving time
				this.time = System.currentTimeMillis() - start;
				// backtrack from the last propagated node
				this.backtrack(last);
				// throw exception
				throw new NoSolutionFoundException("No pseudo-controllable solution found after " + (this.time / 1000) + " (secs) and " + this.stepCounter + " solving steps\n");
			}
			
		} // end while
		
		// get solution plan
		return plan;
	}
}	
