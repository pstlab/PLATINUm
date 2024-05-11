package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class TemporalNetworkNotification 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	protected int id;
	protected TemporalNetworkNotificationTypes type;
	
	/**
	 * 
	 * @param type
	 */
	protected TemporalNetworkNotification(TemporalNetworkNotificationTypes type) {
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
	public TemporalNetworkNotificationTypes getType() {
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TemporalNetworkNotification other = (TemporalNetworkNotification) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
