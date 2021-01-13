package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver;

import java.lang.reflect.Constructor;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.DomainComponentPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class ResolverBuilder 
{
	/**
	 * 
	 * @param type
	 * @param tf
	 * @param pf
	 * @param c
	 * @return
	 */
	public synchronized static Resolver<?> createAndSet(ResolverType type, TemporalFacade tf, ParameterFacade pf, DomainComponent c)
	{
		// create resolver instance
		Resolver<?> r = doCreateResolver(type.getClassName());
		try
		{
			// inject reference to temporal facade
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(r, TemporalFacadePlaceholder.class, tf);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting temporal facade reference into resolver\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// inject parameter facade
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(r, ParameterFacadePlaceholder.class, pf);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting parameter facade reference into resolver\n- message: " + ex.getMessage() + "\n");
		}
		
		try 
		{
			// inject component reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(r, DomainComponentPlaceholder.class, c);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting domain component into resolver\n- message: " + ex.getMessage() + "\n");
		}
		
		try 
		{
			// finalize resolver initialization
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(r, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of resolver\n- message: " + ex.getMessage() + "\n");
		}
		
		// get resolver instance
		return r;
	}
	
	/**
	 * 
	 * @param cName
	 * @return
	 */
	private static Resolver<?> doCreateResolver(String cName)
	{
		// resolver instance
		Resolver<?> r = null;
		try 
		{
			// create resolver
			Class<?> clazz = Class.forName(cName);
			// get declared constructors
			Constructor<?> c = clazz.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			// create instance
			r = (Resolver<?>) c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating resolver from class: " + cName + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get resolver instance
		return r;
	}
}
