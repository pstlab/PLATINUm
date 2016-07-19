package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.PlanDispatcher;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class EarliesStartTimePlanDispatcher extends PlanDispatcher {

	/**
	 * 
	 * @param exec
	 */
	public EarliesStartTimePlanDispatcher(Executive<?, ?, ?> exec) {
		super(exec);
	}
	
	/**
	 * 
	 */
	@Override
	protected void handleTick(long tick) 
	{
		// get current time in seconds
		double tau = this.clock.convertClockTickToSeconds(tick);
		// compute ready to execute nodes
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING)) {
			// check start condition and controllability type
			if (this.pdb.canStartExecution(node)) {
				
				try {
					
					// check the actual bounds of the interval to schedule
					this.pdb.checkSchedule(node);
					// schedule the start time to its earliest start time
					long start = node.getStart()[0];
					// check if delay
					if (tau > start) {
						// set current time
						start = Math.round(tau);
					}
					
					// schedule start time of the activity
					this.pdb.scheduleStartTime(node, start);
					// update node status
					this.pdb.updateNodeStatus(node, ExecutionNodeStatus.SCHEDULED);
				}
				catch (Exception ex) {
					// failure start executing node
					throw new RuntimeException("{tick = " + tick + "} {EarliesStartTimePlanDispatcher} -> Failure start executing node " + node + " ERROR {\n- Failure while scheduling node " + node + "\n}\n" + ex.getMessage());
				}
			}
		}
		
		// check scheduled tokens which start execution
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.SCHEDULED)) {
			
			// check schedule
			this.pdb.checkSchedule(node);
			// get earliest start time
			long start = node.getStart()[0];
			// check if ready to start execution
			if (tau >= start) {
				
				// dispatch node
				this.dispatch(node);
				System.out.println("{tick = " + tick + "} {EarliesStartTimePlanDispatcher} -> Start executing node " + node);
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void dispatch(ExecutionNode node) {
		// simply update node status
		this.pdb.updateNodeStatus(node, ExecutionNodeStatus.IN_EXECUTION);
	}
}
