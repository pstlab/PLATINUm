package it.uniroma3.epsl2.framework.parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ParameterSolverPlaceholder;
import it.uniroma3.epsl2.framework.parameter.csp.solver.ParameterSolver;
import it.uniroma3.epsl2.framework.parameter.csp.solver.ParameterSolverFactory;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterFacadeFactory extends ApplicationFrameworkFactory 
{
	// parameter solver factory
	private ParameterSolverFactory pFactory;
	
	/**
	 * 
	 */
	public ParameterFacadeFactory() {
		super();
		this.pFactory = new ParameterSolverFactory();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterFacade> T create(ParameterFacadeType type) {
		// the facade
		T facade = null;
		try 
		{
			// get facade's class
			Class<T> clazz = (Class<T>) Class.forName(type.getFacadeClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			// check whether constructor annotations are present
			if (c.isAnnotationPresent(ParameterFacadeConfiguration.class))
			{
				// set constructor accessible
				c.setAccessible(true);
				// create instance
				facade = c.newInstance();
				
				// inject logger
				this.injectFrameworkLogger(facade);
				
				// get annotation
				ParameterFacadeConfiguration annotation = c.getAnnotation(ParameterFacadeConfiguration.class);
				// create parameter solver
				ParameterSolver solver = this.pFactory.create(annotation.solver());
				// set reference
				List<Field> fields = this.findFieldsAnnotatedBy(clazz, ParameterSolverPlaceholder.class);
				for (Field field : fields) {
					// set accessible
					field.setAccessible(true);
					// set reference to solver
					field.set(facade, solver);
				}
				
				// complete initialization
				this.doCompleteApplicationObjectInitialization(facade);
				// add entry to the registry
				this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PARAMETER_FACADE, facade);
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
