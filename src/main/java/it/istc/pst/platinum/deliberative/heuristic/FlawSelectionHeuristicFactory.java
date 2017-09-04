package it.istc.pst.platinum.deliberative.heuristic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilter;
import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilterFactory;
import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilterType;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkFactory;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.FlawFilterPipelineModule;

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
			
			// inject logger
			this.injectFrameworkLogger(heuristic);
			// inject plan data base reference
			this.injectPlanDataBase(heuristic);
			
			// check filter pipeline
			List<Field> fields = this.findFieldsAnnotatedBy(clazz, FlawFilterPipelineModule.class);
			for (Field field : fields) {
				// get annotation
				FlawFilterPipelineModule annotation = field.getAnnotation(FlawFilterPipelineModule.class);
				// create filters
				List<FlawFilter> list = new ArrayList<FlawFilter>();
				for (FlawFilterType filterType : annotation.pipeline()) {
					// create filter
					FlawFilter filter = this.factory.create(filterType);
					list.add(filter);
				}
				
				// set field
				field.setAccessible(true);
				field.set(heuristic, list);
			}
			
			// inject domain knowledge
			this.injectDomainKnowledge(heuristic);
			// complete initialization if needed
			this.doCompleteApplicationObjectInitialization(heuristic);
			// add to registry
			this.register(heuristic);
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
}
