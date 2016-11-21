package it.uniroma3.epsl2.executive.lang;

import it.uniroma3.epsl2.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class DurationOverflow extends ExecutionFailureCause 
{
	private ExecutionNode node;
	private long observedDuration;
	
	/**
	 * 
	 * @param tick
	 * @param node
	 * @param observedDuration
	 */
	public DurationOverflow(long tick, ExecutionNode node, long observedDuration) {
		super(tick, ExecutionFailureCauseType.DURATION_OVERFLOW);
		// set value
		this.node = node;
		this.observedDuration = observedDuration;
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
	public long getObservedDuration() {
		return observedDuration;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[DurationOverflow] The observed duration exceeds the upper bound of the domain specification\n"
				+ "\t- observed-duration= " + this.observedDuration + "\n"
				+ "\t- node= " + this.node + "\n";
	}
}
