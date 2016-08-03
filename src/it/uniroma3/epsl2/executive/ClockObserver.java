package it.uniroma3.epsl2.executive;

/**
 * 
 * @author anacleto
 *
 */
public interface ClockObserver {

	/**
	 * 
	 * @param tick
	 */
	public void onTick(long tick); 
}
