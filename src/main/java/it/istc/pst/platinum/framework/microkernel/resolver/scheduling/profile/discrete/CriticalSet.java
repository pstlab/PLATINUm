package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.profile.discrete;

import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceProfileSample;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class CriticalSet extends Flaw implements Comparable<CriticalSet>
{
	private Set<RequirementResourceProfileSample> samples;		// profile samples composing the critical set
	
	/**
	 * 
	 * @param component
	 */
	protected CriticalSet(DomainComponent<?> component) {
		super(component, FlawType.RESOURCE_PEAK);
		this.samples = new HashSet<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTotalRequirement() {
		// initialize the total amount of resource required
		long total = 0;
		for (RequirementResourceProfileSample sample : this.samples) {
			// update the total
			total += sample.getAmount();
		}
		
		// get computed amount
		return total;
	}
	
	/**
	 * 
	 * @param sample
	 */
	public void addSample(RequirementResourceProfileSample sample) {
		this.samples.add(sample);
	}
	
	/**
	 * 
	 * @param sample
	 * @return
	 */
	public boolean isOverlapping(RequirementResourceProfileSample sample) {
		// get start time and end time of the current set
		long start = this.getStartTime();
		long end = this.getEndTime();
		
		// check the start time and the end time of the sample
		start = Math.max(start, sample.getStart());
		end = Math.min(end, sample.getEnd());
		
		// check overlapping condition;
		return start < end;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getStartTime() {
		// initialize the start time 
		long start = Long.MIN_VALUE + 1;
		for (RequirementResourceProfileSample sample : this.samples) {
			// check maximum start time
			start = Math.max(start, sample.getStart());
		}
		
		// get computed start time of the critical set
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getEndTime() {
		// initialize the end time
		long end = Long.MAX_VALUE - 1;
		for (RequirementResourceProfileSample sample : this.samples) {
			// check minimum end time
			end = Math.min(end, sample.getEnd());
		}
		
		// get computed end time of the critical set
		return end;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((samples == null) ? 0 : samples.hashCode());
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
		if (samples == null) {
			if (other.samples != null)
				return false;
		} else if (!samples.equals(other.samples))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(CriticalSet o) {
		// compare the total amount of resource required 
		return this.getTotalRequirement() >= o.getTotalRequirement() ? -1 : 1;
	}
}
