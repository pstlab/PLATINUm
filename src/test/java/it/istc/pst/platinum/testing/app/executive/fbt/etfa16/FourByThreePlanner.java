package it.istc.pst.platinum.testing.app.executive.fbt.etfa16;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
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
public class FourByThreePlanner extends Planner 
{
	private static final String DDL_FILE = "domains/fourbythree/beta/fourbythree.ddl";
	private static final String PDL_FILE = "domains/fourbythree/beta/fourbythree.pdl";
	
	@SolverModule(solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE)
	protected Solver solver;
	
	/**
	 * 
	 */
	@FrameworkLoggerConfiguration(level= FrameworkLoggingLevel.OFF)
	protected FourByThreePlanner() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public SolutionPlan plan() throws NoSolutionFoundException {
		return this.solver.solve();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			// create the planner
			FourByThreePlanner planner = PlannerBuilder.
					build(FourByThreePlanner.class.getName(), DDL_FILE, PDL_FILE);
			
			System.out.println("*******************************************************************");
			System.out.println("Test: [ddl= " + DDL_FILE + ", pdl= " + PDL_FILE + "]");
			System.out.println("Start planning... ");
			try {
				
				// start planning
				SolutionPlan plan = planner.plan();
				System.out.println("... solution found after " + plan.getSolvingTime() + " msecs");
				System.out.println();
				// print final plan
				System.out.println(plan);
				System.out.println();
			}
			catch (NoSolutionFoundException ex) {
				System.out.println("... no solutions have been found");
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}