package it.istc.pst.platinum.framework.domain.knowledge;

import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.FrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public abstract class DomainKnowledge extends FrameworkObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	/**
	 * 
	 */
	protected DomainKnowledge() {
		super();
	}
	
	/**
	 * Get the dependency graph as incident graph on domain components. Each component 
	 * is related to other components it depends on. For example, A -> B means that 
	 * component A's behaviors depends on component B's behavior.
	 */
	public abstract Map<DomainComponent, Set<DomainComponent>> getDependencyGraph();
	
	/**
	 * Get the AND/OR graph representing decomposition dependencies extracted from the 
	 * synchronization rules of the domain theory. 
	 * 
	 *  Each value x of the domain is associated with a list of sets of values. Each set 
	 *  represents a disjunction with respect to the possible decomposition of a value x i.e., 
	 *  OR nodes of the graph. The values within the same set are congjunctions of values 
	 *  that should be considered when decomposing a value x i.e., AND nodes of the graph.
	 *  
	 *  Recursive references into the domain theory are ignored and not represented into 
	 *  the graph
	 *   
	 */
	public abstract Map<ComponentValue, List<List<ComponentValue>>> getDecompositionGraph();

	/**
	 * 
	 * @param component
	 * @return
	 */
	public abstract int getHierarchicalLevelValue(DomainComponent component);
	
	/**
	 * 
	 * @return
	 */
	public abstract List<DomainComponent>[] getDomainHierarchy();
	
	/**
	 * 
	 */
	public abstract void printDecompositionTree();
	
	/**
	 * 
	 */
	public abstract void printDependencyGraph();
}
