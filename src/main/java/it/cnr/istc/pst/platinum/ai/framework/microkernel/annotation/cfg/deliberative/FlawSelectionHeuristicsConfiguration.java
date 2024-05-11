package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.FlawSelectionHeuristic;
import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline.PipelineFlawSelectionHeuristic;

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
public @interface FlawSelectionHeuristicsConfiguration {

	// flaw selection heuristics type
	Class<? extends FlawSelectionHeuristic> heuristics() default PipelineFlawSelectionHeuristic.class;
}
