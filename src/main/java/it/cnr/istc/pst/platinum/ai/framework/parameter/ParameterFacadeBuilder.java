package it.cnr.istc.pst.platinum.ai.framework.parameter;

import java.lang.reflect.Constructor;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ParameterSolverPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolver;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterFacadeBuilder 
{
	/**
	 * 
	 * @param reference
	 * @return
	 */
	public synchronized static ParameterFacade createAndSet(Object reference) 
	{
		// look for configuration annotation on reference class
		ParameterFacadeConfiguration annot = FrameworkReflectionUtils.
				doFindnAnnotation(reference.getClass(), ParameterFacadeConfiguration.class);
		// check if configuration has been found
		if (annot == null) {
			// use default annotation configuration
			annot = ParameterFacade.class.getAnnotation(ParameterFacadeConfiguration.class);
		}
		
		// create parameter facade
		ParameterFacade facade = doCreateParameterFacade();
		// create parameter solver
		ParameterSolver s = doCreateParameterSolver(annot.solver().getClassName());
		
		try
		{
			// complete initialization of the parameter solver
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(s, PostConstruct.class);
			try
			{
				// inject reference to parameter solver
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, ParameterSolverPlaceholder.class, s);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting parameter solver reference into thefacade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on parameter solver\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// complete initialization of the facade
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(facade, PostConstruct.class);
			try
			{
				// inject facade reference 
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(reference, ParameterFacadeConfiguration.class, facade);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting parameter facade reference into the reference object\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of the parameter facade\n- message: " + ex.getMessage() + "\n");
		}
		
		// get facade
		return facade;
	}
	
	/**
	 * 
	 * @param annot
	 * @return
	 */
	public synchronized static ParameterFacade createAndSet(ParameterFacadeConfiguration annot) 
	{
		// create parameter facade
		ParameterFacade facade = doCreateParameterFacade();
		// create parameter solver
		ParameterSolver s = doCreateParameterSolver(annot.solver().getClassName());
		
		try
		{
			// complete initialization of the parameter solver
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(s, PostConstruct.class);
			try
			{
				// inject reference to parameter solver
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, ParameterSolverPlaceholder.class, s);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting parameter solver reference into thefacade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on parameter solver\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// complete initialization of the facade
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(facade, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of the parameter facade\n- message: " + ex.getMessage() + "\n");
		}
		
		// get facade
		return facade;
	}
	
	/**
	 * 
	 * @param className
	 * @param origin
	 * @param horizon
	 * @return
	 */
	private static ParameterFacade doCreateParameterFacade() 
	{
		// parameter facade instance
		ParameterFacade facade = null;
		try
		{
			// create the parameter facade through reflection
			Class<?> clazz = Class.forName(ParameterFacade.class.getName());
			// get declared constructor
			Constructor<?> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance 
			facade = (ParameterFacade) c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating parameter facade instance from class: " + TemporalFacade.class.getName() + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get facade
		return facade;
	}
	
	/**
	 * 
	 * @return
	 */
	private static ParameterSolver doCreateParameterSolver(String className)
	{
		// parameter solver instance
		ParameterSolver solver = null;
		try
		{
			// create the parameter solver through reflection
			Class<?> clazz = Class.forName(className);
			// get declared constructor
			Constructor<?> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance 
			solver = (ParameterSolver) c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating parameter solver instance from class: " + className + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get solver instance
		return solver;
	}
}
