package it.cnr.istc.pst.platinum.executive.dc;

import it.cnr.istc.pst.platinum.ai.executive.lang.ExecutionFeedback;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.NodeExecutionErrorException;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.NodeObservationException;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.NodeDurationOverflow;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.NodeExecutionError;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.NodeStartOverflow;
import it.cnr.istc.pst.platinum.ai.executive.monitor.Monitor;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ControllabilityType;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

/**
 * 
 * @author alessandro
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
			throws ExecutionException, PlatformException {
		
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		// check received observations
		while (this.hasObservations()) {
			
			// get next feedback
			ExecutionFeedback feedback = this.next();
			// get node 
			ExecutionNode node = feedback.getNode();
			// execution feedback
			debug("[DCMonitor] [tick: " + tick + "][{tau: " +  tau + "] -> Execution feedback:\n"
					+ "\t- type: " + feedback.getType() + "\n"
					+ "\t- node: " + feedback.getNode() + "\n");
			
			
			// check feedback type
			switch (feedback.getType()) {
			
				case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
				case UNCONTROLLABLE_TOKEN_COMPLETE : 
				{
					// compute node duration of the token in execution 
					long duration = Math.max(1, tau - node.getStart()[0]);
					try {
						
						// schedule token duration
						this.executive.scheduleTokenDuration(node, duration);
						// update node state
						this.executive.updateNode(node, ExecutionNodeStatus.EXECUTED);
						info("[DCMonitor] [tick: " + tick + "] [tau: " +  tau + "] -> Observed token execution with duration " + duration + " \n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						
						// set token as in failure
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// create execution failure message
						ExecutionFailureCause cause = new NodeDurationOverflow(tick, node, tau);
						// throw execution exception
						throw new NodeObservationException(
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
						info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> Observed token execution start at time " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						
						// the node can be considered as in execution
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// create execution cause
						ExecutionFailureCause cause = new NodeStartOverflow(tick, node, tau);
						// throw execution exception
						throw new NodeObservationException(
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
					ExecutionFailureCause cause = new NodeExecutionError(tick, node); 
					// throw execution exception
					throw new NodeExecutionErrorException(
							"Node execution error:\n\t- node: " + node + "\n", 
							cause);
				}
			}
		}
		
		// manage controllable tokens of the plan
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
			
			// check node controllability 
			if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE)) {
				
				// check end conditions
				if (this.executive.canEnd(node)) {
					
					// check node schedule
					this.executive.checkSchedule(node);
					// check expected schedule
					if (tau >= node.getEnd()[0]) {
						
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
					else {
						
						// wait - not ready for dispatching
						debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} -> End conditions satisifed but node schedule not ready for ending\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
				else {
					
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
			throws PlatformException {
		
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		// manage uncontrollable tokens of the plan according to the feedbacks
		while (this.hasObservations()) {
			
			// get next observation
			ExecutionFeedback feedback = this.next();
			// get node 
			ExecutionNode node = feedback.getNode();
			// check node schedule
			this.executive.checkSchedule(node);
			// check execution result
			switch (feedback.getType()) {
			
				case PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE : 
				case UNCONTROLLABLE_TOKEN_COMPLETE : {
					
					// compute node duration of the token in execution 
					long duration = Math.max(1, tau - node.getStart()[0]);
					// the node can be considered as executed
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					// add repair information
					cause.addRepairInfo(node, duration);
					// info message
					info("{Monitor} {tick: " + tick + "} {tau: " +  tau + "} {FAILURE-HANDLING} -> Observed token execution with duration " + duration + " \n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case UNCONTROLLABLE_TOKEN_START : {
		
					// update node status
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} {FAILURE-HANDLING} -> Observed token execution start at time " + tau + "\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
				break;
				
				case TOKEN_EXECUTION_FAILURE : {
					
					// the node can be considered as executed
					this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
					info("{Monitor} {tick: " + tick + "} {tau: " + tau + "} {FAILURE-HANDLING} -> Observed execution failure at time " + tau + "\n"
							+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				}
			}
		}
		
		// manage controllable tokens of the plan
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
			
			// check node controllability 
			if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE)) {
				
				// check end conditions
				if (this.executive.canEnd(node)) {
					
					// check schedule
					this.executive.checkSchedule(node);
					// check expected schedule
					if (tau >= node.getEnd()[0]) {
						
						// the node can be considered as executed
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// send stop command
						this.executive.sendStopCommandSignalToPlatform(node);
						// info message
						debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} {FAILURE-HANDLING} -> Stopping execution of controllable token\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
						
					} else {
						
						// info message
						debug("{Monitor} {tick: " + tick + "} {tau: " + tau + "} {FAILURE-HANDLING} -> Waiting stop condition for controllable token\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
				}
			}
		}
	}
}
