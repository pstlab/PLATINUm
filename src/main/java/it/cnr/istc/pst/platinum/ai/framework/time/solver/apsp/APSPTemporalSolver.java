package it.cnr.istc.pst.platinum.ai.framework.time.solver.apsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolver;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetwork;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePointDistanceConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.AddRelationTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.DelRelationTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceToHorizonQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author alessandro
 *
 */
public final class APSPTemporalSolver extends TemporalSolver<TimePointQuery>
{
	private DistanceGraph dg;									// distance graph
	private Map<TimePoint, Map<TimePoint, Long>> distance;		// the distance matrix containing the minimum distances between points;
	private boolean toCompute;								// lazy approach - propagate constraint only when needed
	private int propagationCounter;		
	
	/**
	 * Create an All-Pair-Shortest-Path Solver instance.
	 * 
	 */
	public APSPTemporalSolver(TemporalNetwork tn) {
		super(tn);
		
		// initialize APSP data structure
		this.distance = null;
		// attribute for testing purposes
		this.propagationCounter = 0;
		// initialize
		this.dg();
		// set flag
		this.toCompute = true;
	}
	
	/**
	 * 
	 */
	private void dg() {

		// create the distance graph
		this.dg = new DistanceGraph();
		// set temporal horizon
		this.dg.setInfity(this.tn.getHorizon());
		
		// add all points to the distance graph
		for (TimePoint point : this.tn.getTimePoints()) {
			// add node to the distance graph
			this.dg.add(point);
		}
		
		// add edges between points 
		for (TimePoint reference : this.tn.getTimePoints()) {
			for (TimePoint target : this.tn.getTimePoints()) {
				
				// check if different
				if (!reference.equals(target)) {
					
					// get constraint bounds
					long[] bounds = this.tn.getConstraintBounds(reference, target);
					// check if a bound exists
					if (bounds != null) {
						
						// set distance graph's edges according to the computed bounds
						this.dg.add(reference, target, bounds[1]);
						this.dg.add(target, reference, -bounds[0]);
					}
				} 
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isValid() 
	{
		// check information status
		if (this.toCompute) {
			// clean distance matrix's data
			this.computeDistanceMatrix();
		}
		
		// check consistency
		boolean consistent = true;
		Iterator<TimePoint> it = this.dg.getPoints().iterator();
		while (it.hasNext() && consistent) {
			
			// next time point
			TimePoint tp = it.next();
			// get distance
			long distance = this.distance.get(tp).get(tp); 
			// check cycle distance
			consistent = distance == 0;
		}
		
		// get consistency check result
		return consistent;
	}
	
	/**
	 * 
	 */
	@Override
	public void notify(TemporalNetworkNotification info)
	{
		// check notification type
		switch (info.getType()) {
			
			// time point deleted
			case INITIALIZATION :
			case ADD_TP : 
			case DEL_TP : {
				
				// update the distance graph 
				this.dg();	
				// set to propagate flag
				this.toCompute = true;
			}
			break;
			
			case DEL_REL : {
				
				// get data
				DelRelationTemporalNetworkNotification notify = (DelRelationTemporalNetworkNotification) info;
				
				// check constraints and updated distance graph
				for (TimePointDistanceConstraint constraint : notify.getRels()) {
					// check bound intersection between the two points
					long[] bounds = this.tn.getConstraintBounds(constraint.getReference(), constraint.getTarget());
					// check if a constraint still exists
					if (bounds != null) {
						
						// update distance bounds
						this.dg.add(constraint.getReference(), constraint.getTarget(), bounds[1]);
						this.dg.add(constraint.getTarget(), constraint.getReference(), -bounds[0]);
						
					} else {
						
						// no constraint between the two time points
						this.dg.delete(constraint.getReference(), constraint.getTarget());
						this.dg.delete(constraint.getTarget(), constraint.getReference());
					}
				}
				
				// set to propagate flag
				this.toCompute = true;
			}
			break;
			
			case ADD_REL : {
				
				// get data
				AddRelationTemporalNetworkNotification notify = (AddRelationTemporalNetworkNotification) info;
				
				// check constraints and update distance graph
				for (TimePointDistanceConstraint constraint : notify.getRels()) {
					// check bound intersection between the two time points
					long[] bounds = this.tn.getConstraintBounds(constraint.getReference(), constraint.getTarget());
					
					// update distance bounds
					this.dg.add(constraint.getReference(), constraint.getTarget(), bounds[1]);
					this.dg.add(constraint.getTarget(), constraint.getReference(), -bounds[0]);
				}
				
				// set to propagate flag
				this.toCompute = true;
			}
			break; 
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public DistanceGraph getDistanceGraph() {
		// get the distance graph
		return this.dg;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		
		if (this.toCompute) {
			this.computeDistanceMatrix();
		}
		
		// print distance matrix 
		String matrix = "Distance matrix (Computed using Floyd-Warshall Algorithm)\n";
		for (TimePoint i : this.dg.getPoints()) {
			for (TimePoint j : this.dg.getPoints()) {
				matrix += "\t" + this.distance.get(i).get(j);
			}
			matrix += "\n";
		}
	
		// get description of the distance matrix
		return matrix;
	}
	
	/**
	 * Returns the number of temporal propagation actually done
	 * Only for testing purposes
	 * 
	 * @return
	 */
	public int getPropagationCounter() {
		return propagationCounter;
	}
	
	/**
	 * 
	 */
	@Override
	public void process(TimePointQuery query) {
		
		// check query type 
		switch (query.getType()) {
		
			// handle time point bound query
			case TP_SCHEDULE : {
				
				// get query
				TimePointScheduleQuery tpBoundQuery = (TimePointScheduleQuery) query;
				// get time point
				TimePoint point = tpBoundQuery.getTimePoint();
				// get distance between the origin and the time point
				long[] distance = this.getDistance(this.tn.getOriginTimePoint(), point); 
				// set information 
				point.setLowerBound(distance[0]);
				point.setUpperBound(distance[1]);
			}
			break;
			
			// handle time point distance query
			case TP_DISTANCE : {
				
				// get query
				TimePointDistanceQuery tpDistanceQuery = (TimePointDistanceQuery) query;
				// get source point 
				TimePoint source = tpDistanceQuery.getSource();
				// get target point 
				TimePoint target = tpDistanceQuery.getTarget();
				// get distance between points
				long[] distance = this.getDistance(source, target);
				// set information
				tpDistanceQuery.setDistanceLowerBound(distance[0]);
				tpDistanceQuery.setDistanceUpperBound(distance[1]);
			}
			break;
			
			// handle time point distance to horizon query
			case TP_DISTANCE_TO_HORIZON : {
				
				// get query 
				TimePointDistanceToHorizonQuery tpDistanceQuery = (TimePointDistanceToHorizonQuery) query;
				// get time point
				TimePoint tp = tpDistanceQuery.getTimePoint();
				// get distance to horizon
				long[] distance = this.getDistance(tp, this.tn.getHorizonTimePoint());
				// set information
				tpDistanceQuery.setDistance(distance);
			}
			break;
			
			default : {
				
				// not a time point query
				throw new RuntimeException("Impossible to process this type of temporal query " + query.getType());
			}
		}
	}
	
	/**
	 * Returns distance lower and upper bounds of Time Point 
	 * tp1 to Time Point tp2 
	 * 
	 * @param tp1
	 * @param tp2
	 * @return
	 */
	protected long[] getDistance(TimePoint tp1, TimePoint tp2) {
		
		// compute minimal minimal network if needed
		if (this.toCompute) {
			// clean distance matrix's data
			this.computeDistanceMatrix();
		}
		
		// default distance bounds - total uncertainty
		long[] bounds = new long[] {
				-this.tn.getHorizon(), 
				this.tn.getHorizon()
		};
		
		// check if both time points exists
		if (this.distance.containsKey(tp1) && this.distance.get(tp1).containsKey(tp2)) {
			// set bounds
			bounds = new long[] {
					-this.distance.get(tp2).get(tp1),		// lower bound
					this.distance.get(tp1).get(tp2)			// upper bound
				};
		}
		
		// get bounds
		return bounds;
	}
	
	/**
	 * 
	 */
	private void computeDistanceMatrix() {
		
		// check size of the distance graph
		this.distance = new HashMap<TimePoint, Map<TimePoint, Long>>();
		// initialize distances to infinity
		for (TimePoint i : this.dg.getPoints()) {
			
			// create data structure
			this.distance.put(i, new HashMap<TimePoint, Long>());
			for (TimePoint j : this.dg.getPoints()) {
				
				// check pairs of time points
				if (i.equals(j)) {
					// set distance to 0
					this.distance.get(i).put(j, 0l);
					
				} else {
					
					// set distance to infinity
					this.distance.get(i).put(j, this.dg.getInfity());
				}
			}
		}
		
		// initialize distances using computed intersections from distance constraints
		for (TimePoint point : this.dg.getPoints()) {
			// get adjacent points
			for (TimePoint adj : this.dg.getAdjacents(point)) {
				
				// get distance
				long distance = this.dg.getDistance(point, adj);
				// set distance
				this.distance.get(point).put(adj, distance);
			}
		}
		
		// compute minimum distance between two nodes passing through any k intermediate node
		for (TimePoint k : this.dg.getPoints()) {
			// compute shortest paths using intermediate points
			for (TimePoint i : this.dg.getPoints()) {
				for (TimePoint j : this.dg.getPoints()) {
					
					// compute the path from i to j through k
					long path = this.distance.get(i).get(k) + this.distance.get(k).get(j);
					// compare computed distance with the direct path
					if (this.distance.get(i).get(j) > path) {
						// update distance
						this.distance.get(i).put(j, path);
					}
				}
			}
		}
		
		// update propagation counter
		this.propagationCounter++;
		// set propagation flag
		this.toCompute = false;
	}
	
	/**
	 * 
	 */
	@Override
	public void printDiagnosticData() {
		// compute if temporal data
		if (this.toCompute) {
			this.computeDistanceMatrix();
		}
		
		System.out.println("Distance Graph:\n"
				+ "" + this.dg + "\n\n"
				+ "Distance matrix:\n"
				+ "" + this + "\n");
	}
}
