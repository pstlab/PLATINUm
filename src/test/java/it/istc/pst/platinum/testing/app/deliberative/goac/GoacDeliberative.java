package it.istc.pst.platinum.testing.app.deliberative.goac;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.deliberative.solver.Solver;
import it.istc.pst.platinum.deliberative.solver.SolverType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.SolverModule;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class GoacDeliberative extends Planner
{
	private static final String DDL = "domains/goac/goac.ddl";
	private static final String PDL = "domains/goac/goac-2g-1wind.pdl";
	
	@SolverModule(solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE)
	protected Solver solver;
	
	/**
	 * 
	 */
	@FrameworkLoggerConfiguration(level = FrameworkLoggingLevel.DEBUG)
	protected GoacDeliberative() {
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
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		try 
		{
			Planner planner = PlannerBuilder.build(GoacDeliberative.class.getName(), DDL, PDL);	
			// start planning
			SolutionPlan plan = planner.plan();
			// solution found
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			// print the resulting plan
			System.out.println(plan);
			
			// display the resulting plant
			planner.display();
		}
		catch (NoSolutionFoundException ex) {
			// no solution found
			System.err.println(ex.getMessage());
		}
		catch (ProblemInitializationException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}
