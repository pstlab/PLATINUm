package it.cnr.istc.pst.platinum.ai.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import it.cnr.istc.pst.platinum.ai.executive.dispatcher.ConditionCheckingDispatcher;
import it.cnr.istc.pst.platinum.ai.executive.dispatcher.Dispatcher;
import it.cnr.istc.pst.platinum.ai.executive.lang.ExecutionFeedback;
import it.cnr.istc.pst.platinum.ai.executive.lang.ExecutionFeedbackType;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.monitor.ConditionCheckingMonitor;
import it.cnr.istc.pst.platinum.ai.executive.monitor.Monitor;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ControllabilityType;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutivePlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.DispatcherPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.ExecutivePlanDataBasePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.MonitorPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;
import it.cnr.istc.pst.platinum.ai.framework.utils.view.executive.ExecutiveWindow;
import it.cnr.istc.pst.platinum.control.lang.Goal;
import it.cnr.istc.pst.platinum.control.lang.PlatformCommand;
import it.cnr.istc.pst.platinum.control.lang.PlatformFeedback;
import it.cnr.istc.pst.platinum.control.lang.PlatformObservation;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;
import it.cnr.istc.pst.platinum.control.platform.PlatformObserver;
import it.cnr.istc.pst.platinum.control.platform.PlatformProxy;

/**
 * 
 * @author anacleto
 *
 */
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.WARNING
)
@MonitorConfiguration(
		monitor = ConditionCheckingMonitor.class
)
@DispatcherConfiguration(
		dispatcher = ConditionCheckingDispatcher.class
)
public class Executive extends FrameworkObject implements ExecutionManager, PlatformObserver
{
	@ExecutivePlanDataBasePlaceholder
	protected ExecutivePlanDataBase pdb;										// the (executive) plan to execute
	
	@MonitorPlaceholder
	protected Monitor<?> monitor;												// plan monitor
	
	@DispatcherPlaceholder
	protected Dispatcher<?> dispatcher;											// dispatching process
	
	
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private static final String DISPLAY_PLAN_PROPERTY = "display_plan";			// property specifying the display plan flag
	private FilePropertyReader properties;										// configuration property file
	
	private ExecutionStatus status;												// executive's operating status
	private final Object lock;													// executive's status lock
	private ClockManager clock;													// execution clock controller
	private long currentTick;													// current tick
	
	private ExecutiveWindow window;												// executive window
	private Map<PlatformCommand, ExecutionNode> dispatchedIndex;				// keep track of dispatched nodes
	private AtomicBoolean failure;												// execution failure flag
	private ExecutionFailureCause cause;										// execution failure cause
	
	private PlatformProxy platformProxy;										// platform PROXY to send commands to

	
	
