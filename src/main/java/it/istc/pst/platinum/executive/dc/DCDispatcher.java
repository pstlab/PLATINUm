package it.istc.pst.platinum.executive.dc;

import java.util.List;

import it.istc.pst.platinum.executive.dc.strategy.State;
import it.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.istc.pst.platinum.executive.dc.strategy.result.Wait;
import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public class DCDispatcher extends Dispatcher<DCExecutive>
{
	@ExecutivePlaceholder
	private DCExecutive executive;
	
	/**
	 * 
	 */
	protected DCDispatcher() {
		super();
	}
	
	/**
	 *
	 * 
	 */
	@Override
	public void handleTick(long tick)
	{
		// get tau 
		long tau = this.executive.convertTickToTau(tick);
		// initialize status
		PlanExecutionStatus status = new PlanExecutionStatus(tau);
		// add the status of the timelines
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
			status.addTimelineStatus(node);
		}
		
		
		try 
		{
			// ask the strategy actions to perform
			List<Action> result = this.executive.dcs.askAllStrategySteps(
					status.getTime(), 
					status.getStatus(),
					true);
				
			// check actions
			for (Action a : result) 
			{
				if (a instanceof Wait) 
				{
					/*
					 * wait
					 */
					
					System.out.println("[Dispatching] [tick: " + tick +"] action: Wait");
					
				}
				else if (a instanceof Transition) 
				{
					// get transition
					Transition t = (Transition) a;
					
					// check token to dispatch
					State s = t.getTransitionTo();
					// name of the token
					String tokenName = s.getToken();
					// name of the timeline
					String tlName = s.getTimeline();
					
					System.out.println("[Dispatching] [tick: " + tick +"] action: Dispatch -> " + tlName + "." + tokenName);
					
				}
				else {
					
					throw new Exception("Unknown Action: " + a.getClass().getName() + "\n");
				}
			}
		}
		catch (Exception ex) {
			System.err.println("Error while asking actions to the strategy manager\n\t- message: " + ex.getMessage() + "\n");
		}

	}
}
