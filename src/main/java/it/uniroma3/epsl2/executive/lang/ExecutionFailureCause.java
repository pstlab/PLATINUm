package it.uniroma3.epsl2.executive.lang;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutionFailureCause 
{
	private ExecutionFailureCauseType type;
	private long interruptionTick;
	
	/**
	 * 
	 * @param tick
	 * @param type
	 */
	public ExecutionFailureCause(long tick, ExecutionFailureCauseType type) {
		this.type = type;
		this.interruptionTick = tick;
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
}
