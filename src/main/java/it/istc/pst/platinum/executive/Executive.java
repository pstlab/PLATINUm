package it.istc.pst.platinum.executive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.executive.pdb.ExecutivePlanDataBaseManager;
import it.istc.pst.platinum.executive.pdb.apsi.EPSLExecutivePlanDataBaseManager;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutionDispatcherPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutionMonitorPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Executive extends ApplicationFrameworkObject implements ExecutionManager
{
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private FilePropertyReader config;											// configuration property file
	private final Object lock;													// executive's status lock
	private ExecutionStatus status;												// executive's operating status
	private ClockManager clock;													// execution clock controller
	
	@ExecutionMonitorPlaceholder
	protected PlanMonitor monitor;						// plan monitor
	
	@ExecutionDispatcherPlaceholder
	protected PlanDispatcher dispatcher;				// dispatching process
	
	protected ExecutivePlanDataBaseManager pdb;			// the plan to execute
	
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
		return this.pdb.canEndExecution(node);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean canStart(ExecutionNode node) {
		return this.pdb.canStartExecution(node);
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
		
		// create execution plan data-base
		this.pdb = new EPSLExecutivePlanDataBaseManager(plan.getOrigin(), plan.getHorizon());
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
	 * @param plan
	 * @throws InterruptedException
	 */
	public final void initialize(SolutionPlan plan) 
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
		
		// create execution plan data-base
		this.pdb = new EPSLExecutivePlanDataBaseManager(0, plan.getHorizon());
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
	public abstract boolean onTick(long tick); 
}
