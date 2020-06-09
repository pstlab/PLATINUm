package it.istc.pst.platinum.executive.monitor;

import java.util.List;

import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.NodeExecutionErrorException;
import it.istc.pst.platinum.executive.lang.ex.ObservationException;
import it.istc.pst.platinum.executive.lang.failure.DurationOverflow;
import it.istc.pst.platinum.executive.lang.failure.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.failure.PlatformError;
import it.istc.pst.platinum.executive.lang.failure.StartOverflow;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;

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
			throws ExecutionException, PlatformException
	{
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		// check received feedbacks
		List<ExecutionFeedback> feedbacks = this.getObservations();
		// manage uncontrollable tokens of the plan according to the feedbacks
		for (ExecutionFeedback feedback : feedbacks)
		{
			// get node 
			ExecutionNode node = feedback.getNode();
			// check execution result
			switch (feedback.getType())
			{
				case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
				case UNCONTROLLABLE_TOKEN_COMPLETE : 
				{
					// compute node duration of the token in execution 
					long duration = Math.max(1, tau - node.getStart()[0]);
					try
					{
						// schedule token duration
						this.executive.scheduleTokenDuration(node, duration);
						// update node state
						this.executive.updateNode(node,  ExecutionNodeStatus.EXECUTED);
						info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} -> Observed token execution with duration " + duration + " \n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						// consider the node as executed
						this.executive.updateNode(node, ExecutionNodeStatus.EXECUTED);
						// create failure cause
						ExecutionFailureCause cause = new DurationOverflow(tick, node, duration);
						// throw execution exception
						throw new ObservationException(
								"The observed duration of the token does not comply with the expected one:\n"
								+ "\t- duration: " + duration + "\n"
								+ "\t- node: " + node + "\n", 
								cause);
					}
				}
				break;
				
				case UNCONTROLLABLE_TOKEN_START : 
				{
					try
					{
						// schedule the start of uncontrollable token
						this.executive.scheduleUncontrollableTokenStart(node, tau);
						info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed token execution start at time " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						// the node can be considered as in execution
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// create execution cause
						ExecutionFailureCause cause = new StartOverflow(tick, node, tau);
						// throw execution exception
						throw new ObservationException(
								"The observed start time of the token does not comply with the expected one:\n"
								+ "\t- start: " + tau + "\n"
								+ "\t- node: " + node + "\n", 
								cause);
					}
				}
				break;
				
				case TOKEN_EXECUTION_FAILURE : {
					
					// update node status
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					// execution failure
					ExecutionFailureCause cause = new PlatformError(tick, node); 
					// throw execution exception
					throw new NodeExecutionErrorException(
							"Node execution error:\n\t- node: " + node + "\n", 
							cause);
				}
			}
		}
		
		// manage controllable tokens of the plan
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION))
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
					if (tau >= node.getEnd()[0]) 
					{
						// compute (controllable) execution duration
						long duration = Math.max(1, tau - node.getStart()[0]);
						// send stop signal to the platform
						this.executive.sendStopCommandSignalToPlatform(node);
						// set node as executed
						this.executive.updateNode(node, ExecutionNodeStatus.EXECUTED);
						
						// token scheduled
						info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Scheduling duration for controllable token\n"
								+ "\t- duration: " + duration + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					else 
					{
						// wait - not ready for dispatching
						debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> End conditions satisifed but node schedule not ready for ending\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
				else 
				{
					// print a message in debug mode
					debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> End execution conditions not satisfied yet\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void handleExecutionFailure(long tick, ExecutionFailureCause cause) 
			throws PlatformException
	{
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		// check received feedbacks
		List<ExecutionFeedback> feedbacks = this.getObservations();
		// manage uncontrollable tokens of the plan according to the feedbacks
		for (ExecutionFeedback feedback : feedbacks)
		{
			// get node 
			ExecutionNode node = feedback.getNode();
			// check node schedule
			this.executive.checkSchedule(node);
			// check execution result
			switch (feedback.getType())
			{
				case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
				case UNCONTROLLABLE_TOKEN_COMPLETE : 
				{
					// compute node duration of the token in execution 
					long duration = Math.max(1, tau - node.getStart()[0]);
					// the node can be considered as executed
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					// add repair information
					cause.addRepairInfo(node, duration);
					// info message
					info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} -> Observed token execution with duration " + duration + " \n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case UNCONTROLLABLE_TOKEN_START : 
				{
					// update node status
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed token execution start at time " + tau + "\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case TOKEN_EXECUTION_FAILURE : 
				{
					// the node can be considered as executed
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed execution failure at time " + tau + "\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
			}
		}
		
		// manage controllable tokens of the plan
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION))
		{
			// check node controllability 
			if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE))
			{
				// stop controllable tokens in execution
				// the node can be considered as executed
				this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
				// simply send stop command
				this.executive.sendStopCommandSignalToPlatform(node);
				// info message
				debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Stopping execution of controllable token\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
			}
		}
	}
}
