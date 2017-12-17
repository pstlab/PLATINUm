package it.istc.pst.platinum.executive.dispatcher;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Dispatcher extends ExecutiveObject 
{
	/**
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 */
	public abstract void handleTick(long tick);
	
	/**
	 * Dispatch the node to start execution
	 * 
	 * @param node
	 */
	public abstract void dispatch(ExecutionNode node);
}
