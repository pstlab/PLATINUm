package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
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
				// no resolvers are needed for functional state variables
		},
		
		view = ComponentViewType.GANTT
)
public class FunctionalStateVariable extends StateVariable 
{
	/**
	 * 
	 * @param name
	 */
	protected FunctionalStateVariable(String name) {
		super(name, DomainComponentType.SV_FUNCTIONAL);
	}
	
	/**
	 * Every value of a functional variable is controllable
	 */
	@Override
	public ComponentValue addValue(String value, long[] duration, boolean controllable) {
		// create an uncontrollable value
		return super.addValue(value, duration, true);
	}
	
	/**
	 * Every value of a functional variable is controllable
	 */
	@Override
	public ComponentValue addValue(String value, boolean controllable) {
		// create uncontrollable value
		return super.addValue(value, true);
	}
	
	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() throws PseudoControllabilityCheckException {
		// nothing to do
	}
}
