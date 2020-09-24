package it.istc.pst.platinum.framework.time.tn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.framework.time.tn.ex.DistanceConstraintNotFoundException;
import it.istc.pst.platinum.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.istc.pst.platinum.framework.time.tn.ex.IntervalDisjunctionException;
import it.istc.pst.platinum.framework.time.tn.ex.NotCompatibleConstraintsFoundException;
import it.istc.pst.platinum.framework.time.tn.ex.TimePointNotFoundException;

/**
 * 
 * @author anacleto
 *
 */
public final class SimpleTemporalNetworkWithUncertainty extends TemporalNetwork 
{
	private Map<Integer, TimePoint> points;
	// set of all constraints added to the network
	private Map<TimePoint, Map<TimePoint, List<TimePointDistanceConstraint>>> constraints;
	// requirement edges
	private Map<TimePoint, Map<TimePoint, TimePointDistanceConstraint>> requirements;
	// contingent links
	private Map<TimePoint, Map<TimePoint, TimePointDistanceConstraint>> contingents;
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	public SimpleTemporalNetworkWithUncertainty(long origin, long horizon) 
	{
		super(origin, horizon);
		
		// initialize data structures
		this.points = new HashMap<>();
		this.constraints = new HashMap<>();
		this.requirements = new HashMap<>();
		this.contingents = new HashMap<>();
		
		// add the origin to the network
		this.points.put(this.tpOrigin.getId(), this.tpOrigin);
		this.constraints.put(this.tpOrigin, new HashMap<TimePoint, List<TimePointDistanceConstraint>>());
		this.contingents.put(this.tpOrigin, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.requirements.put(this.tpOrigin, new HashMap<TimePoint, TimePointDistanceConstraint>());
		
		// add the horizon to the network
		this.points.put(this.tpHorizion.getId(), this.tpHorizion);
		this.constraints.put(this.tpHorizion, new HashMap<TimePoint, List<TimePointDistanceConstraint>>());
		this.contingents.put(this.tpHorizion, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.requirements.put(this.tpHorizion, new HashMap<TimePoint, TimePointDistanceConstraint>());
		
		try 
		{
			// create constraint
			TimePointDistanceConstraint oh = this.createDistanceConstraint(
					this.tpOrigin, 
					this.tpHorizion, 
					new long[] {horizon,  horizon}, 
					true);
			// add constraint
			this.addDistanceConstraint(oh);
		} 
		catch (InconsistentDistanceConstraintException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 
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
			throws TimePointNotFoundException 
	{
		// check network 
		if (!this.points.containsKey(id)) {
			throw new TimePointNotFoundException("The network does not contain any time point with id= " + id);
		}
		// get time point
		return this.points.get(id);
	}

	/**
	 * 
	 */
	@Override
	public List<TimePoint> getTimePoints() {
		return new ArrayList<>(this.points.values());
	}

	/**
	 * Get the list of all constraints concerning the time point.
	 * 
	 * The method returns the list of all requirement and 
	 * contingent constraints starting from the time point.
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraints(TimePoint tp) 
	{
		// check constraints between time points
		Map<TimePoint, Map<TimePoint, TimePointDistanceConstraint>> cons = new HashMap<>();
		cons.put(tp, new HashMap<>());
		
		// check requirement constraints
		if (this.requirements.containsKey(tp)) {
			// get all requirement constraints
			for (TimePointDistanceConstraint rc : this.requirements.get(tp).values()) {
				// add entry
				cons.get(tp).put(rc.getTarget(), rc);
			}
		}
		
		// check contingent constraints
		if (this.contingents.containsKey(tp)) {
			// get all contingent constraints
			for (TimePointDistanceConstraint cc : this.contingents.get(tp).values()) {
				// add entry
				cons.get(tp).put(cc.getTarget(), cc);
			}
		}
		
		// get the list extracted constraints
		return new ArrayList<>(cons.get(tp).values()); 
	}

	/**
	 * Get the list of all constraints concerning the two time points.
	 * 
	 * The method returns the list of all requirement or contingent 
	 * constraints concerning between the time points. Indeed only one 
	 * type of constraint is allowed between two time points. Namely two 
	 * time points are connected by a requirement constraint or a contingent
	 * constraint
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraints(TimePoint tp1, TimePoint tp2) {
		List<TimePointDistanceConstraint> list = new ArrayList<>();
		// check requirements 
		if (this.requirements.containsKey(tp1) && this.requirements.get(tp1).containsKey(tp2)) {
			// add requirement constraint
			list.add(this.requirements.get(tp1).get(tp2));
		}
		else if (this.contingents.containsKey(tp1) && this.contingents.get(tp1).containsKey(tp2)) {
			// add requirement constraint
			list.add(this.contingents.get(tp1).get(tp2));
		}
		// get the list
		return list;
	}

	/**
	 * Return the constraint between the origin and the time point.
	 * 
	 * Note that only requirement constraints can be specified between the origin
	 * and a time point.
	 */
	@Override
	public TimePointDistanceConstraint getConstraintFromOrigin(TimePoint point) {
		// get requirement constraint from the origin
		return this.requirements.get(this.tpOrigin).get(point);
	}
	
	/**
	 * 
	 */
	@Override
	public TimePointDistanceConstraint getConstraintToHorizon(TimePoint point) {
		// get requirement constraint to the horizon
		return this.requirements.get(point).get(this.tpHorizion);
	}
	
	/**
	 * This method returns a list of all contingent constraints of the STNU.
	 * 
	 * @return
	 */
	public List<TimePointDistanceConstraint> getContingetConstraints() {
		List<TimePointDistanceConstraint> list = new ArrayList<>();
		for (TimePoint point : this.contingents.keySet()) {
			// add all contingent constraints
			list.addAll(this.contingents.get(point).values());
		}
		// get list of contingent constraints
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String str = "- SimpleTemporalNetworkWithUncertainty {\n";
		for (TimePoint tp : this.points.values()) {
			str += "\t" + tp + "\n";
			for (TimePointDistanceConstraint rel : this.getConstraints(tp)) {
				str += "\t\t" + rel + "\n";
			}
		}
		str += "}\n";
		return str;
	}

	/**
	 */
	@Override
	protected void doAddTimePoint(TimePoint tp) 
			throws InconsistentDistanceConstraintException 
	{
		// add the point to the network
		this.points.put(tp.getId(), tp);
		// create constraint from the origin
		TimePointDistanceConstraint t0 = this.createDistanceConstraint(
				this.tpOrigin, 
				tp, 
				new long[] {
					tp.getDomLb(), 
					tp.getDomUb()
				}, 
				true);
		
		// create constraint to horizon
		TimePointDistanceConstraint t1 = this.createDistanceConstraint(
				tp, 
				this.tpHorizion, 
				new long[] {
					this.horizon - tp.getDomUb(),
					this.horizon - tp.getDomLb()
				}, 
				true);
		
		// add constraints
		this.requirements.get(this.tpOrigin).put(tp, t0);
		this.requirements.put(tp, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.contingents.put(tp, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.requirements.get(tp).put(this.tpHorizion, t1);
		
		// add general constraints
		this.constraints.get(this.tpOrigin).put(tp, new ArrayList<TimePointDistanceConstraint>());
		this.constraints.get(this.tpOrigin).get(tp).add(t0);
		this.constraints.put(tp, new HashMap<TimePoint, List<TimePointDistanceConstraint>>());
		this.constraints.get(tp).put(this.tpHorizion, new ArrayList<TimePointDistanceConstraint>());
		this.constraints.get(tp).get(this.tpHorizion).add(t1);
		
		// export temporal network graph
		this.exportTemporalNetworkGraph();
	}

	/**
	 * 
	 */
	@Override
	protected List<TimePointDistanceConstraint> doRemoveTimePoint(TimePoint tp) 
			throws TimePointNotFoundException 
	{
		// check if time point exists
		if (!this.points.containsKey(tp.getId())) {
			throw new TimePointNotFoundException("Time Point id= " + tp.getId() +" not found");
		}
		
		
		// list of removed constraints
		List<TimePointDistanceConstraint> removed = new ArrayList<>();
		
		// remove time point related relations (outgoing)
		Collection<TimePoint> pList = new ArrayList<>(this.constraints.get(tp).keySet()); 
		for (TimePoint p : pList) {
			// get constraint
			List<TimePointDistanceConstraint> cList = new ArrayList<>(this.constraints.get(tp).get(p)); 
			for (TimePointDistanceConstraint c : cList) {
				try {
					// remove constraint
					this.doRemoveDistanceConstraint(c);
					// add constraint to the list of removed ones
					removed.add(c);
				}
				catch (DistanceConstraintNotFoundException ex) {
					error(ex.getMessage());
				}
			}
		}
		
		// remove time point relations (incoming)
		for (TimePoint p : this.constraints.keySet()) {
			// check node
			if (!p.equals(tp)) 
			{
				// check if contains data
				if (this.constraints.get(p).containsKey(tp)) 
				{ 
					// get constraints to remove
					List<TimePointDistanceConstraint> cList = new ArrayList<>(this.constraints.get(p).get(tp));
					for (TimePointDistanceConstraint c : cList) {
						try {
							// remove constraint
							this.doRemoveDistanceConstraint(c);
							// add constraitn to the list of removed ones
							removed.add(c);
						}
						catch (DistanceConstraintNotFoundException ex) {
							error(ex.getMessage());
						}
					}
				}
			}
		}
		
		// now remove the timepoint and clera related data structures
		this.points.remove(tp.getId());
		// remove all outgoing edges concerning the time point
		this.requirements.remove(tp);
		this.contingents.remove(tp);
		
		
		// remove all incoming edges concerning the time point
		for (TimePoint point : this.requirements.keySet()) {
			if (this.requirements.get(point).containsKey(tp)) {
				// remove
				this.requirements.get(point).remove(tp);
			}
		}
		for (TimePoint point : this.contingents.keySet()) {
			if (this.contingents.get(point).containsKey(tp)) {
				// remove
				this.contingents.get(point).remove(tp);
			}
		}
		
		// get the list of 
		// remove all general constraints concerning the time point
		this.constraints.remove(tp);
		// check "incoming" constraints
		for (TimePoint point : this.constraints.keySet()) {
			// check if a constraint exists
			if (this.constraints.get(point).containsKey(tp)) {
				// remove entry
				this.constraints.get(point).remove(tp);
			}
		}
		
		// export temporal network graph
		this.exportTemporalNetworkGraph();
		
		// get the list
		return removed;
	}

	/**
	 * The STNU can handle only one contingent constraint for each  pair of time points.
	 * 
	 * The method throws an exception when trying to override an existing contingent 
	 * constraint either by adding a contingent or a requirement constraint. 
	 */
	@Override
	protected boolean doAddConstraint(TimePointDistanceConstraint c) 
			throws InconsistentDistanceConstraintException 
	{
		// change flag
		boolean change = false;
		// get time point involved
		TimePoint reference = c.getReference();
		TimePoint target = c.getTarget();
		
		// check if related time points exits
		if (!this.points.containsKey(reference.getId()) || 
				!this.points.containsKey(target.getId())) 
		{
			// unknown time points
			throw new InconsistentDistanceConstraintException("Unknown time points [from= " + reference + ", to= " + target + "]");
		}
		
		// check controllability of the constraint to add
		if (c.isControllable())
		{
			// check if a contingent link exists between time points
			if (this.contingents.containsKey(reference) && 
					this.contingents.get(reference).containsKey(target)) 
			{
				// a contingent link exists
				throw new NotCompatibleConstraintsFoundException("Impossible to add the requirement constraint, a contingent constraint already exists between tp= " + c.getReference() + " and tp= " + c.getTarget());
			}
			
			// set structures
			if (!this.constraints.containsKey(reference)) {
				// add entries
				this.constraints.put(reference, new HashMap<TimePoint, List<TimePointDistanceConstraint>>());
			}
			
			if (!this.constraints.get(reference).containsKey(target)) {
				// setup data structures
				this.constraints.get(reference).put(target, new ArrayList<TimePointDistanceConstraint>());
			}
			
			// add constraint
			this.constraints.get(reference).get(target).add(c);
			
			// setup requirements
			if (!this.requirements.containsKey(reference)) {
				// setup data structure
				this.requirements.put(reference, new HashMap<TimePoint, TimePointDistanceConstraint>());
			}
			
 			// check if a requirement constraint already exists between points			
			if (this.requirements.get(reference).containsKey(target)) 
			{
				// compute intersection of all propagated constraints
				TimePointDistanceConstraint current = this.requirements.get(reference).get(target);
				// compute intersection
				long lb = Math.max(current.getDistanceLowerBound(), c.getDistanceLowerBound());
				long ub = Math.min(current.getDistanceUpperBound(), c.getDistanceUpperBound());
				// check if an intersection exists
				if (lb > ub) {
					// remove added constraint as it leads to inconsistency
					this.constraints.get(reference).get(target).remove(c);
					// clear data structure
					if (this.constraints.get(reference).get(target).isEmpty()) {
						this.constraints.get(reference).remove(target);
					}
					
					// clear data structure
					if (this.constraints.get(reference).isEmpty()) {
						this.constraints.remove(reference);
					}
					
					// error - the STP(U) does not handle disjunction among intervals
					throw new IntervalDisjunctionException("Error while adding constraint. Disjunciton of intervals is not allowed\n- old= " + current + "\n- rel= " + c);
				}
				
				
				// check if a change actually occurs and update the network's edge 
				if (lb > current.getDistanceLowerBound() || 
						ub < current.getDistanceUpperBound()) {
					
					// create distance intersection constraint 
					TimePointDistanceConstraint intersection = this.createDistanceConstraint(
							reference, 
							target, 
							new long[] {
								lb,				// interesection lower bound 
								ub				// intersection upper bound
							}, 
							true); 
					
					// replace the distance constraint between the two time points
					this.requirements.get(reference).put(target, intersection);
					// set flag
					change = true;
				}
			}
			else 
			{
				// only one requirement constraints between the two time points
				this.requirements.get(reference).put(target, c);
				// set flag
				change = true;
			}
		}
		else // contingent link to add
		{
			// check if requirement constraint exists
			if (this.requirements.containsKey(reference) && 
					this.requirements.get(reference).containsKey(target)) {
				// constraint already exists
				throw new NotCompatibleConstraintsFoundException("Impossible to add the contingent constraint, some requirement constraints already exist between time points tp= " + c.getReference() + ", and tp= " + c.getTarget());
			}
			
			// set structures
			if (!this.constraints.containsKey(reference)) {
				// add entries
				this.constraints.put(reference, new HashMap<TimePoint, List<TimePointDistanceConstraint>>());
			}
			
			if (!this.constraints.get(reference).containsKey(target)) {
				// setup data structures
				this.constraints.get(reference).put(target, new ArrayList<TimePointDistanceConstraint>());
			}
			
			// add constraint
			this.constraints.get(reference).get(target).add(c);
			
			
			
			// check contingent links
			if (!this.contingents.containsKey(reference)) {
				this.contingents.put(reference, new HashMap<TimePoint, TimePointDistanceConstraint>());
			}
			
			// add contingent distance constraint
			this.contingents.get(reference).put(target, c);
			
			// set flag
			change = true;
		}
		
		
		// export updated temporal network
		if (change) {
			// export temporal network graph
			this.exportTemporalNetworkGraph();
		}
		
		// get flag
		return change;
	}

	/**
	 * 
	 */
	@Override
	protected void doRemoveDistanceConstraint(TimePointDistanceConstraint c) 
			throws DistanceConstraintNotFoundException 
	{
		// get time points involved
		TimePoint reference = c.getReference();
		TimePoint target = c.getTarget();
		
		// check if constraint exists
		if (this.constraints.containsKey(reference) && 
				this.constraints.get(reference).containsKey(target) && 
				this.constraints.get(reference).get(target).contains(c)) 
		{
			// remove constraint
			this.constraints.get(reference).get(target).remove(c);
			// check if the constraint to remove is controllable or not
			if (c.isControllable()) 
			{
				// check constraints
				if (!this.constraints.containsKey(reference) || 
						!this.constraints.get(reference).containsKey(target) || 
						this.constraints.get(reference).get(target).isEmpty()) {

					// remove requirement constraint
					this.requirements.get(reference).remove(target);
				}
				else 
				{
					// compute the current intersection constraint
					long lb = Long.MIN_VALUE + 1;
					long ub = Long.MAX_VALUE - 1;
					
					// compute intersection among remaining distance constraints
					for (TimePointDistanceConstraint cons : this.constraints.get(reference).get(target)) {
						lb = Math.max(cons.getDistanceLowerBound(), lb);
						ub = Math.min(cons.getDistanceUpperBound(), ub);
					}
					
					// check if a change actually occurs
					TimePointDistanceConstraint current = this.requirements.get(reference).get(target);
					// check if distance bounds have changed
					if (lb != current.getDistanceLowerBound() || 
							ub != current.getDistanceUpperBound()) 
					{
						// replace the requirement constraint with the computed intersection bounds
						TimePointDistanceConstraint intersection = this.
								createDistanceConstraint(
										reference, 
										target, 
										new long[] {
											lb,				// intersection lower bound 
											ub				// interesection upper bound
										}, 
										true);
						
						// actually replace intersection constraint
						this.requirements.get(reference).put(target, intersection);
					}
				}
			}
			else 
			{
				// check if the current contingent constraint is to be removed
				if (this.contingents.get(reference).get(target).equals(c)) 
				{
					// remove constraint
					this.contingents.get(reference).remove(target);
					// check general constraints
					if (this.constraints.containsKey(reference) && 
							this.constraints.get(reference).containsKey(target))
					{
						// set new constraint if any
						List<TimePointDistanceConstraint> list = this.constraints.get(reference).get(target);
						// only one contingent link is expected between two time points
						for (TimePointDistanceConstraint cont : list) {
							// check type
							if (!cont.isControllable()) {
								// set the constraint as the currently active contingent constraint
								this.contingents.get(reference).put(target, cont);
								break;
							}
						}
					}
				}
			}
		}
		
		
		// export temporal network graph
		this.exportTemporalNetworkGraph();
	}
	
	
	/**
	 * 
	 */
	private void exportTemporalNetworkGraph() 
	{
		// check size 
		if (this.getTimePoints().size() < 50) 
		{
			// export a partila view of temporal network
			String str = "digraph stnu {\n";
			str += "\trankdir=LR;\n";
			str += "\tnode [with=.2, height=.2, fontsize=5, shape=circle, style=filled, fillcolor=white];"; 
			
			// print edges
			for (TimePoint source : this.constraints.keySet()) {
				// get outgoing requirement constraints
				for (TimePointDistanceConstraint requirement : this.requirements.get(source).values()) {
					// add edge
					str += "\t" + source.getId() + " -> " + requirement.getTarget().getId() + " [fontsize=8, arrowsize= .5, label = \"[" + requirement.getDistanceLowerBound() + "," + requirement.getDistanceUpperBound() + "]\"];\n";
				}
				
				// check contingent constraints
				if (this.contingents.containsKey(source)) {
					// get outgoing contingent constraints
					for (TimePointDistanceConstraint requirement : this.contingents.get(source).values()) {
						// add edge
						str += "\t" + source.getId() + " -> " + requirement.getTarget().getId() + " [fontsize=8, arrowsize= .5, style= dotted, label = \"[" + requirement.getDistanceLowerBound() + "," + requirement.getDistanceUpperBound() + "]\"];\n";
					}
				}
			}
			
			// close 
			str += "\n}\n\n";
			
			try 
			{
				File pdlFile = new File("stnu.dot");
				try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
					// write file
					writer.write(str);
				}
			}
			catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}			
	}
}
