package it.istc.pst.platinum.executive.lang.ex;

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
	public PlatformError() {
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
