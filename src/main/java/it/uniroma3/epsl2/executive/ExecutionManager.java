package it.uniroma3.epsl2.executive;

/**
 * 
 * @author anacleto
 *
 */
public interface ExecutionManager 
{
	/**
	 * 
	 * @param tick
	 * @return
	 * @throws InterruptedException
	 */
	public boolean onTick(long tick) 
			throws InterruptedException;
}
