package it.istc.pst.platinum.testing.framework.domain.component.resource;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ResourceUsageValue;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir.ConsumptionScheduling;
import it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir.ProductionPlanning;
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
	 */
	@Test
	public void detectAndSolvePeakThrougPlanningTest() {
		System.out.println("[Test]: detectAndSolvePeakThrougPlanningTest() --------------------");
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
			
			// get solution - expected planning solution
			FlawSolution solution = solutions.get(0);
			Assert.assertTrue(solution instanceof ProductionPlanning);
			ProductionPlanning planning = (ProductionPlanning) solution;
			// check amount of resource to produce
			Assert.assertTrue(planning.getAmount() == 8);
			Assert.assertTrue(planning.getDecisionsBeforeProduction().size() == 2);
			Assert.assertTrue(planning.getDecisionsAfterProduction().size() == 1);
			Assert.assertTrue(planning.getDecisionsAfterProduction().get(0).equals(c3));
			
			// try to apply solution
			this.resource.commit(planning);
			
			// check consistency
			this.tdb.checkConsistency();
			this.pdb.checkConsistency();
			System.out.println("Resource planning solution successfully applied:\n- solution: " + planning + "\n");
			
			// check pending decisions and relations
			List<Decision> pending = this.resource.getPendingDecisions();
			Set<Relation> relations = this.resource.getPendingRelations();
			Assert.assertTrue(pending.size() == 1);
			System.out.println("One production goal found after solution propagation");
			Assert.assertTrue(planning.getDecisionsBeforeProduction().size() == 2);
			Assert.assertTrue(planning.getDecisionsAfterProduction().size() == 1);
			System.out.println("Current agenda:\n- pending decisions: " + pending + "\n- pending relations: " + relations + "\n");
			
			// add decision and then check flaws
			this.resource.activate(pending.get(0));
			// check consistency
			this.tdb.checkConsistency();
			this.pdb.checkConsistency();
			System.out.println("Production successfully propagated:\n- production: " + pending.get(0) + "\n");
			
			// check flaws after production
			flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.isEmpty());
			System.out.println("Peak successfully solved after production propagation");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 	
	 */
	@Test
	public void applyAndRetractSolutionTest()
	{
		System.out.println("[Test]: applyAndRetractSolutionTest() --------------------");
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
			
			// get solution - expected planning solution
			FlawSolution solution = solutions.get(0);
			Assert.assertTrue(solution instanceof ProductionPlanning);
			Decision goal = this.applySolution(solution);
			
			// check flaws after production
			flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.isEmpty());
			System.out.println("Peak successfully solved after production propagation");
			
			// retract flaw solution
			this.resource.deactivate(goal);
			// retract flaw solution
			this.resource.rollback(solution);
			// check consistency
			this.tdb.checkConsistency();
			this.pdb.checkConsistency();
			System.out.println("Solution successfully retracted:\n- solution: " + solution + "\n");
			
			// check peak again
			flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			System.out.println("#" + flaws.size() + " peaks found");
			// check available solutions
			solutions = flaws.get(0).getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertFalse(solutions.isEmpty());
			Assert.assertTrue(solutions.size() == 1);
			System.out.println("Available solutions of the peak:\n- solution: " + solutions.get(0) + "\n");
			
			// get solution - expected planning solution
			solution = solutions.get(0);
			Assert.assertTrue(solution instanceof ProductionPlanning);
			goal = this.applySolution(solution);
			
			// check flaws after production
			flaws = this.resource.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.isEmpty());
			System.out.println("Peak successfully solved after production propagation");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 * @param solution
	 * @throws FlawSolutionApplicationException 
	 * @throws ConsistencyCheckException 
	 * @throws DecisionPropagationException 
	 */
	private Decision applySolution(FlawSolution solution) 
			throws FlawSolutionApplicationException, ConsistencyCheckException, DecisionPropagationException
	{
		ProductionPlanning planning = (ProductionPlanning) solution;
		// try to apply solution
		this.resource.commit(planning);
		
		// check consistency
		this.tdb.checkConsistency();
		this.pdb.checkConsistency();
		System.out.println("Resource planning solution successfully applied:\n- solution: " + planning + "\n");
		
		// check pending decisions and relations
		List<Decision> pending = this.resource.getPendingDecisions();
		Set<Relation> relations = this.resource.getPendingRelations();
		Assert.assertTrue(pending.size() == 1);
		System.out.println("One production goal found after solution propagation");
		Assert.assertTrue(planning.getDecisionsBeforeProduction().size() == 2);
		Assert.assertTrue(planning.getDecisionsAfterProduction().size() == 1);
		System.out.println("Current agenda:\n- pending decisions: " + pending + "\n- pending relations: " + relations + "\n");
		
		// add decision and then check flaws
		Decision goal = pending.get(0);
		this.resource.activate(goal);
		// check consistency
		this.tdb.checkConsistency();
		this.pdb.checkConsistency();
		System.out.println("Production successfully propagated:\n- production: " + pending.get(0) + "\n");
		return goal;
	}
	
	/**
	 * 
	 */
	@Test
	public void solveProblemTest1() {
		System.out.println("[Test]: solveProblemTest1() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision c1 = this.postConsumption(
					0, 
					new long [] {2, 5}, 
					new long[] {8, 23}, 
					new long[] {1, this.tdb.getHorizon()}, 
					3);
			// print posted activity
			System.out.println("Successfully posted resource consumption: " + c1 + "\n");
			
			// create decision
			Decision c2 = this.postConsumption(
					1, 
					new long[] {4, 11}, 
					new long[] {16, 33}, 
					new long[] {1, this.tdb.getHorizon()}, 
					3);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c2 + "\n");
			
			// create decision
			Decision c3 = this.postConsumption(
					3, 
					new long[] {45, 50}, 
					new long[] {65, 70}, 
					new long[] {1, this.tdb.getHorizon()}, 
					8);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c3 + "\n");
			
			// create decision
			Decision c4 = this.postConsumption(
					4, 
					new long[] {5, 61}, 
					new long[] {13, 80}, 
					new long[] {1, this.tdb.getHorizon()},
					3);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c4 + "\n");

			// solve resource flaws
			int counter = this.solve();
			System.out.println("Resource peaks solved in " + counter + " solving steps\n");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void solveProblemTest2() {
		System.out.println("[Test]: solveProblemTest2() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision c1 = this.postConsumption(
					0, 
					new long [] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource consumption: " + c1 + "\n");
			
			// create decision
			Decision c2 = this.postConsumption(
					1, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					2);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c2 + "\n");
			
			// create decision
			Decision c3 = this.postConsumption(
					3, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					9);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c3 + "\n");
			
			// create decision
			Decision c4 = this.postConsumption(
					4, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					2);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c4 + "\n");
			
			// create decision
			Decision c5 = this.postConsumption(
					5, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					5);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c5 + "\n");

			// solve resource flaws
			int counter = this.solve();
			System.out.println("Resource peaks solved in " + counter + " solving steps\n");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void solveProblemTest3() {
		System.out.println("[Test]: solveProblemTest3() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision c1 = this.postConsumption(
					0, 
					new long [] {1, 50}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource consumption: " + c1 + "\n");
			
			// create decision
			Decision c2 = this.postConsumption(
					1, 
					new long[] {3, 53}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					3);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c2 + "\n");
			
			// create decision
			Decision c3 = this.postConsumption(
					3, 
					new long[] {4, 54}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity 
			System.out.println("Successfully posted resource consumption: " + c3 + "\n");
			
			// create decision
			Decision c4 = this.postConsumption(
					4, 
					new long[] {5, 55}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					4);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c4 + "\n");
			
			// create decision
			Decision c5 = this.postConsumption(
					5, 
					new long[] {6, 56}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					2);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + c5 + "\n");

			// solve resource flaws
			int counter = this.solve();
			System.out.println("Resource peaks solved in " + counter + " solving steps\n");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
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
		this.resource.activate(consumption);
		
		// bind parameter 
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, consumption, consumption);
		bind.setReferenceParameterLabel(consumption.getParameterLabelByIndex(0));
		bind.setValue(Long.toString(amount));
		// post constraint
		this.resource.activate(bind);
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
		this.resource.activate(consumption);
		
		// bind parameter 
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, consumption, consumption);
		bind.setReferenceParameterLabel(consumption.getParameterLabelByIndex(0));
		bind.setValue(Long.toString(amount));
		// post constraint
		this.resource.activate(bind);
		// check consistency
		this.tdb.checkConsistency();
		this.pdb.checkConsistency();
		
		// get decision 
		return consumption;
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 * @throws FlawSolutionApplicationException
	 * @throws ConsistencyCheckException
	 * @throws DecisionPropagationException 
	 */
	private int solve() 
			throws UnsolvableFlawException, FlawSolutionApplicationException, ConsistencyCheckException, DecisionPropagationException
	{
		// check peak
		List<Flaw> flaws = this.resource.detectFlaws();
		// step counter
		int counter = 0;
		do
		{
			// check detected peaks
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			System.out.println("There is/are " + flaws.size() + " peak(s) on resource\n");
			// randomly select a solution
			Random rand = new Random(System.currentTimeMillis());
			int index = rand.nextInt(flaws.size());
			
			// get the only one flaw expected
			Flaw flaw = flaws.get(index);
			counter++;
			System.out.println("Flaw to solve: " + flaw + "\n");
			
			// check available flaw solutions
			List<FlawSolution> solutions = flaw.getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertFalse(solutions.isEmpty());
			System.out.println("There is/are " + solutions.size() + " solution(s) available\n");
			
			// randomly select a solution
			rand = new Random(System.currentTimeMillis());
			index = rand.nextInt(solutions.size());
			
			// get a solution and try to apply 
			FlawSolution solution = solutions.get(index);
			// check solution type 
			if (solution instanceof ConsumptionScheduling)
			{
				// get consumption scheduling 
				ConsumptionScheduling scheduling = (ConsumptionScheduling) solution;
				System.out.println("Try to apply scheduling solution: " + scheduling + "\n");
				// apply selected solution
				this.resource.commit(solution);
			}
			
			if (solution instanceof ProductionPlanning)
			{
				ProductionPlanning planning = (ProductionPlanning) solution;
				System.out.println("Try to apply planning solution: " + planning + "\n");
				// try to apply solution
				this.resource.commit(planning);
				
				// check consistency
				this.tdb.checkConsistency();
				this.pdb.checkConsistency();
				System.out.println("Resource planning solution successfully applied:\n- solution: " + planning + "\n");
				
				// check pending decisions and relations
				List<Decision> pending = this.resource.getPendingDecisions();
				Set<Relation> relations = this.resource.getPendingRelations();
				Assert.assertTrue(pending.size() == 1);
				System.out.println("One production goal found after solution propagation");
				System.out.println("Current agenda:\n- pending decisions: " + pending + "\n- pending relations: " + relations + "\n");
				
				// add decision and then check flaws
				this.resource.activate(pending.get(0));
			}
			
			// check consistency 
			this.tdb.checkConsistency();
			this.pdb.checkConsistency();
			System.out.println("... Solution successfully applied\n");
			// check peak
			flaws = this.resource.detectFlaws();
		}
		while (!flaws.isEmpty());
		// get solving steps
		return counter;
	}
}
