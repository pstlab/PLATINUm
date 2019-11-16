package it.istc.pst.platinum.testing.icaps20;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class ICAPS20PlanningProblemGenerator 
{
	// problem folder
	protected static String PROBLEM_FOLDER = "domains/satellite/icaps20/problems";
		
	// planning horizons
	protected static int[] HORIZON = {		
		100,
		200,
		300,
	};
	
	// amount of uncertainty
	protected static int[] UNCERTAINTY = {
		10,
		20,
		30
	};
	
	// available communication windows
	protected static int[] WINDOWS = {
		1,
		2,
		3,
	};
	
	// available instruments
	protected static int[] INSTRUMENTS = {
		1,
		2,
		3,
	};
	
	
	// number of scientific operations
	protected static int[] GOALS = {
		1,
		2,
		3,
		4,
		5,
	};
	
	/**
	 * 
	 */
	public void generate()
	{
		// run experiments
		for (int horizon : HORIZON)
		{
			for (int uncertainty : UNCERTAINTY) 
			{
				for (int window : WINDOWS) 
				{
					for (int instrument : INSTRUMENTS) 
					{
						for (int goal : GOALS)
						{
							try
							{
								// prepare problem name
								String problemName = "rsa_h" + horizon + "_u" + uncertainty + "_i" + instrument + "_w" + window + "_g" + goal;
								
								// prepare domain specification file
								String pdl = "PROBLEM RSA_H" + horizon + "_W" + window + "_G" + goal +" (DOMAIN RSA_H" + horizon +"_U" + uncertainty +"_I" + instrument + ") {\n\n";
								
								// set orbit facts
								if (horizon == 100) 
								{
									// write facts
									pdl += "\t\tf11 <fact> Orbit.orbit.PERI() AT [0, 0] [30, 30] [30, 30];\n"
											+ "\t\tf12 <fact> Orbit.orbit.INT() AT [30, 30] [70, 70] [40, 40];\n"
											+ "\t\tf13 <fact> Orbit.orbit.APO() AT [70, 70] [100, 100] [30, 30];\n\n";
									
									// check visibility windows
									if (window == 1) {
										// writ facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [35, 40] [35, 40];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [35, 40] [60, 65] [20, 30];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [60, 65] [100, 100] [35, 40];\n\n";
									}
									else if (window == 2) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [35, 40] [35, 40];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [35, 40] [50, 55] [10, 20];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [50, 55] [50, 55] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [50, 55] [65, 75] [10, 25];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [65, 75] [100, 100] [25, 35];\n\n";
									}
									else if (window == 3) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [30, 35] [30, 35];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [30, 35] [40, 45] [10, 15];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [40, 45] [40, 45] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [40, 45] [50, 55] [10, 15];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [50, 55] [50, 55] [1, 5];\n" + 
												"\t\tf26 <fact> GroundStation.visibility.Visible() AT [50, 55] [60, 75] [10, 15];\n" + 
												"\t\tf27 <fact> GroundStation.visibility.NotVisible() AT [60, 75] [100, 100] [25, 40];\n\n";
									}
									
									
									// set RSA fact
									pdl += "\t\tf31 <fact> RSA.operations.Idle() AT [0, 0] [0, 100] [1, 100];\n\n";
									
									// set pointing mode fact
									pdl += "\t\tf41 <fact> PointingMode.pointing.Earth() AT [0, 0] [0, 100] [1, 100];\n\n";
									
									// set instruments
									if (instrument == 1) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument.inst.Off() AT [0, 0] [0, 100] [1, 100];\n\n";
									}
									else if (instrument == 2) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 100] [1, 100];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 100] [1, 100];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 100] [1, 100];\n\n"; 
									}
									else if (instrument == 3) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 100] [1, 100];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 100] [1, 100];\n" + 
												"\t\tf53 <fact> Instrument3.inst3.Off() AT [0, 0] [0, 100] [1, 100];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 100] [1, 100];\n\n";
									}
									
									// set goals 
									if (goal == 1) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n\n";
									}
									else if (goal == 2) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n\n";
									}
									else if (goal == 3) {
										// write goals 
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n\n";
									}
									else if (goal == 4) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n\n";
									}
									else if (goal == 5) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" +
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n" +
												"\t\tg5 <goal> RSA.operations.Scien() AT [0, 100] [0, 100] [1, 100];\n\n";
									}
								}
								else if (horizon == 200) 
								{
									// write facts
									pdl += "\t\tf11 <fact> Orbit.orbit.PERI() AT [0, 0] [60, 60] [60, 60];\n"
											+ "\t\tf12 <fact> Orbit.orbit.INT() AT [60, 60] [140, 140] [80, 80];\n"
											+ "\t\tf13 <fact> Orbit.orbit.APO() AT [140, 140] [200, 200] [60, 60];\n\n";
									
									
									// check visibility windows
									if (window == 1) {
										// writ facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [60, 65] [60, 65];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [60, 65] [130, 135] [70, 75];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [130, 135] [200, 200] [65, 70];\n\n";
									}
									else if (window == 2) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [60, 65] [60, 65];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [60, 65] [90, 95] [25, 35];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [90, 95] [90, 95] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [90, 95] [130, 140] [35, 50];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [130, 140] [200, 200] [60, 70];\n\n";
									}
									else if (window == 3) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [60, 65] [60, 65];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [60, 65] [85, 90] [15, 30];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [85, 90] [85, 90] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [85, 90] [110, 115] [20, 30];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [110, 115] [110, 115] [1, 5];\n" + 
												"\t\tf26 <fact> GroundStation.visibility.Visible() AT [110, 115] [130, 140] [15, 30];\n" + 
												"\t\tf27 <fact> GroundStation.visibility.NotVisible() AT [130, 140] [200, 200] [60, 70];\n\n";
									}
									
									
									// set RSA fact
									pdl += "\t\tf31 <fact> RSA.operations.Idle() AT [0, 0] [0, 200] [1, 200];\n\n";
									
									// set pointing mode fact
									pdl += "\t\tf41 <fact> PointingMode.pointing.Earth() AT [0, 0] [0, 200] [1, 200];\n\n";
									
									// set instruments
									if (instrument == 1) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument.inst.Off() AT [0, 0] [0, 200] [1, 200];\n\n";
									}
									else if (instrument == 2) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 200] [1, 200];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 200] [1, 200];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 200] [1, 200];\n\n"; 
									}
									else if (instrument == 3) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 200] [1, 200];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 200] [1, 200];\n" + 
												"\t\tf53 <fact> Instrument3.inst3.Off() AT [0, 0] [0, 200] [1, 200];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 200] [1, 200];\n\n";
									}
									
									// set goals 
									if (goal == 1) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n\n";
									}
									else if (goal == 2) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n\n";
									}
									else if (goal == 3) {
										// write goals 
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n\n";
									}
									else if (goal == 4) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n\n";
									}
									else if (goal == 5) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" +
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n" +
												"\t\tg5 <goal> RSA.operations.Scien() AT [0, 200] [0, 200] [1, 200];\n\n";
									}
								}
								else if (horizon == 300) 
								{
									// write facts
									pdl += "\t\tf11 <fact> Orbit.orbit.PERI() AT [0, 0] [90, 90] [90, 90];\n"
											+ "\t\tf12 <fact> Orbit.orbit.INT() AT [90, 90] [210, 210] [120, 120];\n"
											+ "\t\tf13 <fact> Orbit.orbit.APO() AT [210, 210] [300, 300] [90, 90];\n\n";
									
									
									// check visibility windows
									if (window == 1) {
										// writ facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [90, 95] [90, 95];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [90, 95] [205, 215] [110, 125];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [205, 215] [300, 300] [85, 95];\n\n";
									}
									else if (window == 2) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [90, 95] [90, 95];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [90, 95] [130, 145] [35, 55];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [130, 145] [130, 145] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [130, 145] [200, 205] [55, 75];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [200, 205] [300, 300] [95, 100];\n\n";
									}
									else if (window == 3) {
										// write facts
										pdl += "\t\tf21 <fact> GroundStation.visibility.NotVisible() AT [0, 0] [90, 95] [90, 95];\n" + 
												"\t\tf22 <fact> GroundStation.visibility.Visible() AT [90, 95] [130, 135] [35, 45];\n" + 
												"\t\tf23 <fact> GroundStation.visibility.NotVisible() AT [130, 135] [130, 135] [1, 5];\n" + 
												"\t\tf24 <fact> GroundStation.visibility.Visible() AT [130, 135] [155, 160] [20, 30];\n" + 
												"\t\tf25 <fact> GroundStation.visibility.NotVisible() AT [155, 160] [155, 160] [1, 5];\n" + 
												"\t\tf26 <fact> GroundStation.visibility.Visible() AT [155, 160] [200, 210] [40, 55];\n" + 
												"\t\tf27 <fact> GroundStation.visibility.NotVisible() AT [200, 210] [300, 300] [90, 100];\n\n";
									}
									
									
									// set RSA fact
									pdl += "\t\tf31 <fact> RSA.operations.Idle() AT [0, 0] [0, 300] [1, 300];\n\n";
									
									// set pointing mode fact
									pdl += "\t\tf41 <fact> PointingMode.pointing.Earth() AT [0, 0] [0, 300] [1, 300];\n\n";
									
									// set instruments
									if (instrument == 1) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument.inst.Off() AT [0, 0] [0, 300] [1, 300];\n\n";
									}
									else if (instrument == 2) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 300] [1, 300];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 300] [1, 300];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 300] [1, 300];\n\n"; 
									}
									else if (instrument == 3) {
										// write facts
										pdl += "\t\tf51 <fact> Instrument1.inst1.Off() AT [0, 0] [0, 300] [1, 300];\n" + 
												"\t\tf52 <fact> Instrument2.inst2.Off() AT [0, 0] [0, 300] [1, 300];\n" + 
												"\t\tf53 <fact> Instrument3.inst3.Off() AT [0, 0] [0, 300] [1, 300];\n\n";
										
										// write fact
										pdl += "\t\tf61 <fact> InstLock.locking.None() AT [0, 0] [0, 300] [1, 300];\n\n";
									}
									
									// set goals 
									if (goal == 1) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n\n";
									}
									else if (goal == 2) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n\n";
									}
									else if (goal == 3) {
										// write goals 
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n\n";
									}
									else if (goal == 4) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n\n";
									}
									else if (goal == 5) {
										// write goals
										pdl += "\t\tg1 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg2 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" + 
												"\t\tg3 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" +
												"\t\tg4 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n" +
												"\t\tg5 <goal> RSA.operations.Scien() AT [0, 300] [0, 300] [1, 300];\n\n";
									}
								}
								
								
								// close problem specification 
								pdl += "}\n\n";
								

								
								// prepare domain specification file
								File pdlFile = new File(PROBLEM_FOLDER + "/" + problemName + ".pdl");
								try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
									// write file 
									writer.write(pdl);
								}
							}
							catch (IOException ex) {
								System.err.println(ex.getMessage());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// create generatore
		ICAPS20PlanningProblemGenerator generator = new ICAPS20PlanningProblemGenerator();
		// generate
		generator.generate();
	}
}

