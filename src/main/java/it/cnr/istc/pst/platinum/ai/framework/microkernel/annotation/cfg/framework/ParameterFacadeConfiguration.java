package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;

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
public @interface ParameterFacadeConfiguration {

	/**
	 * 
	 * @return
	 */
	ParameterSolverType solver();
}
