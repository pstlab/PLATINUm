package it.istc.pst.platinum.executive.lang.failure;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class StartOverflow extends ExecutionFailureCause 
{
	private long observedStartTime;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param observedStartTime
	 */
	public StartOverflow(long tick, ExecutionNode node, long observedStartTime) {
		super(tick, ExecutionFailureCauseType.START_OVERFLOW, node);
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
