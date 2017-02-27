package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.tn.TemporalData;

/**
 * 
 * @author anacleto
 *
 * @param <R>
 * @param <T>
 */
public abstract class BinaryTemporalConstraint<R extends TemporalData, T extends TemporalData> extends TemporalConstraint
{
	protected R reference;
	protected T target;
	
	/**
	 * 
	 * @param type
	 */
	protected BinaryTemporalConstraint(TemporalConstraintType type) {
		super(type);
	}
	
	/**
	 * 
	 * @param reference
	 */
	public void setReference(R reference) {
		this.reference = reference;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(T target) {
		this.target = target;
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
	 * @return
	 */
	public T getTarget() {
		return target;
	}
}
