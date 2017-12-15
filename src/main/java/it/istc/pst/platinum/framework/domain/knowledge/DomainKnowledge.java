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
	 * component A is dependent from component B.
	 */
	public abstract Map<DomainComponent, Set<DomainComponent>> getDependencyGraph();
	
	/**
	 * 
	 */
	public abstract Map<ComponentValue, Set<ComponentValue>> getDecompositionTree();

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
