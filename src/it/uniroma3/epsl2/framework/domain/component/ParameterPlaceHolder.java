package it.uniroma3.epsl2.framework.domain.component;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterPlaceHolder 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private ComponentValue value;
	private ParameterType type;
	private ParameterDomain domain;
	
	/**
	 * 
	 * @param value
	 * @param type
	 * @param domain
	 */
	protected ParameterPlaceHolder(ComponentValue value, ParameterType type, ParameterDomain domain) {
		this.id = ID_COUNTER.getAndIncrement();
		this.value = value;
		this.type = type;
		this.domain = domain;
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
	 * @return
	 */
	public ParameterDomain getDomain() {
		return domain;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterType getType() {
		return type;
	
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentValue getValue() {
		return value;
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
		ParameterPlaceHolder other = (ParameterPlaceHolder) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
