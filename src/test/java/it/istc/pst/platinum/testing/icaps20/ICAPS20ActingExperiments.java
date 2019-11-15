package it.istc.pst.platinum.testing.icaps20;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.control.acting.GoalOrientedActingAgent;
import it.istc.pst.platinum.control.lang.AgentTaskDescription;
import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.TokenDescription;
import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;

/**
 * 
 * @author anacleto
 *
 */
public class ICAPS20ActingExperiments 
{
	// platform simulator class
	protected static Class<PlatformSimulator> PLATFORM_SIMULATOR_CLASS = PlatformSimulator.class;
	// platform simulator configuration folder
	protected static String PLATFORM_SIMULATOR_CONFIG_FOLDER = "etc/platform/icaps20";
	
	// domain folder
	protected static String DOMAIN_FOLDER = "domains/satellite/icaps20/domains";
	// problem folder
	protected static String PROBLEM_FOLDER = "domains/satellite/icaps20/problems";
	
	
	// data folder
	protected static String DATA_FOLDER = "data/icaps20";
	protected static String PLAN_FOLDER = DATA_FOLDER + "/plans";
	
		
	// planning timeout
	protected static final int TIMEOUT = 60000;
	
	// number of run for each experiment
	protected static int EXPERIMETN_RUNS = 3;
	
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
	 * @param horizon
	 * @param uncdertainty
	 * @param window
	 * @param instrument
	 * @param goal
	 * @return
	 */
	private static AgentTaskDescription createTaskDescription(int horizon, int uncdertainty, int window, int instrument, int goal) 
	{
		// create task description
		AgentTaskDescription description = new AgentTaskDescription();
		description.addFactDescription(new TokenDescription(
				"Production", 
				"Idle", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 500}, 
				new long[] {0, 500}));
		
		description.addFactDescription(new TokenDescription(
				"Human", 
				"Idle", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 500}, 
				new long[] {0, 500}));
		
		description.addFactDescription(new TokenDescription(
				"Robot", 
				"Idle", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 500}, 
				new long[] {0, 500}));
		
		description.addFactDescription(new TokenDescription(
				"Arm", 
				"SetOnBase", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 500}, 
				new long[] {0, 500}));
		
		description.addFactDescription(new TokenDescription(
				"Tool", 
				"Idle", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 500}, 
				new long[] {0, 500}));
		
		
		description.addGoalDescription(new TokenDescription(
				"Production", 
				"Assembly"));
		
		// get task description
		return description;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// create data file
			File dataFile = new File(DATA_FOLDER + "/exp_acting_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)))) {
				// write file header
				writer.println(
						"Horizon;"
						+ "Uncertainty;"
						+ "Windows;"
						+ "Instruments;"
						+ "Planning Goals;"
						+ "Planning Attempts;"
						+ "Planning Time (secs);"
						+ "Execution Attempts;"
						+ "Execution Time (secs);"
						+ "Exogenous Events;"
						+ "Contingency Handling Time (secs);"
						+ "Goal Status");
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
							for (int g : GOALS)
							{
								// prepare domain name
								String domainName = "rsa_h" + horizon + "_u" + uncertainty + "_i" + instrument;
								// prepare problem name
								String problemName = "rsa_h" + horizon + "_u" + uncertainty + "_i" + instrument + "_w" + window + "_g" + g;
								// prepare DDL file path
								String DDL = DOMAIN_FOLDER + "/" + domainName + ".ddl";
								
								
								System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								System.out.println("+++ Acting Test - Problem: \"" + problemName +  "\""
										+ "- Horizon: " + horizon + ""
										+ "- Uncertainty: " + uncertainty + " ++++");
								System.out.println();
								

								
								
								// set acting timeout according to the planning horizon and the planning timeout
								long actingTimeout = TIMEOUT + (horizon * 1000);
								
								
								// create a parallel thread for the acting agent
								Thread p = new Thread(new Runnable() {
									
									/**
									 * 
									 */
									public void run() 
									{
										// acting agent
										GoalOrientedActingAgent agent = null;
										// platform simulator
										PlatformSimulator simulator = null;
										// list of goals managed by the agent (only one expected)
										List<Goal> goals = new ArrayList<>();
										try
										{
											// set goal-oriented agent
											agent = new GoalOrientedActingAgent();
											System.out.println("Starting agent...");
											agent.start();
											
											// configuration loader
											simulator = PlatformProxyBuilder.build(
													PLATFORM_SIMULATOR_CLASS,
													PLATFORM_SIMULATOR_CONFIG_FOLDER + "/config_h" + horizon + "_u" + uncertainty + ".xml");
											
											// start simulator
											simulator.start();
											
											// set the agent
											agent.initialize(
													simulator,
													DDL);
						
											// create task description 
											AgentTaskDescription task = createTaskDescription(horizon, uncertainty, window, instrument, g);
											
											// buffer task description
											agent.buffer(task);
											
											// get managed goals
											goals = agent.getResults();
										}
										catch (Exception ex) {
											System.err.println(ex.getMessage());
										}
										finally 
										{
											try
											{
												// check goals 
												if (goals.isEmpty()) 
												{
													// append result to the data file
													try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
														// add data entry
														writer.println(
																horizon+ ";"
																+ uncertainty + ";"
																+ window + ";"
																+ instrument + ";"
																+ g + ";"
																+ "execution error;"
																+ "execution error;"
																+ "execution error;"
																+ "execution error;"
																+ "execution error;"
																+ "execution error;"
																+ "execution error");
													}
												}
												else 
												{
													for (Goal goal : goals) 
													{
														System.out.println("Completed goal " + goal +":\n"
																+ "\t- Planning: " + goal.getPlanningAttempts() + " sessions, total time: " + (goal.getTotalPlanningTime() / 1000) +" seconds\n"
																+ "\t- Execution: " + goal.getExecutionAttempts() + " sessions, total time: " + (goal.getTotalExecutionTime() / 1000) + " seconds\n"
																+ "\t- Contingency handling: " + goal.getContingencyHandlingAttempts() + " sessions, total time: " + (goal.getTotalContingencyHandlingTime() / 1000 ) + " seconds\n\n");
														
														// append result to the data file
														try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
															// add data entry
															writer.println(
																	horizon+ ";"
																	+ uncertainty + ";"
																	+ window + ";"
																	+ instrument + ";"
																	+ g + ";"
																	+ goal.getPlanningAttempts() + ";"
																	+ (goal.getTotalPlanningTime() / 1000) + ";"
																	+ goal.getExecutionAttempts() + ";"
																	+ (goal.getTotalExecutionTime() / 1000) + ";"
																	+ goal.getContingencyHandlingAttempts() + ";"
																	+ (goal.getTotalContingencyHandlingTime() / 1000) + ";"
																	+ goal.getStatus());
														}
													}
												}
												
												// terminate agent
												if (agent != null) 
												{
													// stop agent
													System.out.println("Terminating agent...");
													agent.stop();
													
													
												}
												
												// terminate simulator
												if (simulator != null) {
													// stop simulator
													System.out.println("Terminating simulator...");
													simulator.stop();
												}
												
												System.out.println(".... finish!");
												System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
												
											}
											catch (Exception ex) {
												System.err.println(ex.getMessage());
											}
										}
									}
								});
								
								// start acting agent
								p.start();
								// wait until the timeout
								p.join(actingTimeout);
								
								// check if alive
								if (p.isAlive()) {
									p.interrupt();
								}
								
								// force cache clear calling garbage collector
								System.gc();
							}
						}
					}
				}
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
			
			
			
