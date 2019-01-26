package it.istc.pst.platinum.executive;

import it.istc.pst.platinum.executive.lang.ex.ExecutionException;

/**
 * 
 * @author anacleto
 *
 */
public interface ExecutionManager 
{
	/**
	 * 
	 * @param property
	 * @return
	 */
	public String getProperty(String property); 
	
	/**
	 * 
	 * @param tick
	 * @return
	 * @throws ExecutionException
	 */
	public boolean onTick(long tick) 
			throws ExecutionException;
}
