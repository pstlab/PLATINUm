package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public class ValuePath implements Comparable<ValuePath> 
{
	private Deque<ComponentValue> steps;
	
	/**
	 * 
	 * @param steps
	 */
	protected ValuePath(List<ComponentValue> steps) {
		this.steps = new LinkedList<>(steps);
	}
	
	/**
	 * 
	 */
	protected ValuePath() {
		this.steps = new LinkedList<>();
	}
	
	/**
	 * 
	 * @param step
	 */
	public void addLastStep(ComponentValue step) {
		this.steps.addLast(step);
	}
	
	/**
	 * 
	 * @param step
	 */
	public void addFirstStep(ComponentValue step) {
		this.steps.addFirst(step);
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
	 * @param value
	 * @return
	 */
	public boolean contains(ComponentValue value) {
		return this.steps.contains(value);
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

