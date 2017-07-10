package it.uniroma3.epsl2.framework.microkernel.annotation.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface FrameworkLoggerConfiguration {

	/**
	 * 
	 * @return
	 */
	FrameworkLoggingLevel level() default FrameworkLoggingLevel.OFF;
	
	/**
	 * 
	 * @return
	 */
	boolean singleton() default true;
}
