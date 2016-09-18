package it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.solver.SolverType;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlannerConfiguration {

	/**
	 * 
	 * @return
	 */
	SolverType solver() default SolverType.PSEUDO_CONTROLLABILITY_AWARE;
	
	/**
	 * 
	 * @return
	 */
	FlawSelectionHeuristicType heuristic() default FlawSelectionHeuristicType.HTFSH;
	
	/**
	 * 
	 * @return
	 */
	SearchStrategyType strategy() default SearchStrategyType.DFS;
	
	/**
	 * 
	 * @return
	 */
	FrameworkLoggingLevel logging() default FrameworkLoggingLevel.DEBUG;
}
