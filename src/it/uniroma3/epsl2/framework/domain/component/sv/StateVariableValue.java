package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class StateVariableValue extends ComponentValue {

	/**
	 * 
	 * @param value
	 * @param duration
	 * @param controllable
	 * @param component
	 */
	protected StateVariableValue(String value, long[] duration, boolean controllable, DomainComponent component) {
		super(value, duration, controllable, component);
	}
}
