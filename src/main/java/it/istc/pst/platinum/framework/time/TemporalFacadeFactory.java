package it.istc.pst.platinum.framework.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkFactory;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalNetworkPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalSolverPlaceholder;
import it.istc.pst.platinum.framework.time.solver.TemporalSolver;
import it.istc.pst.platinum.framework.time.solver.TemporalSolverFactory;
import it.istc.pst.platinum.framework.time.tn.TemporalNetwork;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public final class TemporalFacadeFactory extends ApplicationFrameworkFactory 
{
	// temporal network factory
	private TemporalNetworkFactory tnFactory;
	// temporal solver factory
	private TemporalSolverFactory trfactory;

	/**
	 * 
	 */
	public TemporalFacadeFactory() {
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
	public <T extends TemporalFacade> T create(TemporalFacadeType type, long origin, long horizon) 
	{
		// temporal reasoning facade instance
		T tdb = null;
		try 
		{
			// get facade's class
			Class<T> clazz = (Class<T>) Class.forName(type.getFacadeClassName());
			// get constructor 
			Constructor<T> c = clazz.getDeclaredConstructor();
			// check if needed annotations are present
			if (c.isAnnotationPresent(TemporalFacadeConfiguration.class))
			{
				// set constructor accessible
				c.setAccessible(true);
				// create new instance
				tdb = c.newInstance();
				

				// inject singleton framework logger
				this.injectFrameworkLogger(tdb);
				
				// get constructor annotation
				TemporalFacadeConfiguration annotation = c.getAnnotation(TemporalFacadeConfiguration.class);
				
				// create the temporal network 
				TemporalNetwork tn = this.tnFactory.create(annotation.network(), origin, horizon);
				// inject reference to the network
				List<Field> fields = this.findFieldsAnnotatedBy(clazz, TemporalNetworkPlaceholder.class);
				for (Field field : fields) {
					// set accessible
					field.setAccessible(true);
					// set reference
					field.set(tdb, tn);
				}
				
				// create the temporal reasoner
				TemporalSolver<?> solver = this.trfactory.create(annotation.solver());
				// inject reference to the solver
				fields = this.findFieldsAnnotatedBy(clazz, TemporalSolverPlaceholder.class);
				for (Field field : fields) {
					// set accessible 
					field.setAccessible(true);
					// set reference
					field.set(tdb, solver);
				}

				// complete initialization
				this.doCompleteApplicationObjectInitialization(tdb);
				// add entry to the registry
				this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_TEMPORAL_FACADE, tdb);
			}
			else {
				// configuration annotation not found
				throw new RuntimeException("TemporalFacadeConfiguration annotation found on constructors");
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
}
