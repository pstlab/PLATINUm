package it.cnr.istc.pst.platinum.executive.dc;

import java.util.List;

import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;
import it.cnr.istc.pst.platinum.executive.dc.strategy.State;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Wait;
import it.cnr.istc.pst.platinum.ai.executive.dispatcher.Dispatcher;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.NodeObservationException;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.NodeStartOverflow;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;

/**
 * 
 */
public class DCDispatcher extends Dispatcher<DCExecutive>
{
	/**
	 * 
	 */
	protected DCDispatcher() {
		super();
	}
	
	
	/**
	 *
	 * 
	 */
	@Override
	public void handleTick(long tick) 
			throws ExecutionException, PlatformException {
		
		// get tau 
		long tau = this.executive.convertTickToTau(tick);
		// current status
		PlanExecutionStatus status = new PlanExecutionStatus(tau);
		// add the status of the timelines
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
			
			// check token id 
			String tokenId = node.getTimeline().toLowerCase() + "" + node.getId();
			// add timeline status
			status.addTimelineStatus(node.getTimeline().toLowerCase(), tokenId);
		}
		
		// print query 
		debug("[DCDispatcher] [tick: " + tick + "] DC checker query:\n"
				+ "\t- time= " + status.getTime() + "\n"
				+ "\t- status= " + status.getStatus() + "\n");
		
		// ask the strategy actions to perform
		System.out.println("checker:"+this.executive.checker);
		List<Action> result = this.executive.checker.askAllStrategySteps(
				status.getTime(), 
				status.getStatus(),
				true);
		
		// print the list of actions received
		debug("[DCDispatcher] [tick: " + tick + "] DC checker query result:\n"
				+ "\t- result= " + result + "\n");
			
		// check actions
		for (Action a : result) 
		{
			if (a instanceof Wait) 
			{
				/*
				 * wait
				 */
				
				debug("[DCDispatcher] [tick: " + tick +"] action: Wait");
				
			}
			else if (a instanceof Transition) 
			{
				// get transition
				Transition t = (Transition) a;
				
				// check token to dispatch
				State s = t.getTransitionTo();
				// name of the token
				String tokenName = s.getToken();
				// name of the timeline
				String tlName = s.getTimeline();
				// transition action
				debug("[DCDispatcher] [tick: " + tick + "] Transition action:\n"
						+ "- Source state of transition\n\t- timelineName= " + t.getTransitionFrom().getTimeline() + "\n\t- tokenName= " + t.getTransitionFrom().getToken() + "\n"
						+ "- Target state of transition\n\t- timelineName= " + tlName + "\n\t- tokenName= " + tokenName);

				// check nodes waiting for execution
				List<ExecutionNode> nodes = this.executive.getNodes(ExecutionNodeStatus.WAITING);
				// find the node to dispatch according to the transition
				ExecutionNode node = null;
				for (ExecutionNode n : nodes) 
				{
					// check token id 
					String tokenId = n.getTimeline().toLowerCase() + "" + n.getId();
					// check timeline and predicate
					if (n.getPredicate().getTimeline().equalsIgnoreCase(tlName) &&
							tokenId.equalsIgnoreCase(tokenName)) 
					{
						// node found
						node = n;
						debug("[DCDispatcher] [tick: " + tick + "] Transition node found:\n"
								+ "- target state of transition\n\t- timelineName= " + tlName + "\n\t- tokeNmae= " + tokenName + "\n"
								+ "- Execution node:\n\t- node= " + node + "\n");
						break;
					}
				}
				
				// check if a node has been found
				if (node != null) {
					
					try {
						
						// schedule token start time
						this.executive.scheduleTokenStart(node, tau);
						// check schedule
						this.executive.checkSchedule(node);
						// update the status of the previous node if any
						ExecutionNode prev = node.getPrev();
						if (prev != null) {
							
							// update status to executed
							this.executive.updateNode(prev, ExecutionNodeStatus.EXECUTED);
						}
						
						// start node execution
						info("[DCDispatcher] [tick: " + tick + "] [tau: " + tau + "] -> Start executing node at time: " + tau + "\n"
								+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						
						// set token as in execution to wait for feedbacks
						this.executive.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
						// create execution cause
						ExecutionFailureCause cause = new NodeStartOverflow(tick, node, tau);
						// throw execution exception
						throw new NodeObservationException(
								"The dispatched start time of the token does not comply with the plan:\n"
								+ "\t- start: " + tau + "\n"
								+ "\t- node: " + node + "\n", 
								cause);
					}
				}
				else {
					
					error("[DCDispatcher] [tick: " + tick + "] Transition token not found:\n- timelineName= " + tlName + "\n- tokenName= " + tokenName + "\n");
				}
				
			}
			else {
				// unknown action from DC strategy executive  
				throw new PlatformException("[DCDispatcher] [tick: " + tick +"] Unknown Action: " + a.getClass().getName() + "\n");
			}
		}
		
		
		/* 
		 * Finally for each executed action of the timeline check whether
		 * then next token has been dispatched (if any)
		 */
		
		
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.EXECUTED)) {
			
			// get next node
			ExecutionNode next = node.getNext();
			// check if node has a next token
			if (next != null) {
				
				// check next node of the associated timeline
				if (next.getStatus().equals(ExecutionNodeStatus.WAITING)) {
					
					// dispatch start time
					debug("[DCMonitor] [tick: " + tick + "][{tau: " +  tau + "] -> Dispatching start time of the next token\n"
							+ "\t- start-tau: " + tau + "\n"
							+ "\t- node: " + next.getGroundSignature() + " (" + next + ")\n");
					
					try {
						
						// dispatch start time 
						this.executive.scheduleTokenStart(next, tau);
						// check schedule
						this.executive.checkSchedule(next);
						// dispatch start time
						debug("[DCMonitor] [tick: " + tick + "][{tau: " +  tau + "] -> Dispatching start time of the next token\n"
								+ "\t- start-tau: " + tau + "\n"
								+ "\t- node: " + next.getGroundSignature() + " (" + next + ")\n");
					}
					catch (TemporalConstraintPropagationException ex) {
						// set token as in failure
						this.executive.updateNode(node, ExecutionNodeStatus.FAILURE);
						// create execution failure message
						ExecutionFailureCause cause = new NodeStartOverflow(tick, node, tau);
						// throw execution exception
						throw new ExecutionException(
								"Start time does not comply with the expected one:\n"
								+ "\t- start: " + tau + "\n"
								+ "\t- node: " + next + "\n", 
								cause);
					}
				}
			}
		}
	}
}
