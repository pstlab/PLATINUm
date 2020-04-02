package it.istc.pst.platinum.testing.aij;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import it.istc.pst.platinum.control.acting.GoalOrientedActingAgent;
import it.istc.pst.platinum.control.lang.AgentTaskDescription;
import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.TokenDescription;
import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;

/**
 * 
 * @author anacleto
 *
 */
public class AIJFbTActingExperimentManager extends AIJFbT 
{
	private static final Class<PlatformSimulator> HRC_PLATFORM_SIMULATOR_CLASS = PlatformSimulator.class;
	
	/**
	 * 
	 * @return
	 */
	private static AgentTaskDescription createTaskDescription() 
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
			// start agent
			
			// create data file
			File dataFile = new File(DATA_FOLDER + "/acting_runs_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)))) {
				// write file header
				writer.println("Number of Tasks;"
						+ "Percentage of Shared Tasks;"
						+ "Planning Model Uncertainty;"
						+ "Platform Simulator Uncertainty;"
						+ "Number of Planning Attempts;"
						+ "Total Planning Time (secs);"
						+ "Number of Execution Attempts;"
						+ "Total Execution Time (secs);"
						+ "Number of Exogenous Events;"
						+ "Total Contingency Handling Time (secs);"
						+ "Goal Status");
			}
			
			// deliberative model uncertainty
			for (int i = 0; i < ACTING_DELIBERATIVE_UNCERTAINTY.length; i++) 
			{
				// deliberative uncertainty
				int modelUncertainty = ACTING_DELIBERATIVE_UNCERTAINTY[i];
				// simulator uncertainty
				for (int j = 0; j < ACTING_PLATFORM_UNCERTAINTY.length; j++)
				{
					// simulator uncertainty
					int platformUncertainty = ACTING_PLATFORM_UNCERTAINTY[j];
					System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					System.out.println("+++ Acting Test - Tasks: " + ACTING_DELIBERATIVE_TASKS +  ""
							+ " - Shared: " + ACTING_DELIBERATIVE_SHARED + ""
							+ "- ModelUncertainty: " + modelUncertainty + ""
							+ "- PlatformUncertainty: " + platformUncertainty + " ++++");
					System.out.println();
					
					// create goal-oriented agent
					GoalOrientedActingAgent agent = new GoalOrientedActingAgent();
					agent.start();
					System.out.println("Starting agent...");
					
					// configuration loader
					PlatformSimulator simulator = PlatformProxyBuilder.build(
							HRC_PLATFORM_SIMULATOR_CLASS,
							PLATFORM_CFG_FOLDER + "/AIJ_EXP_PLATFORM_CONFIG_U" + platformUncertainty + ".xml");
					// start simulator
					simulator.start();
					
					try
					{
						// initialize the agent
						agent.initialize(
								simulator,
								DOMAIN_FOLDER + "/AIJ_EXP_T" + ACTING_DELIBERATIVE_TASKS + "_S" + ACTING_DELIBERATIVE_SHARED + "_U" + modelUncertainty  +".ddl");
	
						// create task description 
						AgentTaskDescription task = createTaskDescription();
						// buffer task description
						agent.buffer(task);
						
						// get managed goals
						List<Goal> goals = agent.getResults();
						for (Goal goal : goals) 
						{
							System.out.println("Completed goal " + goal +":\n"
									+ "\t- Planning: " + goal.getPlanningAttempts() + " sessions, total time: " + (goal.getTotalPlanningTime() / 1000) +" seconds\n"
									+ "\t- Execution: " + goal.getExecutionAttempts() + " sessions, total time: " + (goal.getTotalExecutionTime() / 1000) + " seconds\n"
									+ "\t- Contingency handling: " + goal.getContingencyHandlingAttempts() + " sessions, total time: " + (goal.getTotalContingencyHandlingTime() / 1000 ) + " seconds\n\n");
							
							// append result to the data file
							try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
								// add data entry
								writer.println(ACTING_DELIBERATIVE_TASKS + ";"
										+ "" + ACTING_DELIBERATIVE_SHARED + ";"
										+ "" + modelUncertainty + ";"
										+ "" + platformUncertainty + ";"
										+ "" + goal.getPlanningAttempts() + ";"
										+ "" + (goal.getTotalPlanningTime() / 1000) + ";"
										+ "" + goal.getExecutionAttempts() + ";"
										+ "" + (goal.getTotalExecutionTime() / 1000) + ";"
										+ "" + goal.getContingencyHandlingAttempts() + ";"
										+ "" + (goal.getTotalContingencyHandlingTime() / 1000) + ";"
										+ "" + goal.getStatus());
							}
						}
					}
					catch (SynchronizationCycleException | PlatformException ex) {
						System.err.println(ex.getMessage());
					}
					finally {
						// stop simulator
						simulator.stop();
					}
					
					
					System.out.println("Terminating agent...");
					// stop agent
					agent.stop();
					System.out.println(".... finish!");
					System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				}
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
