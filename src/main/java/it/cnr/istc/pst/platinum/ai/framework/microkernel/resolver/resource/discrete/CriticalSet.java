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
//	private double minCapacity;									// minimum level of resource capacity
//	private double maxCapacity;									// maximum level of resource capacity
//	private Set<RequirementResourceProfileSample> samples;		// profile samples composing the critical set
	
	private Set<RequirementResourceEvent> events;				// requirement events of a critical set
	
	/**
	 * 
	 * @param id
	 * @param resource
	 */
	protected CriticalSet(int id, DiscreteResource resource) {
		super(id, resource, FlawType.RESOURCE_OVERFLOW);
		this.events = new HashSet<>();
		
//		this.samples = new HashSet<>();
//		this.minCapacity = resource.getMinCapacity();
//		this.maxCapacity = resource.getMaxCapacity();
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
	
	
//	/**
//	 * 
//	 * @return
//	 */
//	public double getMaxCapacity() {
//		return maxCapacity;
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public double getMinCapacity() {
//		return minCapacity;
//	}
	
//	/**
//	 * 
//	 * @return
//	 */
//	public long getTotalRequirement() {
//		// initialize the total amount of resource required
//		long total = 0;
//		for (RequirementResourceProfileSample sample : this.samples) {
//			// update the total
//			total += sample.getAmount();
//		}
//		
//		// get computed amount
//		return total;
//	}
	
//	/**
//	 * 
//	 * @return
//	 */
//	public List<RequirementResourceProfileSample> getSamples() {
//		// get the list of samples 
//		return new ArrayList<>(this.samples);
//	}
//	
//	/**
//	 * 
//	 * @param sample
//	 */
//	public void addSample(RequirementResourceProfileSample sample) {
//		this.samples.add(sample);
//	}
	
//	/**
//	 * 
//	 * @param sample
//	 * @return
//	 */
//	public boolean isOverlapping(RequirementResourceProfileSample sample) {
//		// get start time and end time of the current set
//		long start = this.getStartTime();
//		long end = this.getEndTime();
//		
//		// check the start time and the end time of the sample
//		start = Math.max(start, sample.getStart());
//		end = Math.min(end, sample.getEnd());
//		
//		// check overlapping condition;
//		return start < end;
//	}
	
//	/**
//	 * 
//	 * @return
//	 */
//	public long getStartTime() {
//		// initialize the start time 
//		long start = Long.MIN_VALUE + 1;
//		for (RequirementResourceProfileSample sample : this.samples) {
//			// check maximum start time
//			start = Math.max(start, sample.getStart());
//		}
//		
//		// get computed start time of the critical set
//		return start;
//	}
	
//	/**
//	 * 
//	 * @return
//	 */
//	public long getEndTime() {
//		// initialize the end time
//		long end = Long.MAX_VALUE - 1;
//		for (RequirementResourceProfileSample sample : this.samples) {
//			// check minimum end time
//			end = Math.min(end, sample.getEnd());
//		}
//		
//		// get computed end time of the critical set
//		return end;
//	}

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
//		return this.getTotalRequirement() >= o.getTotalRequirement() ? -1 : 1;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[CriticalSet requirement: " + this.getAmountOfRequirement() + ", conflicting events: " + this.events + "]";
	}
}
