package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class BinaryDecisionConflict extends Flaw  
{
	protected Decision[] decisions;
	
	/**
	 * 
	 * @param id
	 * @param sv
	 */
	protected BinaryDecisionConflict(int id, StateVariable sv) {
		super(id, sv, FlawType.TIMELINE_OVERFLOW);
		this.decisions = null;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void setDecisions(Decision[] decs) {
		this.decisions = decs;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Decision[] getDecisions() {
		// get overlapping decisions
		return this.decisions;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ type: \"PRECEDENCE_CONSTRAINT\", reference: " + this.decisions[0] + ", target: " + this.decisions[1] + " }";
	}
}
