package it.uniroma3.epsl2.framework.parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.ParameterDataBaseFacadeConfiguration;
import it.uniroma3.epsl2.framework.parameter.solver.ParameterSolverFactory;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterDataBaseFacadeFactory extends ApplicationFrameworkFactory {

	// parameter solver factory
	private ParameterSolverFactory pFactory;
	
	/**
	 * 
	 */
	public ParameterDataBaseFacadeFactory() {
		super();
		this.pFactory = new ParameterSolverFactory();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterDataBaseFacade> T createSingleton(ParameterDataBaseFacadeType type) {
		// the facade
		T facade = null;
		try {
			// get facade's class
			Class<T> clazz = (Class<T>) Class.forName(type.getFacadeClassName());
			// get configuration annotation
			if (clazz.isAnnotationPresent(ParameterDataBaseFacadeConfiguration.class))
			{
				// get constructor
				Constructor<T> c = clazz.getDeclaredConstructor();
				c.setAccessible(true);
				facade = c.newInstance();
				
				// get configuration annotation
				ParameterDataBaseFacadeConfiguration cfg = clazz.getDeclaredAnnotation(ParameterDataBaseFacadeConfiguration.class);
				
				// create solver
				this.pFactory.create(cfg.solver());
				this.injectSingletonParameterReasonerReference(facade);
				
				// inject logger
				this.injectFrameworkLoggerReference(facade);
				// complete initialization
				this.completeApplicationObjectInitialization(facade);
				// add entry to the registry
				this.register(SINGLETON_PARAMETER_FACADE_REFERENCE, facade);
			}
			else {
				// no configuration found
				throw new RuntimeException("ParameterDataBase configuration anotation not found on class " + clazz);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get facade
		return facade;
	}
}
