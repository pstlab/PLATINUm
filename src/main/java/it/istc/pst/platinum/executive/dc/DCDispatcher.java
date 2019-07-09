package it.istc.pst.platinum.executive.dc;

import java.util.List;

import it.istc.pst.platinum.executive.dc.strategy.State;
import it.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.istc.pst.platinum.executive.dc.strategy.result.Wait;
import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
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
	{
		// get tau 
		long tau = this.executive.convertTickToTau(tick);
		// current status
		PlanExecutionStatus status = new PlanExecutionStatus(tau);
		// add the status of the timelines
		for (ExecutionNode node : this.executive.getNodes(ExecutionNodeStatus.IN_EXECUTION)) {
			status.addTimelineStatus(node);
		}
		
		
		try 
		{
			// ask the strategy actions to perform
			List<Action> result = this.executive.checker.askAllStrategySteps(
					status.getTime(), 
					status.getStatus(),
					true);
			
			// print the list of actions received
			logger.debug("[Dispatcher] [tick: " + tick + "] DC checker query result:\n"
					+ "\t- result= " + result + "\n");
				
			// check actions
			for (Action a : result) 
			{
				if (a instanceof Wait) 
				{
					/*
					 * wait
					 */
					
					logger.debug("[Dispatching] [tick: " + tick +"] action: Wait");
					
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
					
					// get node >>>>> TODO
					ExecutionNode node = null;
					
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
						// set token as in execution to wait for feedbacks
						this.executive.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
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
				else {
					
					throw new Exception("Unknown Action: " + a.getClass().getName() + "\n");
				}
			}
		}
		catch (Exception ex) {
			logger.error("Error while asking actions to the strategy manager\n\t- message: " + ex.getMessage() + "\n");
		}

	}
}
