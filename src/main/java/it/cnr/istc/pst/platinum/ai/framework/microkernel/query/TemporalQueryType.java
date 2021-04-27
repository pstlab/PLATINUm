package it.cnr.istc.pst.platinum.ai.framework.microkernel.query;

import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalOverlapQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalPseudoControllabilityQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceToHorizonQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author alessandro
 *
 */
public enum TemporalQueryType 
{
	/**
	 * Check the distance between two time points
	 */
	TP_DISTANCE(TimePointDistanceQuery.class.getName()),
	
	/**
	 * Check the distance of a time point to the temporal origin
	 */
//	TP_DISTANCE_FROM_ORIGIN(TimePointDistanceFromOriginQuery.class.getName()),
	
	/**
	 * Check the distance of a time point to the temporal horizon
	 */
	TP_DISTANCE_TO_HORIZON(TimePointDistanceToHorizonQuery.class.getName()),
	
	/**
	 * Check the schedule of a time point 
	 */
	TP_SCHEDULE(TimePointScheduleQuery.class.getName()),
	
	/**
	 * Check the actual flexible duration of a temporal interval and 
	 * its schedule over time, i.e. compute the flexible start and 
	 * end times of the interval
	 */
	INTERVAL_SCHEDULE(IntervalScheduleQuery.class.getName()),
	
	/**
	 * Check if two flexible temporal intervals overlap
	 */
	INTERVAL_OVERLAP(IntervalOverlapQuery.class.getName()),
	
	/**
	 * Check the flexible distance between two temporal intervals
	 */
	INTERVAL_DISTANCE(IntervalDistanceQuery.class.getName()),
	
	/**
	 * Check if the temporal interval has been squeezed, i.e. if the
	 * actual duration of the interval is tighter than the "domain"
	 * duration
	 */
	INTERVAL_PSEUDO_CONTROLLABILITY(IntervalPseudoControllabilityQuery.class.getName());
	
//	/**
//	 * Compute the makespan of the temporal network by taking into account
//	 * controllable events of the network.
//	 * 
//	 * It is possible to specify a subset of temporal intervals for computing the 
//	 * makespan on.
//	 */
//	COMPUTE_MAKESPAN(ComputeMakespanQuery.class.getName());
	
	// query class name
	private String className;
	
	/**
	 * 
	 * @param name
	 */
	private TemporalQueryType(String name) {
		this.className = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQueryClassName() {
		return this.className;
	}
}
