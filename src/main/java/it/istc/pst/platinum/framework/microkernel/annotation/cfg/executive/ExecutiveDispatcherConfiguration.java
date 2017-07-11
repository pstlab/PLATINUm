package it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.executive.PlanDispatcher;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutiveDispatcherConfiguration {

	/**
	 * 
	 * @return
	 */
	Class<? extends PlanDispatcher> dispatcher();
}
