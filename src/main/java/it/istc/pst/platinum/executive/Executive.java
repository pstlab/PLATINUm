package it.istc.pst.platinum.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import it.istc.pst.platinum.control.platform.PlatformObserver;
import it.istc.pst.platinum.control.platform.lang.PlatformCommand;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.executive.dispatcher.ConditionCheckingDispatcher;
import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ExecutionFeedbackType;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.ex.PlatformError;
import it.istc.pst.platinum.executive.monitor.ConditionCheckingMonitor;
import it.istc.pst.platinum.executive.monitor.Monitor;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.executive.pdb.ExecutivePlanDataBase;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.DispatcherPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.MonitorPlaceholder;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;
import it.istc.pst.platinum.framework.utils.view.executive.ExecutiveWindow;

/**
 * 
 * @author anacleto
 *
 */
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.INFO
)
@MonitorConfiguration(
		monitor = ConditionCheckingMonitor.class
)
@DispatcherConfiguration(
		dispatcher = ConditionCheckingDispatcher.class
)
public class Executive extends ExecutiveObject implements ExecutionManager, PlatformObserver
{
	@ExecutivePlanDataBasePlaceholder
	protected ExecutivePlanDataBase pdb;										// the (executive) plan to execute
	
	@MonitorPlaceholder
	protected Monitor<?> monitor;												// plan monitor
	
	@DispatcherPlaceholder
	protected Dispatcher<?> dispatcher;											// dispatching process
	
	
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private static final String DISPLAY_PLAN_PROPERTY = "display_plan";			// property specifying the display plan flag
	private FilePropertyReader config;											// configuration property file
	
	private ExecutionStatus status;												// executive's operating status
	private final Object lock;													// executive's status lock
	private ClockManager clock;													// execution clock controller
	private long currentTick;															// current tick
	
	private ExecutiveWindow window;												// executive window
	private Map<PlatformCommand, ExecutionNode> dispatchedIndex;				// keep track of dispatched nodes
	private AtomicBoolean failure;												// execution failure flag
	private ExecutionFailureCause cause;										// execution failure cause
	
	private PlatformSimulator platformProxy;									// platform PROXY to send commands to 
	
	/**
	 * 
	 */
	protected Executive() {
		super();
		// get executive file properties
		this.config = FilePropertyReader.getExecutivePropertyFile();
		// set clock and initial status
		this.lock = new Object();
		this.status = ExecutionStatus.INACTIVE;
		// initialize clock manager
		this.clock = new AtomicClockManager(this);
		// initialize the PROXY and the observer
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
		return this.config.getProperty(property);
	}

	/**
	 * 
	 * @param proxy
	 */
	public void link(PlatformSimulator proxy) {
		// bind the executive
		this.platformProxy = proxy;
		// register to the PROXY
		this.platformProxy.register(this);
	}
	
