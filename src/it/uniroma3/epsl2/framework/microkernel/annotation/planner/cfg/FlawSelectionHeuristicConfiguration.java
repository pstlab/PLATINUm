package it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.planner.heuristic.fsh.filter.FlawFilterType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlawSelectionHeuristicConfiguration {

	/**
	 * 
	 * @return
	 */
	FlawFilterType[] pipeline();
}
