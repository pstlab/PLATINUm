package it.uniroma3.epsl2.executive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.executive.pdb.apsi.EPSLExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.PlanDispatcherReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.PlanMonitorReference;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Executive extends ApplicationFrameworkObject implements Runnable, ClockObserver
{
	private static final String TIME_UNIT_PROPERTY = "time_unit_to_second";		// property specifying the amount of seconds a time unit corresponds to
	private Properties config;													// configuration property file
	private final Object lock;							// executive's status lock
	private ExecutionStatus status;						// executive's operating status
	private Thread process;								// execution controller process
	private ClockManager clock;							// execution clock controller
	
	@PlanMonitorReference
	protected PlanMonitor monitor;						// plan monitor
	
	@PlanDispatcherReference
	protected PlanDispatcher dispatcher;				// dispatching process
	
	protected ExecutivePlanDataBaseManager pdb;			// the plan to execute
	
	/**
	 * 
	 */
	protected Executive() 
	{
		super();
		try 
		{
			// create property file
			this.config = new Properties();
			// load file property
			try (InputStream input = new FileInputStream("etc/executive.properties")) { 
				// load file
				this.config.load(input);
			}
			
			// set clock and initial status
			this.lock = new Object();
			this.status = ExecutionStatus.INACTIVE;
			this.process = null;
		}
		catch (IOException ex) {
			// problems locating property file
			throw new RuntimeException(ex.getMessage());
		}
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
			throws Exception {
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
			throws Exception {
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
	public final void initialize(EPSLPlanDescriptor plan) 
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
		
		// start and wait the end of the execution
		this.process = new Thread(this);
		process.start();
		process.join();
		this.process = null;
		
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
	@Override
	public void run() 
	{
		try 
		{
			// setup the clock
			// create clock
			this.clock = new AtomicClockManager();
			// subscribe to clock
			this.clock.subscribe(this);
			// start clock
			this.clock.start();
			
			// prepare execution
			boolean running = true;
			while (running)
			{
				try 
				{
					// periodically check execution end condition
					Thread.sleep(1000);
					
					// check execution end
					running = !this.isExecutionComplete();
				}
				catch (InterruptedException ex) {
					running = false;
				}
			} // execution ended
			
			// stop clock
			this.clock.stop();
			this.clock.unSubscribe(this);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isExecutionComplete();
	
	/**
	 * 
	 */
	@Override
	public abstract void onTick(long tick); 
	
	/**
	 * 
	 */
	public final void interrupt() 
	{
		// check status
		synchronized (this.lock) 
		{
			// check if in execution
			if (this.status.equals(ExecutionStatus.EXECUTING)) 
			{
				try 
				{
					// interrupt process
					if (this.process != null) {
						this.process.interrupt();
						this.process.join();
						this.process = null;
					}
				}
				catch (InterruptedException ex) {
					System.err.println(ex.getMessage());
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
				try 
				{
					// interrupt process
					if (this.process != null) {
						this.process.interrupt();
						this.process.join();
						this.process = null;
					}
				}
				catch (InterruptedException ex) {
					System.err.println(ex.getMessage());
				}
			}
			
			// update status and send signal
			this.status = ExecutionStatus.ERROR;
			this.lock.notifyAll();
		}
	}
}
