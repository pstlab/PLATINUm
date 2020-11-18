package it.istc.pst.platinum.deliberative.strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledge;
import it.istc.pst.platinum.framework.microkernel.DeliberativeObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.plan.Goal;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends DeliberativeObject implements Comparator<SearchSpaceNode> 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	protected Queue<SearchSpaceNode> fringe;								// the fringe of the search space
	
	protected String label;													// strategy label
	
	protected Map<ComponentValue, List<List<ComponentValue>>> pgraph;		// planning graph
	protected Map<DomainComponent, Set<DomainComponent>> dgraph;			// dependency graph
	protected List<DomainComponent>[] dhierarchy;							// domain hierarchy
	
	protected double schedulingCost;										// set scheduling cost
	protected double completionCost;										// set completion cost
	protected double planningCost;											// general planning cost
	protected double expansionCost;											// detailed planning cost
	protected double unificationCost;										// detailed unification cost
	
	/**
	 * 
	 * @param label
	 */
	protected SearchStrategy(String label) {
		super();
		// initialize the fringe 
		this.fringe = new PriorityQueue<SearchSpaceNode>(this);
		
		// set label 
		this.label = label;
		
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		// set operation costs from parameters
		this.planningCost = Double.parseDouble(properties.getProperty("expansion-cost"));
		this.expansionCost = Double.parseDouble(properties.getProperty("expansion-cost"));
		this.unificationCost = Double.parseDouble(properties.getProperty("unification-cost"));
		this.schedulingCost = Double.parseDouble(properties.getProperty("scheduling-cost"));
		this.completionCost = Double.parseDouble(properties.getProperty("completion-cost"));
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// get domain knowledge
		DomainKnowledge dk = this.pdb.getDomainKnowledge();
		// get the decomposition tree from the domain theory
		this.pgraph = dk.getDecompositionGraph();
		// export decomposition graph
		this.exportDecompositionGraph(this.pgraph);
		
		// get dependency graph
		this.dgraph = dk.getDependencyGraph();
		// export dependency graph
		this.exportDependencyGraph(this.dgraph);
		
		// get domain hierarchy
		this.dhierarchy = dk.getDomainHierarchy();
		// export hierarchy
		this.exportHierarchyGraph(this.dhierarchy);
		
	}
	
	/** 
	 * Compute the planning cost of a domain value by analyzing the extraced decomposition graph 
	 * 
	 * @param value
	 * @return
	 */
	private double computePlanningCost(ComponentValue value) 
	{
		// check if leaf
		if (!this.pgraph.containsKey(value) || 
				this.pgraph.get(value).isEmpty()) 
		{
			// get hierarchical value of the associated component
			double hValue = this.pdb.getDomainKnowledge().getHierarchicalLevelValue(value.getComponent()) + 1;
			// base base as expansion cost of a single decision 
			return 1.0 * hValue;
		}
		else 
		{
			// get the maximum cost among possible decompositions
			double cost = 0;
			
			// get possible decompositions
			for (List<ComponentValue> decomposition : this.pgraph.get(value))
			{
				// subgoals costs
				double subgoalCost = 0;
				for (ComponentValue subgoal : decomposition) {
					// compute planning cost of the subgoal
					subgoalCost += this.computePlanningCost(subgoal);
				}
				
				// update the decomposition cost by taking into account the maximum value
				cost = Math.max(cost, subgoalCost);
			}

			// take into account the hierarchical value of the component
			double hValue = this.pdb.getDomainKnowledge().getHierarchicalLevelValue(value.getComponent()) + 1;
			// get computed cost
			return cost * hValue;
		}
	}
	
	/**
	 * Compute the (pessimistic) makespan projection by analyzing the extraced decomposition graph starting 
	 * from a given value of the domain
	 * 
	 * @param value
	 * @return
	 */
	private Map<DomainComponent, Long> computeMakespanProjection(ComponentValue value)
	{
		// set data structure
		Map<DomainComponent, Long> makespan = new HashMap<>();
		// check if leaf
		if (!this.pgraph.containsKey(value) || 
				this.pgraph.get(value).isEmpty()) 
		{
			// set value expected minimum duration
			makespan.put(value.getComponent(), value.getDurationLowerBound());
		}
		else 
		{
			// check possible decompositions
			for (List<ComponentValue> decomposition : this.pgraph.get(value))
			{
				// set decomposition makespan 
				Map<DomainComponent, Long> dMakespan = new HashMap<>();
				// check subgoals
				for (ComponentValue subgoal : decomposition) 
				{
					// recursive call to compute (pessimistic) makespan estimation
					Map<DomainComponent, Long> sgMakespan = this.computeMakespanProjection(subgoal);
					// increment decompostion makespan
					for (DomainComponent c : sgMakespan.keySet()) {
						// check decompostion makespan 
						if (!dMakespan.containsKey(c)) {
							// add entry 
							dMakespan.put(c, sgMakespan.get(c));
						}
						else {
							// increment component's makespan 
							dMakespan.put(c, 
									dMakespan.get(c) + sgMakespan.get(c));
						}
					}
				}
				
				// update resulting makespan by taking into account the maximum value
				for (DomainComponent c : dMakespan.keySet()) {
					// check makespan
					if (!makespan.containsKey(c)) {
						// add entry
						makespan.put(c, dMakespan.get(c));
					}
					else {
						// set the max
						makespan.put(c, 
								Math.max(makespan.get(c), dMakespan.get(c)));
					}
				}
			}
		}
		
		// get the makespan 
		return makespan;
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
	public int getFringeSize() {
		return this.fringe.size();
	}
	
	/**
	 * 
	 * @param node
	 */
	public abstract void enqueue(SearchSpaceNode node);
	
	/**
	 * 
	 */
	@Override
	public abstract int compare(SearchSpaceNode n1, SearchSpaceNode n2);
	
	/**
	 * 
	 * @return
	 * @throws EmptyFringeException
	 */
	public SearchSpaceNode dequeue() 
			throws EmptyFringeException 
	{
		// set next node of the fringe
		SearchSpaceNode next = null;
		try {
			
			// extract the "best" node from the fringe
			next = this.fringe.remove();
		}
		catch (NoSuchElementException ex) {
			// empty fringe
			throw new EmptyFringeException("No more nodes in the fringe");
		}
		
		// get extracted node
		return next;
	}
	
	/**
	 * Clear the internal data structures of a search strategy
	 */
	public void clear() {
		// clear queue
		this.fringe.clear();
	}
	/**
	 * 
	 */
	public String toString() {
		// JSON like object description
		return "{ \"label\": \"" + this.label + "\" }";
	}
	
	/**
	 * This method computes a pessimistic evaluation concerning the (planning) distance of 
	 * a given node from a solution plan. 
	 * 
	 * Namely the method computes the expected cost the planner should "pay" to refine 
	 * the given node and obtain a valid solution. The cost takes into account both planning 
	 * and scheduling decisions. Also the cost considers possible "gaps" on timelines and 
	 * tries to estimates the planning effort needed to complete the behaviors of 
	 * related timelines.
	 * 
	 * @param node
	 * @return
	 */
	protected double computePlanningHeuristics(SearchSpaceNode node) 
	{
		// compute a pessimistic estimation of flaw resolution cost
		double hValue = 0;
		// check node flaws and compute heuristic estimation
		for (Flaw flaw : node.getAgenda()) {
			// check planning goal 
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) {
				// get flaw data
				Goal goal = (Goal) flaw;
				// compute planning costs of each subgoal
				hValue += this.planningCost * this.computePlanningCost(goal.getDecision().getValue());
			}
			
			// check scheduling goal
			if (flaw.getType().equals(FlawType.TIMELINE_OVERFLOW)) {
				// get component
				DomainComponent comp = flaw.getComponent();
				// get component hierarchy
				hValue += this.schedulingCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1);
			}
			
			// check scheduling goal
			if (flaw.getType().equals(FlawType.TIMELINE_BEHAVIOR_PLANNING)) {
				// get component
				DomainComponent comp = flaw.getComponent();
				// get component hierarchy
				hValue += this.completionCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1);
			}
		}
		
		// get value
		return hValue;
	}
	
	/**
	 * 
	 * This method provides a pessimistic evaluation of the makespan of domain components. 
	 * 
	 * Namely, the method considesrs planning subgoals of a given partial plan and computes 
	 * a pessimistic projection of the makespan of domain components
	 * 
	 * @param node
	 * @return
	 */
	protected Map<DomainComponent, Long> computeMakespanHeuristics(SearchSpaceNode node)
	{
		// initialize makespan projects
		Map<DomainComponent, Long> projections = new HashMap<>();
		for (DomainComponent c : this.pdb.getComponents()) {
			// set initial projection
			projections.put(c, 0l);
		}
		
		// check node flaws and compute heuristic estimation
		for (Flaw flaw : node.getAgenda()) 
		{
			// check planning goals
			
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) {
				// get planning goal
				Goal goal = (Goal) flaw;
				// compute goal (pessimistic) projection
				Map<DomainComponent, Long> gProjection = this.computeMakespanProjection(goal.getDecision().getValue());
				
				// update plan projections
				for (DomainComponent c : gProjection.keySet()) {
					// increment project
					projections.put(c, projections.get(c) + gProjection.get(c));
				}
				
			}
		}
		
		// get projections
		return projections;
	}
	
	/**
	 * 
	 * @param graph
	 */
	private void exportHierarchyGraph(List<DomainComponent>[] graph)
	{
		// export graph
		String str = "digraph hierarhcy_graph {\n";
		str += "\trankdir=TB;\n";
		str += "\tnode [fontsize=11, style=filled, fillcolor=azure, shape = box]\n";
		
		
		// check dependencies
		for (int index = 0; index < graph.length - 1; index++) {
			// get components at current level
			List<DomainComponent> currlist = graph[index];
			// get components at next level
			List<DomainComponent> nextlist = graph[index + 1];
			
			for (DomainComponent curr : currlist) {
				for (DomainComponent next : nextlist) {
					// add an edge to the graph
					str += "\t" + curr.getName() + " -> " + next.getName();
					
				}
			}
			
		}
		
		// close 
		str += "\n}\n\n";
		
		try 
		{
			File pdlFile = new File("hierarchy_graph.dot");
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
				// write file
				writer.write(str);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param graph
	 */
	private void exportDependencyGraph(Map<DomainComponent, Set<DomainComponent>> graph)
	{
		// export graph
		String str = "digraph dependency_graph {\n";
		str += "\trankdir=TB;\n";
		str += "\tnode [fontsize=11, style=filled, fillcolor=azure, shape = box]\n";
		
		// check dependencies
		for (DomainComponent comp : graph.keySet()) {
			// check dependencies
			for (DomainComponent dep : graph.get(comp)) {
				// add an edge to the graph
				str += "\t" + dep.getName() + " -> " + comp.getName();
			}
		}
		
		// close 
		str += "\n}\n\n";
		
		try 
		{
			File pdlFile = new File("dependency_graph.dot");
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
				// write file
				writer.write(str);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param graph
	 */
	private void exportDecompositionGraph(Map<ComponentValue, List<List<ComponentValue>>> graph) 
	{
		// export graph
		String str = "digraph decomposition_graph {\n";
		str += "\trankdir=TB;\n";
		str += "\tnode [fontsize=11, style=filled, fillcolor=azure, shape = box]\n";
		
		// node id
		int counter = 0;
		// create AND nodes
		int andCounter = 0;
		// check the graph
		for (ComponentValue value : graph.keySet())
		{
			// check number of disjunctions
			List<List<ComponentValue>> disjunctions = graph.get(value);
			if (disjunctions.size() == 1) 
			{
				String andNode = "AND_" + andCounter;
				str += "\t" + andNode + " [fontsize=6, shape= oval, style=filled, fillcolor= palegreen];\n";
				
				str += "\t" + value.getComponent().getName() + "_" + value.getLabel().replace("-", "_") + 
						" -> " + andNode + ";\n";
				
				
				
				// set weight of the edge
				Map<ComponentValue, Integer> wc = new HashMap<>();
				for (ComponentValue child : disjunctions.get(0)) {
					if (!wc.containsKey(child)) {
						wc.put(child, 1);
					}
					else {
						// increment
						int v = wc.get(child);
						wc.put(child, ++v);
					}
				}
				
				// no disjunctions
				for (ComponentValue child : wc.keySet()) {
					// add edge
					str += "\t" + andNode + " -> " + child.getComponent().getName() + "_" + child.getLabel().replace("-", "_") + " [label= \"" + wc.get(child) + "\"];\n";
				}
				
				// increment and node counter
				andCounter++;
			}
			else 
			{
				// add OR node label
				String orLabel = "OR_" + counter;
				// add an edge to the OR node
				str += "\t" + orLabel + " [fontsize=6, shape= diamond, style=filled, fillcolor= thistle];\n";
				str += "\t" + value.getComponent().getName() + "_" + value.getLabel().replace("-", "_") + 
						" -> " + orLabel + ";\n";
				
				// add disjunctions
				for (List<ComponentValue> conjunctions : disjunctions) 
				{
					// set AND node label
					String andLabel = "AND_" + andCounter;
					str += "\t" + andLabel + " [fontsize=6, shape= oval, style=filled, fillcolor= palegreen];\n";
					
					// set weight of the edge
					Map<ComponentValue, Integer> wc = new HashMap<>();
					for (ComponentValue child : conjunctions) {
						if (!wc.containsKey(child)) {
							wc.put(child, 1);
						}
						else {
							// increment
							int v = wc.get(child);
							wc.put(child, ++v);
						}
					}
					
					// add and edge to the AND node
					str += "\t" + orLabel + " -> " + andLabel + ";\n";
					for (ComponentValue child : wc.keySet()) {
						// add edge from AND node to the value
						str += "\t" + andLabel + 
								" -> " + child.getComponent().getName() + "_" +child.getLabel().replace("-", "_") + " [label= \"" + wc.get(child) + "\"];\n";
					}
					
					
					// increment and node counter
					andCounter++;
				}
			
				counter++;
			}
			
			
		}
		
		// close 
		str += "\n}\n\n";
		
		try 
		{
			File pdlFile = new File("decomposition_graph.dot");
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
				// write file
				writer.write(str);
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}
