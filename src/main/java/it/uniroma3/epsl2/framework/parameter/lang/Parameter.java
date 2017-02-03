package it.uniroma3.epsl2.framework.parameter.lang;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Parameter <T extends ParameterDomain>
{
	private final static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	protected int id;					// id
	protected String label;				// variable name
	protected ParameterType type;		// type of parameter
	protected T domain;					// parameter's domain

	/**
	 * 
	 * @param label
	 * @param type
	 * @param domain
	 */
	protected Parameter(String label, ParameterType type, T domain) {
		// get ID
		this.id = ID_COUNTER.getAndIncrement();
		this.label = label;
		this.type = type;
		this.domain = domain;
	}
	
	/**
	 * 
	 * @param type
	 * @param domain
	 */
	protected Parameter(ParameterType type, T domain) {
		// get ID
		this.id = ID_COUNTER.getAndIncrement();
		this.label = "anonym-" + this.id;
		this.type = type;
		this.domain = domain;
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
	public ParameterType getType() {
		return this.type;
	}
	
	/**
	 * 
	 * @return
	 */
	public  T getDomain() {
		return this.domain;
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
		Parameter<?> other = (Parameter<?>) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
