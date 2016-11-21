package it.uniroma3.epsl2.executive;

import it.uniroma3.epsl2.executive.ex.ExecutionException;

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
	 * @throws ExecutionException
	 */
	public boolean onTick(long tick) 
			throws ExecutionException;
}
