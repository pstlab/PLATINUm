package it.cnr.istc.pst.platinum.ai.executive.lang;


/**
 * Enumeration of types of feedback handled by the executive during the execution of a plan
 * 
 * @author anacleto
 *
 */
public enum ExecutionFeedbackType {
	
	/**
	 * Feedback about the complete execution of a partially controllable token 
	 */
	PARTIALLY_CONTROLLABLE_TOKEN_COMPLETE,
	
	/**
	 * Feedback about the start of an uncontrollable token
	 */
	UNCONTROLLABLE_TOKEN_START,
	
	/**
	 * Feedback about the complete execution of an uncontrollable token
	 */
	UNCONTROLLABLE_TOKEN_COMPLETE,
	
	/**
	 * Feedback about failure of the execution of a token
	 */
	TOKEN_EXECUTION_FAILURE,
	
	/**
	 * Feedback about interrupt of the whole plan being executed
	 */
	PLAN_INTERRUPT
	
}
