package it.istc.pst.platinum.testing.app.executive.dc;

import it.istc.pst.platinum.control.acting.GoalOrientedActingAgent;
import it.istc.pst.platinum.control.lang.AgentTaskDescription;
import it.istc.pst.platinum.control.lang.TokenDescription;
import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.executive.dc.DCExecutive;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class SimpleDCExecutiveTest 
{
	private static int HORIZON = 100;								// in seconds
	
	private static int[] UNCERTAINTY = new int[] {
			5, 10, 15, 20, 25, 30
	};
	
	private static int[] GOAL = new int[] {
			1, 2, 3, 4, 5
	};
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		/*for (int uncertainty : UNCERTAINTY) 
		{
			for (int goal : GOAL) 
			{
				try
				{
					// create a thread managing the execution
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() 
						{
							try 
							{
								// create goal oriented agent
								GoalOrientedActingAgent agent = new GoalOrientedActingAgent(Planner.class, DCExecutive.class);
								// starting agent
								System.out.println("Starting goal-oriented agent...");
								// start agent
								agent.start();
								
								
								
								// crate satellite platform simulator
								PlatformSimulator simulator = PlatformProxyBuilder.build(
										PlatformSimulator.class,
										"etc/platform/dc/satellite/config_u" + uncertainty + ".xml");
								
								// start platform simulator
								System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
								simulator.start();
								
								// initialize and start the agent
								System.out.println("Initializing goal-oriented agent on planning domain [domains/satellite/dc/satellite_u\"" + uncertainty + "\".ddl] with #goal " + goal + "\n");
								agent.initialize(
										simulator, 
										"domains/satellite/dc/satellite_u" + uncertainty + ".ddl");
								
								
								// create task description
								AgentTaskDescription task = createTaskDescription(goal);
								// buffer task description
								agent.buffer(task);
							}
							catch (Exception ex) {
								System.err.println("Interrupting agent execution");
							}
						}
					});
					
					// start thread
					thread.start();
					Thread.sleep((HORIZON * 1000) + 5000);
					thread.interrupt();
				
				}
				catch (InterruptedException ex) {
					System.err.println(ex.getMessage());
				}
				
				
			}
			
			
		} */
		
		
		// -------------------------------------------------------------
		int uncertainty = 30;
		int goal = 4;
		try {// create goal oriented agent
		GoalOrientedActingAgent agent = new GoalOrientedActingAgent(Planner.class, DCExecutive.class);
		// starting agent
		System.out.println("Starting goal-oriented agent...");
		// start agent
		agent.start();
		
		
		
		// crate satellite platform simulator
		PlatformSimulator simulator = PlatformProxyBuilder.build(
				PlatformSimulator.class,
				"etc/platform/dc/satellite/config_u" + uncertainty + ".xml");
		
		// start platform simulator
		System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
		simulator.start();
		
		// initialize and start the agent
		System.out.println("Initializing goal-oriented agent on planning domain [domains/satellite/dc/satellite_u\"" + uncertainty + "\".ddl] with #goal " + goal + "\n");
		agent.initialize(
				simulator, 
				"domains/satellite/dc/satellite_u" + uncertainty + ".ddl");
		
		
		// create task description
		AgentTaskDescription task = createTaskDescription(goal);
		// buffer task description
		agent.buffer(task);
	}
	catch (Exception ex) {
		System.err.println("Interrupting agent execution");
	}
		
		// -------------------------------------------------------------

	}
	
	/**
	 * 
	 * @param goals
	 * @return
	 */
	private static AgentTaskDescription createTaskDescription(int goals) 
	{
		// create task description
		AgentTaskDescription description = new AgentTaskDescription();
		description.addFactDescription(new TokenDescription(
				"PointingMode", 
				"Earth", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {0, 100}, 
				new long[] {1, 100}));
		
		description.addFactDescription(new TokenDescription(
				"Window", 
				"_NotVisible", 
				new String[] {}, 
				new long[] {0, 0}, 
				new long[] {10, 10}, 
				new long[] {10, 10}));
		
		description.addFactDescription(new TokenDescription(
				"Window", 
				"_Visible", 
				new String[] {}, 
				new long[] {10, 10}, 
				new long[] {40, 40}, 
				new long[] {30, 30}));
		
		description.addFactDescription(new TokenDescription(
				"Window", 
				"_NotVisible", 
				new String[] {}, 
				new long[] {40, 40}, 
				new long[] {50, 50}, 
				new long[] {10, 10}));
		
		// create planning goals
		for (int index = 0; index < goals; index++) {
			// create planning goal
			description.addGoalDescription(new TokenDescription(
					"PointingMode", 
					"Science"));
		}
		
		// get task description
		return description;
	}
}
