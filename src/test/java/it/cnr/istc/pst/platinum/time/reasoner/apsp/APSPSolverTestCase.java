package it.cnr.istc.pst.platinum.time.reasoner.apsp;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryFactory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.apsp.APSPTemporalSolver;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.apsp.DistanceGraph;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.SimpleTemporalNetworkWithUncertainty;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetwork;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePointDistanceConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author alessandro
 *
 */
public class APSPSolverTestCase 
{
	private static final long ORIGIN = 0;
	private static final long HORIZON = 500;
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
	@BeforeAll
	public void setupTest() 
	{
		try {
			System.out.println("**********************************************************************************");
			System.out.println("****************************** APSPU Solver Test Case ****************************");
			System.out.println("**********************************************************************************");
			
			// create temporal network
			this.tn = new SimpleTemporalNetworkWithUncertainty(ORIGIN, HORIZON);
			
			// get query factory
			this.qf = new TemporalQueryFactory();
			this.cf = new TemporalConstraintFactory();
			
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
		
		try {
			
			// create APSP solver
			APSPTemporalSolver solver = new APSPTemporalSolver(this.tn);
			// check initialization
			assertNotNull(solver);
			
			
			
			
			// print initial matrix
			assertTrue(solver.isValid());
			System.out.println(solver);
			
			// get the underlying distance graph
			DistanceGraph graph = solver.getDistanceGraph();
			assertNotNull(graph);
			System.out.println(graph);
			
			// print also the network 
			System.out.println(this.tn);
			
			// get distance between origin and tp1
			TimePointScheduleQuery oquery = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			oquery.setTimePoint(this.tp1);
			// process query
			solver.process(oquery);
			// check bounds
			assertTrue(this.tp1.getLowerBound() == 10);
			assertTrue(this.tp1.getUpperBound() == 20);
			
			// get distance between tp3 tp4
			TimePointDistanceQuery dquery = this.qf.create(TemporalQueryType.TP_DISTANCE);
			// set points
			dquery.setSource(this.tp3);
			dquery.setTarget(this.tp4);
			// process query
			solver.process(dquery);
			// check bounds
			assertTrue(dquery.getDistanceLowerBound() == 40);
			assertTrue(dquery.getDistanceUpperBound() == 50);
			
			// check distances between origin and tp2
			oquery.setTimePoint(this.tp2);
			// process query
			solver.process(oquery);
			// check bounds
			assertTrue(this.tp2.getLowerBound() == 40);
			assertTrue(this.tp2.getUpperBound() == 50);
			
			// check distance between origin and tp3
			oquery.setTimePoint(this.tp3);
			// process query
			solver.process(oquery);
			assertTrue(this.tp3.getLowerBound() == 20);
			assertTrue(this.tp3.getUpperBound() == 30);
			
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 1);
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
	public void example1() {
		try 
		{
			System.out.println("[Test]: example1() --------------------");
			// create temporal network
			TemporalNetwork itn = new SimpleTemporalNetworkWithUncertainty(ORIGIN, HORIZON);
			// create APSP solver
			APSPTemporalSolver solver = new APSPTemporalSolver(itn);

			// create time points
			List<TimePoint> tps = itn.addMultipleTimePoints(3);
			
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
			assertFalse(solver.isValid());
			// print initial distance information
			System.out.println(solver);
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 1);
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
			TemporalNetwork exTn = new SimpleTemporalNetworkWithUncertainty(ORIGIN, HORIZON);
			
			// create time points
			List<TimePoint> tps = exTn.addMultipleTimePoints(2);
			
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
			APSPTemporalSolver solver = new APSPTemporalSolver(exTn);
			// check consistency
			assertTrue(solver.isValid());
			System.out.println(solver);
			
			// check information
			assertTrue(solver.isValid());
			// create query
			TimePointScheduleQuery query = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			TimePoint p = tps.get(0);
			query.setTimePoint(p);
			// process query
			solver.process(query);
			// check bounds
			assertTrue(p.getLowerBound() == 5);
			assertTrue(p.getUpperBound() == 10);
			
			// get distance between origin and p2
			p = tps.get(1);
			query.setTimePoint(p);
			// process query
			solver.process(query);
			// check bounds
			assertTrue(p.getLowerBound() == 25);
			assertTrue(p.getUpperBound() == 30);
			
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 1);
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
			TemporalNetwork exTn = new SimpleTemporalNetworkWithUncertainty(ORIGIN, HORIZON);
			// create APSP solver
			APSPTemporalSolver solver = new APSPTemporalSolver(exTn);
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
			assertTrue(solver.isValid());
			// print distance matrix
			System.out.println(solver);
			// get distance between origin and p1
			TimePointScheduleQuery query1 = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			query1.setTimePoint(p1);
			// process 
			solver.process(query1);
			// check bounds
			assertTrue(p1.getLowerBound() == 5);
			assertTrue(p1.getUpperBound() == 10);
			
			// get distance between origin and p2
			query1.setTimePoint(p2);
			// process 
			solver.process(query1);
			assertTrue(p2.getLowerBound() == 25);
			assertTrue(p2.getUpperBound() == 30);
			
			// get distance between p1 and horizon
			TimePointDistanceQuery query2 = this.qf.create(TemporalQueryType.TP_DISTANCE);
			// set points
			query2.setSource(p1);
			query2.setTarget(p2);
			// process
			solver.process(query2);
			// check bounds
			assertTrue(query2.getDistanceLowerBound() == 20);
			assertTrue(query2.getDistanceUpperBound() == 20);
			
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 1);
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
			APSPTemporalSolver solver =  new APSPTemporalSolver(this.tn);
			// check consistency
			assertTrue(solver.isValid());
			
			// create time point
			TimePoint tp5 = this.tn.addTimePoint();
			// check consistency
			assertTrue(solver.isValid());
			
			// create time point
			TimePoint tp6 = this.tn.addTimePoint();
			// check consistency
			assertTrue(solver.isValid());
			// print network
			System.out.println(solver);
			
			// delete time point
			this.tn.removeTimePoint(tp5);
			// check consistency
			assertTrue(solver.isValid());
			// delete time point
			this.tn.removeTimePoint(tp6);
			// check consistency
			assertTrue(solver.isValid());
			
			// get distance between origin and tp1
			TimePointScheduleQuery query1 = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			query1.setTimePoint(this.tp1);
			// process
			solver.process(query1);
			// check bounds
			assertTrue(this.tp1.getLowerBound() == 10);
			assertTrue(this.tp1.getUpperBound() == 20);
			
			// get distance between tp3 tp4
			TimePointDistanceQuery query2 = this.qf.create(TemporalQueryType.TP_DISTANCE);
			// set points
			query2.setSource(this.tp3);
			query2.setTarget(this.tp4);
			// process 
			solver.process(query2);
			// check bounds
			assertTrue(query2.getDistanceLowerBound() == 40);
			assertTrue(query2.getDistanceUpperBound() == 50);
			
			// check distances between origin and tp2
			query1.setTimePoint(this.tp2);
			// process query
			solver.process(query1);
			// check bounds
			assertTrue(this.tp2.getLowerBound() == 40);
			assertTrue(this.tp2.getUpperBound() == 50);
			
			// check distance between origin and tp3
			query1.setTimePoint(this.tp3);
			// process query
			solver.process(query1);
			assertTrue(this.tp3.getLowerBound() == 20);
			assertTrue(this.tp3.getUpperBound() == 30);
			
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 5);
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
			APSPTemporalSolver solver = new APSPTemporalSolver(this.tn);
			// check consistency
			assertTrue(solver.isValid());
			// check distances
			TimePointScheduleQuery query = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			query.setTimePoint(this.tp4);
			// process query
			solver.process(query);
			assertTrue(this.tp4.getLowerBound() == 60);
			assertTrue(this.tp4.getUpperBound() == 70);
			// print network
			System.out.println(solver);
			
