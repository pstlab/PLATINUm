package it.uniroma3.epsl2.framework.domain;

import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentFactory;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseComponent;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.PlanDataBaseConfiguration;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.utils.compat.AnnotationUtils;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseFactory extends ApplicationFrameworkFactory 
{
	private static PlanDataBaseFactory INSTANCE = null;
	private TemporalDataBaseFacadeFactory tdbFactory;
	private ParameterDataBaseFacadeFactory pdbFactory;
	private DomainComponentFactory cFactory;
	private FrameworkLoggerFactory lFactory;
	
	/**
	 * 
	 */
	private PlanDataBaseFactory() {
		super();
		this.tdbFactory = new TemporalDataBaseFacadeFactory();
		this.pdbFactory = new ParameterDataBaseFacadeFactory();
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
		PlanDataBase pdb = null;
		try 
		{
			// get class
			Class<PlanDataBase> clazz = (Class<PlanDataBase>) Class.
					forName(DomainComponentType.PDB.getComponentClassName());
			// check annotation
			if (clazz.isAnnotationPresent(PlanDataBaseConfiguration.class)) 
			{
				// get annotation
				PlanDataBaseConfiguration cfg = AnnotationUtils.
				        getDeclaredAnnotation(clazz, PlanDataBaseConfiguration.class);
			
				// create logger
				this.lFactory.createFrameworkLogger(cfg.logging());
				
				// create temporal data base facade
				this.tdbFactory.createSingleton(cfg.tdb(), origin, horizon);
				
				// create parameter data base facade
				this.pdbFactory.createSingleton(cfg.pdb());
				
				// create component (cast needed for Java7 compatibility)
				pdb = (PlanDataBase) this.cFactory.create(name, DomainComponentType.PDB);
				// complete initialization
				this.doCompleteApplicationObjectInitialization((PlanDataBaseComponent) pdb);
				// add instance to framework registry
				this.register(SINGLETON_PLAN_DATA_BASE_REFERENCE, (PlanDataBaseComponent) pdb);
			}
			else {
				// configuration annotation not found
				throw new RuntimeException("Plan DataBase Configuration Annotation not found on class " + clazz);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (SecurityException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get plan data-base
		return pdb;
	}
}
