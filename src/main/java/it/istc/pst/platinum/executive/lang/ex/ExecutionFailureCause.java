package it.istc.pst.platinum.executive.lang.ex;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

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
	 * @param type
	 */
	public ExecutionFailureCause(ExecutionFailureCauseType type) {
		this.type = type;
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
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FailureCause tick: " + this.interruptionTick + ", type: " + this.type + "]";
	}
}
