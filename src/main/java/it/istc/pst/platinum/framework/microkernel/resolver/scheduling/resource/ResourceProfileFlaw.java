package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.resource;

import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceProfileFlaw extends Flaw 
{
	private Set<ResourceEvent> events;			// resource demand events
	private Set<Decision> peak;					// conflicting decisions
	
	/**
	 * 
	 * @param component
	 */
	protected ResourceProfileFlaw(DomainComponent component) {
		super(component, FlawType.RESOURCE_PEAK);
		this.events = new HashSet<>();
		this.peak = new HashSet<>();
	}
	
	/**
	 * 
	 * @param event
	 */
	public void addEvent(ResourceEvent event) {
		// add event
		this.events.add(event);
		// add related decision
		this.peak.add(event.getDecision());
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Decision> getPeak() {
		// get peak
		return this.peak;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPeakSize() {
		// get number of conflicting decisions
		return this.peak.size();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ResourcePeak size= " + this.peak.size() + "\n- events= " + this.events + "\n]";
	}
}
