package it.istc.pst.platinum.executive;

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
	 */
	public boolean onTick(long tick);
}
