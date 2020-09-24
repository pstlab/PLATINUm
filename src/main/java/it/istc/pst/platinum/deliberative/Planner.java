package it.istc.pst.platinum.deliberative;

import it.istc.pst.platinum.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.solver.PseudoControllabilityAwareSolver;
import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.solver.Solver;
import it.istc.pst.platinum.deliberative.strategy.GreedyDepthSearchStrategy;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.DeliberativeObject;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PlannerSolverPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.PlanControllabilityType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@PlannerSolverConfiguration(
		solver = PseudoControllabilityAwareSolver.class,
		timeout = 180000												// set solving timeout to 60 seconds
)
@FlawSelectionHeuristicsConfiguration(
		heuristics = PipelineFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
		strategy = GreedyDepthSearchStrategy.class
)
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.INFO
)
public class Planner extends DeliberativeObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	@PlannerSolverPlaceholder
	protected Solver solver;
	
	protected SearchSpaceNode currentSolution;			// current solution found
	
	/**
	 * 
	 */
	protected Planner() {
		super();
		this.currentSolution = null;
	}
	
	/**
	 * Display the current plan
	 */
	public void display() {
		// display the current plan
		this.pdb.display();
	}
	
	/**
	 * The method starts the planning process and return the solution plan if any.
	 * 
	 * If no solution plan is found the method throws an exception
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public SolutionPlan plan() 
			throws NoSolutionFoundException 
	{
		// get time 
		long start = System.currentTimeMillis();
		// find a solution to the planning problem
		this.currentSolution = this.solver.solve();
		
		// extract solution plan
		SolutionPlan plan = this.pdb.getSolutionPlan();
		plan.setControllability(PlanControllabilityType.PSEUDO_CONTROLLABILITY);
		
		// check solving time 
		long time = System.currentTimeMillis() - start;
		plan.setSolvingTime(time);
		// get the solution plan
		return plan;
	}
	
	/**
	 * The method returns a structure representing the current plan. 
	 * 
	 * @return
	 */
	public SolutionPlan getCurrentPlan() {
		// get current plan
		return this.pdb.getSolutionPlan();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		// get a description of the plan data base
		return this.pdb.toString();
	}
}
