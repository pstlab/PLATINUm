package it.istc.pst.platinum.executive.dc;

import java.util.List;

import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.DurationOverflow;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.ex.ObservationException;
import it.istc.pst.platinum.executive.lang.ex.StartOverflow;
import it.istc.pst.platinum.executive.monitor.Monitor;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;

/**
 * 
 * @author anacleto
 *
 */
public class DCMonitor extends Monitor<DCExecutive> 
{
	/**
	 * 
	 * @param exec
	 */
	protected DCMonitor() {
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
			logger.debug("[DCMonitor] [tick: " + tick + "][{tau: " +  tau + "] -> Execution feedback:\n"
					+ "\t- type: " + feedback.getType() + "\n"
					+ "\t- node: " + feedback.getNode() + "\n");	
			// get node 
			ExecutionNode node = feedback.getNode();
			// check execution result
			switch (feedback.getType())
			{
				case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
				case UNCONTROLLABLE_TOKEN_COMPLETE : 
				{
					// set next node in execution if any
//					ExecutionNode next = node.getNext();
					// compute node duration of the token in execution 
					long duration = Math.max(1, tau - node.getStart()[0]);
					logger.info("[DCMonitor] [tick: " + tick + "] [tau: " +  tau + "] -> Observed token execution with duration " + duration + " \n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					
					try 
					{
						// update node state
						this.executive.updateNode(node, ExecutionNodeStatus.EXECUTED);
						// schedule token duration
						this.executive.scheduleTokenDuration(node, duration);
						// check schedule
						this.executive.checkSchedule(node);
						logger.info("[DCMonitor] [tick: " + tick + "] [tau: " +  tau + "] -> Observed token execution with duration " + duration + " \n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						// set token as in failure
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// create execution failure message
						ExecutionFailureCause cause = new DurationOverflow(tick, node, tau);
						// throw execution exception
						throw new ObservationException(
								"Observed duration time does not comply with the expected one:\n"
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
