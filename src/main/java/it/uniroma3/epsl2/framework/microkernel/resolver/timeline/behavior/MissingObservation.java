package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior;

import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class MissingObservation extends Flaw 
{
	/**
	 * 
	 * @param sv
	 */
	protected MissingObservation(StateVariable sv) {
		super(sv, FlawType.INVALID_BEHAVIOR);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isSolvable() {
		// unsolvable flaw
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[MissingObservation sv= " + this.getComponent() + "]";
	}
}
