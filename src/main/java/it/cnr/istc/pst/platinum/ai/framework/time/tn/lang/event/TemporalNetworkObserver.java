package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;

/**
 * 
 * @author alessandro
 *
 */
public interface TemporalNetworkObserver 
{
	/**
	 * Notify the observer about some changes in the 
	 * Temporal Network structure. 
	 * 
	 * Returns true if and only if the notification is successfully propagated
	 * 
	 * @param info
	 * @return
	 * @throws NotificationPropagationFailureException
	 */
	public void notify(TemporalNetworkNotification info);
	
}
