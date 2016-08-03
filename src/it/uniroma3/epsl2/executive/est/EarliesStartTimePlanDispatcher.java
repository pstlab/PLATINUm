package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.PlanDispatcher;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class EarliesStartTimePlanDispatcher implements PlanDispatcher 
{
	private EarliestStartTimeExecutive executive;
	
	/**
	 * 
	 * @param exec
	 */
	public EarliesStartTimePlanDispatcher(EarliestStartTimeExecutive exec) {
		this.executive = exec;
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
						System.out.println("{tick = " + tick + " } {tau= " + tau + "} {PlanDispatcher} -> Start executing node at time= " + tau + " - node " + node);
					}
					else if (tau > node.getStart()[1]) {
						// keep going with the execution by scheduling to the upper bound
						this.executive.scheduleStartTime(node, node.getStart()[1]);
						// dispatch node
						this.dispatch(node);
						System.out.println("{tick = " + tick + " } {tau= " + tau + "} {PlanDispatcher} -> Start executing node at time= " + tau + " - node " + node);
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
