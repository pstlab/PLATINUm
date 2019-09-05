package it.istc.pst.platinum.testing.app.executive.dc;

import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
import it.istc.pst.platinum.executive.dc.DCExecutive;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class SimpleDCExecutiveTest 
{
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
		for (int uncertainty : UNCERTAINTY) 
		{
			for (int goal : GOAL) 
			{
				try
				{
					// build the plan database
					PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(
							"domains/satellite/dc/satellite_u" + uncertainty + ".ddl", 
							"domains/satellite/dc/satellite_u" + uncertainty + "_g" + goal + ".pdl");
					
					// create planner 
					Planner planner = PlannerBuilder.createAndSet(pdb);
					System.out.println("Start planning... ");
					// do planning
					SolutionPlan solution = planner.plan();
					System.out.println("Plan found after " + solution.getSolvingTime() + " msecs\n");
					System.out.println(solution.export());
					System.out.println();
					
					
					
					// crate satellite platform simulator
					PlatformSimulator simulator = PlatformProxyBuilder.build(
							PlatformSimulator.class,
							"etc/platform/dc/satellite/config_u" + uncertainty + ".xml");
					
					// create executive
					Executive exec  = ExecutiveBuilder.createAndSet(DCExecutive.class, 0, solution.getHorizon());
					// link executive to platform simulator
					exec.link(simulator);
					// export the plan
					exec.initialize(solution.export());
					
					// get start time
					long start  = System.currentTimeMillis();
					// start platform simulator
					System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
					simulator.start();
					// start plan execution
					System.out.println("Starting plan execution...");
					if (exec.execute()) {
						// get execution time
						long execTime = System.currentTimeMillis() - start;
						System.out.println("... plan execution ends after " + (execTime / 1000) + " seconds\n");
					}
					else {
						// get execution time
						long execTime = System.currentTimeMillis() - start;
						System.out.println("... plan execution failure after " + (execTime / 1000) +  " seconds\n"
								+ "\t- failure-type: " + exec.getFailureCause().getType() + "\n"
								+ "\t- failure-tick: " + exec.getFailureCause().getInterruptionTick() + "\n"
								+ "\t- failure-node: " + exec.getFailureCause().getInterruptionNode() + "\n");
					}
					
					// stop simulator
					simulator.stop();

				}
				catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		} 
	}
	
//	/**
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) 
//	{
//		
//		/*for (int uncertainty : UNCERTAINTY) 
//		{
//			for (int goal : GOAL) 
//			{
//				try
//				{
//					// create a thread managing the execution
//					Thread thread = new Thread(new Runnable() {
//						
//						@Override
//						public void run() 
//						{
//							try 
//							{
//								// create goal oriented agent
//								GoalOrientedActingAgent agent = new GoalOrientedActingAgent(Planner.class, DCExecutive.class);
//								// starting agent
//								System.out.println("Starting goal-oriented agent...");
//								// start agent
//								agent.start();
//								
//								
//								
//								// crate satellite platform simulator
//								PlatformSimulator simulator = PlatformProxyBuilder.build(
//										PlatformSimulator.class,
//										"etc/platform/dc/satellite/config_u" + uncertainty + ".xml");
//								
//								// start platform simulator
//								System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
//								simulator.start();
//								
//								// initialize and start the agent
//								System.out.println("Initializing goal-oriented agent on planning domain [domains/satellite/dc/satellite_u\"" + uncertainty + "\".ddl] with #goal " + goal + "\n");
//								agent.initialize(
//										simulator, 
//										"domains/satellite/dc/satellite_u" + uncertainty + ".ddl");
//								
//								
//								// create task description
//								AgentTaskDescription task = createTaskDescription(goal);
//								// buffer task description
//								agent.buffer(task);
//							}
//							catch (Exception ex) {
//								System.err.println("Interrupting agent execution");
//							}
//						}
//					});
//					
//					// start thread
//					thread.start();
//					Thread.sleep((HORIZON * 1000) + 5000);
//					thread.interrupt();
//				
//				}
//				catch (InterruptedException ex) {
//					System.err.println(ex.getMessage());
//				}
//				
//				
//			}
//			
//			
//		} */
//		
//		
//		// -------------------------------------------------------------
//		int uncertainty = 30;
//		int goal = 4;
//		try {// create goal oriented agent
//		GoalOrientedActingAgent agent = new GoalOrientedActingAgent(Planner.class, DCExecutive.class);
//		// starting agent
//		System.out.println("Starting goal-oriented agent...");
//		// start agent
//		agent.start();
//		
//		
//		
//		// crate satellite platform simulator
//		PlatformSimulator simulator = PlatformProxyBuilder.build(
//				PlatformSimulator.class,
//				"etc/platform/dc/satellite/config_u" + uncertainty + ".xml");
//		
//		// start platform simulator
//		System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
//		simulator.start();
//		
//		// initialize and start the agent
//		System.out.println("Initializing goal-oriented agent on planning domain [domains/satellite/dc/satellite_u\"" + uncertainty + "\".ddl] with #goal " + goal + "\n");
//		agent.initialize(
//				simulator, 
//				"domains/satellite/dc/satellite_u" + uncertainty + ".ddl");
//		
//		
//		// create task description
//		AgentTaskDescription task = createTaskDescription(goal);
//		// buffer task description
//		agent.buffer(task);
//	}
//	catch (Exception ex) {
//		System.err.println("Interrupting agent execution");
//	}
//		
//		// -------------------------------------------------------------
//
//	}
//	
//	/**
//	 * 
//	 * @param goals
//	 * @return
//	 */
//	private static AgentTaskDescription createTaskDescription(int goals) 
//	{
//		// create task description
//		AgentTaskDescription description = new AgentTaskDescription();
//		description.addFactDescription(new TokenDescription(
//				"PointingMode", 
//				"Earth", 
//				new String[] {}, 
//				new long[] {0, 0}, 
//				new long[] {0, 100}, 
//				new long[] {1, 100}));
//		
//		description.addFactDescription(new TokenDescription(
//				"Window", 
//				"_NotVisible", 
//				new String[] {}, 
//				new long[] {0, 0}, 
//				new long[] {10, 10}, 
//				new long[] {10, 10}));
//		
//		description.addFactDescription(new TokenDescription(
//				"Window", 
//				"_Visible", 
//				new String[] {}, 
//				new long[] {10, 10}, 
//				new long[] {40, 40}, 
//				new long[] {30, 30}));
//		
//		description.addFactDescription(new TokenDescription(
//				"Window", 
//				"_NotVisible", 
//				new String[] {}, 
//				new long[] {40, 40}, 
//				new long[] {50, 50}, 
//				new long[] {10, 10}));
//		
//		// create planning goals
//		for (int index = 0; index < goals; index++) {
//			// create planning goal
//			description.addGoalDescription(new TokenDescription(
//					"PointingMode", 
//					"Science"));
//		}
//		
//		// get task description
//		return description;
//	}
}
