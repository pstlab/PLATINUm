package it.uniroma3.epsl2.framework.time.tn.solver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalNetworkReference;
import it.uniroma3.epsl2.framework.microkernel.query.Query;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetwork;

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
	public <T extends TemporalSolver<? extends Query>> T create(TemporalSolverType type) {
		// temporal reasoning engine
		T reasoner = null;
		try {
			// get reasoning class
			Class<T> clazz = (Class<T>) Class.forName(type.getClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			reasoner = c.newInstance();
			
			// inject logger
			this.injectFrameworkLoggerReference(reasoner);
			// inject temporal network reference
			this.injectSingletonTemporalNetworkReference(reasoner, false);
			// complete initialization
			this.doCompleteApplicationObjectInitialization(reasoner);
			// add entry to the registry
			this.doRegister(reasoner);
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
	
	/**
	 * 
	 * @param type
	 * @param tn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemporalSolver<? extends Query>> T create(TemporalSolverType type, TemporalNetwork tn) {
		// temporal reasoning engine
		T reasoner = null;
		try {
			// get reasoning class
			Class<T> clazz = (Class<T>) Class.forName(type.getClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			reasoner = c.newInstance();
			
			// inject temporal network reference
			Field field = this.findFieldAnnotatedBy(clazz, TemporalNetworkReference.class);
			field.setAccessible(true);
			field.set(reasoner, tn);
			// complete initialization
			this.doCompleteApplicationObjectInitialization(reasoner);
			// add entry to the registry
			this.doRegister(reasoner);
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
