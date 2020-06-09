package it.istc.pst.platinum.executive.lang.failure;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class PlatformError extends ExecutionFailureCause 
{
	/**
	 * 
	 */
	public PlatformError(long tick, ExecutionNode node) {
		super(ExecutionFailureCauseType.PLATFORM_ERROR);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PlatformError] Error while communicating with the platform\n";
	}
}
