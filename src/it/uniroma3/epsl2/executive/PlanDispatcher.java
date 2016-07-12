package it.uniroma3.epsl2.executive;

import it.uniroma3.epsl2.executive.clock.AtomicClockManager;
import it.uniroma3.epsl2.executive.clock.ClockManager;
import it.uniroma3.epsl2.executive.clock.ClockObserver;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ClockReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ExecutivePlanDataBaseReference;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlanDispatcher extends ApplicationFrameworkObject implements ClockObserver, Runnable {

	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// the plan
	
	@ClockReference
	protected ClockManager clock;				// execution clock
	
	private Thread process;							// tick-driven process
	
	private final Object lock;						// process status' lock	
	private ProcessStatus processStatus;			// process status
	
	private long currentTick;						// current execution tick

	/**
	 * 
	 * @param exec
	 */
	protected PlanDispatcher(Executive<?,?,?> exec) {
		super();
		// set the clock
		this.clock = (AtomicClockManager) exec.clock;
		// set the plan
		this.pdb = exec.pdb;
		
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
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException {
		
		// check status
		synchronized (this.lock) {
			// check if already active
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
	public final void run() {
		
		// start running
		boolean running = true;
		// run process
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
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 */
	protected abstract void onTick(long tick);
	
	/**
	 * Dispatch the node to start execution
	 * 
	 * @param node
	 */
	protected abstract void dispatch(ExecutionNode node);
}
