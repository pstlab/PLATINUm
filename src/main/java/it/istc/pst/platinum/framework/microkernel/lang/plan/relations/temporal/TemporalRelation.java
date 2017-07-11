package it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalRelation extends Relation 
{
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 */
	protected TemporalRelation(RelationType type, Decision reference, Decision target) {
		super(type, reference, target);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract long[][] getBounds();
	
	/**
	 * 
	 */
	public abstract void setBounds(long[][] bounds);
	
	/**
	 * 
	 * @return
	 */
	public abstract TemporalConstraintType getConstraintType();
	
	/**
	 * 
	 */
	@Override
	public abstract TemporalConstraint create();
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraint getConstraint() {
		// cast constraint
		return (TemporalConstraint) this.constraint;
	}
}
