package it.cnr.istc.pst.platinum.ai.executive.lang.failure;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutionInterruptCause extends ExecutionFailureCause {
	
	/**
	 * 
	 * @param tick
	 * @param node
	 */
	public ExecutionInterruptCause(long tick, ExecutionNode node) {
		super(tick, ExecutionFailureCauseType.EXECUTION_INTERRUPT, node);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "Execution interrupt (tick: " + this.getInterruptionTick() + "): " + this.getInterruptionNode();
	}
}
