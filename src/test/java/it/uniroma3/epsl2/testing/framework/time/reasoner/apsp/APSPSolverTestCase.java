package it.uniroma3.epsl2.testing.framework.time.reasoner.apsp;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.lang.query.TimePointDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.lang.query.TimePointScheduleQuery;
import it.uniroma3.epsl2.framework.time.tn.simple.SimpleTemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverFactory;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverType;
import it.uniroma3.epsl2.framework.time.tn.solver.apsp.APSPTemporalSolver;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.SimpleTemporalNetworkWithUncertainty;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;


/**
 * 
 * @author alessandroumbrico
 *
 */
public class APSPSolverTestCase 
{
	private static final long ORIGIN = 0;
	private static final long HORIZON = 500;
	private TemporalSolverFactory factory;
	private SimpleTemporalNetworkWithUncertainty tn;
	private TemporalQueryFactory qf;
	private TemporalConstraintFactory cf;
	private TimePoint tp1;
	private TimePoint tp2;
	private TimePoint tp3;
	private TimePoint tp4;
	
	/**
	 * 
	 */
	@Before
	public void setupTest() 
	{
		try {
			System.out.println("**********************************************************************************");
			System.out.println("****************************** APSPU Solver Test Case ****************************");
			System.out.println("**********************************************************************************");
			
			// create logger
			FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
			lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
			
			// set factory
			this.factory = new TemporalSolverFactory();
			// create temporal network
			TemporalNetworkFactory tnFactory = new TemporalNetworkFactory();
			this.tn = tnFactory.createSingleton(TemporalNetworkType.STNU, ORIGIN, HORIZON);
			
			// get query factory
			this.qf = TemporalQueryFactory.getInstance();
			this.cf = TemporalConstraintFactory.getInstance();
			
			// create time points
			this.tp1 = this.tn.addTimePoint();
			this.tp2 = this.tn.addTimePoint();
			this.tp3 = this.tn.addTimePoint();
			this.tp4 = this.tn.addTimePoint();
			
			// create constraints
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(this.tn.getOriginTimePoint());
			c1.setTarget(this.tp1);
			c1.setDistanceLowerBound(10);
			c1.setDistanceUpperBound(20);
			c1.setControllable(true);
			
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(this.tn.getOriginTimePoint());
			c2.setTarget(this.tp4);
			c2.setDistanceLowerBound(60);
			c2.setDistanceUpperBound(70);
			c2.setControllable(true);
			
			
			TimePointDistanceConstraint c3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c3.setReference(this.tp1);
			c3.setTarget(this.tp2);
			c3.setDistanceLowerBound(30);
			c3.setDistanceUpperBound(40);
			c3.setControllable(true);
			
			
			TimePointDistanceConstraint c4 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c4.setReference(this.tp3);
			c4.setTarget(this.tp2);
			c4.setDistanceLowerBound(10);
			c4.setDistanceUpperBound(20);
			c4.setControllable(true);
			
			
			TimePointDistanceConstraint c5 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c5.setReference(this.tp3);
			c5.setTarget(this.tp4);
			c5.setDistanceLowerBound(40);
			c5.setDistanceUpperBound(50);
			c5.setControllable(true);
			
			// add constraints
			this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {
					c1, c2, c3, c4, c5
			});
		}
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
		finally {
			// print the initial status of the network
			System.out.println(this.tn);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void init() {
		System.out.println("[Test]: init() --------------------");
		// create APSP solver
		APSPTemporalSolver solver = this.factory.create(TemporalSolverType.APSP);
		
		// check initialization
		Assert.assertNotNull(solver);
		// print initial matrix
		Assert.assertTrue(solver.isConsistent());
		System.out.println(solver);
		
		// get distance between origin and tp1
		TimePointScheduleQuery oquery = this.qf.create(TemporalQueryType.TP_SCHEDULE);
		// set point
		oquery.setTimePoint(this.tp1);
		// process query
		solver.process(oquery);
		// check bounds
		Assert.assertTrue(this.tp1.getLowerBound() == 10);
		Assert.assertTrue(this.tp1.getUpperBound() == 20);
		
		// get distance between tp3 tp4
		TimePointDistanceQuery dquery = this.qf.create(TemporalQueryType.TP_DISTANCE);
		// set points
		dquery.setSource(this.tp3);
		dquery.setTarget(this.tp4);
		// process query
		solver.process(dquery);
		// check bounds
		Assert.assertTrue(dquery.getDistanceLowerBound() == 40);
		Assert.assertTrue(dquery.getDistanceUpperBound() == 50);
		
		// check distances between origin and tp2
		oquery.setTimePoint(this.tp2);
		// process query
		solver.process(oquery);
		// check bounds
		Assert.assertTrue(this.tp2.getLowerBound() == 40);
		Assert.assertTrue(this.tp2.getUpperBound() == 50);
		
		// check distance between origin and tp3
		oquery.setTimePoint(this.tp3);
		// process query
		solver.process(oquery);
		Assert.assertTrue(this.tp3.getLowerBound() == 20);
		Assert.assertTrue(this.tp3.getUpperBound() == 30);
		
		// check number of propagations
		Assert.assertTrue(solver.getPropagationCounter() == 1);
	}
	
	/**
	 * 
	 */
	@Test
	public void example1() {
		try 
		{
			System.out.println("[Test]: example1() --------------------");
			// create temporal network
			TemporalNetworkFactory tnFactory = new TemporalNetworkFactory();
			SimpleTemporalNetwork itn = tnFactory.create(TemporalNetworkType.STN, ORIGIN, HORIZON);
			
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, itn);
			
			// create time points
			List<TimePoint> tps = itn.addTimePoints(3);
			
			// create constraints
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(tps.get(0));
			c1.setTarget(tps.get(1));
			c1.setDistanceLowerBound(10);
			c1.setDistanceUpperBound(10);
			c1.setControllable(true);
			
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(tps.get(1));
			c2.setTarget(tps.get(2));
			c2.setDistanceLowerBound(10);
			c2.setDistanceUpperBound(10);
			c2.setControllable(true);
			
			TimePointDistanceConstraint c3 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c3.setReference(tps.get(2));
			c3.setTarget(tps.get(0));
			c3.setDistanceLowerBound(10);
			c3.setDistanceUpperBound(10);
			c3.setControllable(true);
			
			// create inconsistent constraints
			itn.addDistanceConstraint(new TimePointDistanceConstraint[] {
					c1, c2, c3
			});
			
			// check inconsistency
			Assert.assertFalse(solver.isConsistent());
			// print initial distance information
			System.out.println(solver);
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void example2() {
		try 
		{
			System.out.println("[Test]: example2() --------------------");
			// create example temporal network
			TemporalNetworkFactory tnFactory = new TemporalNetworkFactory();
			SimpleTemporalNetwork exTn = tnFactory.create(TemporalNetworkType.STN, ORIGIN, HORIZON);
			
			// create time points
			List<TimePoint> tps = exTn.addTimePoints(2);
			
			// create constraints
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(exTn.getOriginTimePoint());
			c1.setTarget(tps.get(0));
			c1.setDistanceLowerBound(5);
			c1.setDistanceUpperBound(10);
			c1.setControllable(true);
			
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(tps.get(0));
			c2.setTarget(tps.get(1));
			c2.setDistanceLowerBound(20);
			c2.setDistanceUpperBound(20);
			c2.setControllable(true);
			
			// add constraints
			exTn.addDistanceConstraint(new TimePointDistanceConstraint[] {
					c1, c2
			});
			
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, exTn);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			System.out.println(solver);
			
			// check information
			Assert.assertTrue(solver.isConsistent());
			// create query
			TimePointScheduleQuery query = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			TimePoint p = tps.get(0);
			query.setTimePoint(p);
			// process query
			solver.process(query);
			// check bounds
			Assert.assertTrue(p.getLowerBound() == 5);
			Assert.assertTrue(p.getUpperBound() == 10);
			
			// get distance between origin and p2
			p = tps.get(1);
			query.setTimePoint(p);
			// process query
			solver.process(query);
			// check bounds
			Assert.assertTrue(p.getLowerBound() == 25);
			Assert.assertTrue(p.getUpperBound() == 30);
			
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void example2Incremental() {
		try 
		{
			System.out.println("[Test]: example2Incremental() --------------------");
			// create example temporal network
			TemporalNetworkFactory tnFactory = new TemporalNetworkFactory();
			SimpleTemporalNetwork exTn = tnFactory.create(TemporalNetworkType.STN, ORIGIN, HORIZON);
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, exTn);
			
			// create time points
			TimePoint p1 = exTn.addTimePoint();
			TimePoint p2 = exTn.addTimePoint();
			
			
			// create constraints
			TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c1.setReference(exTn.getOriginTimePoint());
			c1.setTarget(p1);
			c1.setDistanceLowerBound(5);
			c1.setDistanceUpperBound(10);
			c1.setControllable(true);
			
			TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			c2.setReference(p1);
			c2.setTarget(p2);
			c2.setDistanceLowerBound(20);
			c2.setDistanceUpperBound(20);
			c2.setControllable(true);
			
			
			// create distance constraints
			exTn.addDistanceConstraint(new TimePointDistanceConstraint[] {
					c1, c2
			});
			
			// check information
			Assert.assertTrue(solver.isConsistent());
			// print distance matrix
			System.out.println(solver);
			// get distance between origin and p1
			TimePointScheduleQuery query1 = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			query1.setTimePoint(p1);
			// process 
			solver.process(query1);
			// check bounds
			Assert.assertTrue(p1.getLowerBound() == 5);
			Assert.assertTrue(p1.getUpperBound() == 10);
			
			// get distance between origin and p2
			query1.setTimePoint(p2);
			// process 
			solver.process(query1);
			Assert.assertTrue(p2.getLowerBound() == 25);
			Assert.assertTrue(p2.getUpperBound() == 30);
			
			// get distance between p1 and horizon
			TimePointDistanceQuery query2 = this.qf.create(TemporalQueryType.TP_DISTANCE);
			// set points
			query2.setSource(p1);
			query2.setTarget(p2);
			// process
			solver.process(query2);
			// check bounds
			Assert.assertTrue(query2.getDistanceLowerBound() == 20);
			Assert.assertTrue(query2.getDistanceUpperBound() == 20);
			
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 1);
		}
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void updateTemporalNetworkPoint() {
		System.out.println("[Test]: updateTemporalNetworkPoint() --------------------");
		try {
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, this.tn);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			
			// create time point
			TimePoint tp5 = this.tn.addTimePoint();
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			
			// create time point
			TimePoint tp6 = this.tn.addTimePoint();
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			// print network
			System.out.println(solver);
			
			// delete time point
			this.tn.removeTimePoint(tp5);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			// delete time point
			this.tn.removeTimePoint(tp6);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			
			// get distance between origin and tp1
			TimePointScheduleQuery query1 = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			query1.setTimePoint(this.tp1);
			// process
			solver.process(query1);
			// check bounds
			Assert.assertTrue(this.tp1.getLowerBound() == 10);
			Assert.assertTrue(this.tp1.getUpperBound() == 20);
			
			// get distance between tp3 tp4
			TimePointDistanceQuery query2 = this.qf.create(TemporalQueryType.TP_DISTANCE);
			// set points
			query2.setSource(this.tp3);
			query2.setTarget(this.tp4);
			// process 
			solver.process(query2);
			// check bounds
			Assert.assertTrue(query2.getDistanceLowerBound() == 40);
			Assert.assertTrue(query2.getDistanceUpperBound() == 50);
			
			// check distances between origin and tp2
			query1.setTimePoint(this.tp2);
			// process query
			solver.process(query1);
			// check bounds
			Assert.assertTrue(this.tp2.getLowerBound() == 40);
			Assert.assertTrue(this.tp2.getUpperBound() == 50);
			
			// check distance between origin and tp3
			query1.setTimePoint(this.tp3);
			// process query
			solver.process(query1);
			Assert.assertTrue(this.tp3.getLowerBound() == 20);
			Assert.assertTrue(this.tp3.getUpperBound() == 30);
			
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 3);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	
	
	/**
	 * 
	 */
	@Test
	public void updateTemporalNetworkEdges() {
		try {
			System.out.println("[Test]: updateTemporalNetworkEdges() --------------------");
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, this.tn);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			
			// create temporal relation
			TimePointDistanceConstraint rel = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel.setReference(this.tn.getOriginTimePoint());
			rel.setTarget(this.tp4);
			rel.setDistanceLowerBound(61);
			rel.setDistanceUpperBound(65);
			rel.setControllable(true);

			// add constraint
			this.tn.addDistanceConstraint(rel);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			// check distances
			TimePointScheduleQuery query = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			query.setTimePoint(this.tp4);
			// process query
			solver.process(query);
			// check bounds
			Assert.assertTrue(this.tp4.getLowerBound() == 61);
			Assert.assertTrue(this.tp4.getUpperBound() == 65);
			// print network
			System.out.println(solver);
			
			// delete relation
			this.tn.removeConstraint(rel);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			// print network
			System.out.println(solver);
			
			// check distance between origin and tp4
			query.setTimePoint(this.tp4);
			// process query
			solver.process(query);
			// check bounds
			Assert.assertTrue(this.tp4.getLowerBound() == 60);
			Assert.assertTrue(this.tp4.getUpperBound() == 100);
			
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 3);
		}
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
