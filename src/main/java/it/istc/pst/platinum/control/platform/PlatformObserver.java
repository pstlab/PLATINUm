package it.istc.pst.platinum.control.platform;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;

/**
 * 
 * @author anacleto
 *
 */
public interface PlatformObserver 
{
	/**
	 * Notify a successful execution of a command
	 * 
	 * @param cmd
	 */
	public void success(PlatformCommand cmd);
	
	/**
	 * Notify a failed execution of a command
	 * 
	 * @param cmd
	 */
	public void failure(PlatformCommand cmd);
}
