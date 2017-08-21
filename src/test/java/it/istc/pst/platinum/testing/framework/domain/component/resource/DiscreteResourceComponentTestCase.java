package it.istc.pst.platinum.testing.framework.domain.component.resource;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceValue;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeFactory;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeType;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameter;
import it.istc.pst.platinum.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.TemporalFacadeFactory;
import it.istc.pst.platinum.framework.time.TemporalFacadeType;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggerFactory;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceComponentTestCase 
{
	private TemporalFacade tdb;
	private ParameterFacade pdb;
	private DiscreteResource resource;
	
	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* Discrete Resource Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
		
		// create temporal facade
		TemporalFacadeFactory tf = new TemporalFacadeFactory();
		this.tdb = tf.create(TemporalFacadeType.UNCERTAINTY_TEMPORAL_FACADE, 0, 100);
		
		// get parameter facade
		ParameterFacadeFactory pf = new ParameterFacadeFactory();
		this.pdb = pf.create(ParameterFacadeType.CSP_PARAMETER_FACADE);
		
		// create unary resource
		DomainComponentFactory df = new DomainComponentFactory();
		this.resource = df.create("Disco1", DomainComponentType.RESOURCE_DISCRETE);
		// set minimum and maximum capacity
		this.resource.setMinCapacity(0);
		this.resource.setMaxCapacity(10);
		this.resource.setInitialCapacity(10);
		this.resource.addRequirementValue();
	}
	
	/**
	 * 
	 */
	@After
	public void clear() {
		this.resource = null;
		this.pdb = null;
		System.gc();
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}
	
	/**
	 * 
	 */
	@Test
	public void createDiscreteResourceTest() {
		System.out.println("[Test]: createDiscreteResourceTest() --------------------");
		System.out.println();
		
		// check state variable object
		Assert.assertNotNull(this.resource);
		// check values
		List<RequirementResourceValue> values = this.resource.getValues();
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size() == 1);
		RequirementResourceValue req = values.get(0);
		Assert.assertNotNull(req);
		Assert.assertTrue(req.getNumberOfParameterPlaceHolders() == 1);
		// check min capacity
		Assert.assertTrue(this.resource.getMinCapacity() == 0);
		// check max capacity
		Assert.assertTrue(this.resource.getMaxCapacity() == 10);
		// add transitions
		System.out.println(this.resource);
	}
	
	/**
	 * 
	 */
	@Test
	public void addResourceRequirementsTest() {
		System.out.println("[Test]: addResourceRequirementsTest() --------------------");
		System.out.println();
		
		// get value
		RequirementResourceValue requirement = this.resource.getValues().get(0);
		Assert.assertNotNull(requirement);
		
		// create a requirement decision
		Decision dec = this.resource.create(requirement, new String[] {"?a0"});
		Assert.assertNotNull(dec);
		Assert.assertNull(dec.getToken());
		System.out.println(dec);
		
		try
		{
			// add decision
			this.resource.add(dec);
			Assert.assertNotNull(dec.getToken());
			
			// bind parameter
			BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, dec, dec);
			bind.setReference(dec);
			bind.setReferenceParameterLabel(dec.getParameterLabelByIndex(0));
			bind.setValue("3");
			// add relation
			this.resource.add(bind);

			// get parameter
			NumericParameter param = (NumericParameter) dec.getParameterByIndex(0);
			// check parameter value
			CheckValuesParameterQuery query = this.pdb.createQuery(ParameterQueryType.CHECK_PARAMETER_VALUES);
			query.setParameter(param);
			// process query
			this.pdb.process(query);
			// check value
			Assert.assertTrue(param.getLowerBound() == 3);
			Assert.assertTrue(param.getUpperBound() == 3);
			System.out.println(param);
		} 
		catch (RelationPropagationException | DecisionPropagationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addDecisionsAndFindPeaksTest1() {
		System.out.println("[Test]: addDecisionsAndFindPeaksTest1() --------------------");
		System.out.println();
		
		// get value
		RequirementResourceValue requirement = this.resource.getValues().get(0);
		Assert.assertNotNull(requirement);
		
		try
		{
			// create decision
			Decision a1 = this.resource.create(
					requirement, 
					new String[] {"?a0"},
					new long[] {2, 5},
					new long[] {8, 23},
					new long[] {1, this.tdb.getHorizon()});
			// add decision
			this.resource.add(a1);
			
			
			// bind parameter
			BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, a1, a1);
			bind.setReference(a1);
			bind.setReferenceParameterLabel(a1.getParameterLabelByIndex(0));
			bind.setValue("5");
			// add relation
			this.resource.add(bind);
			System.out.println("a1: " + a1);
			
			// create decision
			Decision a2 = this.resource.create(
					requirement, 
					new String[] {"?a1"},
					new long[] {4, 11},
					new long[] {16, 33},
					new long[] {1, this.tdb.getHorizon()});
			// add decision
			this.resource.add(a2);
			
			
			// bind parameter
			bind = this.resource.create(RelationType.BIND_PARAMETER, a2, a2);
			bind.setReference(a2);
			bind.setReferenceParameterLabel(a2.getParameterLabelByIndex(0));
			bind.setValue("5");
			// add relation
			this.resource.add(bind);
			System.out.println("a2: " + a2);
			
			// create decision
			Decision a3 = this.resource.create(
					requirement, 
					new String[] {"?a2"},
					new long[] {11, 45},
					new long[] {55, 60},
					new long[] {1, this.tdb.getHorizon()});
			// add decision
			this.resource.add(a3);
			
			// bind relation
			bind = this.resource.create(RelationType.BIND_PARAMETER, a3, a3);
			bind.setReference(a3);
			bind.setReferenceParameterLabel(a3.getParameterLabelByIndex(0));
			bind.setValue("7");
			// add relation
			this.resource.add(bind);
			System.out.println("a3: " + a3);
			
			
			// check peak
			List<Flaw> flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			System.out.println("#" + flaws.size() + " peaks found");
			for (Flaw flaw : flaws) {
				System.out.println("peak -> " + flaw + "\n");
			}
		}
		catch (RelationPropagationException | DecisionPropagationException | UnsolvableFlawFoundException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
