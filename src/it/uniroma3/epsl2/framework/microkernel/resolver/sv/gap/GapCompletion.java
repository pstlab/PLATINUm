package it.uniroma3.epsl2.framework.microkernel.resolver.sv.gap;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class GapCompletion extends FlawSolution {

	private Decision left;
	private Decision right;
	private List<ComponentValue> path;
	
	/**
	 * 
	 * @param left
	 * @param right
	 * @param path
	 */
	protected GapCompletion(Gap gap, List<ComponentValue> path) {
		super(gap);
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
	 */
	@Override
	public String toString() {
		return "[GapCompletion path= " + this.path + "]";
	}
}
