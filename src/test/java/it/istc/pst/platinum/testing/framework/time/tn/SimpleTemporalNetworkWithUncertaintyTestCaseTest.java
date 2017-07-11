package it.istc.pst.platinum.testing.framework.time.tn;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.time.lang.TemporalConstraintFactory;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.tn.SimpleTemporalNetworkWithUncertainty;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkFactory;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkType;
import it.istc.pst.platinum.framework.time.tn.TimePoint;
import it.istc.pst.platinum.framework.time.tn.TimePointDistanceConstraint;
import it.istc.pst.platinum.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.istc.pst.platinum.framework.time.tn.ex.IntervalDisjunctionException;

/**
 * 
 * @author anacleto
 *
 */
public class SimpleTemporalNetworkWithUncertaintyTestCaseTest {

	private static final int ORIGIN = 0;
	private static final int HORIZON = 500;
	private SimpleTemporalNetworkWithUncertainty stnu;
	private TemporalConstraintFactory cf;
	
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
		this.cf = TemporalConstraintFactory.getInstance();
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
			TimePointDistanceConstraint rel01 = stnu.getConstraintFromOrigin(tp1);
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
		try 
		{
			// add a time point to the "observed" network
			tp1 = this.stnu.addTimePoint();
			// create constraint
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(this.stnu.getOriginTimePoint());
			c1.setTarget(tp1);
			c1.setDistanceLowerBound(5);
			c1.setDistanceUpperBound(300);
			c1.setControllable(true);
			// propagate constraints
			this.stnu.addDistanceConstraint(c1);
			
			// create constraint
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(this.stnu.getOriginTimePoint());
			c2.setTarget(tp1);
			c2.setDistanceLowerBound(11);
			c2.setDistanceUpperBound(150);
			c2.setControllable(true);
			// propagate constraint
			this.stnu.addDistanceConstraint(c2);

			// create constraint
			TimePointDistanceConstraint c3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c3.setReference(this.stnu.getOriginTimePoint());
			c3.setTarget(tp1);
			c3.setDistanceLowerBound(80);
			c3.setDistanceUpperBound(160);
			c3.setControllable(true);
			// propagate constraint
			this.stnu.addDistanceConstraint(c3);
			
			System.out.println(this.stnu);
			
			// check temporal constraint in STNU - the actual constraint must be the most tightening constraint propagated
			TimePointDistanceConstraint rel01 = this.stnu.getConstraintFromOrigin(tp1);
			Assert.assertTrue(rel01.getReference().equals(this.stnu.getOriginTimePoint()));
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
			c.setReference(this.stnu.getOriginTimePoint());
			c.setTarget(tp1);
			c.setDistanceLowerBound(250);
			c.setDistanceUpperBound(480);
			c.setControllable(true);
			// add constraint
			this.stnu.addDistanceConstraint(c);
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
			TimePointDistanceConstraint rel01 = this.stnu.getConstraintFromOrigin(tp1);
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
			// create constraint
			TimePointDistanceConstraint rel1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel1.setReference(this.stnu.getOriginTimePoint());
			rel1.setTarget(tp1);
			rel1.setDistanceLowerBound(5);
			rel1.setDistanceUpperBound(300);
			rel1.setControllable(true);
			// add constraint
			this.stnu.addDistanceConstraint(rel1);
			
			// create constraint
			TimePointDistanceConstraint rel2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel2.setReference(this.stnu.getOriginTimePoint());
			rel2.setTarget(tp1);
			rel2.setDistanceLowerBound(11);
			rel2.setDistanceUpperBound(150);
			rel2.setControllable(true);
			// add constraint
			this.stnu.addDistanceConstraint(rel2);
			
			// create constraint
			TimePointDistanceConstraint rel3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel3.setReference(this.stnu.getOriginTimePoint());
			rel3.setTarget(tp1);
			rel3.setDistanceLowerBound(80);
			rel3.setDistanceUpperBound(160);
			rel3.setControllable(true);
			// add constraint
			this.stnu.addDistanceConstraint(rel3);
			// print STN and TN
			System.out.println(this.stnu);

			// check temporal relation in STN			
			TimePointDistanceConstraint rel01 = this.stnu.getConstraints(this.stnu.getOriginTimePoint(), tp1).get(0);
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
		try 
		{
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			TimePoint tp2 = this.stnu.addTimePoint();
			// create contingent constraint
			TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c.setReference(tp1);
			c.setTarget(tp2);
			c.setDistanceLowerBound(5);
			c.setDistanceUpperBound(150);
			c.setControllable(false);			// contingent constraint
			// add constraint
			this.stnu.addDistanceConstraint(c);
			System.out.println(this.stnu);
			
			// check added contingent constraint
			List<TimePointDistanceConstraint> list = this.stnu.getConstraints(tp1);
			Assert.assertNotNull(list);
			Assert.assertTrue(list.size() == 2);
			list = this.stnu.getContingetConstraints();
			Assert.assertTrue(list.size() == 1);
			Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
			Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
			Assert.assertTrue(list.get(0).getReference().equals(tp1));
			Assert.assertTrue(list.get(0).getTarget().equals(tp2));
			Assert.assertFalse(c.isControllable());
			
			try 
			{
				// try to add an "overlapping" contingent constraint
				TimePointDistanceConstraint o = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
				o.setReference(tp1);
				o.setTarget(tp2);
				o.setDistanceLowerBound(11);
				o.setDistanceUpperBound(23);
				o.setControllable(true);
				// add constraint
				this.stnu.addDistanceConstraint(o);
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
			
			try 
			{
				// overwrite contingent constraint
				TimePointDistanceConstraint o = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
				o.setReference(tp1);
				o.setTarget(tp2);
				o.setDistanceLowerBound(23);
				o.setDistanceUpperBound(55);
				o.setControllable(false);
				// add distance constraint
				this.stnu.addDistanceConstraint(o);

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
		try 
		{
			// add a time point to the "observed" network
			TimePoint tp1 = this.stnu.addTimePoint();
			TimePoint tp2 = this.stnu.addTimePoint();
			// create contingent constraint
			TimePointDistanceConstraint cc = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			cc.setReference(tp1);
			cc.setTarget(tp2);
			cc.setDistanceLowerBound(5);
			cc.setDistanceUpperBound(150);
			cc.setControllable(false);
			// add constraint
			this.stnu.addDistanceConstraint(cc);
			System.out.println(this.stnu);
			
			// check added contingent constraint
			List<TimePointDistanceConstraint> list = this.stnu.getContingetConstraints();
			Assert.assertNotNull(list);
			Assert.assertFalse(list.isEmpty());
			Assert.assertTrue(list.get(0).getDistanceLowerBound() == 5);
			Assert.assertTrue(list.get(0).getDistanceUpperBound() == 150);
			Assert.assertTrue(!list.get(0).isControllable());
			
			try 
			{
				// try to add an "overlapping" contingent constraint
				TimePointDistanceConstraint o = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
				o.setReference(tp1);
				o.setTarget(tp2);
				o.setDistanceLowerBound(11);
				o.setDistanceUpperBound(23);
				o.setControllable(true);
				// add distance constraint
				this.stnu.addDistanceConstraint(o);
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
