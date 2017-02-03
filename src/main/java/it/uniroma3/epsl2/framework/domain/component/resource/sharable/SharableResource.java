package it.uniroma3.epsl2.framework.domain.component.resource.sharable;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.ex.PseudoControllabilityCheckException;
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
	
	// TODO - set specific view type
	view = ComponentViewType.GANTT
)
public class SharableResource extends DomainComponent {

	/**
	 * 
	 * @param name
	 */
	protected SharableResource(String name) {
		super(name, DomainComponentType.RESOURCE_SHARABLE);
	}

	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() throws PseudoControllabilityCheckException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public List<ComponentValue> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public ComponentValue getValueByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
