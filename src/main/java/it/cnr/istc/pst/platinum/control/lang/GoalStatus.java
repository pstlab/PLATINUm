package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author anacleto
 *
 */
public enum GoalStatus 
{
	/**
	 * a new buffered task to manage 
	 */
	BUFFERED,
	
	/**
	 * A goal selected for planning
	 */
	SELECTED,
	
	/**
	 * a planned goal selected for execution
	 */
	COMMITTED,
	
	/**
	 * A goal whose execution has been interrupted due some contingencies
	 */
	SUSPENDED,

	/**
	 * a completely executed goal
	 */
	FINISHED,
	
	/**
	 * An aborted goal due to planning or re-planning failures
	 */
	ABORTED;
}
