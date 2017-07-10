package it.uniroma3.epsl2.testing.framework.parameter.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryType;
import it.uniroma3.epsl2.framework.parameter.ParameterFacade;
import it.uniroma3.epsl2.framework.parameter.ParameterFacadeFactory;
import it.uniroma3.epsl2.framework.parameter.ParameterFacadeType;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BindParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ExcludeParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.NotEqualParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;
import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class CSPParameterDataBaseFacadeTest 
{
	private ParameterFacade facade;
	private ParameterFacadeFactory factory;
	private ParameterConstraintFactory cFactory;
	
	/**
	 * 
	 */
	@Before
	public void init() 
	{
		System.out.println("**********************************************************************************");
		System.out.println("*************************** CSP Parameter Facade Test Case ************************");
		System.out.println("**********************************************************************************");
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
		
		this.factory = new ParameterFacadeFactory();
		this.cFactory = ParameterConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@After
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
		this.facade = this.factory.create(ParameterFacadeType.CSP_PARAMETER_FACADE);
		Assert.assertNotNull(this.facade);
		try
		{
			// check consistency
			this.facade.checkConsistency();
			System.out.println("Ok!");
			Assert.assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			Assert.assertTrue(false);
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
		this.facade = this.factory.create(ParameterFacadeType.CSP_PARAMETER_FACADE);
		
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
			this.facade.checkConsistency();
			System.out.println("Ok!");
			Assert.assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			Assert.assertTrue(false);
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
			this.facade.checkConsistency();
			System.out.println("Ok!");
			Assert.assertTrue(true);
		}
		catch (ConsistencyCheckException ex) {
			Assert.assertTrue(false);
		}
		
		
		// check values of parameter "r1locaiton"
		CheckValuesParameterQuery query = this.facade.createQuery(ParameterQueryType.CHECK_PARAMETER_VALUES);
		query.setParameter(r1location);
		// process query
		this.facade.process(query);
		// check values
		Assert.assertTrue(r1location.getValues().length == 1);
		Assert.assertTrue(r1location.getValues()[0].equals("rock2"));
		System.out.println(r1location);
		
		// check values of parameter "r2locaiton"
		query.setParameter(r2location);
		// process query
		this.facade.process(query);
		// check values
		System.out.println(r2location);
		Assert.assertTrue(r2location.getValues().length < r2location.getDomain().getValues().length);
		Assert.assertTrue(r2location.getValues().length == r2location.getDomain().getValues().length - 1);
		
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
			this.facade.checkConsistency();
			System.out.println("Ok!");
			Assert.assertTrue(true);
		}
		catch (ConsistencyCheckException exx) {
			Assert.assertTrue(false);
		}
		
		// check remaining values
		query.setParameter(r2location);
		// process query
		this.facade.process(query);
		// check values
		System.out.println(r2location);
		Assert.assertTrue(r2location.getValues().length < r2location.getDomain().getValues().length);
		Assert.assertTrue(r2location.getValues().length == r2location.getDomain().getValues().length - 2);
	}

}
