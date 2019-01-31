package it.istc.pst.platinum.executive.dc;

import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public class DCDispatcher extends Dispatcher 
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
		
		// evaluate current status
		DCResult answer = this.executive.checker.evaluate(status);
		// check answer type
		switch (answer.getType())
		{
			case WAIT : 
			{
				/*
				 * TODO : no action to perform
				 */
			}
			break;
		
			case DISPATCH : 
			{
				
				/*
				 * TODO : do dispatch according to DC checker answer
				 */
				
			}
			break;
			
			case FAILURE : 
			{
				/*
				 * TODO : do manage DC checker failure 
				 */
			}
			break;
		}
	}
}
