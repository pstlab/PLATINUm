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
	protected static FrameworkLogger logger;

	/**
	 * 
	 */
	protected ExecutiveObject() {}
}
