package it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.istc.pst.platinum.executive.PlanMonitor;

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
public @interface ExecutiveMonitorConfiguration {

	/**
	 * 
	 * @return
	 */
	Class <? extends PlanMonitor> monitor();
}
