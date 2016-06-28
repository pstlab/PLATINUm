package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.time.tn.stnu.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(
		
		// set resolvers to manage functional variables
		resolvers = {
				
				ResolverType.SV_SCHEDULING_RESOLVER,
				
				ResolverType.SV_GAP_RESOLVER
		}
)
public class FunctionalStateVariable extends StateVariable {
	
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
		return this.doCreateValue(value, duration, true);
	}
	
	/**
	 * Every value of a functional variable is controllable
	 */
	@Override
	public ComponentValue addValue(String value, boolean controllable) {
		// create uncontrollable value
		return this.doCreateValue(value, new long[] {1, this.tdb.getHorizon()}, true);
	}
	
	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() throws PseudoControllabilityCheckException {
		// nothing to do
	}
}
