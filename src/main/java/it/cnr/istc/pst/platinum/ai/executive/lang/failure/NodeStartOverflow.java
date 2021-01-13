package it.cnr.istc.pst.platinum.ai.executive.lang.failure;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class NodeStartOverflow extends ExecutionFailureCause 
{
	private long observedStartTime;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param observedStartTime
	 */
	public NodeStartOverflow(long tick, ExecutionNode node, long observedStartTime) {
		super(tick, ExecutionFailureCauseType.NODE_START_OVERFLOW, node);
		this.observedStartTime = observedStartTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getObservedStartTime() {
		return this.observedStartTime;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[StartOverflow] The observed start-time exceeds the upper bound of the plan\n"
				+ "\t- observed-start-time= " + this.observedStartTime + "\n";
	}
}
