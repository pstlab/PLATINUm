package it.istc.pst.platinum.control.platform.hrc;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;

/**
 * 
 * @author anacleto
 *
 */
public interface HRCPlatformAgentObserver 
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
