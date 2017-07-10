package it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlanDataBasePlaceholder {

	// lookup a singleton instance if necessary
	String lookup() default "";
}
