package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Dispatcher<T extends Executive> extends ExecutiveObject 
{
	@ExecutivePlaceholder
	protected T executive;
	
	
	/**
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 */
	public abstract void handleTick(long tick) 
			throws ExecutionException;
	
	/**
	 * Dispatch the node to start execution
	 * 
	 * @param node
	 */
	public abstract void dispatch(ExecutionNode node); 
}
