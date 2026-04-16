package it.cnr.istc.pst.platinum.ai.executive.lang.failure;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class NodeExecutionError extends ExecutionFailureCause 
{
	/**
	 * 
	 * @param tick
	 * @param node
	 */
	public NodeExecutionError(long tick, ExecutionNode node) {
		super(tick, ExecutionFailureCauseType.NODE_EXECUTION_ERROR, node);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Node execution failure(tick: " + this.getInterruptionTick() + "): " + this.getInterruptionNode();
	}
}
