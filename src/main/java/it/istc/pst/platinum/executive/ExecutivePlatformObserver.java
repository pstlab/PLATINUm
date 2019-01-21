package it.istc.pst.platinum.executive;

/**
 * 
 * @author anacleto
 *
 */
public interface ExecutivePlatformObserver 
{
	/**
	 * Notify a successful execution of a command
	 * 
	 * @param opId
	 * @param data
	 */
	public void success(String opId, Object data);
	
	/**
	 * Notify a failed execution of a command
	 * 
	 * @param opId
	 * @param data
	 */
	public void failure(String opId, Object data);
}
