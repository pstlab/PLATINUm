package it.istc.pst.platinum.deliberative.app;

import it.istc.pst.platinum.deliberative.solver.Solver;
import it.istc.pst.platinum.deliberative.solver.SolverType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.SolverModule;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public final class DefaultPlanner extends Planner 
{
	@SolverModule(solver= SolverType.PSEUDO_CONTROLLABILITY_AWARE, timeout= 300000)
	protected Solver solver;
	
	/**
	 * 
	 */
	@FrameworkLoggerConfiguration(level = FrameworkLoggingLevel.OFF)
	protected DefaultPlanner() {
		super();
	}
	
	/**
	 * The method starts the planning process and return the solution plan if any.
	 * 
	 * If no solution plan is found the method throws an exception
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	@Override
	public SolutionPlan plan() 
			throws NoSolutionFoundException {
		// solve the problem and get the plan
		SolutionPlan plan = this.solver.solve();
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
