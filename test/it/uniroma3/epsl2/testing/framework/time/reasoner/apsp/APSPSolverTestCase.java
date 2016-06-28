package it.uniroma3.epsl2.testing.framework.time.reasoner.apsp;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkFactory;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.TemporalNetworkTransactionFailureException;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverFactory;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolverType;
import it.uniroma3.epsl2.framework.time.tn.solver.apsp.APSPTemporalSolver;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointBoundQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.stn.SimpleTemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.stnu.SimpleTemporalNetworkWithUncertainty;
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
	private TimePoint tp1;
	private TimePoint tp2;
	private TimePoint tp3;
	private TimePoint tp4;
	
	/**
	 * 
	 */
	@Before
	public void setupTest() {
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
			
			// create time points
			this.tp1 = this.tn.addTimePoint();
			this.tp2 = this.tn.addTimePoint();
			this.tp3 = this.tn.addTimePoint();
			this.tp4 = this.tn.addTimePoint();
			
			// create constraints
			this.tn.addConstraints(
			new TimePoint[] {
				this.tn.getOriginTimePoint(),
				this.tn.getOriginTimePoint(),
				this.tp1,
				this.tp3,
				this.tp3
			}, 
			new TimePoint[] {
					this.tp1,
					this.tp4,
					this.tp2,
					this.tp2,
					this.tp4
			},
			new long[][] {
				{10, 20},
				{60, 70},
				{30, 40},
				{10, 20},
				{40, 50}
			},
			new boolean[] {true, true, true, true, true});
		}
		catch (TemporalNetworkTransactionFailureException | InconsistentDistanceConstraintException ex) {
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
		TimePointBoundQuery oquery = this.qf.create(TemporalQueryType.TP_BOUND);
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
		Assert.assertTrue(dquery.getDistance()[0] == 40);
		Assert.assertTrue(dquery.getDistance()[1] == 50);
		
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
			// create inconsistent constraints
			itn.addConstraints(
					new TimePoint[] {tps.get(0), tps.get(1), tps.get(2)}, 
					new TimePoint[] {tps.get(1), tps.get(2), tps.get(0)},
					new long[][] {
						{10, 10},
						{10, 10},
						{10, 10}
					},
					new boolean[] {true, true, true}
					);
			
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
			// create distance constraints
			exTn.addConstraints(
					new TimePoint[] {exTn.getOriginTimePoint(), tps.get(0)}, 
					new TimePoint[] {tps.get(0), tps.get(1)},
					new long[][] {
						{5, 10},
						{20, 20}
					},
					new boolean[] {true, true}
					);
			
			// create APSP solver
			APSPTemporalSolver solver = this.factory.
					create(TemporalSolverType.APSP, exTn);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			System.out.println(solver);
			
			// check information
			Assert.assertTrue(solver.isConsistent());
			// create query
			TimePointBoundQuery query = this.qf.create(TemporalQueryType.TP_BOUND);
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
			// create distance constraints
			exTn.addConstraint(exTn.getOriginTimePoint(), p1, new long[] {5, 10}, true);
			exTn.addConstraint(p1, p2, new long[] {20, 20}, true);
			
			// check information
			Assert.assertTrue(solver.isConsistent());
			// print distance matrix
			System.out.println(solver);
			// get distance between origin and p1
			TimePointBoundQuery query1 = this.qf.create(TemporalQueryType.TP_BOUND);
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
			Assert.assertTrue(query2.getDistance()[0] == 20);
			Assert.assertTrue(query2.getDistance()[1] == 20);
			
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
			TimePointBoundQuery query1 = this.qf.create(TemporalQueryType.TP_BOUND);
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
			Assert.assertTrue(query2.getDistance()[0] == 40);
			Assert.assertTrue(query2.getDistance()[1] == 50);
			
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
			TimePointConstraint rel = this.tn.addConstraint(this.tn.getOriginTimePoint(), this.tp4, new long[] {61, 65}, true);
			// check consistency
			Assert.assertTrue(solver.isConsistent());
			// check distances
			TimePointBoundQuery query = this.qf.create(TemporalQueryType.TP_BOUND);
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
			Assert.assertTrue(this.tp4.getUpperBound() == 70);
			
			// check number of propagations
			Assert.assertTrue(solver.getPropagationCounter() == 3);
		}
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
