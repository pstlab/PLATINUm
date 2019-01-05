package it.istc.pst.platinum.deliberative.heuristic.semantic;

import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;

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
