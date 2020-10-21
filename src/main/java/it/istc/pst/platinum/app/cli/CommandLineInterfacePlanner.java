package it.istc.pst.platinum.app.cli;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.solver.PseudoControllabilityAwareSolver;
import it.istc.pst.platinum.deliberative.strategy.GreedyDepthSearchStrategy;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;



/**
 * 
 * @author alessandroumbrico
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
public class CommandLineInterfacePlanner extends Planner {
	
	
	/**
	 * 
	 */
	protected CommandLineInterfacePlanner() {
		super();
	}

}
