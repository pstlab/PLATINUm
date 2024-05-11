package it.cnr.istc.pst.platinum.ai.deliberative.strategy;

import java.util.Map;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;

/**
 * 
 * @author alessandro
 *
 */
public class StandardDeviationMinimizationSearchStrategy extends SearchStrategy {
	
	/**
	 * 
	 */
	protected StandardDeviationMinimizationSearchStrategy() {
		super("TimelineSDMinimizationSearchStrategy");
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) {
		
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
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		
		// get plan horizon
		long horizon = this.pdb.getHorizon();
		
		double sd1 = this.standardDevitation(horizon, o1);
		double sd2 = this.standardDevitation(horizon, o2);
		
		// greedy search checking standard deviation to the horizon
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 :
			// check standard deviations
			sd1 < sd2 ? -1 : sd1 > sd2 ? 1 :
				0;
	}
	
	/**
	 * 
	 * @param horizon
	 * @param node
	 * @return
	 */
	public double standardDevitation(long horizon, SearchSpaceNode node) {
		
		// initialize standard deviation with horizon
		double sd = horizon;
		
		// get estimated makespan of the plan
		Map<DomainComponent, Double[]> mk = node.getEstimatedMakespan();
		double N = mk.keySet().size();
		double quadSum = 0;
		for (DomainComponent comp : mk.keySet()) {
			
			quadSum += Math.pow((mk.get(comp)[0] - horizon), 2.0);
		}
		
		// check data
		if (N > 0 && quadSum > 0) {
			// compute standard deviation
			sd = Math.sqrt(quadSum / N);
		}
		
		// return standard deviation
		return sd;
		
	}
	
}
