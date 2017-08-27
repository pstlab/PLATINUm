package it.istc.pst.platinum.framework.microkernel.resolver.timeline.planning;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

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
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("completion-cost");
		// parse and get double value
		return Double.parseDouble(cost);
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
