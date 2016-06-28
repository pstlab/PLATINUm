package it.uniroma3.epsl2.executive.dispatcher;

import it.uniroma3.epsl2.executive.ClockManager;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ClockReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ExecutivePlanDataBaseReference;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlanDispatcher extends ApplicationFrameworkObject {

	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// the plan
	
	@ClockReference
	protected ClockManager clock;					// execution clock
	
	private final Thread process;					// tick-driven process

	/**
	 * 
	 * @param c
	 * @param pdb
	 */
	public PlanDispatcher(ClockManager c, ExecutivePlanDataBaseManager pdb) {
		super();
		// set the clock
		this.clock = c;
		// set the plan
		this.pdb = pdb;
		
		// create thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				boolean running = true;
				while (running) {
					try {
						// wait current tick
						long tick = clock.waitTick();
						onTick(tick);
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 */
	public void start() {
		synchronized(this.process) {
			if (!this.process.isAlive()) {
				// start process
				this.process.start();
				
				// notify all
				this.process.notifyAll();
			}
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException {
		synchronized(this.process) {
			if (this.process.isAlive()) {
				// stop process
				this.process.interrupt();
				this.process.join();
				
				// signal
				this.process.notifyAll();
			}
		}
	}
	
	/**
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 */
	protected abstract void onTick(long tick);
}
