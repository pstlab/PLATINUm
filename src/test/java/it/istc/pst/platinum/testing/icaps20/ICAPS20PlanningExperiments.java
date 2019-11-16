package it.istc.pst.platinum.testing.icaps20;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class ICAPS20PlanningExperiments 
{
	// domain folder
	protected static String DOMAIN_FOLDER = "domains/satellite/icaps20/domains";
	// problem folder
	protected static String PROBLEM_FOLDER = "domains/satellite/icaps20/problems";
	
	
	// data folder
	protected static String DATA_FOLDER = "data/icaps20";
	protected static String PLAN_FOLDER = DATA_FOLDER + "/plans";
	
		
	// planning timeout
	protected static final int TIMEOUT = 60000;
	
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
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// create data file
			File dataFile = new File(DATA_FOLDER + "/exp_planning_runs_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)))) {
				// write file
				writer.println("H;U;W;I;G;time (msecs)");
			}
			
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
								// prepare domain name
								String domainName = "rsa_h" + horizon + "_u" + uncertainty + "_i" + instrument;
								// prepare problem name
								String problemName = "rsa_h" + horizon + "_u" + uncertainty + "_i" + instrument + "_w" + window + "_g" + goal;
								// prepare DDL file path
								String DDL = DOMAIN_FOLDER + "/" + domainName + ".ddl";
								// prepare PDL file path
								String PDL = PROBLEM_FOLDER + "/" + problemName + ".pdl";
								
								// planning time 
								long time = -1;
								try
								{
									// build the plan database
									PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(
											DDL, 
											PDL);
									
									// set a planning instance of the plan database
									Planner planner = PlannerBuilder.createAndSet(
											ICAPS20Planner.class, 
											pdb);
									
									// start planning
									System.out.println("Run planner on problem \"" + problemName + "\" ... ");
									SolutionPlan plan = planner.plan();
									
									
									// get plan solving time
									time = plan.getSolvingTime();
									
									// print solving information
									System.out.println("... problem \"" + problemName + "\" solved after " + time + " msecs\nSolution plan:\n" + plan + "\n"
											+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
									
									// print resulting plan 
									File planFile = new File(PLAN_FOLDER + "/" + problemName + "_plan.txt");
									try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(planFile)))) {
										// print the resulting plan 
										writer.println(plan.export());
										writer.println();
										writer.print(plan);
									}
									
									
									// wait some seconds before starting next run
									Thread.sleep(5000);
									
								}
								catch (Exception ex) 
								{
									// error while solving planning instance
									System.out.println("... error while solving problem \"" + problemName + "\" \n- message: " + ex.getMessage() +"\n"
											+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
									// set solving time
									time = -1;
								}
								finally 
								{
									// check solving time
									if (time > 0) 
									{
										// append result to the data file
										try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
											// add data entry
											writer.println(horizon + ";" + uncertainty + ";" + window + ";" + instrument + ";" + goal + ";" + time);
										}
									}
									else 
									{
										// append result to the data file
										try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
											// add data entry
											writer.println(horizon + ";" + uncertainty + ";" + window + ";" + instrument + ";" + goal + ";no-pseudo-plan");
										}
										
									}
									

									// try to clear memory
									System.gc();
								}
							}
						}
					}
				}
			}
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}


