package it.istc.pst.platinum.deliberative.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class PartialPlan 
{
	private Map<DomainComponent, Set<Decision>> decisions;			// decisions of the partial plan
	private Set<Relation> relations;								// relations between decisions of the partial plan
	private Map<Decision, Set<Decision>> temporalDominance;			// data structure to keep track of temporal dominance among decisions
	
	/**
	 * 
	 */
	protected PartialPlan() {
		this.decisions = new HashMap<>();
		this.relations = new HashSet<>();
		this.temporalDominance = new HashMap<>();
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addDecision(Decision dec) {
		// check decision component
		if (!this.decisions.containsKey(dec.getComponent())) {
			this.decisions.put(dec.getComponent(), new HashSet<>());
		}
		
		// add decision to component
		this.decisions.get(dec.getComponent()).add(dec);
		
		// update temporal dominance data structure
		this.temporalDominance.put(dec, new HashSet<>());
 	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addRelation(Relation rel) 
	{
		// add relation to the partial plan
		this.relations.add(rel);
		
		// get reference decision
		Decision reference = rel.getReference();
		// get target decision
		Decision target = rel.getTarget();
		// check relation and set related temporal dominance information
		switch (rel.getType()) 
		{
			case DURING : {
				// the target decision temporally dominates the reference decision in this case
				if (!this.temporalDominance.containsKey(reference)) {
					this.temporalDominance.put(reference, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(reference).add(target);
			}
			break;
			
			case CONTAINS : {
				// the reference decision temporally dominates the target decision in this case
				if (!this.temporalDominance.containsKey(target)) {
					this.temporalDominance.put(target, new HashSet<>());
				}
				
				// add temporal dominance
				this.temporalDominance.get(target).add(reference);
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
	public List<Relation> getRelations() {
		return new ArrayList<>(this.relations);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisions() {
		// decisions of the partial plan
		List<Decision> list = new ArrayList<>();
		// add components' decisions
		for (DomainComponent comp : this.decisions.keySet()) {
			list.addAll(this.decisions.get(comp));
		}
		
		// get the list of decisions 
		return list;
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public List<Decision> getDecisions(DomainComponent comp) {
		// decisions of the partial plan
		List<Decision> list = new ArrayList<>();
		// add component's decisions if any
		if (this.decisions.containsKey(comp)) {
			list.addAll(this.decisions.get(comp));
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
		for (Decision dec : this.temporalDominance.keySet())
		{
			// check temporal dominance
			if (this.temporalDominance.get(dec).isEmpty())
			{
				// get possible end time
				long[] end = dec.getEnd();
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
		result = prime * result + ((decisions == null) ? 0 : decisions.hashCode());
		result = prime * result + ((relations == null) ? 0 : relations.hashCode());
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
		if (decisions == null) {
			if (other.decisions != null)
				return false;
		} else if (!decisions.equals(other.decisions))
			return false;
		if (relations == null) {
			if (other.relations != null)
				return false;
		} else if (!relations.equals(other.relations))
			return false;
		return true;
	}
}
