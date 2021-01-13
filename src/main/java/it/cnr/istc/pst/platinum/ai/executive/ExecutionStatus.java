package it.cnr.istc.pst.platinum.ai.executive;

/**
 * 
 * @author anacleto
 *
 */
public enum ExecutionStatus 
{
	/**
	 * The executive system has been created but but not initialized yet
	 */
	INACTIVE,
	
	/**
	 * The executive is being initialized
	 */
	INITIALIZING,
	
	/**
	 * The executive system has been initialized and is ready for execution
	 */
	READY,
	
	/**
	 * The executive system is executing a plan
	 */
	EXECUTING,
	
//	/**
//	 * The execution has been interrupted due to some unexpected events
//	 */
//	INTERRUPTED,
//	
//	/**
//	 * The executive is trying to repair the current plan in order to start re-planning
//	 * from a new feasible situation
//	 */
//	REPAIRING,
	
	/**
	 * Some errors occur during the execution and system is no longer able to proceed
	 */
	ERROR,
	
	
}
