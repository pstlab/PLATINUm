package it.istc.pst.platinum.executive.lang;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class PlanRepairInformation 
{
	private ExecutionNode node;
	private long duration;
	
	/**
	 * 
	 * @param node
	 * @param duration
	 */
	public PlanRepairInformation(ExecutionNode node, long duration) {
		this.node = node;
		this.duration = duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public ExecutionNode getNode() {
		return node;
	}
}
