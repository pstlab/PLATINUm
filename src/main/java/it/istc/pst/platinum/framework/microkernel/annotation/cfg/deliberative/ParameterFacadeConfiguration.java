package it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.parameter.ParameterFacadeType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterFacadeConfiguration {

	/**
	 * 
	 * @return
	 */
	ParameterFacadeType facade();
	
	/**
	 * 
	 * @return
	 */
	boolean singletion() default true;
}
