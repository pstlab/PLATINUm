package it.istc.pst.platinum.testing.app.deliberative.satellite.battery;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author anacleto
 *
 */
public class SatelliteBatteryTestManager extends SatelliteBatteryTest
{
	private static final int NUMBER_OF_RUNS = 3;
	
	private static final String[] CONFIGURATIONS = new String[] {
		"cfg1",			// strategy= DFS, heuristics= S&B
//		"cfg2",			// strategy= GREEDY, heuristics= S&B
//		"cfg3",			// strategy= A*, heuristics= S&B
	};
	
	private static final String[] DOMAIN_VERSIONS = new String[] {
//			"1",
			"2"
	};
	private static final String[] NUMBER_OF_SUN_WINDOWS = new String[] {
			"1", 
//			"2", 
//			"3", 
	};
	private static final String[] NUMBER_OF_COMM_WINDOWS = new String[] {
			"1", 
//			"2", 
//			"3",
	};
	private static final String[] NUMBER_OF_SCIENCE_OPERATIONS = new String[] {
			"1", 
			"2", 
			"3", 
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
			for (String cfg : CONFIGURATIONS)
			{
				// data file path
				String dataPath = DATA_FOLDER + "/" + cfg + "_data.csv"; 
				// create writer
				try (PrintWriter dataWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataPath)))) 
				{
					// print CSV header
					dataWriter.println("horizon;#comm;#sun;#science;time;makespan;");
					dataWriter.flush();
					for (int v = 0; v < DOMAIN_VERSIONS.length; v++)
					{
						for (int i = 0; i < NUMBER_OF_COMM_WINDOWS.length; i++) 
						{
							for (int j = 0; j < NUMBER_OF_SUN_WINDOWS.length; j++) 
							{
								for (int k = 0; k < NUMBER_OF_SCIENCE_OPERATIONS.length; k++) 
								{
									try
									{
										// check domain version 1
										if (DOMAIN_VERSIONS[v].equals("1")) 
										{
											SatelliteBatteryProblemGeneratorDomainV1 generator = new SatelliteBatteryProblemGeneratorDomainV1(PROBLEM_DIRECTORY);
											String path = generator.generateProblemFile(
													Integer.parseInt(NUMBER_OF_COMM_WINDOWS[i]), 
													Integer.parseInt(NUMBER_OF_SUN_WINDOWS[j]), 
													Integer.parseInt(NUMBER_OF_SCIENCE_OPERATIONS[k]));
											// print generated file
											System.out.println("(Generated) Problem specification: " + path + "\n");
										}
										// check domain version 2
										else if (DOMAIN_VERSIONS[v].equals("2")) 
										{
											SatelliteBatteryProblemGeneratorDomainV2 generator = new SatelliteBatteryProblemGeneratorDomainV2(PROBLEM_DIRECTORY);
											String path = generator.generateProblemFile(
													Integer.parseInt(NUMBER_OF_COMM_WINDOWS[i]), 
													Integer.parseInt(NUMBER_OF_SUN_WINDOWS[j]), 
													Integer.parseInt(NUMBER_OF_SCIENCE_OPERATIONS[k]));
											// print generated file
											System.out.println("(Generated) Problem specification: " + path + "\n");
										}
										else {
											// unknown version
											throw new RuntimeException("Unknown domain version: " + DOMAIN_VERSIONS[v]);
										}
										
										int counter = 0;
										while (counter < NUMBER_OF_RUNS) 
										{
											// prepare command
											String cmd = "java -jar src/test/resources/satellite_" + cfg + "_runner.jar "
													+ DOMAIN_VERSIONS[v] + " "
													+ NUMBER_OF_COMM_WINDOWS[i] + " "
													+ NUMBER_OF_SUN_WINDOWS[j] + " "
													+ NUMBER_OF_SCIENCE_OPERATIONS[k] + " "
													+ cfg + " "
													+ dataPath;
											
											System.out.println("... running command: \"" + cmd + "\"");
											// run planner on a separate process
											Process proc = Runtime.getRuntime().exec(cmd);
											// wait process execution
											proc.waitFor();
											// update 
											counter++;
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
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}
