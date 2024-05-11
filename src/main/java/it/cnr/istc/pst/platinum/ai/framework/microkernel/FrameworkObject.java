package it.cnr.istc.pst.platinum.ai.framework.microkernel;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author alessandro
 *
 */
public abstract class FrameworkObject 
{
	// set framework home
	protected static final String FRAMEWORK_HOME = System.getenv("PLATINUM_HOME") != null ?
			System.getenv("PLATINUM_HOME") + "/" : "";
	
	@FrameworkLoggerPlaceholder
	private static FrameworkLogger logger;
	
	protected String frameworkHome;

	/**
	 * 
	 */
	protected FrameworkObject() {}
	
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
