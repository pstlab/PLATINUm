package it.uniroma3.epsl2.framework.microkernel.resolver.planning;

import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class GoalExpansion extends GoalJustification 
{
	private SynchronizationRule rule;
	
	/**
	 * 
	 * @param decision
	 * @param rule
	 */
	protected GoalExpansion(Goal goal, SynchronizationRule rule) {
		super(goal, JustificationType.EXPANSION);
		this.rule = rule;
	}
	
	/**
	 * 
	 * @param decision
	 */
	protected GoalExpansion(Goal goal) {
		super(goal, JustificationType.EXPANSION);
		this.rule = null;
	}
	
	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("expansion-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasSubGoals() {
		return this.rule != null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getGoalDecision() {
		return decision;
	}
	
	/**
	 * 
	 * @return
	 */
	public SynchronizationRule getSynchronizationRule() {
		return rule;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[GoalExpansion decision= " + this.decision + " synchronization-rule= " + this.rule + "]";
	}
}
