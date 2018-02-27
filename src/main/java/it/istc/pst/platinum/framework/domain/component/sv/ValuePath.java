package it.istc.pst.platinum.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public class ValuePath implements Comparable<ValuePath> 
{
	private List<ComponentValue> steps;
	
	/**
	 * 
	 * @param steps
	 */
	protected ValuePath(List<ComponentValue> steps) {
		this.steps = new ArrayList<>(steps);
	}
	
	/**
	 * 
	 * @param step
	 */
	public void addStep(ComponentValue step) {
		this.steps.add(step);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ComponentValue> getSteps() {
		return new ArrayList<>(steps);
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.steps.size();
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ValuePath o) {
		return this.size() < o.size() ? -1 : this.size() > o.size() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ValuePath steps= " + this.steps + "]";
	}
}

