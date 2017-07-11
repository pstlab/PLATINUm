package it.istc.pst.platinum.testing.app.executive.satellite;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.est.EarliestStartTimeExecutive;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
class SatelliteExecutorTest 
{
	private static final String DDL = "domains/satellite/satellite.ddl";
	private static final String PDL = "domains/satellite/satellite.pdl";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			// build the planner
			Planner planner = PlannerBuilder.build(EPSL2SatellitePlanner.class.getName(), DDL, PDL);
			// start deliberative process 
			SolutionPlan plan = planner.plan();
			System.out.println();
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			System.out.println(plan);
			System.out.println();
			
			// create executor
			Executive executor = new EarliestStartTimeExecutive();
			executor.initialize(plan);
			// start executing the plan
			executor.execute();
		}
		catch (SynchronizationCycleException  | ProblemInitializationException | NoSolutionFoundException ex) {
			System.err.println(ex.getMessage());
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
