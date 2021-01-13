package it.cnr.istc.pst.platinum.ai.executive.lang.failure;

/**
 * 
 * @author anacleto
 *
 */
public enum ExecutionFailureCauseType 
{
	/**
	 * General action execution error
	 */
	NODE_EXECUTION_ERROR,
	
	/**
	 * Action successful executed but with an observed duration not compliant with the plan
	 */
	NODE_DURATION_OVERFLOW,
	
	/**
	 * Synchronization error between the planned start time of an uncontrollable action and its real start
	 */
	NODE_START_OVERFLOW;
}