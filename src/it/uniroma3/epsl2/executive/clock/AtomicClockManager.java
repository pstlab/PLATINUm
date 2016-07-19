package it.uniroma3.epsl2.executive.clock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;

/**
 * 
 * @author anacleto
 *
 */
public class AtomicClockManager extends ApplicationFrameworkObject implements Runnable, ClockManager
{
	private static final String CLOCK_RATE_PROPERTY = "clock_frequency";	// property specifying sampling time rate (in milliseconds)
	private Properties config;												// executive configuration file
	private long clockSart;													// clock start time (in milliseconds)
	private Thread process;													// clock updating process
	
	private long tickStart;
	private AtomicLong tick;
	private List<ClockObserver> observers;				// list of observers
	
	/**
	 * 
	 */
	public AtomicClockManager() {
		super();
		// set tick start
		this.tickStart = 0;
		// set observers
		this.observers = new ArrayList<ClockObserver>();
		// initialize clock thread and tick
		this.tick = null;
		this.process = null;
		try 
		{
			// create property file
			this.config = new Properties();
			// load file property
			try (InputStream input = new FileInputStream("etc/executive.properties")) { 
				// load file
				this.config.load(input);
			}
		}
		catch (IOException ex) {
			// problems locating property file
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param tick
	 */
	public AtomicClockManager(long tick) {
		this();
		// set tick start
		this.tickStart = tick;
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
		if (this.process != null && this.process.isAlive()) {
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
	public void run() 
	{
		try 
		{
			// set clock start time
			this.clockSart = System.currentTimeMillis();
			// initialize tick
			this.tick = new AtomicLong(this.tickStart);
			// set thread running
			boolean running = true;
			// start process
			while (running) 
			{
				try 
				{
					// get sampling rate from the configuration file
					long sampling = Long.parseLong(this.config.getProperty(CLOCK_RATE_PROPERTY));
					// wait latency
					Thread.sleep(sampling);
	
					// update the current tick
					long currentTick = this.tick.getAndIncrement();
					// notify all observers
					for (ClockObserver obs : this.observers) {
						// notify clock update
						obs.onTick(currentTick);
					}
				} catch (InterruptedException ex) {
					// stop thread
					running = false;
				}
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
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
	public double convertClockTickToSeconds(long tick) {
		// get sampling rate in milliseconds
		long sampling = Long.parseLong(this.config.getProperty(CLOCK_RATE_PROPERTY));
		// convert to seconds
		return (tick * sampling) / 1000.0;
	}
	
	/**
	 * 
	 */
	@Override
	public double convertSecondsToClockTick(long seconds) {
		// get sampling rate in milliseconds
		long sampling = Long.parseLong(this.config.getProperty(CLOCK_RATE_PROPERTY));
		// convert to tick
		return (seconds * 1000) / sampling;
	}
}
