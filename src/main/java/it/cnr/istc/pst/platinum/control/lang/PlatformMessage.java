package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author alessandro
 *
 */
public abstract class PlatformMessage 
{
	protected long id;						// observation ID
	
	/**
	 * 
	 * @param id
	 */
	protected PlatformMessage(long id) {
		this.id = id;
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
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		PlatformObservation<?> other = (PlatformObservation<?>) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
