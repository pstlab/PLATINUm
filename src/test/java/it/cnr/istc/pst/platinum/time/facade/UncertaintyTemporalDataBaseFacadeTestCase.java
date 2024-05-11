package it.cnr.istc.pst.platinum.time.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryFactory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.PseudoControllabilityException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.BeforeIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.DuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.MeetsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author alessandro
 *
 */
@TemporalFacadeConfiguration(
		network = TemporalNetworkType.STNU,
		solver = TemporalSolverType.APSP
)
public class UncertaintyTemporalDataBaseFacadeTestCase 
{
	private static final int ORIGIN = 0;
	private static final int HORIZON = 500;
	private TemporalFacade facade;
	private TemporalConstraintFactory intervalFactory;
	private TemporalQueryFactory queryFactory;
	
	/**
	 * 
	 */
	@Before
	public void setupTest() {
		System.out.println("**********************************************************************************");
		System.out.println("********************** Uncertainty Temporal Facade Test Case *********************");
		System.out.println("**********************************************************************************");
		
		// create temporal network
		this.facade = TemporalFacadeBuilder.createAndSet(this, ORIGIN, HORIZON);
		
		// get interval factory
		this.intervalFactory = new TemporalConstraintFactory();
		// get query factory
		this.queryFactory = new TemporalQueryFactory();
	}
	