	/***
	 * 
	 */
	public void unlink() {
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
		double converter = Double.parseDouble(this.config.getProperty(TIME_UNIT_PROPERTY));
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
			throws InterruptedException {
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
			throws InterruptedException {
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
		List<ExecutionNode> list = new ArrayList<>();
		list.addAll(this.getNodes(ExecutionNodeStatus.WAITING));
		list.addAll(this.getNodes(ExecutionNodeStatus.STARTING));
		list.addAll(this.getNodes(ExecutionNodeStatus.IN_EXECUTION));
		list.addAll(this.getNodes(ExecutionNodeStatus.EXECUTED));
		Collections.sort(list);
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
	
	/**
	 * 
	 * @param node
	 * @param start
	 * @throws TemporalConstraintPropagationException
	 */
	public void scheduleTokenStart(ExecutionNode node, long start) 
			throws TemporalConstraintPropagationException 
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
				// check if virtual node
				if (!node.isVirtual()) {
					// actually schedule the start time of the token
					this.pdb.scheduleStartTime(node, start);
				}
				
				// update node status
				this.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
		}
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
		// check if not virtual
		if (!node.isVirtual()) {
			// propagate scheduled duration time
			this.pdb.scheduleDuration(node, duration);
		}
		
		// the node can be considered as executed
		this.updateNode(node, ExecutionNodeStatus.EXECUTED);
		// if controllable send a stop command
		if (node.getControllabilityType().equals(ControllabilityType.CONTROLLABLE)) {
			// send stop signal to the platform
			this.sendStopCommandSignalToPlatform(node);
		}
	}
	
	/**
	 * This method allows to initialize an executive system on a generated plan.
	 * 
	 * It builds the plan data-based related to the generated plan and initializes
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
		
		
		// initialize plan data-base
		this.pdb.setup(plan);
		
		// initialization complete
		synchronized (this.lock) {
			// update status and send a signal
			this.status = ExecutionStatus.READY;
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public final boolean execute() 
			throws InterruptedException {
		// call executive starting at tick 0
		return this.execute(0);
	}
	
	/**
	 * Blocking method which start the execution of the plan and waits for completion.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public final boolean execute(long startTick) 
			throws InterruptedException
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
		
		// perform setting operations before execution
		this.doPrepareExecution();
		
		// initialize dispatching index
		this.dispatchedIndex = new ConcurrentHashMap<>();
		// start clock
		this.clock.start(startTick);
		// wait execution completes
		this.clock.join();
			
		
		// check execution failure or not 
		if (this.failure.get()) {
			// execution failure
			logger.error("Execution failure:\n\t- tick: " + this.cause.getInterruptionTick() +"\n"
					+ "\t- cause: " + this.cause.getType() + "\n"
					+ "\t- node: " + this.cause.getInterruptNode() + "\n");
			
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
			logger.info("Execution successfully complete:\n\t- tick: " + this.currentTick + "\n");
			
			// update executive status
			synchronized (this.lock) {
				// set inactive status
				this.status = ExecutionStatus.INACTIVE;
				// send signal 
				this.lock.notifyAll();
			}
		}
			
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
				logger.debug("{Executive} -> Handle tick: " + tick + "\n");
				// synchronization step
				logger.debug("{Executive} {tick: " + tick + "} -> Synchronization step\n");
				this.monitor.handleTick(tick);
				// dispatching step
				logger.debug("{Executive} {tick: " + tick + "} -> Dispatching step\n");
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
				logger.debug("{Executive} -> Handle tick: " + tick + "\n");
				// synchronization step only in "failure" mode
				logger.debug("{Executive} {tick: " + tick + "} -> Synchronization step\n");
				// handle observations
				this.monitor.handleObservations(tick);
				
				// hypothesis
				complete = true;
				// get nodes in starting state
				for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.STARTING)) {
					if (!node.isVirtual()) {
						// the executive cannot complete 
						complete = false;
						// waiting for a feedback of the node 
						logger.info("{Executive} {tick: " + tick + "} -> Terminating execution... waiting for feedback:\n"
								+ "\t- node: " + node + "\n");
					}
				}
				
				// get nodes in execution 
				for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION)) {
					if (!node.isVirtual()) {
						// the executive cannot complete 
						complete = false;
						// waiting for a feedback of the node 
						logger.info("{Executive} {tick: " + tick + "} -> Terminating execution... waiting for feedback:\n"
								+ "\t- node: " + node + "\n");
					}
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
			logger.error("{Executive} {tick: " + tick + "} -> Error while executing plan:\n"
					+ "\t- message: " + ex.getMessage() + "\n\n"
					+ "Wait for execution feedbacks of pending controllable and partially-controllable tokens if any... \n\n");
		}
		catch (PlatformException ex) 
		{
			// set failure
			this.failure.set(true);
			// complete execution in this case
			complete = true;
			// set cause
			this.cause = new PlatformError();
			// error message
			logger.error("{Executive} {tick: " + tick + "} -> Platform error:\n"
					+ "\t- message: " + ex.getMessage() + "\n");
		}
		catch (InterruptedException ex) {
			// execution error
			logger.error(ex.getMessage());
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
	 * @param node
	 * @throws PlatformException
	 */
	public void sendStopCommandSignalToPlatform(ExecutionNode node) 
			throws PlatformException
	{
		if (!node.isVirtual()) {
			// also send stop command execution request
			this.platformProxy.stopCommand(node);
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
				// send command and take operation ID
				PlatformCommand cmd = this.platformProxy.executedCommand(node);
				// add entry to the index
				this.dispatchedIndex.put(cmd, node);
			}
			else
			{
				// check if controllable token is virtual
				if (!node.isVirtual()) {
					// require execution start
					PlatformCommand cmd = this.platformProxy.startCommand(node);
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
	 * 
	 */
	@Override
	public void success(PlatformCommand cmd) 
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
				logger.info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the start of the execution of an uncontrollable token:\n"
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
				logger.info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the end of the execution of either a partially-controllable or uncontrollable token:\n"
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
			logger.warning("{Executive} {tick: " + this.currentTick + "} -> Receiving feedback about an unknown operation:\n\t- cmd: " + cmd + "\n\t-data: " + cmd.getData() + "\n");
		}
	}
	

	/**
	 * 
	 */
	@Override
	public void failure(PlatformCommand cmd) 
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO : Handle failure signals from platform");
	}
}
