package it.uniroma3.epsl2.framework.time.solver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.query.Query;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalSolverFactory extends ApplicationFrameworkFactory {

	/**
	 * 
	 */
	public TemporalSolverFactory() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @param tn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemporalSolver<? extends Query>> T create(TemporalSolverType type) 
	{
		// temporal reasoning engine
		T reasoner = null;
		try 
		{
			// get reasoning class
			Class<T> clazz = (Class<T>) Class.forName(type.getClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			reasoner = c.newInstance();
			
			// inject framework logger if necessary
			this.injectFrameworkLogger(reasoner);
			// complete initialization
			this.doCompleteApplicationObjectInitialization(reasoner);
			// add entry to the registry
			this.register(reasoner);
		}
		catch (SecurityException | NoSuchMethodException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get reasoner
		return reasoner;
	}
}
