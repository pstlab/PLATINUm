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
public class DeliberativeProcess implements Runnable 
{
	private GoalOrientedActingAgent agent;
	private Class<? extends Planner> pClass;
	private boolean displayPlan;
	
	/**
	 * 
	 * @param pClass
	 * @param displayPlan
	 * @param agent
	 */
	protected DeliberativeProcess(Class<? extends Planner> pClass, boolean displayPlan, GoalOrientedActingAgent agent) {
		this.agent = agent;
		this.pClass = pClass;
		this.displayPlan = displayPlan;
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
	protected SolutionPlan doHandle(PlanDataBase pdb) 
			throws NoSolutionFoundException 
	{
		// setup planner on the current status of the plan database
		Planner planner = PlannerBuilder.createAndSet(this.pClass, pdb);
		// start planning 
		SolutionPlan plan = planner.plan();
		// display plan 
		if (this.displayPlan) {
			planner.display();
		}
		
		// get plan found
		return plan;
	}
}
