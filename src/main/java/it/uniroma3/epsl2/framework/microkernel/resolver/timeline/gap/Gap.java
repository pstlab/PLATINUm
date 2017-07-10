package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.gap;

import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;

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
		String str =  "[Gap left-decision= " + this.left + " right-decision= " + this.right + " solutions=\n";
		for (FlawSolution sol : this.getSolutions()) {
			str += "\t- completion= " + sol + "\n";
		}
		str += "]";
		return str;
	}
}
