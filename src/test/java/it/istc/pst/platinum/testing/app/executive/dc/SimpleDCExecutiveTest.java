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
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			// create goal oriented agent
			GoalOrientedActingAgent agent = new GoalOrientedActingAgent(Planner.class, DCExecutive.class);
			agent.start();
			System.out.println("Starting agent...");
			
			// crate satellite platform simulator
			PlatformSimulator simulator = PlatformProxyBuilder.build(
					PlatformSimulator.class,
					"etc/platform/satellite/config.xml");
			// start simulator
			simulator.start();
			
			// initialize the agent
			agent.initialize(
					simulator, 
					"domains/satellite/acta/satellite.ddl");
			
			// create task description
			AgentTaskDescription task = createTaskDescription();
			// buffer task description
			agent.buffer(task);

			
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private static AgentTaskDescription createTaskDescription() 
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
		
		description.addGoalDescription(new TokenDescription(
				"PointingMode", 
				"Science"));
		
		// get task description
		return description;
	}
}
