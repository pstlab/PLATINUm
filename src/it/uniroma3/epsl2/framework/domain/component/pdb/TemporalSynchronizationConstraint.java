package it.uniroma3.epsl2.framework.domain.component.pdb;

import it.uniroma3.epsl2.framework.lang.plan.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalSynchronizationConstraint extends SynchronizationConstraint 
{
	private long[][] bounds;
	
	/**
	 * 
	 * @param type
	 * @param source
	 * @param target
	 * @param bounds
	 */
	protected TemporalSynchronizationConstraint(RelationType type, TokenVariable source, TokenVariable target, long[][] bounds) {
		super(type, source, target);
		this.bounds = bounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[][] getBounds() {
		return bounds;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[TemporalSynchronizationConstraint type= " + this.type + " source= " + source + " target= " + target + "]";
	}
}
