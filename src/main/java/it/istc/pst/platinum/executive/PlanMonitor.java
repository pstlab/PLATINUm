package it.istc.pst.platinum.executive;

import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlanMonitor extends ExecutiveObject 
{
	/**
	 * 
	 * @param tick
	 */
	public abstract void handleTick(long tick);
}
