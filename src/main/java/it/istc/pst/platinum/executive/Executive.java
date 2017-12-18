package it.istc.pst.platinum.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.executive.dispatcher.Dispatcher;
import it.istc.pst.platinum.executive.dispatcher.EarliestStartTimePlanDispatcher;
import it.istc.pst.platinum.executive.ex.ExecutionException;
import it.istc.pst.platinum.executive.monitor.EarliestStartTimePlanMonitor;
import it.istc.pst.platinum.executive.monitor.Monitor;
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
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;
import it.istc.pst.platinum.framework.utils.view.executive.ExecutiveWindow;

/**
 * 
 * @author anacleto
 *
 */
@FrameworkLoggerConfiguration(level = FrameworkLoggingLevel.DEBUG)
@MonitorConfiguration(monitor = EarliestStartTimePlanMonitor.class)
@DispatcherConfiguration(dispatcher = EarliestStartTimePlanDispatcher.class)
public class Executive extends ExecutiveObject implements ExecutionManager
{
	@ExecutivePlanDataBasePlaceholder
	protected ExecutivePlanDataBase pdb;										// the (executive) plan to execute
	
	@MonitorPlaceholder
	protected Monitor monitor;												// plan monitor
	
	@DispatcherPlaceholder
	protected Dispatcher dispatcher;										// dispatching process
	
	
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private FilePropertyReader config;											// configuration property file
	
	private final Object lock;													// executive's status lock
	private ClockManager clock;													// execution clock controller
	private ExecutionStatus status;												// executive's operating status
	
	
	private ExecutiveWindow window;				// executive window
	
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
	 * @throws Exception
	 */
	public void scheduleStartTime(ExecutionNode node, long start) 
			throws Exception 
	{
		// propagate scheduled start time
		this.pdb.scheduleStartTime(node, start);
		this.pdb.checkSchedule(node);
	}
	
	/**
	 * 
	 * @param node
	 * @param duration
	 * @throws Exception
	 */
	public void scheduleDuration(ExecutionNode node, long duration) 
			throws Exception 
	{
		// propagate scheduled duration time
		this.pdb.scheduleDuration(node, duration);
		this.pdb.checkSchedule(node);
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
			// setup the clock
			this.clock = new AtomicClockManager(this);
			// start clock
			this.clock.start();
			// wait execution completes
			this.clock.join();
		}
		catch (InterruptedException ex) 
		{
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
	 * @param tick
	 * @return
	 */
	@Override
	public boolean onTick(long tick)
	{
		boolean complete = false;
		try 
		{
			// prepare logging message
			String msg = "\n##################################################\n";
			msg += "#### Handle tick = " + tick + " ####\n";
			msg += "#### Synchronization step ####\n";
			// synchronization step
			this.monitor.handleTick(tick);
			msg += "#### Dispatching step ####\n";
			// dispatching step
			this.dispatcher.handleTick(tick);
			msg += "##################################################\n";
			// print info 
			logger.info(msg);
			// display executive window
			this.displayWindow();
			// check if execution is complete
			complete = this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING).isEmpty() &&
					this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
		}
		catch (InterruptedException ex) {
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
}
