package it.cnr.istc.pst.platinum.ai.deliberative.strategy;

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

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;
import it.cnr.istc.pst.platinum.ai.deliberative.strategy.ex.EmptyFringeException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledge;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan.Goal;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SearchStrategy extends FrameworkObject implements Comparator<SearchSpaceNode> 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;												// reference to plan data-base
	
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
	
	
	// set client connection
	protected static MongoClient client;
	// prepare collection
	protected MongoCollection<Document> collection;
	
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
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
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
		
		
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		
		// get mongodb
		String mongodb = properties.getProperty("mongodb").trim();
		// check if exists
		if (mongodb != null && !mongodb.equals("")) 
		{
			// create a collection to the DB
			if (client == null) {
				// set connection
				client = new MongoClient();
			}
			
			
			// get db 
			MongoDatabase db = client.getDatabase(mongodb);
			// get collection
			this.collection = db.getCollection("search_data");
			// remove all data from the collection
			this.collection.drop();
		}
	}
	
	
	/** 
	 * Compute the (pessimistic) planning cost of a domain value by analyzing the extraced decomposition graph 
	 * 
	 * @param value
	 * @return
	 */
	private Map<DomainComponent, Double[]> computeCostProjections(ComponentValue value) 
	{
		// set cost
		Map<DomainComponent, Double[]> cost = new HashMap<>();
		// get hierarchical value of the associated component
		double hValue = this.pdb.getDomainKnowledge().getHierarchicalLevelValue(value.getComponent()) + 1;
					
		// check if leaf
		if (!this.pgraph.containsKey(value) || 
				this.pgraph.get(value).isEmpty()) 
		{
			// set cost
			cost.put(value.getComponent(), new Double[] {
					1.0 * hValue,
					1.0 * hValue
			});
		}
		else 
		{
			// get possible decompositions
			for (List<ComponentValue> decomposition : this.pgraph.get(value))
			{
				// decomposition costs
				Map<DomainComponent, Double[]> dCosts = new HashMap<>();
				for (ComponentValue subgoal : decomposition) 
				{
					// compute planning cost of the subgoal
					Map<DomainComponent, Double[]> update = this.computeCostProjections(subgoal);
					for (DomainComponent c : update.keySet()) {
						if (!dCosts.containsKey(c)) {
							// set cost
							dCosts.put(c, new Double[] {
									update.get(c)[0],
									update.get(c)[1]
							});
						}
						else {
							// update cost
							dCosts.put(c, new Double[] {
									dCosts.get(c)[0] + update.get(c)[0],
									dCosts.get(c)[1] + update.get(c)[1]
							});
						}
					}
				}
				
				// update pessimistic and optimistic projections
				for (DomainComponent c : dCosts.keySet()) 
				{
					if (!cost.containsKey(c)) {
						// set cost
						cost.put(c, new Double[] {
							dCosts.get(c)[0],
							dCosts.get(c)[1]
						});
					}
					else {
						// get min and max
						cost.put(c, new Double[] {
								Math.min(cost.get(c)[0], dCosts.get(c)[0]),
								Math.max(cost.get(c)[1], dCosts.get(c)[1])
						});
					}
				}
			}
			
			// set cost associated to the value
			if (!cost.containsKey(value.getComponent())) {
				// set cost
				cost.put(value.getComponent(), new Double[] {
						1.0 * hValue,
						1.0 * hValue
				});
			}
			else {
			
				// weight cost according to the hierarchical value
				cost.put(value.getComponent(), new Double[] {
						1.0 * hValue + cost.get(value.getComponent())[0],
						1.0 * hValue + cost.get(value.getComponent())[1]
				});
			}
		}
		
		// get cost
		return cost;
	}
	
	/**
	 * Compute the (pessimistic) makespan projection by analyzing the extraced decomposition graph starting 
	 * from a given value of the domain
	 * 
	 * @param value
	 * @return
	 */
	private Map<DomainComponent, Double[]> computeMakespanProjections(ComponentValue value)
	{
		// set data structure
		Map<DomainComponent, Double[]> makespan = new HashMap<>();
		// check if leaf
		if (!this.pgraph.containsKey(value) || 
				this.pgraph.get(value).isEmpty()) 
		{
//			// get average duration
//			double avg = (value.getDurationLowerBound() + value.getDurationUpperBound()) / 2.0;
//			// set value expected average duration
//			makespan.put(value.getComponent(), 
//					new Double[] {
//							avg,
//							avg
//					});
			
			// set value expected minimum duration
			makespan.put(value.getComponent(), new Double[] {
					(double) value.getDurationLowerBound(),
					(double) value.getDurationLowerBound()
			});
		}
		else 
		{
			// check possible decompositions
			for (List<ComponentValue> decomposition : this.pgraph.get(value))
			{
				// set decomposition makespan 
				Map<DomainComponent, Double[]> dMakespan = new HashMap<>();
				// check subgoals
				for (ComponentValue subgoal : decomposition) 
				{
					// recursive call to compute (pessimistic) makespan estimation
					Map<DomainComponent, Double[]> update = this.computeMakespanProjections(subgoal);
					// increment decompostion makespan
					for (DomainComponent c : update.keySet()) {
						// check decompostion makespan 
						if (!dMakespan.containsKey(c)) {
							// add entry 
							dMakespan.put(c, new Double[] {
									update.get(c)[0],
									update.get(c)[1]
							});
						}
						else {
							// increment component's makespan 
							dMakespan.put(c, new Double[] {
									dMakespan.get(c)[0] + update.get(c)[0],
									dMakespan.get(c)[1] + update.get(c)[1]
							});
						}
					}
				}
				
				// update resulting makespan by taking into account the maximum value
				for (DomainComponent c : dMakespan.keySet()) {
					// check makespan
					if (!makespan.containsKey(c)) {
						// add entry
						makespan.put(c, new Double[] {
								dMakespan.get(c)[0],
								dMakespan.get(c)[1]
						});
					}
					else {
						// set the pessimistic and optimistic projections
						makespan.put(c, new Double[] {
								Math.min(makespan.get(c)[0], dMakespan.get(c)[0]),
								Math.max(makespan.get(c)[1], dMakespan.get(c)[1])
						});
					}
				}
			}
			
			// set cost associated to the value
			if (!makespan.containsKey(value.getComponent())) {
				// set cost
				makespan.put(value.getComponent(), new Double[] {
						(double) value.getDurationLowerBound(),
						(double) value.getDurationLowerBound()
				});
			}
			else {
			
				// increment makespan
				makespan.put(value.getComponent(), new Double[] {
						makespan.get(value.getComponent())[0] + ((double) value.getDurationLowerBound()),
						makespan.get(value.getComponent())[1] + ((double) value.getDurationLowerBound())
				});
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
		try 
		{
			// extract the "best" node from the fringe
			next = this.fringe.remove();
			// store search data record
			this.registerSearchChoice(next);
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
		// close DB connection if necessary 
		if (client != null) {
			client.close();
			client = null;
		}
	}
	
	/**
	 * 
	 */
	public String toString() {
		// JSON like object description
		return "{ \"label\": \"" + this.label + "\" }";
	}

	/**
	 * 
	 * @param node
	 */
	protected void registerSearchChoice(SearchSpaceNode node) 
	{
		// check db collection
		if (this.collection != null) {
			// create solving statistic record
			Document doc = new Document("step", node.getId());
			doc.append("fringe-size", this.fringe.size());
			doc.append("node-number-of-flaws", node.getNumberOfFlaws());
			doc.append("node-depth", node.getDepth());
			
			// consolidated values of metrics
			doc.append("node-plan-cost", node.getPlanCost());
			doc.append("node-plan-makespan-min", node.getPlanMakespan()[0]);
			doc.append("node-plan-makespan-max", node.getPlanMakespan()[1]);
			
			// heuristic estimation of metrics
			doc.append("node-heuristic-plan-cost-min", node.getPlanHeuristicCost()[0]);
			doc.append("node-heuristic-plan-cost-max", node.getPlanHeuristicCost()[1]);
			doc.append("node-heuristic-plan-makespan-min", node.getPlanHeuristicMakespan()[0]);
			doc.append("node-heuristic-plan-makespan-max", node.getPlanHeuristicMakespan()[1]);
			
			// insert data into the collection
			this.collection.insertOne(doc);
		}
	}
	
	/**
	 * This method computes an evaluation concerning the (planning) distance of 
	 * a given node from a solution plan. 
	 * 
	 * Namely the method computes the expected cost the planner should "pay" to refine 
	 * the given node and obtain a valid solution. The cost takes into account both planning 
	 * and scheduling decisions. Also, the cost considers possible "gaps" on timelines and 
	 * tries to estimates the planning effort needed to complete the behaviors of 
	 * related timelines.
	 * 
	 * The heuristics computes a cost for each component of the domain and 
	 * takes into account timeline projections and therefore computes a pessimistic 
	 * and optimistic evaluation.
	 * 
	 * @param node
	 * @return
	 */
	protected Map<DomainComponent, Double[]> computeHeuristicCost(SearchSpaceNode node) 
	{
		// compute an optimistic and pessimistic estimation of planning operations
		Map<DomainComponent, Double[]> cost = new HashMap<>();
		// check node flaws and compute heuristic estimation
		for (Flaw flaw : node.getAgenda()) 
		{
			// check planning goal 
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) 
			{
				// get flaw data
				Goal goal = (Goal) flaw;
				// compute cost projections 
				Map<DomainComponent, Double[]> update = this.computeCostProjections(goal.getDecision().getValue());
				// update cost
				for (DomainComponent c : update.keySet()) {
					if (!cost.containsKey(c)) {
						// set cost
						cost.put(c, new Double[] {
								this.planningCost * update.get(c)[0],
								this.planningCost * update.get(c)[1]
						});
					}
					else {
						// update cost
						cost.put(c, new Double[] {
								cost.get(c)[0] + (this.planningCost * update.get(c)[0]),
								cost.get(c)[1] + (this.planningCost * update.get(c)[1])
						});
					}
				}
			}
			
			// check scheduling goal
			if (flaw.getType().equals(FlawType.TIMELINE_OVERFLOW)) 
			{
				// get component
				DomainComponent comp = flaw.getComponent();
				// update cost
				if (!cost.containsKey(comp)) {
					// set cost
					cost.put(comp, new Double[] {
							this.schedulingCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1),
							this.schedulingCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1)
					});
				}
				else {
					// update cost
					cost.put(comp, new Double[] {
						cost.get(comp)[0] + (this.schedulingCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1)),
						cost.get(comp)[1] + (this.schedulingCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1))
					});
				}
			}
			
			// check scheduling goal
			if (flaw.getType().equals(FlawType.TIMELINE_BEHAVIOR_PLANNING)) 
			{
				// get component
				DomainComponent comp = flaw.getComponent();
				// update cost
				if (!cost.containsKey(comp)) {
					// set cost
					cost.put(comp, new Double[] {
							this.completionCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1),
							this.completionCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1)
					});
				}
				else {
					// update cost
					cost.put(comp, new Double[] {
						cost.get(comp)[0] + (this.completionCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1)),
						cost.get(comp)[1] + (this.completionCost * (this.pdb.getDomainKnowledge().getHierarchicalLevelValue(comp) + 1))
					});
				}
			}
		}
		
		
		// finalize data structure
		for (DomainComponent c : this.pdb.getComponents()) {
			if (!cost.containsKey(c)) {
				cost.put(c, new Double[] {
						(double) 0, 
						(double) 0
				});
			}
		}
		
		// get cost
		return cost;
	}
	
	/**
	 * 
	 * This method provides an heuristic evaluation of the makespan of domain components.
	 * 
	 * Namely, the method considesrs planning subgoals of a given partial plan and computes 
	 * a projection of the makespan. The evalution takes into account optmistic and pessimistic
	 * projections of timelines
	 * 
	 * @param node
	 * @return
	 */
	protected Map<DomainComponent, Double[]> computeHeuristicMakespan(SearchSpaceNode node)
	{
		// initialize makespan projects
		Map<DomainComponent, Double[]> projections = new HashMap<>();
		// check node flaws and compute heuristic estimation
		for (Flaw flaw : node.getAgenda()) 
		{
			// check planning goals
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) 
			{
				// get planning goal
				Goal goal = (Goal) flaw;
				// compute optimistic and pessimistic projections of makespan from goals
				Map<DomainComponent, Double[]> update = this.computeMakespanProjections(
						goal.getDecision().getValue());
				
				// update plan projections
				for (DomainComponent c : update.keySet()) 
				{
					// check projection
					if (!projections.containsKey(c)) {
						// set projection
						projections.put(c, 
							new Double[] {
									update.get(c)[0],
									update.get(c)[1]
							});
					}
					else {
						// update projection
						projections.put(c, 
							new Double[] {
									projections.get(c)[0] + update.get(c)[0],
									projections.get(c)[1] + update.get(c)[1]
							});
					}
				}
			}
		}
		
		// finalize data structure
		for (DomainComponent c : this.pdb.getComponents()) {
			if (!projections.containsKey(c)) {
				projections.put(c, new Double[] {
						(double) 0, 
						(double) 0
				});
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
			File pdlFile = new File(FRAMEWORK_HOME + "hierarchy_graph.dot");
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
			File pdlFile = new File(FRAMEWORK_HOME + "dependency_graph.dot");
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
			File pdlFile = new File(FRAMEWORK_HOME + "decomposition_graph.dot");
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
