package it.uniroma3.epsl2.framework.parameter.csp.event;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterNotification 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	private int id;
	private ParameterNotificationType type;
	
	/**
	 * 
	 * @param type
	 */
	protected ParameterNotification(ParameterNotificationType type) {
		this.id = ID_COUNTER.getAndIncrement();
		this.type = type;
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
	public ParameterNotificationType getType() {
		return type;
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
		ParameterNotification other = (ParameterNotification) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
