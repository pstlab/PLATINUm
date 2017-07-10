package it.uniroma3.epsl2.framework.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentFactory;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseComponent;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.deliberative.ParameterFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.deliberative.TemporalFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.parameter.ParameterFacadeFactory;
import it.uniroma3.epsl2.framework.time.TemporalFacadeFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseFactory extends ApplicationFrameworkFactory 
{
	private static PlanDataBaseFactory INSTANCE = null;
	private TemporalFacadeFactory tdbFactory;
	private ParameterFacadeFactory pdbFactory;
	private DomainComponentFactory cFactory;
	private FrameworkLoggerFactory lFactory;
	
	/**
	 * 
	 */
	private PlanDataBaseFactory() {
		super();
		this.tdbFactory = new TemporalFacadeFactory();
		this.pdbFactory = new ParameterFacadeFactory();
		this.cFactory = new DomainComponentFactory();
		this.lFactory = new FrameworkLoggerFactory();
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
			Constructor<PlanDataBase> c = clazz.getDeclaredConstructor();
			// check whether constructor configuration annotations are present
			if (c.isAnnotationPresent(TemporalFacadeConfiguration.class) && 
					c.isAnnotationPresent(ParameterFacadeConfiguration.class) && 
					c.isAnnotationPresent(FrameworkLoggerConfiguration.class) && 
					c.isAnnotationPresent(DomainComponentConfiguration.class)) 
			{
				// get logger annotation
				FrameworkLoggerConfiguration lannot = c.getAnnotation(FrameworkLoggerConfiguration.class);
				// create logger
				this.lFactory.createFrameworkLogger(lannot.level());
				// get temporal facade annotation
				TemporalFacadeConfiguration tannot = c.getAnnotation(TemporalFacadeConfiguration.class);
				// create facade
				this.tdbFactory.create(tannot.facade(), origin, horizon);
				// get parameter facade annotation
				ParameterFacadeConfiguration pannot = c.getAnnotation(ParameterFacadeConfiguration.class);
				this.pdbFactory.create(pannot.facade());
				
				// create component
				pdb = this.cFactory.create(name, DomainComponentType.PDB);
				// complete initialization
				this.doCompleteApplicationObjectInitialization((PlanDataBaseComponent) pdb);
				// add instance to framework registry
				this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE, pdb);
			}
			else {
				// configuration annotation not found
				throw new RuntimeException("Plan DataBase Configuration Annotation not found on class " + clazz);
			}
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
