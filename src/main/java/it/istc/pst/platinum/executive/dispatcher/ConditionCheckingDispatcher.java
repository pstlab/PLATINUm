package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
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
			throws ExecutionException 
	{
		// get tau
		long tau = this.executive.convertTickToTau(tick);
		// check human waiting nodes ready to dispatch
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.WAITING)) 
		{
			// check if node can start
			if (this.executive.canStart(node)) 
			{
				// check the actual bounds of the token
				this.executive.checkSchedule(node);
				// schedule node if possible 
				if (tau >= node.getStart()[0] && tau <= node.getStart()[1])
				{
					// check if constraint propagation is actually needed
					if (node.getStart()[0] != node.getStart()[1]) {
						// schedule token start time
						this.executive.scheduleTokenStart(node, tau);
					}
					
					// start node execution
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
					logger.warning("{Dispatcher} {tick = " + tick + " } {tau: " + tau + "} -> (Late) Start executing node at time: " + node.getStart()[1] + "\n"
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
	}
	
	/**
	 * 
	 */
	@Override
	public void dispatch(ExecutionNode node) 
			throws ExecutionException 
	{
		try {
			// dispatch the command through the executive if needed
			this.executive.dispatchCommandToThePlatform(node);
		}
		catch (PlatformException ex) {
			
			/*
			 * TODO : add exception cause
			 */
			throw new ExecutionException(ex.getMessage(), null);
		}
	}
}
