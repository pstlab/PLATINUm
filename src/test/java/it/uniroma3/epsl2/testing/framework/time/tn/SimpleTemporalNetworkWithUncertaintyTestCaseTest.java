package it.uniroma3.epsl2.testing.framework.time.tn;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.IntervalDisjunctionException;
import it.uniroma3.epsl2.framework.time.tn.stnu.SimpleTemporalNetworkWithUncertainty;

/**
 * 
 * @author anacleto
 *
 */
public class SimpleTemporalNetworkWithUncertaintyTestCaseTest {

	private static final int ORIGIN = 0;
	private static final int HORIZON = 500;
	private SimpleTemporalNetworkWithUncertainty stnu;
	
	/**
	 * 
	 */
	@Before
	public void setupTest() {
		System.out.println("**********************************************************************************");
		System.out.println("***************** Simple Temporal Network with Uncertainty Test Case *************");
		System.out.println("**********************************************************************************");
		// create Simple Temporal Network with Uncertainty
		TemporalNetworkFactory factory = new TemporalNetworkFactory();
		this.stnu = factory.create(TemporalNetworkType.STNU, ORIGIN, HORIZON);
	}

	/**
	 * 
	 */
	@Test
	public void init() {
		System.out.println("[Test]: init() --------------------");
		// check network
		Assert.assertNotNull(this.stnu);
		System.out.println(this.stnu);
		// check number of initial time points
		Assert.assertTrue(this.stnu.size() == 2);
	}
	
	/**
	 * 
	 */
	@Test
	public void addTimePoint() {
		System.out.println("[Test]: addTimePoint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			// print simple temporal network
			System.out.println(this.stnu);
			
			// check time points
			Assert.assertTrue(this.stnu.size() == 3);
			Assert.assertTrue(this.stnu.getTimePoints().contains(tp1));
			// check temporal relations
			TimePointConstraint rel01 = stnu.getConstraintFromOrigin(tp1);
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
			TimePoint tp1 = this.stnu.addTimePoint();
			TimePoint tp2 = this.stnu.addTimePoint();
			// check time points
			Assert.assertTrue(this.stnu.size() == 4);
			System.out.println(this.stnu);
			
			// delete time points
			this.stnu.removeTimePoint(tp1);
			// check time points
			Assert.assertTrue(this.stnu.size() == 3);
			Assert.assertTrue(this.stnu.getTimePoints().contains(tp2));
			Assert.assertFalse(this.stnu.getTimePoints().contains(tp1));
			System.out.println(this.stnu);
		
			// add a new time point
			TimePoint recTp1 = this.stnu.addTimePoint();
			// check data
			Assert.assertTrue(this.stnu.size() == 4);
			// expected recycled point
			Assert.assertEquals(recTp1, tp1);
			System.out.println(this.stnu);
		} 
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addRequirementConstraint() {
		System.out.println("[Test]: addRequirementConstraint() --------------------");
		TimePoint tp1 = null;
		try {
			// add a time point to the "observed" network
			tp1 = this.stnu.addTimePoint();
			// create temporal relation
			this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {5, 300}, true);
			this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {11, 150}, true);
			this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {80, 160}, true);
			System.out.println(this.stnu);
			
