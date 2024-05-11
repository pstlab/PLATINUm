package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex;

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
