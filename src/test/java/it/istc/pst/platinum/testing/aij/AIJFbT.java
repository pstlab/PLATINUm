package it.istc.pst.platinum.testing.aij;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.heuristic.RandomFlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.solver.PseudoControllabilityAwareSolver;
import it.istc.pst.platinum.deliberative.strategy.DepthFirstSearchStrategy;
import it.istc.pst.platinum.deliberative.strategy.MakespanGreedyDepthSearchStrategy;
import it.istc.pst.platinum.deliberative.strategy.hrc.WorkloadBalancingSearchStrategy;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public abstract class AIJFbT 
{
	// timeout
	protected static final long TIMEOUT = 180000;		// timeout set to 3 minutes
	protected static String DOMAIN_FOLDER = "domains/AIJ_EXP_FbT";
	// temporal horizon 
	protected static int HORIZON = 500;
	// data folder
	protected static String DATA_FOLDER = "data/AIJ_EXP_FbT";
	protected static String PLAN_FOLDER = DATA_FOLDER + "/plans";
	
		
	// number of run for each experiment
	protected static int EXPERIMETN_RUNS = 3;
	
	// planner configurations
	protected static Class[] CONFIGURATIONS = {
		AIJFbTPlannerA.class,
		AIJFbTPlannerB.class,
		AIJFbTPlannerC.class,
		AIJFbTPlannerD.class
	};
	
	// number of tasks composing the assembly process
	protected static int[] TASKS = {		
		10,
		15,
		20,
		25,
		30
	};
	
	// number of shared tasks composing the assembly process
	protected static int[] SHARED = {
		20,
		40,
		60,
		80,
		100
	};
	
	// amount of uncertainty about human task execution
	protected static int[] UNCERTAINTY = {
		10,
		20,
		30
	};
	
	
	protected static String PLATFORM_CONFIGURATION_FOLDER = "etc/platform/AIJ_EXP_FbT";
	protected static int[] PLATOFROM_EXECUTION_UNCERTAINTY = new int[] {
		5,	
		10,
		15,
		20,
		25,
		30
	};
	
	
	// Planning and execution test
	
	// timeout
	protected static final long ACTING_DELIBERATIVE_TIMEOUT = 180000;		// timeout set to 60 seconds
	// folder of simulator configurations
	protected static String PLATFORM_CFG_FOLDER = "etc/platform/AIJ_EXP_FbT";
	// number of tasks composing the assembly process
	protected static int ACTING_DELIBERATIVE_TASKS = 10;	// 10, 15, 20, 25, 30
	// number of shared tasks composing the assembly process
	protected static int ACTING_DELIBERATIVE_SHARED = 60; // 20, 40, 60, 80, 100
	// simulator uncertainty
	protected static int[] ACTING_PLATFORM_UNCERTAINTY = new int[] {
			5, 10, 15, 20, 25, 30
	};
	
	// amount of uncertainty about human task execution
	protected static int[] ACTING_DELIBERATIVE_UNCERTAINTY = {
		30, 20, 10
	};
}


//PLANNER CONFIGURATION A


@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = AIJFbTPlannerExperimentManager.TIMEOUT
)
@FlawSelectionHeuristicsConfiguration(
	heuristics = PipelineFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
	strategy = WorkloadBalancingSearchStrategy.class
)
@FrameworkLoggerConfiguration(
	level = FrameworkLoggingLevel.OFF
)
class AIJFbTPlannerA extends Planner {
	
	protected AIJFbTPlannerA() {
		super();
	}
}
	
	
//PLANNER CONFIGURATION B

@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = AIJFbTPlannerExperimentManager.TIMEOUT
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
class AIJFbTPlannerB extends Planner {
	
	protected AIJFbTPlannerB() {
		super();
	}
}
	
	
//PLANNER CONFIGURATION C

@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = AIJFbTPlannerExperimentManager.TIMEOUT
)
@FlawSelectionHeuristicsConfiguration(
	heuristics = PipelineFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
	strategy = DepthFirstSearchStrategy.class
)
@FrameworkLoggerConfiguration(
	level = FrameworkLoggingLevel.OFF
)
class AIJFbTPlannerC extends Planner {
	
	protected AIJFbTPlannerC() {
		super();
	}
}


//PLANNER CONFIGURATION D

@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = AIJFbTPlannerExperimentManager.TIMEOUT
)
@FlawSelectionHeuristicsConfiguration(
	heuristics = RandomFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
	strategy = DepthFirstSearchStrategy.class
)
@FrameworkLoggerConfiguration(
	level = FrameworkLoggingLevel.OFF
)
class AIJFbTPlannerD extends Planner {
	
	protected AIJFbTPlannerD() {
		super();
	}
}
