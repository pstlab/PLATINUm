package it.cnr.istc.pst.platinum.ai.framework.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anacleto
 *
 */
public class FrameworkReflectionUtils 
{
	/**
	 * 
	 * @param oClass
	 * @param aClass
	 * @return
	 */
	public static <T extends Annotation> T doFindnAnnotation(Class<?> oClass, Class<T> aClass)
	{
		// set annotation
		T annot = null;
		
		// Look for annotation on class declaration
		List<Class<?>> classes = doFindClassAnnotatedBy(oClass, aClass);
		if (!classes.isEmpty()) {
			// one class expected
			Class<?> c = classes.get(0);
			// get annotation
			annot = c.getAnnotation(aClass);
		}
		
		// check if annotation has been found
		if (annot == null) {
			// look for annotation on constructor declaration
			List<Constructor<?>> constructors = doFindConstructorsAnnotatedBy(oClass, aClass);
			if (!constructors.isEmpty()) {
				// one constructor expected
				Constructor<?> c = constructors.get(0);
				// get annotation
				annot = c.getAnnotation(aClass);
			}
		}
		
		// check if annotation has been found
		if (annot == null) {
			// look for annotation on fields
			List<Field> fields = doFindFieldsAnnotatedBy(oClass, aClass);
			if (!fields.isEmpty()) {
				// one field expected
				Field field = fields.get(0);
				// get annotation
				annot = field.getAnnotation(aClass);
			}
		}
		
		// return reference to annotation
		return annot;
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
	public static void doInvokeMethodTaggedWithAnnotation(Object obj, Class<? extends Annotation> annot) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		// call post construct method
		boolean found = false;
		// get class
		Class<?> clazz = obj.getClass();
		// find method
		for (Method m : clazz.getDeclaredMethods()) 
		{
			// check annotation
			found = m.isAnnotationPresent(annot);
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
					found = m.isAnnotationPresent(annot);
					if (found) {
						// invoke method
						m.setAccessible(true);
						m.invoke(obj, new Object[] {});
					}
				}
				
				// update current class
				current = current.getSuperclass();
			}
		}
	}
	
	/**
	 * 
	 * @param objClass
	 * @param annot
	 * @param optional
	 * @return
	 */
	public static List<Field> doFindFieldsAnnotatedBy(Class<?> rClass, Class<? extends Annotation> annot) 
	{
		// set the list of fields
		List<Field> list = new ArrayList<Field>();
		// get object class
		Class<?> clazz = rClass;
		for (Field field : clazz.getDeclaredFields()) {
			// check annotation
			if (field.isAnnotationPresent(annot)) {
				list.add(field);
			}
		}
		
		// check super classes if no field has been found
		if (list.isEmpty()) 
		{
			// start from current class
			Class<?> current = clazz;
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
		}
		
		// get field
		return list;
	}
	
	/**
	 * 
	 * @param reference
	 * @param annot
	 * @return
	 */
	public static List<Constructor<?>> doFindConstructorsAnnotatedBy(Class<?> rClass, Class<? extends Annotation> annot)
	{
		// initialize the list of constructors
		List<Constructor<?>> list = new ArrayList<Constructor<?>>();
		// get object class
		Class<?> clazz = rClass;
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			// check annotation
			if (c.isAnnotationPresent(annot)) {
				list.add(c);
			}
		}
		
		// check super classes if no constructor has been found
		if (list.isEmpty()) 
		{
			// start from current class
			Class<?> current = clazz;
			while (current.getSuperclass() != null) {
				// check constructors
				for (Constructor<?> c : current.getSuperclass().getDeclaredConstructors()) {
					// check annotation
					if (c.isAnnotationPresent(annot)) {
						list.add(c);
					}
				}
				
				// update current
				current = current.getSuperclass();
			}
		}
		
		// get field
		return list;
	}

	/**
	 * 
	 * @param reference
	 * @param annot
	 * @return
	 */
	public static List<Class<?>> doFindClassAnnotatedBy(Class<?> rClass, Class<? extends Annotation> annot)
	{
		// initialize the list of class declarations
		List<Class<?>> list = new ArrayList<Class<?>>();
		// get object class
		Class<?> clazz = rClass;
		if (clazz.isAnnotationPresent(annot)) {
			list.add(clazz);
		}
		
		// check super classes if no constructor has been found
		if (list.isEmpty()) 
		{
			// start from current class
			Class<?> current = clazz;
			while (current.getSuperclass() != null) {
				// check super class
				if (current.getSuperclass().isAnnotationPresent(annot)) {
					list.add(current.getSuperclass());
				}
					
				// update current
				current = current.getSuperclass();
			}
		}
		
		// get field
		return list;
	}
	
	
	/**
	 * 
	 * @param reference
	 * @param annot
	 * @param target
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void doInjectReferenceThroughAnnotation(Object reference, Class<? extends Annotation> annot, Object target) 
			throws IllegalArgumentException, IllegalAccessException
	{
		// first of all find fields annotated with the desired annotation
		List<Field> fields = doFindFieldsAnnotatedBy(reference.getClass(), annot);
		// inject target reference
		for (Field field : fields) {
			// inject reference
			field.setAccessible(true);
			field.set(reference, target);
		}
	}
	
	/**
	 * 
	 * @param refClass
	 * @param annot
	 * @param target
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void doInjectStaticReferenceThroughAnnotation(Class<?> refClass, Class<? extends Annotation> annot, Object target) 
			throws IllegalArgumentException, IllegalAccessException
	{
		// first of all find fields annotated with the desired annotation
		List<Field> fields = doFindFieldsAnnotatedBy(refClass, annot);
		// inject target reference
		for (Field field : fields) {
			// inject reference
			field.setAccessible(true);
			field.set(null, target);
		}
	}
}
