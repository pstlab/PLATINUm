package it.istc.pst.platinum.executive.lang.failure;

/**
 * 
 * @author anacleto
 *
 */
public enum ExecutionFailureCauseType 
{
	/**
	 * Action successful executed but with an observed duration not compliant with the plan
	 */
	DURATION_OVERFLOW,
	
	/**
	 * Synchronization error between the planned start time of an uncontrollable action and its real start
	 */
	START_OVERFLOW,
	
	/**
	 * General action execution error
	 */
	PLATFORM_ERROR
}
