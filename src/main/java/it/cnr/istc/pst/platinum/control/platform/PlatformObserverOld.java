package it.cnr.istc.pst.platinum.control.platform;

import it.cnr.istc.pst.platinum.control.platform.PlatformCommand;

public interface PlatformObserverOld {
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
