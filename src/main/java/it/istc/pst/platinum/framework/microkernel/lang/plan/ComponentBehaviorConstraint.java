package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class ComponentBehaviorConstraint 
{
	private static final AtomicLong ID_COUNTER = new AtomicLong(0); 
	private long id;
	private ComponentBehavior reference;
	private ComponentBehavior target;
	private RelationType relType;
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 */
	protected ComponentBehaviorConstraint(RelationType type, ComponentBehavior reference, ComponentBehavior target) {
		this.id = ID_COUNTER.getAndIncrement();
		this.reference = reference;
		this.target = target;
		this.relType = type;
	}
	
	public long getId() {
		return id;
	}
	
	public ComponentBehavior getReference() {
		return reference;
	}
	
	public ComponentBehavior getTarget() {
		return target;
	}
	
	public RelationType getRelType() {
		return relType;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "[BehaviorConstraint type: " + this.relType + ", reference: " + this.reference + ", target: " + this.target + "]";
	}
}
