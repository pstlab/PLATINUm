package it.uniroma3.epsl2.framework.time.tn.solver.apsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;
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
	private DistanceGraph dg;									// distance graph
	private Map<TimePoint, Map<TimePoint, Long>> distance;		// the distance matrix containing the minimum distances between points;
	private boolean toPropagate;								// lazy approach - propagate constraint only when needed
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
		this.dg = new DistanceGraph();
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
			// check condition
			consistent = this.distance.get(tp).get(tp) == 0;
		}
		// get consistency check result
		return consistent;
	}
	
	/**
	 * 
	 */
	@Override
	public void notify(TemporalNetworkNotification info)
			throws NotificationPropagationFailureException 
	{
		// set update flag
		this.toPropagate = true;
		// check notification type
		switch (info.getType()) 
		{
			// temporal network initialized
			case INITIALIZATION: 
			{
				// set temporal horizon
				this.dg.setInfity(this.tn.getHorizon() + 1);
				// add all points to the distance graph
				for (TimePoint point : this.tn.getTimePoints()) {
					// add node to the distance graph
					this.dg.add(point);
				}
				
				// add edges to the distance graph
				for (TimePoint reference : this.tn.getTimePoints()) 
				{
					// get related constraints
					for (TimePointDistanceConstraint constraint : this.tn.getConstraints(reference)) 
					{
						// get target
						TimePoint target = constraint.getTarget();
						// get bounds
						long max = constraint.getDistanceUpperBound();
						long min = -constraint.getDistanceLowerBound();
						// add edges
						this.dg.add(reference, target, max);
						this.dg.add(target, reference, min);
					}
				}
			}
			break;
		
			// temporal constraint added
			case ADD_REL: 
			{
				// handle request
				AddRelationTemporalNetworkNotification notif = (AddRelationTemporalNetworkNotification) info;
				// add relations
				for (TimePointDistanceConstraint constraint : notif.getRels()) 
				{
					// get edges' weights
					long max = constraint.getDistanceUpperBound();
					long min = -constraint.getDistanceLowerBound();
					// add edge to the distance graph
					this.dg.add(constraint.getReference(), constraint.getTarget(), max);
					this.dg.add(constraint.getTarget(), constraint.getReference(), min);
				}
			}
			break;
			
			// temporal relation deleted 
			case DEL_REL: 
			{
				// handle request
				DelRelationTemporalNetworkNotification notif = (DelRelationTemporalNetworkNotification) info;
				// delete relations
				for (TimePointDistanceConstraint constraint : notif.getRels()) 
				{
					// delete constraints in the distance graph
					this.dg.delete(constraint.getReference(), constraint.getTarget());
					this.dg.delete(constraint.getTarget(), constraint.getReference());
				}
			}
			break;
			
			// time point deleted
			case DEL_TP: 
			{
				// handle request
				DelTimePointTemporalNetworkNotification notif = (DelTimePointTemporalNetworkNotification) info;
				// delete time points
				for (TimePoint point : notif.getPoints()) {
					// delete node from the distance graph
					this.dg.delete(point);
				}
			}
			break;
			
			// time point added 
			case ADD_TP: 
			{
				// handle request
				AddTimePointTemporalNetworkNotification notif = (AddTimePointTemporalNetworkNotification) info;
				// get added time points
				for (TimePoint point : notif.getPoints()) 
				{
					// add the point to the distance graph
					this.dg.add(point);
					
					// add edges w.r.t. the origin
					TimePointDistanceConstraint constraint = this.tn.getConstraintFromOrigin(point);
					long max = constraint.getDistanceUpperBound();
					long min = -constraint.getDistanceLowerBound();
					this.dg.add(constraint.getReference(), constraint.getTarget(), max);
					this.dg.add(constraint.getTarget(), constraint.getReference(), min);
					
					// add edges w.r.t. the horizon
					constraint = this.tn.getConstraintToHorizon(point);
					max = constraint.getDistanceUpperBound();
					min = -constraint.getDistanceLowerBound();
					this.dg.add(constraint.getReference(), constraint.getTarget(), max);
					this.dg.add(constraint.getTarget(), constraint.getReference(), min);
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
	public String toString() 
	{
		String matrix = "[Minimum Distance Matrix] - Computed by Floyd-Warshall Algorithm\n";
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
	public void process(TimePointQuery query) 
	{
		// check query type 
		switch (query.getType()) 
		{
			// handle time point bound query
			case TP_BOUND : 
			{
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
			case TP_DISTANCE : 
			{
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
			case TP_DISTANCE_FROM_ORIGIN : 
			{
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
			case TP_DISTANCE_TO_HORIZON : 
			{
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
	protected long[] getDistance(TimePoint tp1, TimePoint tp2) 
	{
		// compute minimal minimal network if needed
		if (this.toPropagate) {
			// clean distance matrix's data
			this.compute();
		}
		
		return new long[] {
			-this.distance.get(tp2).get(tp1),		// lower bound
			this.distance.get(tp1).get(tp2)			// upper bound
		};
	}
	
	/**
	 * 
	 */
	private void compute() 
	{
		// check size of the distance graph
		this.distance = new HashMap<TimePoint, Map<TimePoint, Long>>();
		
		// initialize distances to "infinity"
		for (TimePoint i : this.dg.getPoints()) 
		{
			this.distance.put(i, new HashMap<TimePoint, Long>());
			for (TimePoint j : this.dg.getPoints()) 
			{
				// initialize
				if (i.equals(j)) {
					this.distance.get(i).put(j, 0l);
				}
				else {
					this.distance.get(i).put(j, this.tn.getHorizon() + 1);
				}
			}
		}
		
		// initialize distances using the weights of the distance graph's edges
		for (TimePoint point : this.dg.getPoints()) 
		{
			// get adjacent points
			for (TimePoint adj : this.dg.getAdjacents(point)) 
			{
				// set distance weight
				this.distance.get(point).put(adj, this.dg.getEdgeWeight(point, adj));
			}
		}
		
		// compute minimum distances
		for (TimePoint k : this.dg.getPoints()) 
		{
			// compute shortest paths using intermediate points
			for (TimePoint i : this.dg.getPoints()) 
			{
				for (TimePoint j : this.dg.getPoints()) 
				{
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
}
