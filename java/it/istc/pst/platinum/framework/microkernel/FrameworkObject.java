package it.istc.pst.platinum.framework.microkernel;

import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FrameworkObject 
{
	@FrameworkLoggerPlaceholder
	private static FrameworkLogger logger;

	/**
	 * 
	 */
	protected FrameworkObject() {}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void debug(String msg) {
		// check if logger variable has been set
		if (logger != null) {
			logger.debug(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void warning(String msg) {
		// check if logger variable has been set
		if (logger != null) {
			logger.warning(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void info(String msg) {
		// check if logger variable has been set
		if (logger != null) {
			logger.info(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void error(String msg) {
		// check if logger variable has been set
		if (logger != null) {
			logger.error(msg);
		}
	}
}
