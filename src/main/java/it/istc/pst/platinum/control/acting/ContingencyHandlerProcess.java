package it.istc.pst.platinum.control.acting;

import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.GoalStatus;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class ContingencyHandlerProcess implements Runnable 
{
	private GoalOrientedActingAgent agent;
	
	/**
	 * 
	 * @param agent
	 */
	protected ContingencyHandlerProcess(GoalOrientedActingAgent agent) {
		this.agent = agent;
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
				Goal goal = this.agent.waitGoal(GoalStatus.SUSPENDED);
				System.out.println("... repairing goal\n" + goal + "\n");
				// try to repair the goal 
				boolean success = this.agent.repair(goal);
				// check executive result
				if (success) {
					// goal repaired try to execute it again
					this.agent.commit(goal);
				}
				else {
					// goal aborted due to unsolvable failure during execution 
					this.agent.abort(goal);
				}
			}
			catch (InterruptedException ex) {
				running = false;
			}
		}
	}
	
	/**
	 * 
	 * @param pdb
	 * @return
	 * @throws NoSolutionFoundException
	 */
	protected SolutionPlan doHandle(PlanDataBase pdb) 
			throws NoSolutionFoundException 
	{
		// setup planner on the current status of the plan database
		Planner planner = PlannerBuilder.createAndSet(pdb);
		// start planning 
		SolutionPlan plan = planner.plan();
		// get plan found
		return plan;
	}
}
