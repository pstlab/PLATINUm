package it.cnr.istc.pst.platinum.ai.deliberative;

import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.HierarchicalFlawSelectionHeuristic;
import it.cnr.istc.pst.platinum.ai.deliberative.solver.PseudoControllabilityAwareSolver;
import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;
import it.cnr.istc.pst.platinum.ai.deliberative.solver.Solver;
import it.cnr.istc.pst.platinum.ai.deliberative.strategy.DepthFirstSearchStrategy;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.deliberative.PlannerSolverPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.PlanControllabilityType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author alessandro
 *
 */
@PlannerSolverConfiguration(
		solver = PseudoControllabilityAwareSolver.class
)
@FlawSelectionHeuristicsConfiguration(
		heuristics = HierarchicalFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
		strategy = DepthFirstSearchStrategy.class
)
@FrameworkLoggerConfiguration(		
		// set logging level
		level = FrameworkLoggingLevel.INFO
)
public class Planner extends FrameworkObject {
	
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
			throws NoSolutionFoundException {
		
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
	public SearchSpaceNode getCurrentNode() {
		return this.currentSolution;
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

	/**
	 * 
	 */
	public void clear() {
		// clear solver
		this.solver.clear();
		// clear current solution
		this.currentSolution = null;
	}
}