	/**
	 * 
	 */
	@After
	public void setDownTest() {
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
			this.facade.verify();
			System.out.println(this.facade);
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
	public void createTemporalIntervals() {
		System.out.println("[Test]: createTemporalIntervals() --------------------");
		System.out.println();
		try {
			// create flexible temporal interval
			TemporalInterval i1 = this.facade.createTemporalInterval(true);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 1);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == HORIZON);
			// print interval description
			System.out.println(i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = facade.createTemporalInterval(new long[] {10, 20}, true);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 20);
			// print interval description
			System.out.println(i2);
			
			Assert.assertFalse(i1.equals(i2));
			
			// print temporal network information
			System.out.println(this.facade);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createContingentTemporalIntervals() {
		System.out.println("[Test]: createContingentTemporalIntervals() --------------------");
		System.out.println();
		try {
			// create flexible temporal interval
			TemporalInterval i1 = this.facade.createTemporalInterval(new long[] {1, HORIZON}, false);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 1);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == HORIZON);
			// print interval description
			System.out.println(i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = this.facade.createTemporalInterval(new long[] {10, 20}, false);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 20);
			// print interval description
			System.out.println(i2);
			
			Assert.assertFalse(i1.equals(i2));
			this.facade.verify();
			// print temporal network information
			System.out.println(this.facade);
		}
		catch (ConsistencyCheckException ex) {
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
	public void createTemporalIntervalsAndCheckPseudoControllability1() {
		System.out.println("[Test]: createTemporalIntervalsAndCheckPseudoControllability1() --------------------");
		System.out.println();
		TemporalInterval i1 = null;
		try 
		{
			// create flexible temporal interval
			i1 = this.facade.createTemporalInterval(new long[] {10, 50}, false);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == 50);
			// print interval description
			System.out.println("Added Contingent Interval: " + i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = this.facade.createTemporalInterval(new long[] {10, 30}, true);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 30);
			// print interval description
			System.out.println("Added Interval: " + i2);
			Assert.assertFalse(i1.equals(i2));
			
			// create and propagate temporal constraint
			DuringIntervalConstraint constraint = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			constraint.setReference(i1);
			constraint.setTarget(i2);
			// propagate constraint
			this.facade.propagate(constraint);
			
			// print data
			this.facade.printDiagnosticData();
			
			// check consistency
			this.facade.verify();
		}
		catch (PseudoControllabilityException ex) {
			// the network is not pseudo-controllable
			System.out.println(ex.getMessage());
			// get actual duration of the contingent interval
			IntervalScheduleQuery query = this.queryFactory.
					create(TemporalQueryType.INTERVAL_SCHEDULE);
			// set interval
			query.setInterval(i1);
			this.facade.process(query);
			System.out.println("Checking actual duration of ContingetInterval: " + i1);
			System.out.println("---> dmin= " + i1.getDurationLowerBound() + ", dmax= " + i1.getDurationUpperBound());
			System.out.println();
			// print temporal network information
			System.out.println(this.facade);
		}
		catch (Exception exx) {
			System.err.println(exx.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createTemporalIntervalsAndCheckPseudoControllability2() {
		System.out.println("[Test]: createTemporalIntervalsAndCheckPseudoControllability2() --------------------");
		System.out.println();
		try {
			// create flexible temporal interval
			TemporalInterval i1 = this.facade.createTemporalInterval(new long[] {10, 50}, true);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == 50);
			// print interval description
			System.out.println("Added Interval: " + i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = this.facade.createTemporalInterval(new long[] {10, 30}, false);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 30);
			// print interval description
			System.out.println("Added Contingent Interval: " + i2);
			Assert.assertFalse(i1.equals(i2));
			
			// create and propagate temporal constraint
			DuringIntervalConstraint constraint = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			constraint.setReference(i1);
			constraint.setTarget(i2);
			// propagate constraint
			this.facade.propagate(constraint);

			// check consistency
			this.facade.verify();
			
			// get actual duration of the contingent interval
			IntervalScheduleQuery query = this.queryFactory.
					create(TemporalQueryType.INTERVAL_SCHEDULE);
			// set interval
			query.setInterval(i2);
			this.facade.process(query);
			System.out.println("Checking actual duration of ContingetInterval: " + i2);
			System.out.println("---> dmin= " + i2.getDurationLowerBound() + ", dmax= " + i2.getDurationUpperBound());
			System.out.println();
			
			// get actual duration of the requirement interval
			query.setInterval(i1);
			this.facade.process(query);
			System.out.println("Checking actual duration of ContingetInterval: " + i1);
			System.out.println("---> dmin= " + i1.getDurationLowerBound() + ", dmax= " + i1.getDurationUpperBound());
			System.out.println();
			
			// print temporal network information
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
	public void createAndDeleteTemporalIntervals() {
		System.out.println("[Test]: createAndDeleteTemporalIntervals() --------------------");
		System.out.println();
		try {
			// create temporal intervals
			TemporalInterval i1 = facade.createTemporalInterval(true);
			Assert.assertNotNull(i1);

			// check consistency
			this.facade.verify();
			// print network
			System.out.println(this.facade);
			
			// delete interval
			facade.deleteTemporalInterval(i1);
			// check consistency
			this.facade.verify();
			// print temporal network
			System.out.println(this.facade);
			
			// create another temporal interval
			TemporalInterval i2 = facade.createTemporalInterval(true);
			Assert.assertNotNull(i2);
			
			// check consistency
			this.facade.verify();
			// print network
			System.out.println(this.facade);
			
			// delete interval
			facade.deleteTemporalInterval(i2);
			// check consistency
			this.facade.verify();
			// print temporal network
			System.out.println(this.facade);
			
			// create other intervals
			facade.createTemporalInterval(true);
			facade.createTemporalInterval(true);
			facade.createTemporalInterval(true);
			
			// check consistency
			this.facade.verify();
			// print network
			System.out.println(this.facade);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void createTemporalIntervalsWithConstraints() {
		System.out.println("[Test]: createTemporalIntervalsWithPrecedenceConstraints() --------------------");
		System.out.println();
		try {
			// create flexible temporal interval
			TemporalInterval i1 = facade.createTemporalInterval(true);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 1);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == HORIZON);
			// print interval description
			System.out.println(i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = facade.createTemporalInterval(new long[] {10, 20}, true);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 20);
			// print interval description
			System.out.println(i2);
			
			// create temporal constraint
			BeforeIntervalConstraint before = this.intervalFactory.
					create(TemporalConstraintType.BEFORE);
			before.setReference(i1);
			before.setTarget(i2);
			// set bounds
			before.setLowerBound(10);
			before.setUpperBound(50);
			// propagate constraint
			facade.propagate(before);
			
			// check consistency
			this.facade.verify();
			// print temporal network information
			System.out.println(this.facade);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * 
	 */
	@Test
	public void createTemporalIntervalsWithInconsistentConstraints() {
		System.out.println("[Test]: createTemporalIntervalsWithInconsistentConstraints() --------------------");
		System.out.println();
		try {
			
			// create flexible temporal interval
			TemporalInterval i1 = this.facade.createTemporalInterval(true);
			Assert.assertNotNull(i1);
			Assert.assertNotNull(i1.getStartTime());
			Assert.assertNotNull(i1.getEndTime());
			Assert.assertTrue(i1.getNominalDurationLowerBound() == 1);
			Assert.assertTrue(i1.getNominalDurationUpperBound() == HORIZON);
			// print interval description
			System.out.println(i1);
			
			// create flexible temporal interval with bounds
			TemporalInterval i2 = this.facade.createTemporalInterval(new long[] {10, 20}, true);
			Assert.assertNotNull(i2);
			Assert.assertNotNull(i2.getStartTime());
			Assert.assertNotNull(i2.getEndTime());
			Assert.assertTrue(i2.getNominalDurationLowerBound() == 10);
			Assert.assertTrue(i2.getNominalDurationUpperBound() == 20);
			// print interval description
			System.out.println(i2);
			
			// create temporal constraint
			BeforeIntervalConstraint before1 = this.intervalFactory.
					create(TemporalConstraintType.BEFORE);
			before1.setReference(i2);
			before1.setTarget(i1);
			// set bounds
			before1.setLowerBound(10);
			before1.setUpperBound(50);
			
			// propagate constraint
			this.facade.propagate(before1);
			

			this.facade.printDiagnosticData();
			
			// check consistency
			this.facade.verify();

			// create (inconsistent) constraint
			BeforeIntervalConstraint before2 = this.intervalFactory.
					create(TemporalConstraintType.BEFORE);
			before2.setReference(i1);
			before2.setTarget(i2);
			try {

				// set bounds
				before2.setLowerBound(23);
				before2.setUpperBound(45);
				// propagate constraint
				this.facade.propagate(before2);
			
				this.facade.printDiagnosticData();
				
				// check consistency
				this.facade.verify();
			}
			catch (ConsistencyCheckException ex) {
				
				// inconsistency expected
				System.out.println(ex.getMessage());
				System.out.println(this.facade);
				System.out.println();
				System.out.println("Retract inconsistent temporal constraint");
				// remove inconsistent constraint
				this.facade.retract(before2);
				
				this.facade.printDiagnosticData();
				
				// check consistency
				this.facade.verify();
				
				// print temporal network information
				System.out.println(this.facade);
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
	public void createAndDeleteTemporalIntervalsWithConstraints() {
		System.out.println("[Test]: createAndDeleteTemporalIntervalsWithConstraints() --------------------");
		System.out.println();
		try {
			// create temporal intervals
			TemporalInterval i1 = facade.createTemporalInterval(new long[] {10, 10}, true);
			TemporalInterval i2 = facade.createTemporalInterval(new long[] {5, 8}, true);
			TemporalInterval i3 = facade.createTemporalInterval(new long[] {1, 70}, true);
			
			// meets constraint
			MeetsIntervalConstraint c1 = this.intervalFactory.
					create(TemporalConstraintType.MEETS);
			c1.setReference(i1);
			c1.setTarget(i2);
			// during constraint
			DuringIntervalConstraint c2 = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			c2.setReference(i2);
			c2.setTarget(i3);
			c2.setStartTimeBound(new long[] {1, 80});
			c2.setEndTimeBound(new long[] {1, 80});
			// contains constraint
			DuringIntervalConstraint c3 = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			c3.setReference(i1);
			c3.setTarget(i3);
			c3.setStartTimeBound(new long[] {1,10});
			c3.setEndTimeBound(new long[] {1, 10});
			
			// propagate constraints
			facade.propagate(c1);
			facade.propagate(c2);
			facade.propagate(c3);
			
			// check consistency
			this.facade.verify();
			// print network
			System.out.println(this.facade);
			
			// delete interval
			facade.deleteTemporalInterval(i2);
			System.out.println("Removing " + i2);
			// check consistency
			this.facade.verify();
			// print temporal network
			System.out.println(this.facade);
			
			// delete interval
			facade.deleteTemporalInterval(i1);
			System.out.println("Removing " + i1);
			// check consistency
			this.facade.verify();
			// print temporal network
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
	public void createIntervalsAndProcessQuery() {
		System.out.println("[Test]: createIntervalsAndProcessQuery() --------------------");
		System.out.println();
		try 
		{
			// create temporal intervals
			TemporalInterval i1 = facade.createTemporalInterval(new long[] {10, 10}, true);
			TemporalInterval i2 = facade.createTemporalInterval(new long[] {5, 8}, true);
			TemporalInterval i3 = facade.createTemporalInterval(new long[] {1, 70}, true);
			
			// meets constraint
			MeetsIntervalConstraint c1 = this.intervalFactory.
					create(TemporalConstraintType.MEETS);
			c1.setReference(i1);
			c1.setTarget(i2);
			// during constraint
			DuringIntervalConstraint c2 = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			c2.setReference(i2);
			c2.setTarget(i3);
			c2.setStartTimeBound(new long[] {1, 80});
			c2.setEndTimeBound(new long[] {1, 80});
			// contains constraint
			DuringIntervalConstraint c3 = this.intervalFactory.
					create(TemporalConstraintType.DURING);
			c3.setReference(i1);
			c3.setTarget(i3);
			c3.setStartTimeBound(new long[] {1,10});
			c3.setEndTimeBound(new long[] {1, 10});
			
			// propagate constraints
			this.facade.propagate(c1);
			// check consistency
			this.facade.verify();
			this.facade.propagate(c2);
			// check consistency
			this.facade.verify();
			this.facade.propagate(c3);
			// check consistency
			this.facade.verify();
			// print network
			System.out.println(this.facade);
			
			// make distance query
			IntervalDistanceQuery distanceQuery = this.queryFactory.create(TemporalQueryType.INTERVAL_DISTANCE);
			distanceQuery.setReference(i1);
			distanceQuery.setTarget(i2);
			// process query
			facade.process(distanceQuery);
			// check result
			Assert.assertTrue(distanceQuery.getDistanceLowerBound() == 0);
			Assert.assertTrue(distanceQuery.getDistanceUpperBound() == 0);
			
			// make duration query
			IntervalScheduleQuery query = this.queryFactory.
					create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(i3);
			// process query
			facade.process(query);
			// check actual interval duration after inference
			Assert.assertTrue(i3.getDurationLowerBound() == 17);
			Assert.assertNotNull(i3.getDurationUpperBound() == 70);
		
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}

}
