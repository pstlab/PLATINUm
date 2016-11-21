package it.uniroma3.epsl2.executive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.executive.ex.ExecutionException;

/**
 * 
 * @author anacleto
 *
 */
public final class AtomicClockManager implements Runnable, ClockManager
{
	private static final String CLOCK_RATE_PROPERTY = "clock_frequency";	// property specifying sampling time rate (in milliseconds)
	private Properties config;												// executive configuration file
	private long clockSart;													// clock start time (in milliseconds)
	private Thread process;													// clock updating process
	
	private long tickStart;
	private AtomicLong tick;
	private ExecutionManager executive;										// executive system
	
	/**
	 * 
	 * @param exec
	 */
	public AtomicClockManager(ExecutionManager exec) {
		super();
		// set tick start
		this.tickStart = 0;
		// set executive
		this.executive = exec;
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
	 */
	@Override
	public synchronized void start() 
			throws InterruptedException 
	{
		// check process
		if (this.process == null) {
			// set tick start
			this.tickStart = 0;
			// create and start process
			this.process = new Thread(this);
			// start clock thread
			this.process.start();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void start(long tick) 
			throws InterruptedException 
	{
		// check process
		if (this.process == null) {
			// set tick start
			this.tickStart = tick;
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
	@Override
	public synchronized long stop() 
			throws InterruptedException 
	{
		// get current tick
		long lastTick = this.tick.getAndIncrement();
		// check clock thread status
		if (this.process != null && this.process.isAlive()) {
			// stop and wait clock thread
			this.process.interrupt();
			this.process.join();
			this.process = null;
		}
		
		// return last tick
		return lastTick;
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public synchronized void join() 
			throws InterruptedException
	{
		// check clock thread status
		if (this.process != null && this.process.isAlive()) {
			// wait the process to complete
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
		// set clock start time
		this.clockSart = System.currentTimeMillis();
		// initialize tick
		this.tick = new AtomicLong(this.tickStart);
		// set execution complete flag
		boolean complete = false;
		// start process
		while (!complete) 
		{
			try 
			{
				// get sampling rate from the configuration file
				long sampling = Long.parseLong(this.config.getProperty(CLOCK_RATE_PROPERTY));
				// wait latency
				Thread.sleep(sampling);

				// update the current tick
				long currentTick = this.tick.getAndIncrement();
				// notify executive
				complete = this.executive.onTick(currentTick);
			} 
			catch (InterruptedException ex) {
				// complete execution
				System.err.println("Execution Interrupted\n" + ex.getMessage());
				complete = true;
			}
			catch (ExecutionException ex) { 
				// stop execution
				complete = true;
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
