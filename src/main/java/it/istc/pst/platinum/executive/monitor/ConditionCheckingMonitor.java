package it.istc.pst.platinum.executive.monitor;

import java.util.List;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class ConditionCheckingMonitor extends Monitor<Executive> 
{
	/**
	 * 
	 * @param exec
	 */
	protected ConditionCheckingMonitor() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public void handleTick(long tick) 
	{
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		// check received feedbacks
		List<ExecutionFeedback> feedbacks = this.checkObservations();
		// manage uncontrollable tokens of the plan according to the feedbacks
		for (ExecutionFeedback feedback : feedbacks)
		{
			try
			{
				// get node 
				ExecutionNode node = feedback.getNode();
				// check the schedule of the node
				this.executive.checkSchedule(node);
				// check execution result
				switch (feedback.getType())
				{
					case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
					case UNCONTROLLABLE_TOKEN_COMPLETE : 
					{
						// compute node duration of the token in execution
						long duration = Math.max(1, tau - node.getStart()[0]);
						// do propagate observed duration
						this.executive.scheduleTokenDuration(node, duration);
						logger.info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} -> Observed token execution with computed duration " + duration + " \n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					break;
					
					case UNCONTROLLABLE_TOKEN_START : 
					{
						// schedule the start of uncontrollable token
						this.executive.scheduleUncontrollableTokenStart(node, tau);
						logger.info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed start time= " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					break;
					
					case TOKEN_EXECUTION_FAILURE : {
						
						/*
						 * TODO : Manage execution failure from the functional level
						 */
					}
				}
			}
			catch(ExecutionException ex) 
			{
				// token dispatching error 
				logger.error("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Error while synchronizing observations\n"
						+ "\t- event: " + feedback.getType() + "\n"
						+ "\t- node: " + feedback.getNode().getGroundSignature() + " (" + feedback.getNode() + ")\n"
						+ "\t- message: " + ex.getMessage() + "\n");
			}
		}
		
		
		
		// manage controllable tokens of the plan
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION))
		{
			try
			{
				// check node controllability 
				if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE))
				{
					// check end conditions
					if (this.executive.canEnd(node))
					{
						// check node schedule
						this.executive.checkSchedule(node);
						// check expected schedule
						if (tau >= node.getEnd()[0] && tau <= node.getEnd()[1]) 
						{
							// compute (controllable) execution duration
							long duration = Math.max(1, tau - node.getStart()[0]);
							// schedule token 
							this.executive.scheduleTokenDuration(node, duration);
						}
						else {
							// wait - not ready for dispatching
							logger.debug("{Monitor} {tick: " + tick + " } {tau: " + tau + "} -> End conditions satisifed but node schedule not ready for ending\n"
									+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
						}
					}
					else 
					{
						// print a message in debug mode
						logger.debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> End execution conditions not satisfied yet\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
			}
			catch (ExecutionException ex) 
			{
				// token dispatching error 
				logger.error("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Error while propagating duration of controllable token\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n"
						+ "\t- message: " + ex.getMessage() + "\n");
			}
		}
	}
}