	/**
	 * 
	 */
	protected Executive() 
	{
		super();
		// get executive file properties
		this.properties = new FilePropertyReader(
			FRAMEWORK_HOME + FilePropertyReader.DEFAULT_EXECUTIVE_PROPERTY);
		// set clock and initial status
		this.lock = new Object();
		// set status
		this.status = ExecutionStatus.INACTIVE;
		// set clock manager
		this.clock = new AtomicClockManager(this);
		// set the PROXY and the observer
		this.platformProxy = null;
		// set failure flag
		this.failure = new AtomicBoolean(false);
		
		// check plan display property
		if (this.getProperty(DISPLAY_PLAN_PROPERTY).equals("1")) {
			// create executive window
			this.window = new ExecutiveWindow("Executive Window");
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String getProperty(String property) {
		return this.properties.getProperty(property);
	}

	/**
	 * 
	 * @param proxy
	 */
	public synchronized void link(PlatformProxy proxy) 
	{
		// check if already set
		if (this.platformProxy == null) {
			// bind the executive
			this.platformProxy = proxy;
			// register to the PROXY
			this.platformProxy.register(this);
		}
		else {
			warning("Platform proxy already set. Do unlink before setting another platform proxy");
		}
	}
	
	/**
	 * 
	 */
	public synchronized void unlink() {
		// unlink form simulator
		if (this.platformProxy != null) {
			// unregister
			this.platformProxy.unregister(this);
			// clear data
			this.platformProxy = null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.pdb.getHorizon();
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionStatus getStatus() {
		return this.status;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean canEnd(ExecutionNode node) {
		return this.pdb.checkEndExecutionDependencies(node);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean canStart(ExecutionNode node) {
		return this.pdb.checkStartExecutionDependencies(node);
	}
	
	/**
	 * Convert clock's tick to time units from execution start
	 * 
	 * @param tick
	 * @return
	 */
	public long convertTickToTau(long tick) 
	{
		// covert tick to seconds from the execution start
		double seconds = this.clock.convertClockTickToSeconds(tick);
		// get property to convert seconds to time units
		double converter = Double.parseDouble(this.properties.getProperty(TIME_UNIT_PROPERTY));
		// convert seconds to time units
		return Math.round(seconds / converter);
	}
	
	/**
	 * 
	 * @param tick
	 * @return
	 */
	public double convertClockTickToSeconds(long tick) {
		return this.clock.convertClockTickToSeconds(tick);
	}
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public long getTau() 
			throws InterruptedException 
	{
		// current tick
		long tick = this.clock.getCurrentTick();
		// cover to tau
		return this.convertTickToTau(tick);
	}
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public long getTick() 
			throws InterruptedException 
	{
		// return current tick
		return this.clock.getCurrentTick();
	}
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public List<ExecutionNode> getNodes(ExecutionNodeStatus status) {
		return this.pdb.getNodesByStatus(status);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ExecutionNode> getNodes() {
		// list of nodes
		List<ExecutionNode> list = new ArrayList<>();
		for (ExecutionNodeStatus status : ExecutionNodeStatus.values()) {
			// skip failed nodes
			if (!status.equals(ExecutionNodeStatus.FAILURE)) {
				// add all nodes with current status
				list.addAll(this.getNodes(status));
			}
		}
		
		// sort node list
		Collections.sort(list);
		// get sorted list
		return list;
	}
	
	/**
	 * 
	 * @param node
	 * @param s
	 */
	public void updateNode(ExecutionNode node, ExecutionNodeStatus s) {
		this.pdb.updateNodeStatus(node, s);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void checkSchedule(ExecutionNode node) {
		this.pdb.checkSchedule(node);
	}
	
	/***
	 * 
	 * @param node
	 * @param start
	 * @throws TemporalConstraintPropagationException
	 * @throws PlatformException
	 */
	public void scheduleTokenStart(ExecutionNode node, long start) 
			throws TemporalConstraintPropagationException, PlatformException 
	{
		// check controllability type
		ControllabilityType type = node.getControllabilityType();
		switch (type)
		{
			// schedule uncontrollable token
			case UNCONTROLLABLE : 
			{
				// simply set the proper state - no propagation is needed in this case
				this.updateNode(node, ExecutionNodeStatus.STARTING);
			}
			break;
		
			case PARTIALLY_CONTROLLABLE : 
			case CONTROLLABLE : 
			{
				// actually schedule the start time of the token
				this.pdb.scheduleStartTime(node, start);
				// update node status
				this.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
		}
		
		
		// dispatch the command through the executive if needed
		this.sendStartCommandSignalToPlatform(node);
	}
	
	/**
	 * 
	 * @param node
	 * @param start
	 * @throws ExecutionException
	 */
	public void scheduleUncontrollableTokenStart(ExecutionNode node, long start) 
			throws TemporalConstraintPropagationException
	{
		// schedule the observed start time of the token
		this.pdb.scheduleStartTime(node, start);
		// update node status
		this.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
	}
	
	/**
	 * 
	 * @param node
	 * @param duration
	 * @throws TemporalConstraintPropagationException
	 * @throws PlatformException
	 */
	public void scheduleTokenDuration(ExecutionNode node, long duration) 
			throws TemporalConstraintPropagationException, PlatformException
	{
		// propagate scheduled duration time
		this.pdb.scheduleDuration(node, duration);
		// the node can be considered as executed
		this.updateNode(node, ExecutionNodeStatus.EXECUTED);
		// if controllable send a stop command
		if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE)) {
			// send stop signal to the platform
			this.sendStopCommandSignalToPlatform(node);
		}
	}
	
	/**
	 * This method sets an executive system on a generated plan.
	 * 
	 * It builds the plan data-based related to the generated plan and sets
	 * the clock, the dispatcher and the monitor processes.
	 * 
	 * @param plan
	 */
	public final void initialize(PlanProtocolDescriptor plan) 
			throws InterruptedException
	{
		// check status
		synchronized (this.lock) {
			while (!this.status.equals(ExecutionStatus.INACTIVE)) {
				this.lock.wait();
			}
			
			// change status and send a signal
			this.status = ExecutionStatus.INITIALIZING;
			this.lock.notifyAll();
		}
		
		
		// set plan data-base
		this.pdb.setup(plan);
		
		// set complete
		synchronized (this.lock) {
			// update status and send a signal
			this.status = ExecutionStatus.READY;
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public final boolean execute() 
			throws Exception {
		// call executive starting at tick 0
		return this.execute(0, null);
	}
	
	/**
	 * Blocking method which start the execution of the plan and waits for completion.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final boolean execute(long startTick, Goal goal) 
			throws Exception
	{
		// check status
		synchronized (this.lock) 
		{
			// check lock condition
			while (!this.status.equals(ExecutionStatus.READY)) {
				this.status.wait();
			}
			
			// change status and send signal
			this.status = ExecutionStatus.EXECUTING;
			this.lock.notifyAll();
		}
		
		// set dispatching index
		this.dispatchedIndex = new ConcurrentHashMap<>();
		// start clock
		this.clock.start(startTick);
		// wait execution completes
		this.clock.join();
		
		
		// check execution failure or not 
		if (this.failure.get()) {
			// execution failure
			error("Execution failure:\n\t- tick: " + this.cause.getInterruptionTick() +"\n"
					+ "\t- cause: " + this.cause.getType() + "\n");
			
			// update executive status
			synchronized (this.lock) {
				// set error state
				this.status = ExecutionStatus.ERROR;
				// send signal 
				this.lock.notifyAll();
			}
		}
		else 
		{
			// successful execution 
			info("Execution successfully complete:\n\t- tick: " + this.currentTick + "\n");
			
			// update executive status
			synchronized (this.lock) {
				// set inactive status
				this.status = ExecutionStatus.INACTIVE;
				// send signal 
				this.lock.notifyAll();
			}
		}
			
		// clear monitor and dispatcher
		this.monitor.clear();
		this.dispatcher.clear();
		// return execution result
		return !this.failure.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCause getFailureCause() {
		return this.cause;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFailure() {
		return this.failure.get();
	}
	
	/**
	 * 
	 * @param tick
	 * @return
	 */
	@Override
	public boolean onTick(long tick)
	{
		// execution completion flag
		boolean complete = false;
		try 
		{
			// check failure flag
			if (!this.failure.get()) {
				// handle current tick
				this.currentTick = tick;
				debug("{Executive} -> Handle tick: " + tick + "\n");
				// synch step
				debug("{Executive} {tick: " + tick + "} -> Synchronization step\n");
				this.monitor.handleTick(tick);
				// dispatching step
				debug("{Executive} {tick: " + tick + "} -> Dispatching step\n");
				this.dispatcher.handleTick(tick);
				
				// check if execution is complete
				complete = this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING).isEmpty() &&
						this.pdb.getNodesByStatus(ExecutionNodeStatus.STARTING).isEmpty() && 
						this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
			}
			else 
			{
				// handle current tick
				this.currentTick = tick;
				warning("{Executive} -> [FAILURE] Handle tick: " + tick + "\n");
				// sync step only in "failure" mode
				warning("{Executive} {tick: " + tick + "} -> [FAILURE] Synchronization step\n");
				// handle observations
				this.monitor.handleExecutionFailure(tick, this.cause);
				
				// hypothesis
				complete = true;
				// get nodes in starting state
				for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.STARTING)) {
					// the executive cannot complete 
					complete = false;
					// waiting for a feedback of the node 
					warning("{Executive} {tick: " + tick + "} -> [FAILURE] Terminating execution... waiting for feedback about dispatched starting command request :\n"
							+ "\t- node: " + node + "\n");
				}
				
				// get nodes in execution 
				for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION)) {
					// the executive cannot complete 
					complete = false;
					// waiting for a feedback of the node 
					warning("{Executive} {tick: " + tick + "} -> [FAILURE] Terminating execution... waiting for feedback about dispatched command :\n"
							+ "\t- node: " + node + "\n");
				}
			}
			
			// get tau
			long tau = this.convertTickToTau(tick);
			// display executive window
			this.displayWindow(tau);
		}
		catch (ExecutionException ex) 
		{
			// set execution failure flag
			this.failure.set(true);
			// do not complete execution to wait for pending signals
			complete = false;
			// set execution failure cause
			this.cause = ex.getFailureCause();
			// error message
			error("{Executive} {tick: " + tick + "} -> Error while executing plan:\n"
					+ "\t- message: " + ex.getMessage() + "\n\n"
					+ "Wait for execution feedbacks of pending controllable and partially-controllable tokens if any... \n\n");
		}
		catch (PlatformException ex) 
		{
			// set failure
			this.failure.set(true);
			// complete execution in this case
			complete = true;
			// error message
			error("{Executive} {tick: " + tick + "} -> Platform error:\n"
					+ "\t- message: " + ex.getMessage() + "\n");
		}
		catch (InterruptedException ex) {
			// execution error
			error(ex.getMessage());
			// set execution failure 
			this.failure.set(true);
			// complete execution in this case
			complete = true;
		}

		// get boolean flag
		return complete;
	} 
	
	/**
	 * 
	 * @param tau
	 * @throws InterruptedException
	 */
	private void displayWindow(long tau) 
			throws InterruptedException 
	{
		// check property
		if (this.getProperty(DISPLAY_PLAN_PROPERTY).equals("1")) {
			// set the data-set to show
			this.window.setDataSet(this.pdb.getHorizon(), this.getNodes());
			// display current execution state
			this.window.display(tau);
		}
	}

	/**
	 * Perform some setting operation just before starting execution
	 */
	protected void doPrepareExecution() 
	{
		/*
		 * TODO : set some parametric procedure
		 */
	}
	
	/**
	 * 
	 * @param goal
	 */
	protected void doPrepareExecution(Goal goal) //EDIT POINTER
	{
		/*
		 * TODO : set some parametric procedure
		 */
	}
	
	/**
	 * 
	 * @param node
	 * @throws PlatformException
	 */
	public void sendStopCommandSignalToPlatform(ExecutionNode node) 
			throws PlatformException
	{
		if (this.platformProxy != null && this.platformProxy.isPlatformCommand(node)) {
			// also send stop command execution request
			this.platformProxy.stopNode(node);
		}
	}
	
	/**
	 * 
	 * @param node
	 * @throws PlatformException
	 */
	public void sendStartCommandSignalToPlatform(ExecutionNode node) 
			throws PlatformException
	{
		// check if a platform PROXY exists
		if (this.platformProxy != null) 
		{
			// check controllability type 
			if (node.getControllabilityType().equals(ControllabilityType.PARTIALLY_CONTROLLABLE) || 
					node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE))
			{
				// check if command to execute on platform
				if (this.platformProxy.isPlatformCommand(node)) {
	 				// send command and take operation ID
					PlatformCommand cmd = this.platformProxy.executeNode(node);
					// add entry to the index
					this.dispatchedIndex.put(cmd, node);
				}
			}
			else
			{
				// check if command to execute on platform
				if (this.platformProxy.isPlatformCommand(node)) {
					// require execution start
					PlatformCommand cmd = this.platformProxy.startNode(node);
					// add entry to the index
					this.dispatchedIndex.put(cmd, node);
				}
			}
		}
		else {
			
			// nothing to do, no platform PROXY available
		}
	}
	
	/**
	 * Action execution feedback callback
	 */
	@Override
	public void feedback(PlatformFeedback feedback) 
	{
		// check feedback type
		switch (feedback.getType())
		{
			// successful action execution
			case SUCCESS : { 
				// handle command positive feedback
				this.success(feedback.getCmd());
			}
			break;
			
			// action execution failure
			case FAILURE : {
				// handle failure
				this.failure(feedback.getCmd());
			}
			break;
			
			case UNKNOWN : {
				// runtime exception
				throw new RuntimeException("Received UNKNOWN feedback type:\n- cmd: " + feedback.getCmd());
			}
			
		}
	}
	
	/**
	 * Handle general observations from the environment
	 */
	@Override
	public void observation(PlatformObservation<? extends Object> obs) 
	{
		/* 
		 * TODO Auto-generated method stub
		 */
	}
	
	/**
	 * 
	 * @param cmd
	 */
	private void success(PlatformCommand cmd) 
	{
		// check command  
		if (this.dispatchedIndex.containsKey(cmd))
		{
			// get execution node
			ExecutionNode node = this.dispatchedIndex.get(cmd);
			// check node current status
			if (node.getStatus().equals(ExecutionNodeStatus.STARTING)) 
			{
				// create execution feedback
				ExecutionFeedback feedback = new ExecutionFeedback(
						this.currentTick,
						node, 
						ExecutionFeedbackType.UNCONTROLLABLE_TOKEN_START);
				// forward the feedback to the monitor
				this.monitor.addExecutionFeedback(feedback);
				// got start execution feedback from a completely uncontrollable token
				info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the start of the execution of an uncontrollable token:\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
			}
			else if (node.getStatus().equals(ExecutionNodeStatus.IN_EXECUTION))
			{
				// create execution feedback
				ExecutionFeedback feedback = new ExecutionFeedback(
						this.currentTick,
						node, 
						node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE) ? 
								ExecutionFeedbackType.UNCONTROLLABLE_TOKEN_COMPLETE : 
								ExecutionFeedbackType.PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE);
				// forward feedback to the monitor
				this.monitor.addExecutionFeedback(feedback);
				// remove operation ID from index
				this.dispatchedIndex.remove(cmd);
				// got end execution feedback from either a partially-controllable or uncontrollable token
				info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the end of the execution of either a partially-controllable or uncontrollable token:\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
			}
			else 
			{
				// nothing to do
			}
			
		}
		else 
		{
			// no operation ID found 
			warning("{Executive} {tick: " + this.currentTick + "} -> Receiving feedback about an unknown operation:\n\t- cmd: " + cmd + "\n\t-data: " + cmd.getData() + "\n");
		}
	}
	

	/**
	 * 
	 * @param cmd
	 */
	private void failure(PlatformCommand cmd) 
	{
		// check command  
		if (this.dispatchedIndex.containsKey(cmd))
		{
			// get execution node
			ExecutionNode node = this.dispatchedIndex.get(cmd);
			// create execution feedback
			ExecutionFeedback feedback = new ExecutionFeedback(
					this.currentTick,
					node, 
					ExecutionFeedbackType.TOKEN_EXECUTION_FAILURE);
				
			// forward feedback to the monitor
			this.monitor.addExecutionFeedback(feedback);
			// remove operation ID from index
			this.dispatchedIndex.remove(cmd);
			// got end execution feedback from either a partially-controllable or uncontrollable token
			info("{Executive} {tick: " + this.currentTick + "} -> Got \"failure\" feedback about the execution of token:\n"
					+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
		}
		else 
		{
			// no operation ID found 
			warning("{Executive} {tick: " + this.currentTick + "} -> Receiving feedback about an unknown operation:\n\t- cmd: " + cmd + "\n\t-data: " + cmd.getData() + "\n");
		}
	}
}
