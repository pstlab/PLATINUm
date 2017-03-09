package it.uniroma3.epsl2.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.ResourceProfileManager;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(

		resolvers = {
		
				// scheduling resolver
				ResolverType.SV_SCHEDULING_RESOLVER,
//				ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER,
				
				// time-line gap resolver
				ResolverType.SV_GAP_RESOLVER,
				
				// behavior checking resolver
				ResolverType.SV_BEHAVIOR_CHECKING_RESOLVER
		},
		
		view = ComponentViewType.GANTT
)
public final class PrimitiveStateVariable extends StateVariable implements ResourceProfileManager
{
	/**
	 * 
	 * @param name
	 */
	protected PrimitiveStateVariable(String name) {
		super(name, DomainComponentType.SV_PRIMITIVE);
	}

	/**
	 * 
	 */
	@Override
	public long getMinCapacity() {
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public long getMaxCapacity() {
		return 1;
	}

	/**
	 * 
	 */
	@Override
	public long getInitialCapacity() {
		return 1;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getProductions() 
	{
		List<ResourceEvent> events = new ArrayList<>(); 
		for (Decision dec : this.getActiveDecisions()) 
		{
			// create event
			ResourceEvent event = new VariableFreeEvent(dec, 
					dec.getToken().getInterval().getEndTime());
			// add event
			events.add(event);
		}
		
		// get events
		return events;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getConsumptions() 
	{
		List<ResourceEvent> events = new ArrayList<>(); 
		for (Decision dec : this.getActiveDecisions()) 
		{
			// create event
			ResourceEvent event = new VariableHoldingEvent(dec, 
					dec.getToken().getInterval().getStartTime());
			// add event
			events.add(event);
		}
		
		// get events
		return events;
	}

}
