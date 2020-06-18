package it.istc.pst.platinum.deliberative.strategy;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.DeliberativeObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends DeliberativeObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	protected String label;
	
	/**
	 * 
	 * @param label
	 */
	protected SearchStrategy(String label) {
		super();
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract int getFringeSize();
	
	/**
	 * 
	 * @param node
	 */
	public abstract void enqueue(SearchSpaceNode node);
	
	/**
	 * 
	 * @return
	 * @throws EmptyFringeException
	 */
	public abstract SearchSpaceNode dequeue() 
			throws EmptyFringeException;
	
	/**
	 * 
	 */
	public String toString() {
		return "[SearchStrategy label= " + this.label + "]";
	}
}
