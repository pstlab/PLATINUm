package it.istc.pst.platinum.deliberative.strategy.hrc;

import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.deliberative.solver.DecisionVariable;
import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategy;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class WorkloadBalancingSearchStrategy extends SearchStrategy 
{
	/**
	 * 
	 */
	protected WorkloadBalancingSearchStrategy() {
		super("WorkloadBalancingSearchStrategy");
	}
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) 
	{
		// compute load balancing 
		double balancing = this.loadBalancing(node);
		// set domain specific metric
		node.setDomainSpecificMetric(new Double(balancing));
		// add the node to the priority queue
		this.fringe.offer(node);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// get balancing
		double b1 = (double) o1.getDomainSpecificMetric();
		double b2 = (double) o2.getDomainSpecificMetric();
		
		// go in depth and check balancing
		return b1 < b2 ? -1 : b1 > b2 ? 1 :  
			o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 :
				o1.getCost() < o2.getCost() ? -1 : o1.getCost() > o2.getCost() ? 1 :
					0;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	private double loadBalancing(SearchSpaceNode node) {
		// number of tasks assigned to the human
		int hsize = 0;
		// number of tasks assigned to the operator
		int rsize = 0;
		
		// get partila plan 
		Map<DomainComponent, List<DecisionVariable>> plan = node.getPartialPlan();
		if (plan != null) 
		{
			// get the make span of the human component
			DomainComponent hComp = this.pdb.getComponentByName("Human");
			// check variable
			if (hComp != null) {
				// get partial plan of the component
				if (plan.containsKey(hComp)) {
					// get decisions
					List<DecisionVariable> list = plan.get(hComp);
					for (DecisionVariable var : list) {
						if (!var.getValue().toLowerCase().contains("idle")) {
							// increment H size
							hsize++;
						}
					}
				}
			}
			
		
			// get the makespan of the robot component
			DomainComponent rComp = this.pdb.getComponentByName("Robot");
			// check variable
			if (rComp != null) {
				// get partial plan of the component
				if (plan.containsKey(rComp)) {
					// get decisions
					List<DecisionVariable> list = plan.get(rComp);
					for (DecisionVariable var : list) {
						if (!var.getValue().toLowerCase().contains("idle")) {
							rsize++;
						}
					}
				}
			}
		}
		
		// check balancing as absolute value 
		return Math.abs(hsize - rsize);
	}
}
