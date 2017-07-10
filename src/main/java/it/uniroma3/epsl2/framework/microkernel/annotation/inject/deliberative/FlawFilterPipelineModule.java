package it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlawFilterPipelineModule {

	// reference to the pipeline of filters
	FlawFilterType[] pipeline();
}
