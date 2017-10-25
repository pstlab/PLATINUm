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
public class SatelliteBatteryProblemGeneratorDomainV1 
{
	private static final int VERSION = 1;
	private static final String[] SUN_WINDOW_SPECIFICATIONS = new String[] {
			
			// 1 sun window available
			"\tsun1 <fact> SunWindow.sun.Visible() AT [0, 0] [1000, 1000] [1000, 1000];\n",
			
			
			// 2 sun windows available
			"\tsun0 <fact> SunWindow.sun.NotVisible() AT [0, 0] [200, 200] [200, 200];\n"
			+ "\tsun1 <fact> SunWindow.sun.Visible() AT [200, 200] [400, 400] [200, 200];\n"
			+ "\tsun2 <fact> SunWindow.sun.NotVisible() AT [400, 400] [600, 600] [200, 200];\n"
			+ "\tsun3 <fact> SunWindow.sun.Visible() AT [600, 600] [800, 800] [200, 200];\n"
			+ "\tsun4 <fact> SunWindow.sun.NotVisible() AT [800, 800] [1000, 1000] [200, 200];\n",
			
			// 3 sun windows available
			"\tsun0 <fact> SunWindow.sun.NotVisible() AT [0, 0] [100, 100] [100, 100];\n"
			+ "\tsun1 <fact> SunWindow.sun.Visible() AT [100, 100] [350, 350] [250, 250];\n"
			+ "\tsun2 <fact> SunWindow.sun.NotVisible() AT [350, 350] [500, 500] [150, 150];\n"
			+ "\tsun3 <fact> SunWindow.sun.Visible() AT [500, 500] [700, 700] [200, 200];\n"
			+ "\tsun4 <fact> SunWindow.sun.NotVisible() AT [700, 700] [750, 750] [50, 50];\n"
			+ "\tsun5 <fact> SunWindow.sun.Visible() AT [750, 750] [950, 950] [200, 200];\n"
			+ "\tsun6 <fact> SunWindow.sun.NotVisible() AT [950, 950] [1000, 1000] [50, 50];\n"
	};
	
	private static final String[] COMM_WINDOW_SPECIFICATIONS = new String[] {
			
			// 1 communication window available
			"\twin1 <fact> GroundStationWindow.channel.Visible() AT [0, 0] [1000, 1000] [1000, 1000];\n",
			
			// 2 communication windows available
			"\twin0 <fact> GroundStationWindow.channel.NotVisible() AT [0, 0] [200, 200] [200, 200];\n"
			+ "\twin1 <fact> GroundStationWindow.channel.Visible() AT [200, 200] [500, 500] [300, 300];\n"
			+ "\twin2 <fact> GroundStationWindow.channel.NotVisible() AT [500, 500] [650, 650] [150, 150];\n"
			+ "\twin3 <fact> GroundStationWindow.channel.Visible() AT [650, 650] [800, 800] [150, 150];\n"
			+ "\twin4 <fact> GroundStationWindow.channel.NotVisible() AT [800, 800] [1000, 1000] [200, 200];\n",
	
			// 3 communication windows available
			"\twin0 <fact> GroundStationWindow.channel.NotVisible() AT [0, 0] [100, 100] [100, 100];\n"
			+ "\twin1 <fact> GroundStationWindow.channel.Visible() AT [100, 100] [300, 300] [200, 200];\n"
			+ "\twin2 <fact> GroundStationWindow.channel.NotVisible() AT [300, 300] [400, 400] [100, 100];\n"
			+ "\twin3 <fact> GroundStationWindow.channel.Visible() AT [400, 400] [650, 650] [250, 250];\n"
			+ "\twin4 <fact> GroundStationWindow.channel.NotVisible() AT [650, 650] [700, 700] [50, 50];\n"
			+ "\twin5 <fact> GroundStationWindow.channel.Visible() AT [700, 700] [900, 900] [200, 200];\n"
			+ "\twin6 <fact> GroundStationWindow.channel.NotVisible() AT [900, 900] [1000, 1000] [100, 100];\n"
	};
	
	private static final String[] GOAL_SPECIFICATIONS = new String[] {
			
			// 1 science operation
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 2 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 3 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 4 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 5 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 6 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 7 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			
			// 8 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 9 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg8 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n",
			
			// 10 science operations
			"\tg0 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg1 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg2 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg3 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg4 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg5 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg6 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg7 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg8 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
			+ "\tg9 <goal> PointingMode.pm.Science() AT [0, +INF] [0, +INF] [0, +INF];\n"
	};
	
	private static final String FACTS = "\tf0 <fact> PointingMode.pm.Earth() AT [0, 0] [1, +INF] [1, +INF];\n";
	
	private String directory;			// problem file directory
	
	/**
	 * 
	 * @param directory
	 */
	protected SatelliteBatteryProblemGeneratorDomainV1(String directory) {
		this.directory = directory;
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
		String path = this.directory + "/satellite_" + numberOfCommWindows + "_" + numberOfSunWindos + "_" + numberOfGoals + ".pdl";
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) 
		{
			// print problem statement
			writer.println("PROBLEM BATTERY_RESERVOIR_SATELLITE_"
					+ "" + numberOfGoals + "G_"
					+ "" + numberOfSunWindos + "S_"
					+ "" + numberOfCommWindows + "W "
					+ "(DOMAIN BATTERY_RESERVOIR_SATELLITE_V" + VERSION+ ") {\n");
			
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
