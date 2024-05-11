package it.cnr.istc.pst.platinum.control.platform;

import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

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
