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
public class SatelliteBatteryProblemGeneratorDomain extends SatelliteBatteryTest 
{
	private static final String[] SUN_WINDOW_SPECIFICATIONS = new String[] {
			
			// 1 sun window available
			"\tsun1 <fact> SunWindow.visibility.Visible() AT [0, 0] [500, 500] [500, 500];\n",
			
			// 2 sun windows available
			"\tsun0 <fact> SunWindow.visibility.NotVisible() AT [0, 0] [50, 50] [50, 50];\n"
			+ "\tsun1 <fact> SunWindow.visibility.Visible() AT [50, 50] [250, 250] [200, 200];\n"
			+ "\tsun2 <fact> SunWindow.visibility.NotVisible() AT [250, 250] [300, 300] [50, 50];\n"
			+ "\tsun3 <fact> SunWindow.visibility.Visible() AT [300, 300] [450, 450] [150, 150];\n"
			+ "\tsun4 <fact> SunWindow.visibility.NotVisible() AT [450, 450] [500, 500] [50, 50];\n",
			
			// 3 sun windows available
			"\tsun0 <fact> SunWindow.visibility.NotVisible() AT [0, 0] [50, 50] [50, 50];\n"
			+ "\tsun1 <fact> SunWindow.visibility.Visible() AT [50, 50] [150, 150] [100, 100];\n"
			+ "\tsun2 <fact> SunWindow.visibility.NotVisible() AT [150, 150] [200, 200] [50, 50];\n"
			+ "\tsun3 <fact> SunWindow.visibility.Visible() AT [200, 200] [300, 300] [100, 100];\n"
			+ "\tsun4 <fact> SunWindow.visibility.NotVisible() AT [300, 300] [350, 350] [50, 50];\n"
			+ "\tsun5 <fact> SunWindow.visibility.Visible() AT [350, 350] [450, 450] [100, 100];\n"
			+ "\tsun6 <fact> SunWindow.visibility.NotVisible() AT [450, 450] [500, 500] [50, 50];\n"
	};
	
	private static final String[] COMM_WINDOW_SPECIFICATIONS = new String[] {
			
			// 1 communication window available
			"\twin1 <fact> GroundStationWindow.channel.Visible() AT [0, 0] [500, 500] [500, 500];\n",
			
			// 2 communication windows available
			
			"\twin0 <fact> GroundStationWindow.channel.NotVisible() AT [0, 0] [10, 10] [10, 10];\n"
			+ "\twin1 <fact> GroundStationWindow.channel.Visible() AT [10, 10] [160, 160] [150, 150];\n"
			+ "\twin2 <fact> GroundStationWindow.channel.NotVisible() AT [160, 160] [170, 170] [10, 10];\n"
			+ "\twin3 <fact> GroundStationWindow.channel.Visible() AT [170, 170] [320, 320] [150, 150];\n"
			+ "\twin4 <fact> GroundStationWindow.channel.NotVisible() AT [320, 320] [330, 330] [10, 10];\n"
			+ "\twin5 <fact> GroundStationWindow.channel.Visible() AT [330, 330] [490, 490] [160, 160];\n"
			+ "\twin6 <fact> GroundStationWindow.channel.NotVisible() AT [490, 490] [500, 500] [10, 10];\n",
	
			// 3 communication windows available
			"\twin0 <fact> GroundStationWindow.channel.NotVisible() AT [0, 0] [10, 10] [10, 10];\n"
			+ "\twin1 <fact> GroundStationWindow.channel.Visible() AT [10, 10] [160, 160] [150, 150];\n"
			+ "\twin2 <fact> GroundStationWindow.channel.NotVisible() AT [160, 160] [200, 200] [40, 40];\n"
			+ "\twin3 <fact> GroundStationWindow.channel.Visible() AT [200, 200] [300, 300] [100, 100];\n"
			+ "\twin4 <fact> GroundStationWindow.channel.NotVisible() AT [300, 300] [310, 310] [10, 10];\n"
			+ "\twin5 <fact> GroundStationWindow.channel.Visible() AT [310, 310] [460, 460] [150, 150];\n"
			+ "\twin6 <fact> GroundStationWindow.channel.NotVisible() AT [460, 460] [500, 500] [40, 40];\n"
	};
	
	private static final String[] GOAL_SPECIFICATIONS = new String[] {
			
			// 1 science operation
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 2 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 3 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 4 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 5 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 6 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 7 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			
			// 8 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 9 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg8 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 10 science operations
			"\tg0 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg8 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg9 <goal> Satellite.operations.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
	};
	
	private static final String FACTS = "\tf0 <fact> PointingMode.orientation.Earth() AT [0, 0] [1, +INF] [1, +INF];\n";
	
	private String domain;				// domain version
	
	/**
	 * 
	 * @param domain
	 */
	protected SatelliteBatteryProblemGeneratorDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * 
	 * @param numberOfCommWindows
	 * @param numberOfSunWindos
	 * @param numberOfGoals
	 * @return
	 * @throws IOException
	 */
	public String generateProblemFile(int numberOfCommWindows, int numberOfSunWindos, int numberOfGoals) 
			throws IOException
	{
		// file path
		String path = PROBLEM_DIRECTORY + "/satellite_" + this.domain + "_" + numberOfCommWindows + "_" + numberOfSunWindos + "_" + numberOfGoals + ".pdl";
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) 
		{
			// print problem statement
			writer.println("PROBLEM BATTERY_SATELLITE_" + this.domain.toUpperCase() + "_"
					+ "" + numberOfGoals + "G_"
					+ "" + numberOfSunWindos + "S_"
					+ "" + numberOfCommWindows + "W "
					+ "(DOMAIN BATTERY_SATELLITE_" + this.domain.toUpperCase() + ") {\n");
			
			// print known initial facts
			writer.println(FACTS);
			
			// print available communication windows
			writer.println(COMM_WINDOW_SPECIFICATIONS[numberOfCommWindows - 1]);
			
			// print available sun windows
			writer.println(SUN_WINDOW_SPECIFICATIONS[numberOfSunWindos - 1]);
			
			// print goal specification
			writer.println(GOAL_SPECIFICATIONS[numberOfGoals - 1]);
			
			// close problem specification
			writer.println("\n}\n");
			// write file
			writer.flush();
		}
		
		// get generated file path
		return path;
	}
}
