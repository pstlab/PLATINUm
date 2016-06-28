package it.uniroma3.epsl2.framework.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public class SynchronizationCycleException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public SynchronizationCycleException(String msg) {
		super(msg);
	}
}
