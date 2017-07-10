package it.uniroma3.epsl2.deliberative.heuristic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterFactory;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.FilterPipelineReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

/**
 * 
 * @author anacleto
 *
 */
public class FlawSelectionHeuristicFactory extends ApplicationFrameworkFactory 
{
	private FlawFilterFactory factory;
	
	/**
	 * 
	 */
	public FlawSelectionHeuristicFactory() {
		super();
		this.factory = new FlawFilterFactory();
	}
	
	/**
	 * 
	 * @param type
	 */
	@SuppressWarnings("unchecked")
	public <T extends FlawSelectionHeuristic> T create(FlawSelectionHeuristicType type) 
	{
		// heuristic filter reference
		T heuristic = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getHeuristicClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			heuristic = c.newInstance();
			
			// check configuration annotation
			if (clazz.isAnnotationPresent(FlawSelectionHeuristicConfiguration.class)) {
				// get configuration annotation
				FlawSelectionHeuristicConfiguration cfg = clazz.getAnnotation(FlawSelectionHeuristicConfiguration.class);

				// inject filters
				this.injectFilters(cfg.pipeline(), heuristic);
			}
			
			// inject logger
			this.injectPlannerLoggerReference(heuristic);
			// inject plan data base reference
			this.injectSingletonPlanDataBaseReference(heuristic, false);
			// complete initialization if needed
			this.doCompleteApplicationObjectInitialization(heuristic);
			// add to registry
			this.doRegister(heuristic);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get heuristic
		return heuristic;
	}
	
	/**
	 * 
	 * @param types
	 * @param heuristic
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void injectFilters(FlawFilterType[] types, FlawSelectionHeuristic heuristic) 
			throws IllegalArgumentException, IllegalAccessException 
	{
		// create filters
		List<FlawFilter> filters = new ArrayList<>();
		for (FlawFilterType type : types) { 
			// create filter
			filters.add(this.factory.create(type));
		}
		
		// inject filters
		Field f = this.findFieldAnnotatedBy(heuristic.getClass(), FilterPipelineReference.class);
		// set reference to the filter pipeline
		f.setAccessible(true);
		f.set(heuristic, filters);
	}
}
