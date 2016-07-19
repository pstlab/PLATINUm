package it.uniroma3.epsl2.executive.clock;

/**
 * 
 * @author anacleto
 *
 */
public interface ClockObserver {

	/**
	 * 
	 * @param tick
	 * @throws InterruptedException
	 */
	public void onTick(long tick) 
			throws InterruptedException;
}
