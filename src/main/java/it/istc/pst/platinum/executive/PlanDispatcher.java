package it.istc.pst.platinum.executive;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public interface PlanDispatcher 
{
	/**
	 * The method handle the current tick of the executor's clock
	 * 
	 * @param tick
	 */
	public void handleTick(long tick);
	
	/**
	 * Dispatch the node to start execution
	 * 
	 * @param node
	 */
	public void dispatch(ExecutionNode node);
}
