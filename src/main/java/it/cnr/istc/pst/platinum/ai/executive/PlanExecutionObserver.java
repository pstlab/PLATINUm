package it.cnr.istc.pst.platinum.ai.executive;

import java.util.List;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author alessandro
 *
 */
public interface PlanExecutionObserver {

	/**
	 * 
	 * @param tick
	 * @param failure
	 * @param nodes
	 */
	public void onTick(final long tick, final boolean failure, final List<ExecutionNode> nodes);
}
