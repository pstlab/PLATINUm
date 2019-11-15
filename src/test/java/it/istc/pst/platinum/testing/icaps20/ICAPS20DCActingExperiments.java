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
public class ICAPS20DCActingExperiments 
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
	 * @param window
	 * @param instrument
	 * @param goal
	 * @return
	 */
	private static AgentTaskDescription createTaskDescription(int horizon, int window, int instrument, int goal) 
	{
		// create task description
		AgentTaskDescription description = new AgentTaskDescription();
		
		// add orbit related facts
		if (horizon == 100) 
		{
			// add fact 
			description.addFactDescription(new TokenDescription(
					"Orbit", 
					"PERI", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {30, 30}, 
					new long[] {30, 30}));
			
			// add fact 
			description.addFactDescription(new TokenDescription(
					"Orbit", 
					"INT", 
					new String[] {}, 
					new long[] {30, 30}, 
					new long[] {70, 70}, 
					new long[] {40, 40}));
			
			// add fact 
			description.addFactDescription(new TokenDescription(
					"Orbit", 
					"APO", 
					new String[] {}, 
					new long[] {70, 70}, 
					new long[] {100, 100}, 
					new long[] {30, 30}));
		}
		
		
		// add ground station related facts
		if (window == 1) {
			// add fact 
			description.addFactDescription(new TokenDescription(
					"GroundStation", 
					"NotVisible", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {20, 20}, 
					new long[] {20, 20}));
			
			// add fact 
			description.addFactDescription(new TokenDescription(
					"GroundStation", 
					"Visible", 
					new String[] {}, 
					new long[] {20, 20}, 
					new long[] {80, 80}, 
					new long[] {60, 60}));
			
			// add fact 
			description.addFactDescription(new TokenDescription(
					"GroundStation", 
					"NotVisible", 
					new String[] {}, 
					new long[] {80, 80}, 
					new long[] {100, 100}, 
					new long[] {20, 20}));
		}
		
		
		// add pointing fact
		description.addFactDescription(new TokenDescription(
				"PointingMode", 
				"Earth", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 100}, 
				new long[] {1, 100}));
		
		
		// add instrument related facts
		if (instrument == 1) 
		{
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
		}
		else if (instrument == 2)
		{
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument1", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
			
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument2", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
			
			// add fact
			description.addFactDescription(new TokenDescription(
					"InstLock", 
					"None", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
		}
		else if (instrument == 3)
		{
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument1", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
			
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument2", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
			
			// add fact
			description.addFactDescription(new TokenDescription(
					"Instrument3", 
					"Off", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
			
			// add fact
			description.addFactDescription(new TokenDescription(
					"InstLock", 
					"None", 
					new String[] {}, 
					new long[] {0, 0}, 
					new long[] {0, 100}, 
					new long[] {1, 100}));
		}

		
		// add goals
		for (int i = 0; i < goal; i++) {
			// add goal 
			description.addGoalDescription(new TokenDescription(
					"RSA", 
					"Scien"));
		}
		
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
											agent = new GoalOrientedActingAgent(
													ICAPS20Planner.class,
													ICAPS20DCExecutive.class);
											
											System.out.println("Starting agent... ");
											agent.start();
											
											System.out.println("Starting platform simulator... ");
											// configuration loader
											simulator = PlatformProxyBuilder.build(
													PLATFORM_SIMULATOR_CLASS,
													PLATFORM_SIMULATOR_CONFIG_FOLDER + "/config_h" + horizon + "_u" + uncertainty + ".xml");
											
											// start simulator
											simulator.start();
											
											System.out.println("Initialize agent... ");
											// set the agent
											agent.initialize(
													simulator,
													DDL);
						
											// create task description 
											AgentTaskDescription task = createTaskDescription(horizon, window, instrument, g);
											
											System.out.println("Enter task... ");
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
			
			
			
