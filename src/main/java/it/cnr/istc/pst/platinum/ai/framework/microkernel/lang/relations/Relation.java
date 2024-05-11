package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Constraint;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Relation 
{
	private int id;
	private RelationType type;
	protected Decision reference;
	protected Decision target;

	protected Constraint constraint;
	
	/**
	 * Create a pending relation
	 * 
	 * @param id
	 * @param reference
	 * @param target
	 * @param type
	 */
	protected Relation(int id, RelationType type, Decision reference, Decision target) {
		this.id = id;
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
	 * @return
	 */
	public boolean isActive() {
		// get reference
		DomainComponent referenceComponent = this.reference.getComponent();
		// get target
		DomainComponent targetComponent = this.target.getComponent();
		// check condition
		return referenceComponent.isActive(this.reference) && 
				targetComponent.isActive(this.target) && 
				this.constraint != null;
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	public boolean canBeActivated()
	{
		// get reference component
		DomainComponent referenceComponent = this.reference.getComponent();
		// get target component
		DomainComponent targetComponent = this.target.getComponent();
		// check condition
		return referenceComponent.isActive(this.reference) && 
				targetComponent.isActive(this.target) && 
				this.constraint == null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPending() 
	{
		// get reference component
		DomainComponent refComp = this.reference.getComponent();
		// get target component
		DomainComponent tarComp = this.target.getComponent();
		// check condition
		return (refComp.isPending(this.reference) || tarComp.isPending(this.target)) && 
				!(refComp.isSilent(this.reference) || tarComp.isSilent(this.target) && 
						this.constraint == null);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSilent()
	{
		// get reference component
		DomainComponent refComp = this.reference.getComponent();
		// get target component
		DomainComponent tarComp = this.target.getComponent();
		// check condition
		return (refComp.isSilent(this.reference) || tarComp.isSilent(this.target)) && constraint == null;
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
		// JSON style object description
		return "{ \"id\": " + this.id +", \"type\": \"" + this.type.toString().toLowerCase() + "\", \"refId\": " + this.reference.getId() + ", \"tarId\": " + this.target.getId() + " }";
	}
}
