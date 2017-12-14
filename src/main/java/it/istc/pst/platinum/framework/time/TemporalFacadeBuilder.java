package it.istc.pst.platinum.framework.time;

import java.lang.reflect.Constructor;

import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalNetworkPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalSolverPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.time.solver.TemporalSolver;
import it.istc.pst.platinum.framework.time.tn.TemporalNetwork;
import it.istc.pst.platinum.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalFacadeBuilder 
{
	/**
	 * 
	 * @param reference
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static TemporalFacade createAndSet(Object reference, long origin, long horizon) 
	{
		// look for configuration annotation on reference class
		TemporalFacadeConfiguration annot = FrameworkReflectionUtils.
				doFindConfigurationAnnotation(reference.getClass(), TemporalFacadeConfiguration.class);
		// check if configuration has been found
		if (annot == null) {
			// use default annotation configuration
			annot = TemporalFacade.class.getAnnotation(TemporalFacadeConfiguration.class);
		}
		
		// create temporal facade 
		TemporalFacade facade = doCreateTemporalFacade();
		// create temporal network instance
		TemporalNetwork tn = doCreateTemporalNetwork(annot.network().getClassName(), origin, horizon);
					
		try
		{
			
			// complete initialization of the temporal network
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(tn, PostConstruct.class);
			try
			{
				// inject temporal network reference
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, TemporalNetworkPlaceholder.class, tn);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting temporal network reference into the temporal facade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on temporal network\n- message: " + ex.getMessage() + "\n");
		}

		
		// create temporal solver
		TemporalSolver<?> s = doCreateTemporalSolver(annot.solver().getClassName(), tn);
		try
		{
			// complete initialization of the temporal solver
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(s, PostConstruct.class);
			try
			{
				// inject temporal solver reference
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, TemporalSolverPlaceholder.class, s);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting temporal solver reference into the temporal facade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on temporal solver\n- message: " + ex.getMessage() + "\n");
		}
		
		
		try
		{
			// complete initialization of the temporal facade
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(facade, PostConstruct.class);
			try
			{
				// inject facade reference
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(reference, TemporalFacadePlaceholder.class, facade);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting temporal facade reference into the reference object\n- reference-class= " + reference.getClass().getName() + "]\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of the temporal facade\n- message: " + ex.getMessage() + "\n");
		}
			
		
		// get facade
		return facade;
	}
	
	/**
	 * 
	 * @param annot
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static TemporalFacade createAndSet(TemporalFacadeConfiguration annot, long origin, long horizon) 
	{
		// create temporal facade 
		TemporalFacade facade = doCreateTemporalFacade();
		// create temporal network instance
		TemporalNetwork tn = doCreateTemporalNetwork(annot.network().getClassName(), origin, horizon);
					
		try
		{
			
			// complete initialization of the temporal network
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(tn, PostConstruct.class);
			try
			{
				// inject temporal network reference
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, TemporalNetworkPlaceholder.class, tn);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting temporal network reference into the temporal facade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on temporal network\n- message: " + ex.getMessage() + "\n");
		}

		
		// create temporal solver
		TemporalSolver<?> s = doCreateTemporalSolver(annot.solver().getClassName(), tn);
		try
		{
			// complete initialization of the temporal solver
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(s, PostConstruct.class);
			try
			{
				// inject temporal solver reference
				FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(facade, TemporalSolverPlaceholder.class, s);
			}
			catch (Exception ex) {
				throw new RuntimeException("Error while injecting temporal solver reference into the temporal facade\n- message: " + ex.getMessage() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on temporal solver\n- message: " + ex.getMessage() + "\n");
		}
		
		
		try
		{
			// complete initialization of the temporal facade
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(facade, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of the temporal facade\n- message: " + ex.getMessage() + "\n");
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
	private static TemporalFacade doCreateTemporalFacade() 
	{
		// temporal network instance
		TemporalFacade facade = null;
		try
		{
			// create the temporal facade through reflection
			Class<?> clazz = Class.forName(TemporalFacade.class.getName());
			// get declared constructor
			Constructor<?> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance 
			facade = (TemporalFacade) c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating temporal facade instance from class: " + TemporalFacade.class.getName() + "\n- message: " + ex.getMessage() + "\n");
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
	private static TemporalNetwork doCreateTemporalNetwork(String className, long origin, long horizon) 
	{
		// temporal network instance
		TemporalNetwork tn = null;
		try
		{
			// create the temporal network through reflection
			Class<?> clazz = Class.forName(className);
			// get declared constructor
			Constructor<?> c = clazz.getDeclaredConstructor(Long.TYPE, Long.TYPE);
			c.setAccessible(true);
			// create instance 
			tn = (TemporalNetwork) c.newInstance(origin, horizon);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating temporal network instance from class: " + className + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get network
		return tn;
	}
	
	/**
	 * 
	 * @param className
	 * @param tn
	 * @return
	 */
	private static TemporalSolver<?> doCreateTemporalSolver(String className, TemporalNetwork tn) 
	{
		// temporal solver instance
		TemporalSolver<?> s = null;
		try
		{
			// create temporal solver through reflection
			Class<?> clazz = Class.forName(className);
			// get declared constructors
			Constructor<?> c = clazz.getDeclaredConstructor(TemporalNetwork.class);
			c.setAccessible(true);
			// create instance
			s = (TemporalSolver<?>) c.newInstance(tn);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating temporal solver from class: " + className + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get solver
		return s;
	}
}
