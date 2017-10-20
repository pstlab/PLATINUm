package it.istc.pst.platinum.testing.app.deliberative.satellite.battery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class SatelliteDeliberativeApplicationBatteryTestRunner
{
	private static final String PLAN_FOLDER = "domains/satellite/battery/plans";
	private static final String DDL = "domains/satellite/battery/domains/satellite_$h.ddl";
	private static final String PDL = "domains/satellite/battery/problems/satellite_$c_$s_$g.pdl";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		try 
		{
			// get run parameters
			String horizon = args[0];
			String numOfCommWindows = args[1];
			String numOfSunWindows = args[2];
			String numOfScience = args[3];
			// get file path where to append result data
			String dataFile = args[4];
					
			// create file in append mode
			try (PrintWriter dataWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) 
			{
					// get domain file
					String domain = DDL.replace("$h", horizon);
					// get problem file
					String problem = PDL.replace("$c", numOfCommWindows)
							.replace("$s", numOfSunWindows)
							.replace("$g", numOfScience);
					
					// create planner
					Planner planner = PlannerBuilder.build(domain, problem);	
					// start planning
					SolutionPlan plan = planner.plan();
					System.out.println(plan);
					
					// get solution data 
					double time = plan.getSolvingTime();
					double makespan = plan.getMakespan();
					
					// create resulting plan file
					File planFile = new File(PLAN_FOLDER + "/cfg1_plan"
							+ "_" + numOfCommWindows + ""
							+ "_" + numOfSunWindows + ""
							+ "_" + numOfScience + ".txt");
					
					// create file writer
					try (PrintWriter planWriter = new PrintWriter(planFile)) 
					{
						// export plan
						planWriter.println(plan.export());
						// print data to file
						planWriter.flush();
					}

				
				// print record to data file
				dataWriter.println(horizon + ";" 
						+ numOfCommWindows + ";"
						+ numOfSunWindows + ";"
						+ numOfScience + ";"
						+ time + ";"
						+ makespan + ";");

				// print data to file
				dataWriter.flush();
			}
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
