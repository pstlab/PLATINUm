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
import it.istc.pst.platinum.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;

/**
 * 
 * @author anacleto
 *
 */
public class StaticDomainKnowledge 
{
	// domain theory
	private Map<String, ParameterDomain> parameterDomains;
	private Map<DomainComponent, Map<ComponentValue, List<SynchronizationRule>>> rules;
	
	// additional knowledge
	private Map<DomainComponent, Set<DomainComponent>> dg;		// dependency graph (as incident graph on components)
	private Map<ComponentValue, Set<ComponentValue>> tree;		// decomposition tree
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @return
	 * @throws DomainComponentNotFoundException
	 */
	public SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
			throws DomainComponentNotFoundException 
	{
		// check if related component exists
//		if (!this.components.containsKey(value.getComponent().getName())) {
//			throw new DomainComponentNotFoundException("Value's component not found " + value);
//		}
		// create synchronization rule
		return new SynchronizationRule(value, labels);
	}
	
	/**
	 * 
	 * @param rule
	 * @throws SynchronizationCycleException
	 */
	public void addSynchronizationRule(SynchronizationRule rule) 
			throws SynchronizationCycleException 
	{
		// get head value
		ComponentValue value = rule.getTriggerer().getValue();
		// check data
		if (!this.rules.containsKey(value.getComponent())) {
			this.rules.put(value.getComponent(), new HashMap<ComponentValue, List<SynchronizationRule>>());
		}
		if (!this.rules.get(value.getComponent()).containsKey(value)) {
			// initialize
			this.rules.get(value.getComponent()).put(value, new ArrayList<SynchronizationRule>());
		}
		
		// look for cycles
		for (TokenVariable var : rule.getTokenVariables()) 
		{
			// get value 
			ComponentValue v = var.getValue();
			// check if this value is trigger of other synchronizations
			if (this.rules.containsKey(v.getComponent()) && this.rules.get(v.getComponent()).containsKey(v)) {
				// get synchronizations
				List<SynchronizationRule> existingRules = this.rules.get(v.getComponent()).get(v);
				for (SynchronizationRule existingRule : existingRules) {
					// get rule trigger
					TokenVariable existingRuleTrigger = existingRule.getTriggerer();
					// check constraint
					for (SynchronizationConstraint cons : existingRule.getConstraints()) {
						// consider temporal constraint for cycle detection
						if (cons.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) {
							// get constraint target
							TokenVariable target = cons.getTarget();
							if (!target.equals(existingRuleTrigger) && target.getValue().equals(value)) { 
								// we've got a cycle
								throw new SynchronizationCycleException("A cycle has been detected after the introduction of synchronization rule " + rule);
							}
						}
					}
				}
			}
		}
		
		// add rule if no cycle is detected
		this.rules.get(value.getComponent()).get(value).add(rule);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules() {
		// get all rules
		List<SynchronizationRule> list = new ArrayList<>();
		// get the list of available synchronization rules
		for (DomainComponent comp : this.rules.keySet()) {
			for (ComponentValue v : this.rules.get(comp).keySet()) {
				// add rules
				list.addAll(this.rules.get(comp).get(v));
			}
		}
		
		// get rules
		return list;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules(ComponentValue value) {
		// list of rules
		List<SynchronizationRule> rules = new ArrayList<>();
		// check domain specification
		if (this.rules.containsKey(value.getComponent()) && this.rules.get(value.getComponent()).containsKey(value)) {
			rules.addAll(this.rules.get(value.getComponent()).get(value));
		}
		// get rules
		return rules;
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules(DomainComponent component) {
		// list of rules
		List<SynchronizationRule> rules = new ArrayList<>();
		// check domain specification
		if (this.rules.containsKey(component)) {
			for (ComponentValue value : this.rules.get(component).keySet()) {
				rules.addAll(this.rules.get(component).get(value));
			}
		}
		// get rules
		return rules;
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
		for (DomainComponent node : this.getComponents()) {
			// initialize DG 
			this.dg.put(node, new HashSet<DomainComponent>());
		}
			
		// check synchronization and build the graph as "incident" matrix
		for (SynchronizationRule rule : this.getSynchronizationRules()) 
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
							this.logger.warning(ex.getMessage() + "\nIgnore dependency relation between component=" + master + " and component=" + slave);
							// remove dependency relation
							this.dg.get(slave).remove(master);
						}
					}
				}
			}
		}
	}
}
