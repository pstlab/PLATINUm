package it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverType;

/**
 * Temporal Data Base Configuration annotation
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemporalDataBaseConfiguration {
	
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
