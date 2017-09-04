package it.istc.pst.platinum.framework.microkernel.annotation.inject.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledgeType;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainKnowledgePlaceholder {
	
	// set the desired type of domain knowledge 
	DomainKnowledgeType type() default DomainKnowledgeType.STATIC;

	// lookup a singleton instance if necessary
	String lookup() default ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DOMAIN_KNOWLEDGE;
}
