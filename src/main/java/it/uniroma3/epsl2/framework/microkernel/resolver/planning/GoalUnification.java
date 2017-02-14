package it.uniroma3.epsl2.framework.microkernel.resolver.planning;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class GoalUnification extends GoalJustification 
{
	private Decision unification;	// merging decision
	
	/**
	 * 
	 * @param goal
	 * @param dec
	 */
	protected GoalUnification(Goal goal, Decision dec) {
		super(goal, JustificationType.UNIFICATION);
		this.unification = dec;
	}
	
	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("unification-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 * @param goal
	 */
	protected void setGoal(Decision goal) {
		this.decision = goal;
	}
	
	/**
	 * Get the (active) decision the goal can merge with
	 * 
	 * @return
	 */
	public Decision getUnificationDecision() {
		return this.unification;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[GoalUnification decision= " + this.decision + " unificaiton= " + this.unification + "]";
	}
}
