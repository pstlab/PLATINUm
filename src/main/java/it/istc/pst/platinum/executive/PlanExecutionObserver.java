package it.istc.pst.platinum.executive;

import java.util.List;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author alessandroumbrico
 *
 */
public interface PlanExecutionObserver 
{
	/**
	 * 
	 * @param tick
	 * @param failure - execution status as binary value (0 : regular execution, 1 : error handling)
	 * @param nodes
	 */
	public void onTick(long tick, boolean failure, List<ExecutionNode> nodes);
}
