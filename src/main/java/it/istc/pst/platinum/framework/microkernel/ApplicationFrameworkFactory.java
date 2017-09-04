package it.istc.pst.platinum.framework.microkernel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledgeFactory;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.DomainKnowledgePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;

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
		List<Field> fields = this.findFieldsAnnotatedBy(clazz, FrameworkLoggerPlaceholder.class);
		// check if field has been found
		for (Field field : fields) {
			// get annotation
			FrameworkLoggerPlaceholder placeholder = field.getAnnotation(FrameworkLoggerPlaceholder.class);
			// create logger
			ApplicationFrameworkObject	logger = this.container.lookup(placeholder.lookup());
			// set network
			field.setAccessible(true);
			field.set(obj, logger);
		}
		
	
	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectPlanDataBase(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		List<Field> fields = this.findFieldsAnnotatedBy(clazz, PlanDataBasePlaceholder.class);
		// check field
		for (Field field : fields) {
			// get annotation
			PlanDataBasePlaceholder placeholder = field.getAnnotation(PlanDataBasePlaceholder.class);
			// get temporal network reference
			ApplicationFrameworkObject pdb = this.container.lookup(placeholder.lookup());
			// set plan data-based reference
			field.setAccessible(true);
			field.set(obj, pdb);
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationFrameworkObject> void injectDomainKnowledge(T obj) 
			throws IllegalArgumentException, IllegalAccessException 
	{ 
		// get class
		Class<T> clazz = (Class<T>) obj.getClass();
		// find annotated field
		List<Field> fields = this.findFieldsAnnotatedBy(clazz, DomainKnowledgePlaceholder.class);
		// check field
		for (Field field : fields) 
		{
			// get annotation
			DomainKnowledgePlaceholder placeholder = field.getAnnotation(DomainKnowledgePlaceholder.class);
			// get reference
			ApplicationFrameworkObject element = this.container.lookup(placeholder.lookup());
			// check if already instantiated
			if (element == null) {
				// create factory
				DomainKnowledgeFactory knowledgeFactory = new DomainKnowledgeFactory();
				// create domain knowledge instance
				element = knowledgeFactory.create(placeholder.lookup(), placeholder.type());
			}
			
			// set plan data-based reference
			field.setAccessible(true);
			field.set(obj, element);
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
		List<Field> fields = this.findFieldsAnnotatedBy(clazz, TemporalFacadePlaceholder.class);
		for (Field field : fields) {
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
		List<Field> fields = this.findFieldsAnnotatedBy(clazz, ParameterFacadePlaceholder.class);
		// set fields 
		for (Field field : fields) {
			// get annotation
			ParameterFacadePlaceholder placeholder = field.getAnnotation(ParameterFacadePlaceholder.class);
			// get temporal data-base facade reference
			ApplicationFrameworkObject tdb = this.container.lookup(placeholder.lookup());
			// set reference
			field.setAccessible(true);
			field.set(obj, tdb);
		}
	}
	
	/**
	 * 
	 * @param objClass
	 * @param annot
	 * @param optional
	 * @return
	 */
	protected List<Field> findFieldsAnnotatedBy(Class<? extends ApplicationFrameworkObject> objClass, Class<? extends Annotation> annot) 
	{
		// inject temporal network reference
		List<Field> list = new ArrayList<Field>();
		for (Field field : objClass.getDeclaredFields()) {
			// check annotation
			if (field.isAnnotationPresent(annot)) {
				list.add(field);
			}
		}
		
		// check super classes
		Class<?> current = objClass;
		while (current.getSuperclass() != null) {
			// check fields
			for (Field field : current.getSuperclass().getDeclaredFields()) {
				// check annotation
				if (field.isAnnotationPresent(annot)) {
					list.add(field);
				}
			}
			
			// update current
			current = current.getSuperclass();
		}
		
		// get field
		return list;
	}
}
