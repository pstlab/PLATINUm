package it.uniroma3.epsl2.deliberative.heuristic.filter.semantic;

import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;

/**
 * 
 * @author anacleto
 *
 */
public interface TemporalSemanticReasoner 
{
	/**
	 * 
	 * @param decision
	 */
	public void add(Decision decision);

	/**
	 * 
	 * @param relation
	 */
	public void add(Relation relation);
	
	/**
	 * 
	 * @param relation
	 */
	void delete(Relation relation);

	/**
	 * 
	 * @param decision
	 */
	void delete(Decision decision);
	
	/**
	 * 
	 */
	public Map<Decision, List<Decision>> getOrderingGraph();
}
