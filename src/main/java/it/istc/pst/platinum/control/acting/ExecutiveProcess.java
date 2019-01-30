package it.istc.pst.platinum.control.acting;

import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.GoalStatus;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulatorBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutiveProcess implements Runnable 
{
	private GoalOrientedActingAgent agent;
	private PlatformSimulator simulator;
	
	/**
	 * 
	 * @param agent
	 */
	protected ExecutiveProcess(GoalOrientedActingAgent agent) {
		this.agent = agent;
		this.simulator = null;
	}
	
	/**
	 * 
	 * @param path
	 * @throws PlatformException
	 */
	public void initialize(String path) 
			throws PlatformException 
	{
		// build platform simulator
		this.simulator = PlatformSimulatorBuilder.build(path);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void run() {
		boolean running = true;
		while(running)
		{
			try
			{
				// take a goal to plan for
				Goal goal = this.agent.waitGoal(GoalStatus.COMMITTED);
				System.out.println("executing goal ...\n" + goal.getPlan() + "\n");
				// execute extracted goal
				boolean success = this.agent.execute(goal);
				// check executive result
				if (success) {
					// goal execution successfully complete
					this.agent.finish(goal);
				}
				else {
					// goal execution suspended due to some errors
					this.agent.suspend(goal);
				}
			}
			catch (InterruptedException ex) {
				running = false;
			}
		}
	}
	
	/**
	 * 
	 * @param goal
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	protected void doExecute(Goal goal) 
			throws InterruptedException, ExecutionException  
	{
		// get solution plan 
		SolutionPlan plan = goal.getPlan();
		// build executive
		Executive exec = ExecutiveBuilder.createAndSet(0, plan.getHorizon());
		
		// initialize the executive according to the plan being executed
		exec.initialize(plan.export());
		// bind simulator if any
		if (this.simulator != null) {
			// bind simulator
			exec.link(this.simulator);
			// start simulator
			this.simulator.start();
		}
		
		
		// run the executive
		boolean complete = exec.execute();
		// stop simulator if any
		if (this.simulator != null) {
			// unlink from simulator
			exec.unlink();
			this.simulator.stop();
		}
		
		// check execution result 
		if (!complete) 
		{
			// get failure cause
			ExecutionFailureCause cause = exec.getFailureCause();
			// set failure cause
			goal.setFailureCause(cause);
			// set execution trace by taking into account executed nodes
			for (ExecutionNode node : exec.getNodes(ExecutionNodeStatus.EXECUTED)) {
				// add the node to the goal execution trace
				goal.addNodeToExecutionTrace(node);
			}
			
			// throw exception
			throw new ExecutionException("Execution failure... try to repair the plan through replanning... \n"
					+ "\t- cause: " + cause + "\n", cause);
		}
	}
}
