package it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import it.cnr.istc.pst.platinum.ai.executive.dispatcher.Dispatcher;

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
public @interface DispatcherConfiguration {

	/**
	 * 
	 * @return
	 */
	Class<? extends Dispatcher<?>> dispatcher();
}
