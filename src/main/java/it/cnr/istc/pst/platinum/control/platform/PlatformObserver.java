package it.cnr.istc.pst.platinum.control.platform;

import it.cnr.istc.pst.platinum.control.lang.PlatformFeedback;
import it.cnr.istc.pst.platinum.control.lang.PlatformObservation;

/**
 * 
 * @author anacleto
 *
 */
public interface PlatformObserver 
{
	/**
	 * 
	 * @param feedback
	 */
	public void feedback(PlatformFeedback feedback);
	
	/**
	 * Asynchronous notification of some observed events from 
	 * the platform
	 * 
	 * @param obs
	 */
	public void observation(PlatformObservation<? extends Object> obs);
}
