package it.uniroma3.epsl2.testing.framework.time.tn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.IntervalDisjunctionException;
import it.uniroma3.epsl2.framework.time.tn.stn.SimpleTemporalNetwork;

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
			TimePointConstraint rel01 = stn.getConstraintFromOrigin(tp1);
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
			this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {5, 300}, true);
			this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {11, 150}, true);
			this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {80, 160}, true);
			System.out.println(this.stn);
			
			// check temporal relation in STN
			TimePointConstraint rel01 = this.stn.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stn.getOriginTimePoint()));
			Assert.assertTrue(rel01.getTarget().equals(tp1));
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
		
		try {	
			// try to add a Disjunction of intervals
			this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {250, 480}, true);
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
			TimePointConstraint rel01 = this.stn.getConstraintFromOrigin(tp1);
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
			TimePointConstraint rel1 = this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {5, 300}, true);
			this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {11, 150}, true);
			TimePointConstraint rel3 = this.stn.addConstraint(this.stn.getOriginTimePoint(), tp1, new long[] {80, 160}, true);
			// print STN and TN
			System.out.println(this.stn);

			// check temporal relation in STN			
			TimePointConstraint rel01 = stn.getConstraints(this.stn.getOriginTimePoint(), tp1).get(0);
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
