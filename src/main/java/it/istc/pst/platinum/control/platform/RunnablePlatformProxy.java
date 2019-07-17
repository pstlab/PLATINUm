package it.istc.pst.platinum.control.platform;

import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;

/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class RunnablePlatformProxy extends PlatformProxy 
{
	/**
	 * 
	 */
	protected RunnablePlatformProxy() {
		super();
	}
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void start() 
			throws PlatformException;
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void stop() 
			throws PlatformException;
}
