package it.cnr.istc.pst.platinum.ai.framework.parameter.lang;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterDomain 
{
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	private long id;
	private String name;
	private ParameterDomainType type;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	protected ParameterDomain(String name, ParameterDomainType type) {
		this.id = ID_COUNTER.getAndIncrement();
		this.name = name;
		this.type = type;
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
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterDomainType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterType getParameterType() {
		return this.type.getParameterType();
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
		ParameterDomain other = (ParameterDomain) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
