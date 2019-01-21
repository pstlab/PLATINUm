package it.istc.pst.platinum.executive.lang;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutionFeedback 
{
	private ExecutionNode node;
	private ExecutionFeedbackType result;
	
	/**
	 * 
	 * @param task
	 * @param node
	 * @param result
	 */
	public ExecutionFeedback(ExecutionNode node, ExecutionFeedbackType result) {
		this.node = node;
		this.result = result;
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
}
