package it.cnr.istc.pst.platinum.ai.executive.dispatcher;

import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Dispatcher<T extends Executive> extends FrameworkObject 
{
	@ExecutivePlaceholder
	protected T executive;
	
	/**
	 * 
	 */
	public void clear() {
		// nothing to do
	}
	
	/**
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 * @throws ExecutionException
	 * @throws PlatformException
	 */
	public abstract void handleTick(long tick) 
			throws ExecutionException, PlatformException; 
}
