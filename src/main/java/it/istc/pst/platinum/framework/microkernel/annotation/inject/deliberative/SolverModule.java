package it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.deliberative.solver.SolverType;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolverModule {
	
	// reference to solver algorithm
	SolverType solver();
	
	// set a timeout for the solver
	long timeout() default -1;
}
