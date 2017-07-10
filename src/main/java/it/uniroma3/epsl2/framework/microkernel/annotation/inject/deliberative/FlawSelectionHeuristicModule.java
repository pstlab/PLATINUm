package it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristicType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlawSelectionHeuristicModule {

	// reference to heuristic
	FlawSelectionHeuristicType heuristics();
}