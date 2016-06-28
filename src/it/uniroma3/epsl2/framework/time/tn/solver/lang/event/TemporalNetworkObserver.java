package it.uniroma3.epsl2.framework.time.tn.solver.lang.event;

import it.uniroma3.epsl2.framework.time.tn.solver.lang.event.ex.NotificationPropagationFailureException;


/**
 * 
 * @author alessandroumbrico
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
	public void notify(TemporalNetworkNotification info) 
			throws NotificationPropagationFailureException;
}
