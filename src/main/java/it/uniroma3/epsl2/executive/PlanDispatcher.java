package it.uniroma3.epsl2.executive;

import it.uniroma3.epsl2.executive.pdb.ExecutionNode;

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
