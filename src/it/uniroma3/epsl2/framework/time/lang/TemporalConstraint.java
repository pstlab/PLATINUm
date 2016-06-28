package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.domain.component.Constraint;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalConstraint extends Constraint {

	protected TemporalConstraintType type;
	protected TemporalInterval reference;
	protected TemporalInterval target;
	protected TimePointConstraint[] propagated;
	
	/**
	 * 
	 * @param type
	 */
	protected TemporalConstraint(TemporalConstraintType type) {
		super(type.getLabel(), type.getCategory());
		this.type = type;
		this.propagated = null;
	}
	
	/**
	 * 
	 * @param reference
	 */
	public void setReference(TemporalInterval reference) {
		this.reference = reference;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(TemporalInterval target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public abstract void setBounds(long[][] bounds);
	
	/**
	 * 
	 * @return
	 */
	public TemporalConstraintType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @param constraints
	 */
	public void setPropagatedConstraints(TimePointConstraint[] constraints) {
		this.propagated = constraints;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePointConstraint[] getPropagatedConstraints() {
		return propagated;
	}
	
	/**
	 * 
	 */
	public void clear() {
		this.propagated = null;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[" + this.type.getLabel() + ", reference= " + this.reference + ", target= " + this.target + "]";
	}
}
