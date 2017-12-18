package it.istc.pst.platinum.executive;

import it.istc.pst.platinum.executive.ex.ExecutionException;

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
