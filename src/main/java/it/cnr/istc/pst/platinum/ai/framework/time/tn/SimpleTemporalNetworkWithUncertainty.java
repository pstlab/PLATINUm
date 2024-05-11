package it.cnr.istc.pst.platinum.ai.framework.time.tn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.IntervalDisjunctionException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.NotCompatibleConstraintsFoundException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.TimePointNotFoundException;

/**
 * 
 * @author alessandro
 *
 */
public final class SimpleTemporalNetworkWithUncertainty extends TemporalNetwork 
{
	private Map<Integer, TimePoint> points;
	// set of all constraints added to the network
	private Map<TimePoint, Map<TimePoint, Set<TimePointDistanceConstraint>>> requirements;
	// contingent links
	private Map<TimePoint, Map<TimePoint, TimePointDistanceConstraint>> contingents;
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	public SimpleTemporalNetworkWithUncertainty(long origin, long horizon) {
		super(origin, horizon);
		
		// initialize data structures
		this.points = new HashMap<>();
		this.requirements = new HashMap<>();
		this.contingents = new HashMap<>();
			
		// add the origin to the network
		this.points.put(this.tpOrigin.getId(), this.tpOrigin);
		this.contingents.put(this.tpOrigin, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.requirements.put(this.tpOrigin, new HashMap<TimePoint, Set<TimePointDistanceConstraint>>());
		
		// add the horizon to the network
		this.points.put(this.tpHorizion.getId(), this.tpHorizion);
		this.contingents.put(this.tpHorizion, new HashMap<TimePoint, TimePointDistanceConstraint>());
		this.requirements.put(this.tpHorizion, new HashMap<TimePoint, Set<TimePointDistanceConstraint>>());
		
		try {
			
			// create constraint
			TimePointDistanceConstraint oh = this.createDistanceConstraint(
					this.tpOrigin, 
					this.tpHorizion, 
					new long[] {horizon,  horizon}, 
					true);
			
			// add constraint
			this.doAddConstraint(oh);
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
			throws TimePointNotFoundException {
		
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
		// get all time points
		return new ArrayList<>(this.points.values());
	}

	/**
	 * Get the list of all constraints concerning the time point.
	 * 
	 * The method returns the list of all requirement and contingent constraints starting from the time point.
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraints(TimePoint point) {

		// set of constraints
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		// check requirement constraints
		if (this.requirements.containsKey(point)) {
			// get all requirement constraints
			for (Set<TimePointDistanceConstraint> outs : this.requirements.get(point).values()) {
				// add constraints
				set.addAll(outs);
			}
		}
		
		// check contingent constraints
		if (this.contingents.containsKey(point)) {
			// get all contingent constraints
			for (TimePointDistanceConstraint contingent : this.contingents.get(point).values()) {
				// add constraints
				set.add(contingent);
			}
		}
		
		// get the list of outgoing constraints
		return new ArrayList<>(set); 
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
		
		// list of constraints between the two time points
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		
		// check contingent constraints
		if (this.contingents.containsKey(tp1) && this.contingents.get(tp1).containsKey(tp2)) {
			
			// add requirement constraint
			set.add(this.contingents.get(tp1).get(tp2));
				
		}
		else if (this.requirements.containsKey(tp1) && this.requirements.get(tp1).containsKey(tp2)) {
			
			// add requirement constraint
			set.addAll(this.requirements.get(tp1).get(tp2));
			
		} else {
			
			// no constraint between time points
		}
		
		
		// get the list
		return new ArrayList<>(set);
	}

	/**
	 * Return the constraint between the origin and the time point.
	 * 
	 * Note that only requirement constraints can be specified between the origin
	 * and a time point.
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraintFromOrigin(TimePoint point) {
		
		// prepare list of constraints
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		// check contingencies
		if (this.contingents.containsKey(this.tpOrigin) && this.contingents.get(this.tpOrigin).containsKey(point)) {
			
			// add contingent constraint
			set.add(this.contingents.get(this.tpOrigin).get(point));
			
		} else if (this.requirements.containsKey(this.tpOrigin) && this.requirements.get(this.tpOrigin).containsKey(point)) {
			
			// add all requirement constraints
			set.addAll(this.requirements.get(this.tpOrigin).get(point));
			
		} else {
			
			// no constraints
		}
		
		// get list of constraints
		return new ArrayList<>(set);
	}
	
	/**
	 * 
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraintToHorizon(TimePoint point) {
		
		// prepare list of constraints
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		// check contingencies
		if (this.contingents.containsKey(point) && this.contingents.get(point).containsKey(this.tpHorizion)) {
			
			// add contingent constraint
			set.add(this.contingents.get(point).get(this.tpHorizion));
			
		} else if (this.requirements.containsKey(point) && this.requirements.get(point).containsKey(this.tpHorizion)) {
			
			// add all requirement constraints
			set.addAll(this.requirements.get(point).get(this.tpHorizion));
			
		} else {
			
			// no constraints
		}
		
		// get list of constraints
		return new ArrayList<>(set);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<TimePointDistanceConstraint> getContingentConstraints() {
		// set of constraints
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		// check time points
		for (TimePoint point : this.points.values()) {
			// add contingent constraints if any
			if (this.contingents.containsKey(point)) {
				// add all (outgoing) contingent constraints
				set.addAll(this.contingents.get(point).values());
			}
		}
		
		// get the list
		return new ArrayList<>(set);
	}
	
	/**
	 * 
	 */
	@Override
	public List<TimePointDistanceConstraint> getConstraints() {
		// set of constraints
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		// check time points
		for (TimePoint point : this.points.values()) {
			
			// add contingent constraints 
			if (this.contingents.containsKey(point)) {
				set.addAll(this.contingents.get(point).values());
			}
			
			// add requirements
			if (this.requirements.containsKey(point)) {
				// check targets
				for (TimePoint target : this.requirements.get(point).keySet()) {
					// add requirements 
					set.addAll(this.requirements.get(point).get(target));
				}
			}
		}

		// get the list
		return new ArrayList<>(set);
	}

	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String str = "{\n"
				+ "\ttype: \"stnu\",\n"
				+ "\tnetwork: [\n";
		
		// print data about points and links
		for (TimePoint tp : this.points.values()) {
			// print data about current time point
			str += "\t\t{\n"
					+ "\t\t\tpoint: " + tp.getId() + ",\n";
			if (this.requirements.containsKey(tp)) {
				str += "\t\t\trequirements: [\n";
				for (Set<TimePointDistanceConstraint> rels : this.requirements.get(tp).values()) {
					for (TimePointDistanceConstraint rel : rels) {
						str += "\t\t\t\t{ point: " + rel.getTarget().getId() + ", lb: " + rel.getDistanceLowerBound() + ", ub: " + rel.getDistanceUpperBound() + "}\n";
					}
				}	
			}
			
			str += "\t\t\t],\n";
			str += "\t\t\tcontingencies: [\n";
			if (this.contingents.containsKey(tp)) {
				for (TimePointDistanceConstraint rel : this.contingents.get(tp).values()) {
						str += "\t\t\t\t{ point: " + rel.getTarget().getId()+ ", lb: " + rel.getDistanceLowerBound() + ", ub: " + rel.getDistanceUpperBound() +"}, \n";
				}	
			}
			
			str += "\t\t\t]\n"
					+ "\t\t},\n";
		}
		
		// close network description
		str += "\t]\n"
				+ "}\n";
		// print network 
		return str;
	}

	/**
	 */
	@Override
	protected void doAddTimePoint(TimePoint tp) 
			throws InconsistentDistanceConstraintException {
		
		// add the point to the network
		this.points.put(tp.getId(), tp);
		// create constraint from the origin
		TimePointDistanceConstraint c0p = this.createDistanceConstraint(
				this.tpOrigin, 
				tp, 
				new long[] {
					tp.getDomLb(), 
					tp.getDomUb()
				}, 
				true);
		
		// add distance constraint
		this.doAddConstraint(c0p);
		
		// create constraint to horizon
		TimePointDistanceConstraint cpH = this.createDistanceConstraint(
				tp, 
				this.tpHorizion, 
				new long[] {
					this.horizon - tp.getDomUb(),
					this.horizon - tp.getDomLb()
				}, 
				true);
		
		// add distance constraint
		this.doAddConstraint(cpH);		
	}

	/**
	 * 
	 */
	@Override
	protected List<TimePointDistanceConstraint> doRemoveTimePoint(TimePoint point) 
	{
		// list of removed constraints
		Set<TimePointDistanceConstraint> removed = new HashSet<>();
		
		// check time point constraints
		if (this.requirements.containsKey(point)) {
			
			// get related network
			Map<TimePoint, Set<TimePointDistanceConstraint>> net = this.requirements.get(point);
			// add constraints to remove
			for (Set<TimePointDistanceConstraint> set : net.values()) {
				// add constraints
				removed.addAll(set);
			}
			
			// remove entry from the network
			this.requirements.remove(point);
			
			// check other associated constraints
			for (TimePoint other : this.requirements.keySet()) {
				// check constraints to the point
				if (this.requirements.get(other).containsKey(point)) {
					
					// get constraints
					removed.addAll(this.requirements.get(other).get(point));
					// remove data
					this.requirements.get(other).remove(point);
				}
			}
		}
		
		// check contingent constraints
		if (this.contingents.containsKey(point)) {
			
			// get constraints
			removed.addAll(this.contingents.get(point).values());
			// clear data 
			this.contingents.remove(point);
			
			// check other constraints
			for (TimePoint other : this.contingents.keySet()) {
				// check constraints to the point
				if (this.contingents.get(other).containsKey(point)) {
					
					// get constraint
					removed.add(this.contingents.get(other).get(point));
					// remove data
					this.contingents.get(other).remove(point);
					
				}
			}
			
		}
		
		// finally remove point
		this.points.remove(point.getId());
		// get the list
		return new ArrayList<>(removed);
	}

	/**
	 * The STNU can handle only one contingent constraint for each  pair of time points.
	 * 
	 * The method throws an exception when trying to override an existing contingent 
	 * constraint either by adding a contingent or a requirement constraint. 
	 */
	@Override
	protected boolean doAddConstraint(TimePointDistanceConstraint constraint) 
			throws InconsistentDistanceConstraintException 
	{
		// change flag
		boolean change = false;
		// get reference and target time points
		TimePoint reference = constraint.getReference();
		TimePoint target = constraint.getTarget();
		
		// check if related time points exits
		if (!this.points.containsKey(reference.getId()) || !this.points.containsKey(target.getId())) {
			// unknown time points
			throw new InconsistentDistanceConstraintException("Unknown time points:\n"
					+ "- reference= " + reference + "\n"
					+ "- target= " + target + "\n");
		}
		
		// check controllability of the constraint to add
		if (constraint.isControllable())
		{
			// check if the definition of a contingent constraint between the two time points
			if (this.contingents.containsKey(reference) && this.contingents.get(reference).containsKey(target)) {
				// a contingent link exists
				throw new NotCompatibleConstraintsFoundException("A contingent constraint already exist between time points:\n"
						+ "- reference time point= " + reference + "\n"
						+ "- target time point= " + target + "\n"
						+ "- constraint= " + constraint + "\n"
						+ "- contingent constraint= " + this.contingents.get(reference).get(target) + "\n");
			}
			
			// get current constraint bounds
			long[] bounds = this.getConstraintBounds(reference, target);
			// check the feasibility of the current constraint
			if (bounds != null && (constraint.getDistanceLowerBound() > bounds[1] || constraint.getDistanceUpperBound() < bounds[0])) {
				// not feasible constraint
				throw new IntervalDisjunctionException("Disjunctive interval bounds are not allowed\n"
						+ "- current bound intersection= [" + bounds[0] + ", " + bounds[1] + "]\n"
						+ "- constraint bounds= [" + constraint.getDistanceLowerBound() + ", " + constraint.getDistanceUpperBound() + "]\n"
						+ "- constraint= " + constraint + "\n");
			}
			
			
			// check structures
			if (!this.requirements.containsKey(reference)) {
				this.requirements.put(reference, new HashMap<>());
			}
			
			if (!this.requirements.get(reference).containsKey(target)) {
				this.requirements.get(reference).put(target, new HashSet<>());
			}
			
			// the requirement constraint can be safely added to the network
			this.requirements.get(reference).get(target).add(constraint);
			// check if the new constraint entail a change in the distance bound between the time points
			change = bounds == null || constraint.getDistanceLowerBound() > bounds[0] || 
					constraint.getDistanceUpperBound() < bounds[1];
			
		} else {
			
			// check if a contingent constraint already exists
			if (this.contingents.containsKey(reference) && this.contingents.get(reference).containsKey(target)) {
				// contingent constraint cannot be overwritten
				throw new NotCompatibleConstraintsFoundException("Contingent constraints cannot be overwritten:\n"
						+ "- reference time point= " + reference + "\n"
						+ "- target time point= " + target + "\n"
						+ "- existing contingent constraint= " + this.contingents.get(reference).get(target) + "\n"
						+ "- invalid constraint= " + constraint + "\n");
			}
			
			// check structures
			if (!this.contingents.containsKey(reference)) {
				this.contingents.put(reference, new HashMap<>());
			}
			
			// add contingent constraint
			this.contingents.get(reference).put(target, constraint);
			// set change flag
			change = true;
		}
		
		// get flag
		return change;
	}

	/**
	 * 
	 */
	@Override
	protected boolean doRemoveDistanceConstraint(TimePointDistanceConstraint c) {
		
		// change flag
		boolean change = false;
		// get time points involved
		TimePoint reference = c.getReference();
		TimePoint target = c.getTarget();
		
		// check if requirement constraint
		if (c.isControllable()) {
			
			// remove requirement constraint
			if (this.requirements.containsKey(reference) && this.requirements.get(reference).containsKey(target)) {
				
				// check intersection constraint 
				long[] bound1 = this.getConstraintBounds(reference, target);
				// remove requirement constraint
				this.requirements.get(reference).get(target).remove(c);
				// clear data structure if necessary
				if (this.requirements.get(reference).get(target).isEmpty()) {
					// remove entry 
					this.requirements.get(reference).remove(target);
				}
				
				if (this.requirements.get(reference).isEmpty()) {
					// remove entry 
					this.requirements.remove(reference);
				}
				
				// check again intersection constraint 
				long[] bound2 = this.getConstraintBounds(reference, target);
				// set change flag
				change = bound2 == null || bound1[0] != bound2[0] || bound1[1] != bound2[1];
			}
			
		} else {
			
			// remove contingent constraint
			if (this.contingents.containsKey(reference) && this.contingents.get(reference).containsKey(target)) {
			
				// remove contingent constraint
				this.contingents.get(reference).remove(target);
				this.contingents.remove(reference);
				// set change flag
				change = true;
			}
		}

		// get change flag
		return change;
	}

	/**
	 * 
	 */
	@Override
	public long[] getConstraintBounds(TimePoint reference, TimePoint target) {
		
		// set default bounds
		long[] bounds = new long[] {
				0,
				this.horizon
		};
		
		// check constraints between time points
		if (this.contingents.containsKey(reference) && this.contingents.get(reference).containsKey(target)) {
			
			// get constraint
			TimePointDistanceConstraint constraint = this.contingents.get(reference).get(target);
			// get bounds
			bounds[0] = constraint.getDistanceLowerBound();
			bounds[1] = constraint.getDistanceUpperBound();
			
		} else if (this.requirements.containsKey(reference) && this.requirements.get(reference).containsKey(target)) {
			
			// compute bound intersections
			for (TimePointDistanceConstraint constraint : this.requirements.get(reference).get(target)) {
				
				// update intersection
				bounds[0] = Math.max(bounds[0], constraint.getDistanceLowerBound());
				bounds[1] = Math.min(bounds[1], constraint.getDistanceUpperBound());
			}
			
		} else {
			
			// no constraint, no bound can be computed
			bounds = null;
		}
		
		// get bounds
		return bounds;
	}
	
	/**
	 * 
	 */
	@Override
	public void printDiagnosticData() {
		// print temporal network
		System.out.println("Temporal Network:\n"
				+ this + "\n");
	}
	
}
