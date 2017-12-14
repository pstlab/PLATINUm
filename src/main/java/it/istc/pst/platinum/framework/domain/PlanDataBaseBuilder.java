package it.istc.pst.platinum.framework.domain;

import java.lang.reflect.Constructor;

import it.istc.pst.platinum.framework.compiler.DomainCompilerFactory;
import it.istc.pst.platinum.framework.compiler.DomainCompilerType;
import it.istc.pst.platinum.framework.compiler.ddl.v3.DDLv3Compiler;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseComponent;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.sv.PrimitiveStateVariable;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.problem.Problem;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeBuilder;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.TemporalFacadeBuilder;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;
import it.istc.pst.platinum.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseBuilder 
{
	// domain compiler type
	private static final DomainCompilerType DEFAULT_COMPILER = DomainCompilerType.DDLv3;
	
	
	/**
	 * 
	 * @param ddl
	 * @param pdl
	 * @return
	 * @throws SynchronizationCycleException
	 * @throws ProblemInitializationException
	 */
	public synchronized static PlanDataBase createAndSet(String ddl, String pdl) 
			throws SynchronizationCycleException, ProblemInitializationException
	{
		// get compiler factory
		DomainCompilerFactory factory = DomainCompilerFactory.getInstance();
		// create compiler
		DDLv3Compiler compiler = factory.create(DEFAULT_COMPILER, ddl, pdl);
		
		// compile domain
		PlanDataBase pdb = compiler.compileDomain();
		// compile problem
		Problem problem = compiler.compileProblem(pdb);
		// setup problem
		pdb.setup(problem);
		// get resulting plan database
		return pdb;
	}
	
	/**
	 * 
	 * @param ddl
	 * @return
	 * @throws SynchronizationCycleException
	 */
	public synchronized static PlanDataBase createAndSet(String ddl) 
			throws SynchronizationCycleException
	{
		// get compiler factory
		DomainCompilerFactory factory = DomainCompilerFactory.getInstance();
		// create compiler
		DDLv3Compiler compiler = factory.create(DEFAULT_COMPILER, ddl);
		
		// compile domain
		PlanDataBase pdb = compiler.compileDomain();
		// get resulting plan database
		return pdb;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static PlanDataBase createAndSet(String name, long origin, long horizon)
	{
		// get temporal facade configuration
		TemporalFacadeConfiguration tAnnot = FrameworkReflectionUtils.doFindConfigurationAnnotation(PlanDataBaseComponent.class, TemporalFacadeConfiguration.class);
		if (tAnnot == null) {
			throw new RuntimeException("Error while creating plan database:\n- message: Temporal facade configuration annotation not found\n");
		}
		
		// create temporal facade
		TemporalFacade tf = TemporalFacadeBuilder.createAndSet(tAnnot, origin, horizon);
		
		// get parameter facade configuration
		ParameterFacadeConfiguration pAnnot = FrameworkReflectionUtils.doFindConfigurationAnnotation(PlanDataBaseComponent.class, ParameterFacadeConfiguration.class);
		if (pAnnot == null) {
			throw new RuntimeException("Error while creating plan database:\n- message: Parameter facade configuration annotation not found\n");
		}
		
		// create parameter facade
		ParameterFacade pf = ParameterFacadeBuilder.createAndSet(pAnnot);


		// create plan database component instance
		PlanDataBaseComponent comp = DomainComponentBuilder.createAndSet(name, DomainComponentType.PLAN_DATABASE, tf, pf);
		
		
		// get framework logger configuration
		FrameworkLoggerConfiguration lAnnot = FrameworkReflectionUtils.doFindConfigurationAnnotation(comp.getClass(), FrameworkLoggerConfiguration.class);
		if (lAnnot == null) {
			throw new RuntimeException("Error while creating plan database:\n- message: Framework logger configuration annotation not found\n");
		}
		
		try
		{
			// create logger
			FrameworkLogger logger = doCreateFrameworkLogger(lAnnot.level());
			// inject logger reference
			FrameworkReflectionUtils.doInjectStaticReferenceThroughAnnotation(comp.getClass(), FrameworkLoggerPlaceholder.class, logger);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while injecting logger into the framework:\n- message: " + ex.getMessage() + "\n");
		}
		
		
		try 
		{
			// finalize plan database initialization
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(comp, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method of the plan database\n- message: " + ex.getMessage() + "\n");
		}
		
		// get created domain component
		return comp;
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	private static FrameworkLogger doCreateFrameworkLogger(FrameworkLoggingLevel level)
	{
		// initialize logging instance
		FrameworkLogger logger = null;
		try
		{
			// get logger constructor
			Constructor<FrameworkLogger> c = FrameworkLogger.class.getDeclaredConstructor(FrameworkLoggingLevel.class);
			// set accessible
			c.setAccessible(true);
			// create instance
			logger = c.newInstance(level);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating framework loggerfrom class: " + logger.getClass().getName() + "\n- message: " + ex.getMessage() + "\n");
		}
		
		// get logger
		return logger;
	}
	
	
	
	
	
	
	public static void main(String[] args)
	{
		PlanDataBase pdb = PlanDataBaseBuilder.createAndSet("PDB", 0, 100);
		PrimitiveStateVariable sv1 = pdb.createDomainComponent("SV1", DomainComponentType.SV_PRIMITIVE);
		pdb.addDomainComponent(sv1);
		DiscreteResource res1 = pdb.createDomainComponent("REs1", DomainComponentType.RESOURCE_DISCRETE);
		pdb.addDomainComponent(res1);
		
		System.out.println(">>>> " + pdb + "\n");
	}
}
