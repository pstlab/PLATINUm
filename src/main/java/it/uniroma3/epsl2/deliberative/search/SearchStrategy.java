package it.uniroma3.epsl2.deliberative.search;

import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.deliberative.solver.SearchSpaceNode;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends ApplicationFrameworkObject 
{
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	@PlanDataBaseReference
	protected PlanDataBase pdb;
	
	protected SearchStrategyType type;
	
	/**
	 * 
	 * @param type
	 */
	protected SearchStrategy(SearchStrategyType type) {
		super();
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public SearchStrategyType getType() {
		return type;
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
		return "[SearchStrategy type= " + this.type + "]";
	}
}
