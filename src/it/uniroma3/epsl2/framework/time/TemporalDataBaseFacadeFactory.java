package it.uniroma3.epsl2.framework.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.TemporalDataBaseConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalReasonerReference;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolver;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverFactory;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverType;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointQuery;
import it.uniroma3.epsl2.framework.utils.compat.AnnotationUtils;

/**
 * 
 * @author anacleto
 *
 */
public final class TemporalDataBaseFacadeFactory extends ApplicationFrameworkFactory {
	
	// temporal network factory
	private TemporalNetworkFactory tnFactory;
	private TemporalSolverFactory trfactory;

	/**
	 * 
	 */
	public TemporalDataBaseFacadeFactory() {
		super();
		this.tnFactory = new TemporalNetworkFactory();
		this.trfactory = new TemporalSolverFactory();
	}
	
	/**
	 * 
	 * @param type
	 * @param origin
	 * @param horizon
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemporalDataBaseFacade> T createSingleton(TemporalDataBaseFacadeType type, long origin, long horizon) {
		
		// temporal reasoning facade instance
		T tdb = null;
		try {
			
			// get facade's class
			Class<T> clazz = (Class<T>) Class.forName(type.getFacadeClassName());
			// get temporal data base annotation
			if (clazz.isAnnotationPresent(TemporalDataBaseConfiguration.class)) {
				
				// get constructor 
				Constructor<T> c = clazz.getDeclaredConstructor();
				// set constructor accessible
				c.setAccessible(true);
				// create new instance
				tdb = c.newInstance();
				
				// get configuration annotation
				TemporalDataBaseConfiguration cfg = AnnotationUtils.
				        getDeclaredAnnotation(clazz, TemporalDataBaseConfiguration.class);
				
				// create (singleton) temporal network 
				this.tnFactory.createSingleton(cfg.network(), origin, horizon);
				// get annotated field
				this.injectSingletonTemporalNetworkReference(tdb, false);
				
				// inject temporal reasoner
				this.injectTemporalReasoningEngine(tdb, cfg.solver());
				// inject logger
				this.injectFrameworkLoggerReference(tdb);
	
				// complete initialization
				this.doCompleteApplicationObjectInitialization(tdb);
				// add entry to the registry
				this.register(SINGLETON_TEMPORAL_FACADE_REFERENCE, tdb);
			}
			else {
				// no configuration found
				throw new RuntimeException("Temporal Data Base Configuration Annotation not found on class " + clazz);
			}
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get instance
		return tdb; 
	}
	
	/**
	 * 
	 * @param type
	 * @param origin
	 * @param horizon
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemporalDataBaseFacade> T create(TemporalDataBaseFacadeType type, long origin, long horizon) {
		
		// temporal reasoning facade instance
		T tdb = null;
		try {
			
			// get facade's class
			Class<T> clazz = (Class<T>) Class.forName(type.getFacadeClassName());
			// get temporal data base annotation
			if (clazz.isAnnotationPresent(TemporalDataBaseConfiguration.class)) {
				
				// get constructor 
				Constructor<T> c = clazz.getDeclaredConstructor();
				// set constructor accessible
				c.setAccessible(true);
				// create new instance
				tdb = c.newInstance();
				
				// get configuration annotation
				TemporalDataBaseConfiguration cfg = AnnotationUtils.
				        getDeclaredAnnotation(clazz, TemporalDataBaseConfiguration.class);
				
				// create (singleton) temporal network 
				this.tnFactory.createSingleton(cfg.network(), origin, horizon);
				// get annotated field
				this.injectSingletonTemporalNetworkReference(tdb, false);
				
				// inject temporal reasoner
				this.injectTemporalReasoningEngine(tdb, cfg.solver());
				// inject logger
				this.injectFrameworkLoggerReference(tdb);
	
				// complete initialization
				this.doCompleteApplicationObjectInitialization(tdb);
				// add entry to the registry
				this.doRegister(tdb);
			}
			else {
				// no configuration found
				throw new RuntimeException("Temporal Data Base Configuration Annotation not found on class " + clazz);
			}
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get instance
		return tdb; 
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void unregister(ApplicationFrameworkObject obj) {
		// clear container
		this.doUnregister(obj);
	}
	
	/**
	 * 
	 * @param tdb
	 * @param type
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void injectTemporalReasoningEngine(TemporalDataBaseFacade tdb, TemporalSolverType type) 
			throws IllegalArgumentException, IllegalAccessException {
		// create temporal reasoning engine
		TemporalSolver<TimePointQuery> engine = this.trfactory.create(type);
		// get annotated field
		Field field = this.findFieldAnnotatedBy(tdb.getClass(), TemporalReasonerReference.class);
		// inject temporal reasoner
		field.setAccessible(true);
		field.set(tdb, engine);
	}
}
