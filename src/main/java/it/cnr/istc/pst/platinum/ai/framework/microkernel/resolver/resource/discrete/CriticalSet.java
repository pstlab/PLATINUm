package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.discrete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.DiscreteResource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.RequirementResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class CriticalSet extends Flaw implements Comparable<CriticalSet>
{
	private Set<RequirementResourceEvent> events;				// requirement events of a critical set
	
	/**
	 * 
	 * @param id
	 * @param resource
	 */
	protected CriticalSet(int id, DiscreteResource resource) {
		super(id, resource, FlawType.DISCRETE_OVERFLOW);
		this.events = new HashSet<>();
	}
	
	/**
	 * 
	 * @param event
	 */
	public void addRequirementDecision(RequirementResourceEvent event) {
		this.events.add(event);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RequirementResourceEvent> getRequirementEvents() {
		return new ArrayList<>(this.events);
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAmountOfRequirement() {
		double amount = 0;
		for (RequirementResourceEvent event : this.events) {
			amount += event.getAmount();
		}
		// get total amount
		return amount;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CriticalSet other = (CriticalSet) obj;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(CriticalSet o) {
		// compare the amount of required resource
		return this.getAmountOfRequirement() > o.getAmountOfRequirement()? -1 : this.getAmountOfRequirement() < o.getAmountOfRequirement() ? 1 : 0;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[CriticalSet requirement: " + this.getAmountOfRequirement() + ", conflicting events: " + this.events + "]";
	}
}
