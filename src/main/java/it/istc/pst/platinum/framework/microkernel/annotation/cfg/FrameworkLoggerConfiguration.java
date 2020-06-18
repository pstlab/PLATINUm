package it.istc.pst.platinum.framework.microkernel.annotation.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

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
