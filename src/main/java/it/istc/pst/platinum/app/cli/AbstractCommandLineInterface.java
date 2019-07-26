package it.istc.pst.platinum.app.cli;

import it.istc.pst.platinum.app.cli.ex.CommandLineInterfaceException;
import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.RunnablePlatformProxy;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.ProtocolLanguageFactory;
import it.istc.pst.platinum.framework.protocol.query.ProtocolQueryFactory;


/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class AbstractCommandLineInterface 
{
	protected ProtocolLanguageFactory langFactory;
	protected ProtocolQueryFactory queryFactory;
	protected PlanDataBase pdb;
	protected Planner planner;
	protected SolutionPlan currentSolution;
	protected RunnablePlatformProxy proxy;
	
	/**
	 * 
	 * @param horizon
	 */
	protected AbstractCommandLineInterface(long horizon) 
	{
		this.langFactory =  new ProtocolLanguageFactory(horizon);
		this.queryFactory = ProtocolQueryFactory.getSingletonInstance();
		this.currentSolution = null;
		this.planner = null;
		this.pdb = null;
		this.proxy = null;
	}
	
	/**
	 * 
	 * @param ddl
	 * @param pdl
	 * @throws CommandLineInterfaceInitializationException
	 */
	protected void init(String ddl, String pdl, Class<? extends RunnablePlatformProxy> pClass) 
			throws CommandLineInterfaceException 
	{
		try 
		{
			// create proxy
			this.proxy = PlatformProxyBuilder.build(pClass);
			
			// clear plan database
			this.pdb = null;
			// initialize the plan database
			this.pdb = PlanDataBaseBuilder.createAndSet(ddl, pdl);
			// clear planner
			this.planner = null;
			this.currentSolution = null;
		}
		catch (SynchronizationCycleException | ProblemInitializationException | PlatformException ex) {
			// command line interface initialization exception
			throw new CommandLineInterfaceException(ex.getMessage());
		}
		finally {
			// call garbage collector
			System.gc();
		}
	}
	
	/**
	 * 
	 * @param ddl
	 * @param pdl
	 * @param pClass
	 * @param cfgFile
	 * @throws CommandLineInterfaceException
	 */
	protected void init(String ddl, String pdl, Class<? extends RunnablePlatformProxy> pClass, String cfgFile) 
			throws CommandLineInterfaceException 
	{
		try 
		{
			// create proxy
			this.proxy = PlatformProxyBuilder.build(pClass, cfgFile);
			
			// clear plan database
			this.pdb = null;
			// initialize the plan database
			this.pdb = PlanDataBaseBuilder.createAndSet(ddl, pdl);
			// clear planner
			this.planner = null;
			this.currentSolution = null;
		}
		catch (SynchronizationCycleException | ProblemInitializationException | PlatformException ex) {
			// command line interface initialization exception
			throw new CommandLineInterfaceException(ex.getMessage());
		}
		finally {
			// call garbage collector
			System.gc();
		}
	}
	
	/**
	 * 
	 * @throws CommandLineInterfaceInitializationException
	 * @throws NoSolutionFoundException
	 */
	protected void plan() 
			throws CommandLineInterfaceException, NoSolutionFoundException 
	{
		// check planner
		if (this.pdb == null) {
			throw new CommandLineInterfaceException("No planning domain set!");
		}
		
		// initialize the planner
		this.planner = PlannerBuilder.createAndSet(Planner.class, this.pdb);
		// run the planner on the desired goal
		this.currentSolution = this.planner.plan();
	}
	
	/**
	 * 
	 * @throws CommandLineInterfaceException
	 */
	protected void execute(String path) 
			throws CommandLineInterfaceException
	{
		// check if a solution exists
		if (this.currentSolution == null && this.planner == null) {
			throw new CommandLineInterfaceException("No plan to execute!");
		}
		
		try
		{
			// check if proxy has been set
			if (this.proxy != null) {
				// start simulator
				this.proxy.start();
			}
			
			// create the executive 
			Executive exec = ExecutiveBuilder.createAndSet(Executive.class, 0, this.currentSolution.getHorizon());
			// initialize the executive
			exec.initialize(this.currentSolution.export());
			
			// bind the executive to the platform
			exec.link(this.proxy);
			// run the executive
			exec.execute();
		}
		catch (Exception ex) {
			throw new CommandLineInterfaceException(ex.getMessage());
		}
		finally 
		{
			try 
			{
				// check simulator to stop
				if (this.proxy != null) {
					// stop simulator
					this.proxy.stop();
				}
			}
			catch (PlatformException ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws CommandLineInterfaceInitializationException
	 */
	protected PlanProtocolDescriptor export() 
			throws CommandLineInterfaceException
	{
		// check if a solution has been generated
		if (this.currentSolution == null) {
			throw new CommandLineInterfaceException("No soluion to extract!");
		}
		
		// generate plan descriptor 
		PlanProtocolDescriptor plan = this.currentSolution.export();
		// get the plan
		return plan;
	}
}
