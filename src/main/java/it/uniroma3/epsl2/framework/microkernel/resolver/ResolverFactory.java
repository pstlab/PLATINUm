package it.uniroma3.epsl2.framework.microkernel.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public class ResolverFactory extends ApplicationFrameworkFactory {

	/**
	 * 
	 */
	public ResolverFactory() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Resolver> T create(ResolverType type, DomainComponent component) 
	{
		// resolver
		T resv = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			// set constructor accessible
			c.setAccessible(true);
			resv = c.newInstance();
			
			// set reference to component
			Field field = this.findFieldAnnotatedBy(clazz, ComponentPlaceholder.class);
			// inject reference
			field.setAccessible(true);
			field.set(resv, component);
			
			// inject reference to temporal facade
			this.injectTemporalFacade(resv);
			// inject parameter data base reference
			this.injectParameterFacade(resv);
			// inject logger
			this.injectFrameworkLogger(resv);
			// complete initialization
			this.doCompleteApplicationObjectInitialization(resv);
			
			// add entry to the registry
			this.register(resv);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get resolver
		return resv;
	}
}
