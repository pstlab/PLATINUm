package it.istc.pst.platinum.framework.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseComponent;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkFactory;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.ParameterFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeFactory;
import it.istc.pst.platinum.framework.time.TemporalFacadeFactory;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggerFactory;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseFactory extends ApplicationFrameworkFactory 
{
	private static PlanDataBaseFactory INSTANCE = null;
	private TemporalFacadeFactory temporalFacadeFactory;
	private ParameterFacadeFactory parameterFacadeFactory;
	private DomainComponentFactory componentFactory;
	private FrameworkLoggerFactory loggerFactory;
	
	/**
	 * 
	 */
	private PlanDataBaseFactory() {
		super();
		this.temporalFacadeFactory = new TemporalFacadeFactory();
		this.parameterFacadeFactory = new ParameterFacadeFactory();
		this.componentFactory = new DomainComponentFactory();
		this.loggerFactory = new FrameworkLoggerFactory();
	}
	
	/**
	 * 
	 * @return
	 */
	public static PlanDataBaseFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlanDataBaseFactory();
		}
		// get instance
		return INSTANCE;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PlanDataBase create(String name, long origin, long horizon) 
	{
		PlanDataBaseComponent pdb = null;
		try 
		{
			// get class
			Class<PlanDataBase> clazz = (Class<PlanDataBase>) Class.
					forName(DomainComponentType.PDB.getComponentClassName());
			
			// get declared constructor
			Constructor<PlanDataBase> c = clazz.getDeclaredConstructor(String.class);
			// create logger if necessary
			if (c.isAnnotationPresent(FrameworkLoggerConfiguration.class)) {
				// get annotation
				FrameworkLoggerConfiguration annotation = c.getAnnotation(FrameworkLoggerConfiguration.class);
				// create framework logger
				this.loggerFactory.createFrameworkLogger(annotation.level());
			}
			
			// create temporal facade if necessary
			if (c.isAnnotationPresent(TemporalFacadeConfiguration.class)) {
				// get annotation
				TemporalFacadeConfiguration annotation = c.getAnnotation(TemporalFacadeConfiguration.class);
				// create temporal facade
				this.temporalFacadeFactory.create(annotation.facade(), origin, horizon);
			}
			
			// create parameter facade if necessary
			if (c.isAnnotationPresent(ParameterFacadeConfiguration.class)) {
				// get annotation
				ParameterFacadeConfiguration annotation = c.getAnnotation(ParameterFacadeConfiguration.class);
				// create parameter facade
				this.parameterFacadeFactory.create(annotation.facade());
			}
			
			// finalize plan data-based as "normal" domain component
			pdb = this.componentFactory.create(name, DomainComponentType.PDB);
			// complete initialization
			this.doCompleteApplicationObjectInitialization((PlanDataBaseComponent) pdb);
			// add instance to framework registry
			this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE, pdb);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get plan data-base
		return pdb;
	}
}
