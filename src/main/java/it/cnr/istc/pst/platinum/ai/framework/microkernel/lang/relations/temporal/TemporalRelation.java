package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalRelation extends Relation 
{
	/**
	 * 
	 * @param id
	 * @param type
	 * @param reference
	 * @param target
	 */
	protected TemporalRelation(int id, RelationType type, Decision reference, Decision target) {
		super(id, type, reference, target);
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
