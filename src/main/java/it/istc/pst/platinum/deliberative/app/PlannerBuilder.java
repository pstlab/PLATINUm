package it.istc.pst.platinum.deliberative.app;

import it.istc.pst.platinum.framework.compiler.ex.PDLFileMissingException;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;

/**
 * 
 * @author anacleto
 *
 */
public class PlannerBuilder 
{
	private PlannerBuilder() {}
	
	/**
	 * 
	 * @param plannerClass
	 * @param ddlFile
	 * @param pdlFile
	 * @return
	 * @throws SynchronizationCycleException
	 * @throws ProblemInitializationException
	 * @throws PDLFileMissingException
	 */
	public static <T extends Planner> T build(String plannerClass, String ddlFile, String pdlFile) 
			throws SynchronizationCycleException, ProblemInitializationException 
	{
		// check input class
		if (plannerClass == null) {
			throw new RuntimeException("Specify a valid planner class name...");
		}
		
		// check input files
		if (ddlFile == null || pdlFile == null) {
			throw new RuntimeException("Specify valid DDL and PDL files...");
		}
		
		// build the plan data-base
		PlanDataBaseBuilder.build(ddlFile, pdlFile);
		// get the planner factory
		PlannerFactory factory = PlannerFactory.getInstance();
		// create the planner
		T planner = factory.create(plannerClass);
		// get created planning instance
		return planner;
	}
	
	/**
	 * 
	 * @param ddlFile
	 * @param pdlFile
	 * @return
	 * @throws SynchronizationCycleException
	 * @throws ProblemInitializationException
	 */
	public static Planner build(String ddlFile, String pdlFile) 
			throws SynchronizationCycleException, ProblemInitializationException 
	{
		// check input
		if (ddlFile == null || pdlFile == null) {
			throw new RuntimeException("Specify valid DDL and PDL files...");
		}
				
		// build and save the plan database
		PlanDataBaseBuilder.build(ddlFile, pdlFile);
		// get planner factory
		PlannerFactory factory = PlannerFactory.getInstance();
		// create planner
		Planner planner = factory.create(DefaultPlanner.class.getName());
		// get created planning instance
		return planner;
	}
}
