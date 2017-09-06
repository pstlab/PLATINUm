package it.istc.pst.platinum.framework.microkernel.resolver.timeline.behavior.checking;

import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

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
		super(sv, FlawType.TIMELINE_BEHAVIOR_CHECKING);
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
