package it.uniroma3.epsl2.executive;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;

/**
 * 
 * @author anacleto
 *
 */
public class ClockManager extends ApplicationFrameworkObject
{
	private static double CLOCK_SAMPLING_RATE = .2;		// sampling time rate (in seconds)
	private long startTime;								// clock start time (in milliseconds)
	private AtomicLong tick;							// current tick (relative measure)
	private final Thread process;						// clock
	
	private final Object clockEventLock = new Object();
	
	/**
	 * 
	 */
	public ClockManager() {
		super();
		// initialize tick
		this.tick = new AtomicLong(0);
		// initialize clock thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				boolean running = true;
				// generate the start clock event
				synchronized (clockEventLock) {							
					// signal observers and release the lock
					clockEventLock.notifyAll();
				}
				
				while (running) {
					try {
						// wait latency
						Thread.sleep((long) (CLOCK_SAMPLING_RATE * 1000));
						// increment tick (atomic operation)
						tick.incrementAndGet();
						
						// generate a clock event
						synchronized (clockEventLock) {							
							// signal observers and release the lock
							clockEventLock.notifyAll();
						}
					} catch (InterruptedException ex) {
						// stop thread
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
			// check if alive
			if (!this.process.isAlive()) {
				// start clock thread
				this.process.start();
				// notify all
				this.process.notifyAll();
				// get start time
				this.startTime = System.currentTimeMillis();
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
			// check clock thread status
			if (this.process.isAlive()) {
				// stop and wait clock thread
				this.process.interrupt();
				this.process.join();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public Date getClockStartTime() {
		return new Date(this.startTime);
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public long waitTick() 
			throws InterruptedException {
		// check if process has been started
		synchronized(this.process) {
			while (!this.process.isAlive()) {
				// wait process to start
				this.process.wait();
			}
		}
		
		// wait a clock event
		synchronized (this.clockEventLock) {
			this.clockEventLock.wait();
		}
		
		// get current tick
		return this.tick.longValue();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getCurrentTick() 
			throws InterruptedException 
	{
		// check if process has been started
		synchronized(this.process) {
			while (!this.process.isAlive()) {
				// wait process to start
				this.process.wait();
			}
		}
		
		// get current tick
		return this.tick.longValue();
	}
	
	/**
	 * 
	 * @param tick
	 * @return
	 */
	public long getSecondsFromTheOrigin(long tick) {
		return (long) (CLOCK_SAMPLING_RATE * tick);
	}
	
}
