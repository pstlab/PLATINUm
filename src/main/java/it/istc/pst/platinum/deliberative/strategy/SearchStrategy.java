package it.istc.pst.platinum.deliberative.strategy;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBase;
import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledge;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.DomainKnowledgePlaceholder;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends ApplicationFrameworkObject 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER) 
	protected FrameworkLogger logger;
	
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	@DomainKnowledgePlaceholder
	protected DomainKnowledge knowledge;
	
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
