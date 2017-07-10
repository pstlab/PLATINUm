package it.uniroma3.epsl2.deliberative.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import it.istc.pst.epsl.microkernel.annotation.PlannerConfiguration;
import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristic;
import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristicFactory;
import it.uniroma3.epsl2.deliberative.solver.Solver;
import it.uniroma3.epsl2.deliberative.solver.SolverType;
import it.uniroma3.epsl2.deliberative.strategy.SearchStrategy;
import it.uniroma3.epsl2.deliberative.strategy.SearchStrategyFactory;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.FlawSelectionHeuristicModule;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.SearchStrategyModule;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;

/**
 * 
 * @author anacleto
 *
 */
public class PlannerFactory extends ApplicationFrameworkFactory {

	private static PlannerFactory INSTANCE = null;
	private SearchStrategyFactory strategyFactory;
	private FlawSelectionHeuristicFactory heuristicFactory;
	private FrameworkLoggerFactory loggerFactory;

	/**
	 * 
	 */
	protected PlannerFactory() {
		// set heuristic factory
		this.heuristicFactory = new FlawSelectionHeuristicFactory();
		// set strategy factory
		this.strategyFactory = new SearchStrategyFactory();
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
			if (clazz.isAnnotationPresent(PlannerConfiguration.class)) 
			{
				// get annotation
				PlannerConfiguration cfg = clazz.getAnnotation(PlannerConfiguration.class);
				
				// create planner logger logger
				this.loggerFactory.createPlannerLogger(cfg.logging());
				// inject references
				this.injectSingletonPlanDataBaseReference(planner, false);
				
				// create strategy
				SearchStrategy strategy = this.strategyFactory.create(cfg.strategy());
				// create heuristic
				FlawSelectionHeuristic heuristic = this.heuristicFactory.create(cfg.heuristic());
				// create solver
				Solver solver = this.create(cfg.solver(), heuristic, strategy);
				
				// set solver
				Field f = this.findFieldAnnotatedBy(Planner.class, Solver.class);
				f.setAccessible(true);
				f.set(planner, solver);
	
				// complete initialization
				this.doCompleteApplicationObjectInitialization(planner);
				// register the planner
				this.doRegister(planner);
			}
			else {
				
				// use default planner
				throw new RuntimeException("Specify a planner configuration annotation for the planner " + plannerClass + " otherwise use the default planner builder");
			}
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
	
	/**
	 * 
	 * @param ddl
	 * @param pdl
	 * @return
	 */
	public Planner create() {
		// create instance
		Planner planner = null;
		try 
		{
			// create planner
			planner = new Planner();
			// get annotation
			PlannerConfiguration cfg = planner.getClass().getAnnotation(PlannerConfiguration.class);
			
			// create planner logger logger
			this.loggerFactory.createPlannerLogger(cfg.logging());
			// inject references
			this.injectSingletonPlanDataBaseReference(planner, false);
			
			// create strategy
			SearchStrategy strategy = this.strategyFactory.create(cfg.strategy());
			// create heuristic
			FlawSelectionHeuristic heuristic = this.heuristicFactory.create(cfg.heuristic());
			// create solver
			Solver solver = this.create(cfg.solver(), heuristic, strategy);
			
			// set solver
			Field f = this.findFieldAnnotatedBy(Planner.class, Solver.class);
			f.setAccessible(true);
			f.set(planner, solver);

			// complete initialization
			this.doCompleteApplicationObjectInitialization(planner);
			// register the planner
			this.doRegister(planner);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get planner
		return planner;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends Solver> T create(SolverType type, FlawSelectionHeuristic heuristic, SearchStrategy strategy) {
		// set solver
		T solver;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getSolverClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			solver = c.newInstance();
			
			// inject heuristic
			Field f = this.findFieldAnnotatedBy(clazz, FlawSelectionHeuristicModule.class);
			f.setAccessible(true);
			f.set(solver, heuristic);
			
			// inject strategy
			f = this.findFieldAnnotatedBy(clazz, SearchStrategyModule.class);
			f.setAccessible(true);
			f.set(solver, strategy);
			
			// inject logger
			this.injectPlannerLoggerReference(solver);
			
			// inject plan data base reference
			this.injectSingletonPlanDataBaseReference(solver, false);
			// complete initialization if needed
			this.doCompleteApplicationObjectInitialization(solver);
			// add to registry
			this.doRegister(solver);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get solver
		return solver;
	}
}
