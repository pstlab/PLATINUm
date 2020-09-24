package it.istc.pst.platinum.framework.domain.component.pdb;

import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SynchronizationConstraint 
{
	protected RelationType type;
	protected TokenVariable source;
	protected TokenVariable target;
	
	/**
	 * 
	 * @param type
	 * @param source
	 * @param target
	 */
	protected SynchronizationConstraint(RelationType type, TokenVariable source, TokenVariable target) {
		this.source = source;
		this.target = target;
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenVariable getReference() {
		return source;
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenVariable getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @return
	 */
	public RelationType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintCategory getCategory() {
		return this.type.getCategory();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[SynchronizationConstraint " + this.source + " " + this.type + " " + this.target + "]";
	}
}
