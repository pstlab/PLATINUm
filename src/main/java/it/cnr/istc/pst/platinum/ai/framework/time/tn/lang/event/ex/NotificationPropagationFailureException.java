package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.ex;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class NotificationPropagationFailureException extends Exception 
{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 */
	public NotificationPropagationFailureException(String message) {
		super(message);
	}
}
