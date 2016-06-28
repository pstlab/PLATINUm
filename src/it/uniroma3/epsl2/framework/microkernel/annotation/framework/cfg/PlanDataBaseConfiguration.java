package it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeType;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlanDataBaseConfiguration {

	/**
	 * 
	 * @return
	 */
	ParameterDataBaseFacadeType pdb();
	
	/**
	 * 
	 * @return
	 */
	TemporalDataBaseFacadeType tdb();
	
	/**
	 * 
	 * @return
	 */
	FrameworkLoggingLevel logging() default FrameworkLoggingLevel.OFF;
}
