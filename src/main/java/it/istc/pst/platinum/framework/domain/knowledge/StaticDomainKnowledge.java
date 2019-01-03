package it.istc.pst.platinum.framework.domain.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.deliberative.heuristic.filter.ex.HierarchyCycleException;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.domain.component.pdb.TokenVariable;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public class StaticDomainKnowledge extends DomainKnowledge  
{
	// additional knowledge
	private Map<DomainComponent, Set<DomainComponent>> dg;		// dependency graph (as incident graph on components)
	private Map<ComponentValue, Set<ComponentValue>> tree;		// decomposition tree
	
	private List<DomainComponent>[] hierarchy;					// domain hierarchy
	
	/**
	 * 
	 */
	protected StaticDomainKnowledge() {
		super();
		// initialize additional data
		this.dg = null;
		this.tree = null;
		this.hierarchy = null;
	}

	/**
	 * 
	 */
	@Override
	public void printDecompositionTree() {
		// check decomposition tree
		if (this.tree == null) {
			// compute decomposition tree
			this.computeDecompositionTree();
		}
		
		// print decomposition tree
		String str = "Decomposition tree:\n-----------------------------------\n";
		for (ComponentValue val : this.tree.keySet()) {
			str += "- " + val.getLabel() + ":\n";
			for (ComponentValue tar : this.tree.get(val)) {
				str += "\t- " + tar.getLabel() + "\n";
			}
		}
		str += "-----------------------------------";
		// print resulting decomposition tree
		info(str);
	}
	
	/**
	 * 
	 */
	@Override
	public void printDependencyGraph() {
		// check dependency graph
		if (this.dg == null) {
			// compute domain hierarchy
			this.computeDependencyGraph();
		}
		
		// print computed dependencies
		String str = "Dependency graph:\n-----------------------------------\n";
		for (DomainComponent key : this.dg.keySet()) {
			str += "- " + key.getName() + ":\n";
			for (DomainComponent target : this.dg.get(key)) {
				str += "\t- " + target.getName() + "\n";
			}
		}
		str += "-----------------------------------";
		// print dependency graph
		info(str);
		
		// compute hierarchy from dependency graph
		this.computeHierarchy();
		// print resulting hierarchy
		str = "Resulting hierarchy:\n-----------------------------------\n";
		for (int index = 0; index < this.hierarchy.length; index++) {
			str += "hierarchy[" + index + "]: {";
			for (DomainComponent comp : this.hierarchy[index]) {
				str += " " + comp.getName() + " ";
			}
			str += "}\n";
		}
		str += "-----------------------------------";
		// print dependency graph
		info(str);
	}
	
	/**
	 * 
	 */
	@Override
	public Map<DomainComponent, Set<DomainComponent>> getDependencyGraph() {
		// check if a dependency graph has been computed
		if (this.dg == null) {
			// analyze synchronization to extract dependencies among components
			this.computeDependencyGraph();
		}
		
		// get the dependency graph
		return new HashMap<DomainComponent, Set<DomainComponent>>(this.dg);
	}
	
	/**
	 * 
	 */
	@Override
	public Map<ComponentValue, Set<ComponentValue>> getDecompositionTree() {
		// check if a decomposition tree has been computed
		if (this.tree == null) {
			// analyze domain synchronization to build a decomposition tree of the domain
			this.computeDecompositionTree();
		}
		
		// get the decomposition tree
		return new HashMap<ComponentValue, Set<ComponentValue>>(this.tree);
	}
	
	/**
	 * Compute an acyclic Dependency Graph by analyzing the temporal constraints
	 * of the synchronization rules in the domain specification.
	 * 
	 * The dependency graph represents a relaxed view of the temporal dependencies 
	 * among domain components. Namely, if a cycle exists it is ignored by the
	 * dependency graph representation. Thus, only the sub-set of acyclic relations
	 * are considered for inferring dependencies between components.
	 */
	private void computeDependencyGraph() 
	{
		// initialize incident graph
		this.dg = new HashMap<>();
		// initialize the dependency graph
		for (DomainComponent node : this.pdb.getComponents()) {
			// initialize DG 
			this.dg.put(node, new HashSet<DomainComponent>());
		}
			
		// check synchronization and build the graph as "incident" matrix
		for (SynchronizationRule rule : this.pdb.getSynchronizationRules()) 
		{
			// check constraints
			for (SynchronizationConstraint ruleConstraint : rule.getConstraints()) 
			{
				// consider only temporal constraint to build the dependency graph
				if (ruleConstraint.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) 
				{
					// get related token variables
					TokenVariable source = ruleConstraint.getSource();
					TokenVariable target = ruleConstraint.getTarget();
					// check related values' components
					DomainComponent master = source.getValue().getComponent();
					DomainComponent slave = target.getValue().getComponent();
					// check if "external" constraint
					if (!master.equals(slave)) 
					{
						try 
						{
							// update the dependency graph - recall: the dg is an incident graph
							this.dg.get(slave).add(master);
							// check hierarchy cycle
							this.checkHiearchyCycle(slave, master);
						}
						catch (HierarchyCycleException ex) {
							// a cycle into the hierarchy has been found
							warning(ex.getMessage() + "\nIgnore dependency relation between component=" + master + " and component=" + slave);
							// remove dependency relation
							this.dg.get(slave).remove(master);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @throws HierarchyCycleException
	 */
	private void checkHiearchyCycle(DomainComponent reference, DomainComponent target) 
			throws HierarchyCycleException
	{
		// check direct cycle
		if (this.dg.get(reference).contains(target) &&
				this.dg.get(target).contains(reference)) {
			// cycle detected
			throw new HierarchyCycleException("A direct cycle has been detected in the Dependency Graph between component= " + reference + " and component= " + target);
		}
		else {
			// check paths
			this.findCycle(reference, new HashSet<DomainComponent>());
		}
	}
	
	/**
	 * 
	 * @param comp
	 * @param visited
	 * @throws HierarchyCycleException
	 */
	private void findCycle(DomainComponent comp, Set<DomainComponent> visited) 
			throws HierarchyCycleException
	{
		// add component to visited
		visited.add(comp);
		// check component's successors
		for (DomainComponent next : this.dg.get(comp)) {
			// check if visited
			if (!visited.contains(next)) {
				// recursive call
				this.findCycle(next, new HashSet<DomainComponent>(visited));
			}
			else {
				// throw exception
				throw new HierarchyCycleException("A cycle has been introduced in the Dependency Graph between component= " + comp + " and component= " + next);
			}
		}
	}
	
	/**
	 * Analyze synchronization rule constraints to generate and extract the 
	 * decomposition tree of the domain
	 */
	private void computeDecompositionTree()
	{
		// initialize the decomposition tree
		this.tree = new HashMap<>();
		// initialize the decomposition tree
		for (DomainComponent component : this.pdb.getComponents()) {
			// check component values
			for (ComponentValue value : component.getValues()) {
				// add entry to the tree
				this.tree.put(value, new HashSet<>());
			}
		}
		
		// get synchronization domains
		for (SynchronizationRule rule : this.pdb.getSynchronizationRules()) 
		{
			// get trigger value 
			ComponentValue reference = rule.getTriggerer().getValue();
			// check synchronization target
			for (SynchronizationConstraint constraint : rule.getConstraints())
			{
				// get target value
				ComponentValue target = constraint.getTarget().getValue();
				// avoid reflexive references
				if (!reference.equals(target)) {
					this.tree.get(reference).add(target);
				}
			}
		}
	}
	
	/**
	 * Given an acyclic dependency graph, the method builds the 
	 * hierarchy by means of a topological sort algorithm on the 
	 * dependency graph
	 * 
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void computeHierarchy() 
	{
		// compute hierarchy by means of topological sort algorithm
		List<DomainComponent> S = new ArrayList<>();	// root components
		for (DomainComponent c : this.dg.keySet()) 
		{
			// check if root
			if (this.dg.get(c).isEmpty()) {
				S.add(c);
			}
 		}

		// initialize hierarchy
		this.hierarchy = (List<DomainComponent>[]) new ArrayList[this.pdb.getComponents().size()];
		// initialize hierarchy
		for (int index = 0; index < this.hierarchy.length; index++) {
			this.hierarchy[index] = new ArrayList<>();
		}
		
		// top hierarchy level
		int hlevel = 0;
		// start building hierarchy
		while (!S.isEmpty()) 
		{
			// get all root components
			for (DomainComponent root : S) 
			{
				// add component
				this.hierarchy[hlevel].add(root);
				// remove current root from the graph
				this.dg.remove(root);
				// remove "edges" to 
				for (DomainComponent other : this.dg.keySet())
				{
					if (this.dg.get(other).contains(root)) {
						this.dg.get(other).remove(root);
					}
				}
			}
			
			// clear the set of root node
			S.clear();
			// update hierarchy degree
			hlevel++;
			
			// look for new roots
			for (DomainComponent c : this.dg.keySet()) 
			{
				// check if root
				if (this.dg.get(c).isEmpty() && !S.contains(c)) {
					// add root
					S.add(c);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	@Override
	public int getHierarchicalLevelValue(DomainComponent component)
	{
		// get domain hierarchy
		List<DomainComponent>[] h= this.getDomainHierarchy();
		
		boolean found = false;
		int level = 0;
		for (level = 0; level < h.length && !found; level++) {
			// get components
			found = h[level].contains(component);
		}
		
		// get level value (inverted with respect to the position into the array
		return (h.length - level) + 1;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<DomainComponent>[] getDomainHierarchy() 
	{
		// check if dependency graph has been computed
		if (this.dg == null) {
			// compute dependency graph
			this.computeDependencyGraph();
		}
		
		// check if domain hierarchy has been computed
		if (this.hierarchy == null) {
			// compute domain hierarchy
			this.computeHierarchy();
		}

		// get domain hierarchy
		return this.hierarchy;
	}
}
