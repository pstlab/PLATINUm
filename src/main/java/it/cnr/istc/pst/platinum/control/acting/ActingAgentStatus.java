package it.cnr.istc.pst.platinum.control.acting;

/**
 * Description of agent states within the life cycle of the goal-oriented planning and execution process
 * 
 * @author anacleto
 *
 */
public enum ActingAgentStatus 
{
	/**
	 * The agent is offline 
	 */
	OFFLINE,
	
	/**
	 * The agent is being started and preparing the internal data structures
	 */
	STARTING,
	
	/**
	 * The agent is being stopped and interrupting the internal threads
	 */
	STOPPING,
	
	/**
	 * The agent is being cleared by reinitializing the internal data structures and processes if necessary
	 */
	CLEARNING,
	
	/**
	 * The agent has been successfully started and can be initialized on a planning domain
	 */
	RUNNING,
	
	/**
	 * The agent is being initialized on a given planning domain
	 */
	INITIALIZING,
	
	/**
	 * The agent has been initialized successfully and ready is now ready to receiving planning goals
	 */
	READY,
	
	/**
	 * The agent has received a planning goal and is not generating a plan 
	 */
	DELIBERATING,
	
	/**
	 * The agent has generated a valid plan for an input goal and is now preparing the data structures for execution
	 */
	PREPARING_EXECUTION,
	
	/**
	 * The agent is executing a deliberated plan
	 */
	EXECUTING,
	
	/**
	 * The agent has suspended plan execution due to contingency and is trying to repair it to continue execution
	 */
	SUSPENDED
}
