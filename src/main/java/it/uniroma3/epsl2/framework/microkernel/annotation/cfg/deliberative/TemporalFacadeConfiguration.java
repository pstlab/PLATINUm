package it.uniroma3.epsl2.framework.microkernel.annotation.cfg.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.time.TemporalFacadeType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemporalFacadeConfiguration {

	/**
	 * 
	 * @return
	 */
	TemporalFacadeType facade();
	
	/**
	 * 
	 * @return
	 */
	boolean singleton() default true;
}
