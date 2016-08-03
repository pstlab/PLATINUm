package it.uniroma3.epsl2.framework.domain.component.resource.reservoir;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.time.tn.stnu.ex.PseudoControllabilityCheckException;
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
public class ReservoirResource extends DomainComponent{

	/**
	 * 
	 * @param name
	 */
	protected ReservoirResource(String name) {
		super(name, DomainComponentType.RESOURCE_RESERVOIR);
	}

	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException {
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
