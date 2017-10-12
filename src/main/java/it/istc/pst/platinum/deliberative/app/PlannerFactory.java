package it.istc.pst.platinum.deliberative.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.istc.pst.platinum.deliberative.solver.Solver;
import it.istc.pst.platinum.deliberative.solver.SolverFactory;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkFactory;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.SolverModule;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggerFactory;

/**
 * 
 * @author anacleto
 *
 */
public class PlannerFactory extends ApplicationFrameworkFactory 
{
	private static PlannerFactory INSTANCE = null;
	private SolverFactory solverFactory;
	private FrameworkLoggerFactory loggerFactory;

	/**
	 * 
	 */
	protected PlannerFactory() {
		// set solver factory
		this.solverFactory = new SolverFactory();
		// get logger factory
		this.loggerFactory = new FrameworkLoggerFactory();
	}
	
	/**
	 * 
	 */
	public static PlannerFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlannerFactory();
		}
		return INSTANCE;
	}
	
	
	
	/**
	 * 
	 * @param plannerClassName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Planner> T create(String plannerClass) 
	{
		// the planner instance
		T planner = null;
		try 
		{
			// get planner class
			Class<T> clazz = (Class<T>) Class.forName(plannerClass);
			Constructor<T> c = clazz.getDeclaredConstructor();
			// create instance
			c.setAccessible(true);
			planner = c.newInstance();
			
			// check if annotation exist
			if (c.isAnnotationPresent(FrameworkLoggerConfiguration.class)) 
			{
				// get annotation
				FrameworkLoggerConfiguration annotation = c.getAnnotation(FrameworkLoggerConfiguration.class);
				// create logger
				this.loggerFactory.createDeliberativeLogger(annotation.level());
			}
			
			// inject logger
			this.injectFrameworkLogger(planner);
			// inject plan data-base
			this.injectPlanDataBase(planner);
			
			// set solvers
			List<Field> fields = this.findFieldsAnnotatedBy(clazz, SolverModule.class);
			for (Field field : fields) {
				// get annotation
				SolverModule annotation = field.getAnnotation(SolverModule.class);
				// create solver
				Solver solver = this.solverFactory.create(annotation.solver(), annotation.timeout());
				// set field
				field.setAccessible(true);
				field.set(planner, solver);
			}
	
			// complete initialization
			this.doCompleteApplicationObjectInitialization(planner);
			// register the planner
			this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE, planner);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get planner instance
		return planner;
	}
}
