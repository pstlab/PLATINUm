package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceEventSchedule extends FlawSolution 
{
	private List<ResourceEvent<?>> schedule;			// schedule of consumption and production events
	
	/**
	 * 
	 * @param overflow
	 * @param schedule
	 * @param cost
	 */
	protected ResourceEventSchedule(ReservoirOverflow overflow, List<ResourceEvent<?>> schedule, double cost) {
		super(overflow, cost);
		this.schedule = new ArrayList<>(schedule);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent<?>> getSchedule() {
		return new ArrayList<>(this.schedule);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"type\": \"RESOURCE_EVENT_SCHEDULE\", "
				+ "\"schedule\": " + this.schedule+""
			+ "}";
	}
	
}
