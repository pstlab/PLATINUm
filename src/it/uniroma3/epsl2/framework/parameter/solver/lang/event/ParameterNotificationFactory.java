package it.uniroma3.epsl2.framework.parameter.solver.lang.event;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterNotificationFactory 
{
	private static ParameterNotificationFactory INSTANCE = null;
	
	/**
	 * 
	 */
	private ParameterNotificationFactory() {}

	/**
	 * 
	 * @return
	 */
	public static ParameterNotificationFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParameterNotificationFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterNotification> T create(ParameterNotificationType type) {
		T notif = null;
		try {
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterNotificationClassName());
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			notif = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return notif;
	}
}
