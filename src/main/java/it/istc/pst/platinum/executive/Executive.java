package it.istc.pst.platinum.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import it.istc.pst.platinum.executive.dispatcher.ConditionCheckingDispatcher;
import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ExecutionFeedbackType;
import it.istc.pst.platinum.executive.lang.ex.DurationOverflow;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.lang.ex.ObservationSynchronizationException;
import it.istc.pst.platinum.executive.lang.ex.TokenDispatchingException;
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
public class Executive extends ExecutiveObject implements ExecutionManager, ExecutivePlatformObserver
{
	@ExecutivePlanDataBasePlaceholder
	protected ExecutivePlanDataBase pdb;										// the (executive) plan to execute
	
	@MonitorPlaceholder
	protected Monitor<?> monitor;												// plan monitor
	
	@DispatcherPlaceholder
	protected Dispatcher<?> dispatcher;											// dispatching process
	
	
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private FilePropertyReader config;											// configuration property file
	
	private ExecutionStatus status;												// executive's operating status
	private final Object lock;													// executive's status lock
	private ClockManager clock;													// execution clock controller
	private long currentTick;															// current tick
	
	private ExecutiveWindow window;												// executive window
	private Map<String, ExecutionNode> dispatchedIndex;							// keep track of dispatched nodes
	private AtomicBoolean failure;												// execution failure flag
	private ExecutionFailureCause cause;										// execution failure cause
	