			// check temporal constraint in STNU - the actual constraint must be the most tightening constraint propagated
			TimePointConstraint rel01 = this.stnu.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stnu.getOriginTimePoint()));
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
			this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {250, 480}, true);
			Assert.assertTrue(false);
		}			
		catch (IntervalDisjunctionException ex) {
			// exception expected
			System.out.println(ex.getMessage());
			System.out.println(this.stnu);
		}
		catch (InconsistentDistanceConstraintException ex) {
			Assert.assertTrue(false);
		}
		finally { 
			// check the status of the network
			TimePointConstraint rel01 = this.stnu.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stnu.getOriginTimePoint()));
			Assert.assertTrue(rel01.getTarget().equals(tp1));
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void deleteRequirementConstraint() {
		System.out.println("[Test]: deleteRequirementConstraint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			// create temporal relation
			TimePointConstraint rel1 = this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {5, 300}, true);
			this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {11, 150}, true);
			TimePointConstraint rel3 = this.stnu.addConstraint(this.stnu.getOriginTimePoint(), tp1, new long[] {80, 160}, true);
			// print STN and TN
			System.out.println(this.stnu);

			// check temporal relation in STN			
			TimePointConstraint rel01 = this.stnu.getConstraints(this.stnu.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);

			
			// delete temporal relation
			this.stnu.removeConstraint(rel1);
			rel01 = this.stnu.getConstraints(this.stnu.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 80);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
			// print STN and TN
			System.out.println(this.stnu);
			
			// delete temporal relation
			this.stnu.removeConstraint(rel3);
			rel01 = stnu.getConstraints(this.stnu.getOriginTimePoint(), tp1).get(0);
			Assert.assertTrue(rel01.getDistanceLowerBound() == 11);
			Assert.assertTrue(rel01.getDistanceUpperBound() == 150);
			// print STN and TN
			System.out.println(this.stnu);
			System.out.println(stnu);
		}			
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addContingentConstraint() {
		System.out.println("[Test]: addContingentConstraint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			TimePoint tp2 = this.stnu.addTimePoint();
			// create contingent constraint
			TimePointConstraint c = this.stnu.addConstraint(tp1, tp2, new long[] {5, 150}, false);
			System.out.println(this.stnu);
			
			// check added contingent constraint
			List<TimePointConstraint> list = this.stnu.getConstraints(tp1);
			Assert.assertNotNull(list);
			Assert.assertTrue(list.size() == 2);
			list = this.stnu.getContingetConstraints();
			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
			Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
			Assert.assertTrue(list.get(0).getReference().equals(tp1));
			Assert.assertTrue(list.get(0).getTarget().equals(tp2));
			Assert.assertFalse(c.isControllable());
			
			try {
				// try to add an "overlapping" contingent constraint
				this.stnu.addConstraint(tp1, tp2, new long[] {11, 23},  true);
				Assert.assertTrue(false);
			}
			catch (InconsistentDistanceConstraintException ex) {
				// exception expected
				list = this.stnu.getConstraints(tp1);
				Assert.assertNotNull(list);
				Assert.assertTrue(list.size() == 2);
				// get contingent constraints
				list = this.stnu.getContingetConstraints();
				Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
				Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
				Assert.assertTrue(!list.get(0).isControllable());
				System.out.println(this.stnu);
			}
			
			try {
				// overwrite contingent constraint
				this.stnu.addConstraint(tp1, tp2, new long[] {23, 55}, false);
				// check data
				list = this.stnu.getConstraints(tp1);
				Assert.assertNotNull(list);
				Assert.assertTrue(list.size() == 2);
				list = this.stnu.getContingetConstraints();
				Assert.assertTrue(list.size() == 1);
				Assert.assertTrue(list.get(0).getDistanceLowerBound() == 23);
				Assert.assertTrue(list.get(0).getDistanceUpperBound() == 55);
				Assert.assertTrue(list.get(0).getReference().equals(tp1));
				Assert.assertTrue(list.get(0).getTarget().equals(tp2));
				Assert.assertFalse(c.isControllable());
			}
			catch (InconsistentDistanceConstraintException exx) {
				System.err.println(exx.getMessage());
				Assert.assertTrue(false);
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
	public void deleteContingentConstraint() {
		System.out.println("[Test]: deleteContingentConstraint() --------------------");
		try {
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			TimePoint tp2 = this.stnu.addTimePoint();
			// create contingent constraint
			TimePointConstraint cc = this.stnu.addConstraint(tp1, tp2, new long[] {5, 150}, false);
			System.out.println(this.stnu);
			
			// check added contingent constraint
			List<TimePointConstraint> list = this.stnu.getContingetConstraints();
			Assert.assertNotNull(list);
			Assert.assertFalse(list.isEmpty());
			Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
			Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
			Assert.assertTrue(!list.get(0).isControllable());
			
			try {
				// try to add an "overlapping" contingent constraint
				this.stnu.addConstraint(tp1, tp2, new long[] {11, 23}, true);
				Assert.assertTrue(false);
			}
			catch (InconsistentDistanceConstraintException ex) {
				// exception expected
				list = this.stnu.getConstraints(tp1);
				Assert.assertNotNull(list);
				Assert.assertTrue(list.size() == 2);
				list = this.stnu.getContingetConstraints();
				Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
				Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
				Assert.assertTrue(!list.get(0).isControllable());
				System.out.println(this.stnu);
			}
			
			// remove contingent constraint
			this.stnu.removeConstraint(cc);
			// check network
			list = this.stnu.getContingetConstraints();
			Assert.assertNotNull(list);
			Assert.assertTrue(list.isEmpty());
			Assert.assertFalse(list.contains(cc));
			System.out.println(this.stnu);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
}
