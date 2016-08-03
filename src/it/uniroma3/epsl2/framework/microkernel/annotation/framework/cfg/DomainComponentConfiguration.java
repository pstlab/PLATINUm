package it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * Component Configuration annotation
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainComponentConfiguration {
	
	/**
	 * 
	 * @return
	 */
	ResolverType[] resolvers();
	
	/**
	 * 
	 * @return
	 */
	ComponentViewType view() default ComponentViewType.GANTT;
}
