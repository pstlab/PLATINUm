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
public class DeliberativeProcess implements Runnable 
{
	private GoalOrientedActingAgent agent;
	
	/**
	 * 
	 * @param agent
	 */
	protected DeliberativeProcess(GoalOrientedActingAgent agent) {
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
				Goal goal = this.agent.waitGoal(GoalStatus.SELECTED);
				System.out.println("... deliberating on goal\n" + goal + "\n"); 
				// deliberate extracted goal
				boolean success = this.agent.plan(goal);
				// check deliberative result
				if (success) {
					// commit planned goal
					this.agent.commit(goal);
				}
				else {
					// deliberative failure abort goal
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
	 * @throws NoSolutionFoundException
	 */
	protected SolutionPlan doPlan(PlanDataBase pdb) 
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
