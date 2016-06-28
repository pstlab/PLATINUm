package it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;
import it.uniroma3.epsl2.planner.heuristic.fsh.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.planner.search.SearchStrategyType;
import it.uniroma3.epsl2.planner.solver.SolverType;

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
	FlawSelectionHeuristicType heuristic() default FlawSelectionHeuristicType.HFSH;
	
	/**
	 * 
	 * @return
	 */
	SearchStrategyType strategy() default SearchStrategyType.DFS;
	
	/**
	 * 
	 * @return
	 */
	FrameworkLoggingLevel logging() default FrameworkLoggingLevel.ERROR;
}
