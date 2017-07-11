package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class FunctionalStateVariable extends StateVariable 
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// no resolvers needed for functional state variables
	})
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
