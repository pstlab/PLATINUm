package it.istc.pst.platinum.control.platform.sim;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;

/**
 * 
 * @author anacleto
 *
 */
public interface PlatformAgentObserver 
{

	/**
	 * 
	 * @param cmd
	 */
	void notifySuccess(PlatformCommand cmd);
	
	/**
	 * 
	 * @param cmdw
	 */
	public void notifyFailure(PlatformCommand cmd);

}
