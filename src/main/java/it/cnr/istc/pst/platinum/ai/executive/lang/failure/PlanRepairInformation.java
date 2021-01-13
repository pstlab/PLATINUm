package it.cnr.istc.pst.platinum.ai.executive.lang.failure;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author alessandroumbrico
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
	protected PlanRepairInformation(ExecutionNode node, long duration) {
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
