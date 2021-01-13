package it.cnr.istc.pst.platinum.ai.deliberative.strategy;

import java.util.Map;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class WeightedAStarSearchStrategy extends SearchStrategy 
{
	/**
	 * 
	 */
	protected WeightedAStarSearchStrategy() {
		super("WeightedAStarSearchStrategy");
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) 
	{
		// compute a pessimistic estimation of flaw resolution cost
		Map<DomainComponent, Double[]> h = this.computeHeuristicCost(node);
		// set heuristic estimation
		node.setHeuristicCost(h);
		// add the node to the priority queue
		this.fringe.offer(node);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) 
	{
		// set cost weight
		double weight = .3;
		// compute evaluation functions
		double f1 = (weight * o1.getPlanCost()) + ((1.0 - weight) * o1.getPlanHeuristicCost()[0]);
		double f2 = (weight * o2.getPlanCost()) + ((1.0 - weight) * o2.getPlanHeuristicCost()[0]);
		// compare evaluation functions 
		return f1 < f2 ? -1 : f1 > f2 ? 1 : 
			o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 0;
			 
	}
}