	private ExecutivePlatformProxy platformProxy;								// platform PROXY to send commands to 
	
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
		// create executive window
		this.window = new ExecutiveWindow("Executive Window");
		// initialize clock manager
		this.clock = new AtomicClockManager(this);
		// initialize the PROXY and the observer
		this.platformProxy = null;
	}

	/**
	 * 
	 * @param proxy
	 */
	public void bind(ExecutivePlatformProxy proxy) {
		// bind the executive
		this.platformProxy = proxy;
		// register to the PROXY
		this.platformProxy.register(this);
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
			throws InterruptedException 
	{
		// current tick
		long tick = this.clock.getCurrentTick();
		// cover to tau
		return this.convertTickToTau(tick);
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
	 * @param tick
	 * @param node
	 * @param start
	 * @throws ExecutionException
	 */
	public void scheduleTokenStart(ExecutionNode node, long start) 
			throws ExecutionException 
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
				try
				{
					// check if virtual node
					if (!node.isVirtual()) {
						// actually schedule the start time of the token
						this.pdb.scheduleStartTime(node, start);
					}
					
					// update node status
					this.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
				}
				catch (TemporalConstraintPropagationException ex) 
				{
					// error while propagating token start time
					// TODO : create failure cause
					throw new TokenDispatchingException(ex.getMessage(), null);
				}
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
			throws ExecutionException
	{
		try
		{
			// schedule the observed start time of the token
			this.pdb.scheduleStartTime(node, start);
			// update node status
			this.updateNode(node, ExecutionNodeStatus.IN_EXECUTION);
		}
		catch (TemporalConstraintPropagationException ex) {
			// synchronization exception
			// TODO : create failure cause
			throw new ObservationSynchronizationException(ex.getMessage(), null);
		}
	}
	
	/**
	 * 
	 * @param node
	 * @param duration
	 * @throws ExecutionException
	 */
	public void scheduleTokenDuration(ExecutionNode node, long duration) 
			throws ExecutionException
	{
		try
		{
			// check if virtual node
			if (!node.isVirtual()) {
				// propagate scheduled duration time
				this.pdb.scheduleDuration(node, duration);
			}
			
			// the node can be considered as executed
			this.updateNode(node, ExecutionNodeStatus.EXECUTED);
		}
		catch (TemporalConstraintPropagationException ex) 
		{
			// create execution failure message
			ExecutionFailureCause cause = new DurationOverflow(this.currentTick, node, duration);
			// throw synchronization exception
			throw new ObservationSynchronizationException(
					"The observed duration does not comply with the expected one:\n"
					+ "\t- duration: " + duration + "\n"
					+ "\t- node: " + node + "\n", 
					cause);
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
	 * Blocking method which start the execution of the plan and waits for completion.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public final void execute() 
			throws InterruptedException, ExecutionException
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
		
		try
		{
			// perform setting operations before execution
			this.doPrepareExecution();
			
			// initialize dispatching index
			this.dispatchedIndex = new HashMap<>();
			// start clock
			this.clock.start();
			// wait execution completes
			this.clock.join();
		}
		catch (InterruptedException ex) 
		{
			// execution interrupted
			logger.error("Execution error:\n- message: " + ex.getMessage() + "\n");
			// set error state
			synchronized (this.lock) {
				// set error state
				this.status = ExecutionStatus.ERROR;
				this.lock.notifyAll();
			}
		}
		finally 
		{
			// execution complete
			synchronized (this.lock) 
			{
				// change status if not in error or interrupted and send a signal
				if (!this.status.equals(ExecutionStatus.INTERRUPTED) && 
						!this.status.equals(ExecutionStatus.ERROR)) 
				{
					// update execution status
					this.status = ExecutionStatus.INACTIVE;
				}
				this.lock.notifyAll();
			}
		}
	}
	
	/**
	 * 
	 */
	public final void interrupt() 
	{
		// check status
		synchronized (this.lock) 
		{
			// check if in execution
			if (this.status.equals(ExecutionStatus.EXECUTING)) {
				try {
					// interrupt the clock
					this.clock.stop();
				}
				catch (InterruptedException ex) {
					throw new RuntimeException(ex.getMessage());
				}
			}
			
			// update status and send signal
			this.status = ExecutionStatus.INTERRUPTED;
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	public final void error() 
	{
		// check status
		synchronized (this.lock) 
		{
			// check if in execution
			if (this.status.equals(ExecutionStatus.EXECUTING)) 
			{
				try {
					// interrupt the clock
					this.clock.stop();
				}
				catch (InterruptedException ex) {
					throw new RuntimeException(ex.getMessage());
				}
			}
			
			// update status and send signal
			this.status = ExecutionStatus.ERROR;
			this.lock.notifyAll();
		}
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
			// handle current tick
			this.currentTick = tick;
			logger.info("{Executive} -> Handle tick: " + tick + "\n");
			
			// synchronization step
			logger.info("{Executive} {tick: " + tick + "} -> Synchronization step\n");
			this.monitor.handleTick(tick);
			
			// dispatching step
			logger.info("{Executive} {tick: " + tick + "} -> Dispatching step\n");
			this.dispatcher.handleTick(tick);
			
			// display executive window
			this.displayWindow();
			// check if execution is complete
			complete = this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING).isEmpty() &&
					this.pdb.getNodesByStatus(ExecutionNodeStatus.STARTING).isEmpty() && 
					this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
		}
		catch (ExecutionException ex) {
			// set execution failure flag
			this.failure.set(true);
			// set execution failure cause
			this.cause = ex.getFailureCause();
			// error message
			logger.error(ex.getMessage());
			// end execution
			complete = true;
		}
		catch (InterruptedException ex) {
			// execution error
			logger.error(ex.getMessage());
		}
		
		// get boolean flag
		return complete;
	} 
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	private void displayWindow() 
			throws InterruptedException 
	{
		// get tau
		long tau = this.getTau();
		// set the data-set to show
		this.window.setDataSet(this.pdb.getHorizon(), this.getNodes());
		// display current execution state
		this.window.display(tau);
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
	 */
	public void dispatchCommandToThePlatform(ExecutionNode node) 
	{
		// check if a platform PROXY exists
		if (this.platformProxy != null) 
		{
			// marshal command according to the protocol
			String cmd = node.getGroundSignature() + "@" + node.getComponent();
			// send command and take operation ID
			String opId = this.platformProxy.sendCommand(cmd);
			// add entry to the index
			this.dispatchedIndex.put(opId, node);
		}
		else {
			
			// nothing to do, no platform PROXY available
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void success(String opId, Object data) 
	{
		// check operation id 
		if (this.dispatchedIndex.containsKey(opId))
		{
			// get execution node
			ExecutionNode node = this.dispatchedIndex.get(opId);
			// check node current status
			if (node.getStatus().equals(ExecutionNodeStatus.STARTING)) 
			{
				// got start execution feedback from a completely uncontrollable token
				logger.info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the start of the execution of an uncontrollable token:\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				// create execution feedback
				ExecutionFeedback feedback = new ExecutionFeedback(
						node, 
						ExecutionFeedbackType.UNCONTROLLABLE_TOKEN_START);
				// forward the feedback to the monitor
				this.monitor.addExecutionFeedback(feedback);
			}
			else if (node.getStatus().equals(ExecutionNodeStatus.IN_EXECUTION))
			{
				// got end execution feedback from either a partially-controllable or uncontrollable token
				logger.info("{Executive} {tick: " + this.currentTick + "} -> Got \"positive\" feedback about the end of the execution of either a partially-controllable or uncontrollable token:\n"
						+ "\t- node: " + node.getGroundSignature() + " (" + node + ")\n");
				// create execution feedback
				ExecutionFeedback feedback = new ExecutionFeedback(
						node, 
						node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE) ? 
								ExecutionFeedbackType.UNCONTROLLABLE_TOKEN_COMPLETE : 
								ExecutionFeedbackType.PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE);
				// forward feedback to the monitor
				this.monitor.addExecutionFeedback(feedback);
				// remove operation ID from index
				this.dispatchedIndex.remove(opId);
			}
			else 
			{
				// nothing to do
			}
			
		}
		else 
		{
			// no operation ID found 
			logger.warning("{Executive} {tick: " + this.currentTick + "} -> Receiving feedback about an unknown operation:\n\t- opId: " + opId + "\n\t-data: " + data + "\n");
		}
	}
	

	/**
	 * 
	 */
	@Override
	public void failure(String opId, Object data) 
	{
		// TODO Auto-generated method stub
		
	}
}
