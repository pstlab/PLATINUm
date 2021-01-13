package it.cnr.istc.pst.platinum.control.acting;

import it.cnr.istc.pst.platinum.ai.deliberative.Planner;
import it.cnr.istc.pst.platinum.ai.deliberative.PlannerBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.control.lang.Goal;
import it.cnr.istc.pst.platinum.control.lang.GoalStatus;

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
					// cannot repair the plan - abort the goal  
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
	 * @param pClass
	 * @param pdb
	 * @return
	 * @throws NoSolutionFoundException
	 */
	protected SolutionPlan doHandle(Class<? extends Planner> pClass, PlanDataBase pdb) 
			throws NoSolutionFoundException 
	{
		// setup planner on the current status of the plan database
		Planner planner = PlannerBuilder.createAndSet(pClass, pdb);
		// start planning 
		SolutionPlan plan = planner.plan();
		// get plan found
		return plan;
	}
}
