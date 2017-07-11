package it.istc.pst.platinum.testing.framework.domain.component.sv;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.sv.PrimitiveStateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.gap.Gap;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.gap.GapCompletion;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling.DecisionSchedule;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling.Peak;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeFactory;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeType;
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
public class StateVariableComponentTestCase 
{
	private static final int ORIGIN = 0;
	private static final int HORIZON = 100;
	private TemporalFacade facade;
	private PrimitiveStateVariable psv;

	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* State Variable Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
		
		// get temporal facade
		TemporalFacadeFactory factory = new TemporalFacadeFactory();
		// create temporal facade
		this.facade = factory.create(TemporalFacadeType.UNCERTAINTY_TEMPORAL_FACADE, ORIGIN, HORIZON);
		
		// get parameter facade
		ParameterFacadeFactory pf = new ParameterFacadeFactory();
		pf.create(ParameterFacadeType.CSP_PARAMETER_FACADE);
		
		// create State Variable
		DomainComponentFactory df = new DomainComponentFactory();
		this.psv = df.create("SV1", DomainComponentType.SV_PRIMITIVE);
	}
	
	/**
	 * 
	 */
	@After
	public void clear() {
		this.psv = null;
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
	public void createStateVariableTest() {
		System.out.println("[Test]: createStateVariableTest() --------------------");
		System.out.println();
		
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long [] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", new long[] {11, 25}, false);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v3);
		System.out.println(this.psv);
	}
	
	/**
	 * 
	 */
	@Test
	public void addDecisionsTest() {
		System.out.println("[Test]: addDecisionsTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			Decision d3 = this.psv.create(v1, new String[] {});
			this.psv.add(d3);
			
			// check consistency
			this.facade.checkConsistency();
			// print state variable information
			System.out.println();
			System.out.println(this.psv);
			System.out.println(this.facade);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void detectGapFlawsTest() {
		System.out.println("[Test]: detectGapFlawsTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create decision with specified end interval
			Decision d1 = this.psv.create(
					v1,
					new String[] {},
					new long[] {23, 23},
					v1.getDurationBounds());
			this.psv.add(d1);
			// add decision with specified end interval
			Decision d2 = this.psv.create(
					v2, 
					new String[] {},
					new long[] {80, 80}, 
					v2.getDurationBounds());
			this.psv.add(d2);
			
			// check consistency
			this.facade.checkConsistency();
			
			// get time-line
			List<Decision> decisions = this.psv.getActiveDecisions();
			Assert.assertNotNull(decisions);
			Assert.assertTrue(!decisions.isEmpty());
			Assert.assertTrue(decisions.size() == 2);
			
			// check temporal bounds
			System.out.println("State Variable's decisions");
			for (Decision dec : decisions) {
				System.out.println("\t- " + dec);
				// check end times
				if (dec.getValue().equals(v1.getLabel())) {
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getLowerBound() == 23);
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getUpperBound() == 23);
				}
				if (dec.getValue().equals(v2.getLabel())) {
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getLowerBound() == 80);
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getUpperBound() == 80);
				} 
			}
			
			
			// check that only a gap is found
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			// a gap has been found
			System.out.println("Detected flaws");
			for (Flaw f : flaws) {
				System.out.println("\t- " + f);
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void stateVariablePathsTest() {
		System.out.println("[Test]: stateVariablePathsTest() --------------------");
		System.out.println();
		
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		ComponentValue v4 = this.psv.addValue("Val-4", new long[] {22, 33}, false);
		ComponentValue v5 = this.psv.addValue("Val-5", new long[] {5, 20}, false);
		ComponentValue v6 = this.psv.addValue("Val-6", true);
		ComponentValue v7 = this.psv.addValue("Val-7", true);
		
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v1);
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v5);
		this.psv.addValueTransition(v5, v3);
		this.psv.addValueTransition(v3, v4);
		this.psv.addValueTransition(v2, v4);
		this.psv.addValueTransition(v4, v6);
		this.psv.addValueTransition(v7, v6);
		System.out.println(this.psv);
		
		// get paths between v1 and v6
		List<List<ComponentValue>> paths = this.psv.getPaths(v1, v6);
		Assert.assertNotNull(paths);
		Assert.assertTrue(paths.size() == 2);
		Assert.assertTrue(paths.get(0).size() == 4);
		Assert.assertTrue(paths.get(1).size() == 4);
		System.out.println("Paths available from " + v1 + " to " + v6);
		for (List<ComponentValue> path : paths) {
			System.out.println("\t- path: " + path);
		}
		
		
		// get paths between v1 and v6
		paths = this.psv.getPaths(v1, v1);
		Assert.assertNotNull(paths);
		Assert.assertTrue(paths.size() == 1);
		Assert.assertTrue(paths.get(0).size() == 3);
		System.out.println("Paths available from " + v1 + " to " + v1);
		for (List<ComponentValue> path : paths) {
			System.out.println("\t- path: " + path);
		}
		
		// get paths between v1 and v7 (no path exists)
		paths = this.psv.getPaths(v1, v7);
		Assert.assertNotNull(paths);
		Assert.assertTrue(paths.isEmpty());
	}
	
	/**
	 * 
	 */
	@Test
	public void detectStateVariablePeaksTest() {
		System.out.println("[Test]: detectStateVariablePeaksTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1",new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.add(d3);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			System.out.println(flaws);
			Assert.assertTrue(flaws.size() == 3);
			System.out.println("Detected flaws ");
			for (Flaw f : flaws) {
				// get flaws 
				Assert.assertNotNull(f);
				// check type
				if (f instanceof Peak) {
					// get flaw solution
					Assert.assertNotNull(f.getSolutions());
					Assert.assertTrue(!f.getSolutions().isEmpty());
					DecisionSchedule s1 = (DecisionSchedule) f.getSolutions().get(0);
					Assert.assertNotNull(s1);
				}
				
			}

			// print state variable information
			System.out.println();
			System.out.println(this.psv);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void detectAndSolveStateVariablePeaksTest() {
		System.out.println("[Test]: detectAndSolveStateVariablePeaksTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1",new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.add(d3);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			// get peak
			Peak peak = (Peak) flaws.get(0);
			// check solutions
			Assert.assertNotNull(peak.getSolutions());
			Assert.assertTrue(!peak.getSolutions().isEmpty());
			
			// get a solution
			FlawSolution solution = peak.getSolutions().get(0);
			Assert.assertNotNull(solution);
			System.out.println("Selected Peak solution to apply");
			System.out.println(solution);
			
			
			// print the network
			System.out.println("Before propagation of flaw solution");
			System.out.println(this.facade);
			// apply
			this.psv.commit(solution);
			// print the network
			System.out.println("After propagation of flaw solution");
			System.out.println(this.facade);

			// check flaws
			flaws = this.psv.detectFlaws();
			Assert.assertTrue(!flaws.isEmpty());
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void displayStateVariableComponentAndSolvePeaksTest() {
		System.out.println("[Test]: displayStateVariableComponentAndSolvePeaksTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1",new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create decisions
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.add(d3);
			Decision d4 = this.psv.create(v1,
					new String[] {},
					new long[] {30, 30}, 
					v1.getDurationBounds());
			this.psv.add(d4);
					
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			// get peak
			Flaw flaw = flaws.get(0);
			int schedulingStepCounter = 0;
			do 
			{
				// get peak
				Peak peak = (Peak) flaw;
				// check solutions
				Assert.assertNotNull(peak.getSolutions());
				
				// get a solution
				FlawSolution solution = peak.getSolutions().get(0);
				Assert.assertNotNull(solution);
				System.out.println("Selected Peak solution to apply");
				System.out.println(solution);
				
				// print the network
				System.out.println("Before propagation of flaw solution");
				System.out.println(this.facade);
				// apply 
				this.psv.commit(solution);
				schedulingStepCounter++;
				// print the network
				System.out.println("After propagation of flaw solution");
				System.out.println(this.facade);
				
				// check consistency
				this.facade.checkConsistency();
				
				// display component after scheduling
				this.psv.display();
				Thread.sleep(3000);
				
				// detect flaws
				flaws = this.psv.detectFlaws(FlawType.SV_SCHEDULING);
				Assert.assertNotNull(flaws);
				if (!flaws.isEmpty()) {
					flaw = flaws.get(0);
				}
			}
			while (!flaws.isEmpty());
			// check number of scheduling steps done
			System.out.println("Scheduling done in " + schedulingStepCounter + " steps");
			Assert.assertTrue(schedulingStepCounter > 0);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void detectAndSolveGapFlaws() {
		System.out.println("[Test]: detectAndSolveGapFlaws() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", true);
		ComponentValue v2 = this.psv.addValue("Val-2", true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		ComponentValue v4 = this.psv.addValue("Val-4", true);
		
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v4);
		this.psv.addValueTransition(v4, v1);
		try 
		{
			// display component
			this.psv.display();
			Thread.sleep(3000);
			
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, 
					new long[] {5, 5}, new long[] {5, 5});
			// create decision with specified end interval
			this.psv.add(d1);
			
			Decision d2 = this.psv.create(v4, new String[] {}, 
					new long[] {20, 20}, new long[] {5, 5});
			// add decision with specified end interval
			this.psv.add(d2);
			
			// check consistency
			this.facade.checkConsistency();
			
			// get decisions
			List<Decision> decisions = this.psv.getActiveDecisions();
			Assert.assertNotNull(decisions);
			Assert.assertTrue(!decisions.isEmpty());
			Assert.assertTrue(decisions.size() == 2);
			
			
			// display component
			this.psv.display();
			Thread.sleep(3000);
	
			
			// check that only a gap is found
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			// a gap has been found
			System.out.println("Detected flaws");
			for (Flaw f : flaws) {
				System.out.println("\t- " + f);
			}
			
			// get flaw
			Gap gap = (Gap) flaws.get(0);
			// check solution
			Assert.assertTrue(gap.getSolutions().size() == 1);
			
			// solve the flaw
			this.psv.commit(flaws.get(0).getSolutions().get(0));
			// check pending decisions
			Assert.assertFalse(this.psv.getPendingDecisions().isEmpty());
			Assert.assertTrue(this.psv.getPendingDecisions().size() == 2);
			System.out.println("Pending decisions");
			System.out.println(this.psv.getPendingDecisions());
			
			// add pending decision
			for (Decision dec : this.psv.getPendingDecisions()) {
				System.out.println("Activating peding decision " + dec);
				// propagate pending decisions
				this.psv.add(dec);
			}
			
			// check consistency
			this.facade.checkConsistency();
			// display component
			this.psv.display();
			Thread.sleep(5000);
			
			// check pending decisions
			Assert.assertTrue(this.psv.getPendingDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 4);

			// check if all pending relations have been propagated
			List<Relation> prel = this.psv.getPendingRelations();
			Assert.assertTrue(prel.isEmpty());
			
			// check flaws
			flaws = this.psv.detectFlaws();
			Assert.assertTrue(flaws.isEmpty());
			
			this.facade.checkConsistency();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void commitAndRollbackStateVariableGapFlaws() {
		System.out.println("[Test]: commitAndRollbackStateVariableGapFlaws() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", true);
		ComponentValue v2 = this.psv.addValue("Val-2", true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		ComponentValue v4 = this.psv.addValue("Val-4", true);
		
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v4);
		this.psv.addValueTransition(v4, v1);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, 
					new long[] {5, 5}, new long[] {5, 5});
			// create decision with specified end interval
			this.psv.add(d1);
			
			Decision d2 = this.psv.create(v4, new String[] {}, 
					new long[] {20, 20}, new long[] {5, 5});
			// add decision with specified end interval
			this.psv.add(d2);
			
			// check component
			Assert.assertNotNull(this.psv.getActiveDecisions());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			Assert.assertNotNull(this.psv.getPendingDecisions());
			Assert.assertTrue(this.psv.getPendingDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			Assert.assertTrue(this.psv.getPendingRelations().isEmpty());
			
			// check that only a gap is found
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			
			// get flaw
			Gap gap = (Gap) flaws.get(0);
			// check solution
			Assert.assertTrue(gap.getSolutions().size() == 1);
			// get solution
			GapCompletion completion = (GapCompletion) gap.getSolutions().get(0);
			
			// apply solution
			this.psv.commit(completion);
			this.facade.checkConsistency();
			
			// check component
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			System.out.println("Active Decisions:\n" + this.psv.getActiveDecisions());
			Assert.assertFalse(this.psv.getPendingDecisions().isEmpty());
			Assert.assertTrue(this.psv.getPendingDecisions().size() == 2);
			System.out.println("Pending decisions:\n" + this.psv.getPendingDecisions());
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			System.out.println("Active relations:\n" + this.psv.getActiveRelations());
			Assert.assertFalse(this.psv.getPendingRelations().isEmpty());
			Assert.assertTrue(this.psv.getPendingRelations().size() == 3);
			System.out.println("Pending relations:\n" + this.psv.getPendingRelations());
			
			System.out.println();
			System.out.println();
			
			// roll-back solution
			this.psv.rollback(completion);
			this.facade.checkConsistency();
			
			// check component
			Assert.assertNotNull(this.psv.getActiveDecisions());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			System.out.println("Active Decisions:\n" + this.psv.getActiveDecisions());
			Assert.assertNotNull(this.psv.getPendingDecisions());
			Assert.assertTrue(this.psv.getPendingDecisions().isEmpty());
			System.out.println("Pending decisions:\n" + this.psv.getPendingDecisions());
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			System.out.println("Active relations:\n" + this.psv.getActiveRelations());
			Assert.assertTrue(this.psv.getPendingRelations().isEmpty());
			System.out.println("Pending relations:\n" + this.psv.getPendingRelations());
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void commitAndRollbackStateVariableSchedulingFlaws() {
		System.out.println("[Test]: commitAndRollbackStateVariableSchedulingFlaws() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1",new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			
			// get peak
			Peak peak = (Peak) flaws.get(0);
			// check solutions
			Assert.assertNotNull(peak.getSolutions());
			Assert.assertTrue(!peak.getSolutions().isEmpty());
			
			// get a solution
			FlawSolution solution = peak.getSolutions().get(0);
			Assert.assertNotNull(solution);
			System.out.println(solution);
			
			
			// commit solution
			this.psv.commit(solution);
			this.facade.checkConsistency();
			
			// check relations
			Assert.assertFalse(this.psv.getActiveRelations().isEmpty());
			Assert.assertTrue(this.psv.getActiveRelations().size() == 1);
			System.out.println("Active Relations:\n- " + this.psv.getActiveRelations());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			
			// roll-back solution
			this.psv.rollback(solution);
			this.facade.checkConsistency();
			
			// check relations
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			System.out.println("Active Relations:\n- " + this.psv.getActiveRelations());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
