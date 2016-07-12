package it.uniroma3.epsl2.executive;

import java.util.Date;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;

/**
 * 
 * @author anacleto
 *
 */
public class SimpleClockManager extends ApplicationFrameworkObject implements ClockManager
{
	private static long CLOCK_SAMPLING_RATE = 200;		// sampling time rate (in milliseconds)
	private long startTime;								// clock start time (in milliseconds)
	private final Thread process;						// clock incrementing the tick
	
	private final Object tickLock = new Object();
	private long tick;
	
	/**
	 * 
	 */
	public SimpleClockManager() {
		super();
		// initialize tick
		this.tick = 0;
		// initialize clock thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				
				// set thread running
				boolean running = true;
				// start process
				while (running) {
					try {
						
						// wait latency
						Thread.sleep(CLOCK_SAMPLING_RATE);
						// generate a clock event
						synchronized (tickLock) {

							// update the current tick
							tick++;
							// signal observers and release the lock
							tickLock.notifyAll();
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
	@Override
	public void start() {
		
		// start clock
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
	@Override
	public void stop() 
			throws InterruptedException {
		
		// stop clock
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
		
		// event tick
		long eventTick;
		// wait a clock event
		synchronized (this.tickLock) {

			// wait the event
			this.tickLock.wait();
			// read tick after notification
			eventTick = this.tick;
			// send signal
			this.tickLock.notifyAll();
		}
		
		// get read event tick
		return eventTick;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long getCurrentTick() 
			throws InterruptedException {

		// current clock's tick
		long currentTick;
		synchronized (this.tickLock) {
			// read current tick
			currentTick = this.tick;
			// send signal
			this.tickLock.notifyAll();
		}
		
		// get read tick
		return currentTick;
	}
	
	/**
	 * 
	 * @param tick
	 * @return
	 */
	@Override
	public long getSecondsFromTheOrigin(long tick) {
		// convert to seconds
		return Math.round((tick * CLOCK_SAMPLING_RATE) / 1000.0);
	}

	@Override
	public void subscribe(ClockObserver obs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unSubscribe(ClockObserver obs) {
		// TODO Auto-generated method stub
		
	}
}
