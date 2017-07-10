package it.uniroma3.epsl2.framework.domain;

import it.uniroma3.epsl2.framework.compiler.DomainCompilerFactory;
import it.uniroma3.epsl2.framework.compiler.DomainCompilerType;
import it.uniroma3.epsl2.framework.compiler.ddl.v3.DDLv3Compiler;
import it.uniroma3.epsl2.framework.compiler.ex.PDLFileMissingException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.ProblemInitializationException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.uniroma3.epsl2.framework.microkernel.lang.problem.Problem;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseBuilder 
{
	// domain compiler type
	private static final DomainCompilerType DEFAULT_COMPILER = DomainCompilerType.DDLv3; 
	
	// private constructor
	private PlanDataBaseBuilder() {}

	/**
	 * The method creates and initializes the plan data-base on the give domain and problem description 
	 * files (the DDL and PDL file respectively).
	 * 
	 * The method initializes the plan data-base on the generated problem.
	 * 
	 * @param ddlFile
	 * @param pdlFile
	 * @return
	 * @throws SynchronizationCycleException
	 * @throws ProblemInitializationException
	 * @throws PDLFileMissingException
	 */
	public static PlanDataBase build(String ddlFile, String pdlFile) 
			throws SynchronizationCycleException, ProblemInitializationException 
	{
		// get compiler factory
		DomainCompilerFactory factory = DomainCompilerFactory.getInstance();
		// create compiler
		DDLv3Compiler compiler = factory.create(DEFAULT_COMPILER, ddlFile, pdlFile);
		
		// compile domain
		PlanDataBase pdb = compiler.compileDomain();
		// compile problem
		Problem problem = compiler.compileProblem(pdb);
		// setup problem
		pdb.setup(problem);
		// get plan data-base
		return pdb;
	}
	
	/**
	 * The method creates and initializes a plan data-base without setting any initial problem.
	 * 
	 * @param ddlFile
	 * @return
	 * @throws SynchronizationCycleException
	 */
	public static PlanDataBase build(String ddlFile) 
			throws SynchronizationCycleException  
	{
		// get compiler factory
		DomainCompilerFactory factory = DomainCompilerFactory.getInstance();
		// create compiler
		DDLv3Compiler compiler = factory.create(DEFAULT_COMPILER, ddlFile);
		// get plan data-base
		return compiler.compileDomain();
	}
	
}
