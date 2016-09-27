package it.uniroma3.epsl2.testing.framework.parameter.facade.csp;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryType;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacade;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeType;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConstraintPropagationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.NumericConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.EqualParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;
import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class CSPParameterDataBaseFacadeTest {

	private ParameterDataBaseFacade facade;
	private ParameterConstraintFactory factory;
	
	/**
	 * 
	 */
	@Before
	public void setup() {
		System.out.println("**********************************************************************************");
		System.out.println("********************** CSP Parameter Facade Test Case *********************");
		System.out.println("**********************************************************************************");
		
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
		
		// get factor
		ParameterDataBaseFacadeFactory factory = new ParameterDataBaseFacadeFactory();
		// create facade
		this.facade = factory.createSingleton(ParameterDataBaseFacadeType.CSP_PARAMETER_FACADE);
		// create constraint factory
		this.factory = new ParameterConstraintFactory();
	}
	
	/**
	 * 
	 */
	@After
	public void clear() {
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}
	
	/**
	 * 
	 */
	@Test
	public void init() {
		System.out.println("[Test]: init() --------------------");
		System.out.println();
		try {
			// check facade
			Assert.assertNotNull(this.facade);
			// check consistency
			this.facade.checkConsistency();
			System.out.println("Ok!");
		} catch (ConsistencyCheckException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}

	/**
	 * 
	 */
	@Test
	public void createParameterDomainsTest() {
		System.out.println("[Test]: createParameterDomainsTest() --------------------");
		System.out.println();
		try {
			
			// create domains
			EnumerationParameterDomain edom = this.facade.createParameterDomain("mugs", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(edom);
			edom.setValues(new String[] {"mug1", "mug2", "mug3"});
			System.out.println(edom);
			
			NumericParameterDomain ndom = this.facade.createParameterDomain("x-axis", 
					ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(ndom);
			ndom.setLowerBound(0);
			ndom.setUpperBound(10);
			System.out.println(ndom);
			
			Assert.assertNotNull(this.facade.getParameterDomains());
			Assert.assertTrue(this.facade.getParameterDomains().size() == 2);
			
			String mug3 = edom.getValue(2);
			Assert.assertNotNull(mug3);
			Assert.assertTrue(mug3 == "mug3");
			
			this.facade.checkConsistency();
		}
		catch (ConsistencyCheckException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createParametersTest() {
		System.out.println("[Test]: createParametersTest() --------------------");
		System.out.println();
		try {
			
			// create domains
			EnumerationParameterDomain edom = this.facade.createParameterDomain("mugs", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(edom);
			edom.setValues(new String[] {"mug1", "mug2", "mug3"});
			System.out.println(edom);
			
			NumericParameterDomain ndom = this.facade.createParameterDomain("x-axis", 
					ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(ndom);
			ndom.setLowerBound(0);
			ndom.setUpperBound(10);
			System.out.println(ndom);
			
			Assert.assertNotNull(this.facade.getParameterDomains());
			Assert.assertTrue(this.facade.getParameterDomains().size() == 2);
			
			// create enumeration parameter
			EnumerationParameter mug = this.facade.createParameter("?mug", 
					ParameterType.ENUMERATION_PARAMETER_TYPE, 
					edom);
			
			Assert.assertNotNull(mug);
			Assert.assertTrue(mug.getValue().equals(Arrays.asList(edom.getValues())));
			System.out.println(mug);
			// add parameter
			this.facade.addParameter(mug);
			
			// create numeric parameter
			NumericParameter xPos = this.facade.createParameter("?pos", 
					ParameterType.NUMERIC_PARAMETER_TYPE, 
					ndom);
			
			Assert.assertNotNull(xPos);
			Assert.assertTrue(xPos.getLowerBound() == ndom.getLowerBound());
			Assert.assertTrue(xPos.getUpperBound() == ndom.getUpperBound());
			System.out.println(xPos);
			// add parameter
			this.facade.addParameter(xPos);

			// check consistency
			this.facade.checkConsistency();
		}
		catch (ConsistencyCheckException | ParameterCreationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createConstantParametersTest() {
		System.out.println("[Test]: createConstantParametersTest() --------------------");
		System.out.println();
		try {
			
			// create domains
			EnumerationParameterDomain edom = this.facade.createParameterDomain("mugs", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(edom);
			edom.setValues(new String[] {"mug1", "mug2", "mug3"});
			System.out.println(edom);
			
			NumericParameterDomain ndom = this.facade.createParameterDomain("x-axis", 
					ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(ndom);
			ndom.setLowerBound(0);
			ndom.setUpperBound(10);
			System.out.println(ndom);
			
			Assert.assertNotNull(this.facade.getParameterDomains());
			Assert.assertTrue(this.facade.getParameterDomains().size() == 2);
			
			// create enumeration parameter
			EnumerationConstantParameter mug1 = this.facade.createParameter("?mug", 
					ParameterType.CONSTANT_ENUMERATION_PARAMETER_TYPE, 
					edom);
			// set value
			mug1.setValue("mug1");
			
			// check data 
			Assert.assertNotNull(mug1);
			Assert.assertTrue(mug1.getValue().equals(edom.getValue(0)));
			System.out.println(mug1);
			// add parameter
			this.facade.addParameter(mug1);
			
			// create numeric parameter
			NumericConstantParameter xPos = this.facade.createParameter("?pos", 
					ParameterType.CONSTANT_NUMERIC_PARAMETER_TYPE, 
					ndom);
			// set value
			xPos.setValue(5);
			
			// check data
			Assert.assertNotNull(xPos);
			Assert.assertTrue(xPos.getValue() >= ndom.getLowerBound());
			Assert.assertTrue(xPos.getValue() <= ndom.getUpperBound());
			System.out.println(xPos);
			// add parameter
			this.facade.addParameter(xPos);

			// check consistency
			this.facade.checkConsistency();
		}
		catch (ConsistencyCheckException | ParameterCreationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createParametersAndConstraintsTest() {
		System.out.println("[Test]: createParametersAndConstraintsTest() --------------------");
		System.out.println();
		try {
			
			// create domains
			EnumerationParameterDomain edom = this.facade.createParameterDomain("mugs", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(edom);
			edom.setValues(new String[] {"mug1", "mug2", "mug3"});
			System.out.println(edom);
			
			Assert.assertNotNull(this.facade.getParameterDomains());
			Assert.assertTrue(this.facade.getParameterDomains().size() == 1);
			
			// create parameter
			EnumerationParameter mug1 = this.facade.createParameter("?mug1", 
					ParameterType.ENUMERATION_PARAMETER_TYPE, 
					edom);
			
			Assert.assertNotNull(mug1);
			Assert.assertTrue(mug1.getValue().equals(Arrays.asList(edom.getValues())));
			System.out.println(mug1);
			
			// add parameter
			this.facade.addParameter(mug1);
			
			// create parameter
			EnumerationParameter mug2 = this.facade.createParameter("?mug2",
					ParameterType.ENUMERATION_PARAMETER_TYPE, 
					edom);
			
			Assert.assertNotNull(mug2);
			Assert.assertTrue(mug2.getValue().equals(Arrays.asList(edom.getValues())));
			System.out.println(mug2);
			
			// add parameter
			this.facade.addParameter(mug2);
			
			// create numeric parameter
			EqualParameterConstraint equal = this.factory.create(ParameterConstraintType.EQUAL);
			Assert.assertNotNull(equal);
			equal.setReference(mug1);
			equal.setTarget(mug2);

			try {
				// propagate constraint
				this.facade.propagate(equal);
				System.out.println("Constraint propagation Ok!");
			}
			catch (ParameterConstraintPropagationException ex) {
				System.err.println(ex.getMessage());
				Assert.assertTrue(false);
			}
			
			// check consistency
			this.facade.checkConsistency();
		}
		catch (ConsistencyCheckException | ParameterCreationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}

	/**
	 * 
	 */
	@Test
	public void assignValueToParameterTest() {
		System.out.println("[Test]: assignValueToParameterTest() --------------------");
		System.out.println();
		try {
			
			// create domains
			EnumerationParameterDomain edom = this.facade.createParameterDomain("mugs", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			Assert.assertNotNull(edom);
			edom.setValues(new String[] {"mug1", "mug2", "mug3"});
			System.out.println(edom);
			
			Assert.assertNotNull(this.facade.getParameterDomains());
			Assert.assertTrue(this.facade.getParameterDomains().size() == 1);
			
			// create enumeration parameter
			EnumerationConstantParameter mug1 = this.facade.createAnonymousParameter( 
					ParameterType.CONSTANT_ENUMERATION_PARAMETER_TYPE, 
					edom);
			// set value
			mug1.setValue("mug1");
			
			// check data 
			Assert.assertNotNull(mug1);
			Assert.assertTrue(mug1.getValue().equals(edom.getValue(0)));
			System.out.println(mug1);
			// add parameter
			this.facade.addParameter(mug1);
			
			// create numeric parameter
			EnumerationParameter aMug = this.facade.createParameter("?aMug", 
					ParameterType.ENUMERATION_PARAMETER_TYPE, 
					edom);
			
			// check data
			Assert.assertNotNull(aMug);
			Assert.assertTrue(aMug.getValue().length == 3);
			System.out.println(aMug);
			// add parameter
			this.facade.addParameter(aMug);
			
			// add constraint
			EqualParameterConstraint equal = this.factory.create(ParameterConstraintType.EQUAL);
			equal.setReference(aMug);
			equal.setTarget(mug1);
			// add constraint
			this.facade.propagate(equal);

			// check consistency
			this.facade.checkConsistency();
			
			// create parameter query
			ParameterQueryFactory qFactory = ParameterQueryFactory.getInstance();
			CheckValuesParameterQuery query = qFactory.create(ParameterQueryType.CHECK_PARAMETER_VALUES);
			// set parameter
			query.setParameter(aMug);
			// process query
			this.facade.process(query);
			
			// check value
			Assert.assertTrue(aMug.getValue().length == 1);
			Assert.assertTrue(aMug.getValue()[0].equals(mug1.getValue()));
			
			System.out.println(aMug);
			
			// check again consistency
			this.facade.checkConsistency();
		}
		catch (ConsistencyCheckException | ParameterConstraintPropagationException | ParameterCreationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 * @author anacleto
	 *
	 */
	class ParameterConstraintFactory {
		
		protected ParameterConstraintFactory() {}
		
		/**
		 * 
		 * @param type
		 */
		@SuppressWarnings("unchecked")
		public <T extends ParameterConstraint> T create(ParameterConstraintType type) {
			T constraint = null;
			try {
				Class<T> clazz = (Class<T>) Class.forName(type.getConstraintClassName());
				Constructor<T> c = clazz.getDeclaredConstructor();
				c.setAccessible(true);
				constraint = c.newInstance();
			}
			catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
			return constraint;
		}
		
	}
}
