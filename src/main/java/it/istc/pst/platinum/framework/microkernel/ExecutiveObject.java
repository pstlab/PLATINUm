package it.istc.pst.platinum.framework.microkernel;

import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutiveObject 
{
	@FrameworkLoggerPlaceholder
	private static FrameworkLogger logger;

	/**
	 * 
	 */
	protected ExecutiveObject() {}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void error(String msg) {
		if (logger != null) {
			logger.error(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void warning(String msg) {
		if (logger != null) {
			logger.warning(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void debug(String msg) {
		if (logger != null) {
			logger.debug(msg);
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	protected static void info(String msg) {
		if (logger != null) {
			logger.info(msg);
		}
	}
}
