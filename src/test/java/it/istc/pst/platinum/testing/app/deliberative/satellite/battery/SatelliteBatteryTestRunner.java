package it.istc.pst.platinum.testing.app.deliberative.satellite.battery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
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
	private static final int NUMBER_OF_RUNS = 3;
	
	protected static final String[] DOMAIN_VERSIONS = new String[] {
//			"complete",
//			"reservoir",
			"discrete",
			"simple"
	};
	
	private static final String[] NUMBER_OF_COMM_WINDOWS = new String[] {
			"1", 
//			"2", 
//			"3",
	};
	
	private static final String[] NUMBER_OF_SUN_WINDOWS = new String[] {
			"1", 
//			"2", 
//			"3", 
	};
	
	private static final String[] NUMBER_OF_SCIENCE_OPERATIONS = new String[] {
			"1", 
			"2", 
//			"3", 
//			"4", 
//			"5", 
//			"6", 
//			"7", 
//			"8", 
//			"9", 
//			"10"
	};
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			for (int v = 0; v < DOMAIN_VERSIONS.length; v++)
			{
				// data file path
				String dataPath = DATA_FOLDER + "/" + DOMAIN_VERSIONS[v] + "_data.csv";
				// create writer
				try (PrintWriter dataWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataPath)))) 
				{	
					// print CSV header
					dataWriter.println("version;#comm;#sun;#science;time;makespan;");
					dataWriter.flush();
					for (int i = 0; i < NUMBER_OF_COMM_WINDOWS.length; i++) 
					{
						for (int j = 0; j < NUMBER_OF_SUN_WINDOWS.length; j++) 
						{
							for (int k = 0; k < NUMBER_OF_SCIENCE_OPERATIONS.length; k++) 
							{
								try
								{
									// check domain version 1
									SatelliteBatteryProblemGeneratorDomain generator = new SatelliteBatteryProblemGeneratorDomain(DOMAIN_VERSIONS[v]);
									String path = generator.generateProblemFile(
											Integer.parseInt(NUMBER_OF_COMM_WINDOWS[i]), 
											Integer.parseInt(NUMBER_OF_SUN_WINDOWS[j]), 
											Integer.parseInt(NUMBER_OF_SCIENCE_OPERATIONS[k]));
									// print generated file
									System.out.println("(Generated) Problem specification: " + path + "\n");
									
									int counter = 0;
									while (counter < NUMBER_OF_RUNS) 
									{
										// create file in append mode
										try //(PrintWriter dataWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataPath, true)))) 
										{
											// get domain file
											String ddl = DOMAIN_DIRECTORY + "/" + DDL_PATTERN.replace("$v", DOMAIN_VERSIONS[v]);
											// get problem file
											String pdl = PROBLEM_DIRECTORY + "/" + PDL_PATTERN
													.replace("$v", DOMAIN_VERSIONS[v])
													.replace("$c", NUMBER_OF_COMM_WINDOWS[i])
													.replace("$s", NUMBER_OF_SUN_WINDOWS[j])
													.replace("$g", NUMBER_OF_SCIENCE_OPERATIONS[k]);
											
											System.out.println("----------------------------------------------------------------------\n"
													+ "Solving problem \"" + pdl + "\"\n");
											
											
											
											// create the plan database
											PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(ddl, pdl);
											// create planner
											Planner planner = PlannerBuilder.createAndSet(pdb);
											
											// start planning
											SolutionPlan plan = planner.plan();
											System.out.println("Generated plan:\n" + plan + "\n"
													+ "----------------------------------------------------------------------\n");
											
											// get solution data 
											double time = plan.getSolvingTime();
											double makespan = plan.getMakespan();
											
											// create resulting plan file
											File planFile = new File(PLAN_FOLDER + "/" + DOMAIN_VERSIONS[v] + "_plan"
													+ "_" + NUMBER_OF_COMM_WINDOWS[i] + ""
													+ "_" + NUMBER_OF_SUN_WINDOWS[j] + ""
													+ "_" + NUMBER_OF_SCIENCE_OPERATIONS[k] + ".txt");
											
											// create file writer
											try (PrintWriter planWriter = new PrintWriter(planFile)) 
											{
												// export plan
												planWriter.println(plan.export());
												// print data to file
												planWriter.flush();
											}

											
											// print record to data file
											dataWriter.println(DOMAIN_VERSIONS[v] + ";" 
													+ NUMBER_OF_COMM_WINDOWS[i] + ";"
													+ NUMBER_OF_SUN_WINDOWS[j] + ";"
													+ NUMBER_OF_SCIENCE_OPERATIONS[k] + ";"
													+ time + ";"
													+ makespan + ";");

											// print data to file
											dataWriter.flush();
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
										
										
										// update 
										counter++;
										// call JVM garbage and wait a bit before starting next test
										System.gc();
										Thread.sleep(1000);
									}
								}
								catch (Exception ex) {
									System.err.println(ex.getMessage());
									ex.printStackTrace(System.err);
								}
							}
						}
					}
				}
			}
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}