			// create temporal relation
			TimePointDistanceConstraint rel = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			rel.setReference(this.tn.getOriginTimePoint());
			rel.setTarget(this.tp4);
			rel.setDistanceLowerBound(65);
			rel.setDistanceUpperBound(68);
			rel.setControllable(true);

			// add constraint
			this.tn.addDistanceConstraint(rel);
			// check consistency
			assertTrue(solver.isValid());
			// check distances
			query = this.qf.create(TemporalQueryType.TP_SCHEDULE);
			// set point
			query.setTimePoint(this.tp4);
			// process query
			solver.process(query);
			// check bounds
			assertTrue(this.tp4.getLowerBound() == 65);
			assertTrue(this.tp4.getUpperBound() == 68);
			// print network
			System.out.println(solver);
			
			// delete relation
			this.tn.removeConstraint(rel);
			// check consistency
			assertTrue(solver.isValid());
			// print network
			System.out.println(solver);
			
			// check distance between origin and tp4
			query.setTimePoint(this.tp4);
			// process query
			solver.process(query);
			// check bounds
			assertTrue(this.tp4.getLowerBound() == 60);
			assertTrue(this.tp4.getUpperBound() == 70);
			
			// check number of propagations
			assertTrue(solver.getPropagationCounter() == 3);
		}
		catch (InconsistentDistanceConstraintException ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
}
