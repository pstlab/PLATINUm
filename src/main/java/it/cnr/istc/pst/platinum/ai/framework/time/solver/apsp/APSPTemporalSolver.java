package it.cnr.istc.pst.platinum.ai.framework.time.solver.apsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolver;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetwork;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceFromOriginQuery;
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
	private boolean toPropagate;								// lazy approach - propagate constraint only when needed
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
		this.toPropagate = true;
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
	public boolean isConsistent() 
	{
		// check information status
		if (this.toPropagate) {
			// clean distance matrix's data
			this.compute();
		}
		
		// check consistency
		boolean consistent = true;
		Iterator<TimePoint> it = this.dg.getPoints().iterator();
		while (it.hasNext() && consistent) {
			
			// next time point
			TimePoint tp = it.next();
			// check cyclic distance
			long d = this.distance.get(tp).get(tp);
			// check condition
			consistent = (d == 0);
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
		switch (info.getType()) 
		{
			// temporal network initialized
//			case INITIALIZATION: {
//		
//				// initialize data structures
//				this.dg();
//				// set to propagate flag
//				this.toPropagate = true;
//			}
//			break;
		
			// temporal constraint added
//			case ADD_REL: {
//
//				// handle request
//				AddRelationTemporalNetworkNotification notif = (AddRelationTemporalNetworkNotification) info;
//				// check pairs of involved time points
//				Map<TimePoint, Set<TimePoint>> p2p= new HashMap<>();
//				// check added constraints
//				for (TimePointDistanceConstraint constraint : notif.getRels()) {
//					// check time points
//					if (!p2p.containsKey(constraint.getReference())) {
//						p2p.put(constraint.getReference(), new HashSet<>());
//					}
//					
//					// add pair
//					p2p.get(constraint.getReference()).add(constraint.getTarget());
//				}
//				
//				
//				// update the internal distance graph
//				for (TimePoint reference : p2p.keySet()) {
//					for (TimePoint target : p2p.get(reference)) {
//						
//						// get constraint bounds
//						long[] bounds = this.tn.getConstraintBounds(reference, target);
//						// check if bound exists
//						if (bounds != null) {
//							// add/replace the associated edge of the distance graph
//							this.dg.add(reference, target, bounds[1]);
//							this.dg.add(target, reference, -bounds[0]);
//						}
//					}
//				}
//				
//				// set propagate flag
//				this.toPropagate = true;
//			}
//			break;
			
			// temporal constraint deleted 
//			case DEL_REL: {
//				
//				// handle request
//				DelRelationTemporalNetworkNotification notif = (DelRelationTemporalNetworkNotification) info;
//				// check pairs of involved time points
//				Map<TimePoint, Set<TimePoint>> p2p= new HashMap<>();
//				for (TimePointDistanceConstraint constraint : notif.getRels()) {
//					// check time points
//					if (!p2p.containsKey(constraint.getReference())) {
//						p2p.put(constraint.getReference(), new HashSet<>());
//					}
//					
//					// add pair
//					p2p.get(constraint.getReference()).add(constraint.getTarget());
//				}
//				
//				
//				// update the internal distance graph
//				for (TimePoint reference : p2p.keySet()) {
//					for (TimePoint target : p2p.get(reference)) {
//						
//						// get constraint bounds
//						long[] bounds = this.tn.getConstraintBounds(reference, target);
//						// check if bound exists
//						if (bounds != null) {
//							// add/replace the associated edge of the distance graph
//							this.dg.add(reference, target, bounds[1]);
//							this.dg.add(target, reference, -bounds[0]);
//						}
//					}
//				}
//				
//				// set propagate flag
//				this.toPropagate = true;
//			}
//			break;
			
			// time point deleted
			case INITIALIZATION :
			case ADD_TP : 
			case ADD_REL : 
			case DEL_REL : 
			case DEL_TP : {
				
				// handle request
//				DelTimePointTemporalNetworkNotification notif = (DelTimePointTemporalNetworkNotification) info;
				// delete time points
//				for (TimePoint point : notif.getPoints()) {
//					// delete node from the distance graph
//					this.dg.delete(point);
//				}
//				
				// recompute dependency graph distance bounds
				this.dg();	
				// set to propagate flag
				this.toPropagate = true;
			}
			break;
			
			// time point added 
//			case ADD_TP: {
//				
//				// handle request
//				AddTimePointTemporalNetworkNotification notif = (AddTimePointTemporalNetworkNotification) info;
//				// get added time points
//				for (TimePoint point : notif.getPoints()) {
//					// add the point to the distance graph
//					this.dg.add(point);
//				}
//				
//				
//				// it is not necessary to update information yet
//				this.toPropagate = true;
//			}
//			break;
			
			// other	
			default: {
				throw new RuntimeException("[" + this.getClass().getName() + "]: Unknown notification received type= " + info.getType());
			}
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
		// check if to propagate
		if (this.toPropagate) {
			this.compute();
		}
		
		// print distance matrix 
		String matrix = "Distance matrix (Computed by Floyd-Warshall Algorithm)\n";
		if (this.toPropagate) {
			// not update distance information
			matrix += "Not Updated Distance Information";
		} else {
			for (TimePoint i : this.dg.getPoints()) {
				for (TimePoint j : this.dg.getPoints()) {
					matrix += "\t" + this.distance.get(i).get(j);
				}
				matrix += "\n";
			}
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
			
			// handle time point distance from origin query
			case TP_DISTANCE_FROM_ORIGIN : {
				
				// get query
				TimePointDistanceFromOriginQuery tpDistanceQuery = (TimePointDistanceFromOriginQuery) query;
				// get time point
				TimePoint tp = tpDistanceQuery.getTimePoint();
				// get distance to horizon
				long[] distance = this.getDistance(this.tn.getOriginTimePoint(), tp);
				// set information
				tpDistanceQuery.setDistance(distance);
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
		if (this.toPropagate) {
			// clean distance matrix's data
			this.compute();
		}
		
		// default distance bounds - total uncertainty
		long[] bounds = new long[] {
				0, 
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
	private void compute() {
		
		// check size of the distance graph
		this.distance = new HashMap<TimePoint, Map<TimePoint, Long>>();
		// initialize distances to infinity
		for (TimePoint i : this.dg.getPoints()) {
			
			this.distance.put(i, new HashMap<TimePoint, Long>());
			for (TimePoint j : this.dg.getPoints()) {
				
				// initialize
				if (i.equals(j)) {
					
					this.distance.get(i).put(j, 0l);
					
				} else {
					
					this.distance.get(i).put(j, this.dg.getInfity());
				}
			}
		}
		
		// initialize distances using the weights of the distance graph's edges
		for (TimePoint point : this.dg.getPoints()) {
			
			// get adjacent points
			for (TimePoint adj : this.dg.getAdjacents(point)) {
				// get distance
				long distance = this.dg.getDistance(point, adj);
				// set distance
				this.distance.get(point).put(adj, distance);
			}
		}
		
		// compute minimum distances - k intermediate node
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
		this.toPropagate = false;
	}
	
	/**
	 * 
	 */
	@Override
	public void printDiagnosticData() {
		// compute if temporal data
		if (this.toPropagate) {
			this.compute();
		}
		
		System.out.println("Distance Graph:\n"
				+ "" + this.dg + "\n\n"
				+ "Distance matrix:\n"
				+ "" + this + "\n");
	}
}
