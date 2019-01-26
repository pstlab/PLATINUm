package it.istc.pst.platinum.control.platform.sim;

/**
 * 
 * @author anacleto
 *
 */
public enum PlatformAgentStatus 
{
	OFFLINE,
	
	IDLE,
	
	COMMAND_START_REQUEST,
	
	COMMAND_EXECUTION_REQUEST,
	
	COMMAND_EXECUTION_HANDLING,
	
	FAILURE
	
}
