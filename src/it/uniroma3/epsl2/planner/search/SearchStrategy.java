package it.uniroma3.epsl2.planner.search;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;
import it.uniroma3.epsl2.planner.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.planner.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends ApplicationFrameworkObject {

	protected SearchStrategyType type;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
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
