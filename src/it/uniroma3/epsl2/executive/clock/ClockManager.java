package it.uniroma3.epsl2.executive.clock;

/**
 * 
 * @author anacleto
 *
 */
public interface ClockManager {
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException;
	
	/**
	 * 
	 * @param tick
	 * @throws InterruptedException
	 */
	public void start(long tick) 
			throws InterruptedException;
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException;

	/**
	 * 
	 * @param obs
	 */
	public void subscribe(ClockObserver obs);
	
	/**
	 * 
	 * @param obs
	 */
	public void unSubscribe(ClockObserver obs);
	
	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public long getCurrentTick() 
			throws InterruptedException;
	
	/**
	 * 
	 * @param tick
	 * @return
	 */
	public double convertClockTickToSeconds(long tick);

	/**
	 * 
	 * @param seconds
	 * @return
	 */
	public double convertSecondsToClockTick(long seconds);
}
