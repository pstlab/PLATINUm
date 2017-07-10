package it.uniroma3.epsl2.testing.app.executive.satellite;

import it.uniroma3.epsl2.deliberative.app.Planner;
import it.uniroma3.epsl2.deliberative.app.PlannerBuilder;
import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.est.EarliestStartTimeExecutive;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.ProblemInitializationException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;

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
