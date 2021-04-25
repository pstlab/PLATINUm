package it.cnr.istc.pst.platinum.domain.component.sv;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.cnr.istc.pst.platinum.ai.framework.domain.DomainComponentBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.PrimitiveStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariableValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.ValuePath;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning.Gap;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning.GapCompletion;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
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
public class StateVariableComponentTestCase 
{
	private static final int ORIGIN = 0;
	private static final int HORIZON = 100;
	private TemporalFacade tf;
	private ParameterFacade pf;
	private PrimitiveStateVariable psv;

	/**
	 * 
	 */
	@Before
	public void init() 
	{
		System.out.println("**********************************************************************************");
		System.out.println("************************* State Variable Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create temporal facade
		this.tf = TemporalFacadeBuilder.createAndSet(this, ORIGIN, HORIZON);
		// create parameter facade
		this.pf = ParameterFacadeBuilder.createAndSet(this);
		// create State Variable
		this.psv = DomainComponentBuilder.createAndSet("SV1", DomainComponentType.SV_PRIMITIVE, tf, pf);
		
		
	}
	
	
	/**
	 * 
	 */
	@After
	public void clear() {
		this.psv = null;
		this.tf = null;
		this.pf = null;
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {1, this.tf.getHorizon()}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long [] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3", new long[] {11, 25}, false);
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			Decision d3 = this.psv.create(v1, new String[] {});
			this.psv.activate(d3);
			
			// check consistency
			this.tf.verify();
			this.pf.verify();
			// print state variable information
			System.out.println();
			System.out.println(this.psv);
			System.out.println(this.tf);
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3", new long[] {1, this.tf.getHorizon()}, true);
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create decision with specified end interval
			Decision d1 = this.psv.create(
					v1,
					new String[] {},
					new long[] {23, 23},
					v1.getDurationBounds());
			this.psv.activate(d1);
			// add decision with specified end interval
			Decision d2 = this.psv.create(
					v2, 
					new String[] {},
					new long[] {80, 80}, 
					v2.getDurationBounds());
			this.psv.activate(d2);
			
			// check consistency
			this.tf.verify();
			this.pf.verify();
			
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
				if (dec.getValue().getLabel().equals(v1.getLabel())) {
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getLowerBound() == 23);
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getUpperBound() == 23);
				}
				if (dec.getValue().getLabel().equals(v2.getLabel())) {
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getLowerBound() == 80);
					Assert.assertTrue(dec.getToken().getInterval().getEndTime().getUpperBound() == 80);
				} 
			}
			
			
			// check that only a gap is found
			List<Flaw> flaws = this.psv.detectFlaws(FlawType.TIMELINE_BEHAVIOR_PLANNING);
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		StateVariableValue v4 = this.psv.addStateVariableValue("Val-4", new long[] {22, 33}, false);
		StateVariableValue v5 = this.psv.addStateVariableValue("Val-5", new long[] {5, 20}, false);
		StateVariableValue v6 = this.psv.addStateVariableValue("Val-6");
		StateVariableValue v7 = this.psv.addStateVariableValue("Val-7");
		StateVariableValue v8 = this.psv.addStateVariableValue("Val-8", new long[] {5, 10}, false);
		
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v1);
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v5);
		this.psv.addValueTransition(v5, v3);
		this.psv.addValueTransition(v3, v4);
		this.psv.addValueTransition(v2, v4);
		this.psv.addValueTransition(v4, v6);
		this.psv.addValueTransition(v7, v8);
		this.psv.addValueTransition(v6, v1);
		this.psv.addValueTransition(v5, v1);
		System.out.println(this.psv);
		
		// get paths between v1 and v6
		List<ValuePath> paths = this.psv.getPaths(v1, v6);
		Assert.assertNotNull(paths);
		Assert.assertTrue(paths.size() == 2);
		System.out.println("Two paths expected between Val-1 and Val-6:");
		for (ValuePath path : paths) {
			System.out.println("\t- " + path + "\n");
		}
		
		// get paths between v1 and v6
		paths = this.psv.getPaths(v1, v1);
		Assert.assertNotNull(paths);
		Assert.assertTrue(paths.size() == 4);
		System.out.println("Four paths expected between Val-1 and Val-1:");
		for (ValuePath path : paths) {
			System.out.println("\t- " + path + "\n");
		}
		
		// check no path case
		paths = this.psv.getPaths(v7, v1);
		Assert.assertNotNull(paths);
		System.out.println("No path expected [" + paths.size() + "]:\n- source: " + v7 + "\n- target: " + v1);
		Assert.assertTrue(paths.isEmpty());
		Assert.assertTrue(paths.size() == 0);
		
		paths = this.psv.getPaths(v7, v7);
		Assert.assertNotNull(paths);
		System.out.println("No path expected [" + paths.size() + "]:\n- source: " + v7 + "\n- target: " + v7);
		Assert.assertTrue(paths.isEmpty());
		Assert.assertTrue(paths.size() == 0);
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1",new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.activate(d3);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			System.out.println(flaws);
			Assert.assertTrue(flaws.size() == 1);
			System.out.println("Detected flaws ");
			for (Flaw f : flaws) {
				// get flaws 
				Assert.assertNotNull(f);
				Assert.assertNotNull(f.getSolutions());
				Assert.assertTrue(!f.getSolutions().isEmpty());
				Assert.assertNotNull(f.getSolutions().get(0));
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.activate(d3);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			
			// check solutions
			Assert.assertNotNull(flaws.get(0).getSolutions());
			Assert.assertTrue(!flaws.get(0).getSolutions().isEmpty());
			
			// get a solution
			FlawSolution solution = flaws.get(0).getSolutions().get(0);
			Assert.assertNotNull(solution);
			System.out.println("Selected Peak solution to apply");
			System.out.println(solution);
			
			
			// print the network
			System.out.println("Before propagation of flaw solution");
			System.out.println(this.tf);
			// apply
			this.psv.commit(solution);
			// print the network
			System.out.println("After propagation of flaw solution");
			System.out.println(this.tf);

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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1",new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create decisions
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			Decision d3 = this.psv.create(v3, new String[] {});
			this.psv.activate(d3);
			Decision d4 = this.psv.create(v1,
					new String[] {},
					new long[] {30, 30}, 
					v1.getDurationBounds());
			this.psv.activate(d4);
			// initialize scheduling step counter
			int schedulingStepCounter = 0;
			// initialize the list of flaws
			List<Flaw> flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
			do 
			{
				// print diagnostic data about the network 
				this.tf.printDiagnosticData();
				
				Assert.assertNotNull(flaws);
				// get peak
				Flaw flaw = flaws.get(0);
				Assert.assertTrue(!flaws.isEmpty());
				
				// check solutions
				Assert.assertNotNull(flaw.getSolutions());
				
				// get a solution
				FlawSolution solution = flaw.getSolutions().get(0);
				Assert.assertNotNull(solution);
				System.out.println("Selected Peak solution to apply");
				System.out.println(solution);
				
				// print the network
				System.out.println("Before propagation of flaw solution");
				// apply 
				this.psv.commit(solution);
				schedulingStepCounter++;
				// check consistency
				this.tf.verify();
				// display component after scheduling
				this.psv.display();
				Thread.sleep(3000);
				
				// detect flaws
				flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {1, this.tf.getHorizon()}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 10}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3", new long[] {5, 5}, true);
		StateVariableValue v4 = this.psv.addStateVariableValue("Val-4", new long[] {1, this.tf.getHorizon()}, true);
		StateVariableValue v5 = this.psv.addStateVariableValue("Val-5", new long[] {5, 5}, true);
		StateVariableValue v6 = this.psv.addStateVariableValue("Val-6", new long[] {3, 3}, true);
		
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v4);
		this.psv.addValueTransition(v3, v5);
		this.psv.addValueTransition(v3, v6);
		this.psv.addValueTransition(v4, v1);
		this.psv.addValueTransition(v5, v4);
		this.psv.addValueTransition(v6, v4);
		
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, 
					new long[] {5, 5}, new long[] {5, 5});
			// create decision with specified end interval
			this.psv.activate(d1);
			
			Decision d2 = this.psv.create(v4, new String[] {}, 
					new long[] {18, 20}, new long[] {5, 7});
			// add decision with specified end interval
			this.psv.activate(d2);
			
			// check consistency
			this.tf.verify();
			
			// get decisions
			List<Decision> decisions = this.psv.getActiveDecisions();
			Assert.assertNotNull(decisions);
			Assert.assertTrue(!decisions.isEmpty());
			Assert.assertTrue(decisions.size() == 2);
			
			
			// display component
			this.psv.display();
			Thread.sleep(3000);
	
			// check that only a gap is found
			List<Flaw> flaws = this.psv.detectFlaws(FlawType.TIMELINE_BEHAVIOR_PLANNING);
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			// a gap has been found
			System.out.println("Detected flaws");
			for (Flaw f : flaws) {
				System.out.println("\t- " + f);
			}
			
			// get flaw
			Gap gap = (Gap) flaws.get(0);
			// check solution
			Assert.assertTrue(gap.getSolutions().size() == 2);
			
			// select a solution to commit
			FlawSolution sol = flaws.get(0).getSolutions().get(0);
			// solve the flaw
			this.psv.commit(sol);
			System.out.println("Committed solution:\n- " + sol + "\n");
			
			// check pending decisions
			Assert.assertFalse(this.psv.getPendingDecisions().isEmpty());
			System.out.println("Pending decisions");
			System.out.println(this.psv.getPendingDecisions());
			
			// add pending decision
			for (Decision dec : this.psv.getPendingDecisions()) {
				System.out.println("Activating peding decision " + dec);
				// propagate pending decisions
				this.psv.activate(dec);
			}
			
			// check consistency
			this.tf.verify();
			// display component
			this.psv.display();
			Thread.sleep(5000);
			
			// check pending decisions
			Assert.assertTrue(this.psv.getPendingDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 4);

			// check if all pending relations have been propagated
			Set<Relation> prel = this.psv.getPendingRelations();
			Assert.assertTrue(prel.isEmpty());
			
			// check flaws
			flaws = this.psv.detectFlaws();
			Assert.assertTrue(flaws.isEmpty());
			
			this.tf.verify();
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1");
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2");
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		StateVariableValue v4 = this.psv.addStateVariableValue("Val-4");
		
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
			this.psv.activate(d1);
			
			Decision d2 = this.psv.create(v4, new String[] {}, 
					new long[] {20, 20}, new long[] {5, 5});
			// add decision with specified end interval
			this.psv.activate(d2);
			
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
			this.tf.verify();
			this.pf.verify();
			
			// check component
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			System.out.println("Active Decisions:\n" + this.psv.getActiveDecisions());
			Assert.assertFalse(this.psv.getPendingDecisions().isEmpty());
			System.out.println("Pending decisions:\n" + this.psv.getPendingDecisions());
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			System.out.println("Active relations:\n" + this.psv.getActiveRelations());
			Assert.assertFalse(this.psv.getPendingRelations().isEmpty());
			System.out.println("Pending relations:\n" + this.psv.getPendingRelations());
			
			System.out.println();
			System.out.println();
			
			// roll-back solution
			this.psv.rollback(completion);
			this.tf.verify();
			this.pf.verify();
			
			// check component
			Assert.assertNotNull(this.psv.getActiveDecisions());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
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
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1",new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			
			// check flaws
			List<Flaw> flaws = this.psv.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			Assert.assertTrue(this.psv.getActiveRelations().isEmpty());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			
			// check solutions
			Assert.assertNotNull(flaws.get(0).getSolutions());
			Assert.assertTrue(!flaws.get(0).getSolutions().isEmpty());
			
			// get a solution
			FlawSolution solution = flaws.get(0).getSolutions().get(0);
			Assert.assertNotNull(solution);
			System.out.println(solution);
			
			
			// commit solution
			this.psv.commit(solution);
			this.tf.verify();
			
			// check relations
			Assert.assertFalse(this.psv.getActiveRelations().isEmpty());
			Assert.assertTrue(this.psv.getActiveRelations().size() == 1);
			System.out.println("Active Relations:\n- " + this.psv.getActiveRelations());
			Assert.assertFalse(this.psv.getActiveDecisions().isEmpty());
			Assert.assertTrue(this.psv.getActiveDecisions().size() == 2);
			
			// roll-back solution
			this.psv.rollback(solution);
			this.tf.verify();
			
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
	
	/**
	 * 
	 */
	@Test
	public void applyAndRetractFlawSolutionTest()
	{
		System.out.println("[Test]: applyAndRetractFlawSolutionTest() --------------------");
		System.out.println();
		// create the state variable description
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1",new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3");
		// add transitions
		this.psv.addValueTransition(v1, v3);
		this.psv.addValueTransition(v3, v2);
		this.psv.addValueTransition(v2, v3);
		this.psv.addValueTransition(v3, v1);
		
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {});
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			
			// detect flaws
			List<Flaw> flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			
			// get the flaw
			Flaw flaw = flaws.get(0);
			// check solutions
			List<FlawSolution> solutions = flaw.getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertFalse(solutions.isEmpty());
			Assert.assertTrue(solutions.size() >= 1 && solutions.size() <= 2);
			
			// apply a solution
			FlawSolution solution = solutions.get(0);
			System.out.println("Try to apply solution: " + solution + "\n");
			this.psv.commit(solution);
			
			// check consistency 
			this.tf.verify();
			System.out.println(".... solution successfully applied\n");
			
			
			// no flaws expected
			flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.isEmpty());
			System.out.println("No more flaws on resource " + this.psv + "\n");
			
			// try to roll-back applied solution
			System.out.println("Try to rollback applied solution: " + solution + "\n");
			// retract applied solution
			this.psv.rollback(solution);
			
			// check consistency
			this.tf.verify();
			System.out.println(".... solution successfully retracted\n");
			
			
			// flaws expected
			flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
			Assert.assertNotNull(flaws);
			Assert.assertFalse(flaws.isEmpty());
			Assert.assertTrue(flaws.size() == 1);
			
			// try again to solve the the flaw and check the resulting state
			flaw = flaws.get(0);
			// check solutions
			solutions = flaw.getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertFalse(solutions.isEmpty());
			Assert.assertTrue(solutions.size() >= 1 && solutions.size() <= 2);
			
			// apply a solution
			solution = solutions.get(0);
			System.out.println("Try to apply solution: " + solution + "\n");
			this.psv.commit(solution);
			
			// check consistency 
			this.tf.verify();
			System.out.println(".... solution successfully applied\n");
			
			
			// no flaws expected
			flaws = this.psv.detectFlaws(FlawType.TIMELINE_OVERFLOW);
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.isEmpty());
			System.out.println("No more flaws on resource " + this.psv + "\n");
		}
		catch (FlawSolutionApplicationException | ConsistencyCheckException | DecisionPropagationException | UnsolvableFlawException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
