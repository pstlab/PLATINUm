package it.uniroma3.epsl2.executive.clock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;

/**
 * 
 * @author anacleto
 *
 */
public class AtomicClockManager extends ApplicationFrameworkObject implements Runnable, ClockManager
{
	private static long CLOCK_SAMPLING_RATE = 200;		// sampling time rate (in milliseconds)
	private long clockSart;								// clock start time (in milliseconds)
	private Thread process;								// clock updating process
	
	private AtomicLong tick;
	private List<ClockObserver> observers;				// list of observers
	
	/**
	 * 
	 */
	public AtomicClockManager() {
		super();
		this.observers = new ArrayList<ClockObserver>();
		// initialize clock thread and tick
		this.tick = null;
		this.process = null;
	}
	
	/**
	 * 
	 * @param obs
	 */
	@Override
	public void subscribe(ClockObserver obs) { 
		this.observers.add(obs);
	}
	
	/**
	 * 
	 * @param obs
	 */
	@Override
	public void unSubscribe(ClockObserver obs) {
		this.observers.remove(obs);
	}

	/**
	 * 
	 */
	public synchronized void start() {

		// check process
		if (this.process == null) {
			// create and start process
			this.process = new Thread(this);
			// start clock thread
			this.process.start();
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void stop() 
			throws InterruptedException {
		
		// check clock thread status
		if (this.process.isAlive()) {
			// stop and wait clock thread
			this.process.interrupt();
			this.process.join();
			this.process = null;
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		
		// set clock start time
		this.clockSart = System.currentTimeMillis();
		// initialize tick
		this.tick = new AtomicLong(0);
		// set thread running
		boolean running = true;
		// start process
		while (running) {
			try {
				
				// wait latency
				Thread.sleep(CLOCK_SAMPLING_RATE);

				// update the current tick
				long currentTick = this.tick.getAndIncrement();
				// notify all observers
				for (ClockObserver obs : this.observers) {
					// notify clock update
					obs.clockUpdate(currentTick);
				}
			} catch (InterruptedException ex) {
				// stop thread
				running = false;
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	@Override
	public long getCurrentTick() 
			throws InterruptedException {

		// get read tick
		return this.tick.longValue();
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getClockStart() {
		return new Date(this.clockSart);
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
}
