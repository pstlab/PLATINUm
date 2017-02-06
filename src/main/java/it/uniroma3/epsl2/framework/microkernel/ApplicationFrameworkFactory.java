package it.uniroma3.epsl2.framework.microkernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterReasonerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalNetworkReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ApplicationFrameworkFactory 
{
	protected static final String SINGLETON_TEMPORAL_NETWORK_REFERENCE = "singleton://application/framework/time/network";
	protected static final String SINGLETON_TEMPORAL_FACADE_REFERENCE = "singleton://application/framework/time/facade";

	protected static final String SINGLETON_PARAMETER_SOLVER_REFERENCE = "singleton://application/framework/parameter/solver";
	protected static final String SINGLETON_PARAMETER_FACADE_REFERENCE = "singleton://application/framework/parameter/facade";
	
	protected static final String SINGLETON_PLAN_DATA_BASE_REFERENCE = "singleton://application/framework/pdb";
	
	protected static final String SINGLETON_FRAMEWORK_LOGGER_REFERENCE = "singleton//application/framework/logger";	
	protected static final String SINGLETON_PLANNER_LOGGER_REFERENCE = "singleton://application/planner/logger";
	protected static final String SINGLETON_EXECUTIVE_LOGGER_REFERNECE = "singleton://application/executive/logger";
	
	// application container
	private ApplicationFrameworkContainer container;
	
	/**
	 * 
	 */
	protected ApplicationFrameworkFactory() {
		this.container = ApplicationFrameworkContainer.getInstance();
	}
	
	/**
	 * 
	 * @param obj
	 */
	protected void doRegister(ApplicationFrameworkObject obj) {
		this.container.save(obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	protected void doUnregister(ApplicationFrameworkObject obj) {
		// check if object in container
		this.container.cancel(obj);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	protected void register(String key, ApplicationFrameworkObject obj) {
		this.container.save(key, obj);
	}
	
	/**
	 * Complete the initialization of an application object by calling the 
	 * post construct method
	 * 
	 * @param clazz
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void doCompleteApplicationObjectInitialization(T obj) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		// call post construct method
		boolean found = false;
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find method
		for (Method m : clazz.getDeclaredMethods()) {
			// check annotation
			found = m.isAnnotationPresent(PostConstruct.class);
			if (found) {
				// invoke method
				m.setAccessible(true);
				m.invoke(obj, new Object[]{});
				break;
			}
		}
		
		// check if method has been found
		if (!found) 
		{
			// check super classes
			Class<?> current = clazz;
			while (current.getSuperclass() != null && !found) 
			{
				// check super-class methods
				for (Method m : current.getSuperclass().getDeclaredMethods()) 
				{
					// check annotation
					found = m.isAnnotationPresent(PostConstruct.class);
					if (found) {
						// invoke method
						m.setAccessible(true);
						m.invoke(obj, new Object[] {});
						break;
					}
				}
				
				// update current class
				current = current.getSuperclass();
			}
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectSingletonTemporalNetworkReference(T obj, boolean optional) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, TemporalNetworkReference.class);
		// check if field has been found
		if (field != null) {
			// get temporal network reference
			ApplicationFrameworkObject network = this.container.lookup(SINGLETON_TEMPORAL_NETWORK_REFERENCE);
			// set network
			field.setAccessible(true);
			field.set(obj, network);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null && !optional) {
			throw new RuntimeException("Field tagged by TemporalNetworkReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectPlannerLoggerReference(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, FrameworkLoggerReference.class);
		// check if field has been found
		if (field != null) {
			// get logger reference
			// create logger
			ApplicationFrameworkObject	logger = this.container.lookup(SINGLETON_PLANNER_LOGGER_REFERENCE);
			// set network
			field.setAccessible(true);
			field.set(obj, logger);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null) {
			throw new RuntimeException("Field tagged by FrameworkLoggerReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectFrameworkLoggerReference(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, FrameworkLoggerReference.class);
		// check if field has been found
		if (field != null) 
		{
			// get logger reference
			// create logger
			ApplicationFrameworkObject	logger = this.container.lookup(SINGLETON_FRAMEWORK_LOGGER_REFERENCE);
			// set network
			field.setAccessible(true);
			field.set(obj, logger);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null) {
			throw new RuntimeException("Field tagged by FrameworkLoggerReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @param optional
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectSingletonPlanDataBaseReference(T obj, boolean optional) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, PlanDataBaseReference.class);
		// check field
		if (field != null) {
			// get temporal network reference
			ApplicationFrameworkObject network = this.container.lookup(SINGLETON_PLAN_DATA_BASE_REFERENCE);
			// set network
			field.setAccessible(true);
			field.set(obj, network);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null && !optional) {
			throw new RuntimeException("Field tagged by PlanDataBaseReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectSingletonTemporalDataBaseFacadeReference(T obj, boolean optional) 
			throws IllegalArgumentException, IllegalAccessException 
	{
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, TemporalDataBaseFacadeReference.class);
		if (field != null) {
			// get temporal data-base facade reference
			ApplicationFrameworkObject tdb = this.container.lookup(SINGLETON_TEMPORAL_FACADE_REFERENCE);
			// set reference
			field.setAccessible(true);
			field.set(obj, tdb);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null && !optional) {
			throw new RuntimeException("Field tagged by TemporalDataBaseFacadeReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @param optional
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectSingletonParameterDataBaseFacadeReference(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, ParameterDataBaseFacadeReference.class);
		if (field != null) {
			// get temporal data-base facade reference
			ApplicationFrameworkObject tdb = this.container.lookup(SINGLETON_PARAMETER_FACADE_REFERENCE);
			// set reference
			field.setAccessible(true);
			field.set(obj, tdb);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null) {
			throw new RuntimeException("Field tagged by TemporalDataBaseFacadeReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectSingletonParameterReasonerReference(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, ParameterReasonerReference.class);
		// check if field has been found
		if (field != null) {
			// get logger reference
			// create logger
			ApplicationFrameworkObject	logger = this.container.lookup(SINGLETON_PARAMETER_SOLVER_REFERENCE);
			// set network
			field.setAccessible(true);
			field.set(obj, logger);
		}
		
		// throw exception if a not optional field has not been found
		if (field == null) {
			throw new RuntimeException("Field tagged by FrameworkLoggerReference annotation has not been found in class " + obj.getClass());
		}
	}
	
	/**
	 * 
	 * @param objClass
	 * @param annot
	 * @param optional
	 * @return
	 */
	protected Field findFieldAnnotatedBy(Class<? extends ApplicationFrameworkObject> objClass, Class<? extends Annotation> annot) 
	{
		// inject temporal network reference
		Field field = null;
		for (Field f : objClass.getDeclaredFields()) {
			// check annotation
			if (f.isAnnotationPresent(annot)) {
				field = f;
				break;
			}
		}
		
		// check if injection has been done
		if (field == null) {
			// check super classes
			Class<?> current = objClass;
			while (current.getSuperclass() != null && field == null) {
				// check fields
				for (Field f : current.getSuperclass().getDeclaredFields()) {
					// check annotation
					if (f.isAnnotationPresent(annot)) {
						field = f;
						break;	// exit cycle
					}
				}
				
				// update current
				current = current.getSuperclass();
			}
		}
		
		// get field
		return field;
	}
}
