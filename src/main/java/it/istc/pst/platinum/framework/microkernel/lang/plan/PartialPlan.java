package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class PartialPlan 
{
	private Map<Integer, ComponentBehavior> index;									// behavior index
	private Map<DomainComponent, Set<ComponentBehavior>> behaviors;				// possible behaviors
	private Set<ComponentBehaviorConstraint> constraints;						// possible behavior constraints
	private Map<ComponentBehavior, Set<ComponentBehavior>> temporalDominance;	// data structure to keep track of temporal dominance among decisions
	
	/**
	 * 
	 */
	public PartialPlan() {
		this.index = new HashMap<>();
		this.behaviors = new HashMap<>();
		this.constraints = new HashSet<>();
		this.temporalDominance = new HashMap<>();
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addBehavior(Decision dec) 
	{
		// check if behavior already exists
		if (!this.index.containsKey(dec.getId()))
		{
			// check decision component
			if (!this.behaviors.containsKey(dec.getComponent())) {
				this.behaviors.put(dec.getComponent(), new HashSet<>());
			}
			
			// create behavior 
			ComponentBehavior b = new ComponentBehavior(dec.getValue(), dec.getEnd(), dec.getDuration());
			this.index.put(dec.getId(), b);
			// add decision to component
			this.behaviors.get(dec.getComponent()).add(b);
			
			// update temporal dominance data structure
			this.temporalDominance.put(b, new HashSet<>());
		}
 	}
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 */
	public void addBehaviorConstraint(RelationType type, Decision reference, Decision target)
	{
		// check if reference exists
		this.addBehavior(reference);
		// check if target exists
		this.addBehavior(target);
		
		// get reference and target behaviors
		ComponentBehavior refBehavior = this.index.get(reference.getId());
		ComponentBehavior targetBehavior = this.index.get(target.getId());
		
		// create behavior constraint
		ComponentBehaviorConstraint c = new ComponentBehaviorConstraint(type, refBehavior, targetBehavior);
		// add constraint the partial plan
		this.constraints.add(c);
		// check relation and set related temporal dominance information
		switch (type) 
		{
			case DURING : {
				// the target decision temporally dominates the reference decision in this case
				if (!this.temporalDominance.containsKey(refBehavior)) {
					this.temporalDominance.put(refBehavior, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(refBehavior).add(targetBehavior);
			}
			break;
			
			case CONTAINS : {
				// the reference decision temporally dominates the target decision in this case
				if (!this.temporalDominance.containsKey(targetBehavior)) {
					this.temporalDominance.put(targetBehavior, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(targetBehavior).add(refBehavior);
			}
			break;
			
			default : {
				// ignore any other case
			}
		}
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addBehaviorConstraint(Relation rel) 
	{
		// check if reference exists
		this.addBehavior(rel.getReference());
		// check if target exists
		this.addBehavior(rel.getTarget());
		
		// get reference and target behaviors
		ComponentBehavior ref = this.index.get(rel.getReference().getId());
		ComponentBehavior target = this.index.get(rel.getTarget().getId());
		
		// create behavior constraint
		ComponentBehaviorConstraint c = new ComponentBehaviorConstraint(rel.getType(), ref, target);
		// add constraint the partial plan
		this.constraints.add(c);
		// check relation and set related temporal dominance information
		switch (rel.getType()) 
		{
			case DURING : {
				// the target decision temporally dominates the reference decision in this case
				if (!this.temporalDominance.containsKey(ref)) {
					this.temporalDominance.put(ref, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(ref).add(target);
			}
			break;
			
			case CONTAINS : {
				// the reference decision temporally dominates the target decision in this case
				if (!this.temporalDominance.containsKey(target)) {
					this.temporalDominance.put(target, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(target).add(ref);
			}
			break;
			
			default : {
				// ignore any other case
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ComponentBehaviorConstraint> getBehaviorConstraints() {
		return new ArrayList<>(this.constraints);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ComponentBehavior> getBehaviors() {
		// behaviors of the partial plan
		List<ComponentBehavior> list = new ArrayList<>();
		// add components' decisions
		for (DomainComponent comp : this.behaviors.keySet()) {
			list.addAll(this.behaviors.get(comp));
		}
		
		// get the list of decisions 
		return list;
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public List<ComponentBehavior> getBehaviors(DomainComponent comp) {
		// behaviors of the partial plan
		List<ComponentBehavior> list = new ArrayList<>();
		// add component's decisions if any
		if (this.behaviors.containsKey(comp)) {
			list.addAll(this.behaviors.get(comp));
		}

		// get the list of decisions
		return list;
	}
	
	/**
	 * This method computes and returns an estimate of the cycle time of the partial plan 
	 * 
	 * @return
	 */
	public double estimateMakespan()
	{
		// estimated cycle time
		double makespan = 0;
		
		// take into account the temporally dominant decisions of the plan
		for (ComponentBehavior b : this.temporalDominance.keySet())
		{
			// check temporal dominance
			if (this.temporalDominance.get(b).isEmpty())
			{
				// get possible end time
				long[] end = b.getEnd();
				// update cycle time information
				makespan = Math.max(makespan, end[0]);
			}
		}
		
		// get plan cycle time
		return makespan;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((behaviors == null) ? 0 : behaviors.hashCode());
		result = prime * result + ((constraints == null) ? 0 : constraints.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartialPlan other = (PartialPlan) obj;
		if (behaviors == null) {
			if (other.behaviors != null)
				return false;
		} else if (!behaviors.equals(other.behaviors))
			return false;
		if (constraints == null) {
			if (other.constraints != null)
				return false;
		} else if (!constraints.equals(other.constraints))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PartialPlan behaviors: " + this.getBehaviors() + "]";
	}
}
