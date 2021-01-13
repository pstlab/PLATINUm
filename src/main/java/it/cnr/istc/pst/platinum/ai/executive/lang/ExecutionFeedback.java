package it.cnr.istc.pst.platinum.ai.executive.lang;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutionFeedback implements Comparable<ExecutionFeedback>
{
	private long tick;
	private ExecutionNode node;
	private ExecutionFeedbackType result;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param result
	 */
	public ExecutionFeedback(long tick, ExecutionNode node, ExecutionFeedbackType result) {
		this.tick = tick;
		this.node = node;
		this.result = result;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTick() {
		return tick;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getNode() {
		return node;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFeedbackType getType() {
		return result;
	}
	
	@Override
	public int compareTo(ExecutionFeedback o) {
		return this.tick < o.tick ? -1 : this.tick > o.tick ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ExecutionFeedback tick: " +  this.tick + ", event: " + node.getGroundSignature() + ", type: " + this.getType() + "]";
	}
}
