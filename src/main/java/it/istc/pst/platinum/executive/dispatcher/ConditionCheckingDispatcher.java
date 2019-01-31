package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.ex.ObservationException;
import it.istc.pst.platinum.executive.lang.ex.StartOverflow;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;

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
			throws ExecutionException, PlatformException 
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
				if (tau >= node.getStart()[0]) // && tau <= node.getStart()[1])
				{
					try
					{
						// dispatch the command through the executive if needed
						this.executive.sendStartCommandSignalToPlatform(node);
						// schedule token start time
						this.executive.scheduleTokenStart(node, tau);
						// start node execution
						logger.info("{Dispatcher} {tick: " + tick + "} {tau: " + tau + "} -> Start executing node at time: " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						// create execution cause
						ExecutionFailureCause cause = new StartOverflow(tick, node, tau);
						// throw execution exception
						throw new ObservationException(
								"The dispatched start time of the token does not comply with the plan:\n"
								+ "\t- start: " + tau + "\n"
								+ "\t- node: " + node + "\n", 
								cause);
					}
				}
//				else if (tau > node.getStart()[1]) 
//				{
//					try
//					{
//						// dispatch the command through the executive if needed
//						this.executive.sendStartCommandSignalToPlatform(node);
//						// keep going with the execution by scheduling to the upper bound
//						this.executive.scheduleTokenStart(node, node.getStart()[1]);
//						logger.warning("{Dispatcher} {tick: " + tick + " } {tau: " + tau + "} -> (Late) Start executing node at time: " + node.getStart()[1] + "\n"
//								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
//					}
//					catch (TemporalConstraintPropagationException ex) {
//						// create execution cause
//						ExecutionFailureCause cause = new StartOverflow(tick, node, tau);
//						// throw execution exception
//						throw new ObservationException(
//								"The dispatched start time of the token does not comply with the plan:\n"
//								+ "\t- start: " + tau + "\n"
//								+ "\t- node: " + node + "\n", 
//								cause);
//					}
//				}
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
}
