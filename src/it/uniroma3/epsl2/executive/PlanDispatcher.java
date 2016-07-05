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
public abstract class PlanDispatcher extends ApplicationFrameworkObject {

	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// the plan
	
	@ClockReference
	protected ClockManager clock;					// execution clock
	
	private final Thread process;					// tick-driven process

	/**
	 * 
	 * @param exec
	 */
	protected PlanDispatcher(Executive<?,?> exec) {
		super();
		// set the clock
		this.clock = exec.clock;
		// set the plan
		this.pdb = exec.pdb;
		
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
	
	/**
	 * Dispatch the node to start execution
	 * 
	 * @param node
	 */
	protected abstract void dispatch(ExecutionNode node);
}
