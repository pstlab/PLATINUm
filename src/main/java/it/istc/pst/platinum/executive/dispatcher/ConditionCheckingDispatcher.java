package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class ConditionCheckingDispatcher extends Dispatcher<Executive> 
{
	/**
	 * 
	 * @param exec
	 */
	protected ConditionCheckingDispatcher() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public final void handleTick(long tick) 
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
					// check the actual bounds of the token
					this.executive.checkSchedule(node);
					// schedule node if possible 
					if (tau >= node.getStart()[0] && tau <= node.getStart()[1])
					{
						// schedule token start time
						this.executive.scheduleTokenStart(node, tau);
						logger.info("{Dispatcher} {tau: " + tau + "} -> Start executing node at time: " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
						// actually dispatch command if needed
						this.dispatch(node);
					}
					else if (tau > node.getStart()[1]) 
					{
						// keep going with the execution by scheduling to the upper bound
						this.executive.scheduleTokenStart(node, node.getStart()[1]);
						// dispatch node
						this.dispatch(node);
						logger.warning("{Dispatcher} {tick = " + tick + " } {tau: " + tau + "} -> (Late) Start executing node at time= " + node.getStart()[1] + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					else  
					{
						// wait - not ready for dispatching
						logger.debug("{Dispatcher} {tick: " + tick + " } {tau: " + tau + "} -> Start conditions satisifed but node schedule not ready for dispatching\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
				else {
					
					// dispatching conditions not satisfied
					logger.debug("{Dispatcher} {tick: " + tick + "} {tau: " + tau + "} -> Start conditions not satisfied\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
			}
			catch (ExecutionException ex) 
			{
				// token dispatching error 
				logger.error("{Dispatcher} {tick: " + tick + "} {tau= " + tau + "} -> ERROR: Node execution start failure" + node + "\n"
						+ "\t- reason: Node scheduling failure " + node.getGroundSignature() + " (" + node + ")\n"
						+ "\t- message: " + ex.getMessage() + "\n");
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void dispatch(ExecutionNode node) 
	{
		// check node controllability properties
		if (node.getControllabilityType().equals(ControllabilityType.PARTIALLY_CONTROLLABLE) ||
				node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE)) 
		{
			// dispatch the command through the executive if needed
			this.executive.dispatchNodeToThePlatform(node);
		}
	}
}
