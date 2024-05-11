package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline.FlawInspector;

/**
 * 
 * @author anacleto
 *
 */
@Target({
	ElementType.TYPE,
	ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface PipelineConfiguration {

	// reference to the pipeline of filters
	Class<? extends FlawInspector>[] pipeline();
}
