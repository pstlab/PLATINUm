package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.tn.TemporalData;

/**
 * 
 * @author anacleto
 *
 */
public abstract class UnaryTemporalConstraint<R extends TemporalData> extends TemporalConstraint 
{
	protected R reference;
	
	/**
	 * 
	 * @param type
	 */
	protected UnaryTemporalConstraint(TemporalConstraintType type) {
		super(type);
	}
	
	/**
	 * 
	 * @return
	 */
	public R getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @param reference
	 */
	public void setReference(R reference) {
		this.reference = reference;
	}
}
