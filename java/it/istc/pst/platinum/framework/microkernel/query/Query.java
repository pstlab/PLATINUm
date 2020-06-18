package it.istc.pst.platinum.framework.microkernel.query;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Query {

	protected static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	protected int id;
	
	/**
	 * 
	 */
	protected Query() {
		this.id = ID_COUNTER.getAndIncrement();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
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
		Query other = (Query) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
