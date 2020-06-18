package it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.framework.time.solver.TemporalSolverType;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkType;

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
public @interface TemporalFacadeConfiguration {

	/**
	 * 
	 * @return
	 */
	TemporalNetworkType network();
	
	/**
	 * 
	 * @return
	 */
	TemporalSolverType solver();
}
