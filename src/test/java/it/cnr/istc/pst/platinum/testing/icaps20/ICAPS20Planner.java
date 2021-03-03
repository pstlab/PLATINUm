package it.cnr.istc.pst.platinum.testing.icaps20;

import it.cnr.istc.pst.platinum.ai.deliberative.Planner;
import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;
import it.cnr.istc.pst.platinum.ai.deliberative.solver.PseudoControllabilityAwareSolver;
import it.cnr.istc.pst.platinum.ai.deliberative.strategy.MakespanGreedyDepthSearchStrategy;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;


/**
 * 
 * @author alessandroumbrico
 *
 */
@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = ICAPS20PlanningExperiments.TIMEOUT
)
@FlawSelectionHeuristicsConfiguration(
	heuristics = PipelineFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
	strategy = MakespanGreedyDepthSearchStrategy.class
)
@FrameworkLoggerConfiguration(
	level = FrameworkLoggingLevel.OFF
)
public class ICAPS20Planner extends Planner 
{
	/**
	 * 
	 */
	protected ICAPS20Planner() {
		super();
	}
}	