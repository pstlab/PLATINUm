package it.cnr.istc.pst.platinum.control.acting;

import java.util.HashSet;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.ai.executive.ExecutiveBuilder;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionPreparationException;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.control.lang.Goal;
import it.cnr.istc.pst.platinum.control.lang.GoalStatus;
import it.cnr.istc.pst.platinum.control.lang.TokenDescription;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

/**
 * 
 * @author alessandro
 *
 */
public class ExecutiveProcess implements Runnable {
	
	private GoalOrientedActingAgent agent;
	private Class<? extends Executive> eClass;
	
	/**
	 * 
	 * @param eClass
	 * @param agent
	 */
	protected ExecutiveProcess(Class<? extends Executive> eClass, GoalOrientedActingAgent agent) {
		this.agent = agent;
		this.eClass = eClass;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		
		boolean running = true;
		while(running) {
			
			try {
				
				// take a goal to plan for
				Goal goal = this.agent.waitGoal(GoalStatus.COMMITTED);
				System.out.println("executing goal ...\n" + goal + "\n");
				// execute extracted goal
				int code = this.agent.execute(goal);
				
				// check execution code
				switch (code) {
				
					// success
					case 1 : {
						
						// goal successfully executed
						this.agent.finish(goal);
					}
					break;
					
					// execution failure
					case 2 : {
						
						// goal execution suspended due to some failure
						this.agent.suspend(goal);
					}
					break;
					
					// execution error
					case 3 : {
						
						// goal execution error due to some major failure
						this.agent.abort(goal);
					}
					break;
					
					default : {
						
						// unknown execution code
						System.err.println("Unknown Execution code :\n"
								+ "\t- goal: " + goal + "\n"
								+ "\t- code: " + code + "\n"); 
					}
				}
				
				
			} catch (InterruptedException ex) {
				running = false;
			}
		}
	}
	
	/**
	 * 
	 * @param goal
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ExecutionPreparationException
	 * @throws PlatformException
	 */
	protected void doHandle(Goal goal) 
			throws InterruptedException, ExecutionException, ExecutionPreparationException, PlatformException  {
		
		// get solution plan 
		SolutionPlan plan = goal.getPlan();
		// build executive
		Executive exec = ExecutiveBuilder.createAndSet(this.eClass, 0, plan.getHorizon());
		// export plan 
		PlanProtocolDescriptor desc = plan.export();
		System.out.println("\n\nREADY TO EXECUTE PLAN:\n" + desc + "\n\n");
		// set the executive according to the plan being executed
		exec.initialize(desc);
		
		// bind simulator if any
		if (this.agent.proxy != null) {
			// bind simulator
			exec.link(this.agent.proxy);
		}
		
		
		try {
			
			// run the executive starting at a given tick
			boolean complete = exec.execute(goal.getExecutionTick(), goal);
			// check execution result 
			if (!complete) {
				
				// get failure cause
				ExecutionFailureCause cause = exec.getFailureCause();
				// set failure cause
				goal.setFailureCause(cause);
				// set repaired 
				goal.setRepaired(false);
				// set goal interruption tick
				goal.setExecutionTick(cause.getInterruptionTick());
				// set execution trace by taking into account executed nodes
				for (ExecutionNode node : exec.getNodes(ExecutionNodeStatus.EXECUTED)) {
					// add the node to the goal execution trace
					goal.addNodeToExecutionTrace(node);
				}
				
				// get the name of of goal components
				Set<String> components = new HashSet<>();
				for (TokenDescription t : goal.getTaskDescription().getGoals()) {
					components.add(t.getComponent());
				}
				
				// set execution trace by taking into account also (virtual) nodes in-execution
				for (ExecutionNode node : exec.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
					// do not consider nodes belonging to "goal component"
					if (!components.contains(node.getComponent())) {
						// add the node to the goal execution trace
						goal.addNodeToExecutionTrace(node);
					}
				}
				
				// throw exception
				throw new ExecutionException("Execution failure... try to repair the plan through replanning... \n"
						+ "\t- cause: " + cause + "\n", cause);
			}
			
		} finally {
		
			// stop simulator if any
			if (this.agent.proxy != null) {
				// unlink from simulator
				exec.unlink();
			}
			
		}
	}
}
