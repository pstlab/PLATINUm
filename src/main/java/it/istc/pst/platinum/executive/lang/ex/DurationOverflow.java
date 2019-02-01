package it.istc.pst.platinum.executive.lang.ex;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class DurationOverflow extends ExecutionFailureCause 
{
	private long observedDuration;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param observedDuration
	 */
	public DurationOverflow(long tick, ExecutionNode node, long observedDuration) {
		super(tick, ExecutionFailureCauseType.DURATION_OVERFLOW, node);
		this.observedDuration = observedDuration;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getObservedDuration() {
		return observedDuration;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[DurationOverflow] The observed duration exceeds the upper bound of the domain specification\n"
				+ "\t- observed-duration= " + this.observedDuration + "\n";
	}
}
