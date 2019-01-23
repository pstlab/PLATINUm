package it.istc.pst.platinum.app.control.lang;

/**
 * 
 * @author anacleto
 *
 */
public enum TaskStatus 
{
	/**
	 * a new buffered task to manage 
	 */
	TO_PLAN,
	
	/**
	 * a planned task to execute
	 */
	TO_EXECUTE,

	/**
	 * a failed task to be re-planned
	 */
	TO_REPLAN,
	
	/**
	 * a repaired task to complete execution
	 */
	TO_COMPLETE,
	
	/**
	 * a completely executed task
	 */
	EXECUTED
}
