package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.Solver;

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
public @interface PlannerSolverConfiguration {

	// reference to solver algorithm
	Class<? extends Solver> solver();
	
	// set a timeout for the solver (in milliseconds)
	long timeout() default -1;
}
