package it.uniroma3.epsl2.executive.lang;

import it.uniroma3.epsl2.executive.pdb.ExecutionNode;

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
		super(tick, node, ExecutionFailureCauseType.DURATION_OVERFLOW);
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
				+ "\t- observed-start-time= " + this.observedStartTime + "\n"
				+ "\t- node= " + this.getInterruptNode() + "\n";
	}
}
