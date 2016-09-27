package it.uniroma3.epsl2.framework.time.tn.stn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.time.tn.TemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.DistanceConstraintNotFoundException;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.IntervalDisjunctionException;
import it.uniroma3.epsl2.framework.time.tn.ex.TimePointNotFoundException;
import it.uniroma3.epsl2.framework.time.tn.stnu.ex.UnableToHandleContingentConstraintsException;

/**
 * 
 * @author anacleto
 *
 */
public final class SimpleTemporalNetwork extends TemporalNetwork 
{
	private Map<Integer, TimePoint> points;
	private Map<TimePoint, Map<TimePoint, List<TimePointConstraint>>> constraints;
	private Map<TimePoint, Map<TimePoint, TimePointConstraint>> edges;
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	protected SimpleTemporalNetwork(long origin, long horizon) {
		super(origin, horizon);
		// initialize data structure
		this.points = new HashMap<Integer, TimePoint>();
		this.edges = new HashMap<TimePoint, Map<TimePoint, TimePointConstraint>>();
		this.constraints = new HashMap<TimePoint, Map<TimePoint, List<TimePointConstraint>>>();
		
		// add the origin to the network
		this.points.put(this.tpOrigin.getId(), this.tpOrigin);
		this.edges.put(this.tpOrigin, new HashMap<TimePoint, TimePointConstraint>());
		this.constraints.put(this.tpOrigin, new HashMap<TimePoint, List<TimePointConstraint>>());
		
		// add the horizon to the network
		this.points.put(this.tpHorizion.getId(), this.tpHorizion);
		this.edges.put(this.tpHorizion, new HashMap<TimePoint, TimePointConstraint>());
		this.constraints.put(this.tpHorizion, new HashMap<TimePoint, List<TimePointConstraint>>());
		
		try {
			// add constraint between origin and the horizon
			this.addConstraint(this.tpOrigin, this.tpHorizion, new long[] {horizon, horizon}, true);
		}
		catch (InconsistentDistanceConstraintException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int size() {
		return this.points.size();
	}
	
	/**
	 * 
	 */
	@Override
	public TimePoint getTimePoint(int id) 
			throws TimePointNotFoundException {
		// check network
		if (!this.points.containsKey(id)) {
			throw new TimePointNotFoundException("The network does not contain any time point with id= " + id);
		}
		// get time point
		return this.points.get(id);
	}
	
	/**
	 * Returns all the time points of the network
	 * 
	 * @return
	 */
	@Override
	public List<TimePoint> getTimePoints() {
		return new ArrayList<TimePoint>(this.points.values());
	}
	
	/**
	 * 
	 */
	@Override
	public List<TimePointConstraint> getConstraints(TimePoint tp) {
		return this.edges.containsKey(tp) ? new ArrayList<>(this.edges.get(tp).values()) : new ArrayList<TimePointConstraint>() ;
	}
	
	/**
	 * 
	 */
	@Override
	public List<TimePointConstraint> getConstraints(TimePoint tp1, TimePoint tp2) {
		List<TimePointConstraint> list = new ArrayList<>();
		if (this.edges.containsKey(tp1) && this.edges.get(tp1).containsKey(tp2)) {
			list.add(this.edges.get(tp1).get(tp2));
		}
		return list;
	}
	
	/**
	 * Returns the distance of the time point w.r.t. the network origin
	 * 
	 * @param point
	 * @return
	 */
	@Override
	public TimePointConstraint getConstraintFromOrigin(TimePoint point) {
		// get requirement constraint
		return this.edges.get(this.tpOrigin).get(point);
	}
	
	/**
	 * 
	 */
	@Override
	public TimePointConstraint getConstraintToHorizon(TimePoint point) {
		// get requirement constraint
		return this.edges.get(point).get(this.tpHorizion);
	}
	
	/**
	 * Returns a textual description of the Simple Temporal
	 * Network instance
	 */
	@Override
	public String toString() {
		String str = "- SimpleTemporalNetwork {\n";
		for (TimePoint tp : this.points.values()) {
			str += "\t" + tp + "\n";
			for (TimePointConstraint rel : this.getConstraints(tp)) {
				str += "\t\t" + rel + "\n";
			}
		}
		str += "}\n";
		return str;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doAddTimePoint(TimePoint tp)
			throws InconsistentDistanceConstraintException 
	{
		// add the point to the network
		this.points.put(tp.getId(), tp);
		
		// create constraint from origin
		TimePointConstraint c0P = this.createConstraint(
				this.tpOrigin, 
				tp, 
				new long[] {
						tp.getDomLb(), 
						tp.getDomUb()
				}, 
				true);
		
		// create constraint to horizon
		TimePointConstraint cPH = this.createConstraint(
				tp, 
				this.tpHorizion, 
				new long[] {
					this.horizon - tp.getDomUb(),
					this.horizon - tp.getDomLb()
				}, 
				true);
		
		// add constraints
		this.edges.get(this.tpOrigin).put(tp, c0P);
		this.edges.put(tp, new HashMap<TimePoint, TimePointConstraint>());
		this.edges.get(tp).put(this.tpHorizion, cPH);
		
		this.constraints.get(this.tpOrigin).put(tp, new ArrayList<TimePointConstraint>());
		this.constraints.get(this.tpOrigin).get(tp).add(c0P);
		
		this.constraints.put(tp, new HashMap<TimePoint, List<TimePointConstraint>>());
		this.constraints.get(tp).put(this.tpHorizion, new ArrayList<TimePointConstraint>());
		this.constraints.get(tp).get(this.tpHorizion).add(cPH);
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRemoveTimePoint(TimePoint tp)
			throws TimePointNotFoundException 
	{
		// check if time point exists
		if (!this.points.containsKey(tp.getId())) {
			throw new TimePointNotFoundException("Time Point id= " + tp.getId() + " not found");
		}
		
		// remove the time point
		this.points.remove(tp.getId());
		// remove outgoing edges
		this.edges.remove(tp);
		// remove incoming edges
		for (TimePoint point : this.edges.keySet()) {
			// check if arc exists
			if (this.edges.get(point).containsKey(tp)) {
				// remove edge
				this.edges.get(point).remove(tp);
			}
		}
		
		// remove all constraints concerning the time point
		this.constraints.remove(tp);
		for (TimePoint point : this.constraints.keySet()) {
			// check if a constraint exists
			if (this.constraints.get(point).containsKey(tp)) {
				// remove
				this.constraints.get(point).remove(tp);
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doAddConstraint(TimePointConstraint c)
			throws InconsistentDistanceConstraintException 
	{
		// check controllability
		if (!c.isControllable()) {
			throw new UnableToHandleContingentConstraintsException("Simple Temporal Network cannot handle contingent constraints " + c);
		}
		
		// check if related time points exits
		if (!this.points.containsKey(c.getReference().getId()) || !this.points.containsKey(c.getTarget().getId())) {
			throw new InconsistentDistanceConstraintException("The network does not contain related time points from= " + c.getReference() + ", to= " + c.getTarget());
		}

		// check if a constraint already exists
		if (!this.constraints.containsKey(c.getReference())) {
			// add constraint
			this.constraints.put(c.getReference(), new HashMap<TimePoint, List<TimePointConstraint>>());
			this.constraints.get(c.getReference()).put(c.getTarget(), new ArrayList<TimePointConstraint>());
			this.constraints.get(c.getReference()).get(c.getTarget()).add(c);
			
			// no edges from "from" node exist
			this.edges.put(c.getReference(), new HashMap<TimePoint, TimePointConstraint>());
			this.edges.get(c.getReference()).put(c.getTarget(), c);
		}
		else if (!this.constraints.get(c.getReference()).containsKey(c.getTarget())) {
			// no edge <from, to> exists 
			this.constraints.get(c.getReference()).put(c.getTarget(), new ArrayList<TimePointConstraint>());
			this.constraints.get(c.getReference()).get(c.getTarget()).add(c);
			// add edge
			this.edges.get(c.getReference()).put(c.getTarget(), c);
		}
		else {
			// compute intersection of all propagated constraints
			TimePointConstraint current = this.edges.get(c.getReference()).get(c.getTarget());
			// compute intersection
			long lb = Math.max(current.getDistanceLowerBound(), c.getDistanceLowerBound());
			long ub = Math.min(current.getDistanceUpperBound(), c.getDistanceUpperBound());
			
			// check if an intersection exists
			if (lb > ub) {
				// error - the STP does not handle disjunction among intervals
				throw new IntervalDisjunctionException("Error while adding constraint. Disjunciton of intervals is not allowed\n- old= " + current + "\n- rel= " + c);
			}
			
			// add constraint
			this.constraints.get(c.getReference()).get(c.getTarget()).add(c);
			// check if a change actually occurs and update the network's edge if needed
			if (lb > current.getDistanceLowerBound() || ub < current.getDistanceUpperBound()) {
				// create "intersecting" constraint to add
				TimePointConstraint intersection = this.createConstraint(c.getReference(), c.getTarget(), new long[] {lb, ub}, true); 
				this.edges.get(intersection.getReference()).put(intersection.getTarget(), intersection);
			}
		}
	}
	
	/**
	 * This method removes the constraint between the source and the target of the 
	 * parameter if such a constraint exists
	 */
	@Override
	protected void doRemoveDistanceConstraint(TimePointConstraint c)
			throws DistanceConstraintNotFoundException 
	{
		// check if constraint exists
		if (this.constraints.containsKey(c.getReference()) && 
				this.constraints.get(c.getReference()).containsKey(c.getTarget()) &&
				this.constraints.get(c.getReference()).get(c.getTarget()).contains(c)) {
			
			// remove constraint
			List<TimePointConstraint> list = this.constraints.get(c.getReference()).get(c.getTarget());
			list.remove(c);
			
			// check remaining constraints
			if (!list.isEmpty()) {
				// compute the new "tighter constraint" if possible
				long lb = Long.MIN_VALUE;
				long ub = Long.MAX_VALUE;
				for (TimePointConstraint i : list) {
					lb = i.getDistanceLowerBound() > lb ? i.getDistanceLowerBound() : lb;
					ub = i.getDistanceUpperBound() < ub ? i.getDistanceUpperBound() : ub;
				}
				
				// check if a change actually occurs
				TimePointConstraint current = this.edges.get(c.getReference()).get(c.getTarget());
				if (lb != current.getDistanceLowerBound() || ub != current.getDistanceUpperBound()) {
					// set tighter constraint
					TimePointConstraint intersection = this.createConstraint(c.getReference(), c.getTarget(), new long[] {lb, ub}, true); 
					// update constraint 
					this.edges.get(c.getReference()).put(c.getTarget(), intersection);
				}
			}
			else {
				// remove constraint
				this.edges.get(c.getReference()).remove(c.getTarget());
				this.constraints.get(c.getReference()).remove(c.getTarget());
			}
		}
		else {
			// the constraint does not exists
			throw new DistanceConstraintNotFoundException("Constraint does not exists " + c);
		}
	}
}
