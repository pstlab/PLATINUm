package it.cnr.istc.pst.platinum.domain.component.pdb;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.cnr.istc.pst.platinum.ai.framework.domain.PlanDataBaseBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.PlanDataBaseComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.SynchronizationConstraint;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.SynchronizationRule;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.TokenVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.ExternalStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.PrimitiveStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariableValue;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ProblemInitializationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.Problem;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan.Goal;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan.GoalExpansion;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan.GoalUnification;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling.OverlappingSet;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.EnumerationParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;



/**
 * 
 * @author alessandro
 *
 */
public class PlanDataBaseTestCase {
	private PlanDataBaseComponent pdb;
	
	/**
	 * 
	 */
	@BeforeAll
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("****************************** PDB Component Test Case ***************************");
		System.out.println("**********************************************************************************");
		
		this.pdb = (PlanDataBaseComponent) PlanDataBaseBuilder.createAndSet("PDB", 0, 50);
	}
	
	/**
	 * 
	 */
	@AfterEach
	public void clear() {
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
	public void setupAndDisplayProblem() {
		System.out.println("[Test]: setupAndDisplayProblem() --------------------");
		System.out.println();
		try 
		{
			// create parameter domain
			ParameterDomain xAxis = this.pdb.createParameterDomain("x-axis", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			// add values
			StateVariableValue v11 = c1.addStateVariableValue("Val11", new long[] {5, 11}, false);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			StateVariableValue v13 = c1.addStateVariableValue("Val13", new long[] {6, 12}, false);
			// add parameter to value description
			v13.addParameterPlaceHolder(xAxis);
			
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			// add values
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			v21.addParameterPlaceHolder(xAxis);
			
			StateVariableValue v22 = c2.addStateVariableValue("Val22");
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			// create external state variables
			ExternalStateVariable c3 = this.pdb.createDomainComponent("C3", DomainComponentType.SV_EXTERNAL);
			// add values
			StateVariableValue v31 = c3.addStateVariableValue("Val31");
			StateVariableValue v32 = c3.addStateVariableValue("Val32");
			StateVariableValue v33 = c3.addStateVariableValue("Val33");
			// add transitions
			c3.addValueTransition(v31, v32);
			c3.addValueTransition(v32, v33);
			c3.addValueTransition(v33, v31);
			// add component
			this.pdb.addDomainComponent(c3);		
			
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {"?xPost"});
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			TokenVariable cd1 = rule.addTokenVariable(v32, new String[] {});
			rule.addTemporalConstraint(rule.getTriggerer(), cd0, RelationType.DURING, new long[][] {{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}});
			rule.addTemporalConstraint(rule.getTriggerer(), cd1, RelationType.BEFORE, new long[][] {{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}});
			try {
				
				// add rule
				this.pdb.addSynchronizationRule(rule);
				
			} catch (SynchronizationCycleException ex) {
				System.err.println(ex.getMessage());
				assertTrue(false);
			}
	
			// create Problem
			Problem problem = new Problem();
			
			// add fact
			problem.addFact(v12, new String[] {},
					new long[] {0, 0}, 
					new long[] {5, this.pdb.getHorizon()});
			
			// add fact
			problem.addFact(v22, new String[] {},
					new long[] {0, 0}, 
					new long[] {5, this.pdb.getHorizon()});
			
			// add fact
			problem.addFact(v31, new String[] {},
					new long[] {0, 0}, 
					new long[] {10, 10});
			
			// add fact
			problem.addFact(v32, new String[] {},
					new long[] {10, 10}, 
					new long[] {40, 40});
			
			// add fact
			problem.addFact(v33, new String[] {},
					new long[] {40, 40},
					new long[] {this.pdb.getHorizon(), this.pdb.getHorizon()});
			
			// add goal
			problem.addGoal(v21, new String[] {"?xPos"}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			assertNotNull(problem);
			assertNotNull(problem.getFacts());
			assertTrue(problem.getFacts().size() == 5);
			assertTrue(problem.getGoals().size() == 1);
		
			// set problem
			this.pdb.setup(problem);
			
			// show plan
			this.pdb.display();
			Thread.sleep(5000);
		}
		catch (ProblemInitializationException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void setupDomainComponentAndSynchronizationsTest() {
		System.out.println("[Test]: setupDomainComponentAndSynchronizationsTest() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c1);
			// add values
			StateVariableValue v11 = c1.addStateVariableValue("Val11", new long[] {4, 8}, false);
			assertNotNull(v11);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			assertNotNull(v12);
			StateVariableValue v13 = c1.addStateVariableValue("Val13", new long[] {2, 8}, false);
			assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// print component
			System.out.println(c1);
			// add component
			this.pdb.addDomainComponent(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c2);
			// add values
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			assertNotNull(v21);
			StateVariableValue v22 = c2.addStateVariableValue("Val22", new long[] {2, 7}, false);
			assertNotNull(v22);
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// print component
			System.out.println(c2);
			// add component
			this.pdb.addDomainComponent(c2);
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			assertNotNull(values);
			assertFalse(values.isEmpty());
			assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			assertNotNull(decs);
			assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(rule.getTriggerer(), cd0, RelationType.DURING, new long[][] {{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}});
			assertNotNull(constraint);
			System.out.println(rule);
			
			System.out.println();
			System.out.println(this.pdb);
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
		} 
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}

	
	/**
	 * 
	 */
	@Test
	public void detectGoalTest() {
		System.out.println("[Test]: detectGoalTest() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c1);
			// add values
			StateVariableValue v11 = c1.addStateVariableValue("Val11");
			assertNotNull(v11);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			assertNotNull(v12);
			StateVariableValue v13 = c1.addStateVariableValue("Val13", new long[] {4, 8}, false);
			assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c2);
			// add values
			StateVariableValue v21 = c2.addStateVariableValue("Val21", new long[] {2, 7}, false);
			assertNotNull(v21);
			StateVariableValue v22 = c2.addStateVariableValue("Val22");
			assertNotNull(v22);
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			assertNotNull(values);
			assertFalse(values.isEmpty());
			assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			assertNotNull(decs);
			assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()},
						{0, this.pdb.getHorizon()}
					});
			assertNotNull(constraint);
			try {
				// add rule
				this.pdb.addSynchronizationRule(rule);
			} catch (SynchronizationCycleException ex) {
				System.err.println(ex.getMessage());
				assertTrue(false);
			}
			
			// print pending decisions
			assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
			
			
			
			
			// add pending decision
			Decision dec = ((PlanDataBaseComponent) this.pdb).create(v13, new String[] {});
			assertNotNull(dec);
			assertTrue(this.pdb.isPending(dec));
			assertFalse(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().size() == 1);
			assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
		
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.size() == 1);
			Goal goal = (Goal) flaws.get(0);
			// check goal
			assertTrue(goal.getDecision().equals(dec));
			List<FlawSolution> solutions = goal.getSolutions();
			assertNotNull(solutions);
			assertTrue(solutions.size() == 1);
			
			// get expansion solution
			GoalExpansion exp = (GoalExpansion) solutions.get(0);
			// check rule
			assertTrue(exp.getSynchronizationRule().equals(rule));
			
			// solve flaw
			this.pdb.commit(exp);
			
			assertFalse(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().size() == 1);
			assertFalse(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().size() == 1);
			
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
			System.out.println("PDB Pending Relations");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingRelations());
			System.out.println("PDB Active Relations");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveRelations());
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void detectMultiplaFlaws() {
		System.out.println("[Test]: detectMultiplaFlaws() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c1);
			// add values
			StateVariableValue v11 = c1.addStateVariableValue("Val11");
			assertNotNull(v11);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			assertNotNull(v12);
			StateVariableValue v13 = c1.addStateVariableValue("Val13");
			assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);
			System.out.println(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c2);
			// add values
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			assertNotNull(v21);
			StateVariableValue v22 = c2.addStateVariableValue("Val22", new long[] {1, 20}, false);
			assertNotNull(v22);
			StateVariableValue v23 = c2.addStateVariableValue("Val23", new long[] {2, 8}, false);
			assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);	
			System.out.println(c2);
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			assertNotNull(values);
			assertFalse(values.isEmpty());
			assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			assertNotNull(decs);
			assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			assertNotNull(rule);
			
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()}, 
						{0, this.pdb.getHorizon()}
					});
			
			assertNotNull(constraint);
			
			// add rule
			this.pdb.addSynchronizationRule(rule);
			System.out.println(rule);
		
			// print pending decisions
			assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
		
			// add pending decision
			Decision dec1 = ((PlanDataBaseComponent) this.pdb).create(v13, new String[] {});
			// add decisions
			Decision dec2 = ((PlanDataBaseComponent) this.pdb).create(v21, new String[] {});
			((PlanDataBaseComponent) this.pdb).activate(dec2);
			Decision dec3 = ((PlanDataBaseComponent) this.pdb).create(v22, new String[] {});
			((PlanDataBaseComponent) this.pdb).activate(dec3);
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			assertNotNull(flaws);
			assertTrue(!flaws.isEmpty());
			for (Flaw flaw : flaws) {
				// check flaw
				if (flaw instanceof Goal) {
					Goal goal = (Goal) flaw;
					assertTrue(goal.getDecision().equals(dec1));
					System.out.println(goal);
				}
				if (flaw instanceof OverlappingSet) {
					OverlappingSet peak = (OverlappingSet) flaw;
					assertTrue(peak.size() == 2);
					assertTrue(peak.getDecisions().contains(dec2));
					assertTrue(peak.getDecisions().contains(dec3));
					assertFalse(peak.getDecisions().contains(dec1));
					System.out.println(peak);
				}
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void displayPlanDataBaseTest() {
		System.out.println("[Test]: displayPlanDataBaseTest() --------------------");
		System.out.println();
		try {
			
			// check PDB
			assertNotNull(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c1);
			// add values
			StateVariableValue v11 = c1.addStateVariableValue("Val11");
			assertNotNull(v11);
			StateVariableValue v12 = c1.addStateVariableValue("Val12", new long[] {5, 10}, false);
			assertNotNull(v12);
			StateVariableValue v13 = c1.addStateVariableValue("Val13");
			assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			assertNotNull(c2);
			// add values
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			assertNotNull(v21);
			StateVariableValue v22 = c2.addStateVariableValue("Val22");
			assertNotNull(v22);
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			assertNotNull(values);
			assertFalse(values.isEmpty());
			assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			assertNotNull(decs);
			assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}
					});
			assertNotNull(constraint);
			// add rule
			this.pdb.addSynchronizationRule(rule);
			
			// print PDB
			System.out.println(this.pdb);
			
			// print pending decisions
			assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
		
		
			Decision dec1 = ((PlanDataBaseComponent) this.pdb).
					create(v13, new String[] {}, 
							new long[] {11, 23}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).activate(dec1);

			Decision dec2 = ((PlanDataBaseComponent) this.pdb).
					create(v21, new String[] {}, 
							new long[] {5, 5}, v21.getDurationBounds());
			((PlanDataBaseComponent) this.pdb).activate(dec2);
			
			Decision dec3 = ((PlanDataBaseComponent) this.pdb).
					create(v22, new String[] {}, 
							new long[] {11, 11}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).activate(dec3);
			
			Decision dec4 = ((PlanDataBaseComponent) this.pdb).
					create(v12, new String[] {}, 
							new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).activate(dec4);
			
			Decision dec5 = ((PlanDataBaseComponent) this.pdb).
					create(v12, new String[] {}, 
							new long[] {15, 18}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).activate(dec5);
			
			Decision dec6 = ((PlanDataBaseComponent) this.pdb).
					create(v22, new String[] {}, 
							new long[] {23, 23}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).activate(dec6);
			
			// display plan data-base
			this.pdb.display();
			Thread.sleep(5000);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void detectUnificationTest() {
		System.out.println("[Test]: detectUnificationTest() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue v11 = c1.addStateVariableValue("Val11", new long[] {3, 7}, false);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			StateVariableValue v13 = c1.addStateVariableValue("Val13");
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			StateVariableValue v22 = c2.addStateVariableValue("Val22", new long[] {5, 10}, false);
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			rule.addTemporalConstraint(rule.getTriggerer(), cd0, RelationType.DURING, new long[][] {{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}});
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
			
			// add a decision
			Decision fact = this.pdb.create(v22, new String[] {}, new long[] {0, 0}, new long[] {10, 10}, new long[] {10, 10});
			this.pdb.activate(fact);
			fact = this.pdb.create(v22, new String[] {}, new long[] {22, 22}, new long[] {25, 25}, new long[] {3, 3});
			this.pdb.activate(fact);
			fact = this.pdb.create(v13, new String[] {});
			this.pdb.activate(fact);
			
			// add pending decision
			Decision g1 = this.pdb.create(v22, new String[] {});
			
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.size() == 2);
			

			System.out.println("Detected flaws");
			System.out.println(flaws);
			
			// get a flaw
			Flaw flaw = flaws.get(0);
			System.out.println("Flaw solutions");
			System.out.println(flaw.getSolutions());
			
			// check solutions
			assertFalse(flaw.getSolutions().isEmpty());
			assertTrue(flaw.getSolutions().size() == 3);
			
			for (FlawSolution sol : flaw.getSolutions()) {
				if(sol instanceof GoalUnification) {
					GoalUnification unif = (GoalUnification) sol;
					assertTrue(unif.getGoalDecision().equals(g1));
				}
			}
			
			this.pdb.display();
			Thread.sleep(3000);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	
	/**
	 * 
	 */
	@Test
	public void applyAndRetractExpansionOperator() {
		System.out.println("[Test]: applyAndRetractExpansionOperator() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue v11 = c1.addStateVariableValue("Val11", new long[] {3, 8}, false);
			StateVariableValue v12 = c1.addStateVariableValue("Val12");
			StateVariableValue v13 = c1.addStateVariableValue("Val13");
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue v21 = c2.addStateVariableValue("Val21");
			StateVariableValue v22 = c2.addStateVariableValue("Val22", new long[] {5, 11}, false);
			StateVariableValue v23 = c2.addStateVariableValue("Val23");
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0,
					RelationType.DURING, 
					new long[][] {
						{this.pdb.getOrigin(), this.pdb.getHorizon()},
						{this.pdb.getOrigin(), this.pdb.getHorizon()}
					});
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
			
			// add facts
			Decision f0 = this.pdb.create(v11, new String[] {});
			this.pdb.activate(f0);
			
			Decision f1 = this.pdb.create(
					v21,
					new String[] {},
					new long[] {22, 22}, 
					new long[] {25, 25}, 
					v21.getDurationBounds());
			this.pdb.activate(f1);
			
			// add goal
			Decision g1 = this.pdb.create(v13, new String[] {});
			g1.setMandatoryExpansion();
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.size() == 1);
			
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 2);
			assertTrue(this.pdb.getPendingDecisions().contains(g1));
			assertFalse(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// get flaw solution
			GoalExpansion exp = (GoalExpansion) flaws.get(0).getSolutions().get(0);
			System.out.println(exp);
			
			// apply solution
			this.pdb.commit(exp);
			
			// check data
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 3);
			assertTrue(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// retract solution
			this.pdb.rollback(exp);
			
			// check data
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 2);
			assertFalse(this.pdb.getActiveDecisions().contains(g1));
			assertTrue(this.pdb.getPendingDecisions().contains(g1));
			System.out.println(g1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void applyAndRetractExpansionWithParameterConstraintTest() {
		System.out.println("[Test]: applyAndRetractExpansionWithParameterConstraintTest() --------------------");
		System.out.println();
		try {
			// check PDB
			assertNotNull(this.pdb);
			
			// create parameter domains
			EnumerationParameterDomain positions = this.pdb.createParameterDomain("positions", 
					ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			// set values
			positions.setValues(new String[] {
					"kitchen",
					"bathroom",
					"bedroom",
					"garden"
			});
			
			// create state variable 
			PrimitiveStateVariable robotAction = this.pdb.createDomainComponent("RobotAction", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue idle = robotAction.addStateVariableValue("Idle");
			StateVariableValue go = robotAction.addStateVariableValue("Go");
			go.addParameterPlaceHolder(positions);
			
			// add transitions
			robotAction.addValueTransition(idle, go);
			robotAction.addValueTransition(go, idle);
			// add component
			this.pdb.addDomainComponent(robotAction);		
			
			// create state variables
			PrimitiveStateVariable robotPosition = this.pdb.createDomainComponent("RobotPosition", DomainComponentType.SV_PRIMITIVE);
			StateVariableValue at = robotPosition.addStateVariableValue("At");
			at.addParameterPlaceHolder(positions);
			
			StateVariableValue moving = robotPosition.addStateVariableValue("Moving", new long[] {5, 11}, false);
			moving.addParameterPlaceHolder(positions);
			// add transitions
			robotPosition.addValueTransition(at, moving);
			robotPosition.addValueTransition(moving, at);
			// add component
			this.pdb.addDomainComponent(robotPosition);		
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(go, new String[] {"?to"});
			// add temporal constraint
			TokenVariable cd0 = rule.addTokenVariable(at, new String[] {"?at"});
			rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0,
					RelationType.MEETS, 
					new long[][] {});
			// add parameter constraint
			rule.addParameterConstraint(rule.getTriggerer(), cd0, RelationType.EQUAL_PARAMETER, "?to", "?at");
			rule.addParameterConstraint(rule.getTriggerer(), rule.getTriggerer(), RelationType.BIND_PARAMETER, "?to", "kitchen");
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
			
			// add facts
			Decision f0 = this.pdb.create(idle, new String[] {});
			this.pdb.activate(f0);
			
			// add goal
			Decision g1 = this.pdb.create(go, new String[] {"?destination"});
			g1.setMandatoryExpansion();
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			assertNotNull(flaws);
			assertTrue(flaws.size() == 1);
			
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 1);
			assertTrue(this.pdb.getPendingDecisions().contains(g1));
			assertFalse(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// get flaw solution
			GoalExpansion exp = (GoalExpansion) flaws.get(0).getSolutions().get(0);
			System.out.println(exp);
			
			// apply solution
			this.pdb.commit(exp);
			this.pdb.verify();
			
			// check data
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 2);
			assertTrue(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			
			// print current plan
			SolutionPlan plan = this.pdb.getSolutionPlan();
			System.out.println();
			System.out.println(plan);
			System.out.println();
			
			// retract solution
			this.pdb.rollback(exp);
			this.pdb.verify();
			
			// check data
			assertNotNull(this.pdb.getPendingDecisions());
			assertTrue(this.pdb.getPendingDecisions().size() == 1);
			assertNotNull(this.pdb.getActiveDecisions());
			assertTrue(this.pdb.getActiveDecisions().size() == 1);
			assertFalse(this.pdb.getActiveDecisions().contains(g1));
			assertTrue(this.pdb.getPendingDecisions().contains(g1));
			System.out.println(g1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
}
