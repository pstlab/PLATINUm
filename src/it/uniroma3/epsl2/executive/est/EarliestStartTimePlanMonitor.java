package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.PlanMonitor;
import it.uniroma3.epsl2.executive.ProcessStatus;
import it.uniroma3.epsl2.executive.clock.ClockObserver;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class EarliestStartTimePlanMonitor extends PlanMonitor implements Runnable, ClockObserver {
	
	private long currentTick;				// current tick
	private Thread process;
	private final Object lock;				// status' lock
	private ProcessStatus processStatus;	// process status
	
	/**
	 * 
	 * @param exec
	 */
	protected EarliestStartTimePlanMonitor(EarliestStartTimeExecutive exec) {
		super(exec);
		
		// subscribe to clock
		this.clock.subscribe(this);

		// set status
		this.lock = new Object();
		this.processStatus = ProcessStatus.INACTIVE;
		// create thread
		this.process = null;
	}
	
	/**
	 * 
	 */
	@Override
	public void waitReady() 
			throws InterruptedException {
		
		// check status
		synchronized (this.lock) {
			// wait ready status
			while (!this.processStatus.equals(ProcessStatus.READY)) {
				this.lock.wait();
			}
			
			// send signal
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void start() {
		
		// check status
		synchronized (this.lock) {
			// check if not active
			if (this.processStatus.equals(ProcessStatus.INACTIVE)) {
				// activate process
				this.process = new Thread(this);
				// subscribe to clock
				this.clock.subscribe(this);
				// update status
				this.processStatus = ProcessStatus.READY;
				// start process
				this.process.start();
			}
 			
			// send signal 
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		
		// starting process
		boolean running = true;
		while (running) {
			try {
				
				// check status
				synchronized (this.lock) {
					// check condition
					while (!this.processStatus.equals(ProcessStatus.PROCESSING)) {
						this.lock.wait();
					}
					
					// send signal
					this.lock.notifyAll();
				}
				
				// handle tick
				this.onTick(this.currentTick);
				
				// change status
				synchronized (this.lock) {
					// update status
					this.processStatus = ProcessStatus.READY;
					// send signal
					this.lock.notifyAll();
				}
			}
			catch (InterruptedException ex) {
				// stop thread
				running = false;
			}
		}
	}
	

	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException {
		
		// check status
		boolean toStop = false;
		synchronized (this.lock) {
			// check if active
			toStop = !this.processStatus.equals(ProcessStatus.INACTIVE);
			this.lock.notifyAll();
		}
		
		// stop process if necessary
		if (toStop) {
			// interrupt process
			this.process.interrupt();
			this.process.join();
			this.process = null;
			
			// cancel from clock
			this.clock.unSubscribe(this);
			// update status
			synchronized (this.lock) {
				// set status
				this.processStatus = ProcessStatus.INACTIVE;
				this.lock.notifyAll();
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void clockUpdate(long tick) 
			throws InterruptedException {
		
		// update the current tick
		synchronized (this.lock) {
			// check status
			while (!this.processStatus.equals(ProcessStatus.READY)) {
				// wait signal
				this.lock.wait();
			}
			
			// set current tick
			this.currentTick = tick;
			// update status
			this.processStatus = ProcessStatus.PROCESSING;
			// send signal
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param tick
	 */
	protected void onTick(long tick) {
		
		// get seconds w.r.t. the temporal origin
		long tau = this.clock.getSecondsFromTheOrigin(tick);
		// check nodes in execution
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION)) {
			
			// check end condition
			if (this.pdb.canEndExecution(node)) {
				// check schedule
				this.pdb.checkSchedule(node);
				try {
					
					// schedule duration if needed
					if (node.getDuration()[0] != node.getDuration()[1]) {
						
						// schedule duration to lower bound
						this.pdb.scheduleDuration(node, node.getDuration()[0]);
						this.pdb.checkSchedule(node);
					}
				}
				catch (Exception ex) {
					throw new RuntimeException("{tick = " + tick + "} {EarliestStartTimePlanMonitor} -> Failure scheduling duration of node " + node + " ERROR {\n- message= " + ex.getMessage() + "\n}");
				}
				
				// check if execution ended
				long end = node.getEnd()[0];
				// check if delay
				if (tau >= end) {
					
					// update status
					this.pdb.updateNodeStatus(node, ExecutionNodeStatus.EXECUTED);
					System.out.println("{tick = " + tick +"} {EarliestStartTimePlanMonitor} -> End executing node " + node);
				}
			}
		}
	}
}
