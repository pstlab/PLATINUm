package it.uniroma3.epsl2.framework.time.tn.lang.event;

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
		return new Integer(this.id).hashCode();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		TemporalNetworkNotification other = (TemporalNetworkNotification) obj;
		return this.id == other.id;
	}
}
