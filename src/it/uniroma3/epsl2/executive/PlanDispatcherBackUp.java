package it.uniroma3.epsl2.executive;

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
public abstract class PlanDispatcherBackUp extends ApplicationFrameworkObject implements ClockObserver {

	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// the plan
	
	@ClockReference
	protected SimpleClockManager clock;				// execution clock
	
	
	private Thread process;							// tick-driven process
	
	private final Object lock;
	private boolean ready;

	/**
	 * 
	 * @param exec
	 */
	protected PlanDispatcherBackUp(Executive<?, ?,?> exec) {
		super();
		// set the clock
		this.clock = (SimpleClockManager) exec.clock;
		// set the plan
		this.pdb = exec.pdb;
		this.lock = new Object();
		this.ready = false;
		
		// create thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				
				// start running
				boolean running = true;
				// set status
				synchronized (lock) {
					// set status
					ready = true;
					// send signal
					lock.notifyAll();
				}
				
				// run process
				while (running) {
					try {
						// wait current tick
						long tick = clock.waitTick();
						onTick(tick);
					}
					catch (InterruptedException ex) {
						// stop thread
						running = false;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException {
		
		// start process
		if (!this.process.isAlive()) {
			// start process
			this.process.start();
			// wait process ready
			synchronized (this.lock) {
				// wait the process ready
				while (!this.ready) {
					this.lock.wait();
				}
				
				// send signal
				this.lock.notifyAll();
			}
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException {
		
		// stop process
		if (this.process.isAlive()) {
			// stop process
			this.process.interrupt();
			this.process.join();
			
			// change status
			synchronized (this.lock) {
				// update
				this.ready = false;
				// send signal
				this.lock.notifyAll();
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
