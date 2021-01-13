package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@Target({
	ElementType.TYPE,
	ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface FrameworkLoggerConfiguration {

	/**
	 * 
	 * @return
	 */
	FrameworkLoggingLevel level() default FrameworkLoggingLevel.OFF;
}
