package it.uniroma3.epsl2.framework.lang.plan;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.domain.component.Constraint;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Relation 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private RelationType type;
	protected Decision reference;
	protected Decision target;

	protected Constraint constraint;
	
	/**
	 * Create a pending relation
	 * 
	 * @param reference
	 * @param target
	 * @param type
	 */
	protected Relation(RelationType type, Decision reference, Decision target) {
		this.id = ID_COUNTER.getAndIncrement();
		this.type = type;
		this.reference = reference;
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public RelationType getType() {
		return this.type;
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
	 * @return
	 */
	public boolean isLocal() {
		// check involved components
		return this.reference.getComponent().equals(this.target.getComponent());
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void setReference(Decision dec) {
		this.reference = dec;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void setTarget(Decision dec) {
		this.target = dec;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract Constraint create();
	
	
	/**
	 * 
	 * @return
	 */
	public Constraint getConstraint() {
		return constraint;
	}
	
	/**
	 * 
	 */
	public void clear() { 
		this.constraint = null;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Relation " + this.reference + " " + this.type + " " + this.target +" active= " + (this.constraint != null) + "]";
	}
}
