package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import java.lang.reflect.Constructor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class TemporalNetworkNotificationFactory 
{
	/**
	 * Static Factory
	 */
	private TemporalNetworkNotificationFactory() {}
	
	/**
	 * Creates a new notification instance according 
	 * to the the specified type
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends TemporalNetworkNotification> T createNotification(TemporalNetworkNotificationTypes type) {
		T notif = null;
		try 
		{
			// get constructor
			Constructor<? extends TemporalNetworkNotification> c = type.getNotificationClass().getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			notif = (T) c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("[" + TemporalNetworkNotificationFactory.class.getName() +"]: Error while creating Temporal Network notification type= " + type);
		}
		return notif;
	}
}
