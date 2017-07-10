package it.uniroma3.epsl2.framework.microkernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.uniroma3.epsl2.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.lifecycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ApplicationFrameworkFactory 
{
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
	 * @param key
	 * @param obj
	 */
	protected void register(String key, ApplicationFrameworkObject obj) {
		this.container.save(key, obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	protected void register(ApplicationFrameworkObject obj) {
		this.container.save(obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void unregister(ApplicationFrameworkObject obj) {
		// clear container
		this.container.cancel(obj);
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
	
//	/**
//	 * 
//	 * @param clazz
//	 * @param obj
//	 * @throws IllegalArgumentException
//	 * @throws IllegalAccessException
//	 */
//	@SuppressWarnings("unchecked")
//	protected <T extends ApplicationFrameworkObject> void injectSingletonTemporalNetworkReference(T obj, boolean optional) 
//			throws IllegalArgumentException, IllegalAccessException 
//	{ 
//		// get class
//		Class<T> clazz = (Class<T>) obj.getClass();
//		// find annotated field
//		Field field = this.findFieldAnnotatedBy(clazz, TemporalNetworkReference.class);
//		// check if field has been found
//		if (field != null) {
//			// get temporal network reference
//			ApplicationFrameworkObject network = this.container.lookup(SINGLETON_TEMPORAL_NETWORK_REFERENCE);
//			// set network
//			field.setAccessible(true);
//			field.set(obj, network);
//		}
//		
//		// throw exception if a not optional field has not been found
//		if (field == null && !optional) {
//			throw new RuntimeException("Field tagged by TemporalNetworkReference annotation has not been found in class " + obj.getClass());
//		}
//	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectFrameworkLogger(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, FrameworkLoggerPlaceholder.class);
		// check if field has been found
		if (field != null) 
		{
			// get annotation
			FrameworkLoggerPlaceholder placeholder = field.getAnnotation(FrameworkLoggerPlaceholder.class);
			// create logger
			ApplicationFrameworkObject	logger = this.container.lookup(placeholder.lookup());
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
	protected <T extends ApplicationFrameworkObject> void injectPlanDataBase(T obj, boolean optional) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, PlanDataBasePlaceholder.class);
		// check field
		if (field != null) 
		{
			// get annotation
			PlanDataBasePlaceholder placeholder = field.getAnnotation(PlanDataBasePlaceholder.class);
			// get temporal network reference
			ApplicationFrameworkObject pdb = this.container.lookup(placeholder.lookup());
			// set plan data-based reference
			field.setAccessible(true);
			field.set(obj, pdb);
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
	protected <T extends ApplicationFrameworkObject> void injectTemporalFacade(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, TemporalFacadePlaceholder.class);
		if (field != null) 
		{
			// get annotation
			TemporalFacadePlaceholder placeholder = field.getAnnotation(TemporalFacadePlaceholder.class);
			// get temporal data-base facade reference
			ApplicationFrameworkObject tdb = this.container.lookup(placeholder.lookup());
			// set reference
			field.setAccessible(true);
			field.set(obj, tdb);
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
	protected <T extends ApplicationFrameworkObject> void injectParameterFacade(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		Field field = this.findFieldAnnotatedBy(clazz, ParameterFacadePlaceholder.class);
		if (field != null) 
		{
			// get annotation
			ParameterFacadePlaceholder placeholder = field.getAnnotation(ParameterFacadePlaceholder.class);
			// get temporal data-base facade reference
			ApplicationFrameworkObject tdb = this.container.lookup(placeholder.lookup());
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
