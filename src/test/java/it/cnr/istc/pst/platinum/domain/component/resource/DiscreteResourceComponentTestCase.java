package it.cnr.istc.pst.platinum.domain.component.resource;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import it.cnr.istc.pst.platinum.ai.framework.domain.DomainComponentBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.DiscreteResource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.RequirementResourceValue;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.discrete.PrecedenceConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.NumericParameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author anacleto
 *
 */
@TemporalFacadeConfiguration(
		network = TemporalNetworkType.STNU, 
		solver = TemporalSolverType.APSP
)
@ParameterFacadeConfiguration(
		solver = ParameterSolverType.CHOCHO_SOLVER
)
public class DiscreteResourceComponentTestCase 
{
	private TemporalFacade tdb;
	private ParameterFacade pdb;
	private DiscreteResource resource;
	
	/**
	 * 
	 */
	@BeforeAll
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* Discrete Resource Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create temporal facade
		this.tdb = TemporalFacadeBuilder.createAndSet(this, 0, 100);
		
		// get parameter facade
		this.pdb = ParameterFacadeBuilder.createAndSet(this);
		
		// create discrete resource
		this.resource = DomainComponentBuilder.createAndSet("Disco1", DomainComponentType.RESOURCE_DISCRETE, this.tdb, this.pdb);
		// set minimum and maximum capacity
		this.resource.setMinCapacity(0);
		this.resource.setMaxCapacity(10);
		this.resource.setInitialCapacity(10);
	}
	
	/**
	 * 
	 */
	@AfterEach
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
		assertNotNull(this.resource);
		// check values
		List<RequirementResourceValue> values = this.resource.getValues();
		assertNotNull(values);
		assertTrue(values.size() == 1);
		RequirementResourceValue req = values.get(0);
		assertNotNull(req);
		assertTrue(req.getNumberOfParameterPlaceHolders() == 1);
		// check min capacity
		assertTrue(this.resource.getMinCapacity() == 0);
		// check max capacity
		assertTrue(this.resource.getMaxCapacity() == 10);
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
		
		try
		{
			// post requirement decision 
			Decision dec = this.postRequirement(
					0,					 						// decision id
					new long[] {0, this.tdb.getHorizon()}, 		// start time bound
					new long[] {0, this.tdb.getHorizon()}, 		// end time bound
					new long[] {1, this.tdb.getHorizon()}, 		// decision duration bound
					3);											// resource requirement amount

			// get parameter
			NumericParameter param = (NumericParameter) dec.getParameterByIndex(0);
			// check parameter value
			CheckValuesParameterQuery query = this.pdb.createQuery(ParameterQueryType.CHECK_PARAMETER_VALUES);
			query.setParameter(param);
			// process query
			this.pdb.process(query);
			// check value
			assertTrue(param.getLowerBound() == 3);
			assertTrue(param.getUpperBound() == 3);
			System.out.println(param);
		} 
		catch (ConsistencyCheckException | RelationPropagationException | DecisionPropagationException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addDecisionsAndFindPeaksTest() {
		System.out.println("[Test]: addDecisionsAndFindPeaksTest() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision a1 = this.postRequirement(
					0, 
					new long [] {2, 5}, 
					new long[] {8, 23}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + a1 + "\n");
			
			// create decision
			Decision a2 = this.postRequirement(
					1, 
					new long[] {4, 11}, 
					new long[] {16, 33}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + a2 + "\n");
			
			// create decision
			Decision a3 = this.postRequirement(
					2, 
					new long[] {11, 45}, 
					new long[] {55, 60}, 
					new long[] {1, this.tdb.getHorizon()},
					7);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + a3 + "\n");
			
			
			// check peak
			List<Flaw> flaws = this.resource.detectFlaws();
			assertNotNull(flaws);
			assertTrue(!flaws.isEmpty());
			assertTrue(flaws.size() == 1);
			System.out.println("#" + flaws.size() + " peaks found");
			for (Flaw flaw : flaws) {
				System.out.println("peak -> " + flaw + "\n");
			}
		}
		catch (ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void detectAndSolvePeaksTest1() {
		System.out.println("[Test]: detectAndSolvePeaksTest1() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision a1 = this.postRequirement(
					0, 
					new long [] {2, 5}, 
					new long[] {8, 23}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + a1 + "\n");
			
			// create decision
			Decision a2 = this.postRequirement(
					1, 
					new long[] {4, 11}, 
					new long[] {16, 33}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + a2 + "\n");
			
			// create decision
			Decision a3 = this.postRequirement(
					2, 
					new long[] {11, 45}, 
					new long[] {55, 60}, 
					new long[] {1, this.tdb.getHorizon()},
					7);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + a3 + "\n");
			
			// solve resource constraints
			int counter = this.solve();
			// print number of steps needed to solve the peak
			System.out.println("Resource peak has been solved after " + counter + " steps");
			
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void detectAndSolvePeaksTest2() {
		System.out.println("[Test]: detectAndSolvePeaksTest2() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision r1 = this.postRequirement(
					0, 
					new long [] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + r1 + "\n");
			
			// create decision
			Decision r2 = this.postRequirement(
					1, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					6);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + r2 + "\n");
			
			// create decision
			Decision r3 = this.postRequirement(
					2, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					4);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r3 + "\n");
			
			// create decision
			Decision r4 = this.postRequirement(
					3, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					3);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r4 + "\n");
			
			// create decision
			Decision r5 = this.postRequirement(
					4, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					3);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r5 + "\n");
			
			// create decision
			Decision r6 = this.postRequirement(
					5, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {0, this.tdb.getHorizon()}, 
					new long[] {1, this.tdb.getHorizon()},
					5);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r6 + "\n");
			
			// solve resource constraints
			int counter = this.solve();
			// print number of steps needed to solve the peak
			System.out.println("Resource peak has been solved after " + counter + " steps");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	

	/**
	 * 
	 */
	@Test
	public void detectAndSolvePeaksTest3() {
		System.out.println("[Test]: detectAndSolvePeaksTest3() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision r1 = this.postRequirement(
					0, 
					new long [] {3, 18}, 
					new long[] {33, 65}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + r1 + "\n");
			
			// create decision
			Decision r2 = this.postRequirement(
					1, 
					new long[] {1, 6}, 
					new long[] {8, 11}, 
					new long[] {1, this.tdb.getHorizon()}, 
					6);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + r2 + "\n");
			
			// create decision
			Decision r3 = this.postRequirement(
					2, 
					new long[] {33, 70}, 
					new long[] {77, 85}, 
					new long[] {1, this.tdb.getHorizon()},
					2);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r3 + "\n");
			
			// create decision
			Decision r4 = this.postRequirement(
					3, 
					new long[] {40, 75}, 
					new long[] {80, 89}, 
					new long[] {1, this.tdb.getHorizon()},
					2);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r4 + "\n");
			
			// create decision
			Decision r5 = this.postRequirement(
					4, 
					new long[] {44, 81}, 
					new long[] {65, 99}, 
					new long[] {1, this.tdb.getHorizon()},
					2);
			// print posted activity
			System.out.println("Successfully posted resource requirement: " + r5 + "\n");
			
			// solve resource constraints
			int counter = this.solve();
			// print number of steps needed to solve the peak
			System.out.println("Resource peak has been solved after " + counter + " steps");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void applyAndRetractFlawSolutionTest()
	{
		System.out.println("[Test]: applyAndRetractFlawSolutionTest() --------------------");
		System.out.println();
		
		try
		{
			// create decision
			Decision r1 = this.postRequirement(
					0, 
					new long [] {3, 18}, 
					new long[] {33, 65}, 
					new long[] {1, this.tdb.getHorizon()}, 
					5);
			// print posted activity
			System.out.println("Successfully posted resource requiremet: " + r1 + "\n");
			
			// create decision
			Decision r2 = this.postRequirement(
					1, 
					new long[] {1, 6}, 
					new long[] {8, 11}, 
					new long[] {1, this.tdb.getHorizon()}, 
					6);
			// print posted activity 
			System.out.println("Successfully posted resource requirement: " + r2 + "\n");
			
			// detect flaws
			List<Flaw> flaws = this.resource.detectFlaws();
			assertNotNull(flaws);
			assertFalse(flaws.isEmpty());
			assertTrue(flaws.size() == 1);
			
			// get the flaw
			Flaw flaw = flaws.get(0);
			// check solutions
			List<FlawSolution> solutions = flaw.getSolutions();
			assertNotNull(solutions);
			assertFalse(solutions.isEmpty());
			assertTrue(solutions.size() >= 1 && solutions.size() <= 2);
			
			// apply a solution
			FlawSolution solution = solutions.get(0);
			System.out.println("Try to apply solution: " + solution + "\n");
			this.resource.commit(solution);
			
			// check consistency 
			this.tdb.verify();
			this.pdb.verify();
			System.out.println(".... solution successfully applied\n");
			
			
			// no flaws expected
			flaws = this.resource.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.isEmpty());
			System.out.println("No more flaws on resource " + this.resource + "\n");
			
			// try to roll-back applied solution
			System.out.println("Try to rollback applied solution: " + solution + "\n");
			// retract applied solution
			this.resource.rollback(solution);
			
			// check consistency
			this.tdb.verify();
			this.pdb.verify();
			System.out.println(".... solution successfully retracted\n");
			
			
			// flaws expected
			flaws = this.resource.detectFlaws();
			assertNotNull(flaws);
			assertFalse(flaws.isEmpty());
			assertTrue(flaws.size() == 1);
			
			// try again to solve the the flaw and check the resulting state
			flaw = flaws.get(0);
			// check solutions
			solutions = flaw.getSolutions();
			assertNotNull(solutions);
			assertFalse(solutions.isEmpty());
			assertTrue(solutions.size() >= 1 && solutions.size() <= 2);
			
			// apply a solution
			solution = solutions.get(0);
			System.out.println("Try to apply solution: " + solution + "\n");
			this.resource.commit(solution);
			
			// check consistency 
			this.tdb.verify();
			this.pdb.verify();
			System.out.println(".... solution successfully applied\n");
			
			
			// no flaws expected
			flaws = this.resource.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.isEmpty());
			System.out.println("No more flaws on resource " + this.resource + "\n");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | RelationPropagationException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
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
	private Decision postRequirement(long id, long[] start, long[] end, long[] duration, long amount) 
			throws DecisionPropagationException, RelationPropagationException, ConsistencyCheckException
	{
		// get requirement value
		RequirementResourceValue requirement = this.resource.getRequirementValue();
		assertNotNull(requirement);
		
		// create and post requirement activity  
		Decision activity = this.resource.create(requirement, 
				new String[] {"?a" + id},		// set requirement parameter
				start,							// set requirement start bound
				end,							// set requirement end bound
				duration);						// set requirement duration bound
		
		// post activity
		this.resource.activate(activity);
		
		// bind parameter 
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, activity, activity);
		bind.setReferenceParameterLabel(activity.getParameterLabelByIndex(0));
		bind.setValue(Long.toString(amount));
		// post constraint
		this.resource.activate(bind);
		// check consistency
		this.tdb.verify();
		this.pdb.verify();
		
		// get decision 
		return activity;
	}
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 * @throws FlawSolutionApplicationException
	 * @throws ConsistencyCheckException
	 */
	private int solve() 
			throws UnsolvableFlawException, FlawSolutionApplicationException, ConsistencyCheckException
	{
		// check peak
		List<Flaw> flaws = this.resource.detectFlaws();
		// step counter
		int counter = 0;
		do
		{
			// check detected peaks
			assertNotNull(flaws);
			assertTrue(!flaws.isEmpty());
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
			assertNotNull(solutions);
			assertFalse(solutions.isEmpty());
			System.out.println("There is/are " + solutions.size() + " solution(s) available\n");
			
			// randomly select a solution
			rand = new Random(System.currentTimeMillis());
			index = rand.nextInt(solutions.size());
			
			// get a solution and try to apply 
			PrecedenceConstraint solution = (PrecedenceConstraint) solutions.get(index);
			System.out.println("Try to apply solution: " + solution + "\n");
			// apply selected solution
			this.resource.commit(solution);
			
			// check consistency 
			this.tdb.verify();
			this.pdb.verify();
			
			System.out.println("... Solution successfully applied\n");
			// check peak
			flaws = this.resource.detectFlaws();
		}
		while (!flaws.isEmpty());
		// get solving steps
		return counter;
	}
}
