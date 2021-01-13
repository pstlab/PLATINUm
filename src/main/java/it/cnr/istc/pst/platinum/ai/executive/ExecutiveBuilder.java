package it.cnr.istc.pst.platinum.ai.executive;

import java.lang.reflect.Constructor;

import it.cnr.istc.pst.platinum.ai.executive.dispatcher.Dispatcher;
import it.cnr.istc.pst.platinum.ai.executive.monitor.Monitor;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutivePlanDataBase;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutivePlanDataBaseBuilder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.DispatcherPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.ExecutivePlanDataBasePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.MonitorPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLogger;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutiveBuilder 
{
	/**
	 * 
	 * @return
	 */
	public synchronized static <T extends Executive> T createAndSet(Class<T> eClass, long origin, long horizon) {
		// create executive
		return ExecutiveBuilder.createAndSet(eClass, ExecutivePlanDataBase.class, origin, horizon);
	}
	
	/**
	 * 
	 * @param eClass
	 * @param pdbClass
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static <T extends Executive, V extends ExecutivePlanDataBase> T createAndSet(Class<T> eClass, Class<V> pdbClass, long origin, long horizon)
	{
		// create executive plan database
		V pdb = ExecutivePlanDataBaseBuilder.createAndSet(pdbClass, origin, horizon);
		
		// initialize executive
		T executive = ExecutiveBuilder.doCreateExecutive(eClass);
		// get framework logger configuration
		FrameworkLoggerConfiguration lAnnot = FrameworkReflectionUtils.doFindnAnnotation(eClass, FrameworkLoggerConfiguration.class);
		// create framework logger
		FrameworkLogger logger = ExecutiveBuilder.doCreateFrameworkLogger(lAnnot.level());
		try
		{
			// inject static reference to logger
			FrameworkReflectionUtils.doInjectStaticReferenceThroughAnnotation(eClass, FrameworkLoggerPlaceholder.class, logger);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting static logger reference:\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// inject executive plan database
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(executive, ExecutivePlanDataBasePlaceholder.class, pdb);
		}
		catch (Exception ex) {
			throw new RuntimeException("");
		}
		
		
		// get dispatcher configuration
		DispatcherConfiguration dAnnot = FrameworkReflectionUtils.doFindnAnnotation(eClass, DispatcherConfiguration.class);
		// create dispatcher
		Dispatcher dispatcher = ExecutiveBuilder.doCreateDispatcher(dAnnot.dispatcher());
		try
		{
			// inject executive reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(dispatcher, ExecutivePlaceholder.class, executive);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting executive reference into dispatcher:\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// finalize dispatcher
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(dispatcher, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct on dispatcher:\n- message: " + ex.getMessage() + "\n");
		}
		
		try 
		{
			// inject dispatcher reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(executive, DispatcherPlaceholder.class, dispatcher);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting dispatcher reference into executive from class " + eClass.getName() + ":\n- message: " + ex.getMessage() + "\n");
		}
		
		
		
		// get monitor configuration
		MonitorConfiguration mAnnot = FrameworkReflectionUtils.doFindnAnnotation(eClass, MonitorConfiguration.class);
		// create monitor
		Monitor monitor = ExecutiveBuilder.doCreateMonitor(mAnnot.monitor());
		try
		{
			// inject executive reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(monitor, ExecutivePlaceholder.class, executive);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting executive reference into monitor:\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// finalize monitor
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(monitor, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct on monitor:- message: " + ex.getMessage() + "\n");
		}
		
		
		try
		{
			// inject monitor reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(executive, MonitorPlaceholder.class, monitor);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting monitor reference into executive from class " + eClass.getName() + ":\n- message: " + ex.getMessage() + "\n");
		}
		
		
		try
		{
			// finalize executive 
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(executive, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on executive:\n- message: " + ex.getMessage() + "\n");
		}
		
		// get executive instance
		return executive;
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	private static FrameworkLogger doCreateFrameworkLogger(FrameworkLoggingLevel level)
	{
		// initialize logging instance
		FrameworkLogger logger = null;
		try
		{
			// get logger constructor
			Constructor<FrameworkLogger> c = FrameworkLogger.class.getDeclaredConstructor(FrameworkLoggingLevel.class);
			// set accessible
			c.setAccessible(true);
			// create instance
			logger = c.newInstance(level);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating framework logger from class: " + logger.getClass().getName() + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get logger
		return logger;
	}
	
	/**
	 * 
	 * @param eClass
	 * @return
	 */
	private static synchronized <T extends Executive> T doCreateExecutive(Class<T> eClass) {
		// instance
		T exec = null;
		try
		{
			// get constructor
			Constructor<T> c = (Constructor<T>) eClass.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			// create instance
			exec = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating executive plan database instance:\n- message: " + ex.getMessage() + "\n");
		}
		
		// get instance
		return exec;
	}
	
	/**
	 * 
	 * @param dClass
	 * @return
	 */
	private static synchronized <T extends Dispatcher> T doCreateDispatcher(Class<T> dClass) {
		// instance
		T dispatcher = null;
		try
		{
			// get constructor
			Constructor<T> c = (Constructor<T>) dClass.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			// create instance
			dispatcher = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating dispatcher instance:\n- message: " + ex.getMessage() + "\n");
		}
		
		// get instance
		return dispatcher;
	}
	
	/**
	 * 
	 * @param dClass
	 * @return
	 */
	private static synchronized <T extends Monitor> T doCreateMonitor(Class<T> mClass) {
		// instance
		T monitor = null;
		try
		{
			// get constructor
			Constructor<T> c = (Constructor<T>) mClass.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			// create instance
			monitor = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating monitor instance:\n- message: " + ex.getMessage() + "\n");
		}
		
		// get instance
		return monitor;
	}
}
