package it.uniroma3.epsl2.deliberative.heuristic.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class FlawFilterFactory extends ApplicationFrameworkFactory {

	/**
	 * 
	 */
	public FlawFilterFactory() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends FlawFilter> T create(FlawFilterType type) 
	{
		// filter instance reference
		T filter = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getFilterClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			filter = c.newInstance();
			
			// inject framework logger if needed
			this.injectFrameworkLogger(filter);
			// inject plan data-base if needed
			this.injectPlanDataBase(filter);
			// complete initialization if needed
			this.doCompleteApplicationObjectInitialization(filter);
			// add to registry
			this.register(filter);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get builder reference
		return filter;
	}
}
