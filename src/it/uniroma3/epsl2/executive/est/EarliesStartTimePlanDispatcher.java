package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.PlanDispatcher;
import it.uniroma3.epsl2.executive.pdb.ControllabilityType;
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
	public EarliesStartTimePlanDispatcher(Executive<?, ?> exec) {
		super(exec);
	}
	
	
	/**
	 * 
	 */
	@Override
	protected void onTick(long tick) 
	{
		// compute ready to execute nodes
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.WAIT)) {
			// check start condition and controllability type
			if (this.pdb.canStartExecution(node)) {
				// update the status of the node in the plan
				this.pdb.updateNodeStatus(node, ExecutionNodeStatus.READY);
			}
		}

		// compute schedules of ready to execute nodes
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.READY)) {
			
			try {
				
				// check the actual bounds of the interval to schedule
				this.pdb.checkSchedule(node);
				
				// check controllability type
				if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE) || 
						node.getControllabilityType().equals(ControllabilityType.EXTERNAL_TOKEN)) {
					
					// schedule the controllable token
					long start = node.getStart()[0];
					this.pdb.scheduleStartTime(node, start);
					
					// check resulting schedule 
					this.pdb.checkSchedule(node);
					
					// schedule duration of the node
					long duration = node.getDuration()[0];
					this.pdb.scheduleDuration(node, duration);
				}
				else // ControllabilityType.UNCONTROLLABLE_DURATION)
				{
					// schedule the start time of the token with uncontrollable duration
					long start = node.getStart()[0];
					this.pdb.scheduleStartTime(node, start);
				}
				
				// update node status
				this.pdb.updateNodeStatus(node, ExecutionNodeStatus.SCHEDULED);
			}
			catch (Exception ex) {
				System.err.println("ERROR {\n- Failure while scheduling node " + node + "\n}\n" + ex.getMessage());
			}
		}
		
		// check if execution should start
		long seconds = this.clock.getSecondsFromTheOrigin(tick);
		
		// check scheduled tokens 
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.SCHEDULED)) {
			// get scheduled start time
			long start = node.getStart()[0];
			if (seconds == start) {
				// start executing node
				this.pdb.updateNodeStatus(node, ExecutionNodeStatus.IN_EXECUTION);
				System.out.println("{tick = " + tick + "} {Dispatcher} -> Start executing node " + node);
			}
		}
	}
}
