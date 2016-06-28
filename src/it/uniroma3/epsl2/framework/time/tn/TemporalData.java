package it.uniroma3.epsl2.framework.time.tn;

/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class TemporalData 
{
	protected int id;
	
	/**
	 * 
	 * @param id
	 */
	protected TemporalData(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public final int getId() {
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
		TemporalData other = (TemporalData) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public abstract String toString();
}
