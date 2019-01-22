package it.istc.pst.platinum.app.cli;

import it.istc.pst.platinum.app.cli.ex.CommandLineInterfaceException;
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
	
	/**
	 * 
	 * @param horizon
	 */
	protected AbstractCommandLineInterface(long horizon) {
		this.langFactory = ProtocolLanguageFactory.getSingletonInstance(horizon);
		this.queryFactory = ProtocolQueryFactory.getSingletonInstance();
		this.currentSolution = null;
	}
	
	/**
	 * 
	 * @param ddl
	 * @param pdl
	 * @throws CommandLineInterfaceInitializationException
	 */
	protected void init(String ddl, String pdl) 
			throws CommandLineInterfaceException 
	{
		try 
		{
			// initialize the plan database
			this.pdb = PlanDataBaseBuilder.createAndSet(ddl, pdl);
			// clear planner
			if (this.planner != null) {
				this.planner = null;
			}
		}
		catch (SynchronizationCycleException | ProblemInitializationException ex) {
			// command line interface initialization exception
			throw new CommandLineInterfaceException(ex.getMessage());
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
		this.planner = PlannerBuilder.createAndSet(this.pdb);
		// run the planner on the desired goal
		this.currentSolution = this.planner.plan();
	}
	
	/**
	 * 
	 * @throws CommandLineInterfaceException
	 */
	protected void execute() 
			throws CommandLineInterfaceException
	{
		// check if a solution exists
		if (this.currentSolution == null && this.planner != null) {
			throw new CommandLineInterfaceException("No plan to execute!");
		}
		
		try
		{
			// create the executive 
			Executive exec = ExecutiveBuilder.createAndSet(Executive.class, 0, this.currentSolution.getHorizon());
			// initialize the executive
			exec.initialize(this.planner.export(this.currentSolution));
			// run the executive
			exec.execute();
		}
		catch (Exception ex) {
			throw new CommandLineInterfaceException(ex.getMessage());
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
		PlanProtocolDescriptor plan = this.planner.export(this.currentSolution);
		// get the plan
		return plan;
	}
}