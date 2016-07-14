package it.uniroma3.epsl2.framework.domain.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentViewReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ResolverReferences;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverFactory;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.utils.compat.AnnotationUtils;
import it.uniroma3.epsl2.framework.utils.view.ComponentView;
import it.uniroma3.epsl2.framework.utils.view.ComponentViewFactory;
import it.uniroma3.epsl2.framework.utils.view.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
public class DomainComponentFactory extends ApplicationFrameworkFactory {

	private ResolverFactory resvFactory;
	private ComponentViewFactory viewFactory;
	
	/**
	 * 
	 */
	public DomainComponentFactory() {
		super();
		this.resvFactory = new ResolverFactory();
		this.viewFactory = new ComponentViewFactory(); 
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
		try {
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getComponentClassName());
			// check if configuration annotation exists
			if (clazz.isAnnotationPresent(DomainComponentConfiguration.class)) {
				// constructor
				Constructor<T> c = clazz.getDeclaredConstructor(String.class);
				c.setAccessible(true);
				// create instance
				component = c.newInstance(name);

				// get configuration annotation
				DomainComponentConfiguration cfg = clazz.getAnnotation(DomainComponentConfiguration.class);
				
				// inject logger
				this.injectFrameworkLoggerReference(component);
				// inject resolvers
				this.injectResolvers(component);
				// inject component view
				this.injectComponentView(cfg.view(), component);
				// inject temporal data base reference
				this.injectSingletonTemporalDataBaseFacadeReference(component, false);
				// inject parameter data base reference
				this.injectSingletonParameterDataBaseFacadeReference(component);
				// complete initialization
				this.completeApplicationObjectInitialization(component);
				
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
	
	/**
	 * 
	 * @param component
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void injectResolvers(DomainComponent component) 
			throws IllegalArgumentException, IllegalAccessException {
		// get annotation
		DomainComponentConfiguration annot = AnnotationUtils.getDeclaredAnnotation(
		        component.getClass(),DomainComponentConfiguration.class);
		
		// list of resolvers
		List<Resolver<?>> list = new ArrayList<>();
		for (ResolverType resvType : annot.resolvers()) {
			// create resolver
			list.add(this.resvFactory.create(resvType, component));
		}
		
		// set resolvers
		Field field = this.findFieldAnnotatedBy(component.getClass(), ResolverReferences.class);
		// set reference
		field.setAccessible(true);
		field.set(component, list);
	}
	
	/**
	 * 
	 * @param type
	 * @param component
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void injectComponentView(ComponentViewType type, DomainComponent component) 
			throws IllegalArgumentException, IllegalAccessException {
		// get component type 
		ComponentView view = this.viewFactory.create(type, component);
		// set component view
		Field field = this.findFieldAnnotatedBy(component.getClass(), ComponentViewReference.class);
		// set reference
		field.setAccessible(true);
		// inject
		field.set(component, view);
	}
 }
