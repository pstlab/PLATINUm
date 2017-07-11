package it.istc.pst.platinum.framework.time.tn.lang.event;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum TemporalNetworkNotificationTypes 
{
	/**
	 * The subscriber can initializes its data over
	 * the subscribed temporal network
	 */
	INITIALIZATION(InitializationTemporalNetworkNotifaction.class),
	
	/**
	 * A new Time Point has been added to the 
	 * subscribed Temporal Network
	 */
	ADD_TP(AddTimePointTemporalNetworkNotification.class),
	
	/**
	 * A new Time Point has been deleted from the
	 * subscribed Temporal Network
	 */
	DEL_TP(DelTimePointTemporalNetworkNotification.class),
	
	/**
	 * A new Distance Relation has been added to 
	 * the subscribed Temporal Network
	 */
	ADD_REL(AddRelationTemporalNetworkNotification.class),
	
	/**
	 * A Distance Relation has been deleted from
	 * the subscribed Temporal Network
	 */
	DEL_REL(DelRelationTemporalNetworkNotification.class);
	
	private Class<? extends TemporalNetworkNotification> notificationClass;
	
	/**
	 * 
	 * @param clazz
	 */
	private TemporalNetworkNotificationTypes(Class<? extends TemporalNetworkNotification> clazz) {
		this.notificationClass = clazz;
	}
	
	/**
	 * 
	 * @return
	 */
	public Class<? extends TemporalNetworkNotification> getNotificationClass() {
		return notificationClass;
	}
}
