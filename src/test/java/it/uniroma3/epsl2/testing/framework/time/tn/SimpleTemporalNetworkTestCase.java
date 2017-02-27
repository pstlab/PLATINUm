package it.uniroma3.epsl2.testing.framework.time.tn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.IntervalDisjunctionException;
import it.uniroma3.epsl2.framework.time.tn.simple.SimpleTemporalNetwork;

/**
 * 
 * @author anacleto
 *
 */
public class SimpleTemporalNetworkTestCase
{
	private static final int ORIGIN = 0;
	private static final int HORIZON = 500;
	private SimpleTemporalNetwork stn;
	private TemporalConstraintFactory cf;
	
	/**
	 * 
	 */
	@Before
	public void setupTest() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* Simple Temporal Network Test Case **********************");
		System.out.println("**********************************************************************************");
		// create Simple Temporal Network
		TemporalNetworkFactory factory = new TemporalNetworkFactory();
		this.stn = factory.create(TemporalNetworkType.STN, ORIGIN, HORIZON);
		this.cf = TemporalConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@Test
	public void init() {
		System.out.println("[Test]: init() --------------------");
		// check network
		Assert.assertNotNull(this.stn);
		// print network
		System.out.println(this.stn);
		// check number of time points (only the origin expected)
		Assert.assertTrue(this.stn.size() == 2);
	}
	
	/**
	 * 
	 */
	@Test
	public void addTimePoint() {
		System.out.println("[Test]: addTimePoint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stn.addTimePoint();
			// print simple temporal network
			System.out.println(this.stn);
			
			// check time points
			Assert.assertTrue(this.stn.getTimePoints().size() == 3);
			Assert.assertTrue(this.stn.getTimePoints().contains(tp1));
			// check temporal relations
			TimePointDistanceConstraint rel01 = stn.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getDistanceLowerBound() == ORIGIN);
			Assert.assertTrue(rel01.getDistanceUpperBound() == HORIZON);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void deleteTimePoint() {
		System.out.println("[Test]: deleteTimePoint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stn.addTimePoint();
			TimePoint tp2 = this.stn.addTimePoint();
			// check time points
			Assert.assertTrue(this.stn.getTimePoints().size() == 4);
			System.out.println(this.stn);
			
			// delete time points
			this.stn.removeTimePoint(tp1);
			// check time points
			Assert.assertTrue(this.stn.getTimePoints().size() == 3);
			Assert.assertTrue(this.stn.getTimePoints().contains(tp2));
			Assert.assertFalse(this.stn.getTimePoints().contains(tp1));
			System.out.println(this.stn);
		
			// add a new time point
			TimePoint recTp1 = this.stn.addTimePoint();
			// check data
			Assert.assertTrue(this.stn.getTimePoints().size() == 4);
			// expected recycled point
			Assert.assertEquals(recTp1, tp1);
			System.out.println(this.stn);
		} 
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addDistanceConstraint() {
		System.out.println("[Test]: addDistanceConstraint() --------------------");
		TimePoint tp1 = null;
		try {
			// add a time point to the "observed" network
			tp1 = this.stn.addTimePoint();
			// create temporal relation
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(this.stn.getOriginTimePoint());
			c1.setTarget(tp1);
			c1.setDistanceLowerBound(5);
			c1.setDistanceUpperBound(300);
			c1.setControllable(true);
			// add constraint 
			this.stn.addDistanceConstraint(c1);
			
			// create temporal relation
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(this.stn.getOriginTimePoint());
			c2.setTarget(tp1);
			c2.setDistanceLowerBound(11);
			c2.setDistanceUpperBound(150);
			c2.setControllable(true);
			// add constraint 
			this.stn.addDistanceConstraint(c2);
			
			// create temporal relation
			TimePointDistanceConstraint c3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c3.setReference(this.stn.getOriginTimePoint());
			c3.setTarget(tp1);
			c3.setDistanceLowerBound(80);
			c3.setDistanceUpperBound(160);
			c3.setControllable(true);
			// add constraint 
			this.stn.addDistanceConstraint(c3);
			
			System.out.println(this.stn);
			
			// check temporal relation in STN
			TimePointDistanceConstraint rel01 = this.stn.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stn.getOriginTimePoint()));
			Assert.assertTrue(rel01.getTarget().equals(tp1));
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
		
		try 
		{	
			// try to add a Disjunction of intervals
			TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c.setReference(this.stn.getOriginTimePoint());
			c.setTarget(tp1);
			c.setDistanceLowerBound(250);
			c.setDistanceUpperBound(480);
			c.setControllable(true);
			// add constraint
			this.stn.addDistanceConstraint(c);
			Assert.assertTrue(false);
		}			
		catch (IntervalDisjunctionException ex) {
			// exception expected
			System.out.println(ex.getMessage());
			System.out.println(this.stn);
		}
		catch (InconsistentDistanceConstraintException ex) {
			Assert.assertTrue(false);
		}
		finally {
			// check the status of the network
			TimePointDistanceConstraint rel01 = this.stn.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stn.getOriginTimePoint()));
			Assert.assertTrue(rel01.getTarget().equals(tp1));
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void deleteDistanceConstraint() {
		System.out.println("[Test]: deleteDistanceConstraint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stn.addTimePoint();
			// create temporal relation
			TimePointDistanceConstraint rel1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel1.setReference(this.stn.getOriginTimePoint());
			rel1.setTarget(tp1);
			rel1.setDistanceLowerBound(5);
			rel1.setDistanceUpperBound(300);
			rel1.setControllable(true);
			// add constraint
			this.stn.addDistanceConstraint(rel1);
					
			TimePointDistanceConstraint rel2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel2.setReference(this.stn.getOriginTimePoint());
			rel2.setTarget(tp1);
			rel2.setDistanceLowerBound(11);
			rel2.setDistanceUpperBound(150);
			rel2.setControllable(true);
			// add constraint
			this.stn.addDistanceConstraint(rel2);
					
			
			TimePointDistanceConstraint rel3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel3.setReference(this.stn.getOriginTimePoint());
			rel3.setTarget(tp1);
			rel3.setDistanceLowerBound(80);
			rel3.setDistanceUpperBound(160);
			rel3.setControllable(true);
			// add constraint
			this.stn.addDistanceConstraint(rel3);
			// print STN and TN
			System.out.println(this.stn);

			// check temporal relation in STN			
			TimePointDistanceConstraint rel01 = stn.getConstraints(this.stn.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);

			
			// delete temporal relation
			this.stn.removeConstraint(rel1);
			rel01 = this.stn.getConstraints(this.stn.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
			// print STN and TN
			System.out.println(this.stn);
			
			// delete temporal relation
			this.stn.removeConstraint(rel3);
			rel01 = stn.getConstraints(this.stn.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 11);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
			// print STN and TN
			System.out.println(this.stn);
			System.out.println(stn);
		}			
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
