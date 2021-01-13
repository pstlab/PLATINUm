package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class GapCompletion extends FlawSolution 
{
	private Decision left;
	private Decision right;
	private List<ComponentValue> path;
	
	/**
	 * 
	 * @param gap
	 * @param path
	 * @param cost
	 */
	protected GapCompletion(Gap gap, List<ComponentValue> path, double cost) {
		super(gap, cost);
		this.left = gap.getLeftDecision();
		this.right = gap.getRightDecision();
		this.path = new ArrayList<>(path);
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
	public List<ComponentValue> getPath() {
		return path;
	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(FlawSolution o) {
		// get other gap completion flaw
		GapCompletion sol = (GapCompletion) o;
		return this.path.size() < sol.path.size() ? -1 : this.path.size() > sol.path.size() ? 1 : 0; 
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style object description 
		return "{ \"type\": \"GAP_COMPLETION\", "
			+ "\"left\": " + this.left + ", "
			+ "\"right\": " + this.right + ", "
			+ "\"path\": " + this.path.toString() + " }";
	}
}
