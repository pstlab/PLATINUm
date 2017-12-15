package it.istc.pst.platinum.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.executive.est.EarliestStartTimePlanDispatcher;
import it.istc.pst.platinum.executive.est.EarliestStartTimePlanMonitor;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.executive.pdb.ExecutivePlanDataBase;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.ExecutiveDispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.ExecutiveMonitorConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutionDispatcherPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutionMonitorPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;
import it.istc.pst.platinum.framework.utils.view.executive.ExecutiveWindow;

/**
 * 
 * @author anacleto
 *
 */
@ExecutiveMonitorConfiguration(monitor = EarliestStartTimePlanMonitor.class)
@ExecutiveDispatcherConfiguration(dispatcher = EarliestStartTimePlanDispatcher.class)
public class Executive extends ExecutiveObject implements ExecutionManager
{
	@ExecutionMonitorPlaceholder
	protected PlanMonitor monitor;						// plan monitor
	
	@ExecutionDispatcherPlaceholder
	protected PlanDispatcher dispatcher;				// dispatching process
	
	@ExecutivePlanDataBasePlaceholder
	protected ExecutivePlanDataBase pdb;			// the plan to execute
	
	
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private FilePropertyReader config;											// configuration property file
	private final Object lock;													// executive's status lock
	private ExecutionStatus status;												// executive's operating status
	private ClockManager clock;													// execution clock controller
	
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
	 * Blocking method which start the execution of the plan and
	 *  waits for completion.
	 */
	public final void execute() 
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
		
		// setup the clock
		this.clock = new AtomicClockManager(this);
		// start clock
		this.clock.start();
		// wait execution completes
		this.clock.join();
		
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
			this.status = ExecutionStatus.ERROR;
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onTick(long tick)
	{
		boolean complete = false;
		try 
		{
			System.out.println("\n##################################################");
			System.out.println("#### Handle tick = " + tick + " ####");
			System.out.println("#### Synchronization step ####");
			// synchronization step
			this.monitor.handleTick(tick);
			System.out.println("#### Dispatching step ####");
			// dispatching step
			this.dispatcher.handleTick(tick);
			System.out.println("##################################################");
			// display executive window
			this.displayWindow();
			// check if execution is complete
			complete = this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING).isEmpty() &&
					this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
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
