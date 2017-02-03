package it.uniroma3.epsl2.framework.parameter.csp.solver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterSolverFactory extends ApplicationFrameworkFactory 
{
	/**
	 * 
	 */
	public ParameterSolverFactory() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterSolver> T create(ParameterSolverType type) {
		// parameter reasoner
		T reasoner = null;
		try 
		{
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterSolverClassName());
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			reasoner = c.newInstance();
			
			// inject logger
			this.injectFrameworkLoggerReference(reasoner);
			// complete initialization
			this.doCompleteApplicationObjectInitialization(reasoner);
			// add (singleton) entry to the registry
			this.register(SINGLETON_PARAMETER_SOLVER_REFERENCE, reasoner);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get the reasoner
		return reasoner;
	}
}
