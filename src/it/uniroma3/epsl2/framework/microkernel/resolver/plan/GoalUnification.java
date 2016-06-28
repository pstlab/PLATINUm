package it.uniroma3.epsl2.framework.microkernel.resolver.plan;

import it.uniroma3.epsl2.framework.lang.plan.Decision;

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
