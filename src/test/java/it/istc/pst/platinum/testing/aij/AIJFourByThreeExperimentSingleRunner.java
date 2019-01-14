package it.istc.pst.platinum.testing.aij;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.solver.PseudoControllabilityAwareSolver;
import it.istc.pst.platinum.deliberative.strategy.fbt.HRCBalancingSearchStrategy;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.FlawSelectionHeuristicsConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.PlannerSolverConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.SearchStrategyConfiguration;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class AIJFourByThreeExperimentSingleRunner 
{
	// timeout
	public static final long TIMEOUT = 60000;		// timeout set to 60 seconds
	// domain file folder
	private static String DOMAIN_FOLDER = "domains/AIJ_EXP_FbT";
	// number of tasks composing the assembly process
	private static int TASKS = 20;	// 10, 15, 20, 25, 30
	// number of shared tasks composing the assembly process
	private static int SHARED = 100; // 20, 40, 60, 80, 100
	// amount of uncertainty about human task execution
	private static int UNCERTAINTY = 20; // 10, 20, 30
		
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// prepare domain name
		String domainName = "AIJ_EXP_T" + TASKS  + "_S" + SHARED + "_U" + UNCERTAINTY;
		// prepare DDL file path
		String DDL = DOMAIN_FOLDER + "/" + domainName + ".ddl";
		// prepare PDL file path
		String PDL = DOMAIN_FOLDER + "/" + domainName + ".pdl";
		try
		{
			System.out.println("Run experiemnt on domain: " + domainName + " ... ");
			// build the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(DDL, PDL);
			// initialize a planning instance of the plan database
			Planner planner = PlannerBuilder.createAndSet(AIJFbTSinglePlanner.class, pdb);

			// start planning
			SolutionPlan plan = planner.plan();
			// print solving information
			System.out.println("... problem solved after " + plan.getSolvingTime() + " msecs\nSolution plan:\n" + plan + "\n"
					+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
			// print the resulting plan 
			System.out.println(plan.export());
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
			System.out.print(plan);
			// display the planner
			planner.display();
		}
		catch (Exception ex) {
			// error while solving planning instance
			System.out.println("... error while solving domain instance: " + domainName + "\n- message: " + ex.getMessage() +"\n"
					+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		}
	}
}



@PlannerSolverConfiguration(
	solver = PseudoControllabilityAwareSolver.class,
	timeout = AIJFourByThreeExperimentSingleRunner.TIMEOUT
)
@FlawSelectionHeuristicsConfiguration(
	heuristics = PipelineFlawSelectionHeuristic.class
)
@SearchStrategyConfiguration(
	strategy = HRCBalancingSearchStrategy.class
)
@FrameworkLoggerConfiguration(
	level = FrameworkLoggingLevel.OFF
)
class AIJFbTSinglePlanner extends Planner {
	
	protected AIJFbTSinglePlanner() {
		super();
	}
}
