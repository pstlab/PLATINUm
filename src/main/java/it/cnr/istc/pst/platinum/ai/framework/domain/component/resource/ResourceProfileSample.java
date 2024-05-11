package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceProfileSample implements Comparable<ResourceProfileSample> 
{
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	private long id;
	
	/**
	 * 
	 */
	protected ResourceProfileSample() {
		this.id = ID_COUNTER.getAndIncrement();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
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
		ResourceProfileSample other = (ResourceProfileSample) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
