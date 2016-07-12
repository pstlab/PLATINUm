package it.uniroma3.epsl2.executive;

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
	public long getSecondsFromTheOrigin(long tick);

	
}
