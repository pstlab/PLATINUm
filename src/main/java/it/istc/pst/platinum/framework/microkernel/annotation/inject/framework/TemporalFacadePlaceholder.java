package it.istc.pst.platinum.framework.microkernel.annotation.inject.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.time.TemporalFacadeType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemporalFacadePlaceholder {
	
	// temporal facade type
	TemporalFacadeType type() default TemporalFacadeType.UNCERTAINTY_TEMPORAL_FACADE;

	// lookup a singleton instance if necessary
	String lookup() default ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_TEMPORAL_FACADE;
}
