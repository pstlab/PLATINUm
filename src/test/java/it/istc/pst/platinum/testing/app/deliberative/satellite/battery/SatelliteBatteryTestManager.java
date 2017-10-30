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
		"cfg1",			// strategy= DFS, heuristics= S&B,
	};
	
	private static final String[] NUMBER_OF_SUN_WINDOWS = new String[] {
			"1", 
			"2", 
			"3", 
	};
	private static final String[] NUMBER_OF_COMM_WINDOWS = new String[] {
			"1", 
			"2", 
			"3",
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
				for (int v = 0; v < SatelliteBatteryTest.DOMAIN_VERSIONS.length; v++)
				{
					// data file path
					String dataPath = DATA_FOLDER + "/" + SatelliteBatteryTest.DOMAIN_VERSIONS[v] + "_" + cfg + "_data.csv";
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
										SatelliteBatteryProblemGeneratorDomain generator = new SatelliteBatteryProblemGeneratorDomain(SatelliteBatteryTest.DOMAIN_VERSIONS[v]);
										String path = generator.generateProblemFile(
												Integer.parseInt(NUMBER_OF_COMM_WINDOWS[i]), 
												Integer.parseInt(NUMBER_OF_SUN_WINDOWS[j]), 
												Integer.parseInt(NUMBER_OF_SCIENCE_OPERATIONS[k]));
										// print generated file
										System.out.println("(Generated) Problem specification: " + path + "\n");
										
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
