package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.checking;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class MissingObservation extends Flaw 
{
	/**
	 * 
	 * @param id
	 * @param sv
	 */
	protected MissingObservation(int id, StateVariable sv) {
		super(id, sv, FlawType.TIMELINE_BEHAVIOR_CHECKING);
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
