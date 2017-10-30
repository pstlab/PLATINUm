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
public class SatelliteBatteryTestRunner extends SatelliteBatteryTest
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		try 
		{
			// get run parameters
			String domainVersion = args[0];
			String numOfCommWindows = args[1];
			String numOfSunWindows = args[2];
			String numOfScience = args[3];
			String cfg = args[4];
			// get file path where to append result data
			String dataFile = args[5];
					
			// create file in append mode
			try (PrintWriter dataWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) 
			{
					// get domain file
					String domain = DOMAIN_DIRECTORY + "/" + DDL_PATTERN.replace("$v", domainVersion);
					// get problem file
					String problem = PROBLEM_DIRECTORY + "/" + PDL_PATTERN
							.replace("$v", domainVersion)
							.replace("$c", numOfCommWindows)
							.replace("$s", numOfSunWindows)
							.replace("$g", numOfScience);
					
					System.out.println("----------------------------------------------------------------------\n"
							+ "Solving problem \"" + problem + "\"\n");
					
					// create planner
					Planner planner = PlannerBuilder.build(domain, problem);
					// start planning
					SolutionPlan plan = planner.plan();
					System.out.println("Generated plan:\n" + plan + "\n"
							+ "----------------------------------------------------------------------\n");
					
					// get solution data 
					double time = plan.getSolvingTime();
					double makespan = plan.getMakespan();
					
					// create resulting plan file
					File planFile = new File(PLAN_FOLDER + "/" + domainVersion + "_" + cfg + "_plan"
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
				dataWriter.println(domainVersion + ";" 
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
			System.out.println("No solution found:\n" + ex.getMessage() + "\n"
					+ "----------------------------------------------------------------------\n");
		}
		catch (ProblemInitializationException ex) {
			System.out.println("Error:\n" + ex.getMessage() + "\n"
					+ "----------------------------------------------------------------------\n");
			ex.printStackTrace(System.out);
		}
		catch (Exception ex) {
			System.out.println("Error:\n" + ex.getMessage() + "\n"
					+ "----------------------------------------------------------------------\n");
			ex.printStackTrace(System.out);
		}
	}
}
