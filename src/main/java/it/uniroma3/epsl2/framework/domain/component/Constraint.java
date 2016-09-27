package it.uniroma3.epsl2.framework.domain.component;

import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Constraint 
{
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	
	protected long id;
	protected String label;
	private ConstraintCategory category;
	
	/**
	 * 
	 * @param label
	 * @param category
	 */
	protected Constraint(String label, ConstraintCategory category) {
		// set id
		this.id = ID_COUNTER.getAndIncrement();
		this.label = label;
		this.category = category;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintCategory getCategory() {
		return this.category;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Constraint id= " + this.id + " label= " +  this.label + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Constraint other = (Constraint) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
