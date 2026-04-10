package it.cnr.istc.pst.platinum.ai.executive.lang;

/**
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
	TOKEN_EXECUTION_FAILURE
	
}
