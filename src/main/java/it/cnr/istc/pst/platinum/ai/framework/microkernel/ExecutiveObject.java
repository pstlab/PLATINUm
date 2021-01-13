package it.cnr.istc.pst.platinum.ai.framework.microkernel;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLogger;

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
}
