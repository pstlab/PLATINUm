package it.istc.pst.platinum.testing.framework.domain.component.resource;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ResourceUsageValue;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
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
public class ReservoirResourceComponentTestCase 
{
	private TemporalFacade tdb;
	private ParameterFacade pdb;
	private ReservoirResource resource;
	
	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("********************** Reservoir Resource Component Test Case ********************");
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
		this.resource = df.create("ResvRes", DomainComponentType.RESOURCE_RESERVOIR);
		// set minimum and maximum capacity
		this.resource.setMinCapacity(0);
		this.resource.setMaxCapacity(10);
		this.resource.setInitialCapacity(10);
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
	public void createReservoirResourceTest() {
		System.out.println("[Test]: createReservoirResourceTest() --------------------");
		System.out.println();
		
		// check state variable object
		Assert.assertNotNull(this.resource);
		// check values
		List<ResourceUsageValue> values = this.resource.getValues();
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size() == 2);
		ResourceUsageValue value = values.get(0);
		Assert.assertNotNull(value);
		Assert.assertTrue(value.getType().equals(ComponentValueType.RESOURCE_CONSUMPTION) ||
				value.getType().equals(ComponentValueType.RESOURCE_PRODUCTION));
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
	public void addConsumptionTest() {
		System.out.println("[Test]: addConsumptionTest() --------------------");
		System.out.println();
		
		try
		{
			// post requirement decision 
			Decision dec = this.postConsumption(
					0,					 						// decision id
					new long[] {0, this.tdb.getHorizon()}, 		// start time bound
					new long[] {0, this.tdb.getHorizon()}, 		// end time bound
					new long[] {1, this.tdb.getHorizon()}, 		// decision duration bound
					3);											// resource requirement amount

			// post consumption
			System.out.println("Posted consumption:\n- " + dec + "\n");
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
			Assert.assertTrue(this.resource.getActiveDecisions().size() == 1);
			System.out.println(param);
		} 
		catch (ConsistencyCheckException | RelationPropagationException | DecisionPropagationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addProductionTest() {
		System.out.println("[Test]: addProductionTest() --------------------");
		System.out.println();
		
		try
		{
			// post requirement decision 
			Decision dec = this.postProduction(
					0,					 						// decision id
					new long[] {0, this.tdb.getHorizon()}, 		// start time bound
					new long[] {0, this.tdb.getHorizon()}, 		// end time bound
					new long[] {1, this.tdb.getHorizon()}, 		// decision duration bound
					3);											// resource requirement amount

			// posted production
			System.out.println("Posted production:\n- " + dec + "\n");
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
			Assert.assertTrue(this.resource.getActiveDecisions().size() == 1);
			System.out.println(param);
		} 
		catch (ConsistencyCheckException | RelationPropagationException | DecisionPropagationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void addConsumptionsAndDetectPeaksTest() {
		System.out.println("[Test]: addConsumptionsAndDetectPeaksTest() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision c1 = this.postConsumption(
					0, 
					new long [] {2, 5}, 
					new long[] {8, 23}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + c1 + "\n");
			
			// create decision
			Decision c2 = this.postConsumption(
					1, 
					new long[] {4, 11}, 
					new long[] {16, 33}, 
					new long[] {1, this.tdb.getHorizon()}, 
					3);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + c2 + "\n");
			
			// create decision
			Decision c3 = this.postConsumption(
					2, 
					new long[] {11, 45}, 
					new long[] {55, 60}, 
					new long[] {1, this.tdb.getHorizon()},
					3);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c3 + "\n");
			
			// check peak
			List<Flaw> flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			System.out.println("#" + flaws.size() + " peaks found");
			// check available solutions
			List<FlawSolution> solutions = flaws.get(0).getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertFalse(solutions.isEmpty());
			Assert.assertTrue(solutions.size() == 1);
			System.out.println("Available solutions of the peak:\n- solution: " + solutions.get(0) + "\n");
		}
		catch (ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param duration
	 * @param amount
	 * @return
	 * @throws DecisionPropagationException 
	 * @throws RelationPropagationException 
	 * @throws ConsistencyCheckException 
	 */
	private Decision postConsumption(long id, long[] start, long[] end, long[] duration, long amount) 
			throws DecisionPropagationException, RelationPropagationException, ConsistencyCheckException
	{
		// get consumption value
		ResourceUsageValue value = this.resource.getConsumptionValue();
		Assert.assertNotNull(value);
		Assert.assertTrue(value.getType().equals(ComponentValueType.RESOURCE_CONSUMPTION));
		
		// create and post requirement activity  
		Decision consumption = this.resource.create(value, 
				new String[] {"?a" + id},		// set requirement parameter
				start,							// set requirement start bound
				end,							// set requirement end bound
				duration);						// set requirement duration bound
		
		// post activity
		this.resource.add(consumption);
		
		// bind parameter 
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, consumption, consumption);
		bind.setReferenceParameterLabel(consumption.getParameterLabelByIndex(0));
		bind.setValue(Long.toString(amount));
		// post constraint
		this.resource.add(bind);
		// check consistency
		this.tdb.checkConsistency();
		this.pdb.checkConsistency();
		
		// get decision 
		return consumption;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param duration
	 * @param amount
	 * @return
	 * @throws DecisionPropagationException 
	 * @throws RelationPropagationException 
	 * @throws ConsistencyCheckException 
	 */
	private Decision postProduction(long id, long[] start, long[] end, long[] duration, long amount) 
			throws DecisionPropagationException, RelationPropagationException, ConsistencyCheckException
	{
		// get consumption value
		ResourceUsageValue value = this.resource.getProductionValue();
		Assert.assertNotNull(value);
		Assert.assertTrue(value.getType().equals(ComponentValueType.RESOURCE_PRODUCTION));
		
		// create and post requirement activity  
		Decision consumption = this.resource.create(value, 
				new String[] {"?a" + id},		// set requirement parameter
				start,							// set requirement start bound
				end,							// set requirement end bound
				duration);						// set requirement duration bound
		
		// post activity
		this.resource.add(consumption);
		
		// bind parameter 
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, consumption, consumption);
		bind.setReferenceParameterLabel(consumption.getParameterLabelByIndex(0));
		bind.setValue(Long.toString(amount));
		// post constraint
		this.resource.add(bind);
		// check consistency
		this.tdb.checkConsistency();
		this.pdb.checkConsistency();
		
		// get decision 
		return consumption;
	}
}
