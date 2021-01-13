package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;

/**
 * Component Configuration annotation
 * 
 * @author anacleto
 *
 */
@Target({
	ElementType.TYPE,
	ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainComponentConfiguration {
	
	/**
	 * 
	 * @return
	 */
	ResolverType[] resolvers();
}
