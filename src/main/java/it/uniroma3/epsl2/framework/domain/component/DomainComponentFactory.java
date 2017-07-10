package it.uniroma3.epsl2.framework.domain.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ResolverListPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverFactory;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
public class DomainComponentFactory extends ApplicationFrameworkFactory {

	private ResolverFactory resvFactory;
	
	/**
	 * 
	 */
	public DomainComponentFactory() {
		super();
		this.resvFactory = new ResolverFactory();
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomainComponent> T create(String name, DomainComponentType type) {
		T component = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getComponentClassName());
			// check whether constructor configuration annotation is present
			Constructor<T> c = clazz.getDeclaredConstructor(String.class);
			if (c.isAnnotationPresent(DomainComponentConfiguration.class)) 
			{
				// constructor
				c.setAccessible(true);
				// create instance
				component = c.newInstance(name);

				// inject logger
				this.injectFrameworkLogger(component);
				// inject temporal facade
				this.injectTemporalFacade(component);
				// inject parameter facade
				this.injectParameterFacade(component);
				
				// get configuration annotation
				DomainComponentConfiguration annotation = clazz.getAnnotation(DomainComponentConfiguration.class);
				// prepare list of resolvers
				List<Resolver> list = new ArrayList<Resolver>();
				// check resolver
				for (ResolverType rtype : annotation.resolvers())
				{
					// create resolver
					Resolver resv = this.resvFactory.create(rtype, component);
					// add resolver
					list.add(resv);
				}
				// set reference to resolvers
				Field f = this.findFieldAnnotatedBy(clazz, ResolverListPlaceholder.class);
				// set the field accessible
				f.setAccessible(true);
				// set reference
				f.set(component, list);
				
				
				// complete initialization
				this.doCompleteApplicationObjectInitialization(component);
				
				// add entry to registry
				this.register(component);
			}
			else {
				// configuration annotation not found
				throw new RuntimeException("Domain Component Configuration Annotation not found on class " + clazz);
			}
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get component
		return component;
	}
 }
