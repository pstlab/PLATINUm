package it.istc.pst.platinum.framework.microkernel.resolver.timeline.planning;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

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
	 * @param sv
	 * @param left
	 * @param right
	 * @param distance
	 */
	protected Gap(StateVariable sv, Decision left, Decision right, long[] distance) {
		super(sv, FlawType.SV_GAP);
		this.left = left;
		this.right = right;
		this.dmin = distance[0];
		this.dmax = distance[1];
		this.gapType = GapType.INCOMPLETE_TIMELINE;
	}
	
	/**
	 * 
	 * @param sv
	 * @param left
	 * @param right
	 */
	protected Gap(StateVariable sv, Decision left, Decision right) {
		super(sv, FlawType.SV_GAP);
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
