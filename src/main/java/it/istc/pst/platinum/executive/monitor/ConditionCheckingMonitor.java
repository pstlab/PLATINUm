package it.istc.pst.platinum.executive.monitor;

import java.util.List;

import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.ex.ObservationException;
import it.istc.pst.platinum.executive.lang.ex.StartOverflow;
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
					this.executive.updateNode(node,  ExecutionNodeStatus.EXECUTED);
					logger.info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} -> Observed token execution with duration " + duration + " \n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case UNCONTROLLABLE_TOKEN_START : 
				{
					try
					{
						// schedule the start of uncontrollable token
						this.executive.scheduleUncontrollableTokenStart(node, tau);
						logger.info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed token execution start at time " + tau + "\n"
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
					
					/*
					 * TODO : Manage execution failure from the functional level
					 */
					throw new RuntimeException("TODO: Manage token execution error");
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
					if (tau >= node.getEnd()[0]) // && tau <= node.getEnd()[1]) 
					{
						// compute (controllable) execution duration
						long duration = Math.max(1, tau - node.getStart()[0]);
						// send stop signal to the platform
						this.executive.sendStopCommandSignalToPlatform(node);
						// set node as executed
						this.executive.updateNode(node, ExecutionNodeStatus.EXECUTED);
						// token scheduled
						logger.info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Scheduling duration for controllable token\n"
								+ "\t- duration: " + duration + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					else 
					{
						// wait - not ready for dispatching
						logger.debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> End conditions satisifed but node schedule not ready for ending\n"
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
					logger.info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} -> Observed token execution with duration " + duration + " \n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case UNCONTROLLABLE_TOKEN_START : 
				{
					/*
					 * TODO : GESTIRE ANCHE QUESTE INFORMAZIONI NEL PLAN REPAIR ?
					 * 
					 */
					
					// update node status
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					logger.info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed token execution start at time " + tau + "\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case TOKEN_EXECUTION_FAILURE : {
					
					/*
					 * TODO : Manage execution failure from the functional level
					 */
					throw new RuntimeException("TODO: Manage token execution error");
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
					// check expected schedule
					if (tau >= node.getEnd()[0] && tau <= node.getEnd()[1]) 
					{
						// compute (controllable) execution duration
						long duration = Math.max(1, tau - node.getStart()[0]);
						// the node can be considered as executed
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// simply send stop command
						this.executive.sendStopCommandSignalToPlatform(node);
						// add repair information
						cause.addRepairInfo(node, duration);
						// info message
						logger.info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Scheduling duration for controllable token\n"
								+ "\t- duration: " + duration + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
			}
		}
	}
}
