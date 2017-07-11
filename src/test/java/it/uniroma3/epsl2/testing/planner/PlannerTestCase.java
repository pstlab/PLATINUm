package it.uniroma3.epsl2.testing.planner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.deliberative.app.DefaultPlanner;
import it.uniroma3.epsl2.deliberative.app.Planner;
import it.uniroma3.epsl2.deliberative.app.PlannerBuilder;
import it.uniroma3.epsl2.deliberative.app.PlannerFactory;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.PlanDataBaseBuilder;
import it.uniroma3.epsl2.framework.domain.PlanDataBaseFactory;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.domain.component.pdb.TokenVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.ExternalStateVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.PrimitiveStateVariable;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.lang.problem.Problem;
import it.uniroma3.epsl2.framework.microkernel.lang.problem.ProblemFact;
import it.uniroma3.epsl2.framework.microkernel.lang.problem.ProblemFluent;
import it.uniroma3.epsl2.framework.microkernel.lang.problem.ProblemGoal;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public class PlannerTestCase 
{
	private PlannerFactory plannerFactory;
	private PlanDataBase pdb;
	
	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("****************************** Planner Test Case ***************************");
		System.out.println("**********************************************************************************");
		
		// set factory
		this.plannerFactory = PlannerFactory.getInstance();
		
		// create Plan Data Base
		PlanDataBaseFactory factory = PlanDataBaseFactory.getInstance();
		this.pdb = factory.create("PDB", 0, 100);
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
	public void createPlannerTest() {
		System.out.println("[Test]: createPlannerTest() --------------------");
		System.out.println();
		
		// create Planner
		DefaultPlanner planner = this.plannerFactory.create(DefaultPlanner.class.getName());
		Assert.assertNotNull(planner);
	}
	
	/**
	 * 
	 */
	@Test
	public void simpleSolvingTest() {
		System.out.println("[Test]: simpleSolvingTest() --------------------");
		System.out.println();
		try 
		{
			// create state variable 
			PrimitiveStateVariable c1 = this.pdb.createDomainComponent("C1", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue v11 = c1.addValue("Val11", new long[] {3, this.pdb.getHorizon()}, true);
			ComponentValue v12 = c1.addValue("Val12", new long[] {3, 11}, false);
			ComponentValue v13 = c1.addValue("Val13", new long[] {7, 7}, true);
			// add transitions
			c1.addValueTransition(v11, v12);
			c1.addValueTransition(v12, v11);
			c1.addValueTransition(v12, v13);
			c1.addValueTransition(v13, v12);
			// add component
			this.pdb.addDomainComponent(c1);		
			
			// create state variables
			PrimitiveStateVariable c2 = this.pdb.createDomainComponent("C2", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue v21 = c2.addValue("Val21", new long[] {5, 45}, true);
			ComponentValue v22 = c2.addValue("Val22", new long[] {7, 11}, true);
			ComponentValue v23 = c2.addValue("Val23", new long[] {3, this.pdb.getHorizon()}, true);
			// add transitions
			c2.addValueTransition(v21, v22);
			c2.addValueTransition(v22, v23);
			c2.addValueTransition(v23, v21);
			// add component
			this.pdb.addDomainComponent(c2);		
			
			
			// create state variables
			ExternalStateVariable c3 = this.pdb.createDomainComponent("C3", DomainComponentType.SV_EXTERNAL);
			// add values
			ComponentValue v31 = c3.addValue("Val31", new long[] {1, 50}, true);
			ComponentValue v32 = c3.addValue("Val32", new long[] {1, 50}, false);
			ComponentValue v33 = c3.addValue("Val33", new long[] {1, 50}, true);
			// add transitions
			c3.addValueTransition(v31, v32);
			c3.addValueTransition(v32, v33);
			c3.addValueTransition(v33, v31);
			// add component
			this.pdb.addDomainComponent(c3);
	
			// create Problem
			Problem problem = new Problem();
			
			// add fact
			problem.addFact(v11, new String[] {},
					new long[] {0, 0}, 
					new long[] {5, this.pdb.getHorizon()});
			
			// add fact
			problem.addFact(v21, new String[] {},
					new long[] {0, 0}, 
					new long[] {5, this.pdb.getHorizon()});
			
			// add fact
			problem.addFact(v31, new String[] {},
					new long[] {0, 0}, 
					new long[] {20, 20});
			
			problem.addFact(v32, new String[] {},
					new long[] {20, 20}, 
					new long[] {45, 45});
			
			problem.addFact(v33, new String[] {},
					new long[] {45, 45}, 
					new long[] {50, 50});
			
			
			// add goal
			problem.addGoal(v13, new String[] {},
					new long[] {11, this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			// add goal
			problem.addGoal(v23, new String[] {},
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			// set problem
			this.pdb.setup(problem);
			
			// create planner
			DefaultPlanner planner = this.plannerFactory.create(DefaultPlanner.class.getName());
			// try to solve the plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			
			// display after
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
	public void simpleSolvingWithSynchronizationTest() {
		System.out.println("[Test]: simpleSolvingWithSynchronizationTest() --------------------");
		System.out.println();
		try 
		{
			// create parameter domain
			EnumerationParameterDomain locations = this.pdb.createParameterDomain("locations", ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			locations.setValues(new String[] {
					"kitchen",
					"bedroom",
					"restroom",
					"garden"
			});
			
			// create state variable 
			PrimitiveStateVariable robot = this.pdb.createDomainComponent("Robot", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue idle = robot.addValue("Idle", new long[] {1, this.pdb.getHorizon()}, true);
			ComponentValue goTo = robot.addValue("GoTo", new long[] {1, this.pdb.getHorizon()}, true);
			goTo.addParameterPlaceHolder(locations);
			// add transitions
			robot.addValueTransition(idle, goTo);
			robot.addValueTransition(goTo, idle);
			// add component
			this.pdb.addDomainComponent(robot);		
			
			// create state variables
			PrimitiveStateVariable robotBase = this.pdb.createDomainComponent("RobotBase", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue at = robotBase.addValue("At", new long[] {5, this.pdb.getHorizon()}, true);
			at.addParameterPlaceHolder(locations);
			ComponentValue moving = robotBase.addValue("Moving", new long[] {11, 16}, false);
			// add transitions
			robotBase.addValueTransition(at, moving);
			robotBase.addValueTransition(moving, at);
			// add component
			this.pdb.addDomainComponent(robotBase);		
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(goTo, new String[] {"?destination"});
			TokenVariable trigger = rule.getTriggerer();
			TokenVariable v1 = rule.addTokenVariable(at, new String[] {"?position"});
			rule.addTemporalConstraint(
					trigger, 
					v1, 
					RelationType.MEETS, 
					new long[][] {});
			rule.addParameterConstraint(trigger, v1, RelationType.EQUAL_PARAMETER, "?destination", "?position");
			
			TokenVariable v2 = rule.addTokenVariable(at, new String[] {"?position"});
			rule.addTemporalConstraint(
					trigger, 
					v2, 
					RelationType.MET_BY, 
					new long[][] {});
			rule.addParameterConstraint(trigger, v2, RelationType.NOT_EQUAL_PARAMETER, "?destination", "?position");
			
			
			// add synchronization
			this.pdb.addSynchronizationRule(rule);
	
			// create Problem
			Problem problem = new Problem();
			
			// add fact
			ProblemFact f0 = problem.addFact(at, new String[] {"?at"}, 
					new long[] {0, 0}, 
					new long[] {1, this.pdb.getHorizon()});
			// set initial position
			problem.addBindingParameterConstraint(f0, "?at", "kitchen");
			
			
			// add goal
			ProblemGoal goal = problem.addGoal(goTo, new String[] {"?to"},
					// not pseudo-controllable if new long[] {11, H}
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()},	 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			problem.addBindingParameterConstraint(goal, "?to", "garden");
			
			// set problem
			this.pdb.setup(problem);
			
			// create planner
			DefaultPlanner planner = this.plannerFactory.create(DefaultPlanner.class.getName());
			// get initial plan
			System.out.println(planner.getCurrentPlan());
			// try to solve the plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			
			planner.display();
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
	public void satelliteDomainTest() {
		System.out.println("[Test]: satelliteDomainTest() --------------------");
		System.out.println();
		try 
		{
			// create state variable 
			PrimitiveStateVariable satellite = this.pdb.createDomainComponent("Satellite", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue earth = satellite.addValue("Earth", new long[] {1, this.pdb.getHorizon()}, true);
			ComponentValue slewing = satellite.addValue("Slewing", new long[] {10, 15}, true);
			ComponentValue comm = satellite.addValue("Comm", new long[] {15, 20}, false);
			ComponentValue science = satellite.addValue("Science", new long[] {16, 23}, true);
			// add transitions
			satellite.addValueTransition(earth, slewing);
			satellite.addValueTransition(earth, comm);
			satellite.addValueTransition(slewing, earth);
			satellite.addValueTransition(slewing, science);
			satellite.addValueTransition(science, slewing);
			satellite.addValueTransition(comm, earth);
			// add component
			this.pdb.addDomainComponent(satellite);		
			
			// create state variables
			ExternalStateVariable window = this.pdb.createDomainComponent("Window", DomainComponentType.SV_EXTERNAL);
			// add values
			ComponentValue visible = window.addValue("Visible", new long[] {80, 80}, false);
			ComponentValue notVisible = window.addValue("notVisible", new long[] {10, 10}, false);
			// add transitions
			window.addValueTransition(visible, notVisible);
			window.addValueTransition(notVisible, visible);
			// add component
			this.pdb.addDomainComponent(window);		
			
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(science, new String[] {});
			TokenVariable trigger = rule.getTriggerer();
			TokenVariable v1 = rule.addTokenVariable(comm, new String[] {});
			TokenVariable v2 = rule.addTokenVariable(visible, new String[] {});
			
			// add synchronization constraint
			rule.addTemporalConstraint(
					trigger, 
					v1, 
					RelationType.BEFORE,
					new long[][] {
						{0, this.pdb.getHorizon()}
					});
			
			// add synchronization constraint
			rule.addTemporalConstraint(
					v1, 
					v2, 
					RelationType.DURING, 
					new long[][] {
						{this.pdb.getOrigin(), this.pdb.getHorizon()},
						{this.pdb.getOrigin(), this.pdb.getHorizon()}
					});
			
			// add synchronization
			this.pdb.addSynchronizationRule(rule);
	
			// create Problem
			Problem problem = new Problem();
			
			// add fact
			problem.addFact(earth, new String[] {},
					new long[] {0, 0}, 
					new long[] {0, this.pdb.getHorizon()});
			
			// add observations
			problem.addObservation(visible, 
					new String[] {},
					new long[] {0, 10}, 
					new long[] {15, 20}, 
					new long[] {5, 20});
			
			problem.addObservation(notVisible,
					new String[] {},
					new long[] {15, 20},	 
					new long[] {45, 50}, 
					new long[] {25, 30});
			
			problem.addObservation(visible,
					new String[] {},
					new long[] {45, 50}, 
					new long[] {100, 100}, 
					new long[] {50, 55});
			
			// add goal
			ProblemGoal g1 = problem.addGoal(science,
					new String[] {},
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			// add goal
			ProblemGoal g2 = problem.addGoal(science,
					new String[] {},
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			// add precedence constraint
			problem.addTemporalConstraint(RelationType.BEFORE, 
					g1, 
					g2, 
					new long[][] {{
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					}});
			
			// set problem
			this.pdb.setup(problem);
			
			// create planner
			DefaultPlanner planner = this.plannerFactory.create(DefaultPlanner.class.getName());
			// try to solve the plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			
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
	public void roverDomainTest() {
		System.out.println("[Test]: roverDomainTest() --------------------");
		System.out.println();
		try 
		{
			// create parameter domains
			EnumerationParameterDomain targets = this.pdb.createParameterDomain("targets", ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			targets.setValues(new String[] {"rock1", "rock2", "rock3"});
			EnumerationParameterDomain locations = this.pdb.createParameterDomain("locations", ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
			locations.setValues(new String[] {"location1", "location2", "location3"});
			
			// create state variable 
			PrimitiveStateVariable rover = this.pdb.createDomainComponent("Rover", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue idle = rover.addValue("Idle", new long[] {1, this.pdb.getHorizon()}, true);
			ComponentValue take = rover.addValue("TakePicture", new long[] {1, this.pdb.getHorizon()}, true);
			take.addParameterPlaceHolder(targets);
			take.addParameterPlaceHolder(locations);
			// add transitions
			rover.addValueTransition(idle, take);
			rover.addValueTransition(take, idle);
			// add component
			this.pdb.addDomainComponent(rover);		
			
			// create state variables
			PrimitiveStateVariable base = this.pdb.createDomainComponent("RobotBase", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue at = base.addValue("At", new long[] {1, this.pdb.getHorizon()}, true);
			at.addParameterPlaceHolder(locations);
			ComponentValue moving = base.addValue("Moving", new long[] {10, 15}, false);
			// add transitions
			base.addValueTransition(at, moving);
			base.addValueTransition(moving, at);
			// add component
			this.pdb.addDomainComponent(base);		
			
			// create state variables
			PrimitiveStateVariable camera = this.pdb.createDomainComponent("RobotCamera", DomainComponentType.SV_PRIMITIVE);
			// add values
			ComponentValue takingPicture = camera.addValue("TakingPicture", new long[] {7, 11}, false);
			takingPicture.addParameterPlaceHolder(targets);
			// add component
			this.pdb.addDomainComponent(camera);
			
			// create synchronization
			SynchronizationRule rule = this.pdb.createSynchronizationRule(take, new String[] {"?target", "?position"});
			TokenVariable trigger = rule.getTriggerer();
			TokenVariable v1 = rule.addTokenVariable(at, new String[] {"?location1"});
			TokenVariable v2 = rule.addTokenVariable(takingPicture, new String[] {"?object2"});
			TokenVariable v3 = rule.addTokenVariable(at, new String[] {"?location3"});
			
			// add synchronization constraint
			rule.addTemporalConstraint(
					trigger, 
					v1, 
					RelationType.DURING,
					new long[][] {
						{0, this.pdb.getHorizon()},
						{0, this.pdb.getHorizon()}
					});
			
			// add synchronization constraint
			rule.addTemporalConstraint(
					trigger, 
					v2, 
					RelationType.CONTAINS, 
					new long[][] {
						{0, this.pdb.getHorizon()},
						{0, this.pdb.getHorizon()}
					});
			
			// add synchronization constraint
			rule.addTemporalConstraint(
					trigger, 
					v3, 
					RelationType.AFTER, 
					new long[][] {
						{0, this.pdb.getHorizon()}
					});
			
			// add parameter constraints
			rule.addParameterConstraint(trigger, v1, RelationType.EQUAL_PARAMETER, "?position", "?location1");
			rule.addParameterConstraint(trigger, v2, RelationType.EQUAL_PARAMETER, "?target", "?object2");
			rule.addParameterConstraint(v3, v1, RelationType.NOT_EQUAL_PARAMETER, "?location3", "?location1");
			
			// add synchronization
			this.pdb.addSynchronizationRule(rule);
	
			// create Problem
			Problem problem = new Problem();
			
			// add fact
			problem.addFact(idle, new String[] {},
					new long[] {0, 0}, 
					new long[] {0, this.pdb.getHorizon()});
			
			ProblemFluent f1 = problem.addFact(at,
					new String[] {"?location"},
					new long[] {0, 0}, 
					new long[] {0, this.pdb.getHorizon()});
			
			
			// add goal
			ProblemGoal g1 = problem.addGoal(take,
					new String[] {"?target", "?location"},
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()}, 
					new long[] {this.pdb.getOrigin(), this.pdb.getHorizon()});
			
			// add parameter constraint
			problem.addBindingParameterConstraint(f1, "?location", "location1");
			problem.addBindingParameterConstraint(g1, "?target", "rock2");
			problem.addBindingParameterConstraint(g1, "?location", "location3");
			
			
			this.pdb.setup(problem);
			
			// create planner
			DefaultPlanner planner = this.plannerFactory.create(DefaultPlanner.class.getName());
			// try to solve the plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			
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
	public void buildPlannerOnFourByThreeDomainTest() { 
		System.out.println("[Test]: buildPlannerOnFourByThreeDomainTest() --------------------");
		System.out.println();
		try 
		{
			final String DDL = "domains/fourbythree/fourbythree.ddl";
			final String PDL = "domains/fourbythree/fourbythree.pdl";
			// compile the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.build(DDL, PDL);
			// create planner by means of factory
			DefaultPlanner planner = PlannerFactory.getInstance().create(DefaultPlanner.class.getName());
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			// display plan data-base
			pdb.display();
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
	public void buildPlannerOnSatelliteDomainTest() { 
		System.out.println("[Test]: buildPlannerOnSatelliteDomainTest() --------------------");
		System.out.println();
		try 
		{	
			// domain file
			final String DDL = "domains/satellite/satellite.ddl";
			final String PDL = "domains/satellite/satellite.pdl";
			// compile the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.build(DDL, PDL);
			
			// create a planner instance
			DefaultPlanner planner = PlannerFactory.getInstance().create(DefaultPlanner.class.getName());
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			
			// display plan
			pdb.display();
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
	public void buildPlannerOnGeckoDomainTest() { 
		System.out.println("[Test]: buildPlannerOnGeckoDomainTest() --------------------");
		System.out.println();
		try 
		{	
			// domain file
			final String DDL = "domains/gecko/full.ddl";
			final String PDL = "domains/gecko/full.pdl";
			
			// build the planner
			Planner planner = PlannerBuilder.build(DDL, PDL);
			// get solution plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			// export plan
			System.out.println();
			System.out.println(plan.export());
			System.out.println();
			// display plan
			planner.display();
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
	public void buildPlannerOnNeptusDomainTest() { 
		System.out.println("[Test]: buildPlannerOnNeptusDomainTest() --------------------");
		System.out.println();
		try 
		{	
			// domain file
			final String DDL = "domains/neptus/neptus-1auv-3phen.ddl";
			final String PDL = "domains/neptus/neptus-1auv-3phen-1task.pdl";
			
			// build the planner
			Planner planner = PlannerBuilder.build(DDL, PDL);
			// get solution plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			// export plan
			System.out.println();
			System.out.println(plan.export());
			System.out.println();
			// display plan
			planner.display();
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
	public void buildPlannerOnGoacDomainTest() { 
		System.out.println("[Test]: buildPlannerOnGoacDomainTest() --------------------");
		System.out.println();
		try 
		{	
			// domain file
			final String DDL = "domains/goac/goac.ddl";
			final String PDL = "domains/goac/goac-2g-1wind.pdl";
			
			// build the planner
			Planner planner = PlannerBuilder.build(DDL, PDL);
			// get solution plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			// export plan
			System.out.println();
			System.out.println(plan.export());
			System.out.println();
			// display plan
			planner.display();
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
	public void buildPlannerOnICODomainTest() { 
		System.out.println("[Test]: buildPlannerOnICODomainTest() --------------------");
		System.out.println();
		try 
		{	
			// domain file
			final String DDL = "domains/ico/ico_1guest_1mug.ddl";
			final String PDL = "domains/ico/ico_1guest_1mug_1task.pdl";
			
			// build the planner
			Planner planner = PlannerBuilder.build(DDL, PDL);
			// get solution plan
			SolutionPlan plan = planner.plan();
			Assert.assertNotNull(plan);
			System.out.println(plan);
			// export plan
			System.out.println();
			System.out.println(plan.export());
			System.out.println();
			// display plan
			planner.display();
			Thread.sleep(50000);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
