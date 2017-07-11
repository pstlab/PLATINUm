package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

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
		super(value, ComponentValueType.STATE_VARIABLE_VALUE, duration, controllable, component);
	}
}
