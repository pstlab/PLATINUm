package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(
		
	resolvers = {
			
		/*
		 * TODO - add specific resolvers
		 */
	},
	
	// TODO - change visualization type
	view = ComponentViewType.GANTT
)
public class ReservoirResource extends Resource
{
	/**
	 * 
	 * @param name
	 */
	protected ReservoirResource(String name) {
		super(name, DomainComponentType.RESOURCE_RESERVOIR);
	}

	@Override
	public List<ResourceEvent> getProductions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceEvent> getConsumptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
