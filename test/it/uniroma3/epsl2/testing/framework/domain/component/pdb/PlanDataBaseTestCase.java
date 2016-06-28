package it.uniroma3.epsl2.testing.framework.domain.component.pdb;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.domain.PlanDataBaseFactory;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseComponent;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.domain.component.pdb.TokenVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.ExternalStateVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.PrimitiveStateVariable;
import it.uniroma3.epsl2.framework.lang.ex.ProblemInitializationException;
import it.uniroma3.epsl2.framework.lang.ex.SynchronizationCycleException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.problem.Problem;
import it.uniroma3.epsl2.framework.microkernel.resolver.plan.Goal;
import it.uniroma3.epsl2.framework.microkernel.resolver.plan.GoalExpansion;
import it.uniroma3.epsl2.framework.microkernel.resolver.plan.GoalUnification;
import it.uniroma3.epsl2.framework.microkernel.resolver.sv.scheduling.Peak;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseTestCase {

	private PlanDataBaseComponent pdb;
	
	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("****************************** PDB Component Test Case ***************************");
		System.out.println("**********************************************************************************");
		
		// create Plan Data Base
		PlanDataBaseFactory factory = PlanDataBaseFactory.getInstance();
		this.pdb = (PlanDataBaseComponent) factory.create("PDB", 0, 50);
	}
	
	/**
	 * 
	 */
	@After
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
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			// add values
			ComponentValue v11 = c1.addValue("Val11", false);
			ComponentValue v12 = c1.addValue("Val12", true);
			ComponentValue v13 = c1.addValue("Val13", false);
			// add parameter to value description
			v13.addParameterPlaceHolder(ParameterType.NUMERIC_PARAMETER_TYPE, xAxis);
			
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			// add values
			ComponentValue v21 = c2.addValue("Val21", true);
			v21.addParameterPlaceHolder(ParameterType.NUMERIC_PARAMETER_TYPE, xAxis);
			
			ComponentValue v22 = c2.addValue("Val22", false);
			ComponentValue v23 = c2.addValue("Val23", true);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			// create external state variables
			ExternalStateVariable c3 = this.pdb.createDomainComponent("C3", DomainComponentType.SV_EXTERNAL);
			// add values
			ComponentValue v31 = c3.addValue("Val31", true);
			ComponentValue v32 = c3.addValue("Val32", false);
			ComponentValue v33 = c3.addValue("Val33", true);
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
				Assert.assertTrue(false);
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
			
			Assert.assertNotNull(problem);
			Assert.assertNotNull(problem.getFacts());
			Assert.assertTrue(problem.getFacts().size() == 5);
			Assert.assertTrue(problem.getGoals().size() == 1);
		
			// set problem
			this.pdb.setup(problem);
			
			// show plan
			this.pdb.display();
			Thread.sleep(5000);
		}
		catch (ProblemInitializationException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
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
	public void setupDomainComponentAndSynchronizationsTest() {
		System.out.println("[Test]: setupDomainComponentAndSynchronizationsTest() --------------------");
		System.out.println();
		try {
			// check PDB
			Assert.assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c1);
			// add values
			ComponentValue v11 = c1.addValue("Val11", false);
			Assert.assertNotNull(v11);
			ComponentValue v12 = c1.addValue("Val12", true);
			Assert.assertNotNull(v12);
			ComponentValue v13 = c1.addValue("Val13", false);
			Assert.assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// print component
			System.out.println(c1);
			// add cmponent
			this.pdb.addDomainComponent(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c2);
			// add values
			ComponentValue v21 = c2.addValue("Val21", true);
			Assert.assertNotNull(v21);
			ComponentValue v22 = c2.addValue("Val22", false);
			Assert.assertNotNull(v22);
			ComponentValue v23 = c2.addValue("Val23", true);
			Assert.assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// print component
			System.out.println(c2);
			// add component
			this.pdb.addDomainComponent(c2);
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			Assert.assertNotNull(values);
			Assert.assertFalse(values.isEmpty());
			Assert.assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			Assert.assertNotNull(decs);
			Assert.assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			Assert.assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			Assert.assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(rule.getTriggerer(), cd0, RelationType.DURING, new long[][] {{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}});
			Assert.assertNotNull(constraint);
			System.out.println(rule);
			
			System.out.println();
			System.out.println(this.pdb);
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
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
	public void detectGoalTest() {
		System.out.println("[Test]: detectGoalTest() --------------------");
		System.out.println();
		try {
			// check PDB
			Assert.assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c1);
			// add values
			ComponentValue v11 = c1.addValue("Val11", true);
			Assert.assertNotNull(v11);
			ComponentValue v12 = c1.addValue("Val12", true);
			Assert.assertNotNull(v12);
			ComponentValue v13 = c1.addValue("Val13", false);
			Assert.assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c2);
			// add values
			ComponentValue v21 = c2.addValue("Val21", false);
			Assert.assertNotNull(v21);
			ComponentValue v22 = c2.addValue("Val22", true);
			Assert.assertNotNull(v22);
			ComponentValue v23 = c2.addValue("Val23", true);
			Assert.assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			Assert.assertNotNull(values);
			Assert.assertFalse(values.isEmpty());
			Assert.assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			Assert.assertNotNull(decs);
			Assert.assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			Assert.assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			Assert.assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()},
						{0, this.pdb.getHorizon()}
					});
			Assert.assertNotNull(constraint);
			try {
				// add rule
				this.pdb.addSynchronizationRule(rule);
			} catch (SynchronizationCycleException ex) {
				System.err.println(ex.getMessage());
				Assert.assertTrue(false);
			}
			
			// print pending decisions
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
			
			
			
			
			// add pending decision
			Decision dec = ((PlanDataBaseComponent) this.pdb).createDecision(v13, new String[] {});
			Assert.assertNotNull(dec);
			Assert.assertTrue(dec.isPending());
			Assert.assertFalse(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().size() == 1);
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
		
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.size() == 1);
			Goal goal = (Goal) flaws.get(0);
			// check goal
			Assert.assertTrue(goal.getDecision().equals(dec));
			List<FlawSolution> solutions = goal.getSolutions();
			Assert.assertNotNull(solutions);
			Assert.assertTrue(solutions.size() == 1);
			
			// get expansion solution
			GoalExpansion exp = (GoalExpansion) solutions.get(0);
			// check rule
			Assert.assertTrue(exp.getSynchronizationRule().equals(rule));
			
			// solve flaw
			this.pdb.commit(exp);
			
			Assert.assertFalse(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().size() == 1);
			Assert.assertFalse(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().size() == 1);
			
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
			Assert.assertTrue(false);
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
			Assert.assertNotNull(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c1);
			// add values
			ComponentValue v11 = c1.addValue("Val11", true);
			Assert.assertNotNull(v11);
			ComponentValue v12 = c1.addValue("Val12", true);
			Assert.assertNotNull(v12);
			ComponentValue v13 = c1.addValue("Val13", true);
			Assert.assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c2);
			// add values
			ComponentValue v21 = c2.addValue("Val21", true);
			Assert.assertNotNull(v21);
			ComponentValue v22 = c2.addValue("Val22", false);
			Assert.assertNotNull(v22);
			ComponentValue v23 = c2.addValue("Val23", false);
			Assert.assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			Assert.assertNotNull(values);
			Assert.assertFalse(values.isEmpty());
			Assert.assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			Assert.assertNotNull(decs);
			Assert.assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			Assert.assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			Assert.assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()}, 
						{0, this.pdb.getHorizon()}
					});
			Assert.assertNotNull(constraint);
		
			// add rule
			this.pdb.addSynchronizationRule(rule);
		
			// print PDB
			System.out.println(this.pdb);
			
			// print pending decisions
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
		
			// add pending decision
			Decision dec1 = ((PlanDataBaseComponent) this.pdb).createDecision(v13, new String[] {});
			// add decisions
			Decision dec2 = ((PlanDataBaseComponent) this.pdb).createDecision(v21, new String[] {});
			((PlanDataBaseComponent) this.pdb).addDecision(dec2);
			Decision dec3 = ((PlanDataBaseComponent) this.pdb).createDecision(v22, new String[] {});
			((PlanDataBaseComponent) this.pdb).addDecision(dec3);
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(!flaws.isEmpty());
			for (Flaw flaw : flaws) {
				// check flaw
				if (flaw instanceof Goal) {
					Goal goal = (Goal) flaw;
					Assert.assertTrue(goal.getDecision().equals(dec1));
					System.out.println(goal);
				}
				if (flaw instanceof Peak) {
					Peak peak = (Peak) flaw;
					Assert.assertTrue(peak.size() == 2);
					Assert.assertTrue(peak.getDecisions().contains(dec2));
					Assert.assertTrue(peak.getDecisions().contains(dec3));
					Assert.assertFalse(peak.getDecisions().contains(dec1));
					System.out.println(peak);
				}
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
	public void displayPlanDataBaseTest() {
		System.out.println("[Test]: displayPlanDataBaseTest() --------------------");
		System.out.println();
		try {
			
			// check PDB
			Assert.assertNotNull(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c1);
			// add values
			ComponentValue v11 = c1.addValue("Val11", true);
			Assert.assertNotNull(v11);
			ComponentValue v12 = c1.addValue("Val12", false);
			Assert.assertNotNull(v12);
			ComponentValue v13 = c1.addValue("Val13", true);
			Assert.assertNotNull(v13);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			Assert.assertNotNull(c2);
			// add values
			ComponentValue v21 = c2.addValue("Val21", true);
			Assert.assertNotNull(v21);
			ComponentValue v22 = c2.addValue("Val22", true);
			Assert.assertNotNull(v22);
			ComponentValue v23 = c2.addValue("Val23", true);
			Assert.assertNotNull(v23);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			List<ComponentValue> values = ((PlanDataBaseComponent) this.pdb).getValues();
			Assert.assertNotNull(values);
			Assert.assertFalse(values.isEmpty());
			Assert.assertTrue(values.size() == 6);
			
			List<Decision> decs = ((PlanDataBaseComponent) this.pdb).getActiveDecisions();
			Assert.assertNotNull(decs);
			Assert.assertTrue(decs.isEmpty());
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(v13, new String[] {});
			Assert.assertNotNull(rule);
			// add constraint
			TokenVariable cd0 = rule.addTokenVariable(v23, new String[] {});
			Assert.assertNotNull(cd0);
			SynchronizationConstraint constraint = rule.addTemporalConstraint(
					rule.getTriggerer(), 
					cd0, 
					RelationType.DURING, 
					new long[][] {
						{0, this.pdb.getHorizon()}, {0, this.pdb.getHorizon()}
					});
			Assert.assertNotNull(constraint);
			// add rule
			this.pdb.addSynchronizationRule(rule);
			
			// print PDB
			System.out.println(this.pdb);
			
			// print pending decisions
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getPendingDecisions().isEmpty());
			Assert.assertTrue(((PlanDataBaseComponent) this.pdb).getActiveDecisions().isEmpty());
		
		
			Decision dec1 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v13, new String[] {}, 
							new long[] {11, 23}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).addDecision(dec1);

			Decision dec2 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v21, new String[] {}, 
							new long[] {5, 5}, v21.getDurationBounds());
			((PlanDataBaseComponent) this.pdb).addDecision(dec2);
			
			Decision dec3 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v22, new String[] {}, 
							new long[] {11, 11}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).addDecision(dec3);
			
			Decision dec4 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v12, new String[] {}, 
							new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).addDecision(dec4);
			
			Decision dec5 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v12, new String[] {}, 
							new long[] {15, 18}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).addDecision(dec5);
			
			Decision dec6 = ((PlanDataBaseComponent) this.pdb).
					createDecision(v22, new String[] {}, 
							new long[] {23, 23}, new long[] {5, 5});
			((PlanDataBaseComponent) this.pdb).addDecision(dec6);
			
			// display plan data-base
			this.pdb.display();
			Thread.sleep(5000);
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
	public void detectUnificationTest() {
		System.out.println("[Test]: detectUnificationTest() --------------------");
		System.out.println();
		try {
			// check PDB
			Assert.assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			ComponentValue v11 = c1.addValue("Val11", false);
			ComponentValue v12 = c1.addValue("Val12", true);
			ComponentValue v13 = c1.addValue("Val13", true);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			ComponentValue v21 = c2.addValue("Val21", true);
			ComponentValue v22 = c2.addValue("Val22", false);
			ComponentValue v23 = c2.addValue("Val23", true);
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
			Decision fact = this.pdb.createDecision(v22, new String[] {}, new long[] {0, 0}, new long[] {10, 10}, new long[] {10, 10});
			this.pdb.addDecision(fact);
			fact = this.pdb.createDecision(v22, new String[] {}, new long[] {22, 22}, new long[] {25, 25}, new long[] {3, 3});
			this.pdb.addDecision(fact);
			fact = this.pdb.createDecision(v13, new String[] {});
			this.pdb.addDecision(fact);
			
			// add pending decision
			Decision g1 = this.pdb.createDecision(v22, new String[] {});
			
			// print data
			System.out.println("PDB Agenda:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getPendingDecisions());
			System.out.println("PDB Active Decisions:");
			System.out.println(((PlanDataBaseComponent) this.pdb).getActiveDecisions());
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.size() == 2);
			

			System.out.println("Detected flaws");
			System.out.println(flaws);
			
			// get a flaw
			Flaw flaw = flaws.get(0);
			System.out.println("Flaw solutions");
			System.out.println(flaw.getSolutions());
			
			// check solutions
			Assert.assertFalse(flaw.getSolutions().isEmpty());
			Assert.assertTrue(flaw.getSolutions().size() == 3);
			
			for (FlawSolution sol : flaw.getSolutions()) {
				if(sol instanceof GoalUnification) {
					GoalUnification unif = (GoalUnification) sol;
					Assert.assertTrue(unif.getGoalDecision().equals(g1));
				}
			}
			
			this.pdb.display();
			Thread.sleep(3000);
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
	public void applyAndRetractExpansionOperator() {
		System.out.println("[Test]: applyAndRetractExpansionOperator() --------------------");
		System.out.println();
		try {
			// check PDB
			Assert.assertNotNull(this.pdb);
			System.out.println(this.pdb);
			
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMTIVE);
			ComponentValue v11 = c1.addValue("Val11", false);
			ComponentValue v12 = c1.addValue("Val12", true);
			ComponentValue v13 = c1.addValue("Val13", true);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMTIVE);
			ComponentValue v21 = c2.addValue("Val21", true);
			ComponentValue v22 = c2.addValue("Val22", false);
			ComponentValue v23 = c2.addValue("Val23", true);
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
			Decision f0 = this.pdb.createDecision(v11, new String[] {});
			this.pdb.addDecision(f0);
			
			Decision f1 = this.pdb.createDecision(
					v21,
					new String[] {},
					new long[] {22, 22}, 
					new long[] {25, 25}, 
					v21.getDurationBounds());
			this.pdb.addDecision(f1);
			
			// add goal
			Decision g1 = this.pdb.createDecision(v13, new String[] {});
			g1.setMandatoryExpansion();
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.size() == 1);
			
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 2);
			Assert.assertTrue(this.pdb.getPendingDecisions().contains(g1));
			Assert.assertFalse(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// get flaw solution
			GoalExpansion exp = (GoalExpansion) flaws.get(0).getSolutions().get(0);
			System.out.println(exp);
			
			// apply solution
			this.pdb.commit(exp);
			
			// check data
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 3);
			Assert.assertTrue(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// retract solution
			this.pdb.rollback(exp);
			
			// check data
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 2);
			Assert.assertFalse(this.pdb.getActiveDecisions().contains(g1));
			Assert.assertTrue(this.pdb.getPendingDecisions().contains(g1));
			System.out.println(g1);
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
	public void applyAndRetractExpansionWithParameterConstraintTest() {
		System.out.println("[Test]: applyAndRetractExpansionWithParameterConstraintTest() --------------------");
		System.out.println();
		try {
			// check PDB
			Assert.assertNotNull(this.pdb);
			
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
			PrimitiveStateVariable robotAction = this.pdb.createDomainComponent("RobotAction", DomainComponentType.SV_PRIMTIVE);
			ComponentValue idle = robotAction.addValue("Idle", true);
			ComponentValue go = robotAction.addValue("Go", true);
			go.addParameterPlaceHolder(ParameterType.ENUMERATION_PARAMETER_TYPE, positions);
			
			// add transitions
			robotAction.addValueTransition(idle, go);
			robotAction.addValueTransition(go, idle);
			// add component
			this.pdb.addDomainComponent(robotAction);		
			
			// create state variables
			PrimitiveStateVariable robotPosition = this.pdb.createDomainComponent("RobotPosition", DomainComponentType.SV_PRIMTIVE);
			ComponentValue at = robotPosition.addValue("At", true);
			at.addParameterPlaceHolder(ParameterType.ENUMERATION_PARAMETER_TYPE, positions);
			
			ComponentValue moving = robotPosition.addValue("Moving", new long[] {5, 11}, false);
			moving.addParameterPlaceHolder(ParameterType.ENUMERATION_PARAMETER_TYPE, positions);
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
			Decision f0 = this.pdb.createDecision(idle, new String[] {});
			this.pdb.addDecision(f0);
			
			// add goal
			Decision g1 = this.pdb.createDecision(go, new String[] {"?destination"});
			g1.setMandatoryExpansion();
			
			// check flaws
			List<Flaw> flaws = this.pdb.detectFlaws();
			Assert.assertNotNull(flaws);
			Assert.assertTrue(flaws.size() == 1);
			
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 1);
			Assert.assertTrue(this.pdb.getPendingDecisions().contains(g1));
			Assert.assertFalse(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			// get flaw solution
			GoalExpansion exp = (GoalExpansion) flaws.get(0).getSolutions().get(0);
			System.out.println(exp);
			
			// apply solution
			this.pdb.commit(exp);
			this.pdb.check();
			
			// check data
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 2);
			Assert.assertTrue(this.pdb.getActiveDecisions().contains(g1));
			System.out.println(g1);
			
			
			// print current plan
			Plan plan = this.pdb.getPlan();
			System.out.println();
			System.out.println(plan);
			System.out.println();
			
			// retract solution
			this.pdb.rollback(exp);
			this.pdb.check();
			
			// check data
			Assert.assertNotNull(this.pdb.getPendingDecisions());
			Assert.assertTrue(this.pdb.getPendingDecisions().size() == 1);
			Assert.assertNotNull(this.pdb.getActiveDecisions());
			Assert.assertTrue(this.pdb.getActiveDecisions().size() == 1);
			Assert.assertFalse(this.pdb.getActiveDecisions().contains(g1));
			Assert.assertTrue(this.pdb.getPendingDecisions().contains(g1));
			System.out.println(g1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
