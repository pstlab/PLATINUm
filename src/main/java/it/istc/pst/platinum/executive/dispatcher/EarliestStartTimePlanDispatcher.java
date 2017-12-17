package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public class EarliestStartTimePlanDispatcher extends Dispatcher 
{
	@ExecutivePlaceholder
	private Executive executive;
	
	/**
	 * 
	 * @param exec
	 */
	protected EarliestStartTimePlanDispatcher() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public void handleTick(long tick) 
	{
		// get tau
		long tau = this.executive.convertTickToTau(tick);
		// check human waiting nodes ready to dispatch
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.WAITING)) 
		{
			try
			{
				// check if node can start
				if (this.executive.canStart(node)) 
				{
					// check the actual bounds
					this.executive.checkSchedule(node);
					// check start window
					if (tau >= node.getStart()[0] && tau <= node.getStart()[1]) {
						// schedule node start to tau
						this.executive.scheduleStartTime(node, tau);
						// dispatch node
						this.dispatch(node);
						logger.info("{tick = " + tick + " } {tau= " + tau + "} {PlanDispatcher} -> Start executing node at time= " + tau + " - node " + node);
					}
					else if (tau > node.getStart()[1]) {
						// keep going with the execution by scheduling to the upper bound
						this.executive.scheduleStartTime(node, node.getStart()[1]);
						// dispatch node
						this.dispatch(node);
						logger.info("{tick = " + tick + " } {tau= " + tau + "} {PlanDispatcher} -> Start executing node at time= " + tau + " - node " + node);
					}
				}
			}
			catch (Exception ex) {
				throw new RuntimeException("{tick = " + tick + "} {tau= " + tau + "} {PlanDispatcher} -> Failure start executing node " + node + " ERROR {\n- Failure while scheduling node " + node + "\n}\n" + ex.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @param node
	 */
	@Override
	public void dispatch(ExecutionNode node) {
		// update the status of the node
		this.executive.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
	}
}
