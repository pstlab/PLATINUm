package it.uniroma3.epsl2.executive.lang;

import it.uniroma3.epsl2.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutionFailureCause 
{
	private ExecutionFailureCauseType type;
	private long interruptionTick;
	private ExecutionNode interruptNode;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param type
	 */
	public ExecutionFailureCause(long tick, ExecutionNode node, ExecutionFailureCauseType type) {
		this.type = type;
		this.interruptionTick = tick;
		this.interruptNode = node;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCauseType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getInterruptionTick() {
		return interruptionTick;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getInterruptNode() {
		return this.interruptNode;
	}
}
