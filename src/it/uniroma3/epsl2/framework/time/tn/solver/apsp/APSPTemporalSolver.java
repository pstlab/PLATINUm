package it.uniroma3.epsl2.framework.time.tn.solver.apsp;

import java.util.Iterator;

import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolver;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.AddRelationTemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.AddTimePointTemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.DelRelationTemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.DelTimePointTemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.TemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.ex.NotificationPropagationFailureException;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointBoundQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointDistanceFromOriginQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointDistanceToHorizonQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class APSPTemporalSolver extends TemporalSolver<TimePointQuery>
{
	private DistanceGraph dgraph;		// distance graph
	private long[][] distance;			// the matrix containing the minimum distances between nodes
	private boolean toPropagate;		// lazy approach - propagate constraint only when needed
	private int propagationCounter;		
	
	/**
	 * Create an All-Pair-Shortest-Path Solver instance.
	 * 
	 */
	protected APSPTemporalSolver() {
		super();
		// initialize APSP data structure
		this.toPropagate = false;
		this.distance = null;
		// attribute for testing purposes
		this.propagationCounter = 0;
		// create the distance graph
		this.dgraph = new DistanceGraph();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isConsistent() {
		// check information status
		if (this.toPropagate) {
			// clean distance matrix's data
			this.compute();
		}
		
		// check consistency
		boolean consistent = true;
		Iterator<TimePoint> it = this.dgraph.getPoints().iterator();
		while (it.hasNext() && consistent) {
			// next time point
			TimePoint tp = it.next();
			// check condition
			consistent = this.distance[tp.getId()][tp.getId()] == 0;
		}
		// get consistency check result
		return consistent;
	}
	
	/**
	 * 
	 */
	@Override
	public void notify(TemporalNetworkNotification info)
			throws NotificationPropagationFailureException {
		
		// set update flag
		this.toPropagate = true;
		// check notification type
		switch (info.getType()) {
		
			// temporal network initialized
			case INITIALIZATION: {
				// set temporal horizon
				this.dgraph.setInfity(this.tn.getHorizon() + 1);
				
				// add all points to the distance graph
				for (TimePoint point : this.tn.getTimePoints()) {
					// add node to the distance graph
					this.dgraph.add(point);
				}
				
				// add edges to the distance graph
				for (TimePoint reference : this.tn.getTimePoints()) {
					// get related constraints
					for (TimePointConstraint constraint : this.tn.getConstraints(reference)) {
						// get target
						TimePoint target = constraint.getTarget();
						// get bounds
						long max = constraint.getDistanceUpperBound();
						long min = -constraint.getDistanceLowerBound();
						// add edges
						this.dgraph.add(reference, target, max);
						this.dgraph.add(target, reference, min);
					}
				}
			}
			break;
		
			// temporal constraint added
			case ADD_REL: {
				// handle request
				AddRelationTemporalNetworkNotification notif = (AddRelationTemporalNetworkNotification) info;
				// add relations
				for (TimePointConstraint constraint : notif.getRels()) {
					// get edges' weights
					long max = constraint.getDistanceUpperBound();
					long min = -constraint.getDistanceLowerBound();
					// add edge to the distance graph
					this.dgraph.add(constraint.getReference(), constraint.getTarget(), max);
					this.dgraph.add(constraint.getTarget(), constraint.getReference(), min);
				}
			}
			break;
			
			// temporal relation deleted 
			case DEL_REL: {
				// handle request
				DelRelationTemporalNetworkNotification notif = (DelRelationTemporalNetworkNotification) info;
				// delete relations
				for (TimePointConstraint constraint : notif.getRels()) {
					// delete constraints in the distance graph
					this.dgraph.delete(constraint.getReference(), constraint.getTarget());
					this.dgraph.delete(constraint.getTarget(), constraint.getReference());
				}
			}
			break;
			
			// time point deleted
			case DEL_TP: {
				// handle request
				DelTimePointTemporalNetworkNotification notif = (DelTimePointTemporalNetworkNotification) info;
				// delete time points
				for (TimePoint point : notif.getPoints()) {
					// delete node from the distance graph
					this.dgraph.delete(point);
				}
			}
			break;
			
			// time point added 
			case ADD_TP: {
				// handle request
				AddTimePointTemporalNetworkNotification notif = (AddTimePointTemporalNetworkNotification) info;
				// get added time points
				for (TimePoint point : notif.getPoints()) {
					// add the point to the distance graph
					this.dgraph.add(point);
					
					// add edges w.r.t. the origin
					TimePointConstraint constraint = this.tn.getConstraintFromOrigin(point);
					long max = constraint.getDistanceUpperBound();
					long min = -constraint.getDistanceLowerBound();
					this.dgraph.add(constraint.getReference(), constraint.getTarget(), max);
					this.dgraph.add(constraint.getTarget(), constraint.getReference(), min);
					
					// add edges w.r.t. the horizon
					constraint = this.tn.getConstraintToHorizon(point);
					max = constraint.getDistanceUpperBound();
					min = -constraint.getDistanceLowerBound();
					this.dgraph.add(constraint.getReference(), constraint.getTarget(), max);
					this.dgraph.add(constraint.getTarget(), constraint.getReference(), min);
				}
				
				// it is not necessary to update information yet
				this.toPropagate = false;
			}
			break;
			
			// other	
			default: {
				throw new NotificationPropagationFailureException("[" + this.getClass().getName() + "]: Unknown notification received type= " + info.getType());
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String matrix = "[Minimum Distance Matrix] - Computed by Floyd-Warshall Algorithm\n";
		if (this.toPropagate) {
			// not update distance information
			matrix += "Not Updated Distance Information";
		} else {
			for (TimePoint i : this.tn.getTimePoints()) {
				for (TimePoint j : this.tn.getTimePoints()) {
					matrix += "\t" + this.distance[i.getId()][j.getId()];
				}
				matrix += "\n";
			}
		}
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
		switch (query.getType()) 
		{
			// handle time point bound query
			case TP_BOUND : {
				// get query
				TimePointBoundQuery tpBoundQuery = (TimePointBoundQuery) query;
				// get time point
				TimePoint point = tpBoundQuery.getTimePoint();
				// get distance between the origin and the time point
				long[] distance = this.getDistance(this.tn.getOriginTimePoint(), point); 
				// set information 
				tpBoundQuery.setLb(distance[0]);
				tpBoundQuery.setUb(distance[1]);
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
				tpDistanceQuery.setDistance(distance);
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
				long[] distance = this.getDistance(tp, this.tn.getHorizionTimePoint());
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
		
		// get distance information
		return new long[] { 
				-this.distance[tp2.getId()][tp1.getId()],	// lower bound
				this.distance[tp1.getId()][tp2.getId()]		// upper bound
		};
	}
	
	/**
	 * 
	 */
	private void compute() {
	
		// check size of the distance graph
		int size = this.dgraph.size();
		// initialize distance matrix
		this.distance = new long[size][size];
		
		// initialize distances to "infinity"
		for (TimePoint i : this.dgraph.getPoints()) {
			for (TimePoint j : this.dgraph.getPoints()) {
				// initialize
				if (i.equals(j)) {
					this.distance[i.getId()][j.getId()] = 0;
				}
				else {
					this.distance[i.getId()][j.getId()] = this.tn.getHorizon() + 1;
				}
			}
		}
		
		// initialize distances using the weights of the distance graph's edges
		for (TimePoint point : this.dgraph.getPoints()) {
			// get adjacent points
			for (TimePoint adj : this.dgraph.getAdjacents(point)) {
				// set distance weight
				this.distance[point.getId()][adj.getId()] = this.dgraph.getEdgeWeight(point, adj);
			}
		}
		
		// compute minimum distances
		for (TimePoint k : this.dgraph.getPoints()) {
			// compute shortest paths using intermediate points
			for (TimePoint i : this.dgraph.getPoints()) {
				for (TimePoint j : this.dgraph.getPoints()) {
					
					// compute the path from i to j through k
					long path = this.distance[i.getId()][k.getId()] + this.distance[k.getId()][j.getId()];
					// compare computed distance with the direct path
					if (this.distance[i.getId()][j.getId()] > path) {
						
						// update distance
						this.distance[i.getId()][j.getId()] = path;
					}
				}
			}
		}
		
		// update propagation counter
		this.propagationCounter++;
		// set propagation flag
		this.toPropagate = false;
	}
	

//	/**
//	 * 
//	 * @param tp
//	 */
//	private void addTimePoint(TimePoint tp) 
//	{
//		// check time point's id w.r.t. distance matrix's size
//		if (this.tn.size() >= this.distance.length) {
//			// resize matrix
//			this.resize((this.tn.size() - this.distance.length) + 1);
//		}
//		
//		// current size
//		int size = this.distance.length;
//		// initialize sub-matrix
//		for (int index = 0; index < size; index++) {
//			if (index == tp.getId()) {
//				// set distance
//				this.distance[tp.getId()][index] = 0; 
//			}
//			else {
//				// initialize distance
//				this.distance[tp.getId()][index] = this.tn.getHorizon();
//				this.distance[index][tp.getId()] = this.tn.getHorizon();
//			}
//		}
//		
//		// set distance between the origin and the new time point
//		TimePointConstraint originToTp = this.tn.getConstraintFromOrigin(tp);
//		this.distance[originToTp.getReference().getId()][originToTp.getTarget().getId()] = originToTp.getDistanceUpperBound();
//		this.distance[originToTp.getTarget().getId()][originToTp.getReference().getId()] = -originToTp.getDistanceLowerBound();
//		
//		// set distance between the horizon and the new time point
//		TimePointConstraint tpToHorizon = this.tn.getConstraintToHorizon(tp);
//		this.distance[tpToHorizon.getReference().getId()][tpToHorizon.getTarget().getId()] = tpToHorizon.getDistanceUpperBound();
//		this.distance[tpToHorizon.getTarget().getId()][tpToHorizon.getReference().getId()] = -tpToHorizon.getDistanceLowerBound();
//	}
	
//	/**
//	 * Resizes distance matrix.
//	 * This method is called every time the number of time points in 
//	 * the network exceeds the matrix's size.
//	 * 
//	 * The method takes as input the difference between the current size
//	 * and the id of the time point created.
//	 * 
//	 * The resize delta is 1/3 of the current matrix size
//	 * 
//	 * @param diff
//	 */
//	private void resize(int diff) {
//		// get matrix current size
//		int size = this.distance.length;
//		int delta = size / 3;
//		// make a copy of the current matrix
//		long[][] distanceBackUp = this.distance;
//		// create a larger distance matrix
//		int nextSize = size + diff + delta;
//		this.distance = new long[nextSize][nextSize];
//		// copy data
//		for (int i= 0; i < distanceBackUp.length; i++) {
//			for (int j = 0; j < distanceBackUp.length; j++) {
//				this.distance[i][j] = distanceBackUp[i][j];
//			}
//		}
//	}
}
