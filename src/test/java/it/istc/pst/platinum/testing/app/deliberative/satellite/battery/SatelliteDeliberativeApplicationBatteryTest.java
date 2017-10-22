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
public class SatelliteDeliberativeApplicationBatteryTest
{
	private static final int NUMBER_OF_RUNS = 3;
	
	private static final String[] CONFIGURATIONS = new String[] {
		"cfg1",			// strategy= DFS, heuristics= S&B
		"cfg2",			// strategy= GREEDY, heuristics= S&B
		"cfg3",			// strategy= A*, heuristics= S&B
		"cfg4",			// strategy= DFS, heuristics= HFF
		"cfg5",			// strategy= GREEDY, heuristics= HFF
		"cfg6"			// strategy= A*, heuristics= HFF
	};
	
	private static final String[] HORIZONS = new String[] {
			"1000"
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
			"4", 
			"5", 
			"6", 
			"7", 
			"8", 
			"9", 
			"10"
	};
	private static final String DATA_FOLDER = "domains/satellite/battery/data";
	
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
					
					for (int h = 0; h < HORIZONS.length; h++)
					{
						for (int i = 0; i < NUMBER_OF_COMM_WINDOWS.length; i++) 
						{
							for (int j = 0; j < NUMBER_OF_SUN_WINDOWS.length; j++) 
							{
								for (int k = 0; k < NUMBER_OF_SCIENCE_OPERATIONS.length; k++) 
								{
									try
									{
										int counter = 0;
										while (counter < NUMBER_OF_RUNS) 
										{
											// prepare command
											String cmd = "java -jar src/test/resources/satellite_" + cfg + "_runner.jar "
													+ HORIZONS[h] + " "
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
