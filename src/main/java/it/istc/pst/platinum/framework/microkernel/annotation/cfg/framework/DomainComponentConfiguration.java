package it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;

/**
 * Component Configuration annotation
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainComponentConfiguration {
	
	/**
	 * 
	 * @return
	 */
	ResolverType[] resolvers();
}
