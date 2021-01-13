package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class Gap extends Flaw 
{
	private GapType gapType;
	private Decision left;
	private Decision right;
	private long dmin;
	private long dmax;
	
	/**
	 * 
	 * @param id
	 * @param sv
	 * @param left
	 * @param right
	 * @param distance
	 */
	protected Gap(int id, StateVariable sv, Decision left, Decision right, long[] distance) {
		super(id, sv, FlawType.TIMELINE_BEHAVIOR_PLANNING);
		this.left = left;
		this.right = right;
		this.dmin = distance[0];
		this.dmax = distance[1];
		this.gapType = GapType.INCOMPLETE_TIMELINE;
	}
	
	/**
	 * 
	 * @param id
	 * @param sv
	 * @param left
	 * @param right
	 */
	protected Gap(int id, StateVariable sv, Decision left, Decision right) {
		super(id, sv, FlawType.TIMELINE_BEHAVIOR_PLANNING);
		this.left = left;
		this.right = right;
		this.gapType = GapType.SEMANTIC_CONNECTION;
	}
	
	/**
	 * 
	 */
	public GapType getGapType() {
		return this.gapType;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getLeftDecision() {
		return left;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getRightDecision() {
		return right;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDmin() {
		return dmin;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDmax() {
		return dmax;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Gap [dmin= " + this.dmin + ", dmax= " + this.dmax + "] left= " + this.left + " right-decision= " + this.right + "]";
	}
}
