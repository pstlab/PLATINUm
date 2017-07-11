package it.uniroma3.epsl2.framework.microkernel.annotation.cfg.executive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.uniroma3.epsl2.executive.PlanMonitor;

/**
 * 
 * @author anacleto
 *
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutiveMonitorConfiguration {

	/**
	 * 
	 * @return
	 */
	Class <? extends PlanMonitor> monitor();
}
