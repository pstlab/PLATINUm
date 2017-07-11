package it.uniroma3.epsl2.testing.app.executive.satellite;

import it.uniroma3.epsl2.deliberative.app.Planner;
import it.uniroma3.epsl2.deliberative.solver.Solver;
import it.uniroma3.epsl2.deliberative.solver.SolverType;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.SolverModule;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class EPSL2SatellitePlanner extends Planner
{
	@SolverModule(solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE)
	protected Solver solver;
	
	/**
	 * 
	 */
	@FrameworkLoggerConfiguration(level = FrameworkLoggingLevel.OFF)
	protected EPSL2SatellitePlanner() {
		super();
	}
	
	/**
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
	
}
