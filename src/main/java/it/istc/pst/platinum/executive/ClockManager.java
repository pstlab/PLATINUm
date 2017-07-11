package it.istc.pst.platinum.executive;

/**
 * 
 * @author anacleto
 *
 */
public interface ClockManager 
{
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
	 * @return
	 * @throws InterruptedException
	 */
	public long stop() 
			throws InterruptedException;
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void join() 
			throws InterruptedException;
	
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
