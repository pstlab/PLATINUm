package it.istc.pst.platinum.executive.monitor;

import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Monitor extends ExecutiveObject 
{
	/**
	 * 
	 * @param tick
	 */
	public abstract void handleTick(long tick);
}
