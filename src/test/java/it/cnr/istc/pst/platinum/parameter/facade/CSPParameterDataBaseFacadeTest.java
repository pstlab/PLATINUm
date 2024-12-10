package it.cnr.istc.pst.platinum.parameter.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.EnumerationParameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.EnumerationParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.BindParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ExcludeParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.NotEqualParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.query.CheckValuesParameterQuery;

/**
 * 
 * @author anacleto
 *
 */
@ParameterFacadeConfiguration(
		solver= ParameterSolverType.CHOCHO_SOLVER
)
public class CSPParameterDataBaseFacadeTest 
{
	private ParameterFacade facade;
	private ParameterConstraintFactory cFactory;
	
	/**
	 * 
	 */
	@BeforeAll
	public void init() 
	{
		System.out.println("**********************************************************************************");
		System.out.println("*************************** CSP Parameter Facade Test Case ************************");
		System.out.println("**********************************************************************************");
		
		// get parameter constraint factory
		this.cFactory = new ParameterConstraintFactory();
	}
	
	/**
	 * 
	 */
	@AfterEach
	public void clear() {
		this.facade = null;
		System.gc();
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}
	
	/**
	 * 
	 */
	@Test
	public void createFacadeTest() 
	{
		System.out.println("[Test]: createFacadeTest() --------------------");
		System.out.println();
		// create facade
		this.facade = ParameterFacadeBuilder.createAndSet(this);
		assertNotNull(this.facade);
		try
		{
			// check consistency
			this.facade.verify();
			System.out.println("Ok!");
			assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createParametersAndConstraintsTest() 
			throws Exception
	{
		System.out.println("[Test]: createParametersAndConstraintsTest() --------------------");
		System.out.println();
		
		// create facade
		this.facade = ParameterFacadeBuilder.createAndSet(this);
		
		// create parameter domains
		EnumerationParameterDomain location = this.facade.
				createParameterDomain("locations", ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
		// set possible values
		location.setValues(new String[] {
			"rock1",
			"rock2",
			"rock3",
			"home"
		});
		
		// create enumeration parameters
		EnumerationParameter r1location = this.facade.createParameter("r1-location", location);
		// add parameter
		this.facade.addParameter(r1location);
		// create enumeration parameter
		EnumerationParameter r2location = this.facade.createParameter("r2-location", location);
		// add parameter
		this.facade.addParameter(r2location);
		
		// different robots cannot stay on the same location
		NotEqualParameterConstraint neq = this.cFactory.createParameterConstraint(ParameterConstraintType.NOT_EQUAL);
		neq.setReference(r1location);
		neq.setTarget(r2location);
		System.out.println("Creating parameters:\n- " + r1location + "\n- " + r2location + "\n");
		System.out.println("Positing constraint:\n" + neq + "\n");
		try
		{
			// propagate constraint
			this.facade.propagate(neq);
			// check consistency
			this.facade.verify();
			System.out.println("Ok!");
			assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			assertTrue(false);
		}
		
		// assign a value to a variable
		BindParameterConstraint bind = this.cFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(r1location);
		bind.setValue("rock2");
		System.out.println("Posting constraint:\n" + bind + "\n");
		
		try
		{
			// propagate constraint
			this.facade.propagate(bind);
			// check consistency
			this.facade.verify();
			System.out.println("Ok!");
			assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			assertTrue(false);
		}
		
		
		// check values of parameter "r1locaiton"
		CheckValuesParameterQuery query = this.facade.createQuery(ParameterQueryType.CHECK_PARAMETER_VALUES);
		query.setParameter(r1location);
		// process query
		this.facade.process(query);
		// check values
		assertTrue(r1location.getValues().length == 1);
		assertTrue(r1location.getValues()[0].equals("rock2"));
		System.out.println(r1location);
		
		// check values of parameter "r2locaiton"
		query.setParameter(r2location);
		// process query
		this.facade.process(query);
		// check values
		System.out.println(r2location);
		assertTrue(r2location.getValues().length < r2location.getDomain().getValues().length);
		assertTrue(r2location.getValues().length == r2location.getDomain().getValues().length - 1);
		
		// further constrain the values of parameter "r2location"
		ExcludeParameterConstraint ex = this.cFactory.createParameterConstraint(ParameterConstraintType.EXCLUDE);
		ex.setReference(r2location);
		ex.setValue("rock1");
		System.out.println("Posting constraint:\n" + ex + "\n");
		
		try
		{
			// propagate constraint
			this.facade.propagate(ex);
			// check consistency
			this.facade.verify();
			System.out.println("Ok!");
			assertTrue(true);
		}
		catch (ConsistencyCheckException exx) {
			assertTrue(false);
		}
		
		// check remaining values
		query.setParameter(r2location);
		// process query
		this.facade.process(query);
		// check values
		System.out.println(r2location);
		assertTrue(r2location.getValues().length < r2location.getDomain().getValues().length);
		assertTrue(r2location.getValues().length == r2location.getDomain().getValues().length - 2);
	}

}
