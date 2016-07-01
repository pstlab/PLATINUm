package it.uniroma3.epsl2.framework.microkernel.annotation.executive.cfg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.executive.dispatcher.PlanDispatcher;
import it.uniroma3.epsl2.executive.monitor.PlanMonitor;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutiveConfiguration {

	/**
	 * 
	 * @return
	 */
	Class<? extends PlanDispatcher> dispatcher();

	/**
	 * 
	 * @return
	 */
	Class <? extends PlanMonitor> monitor();
}
