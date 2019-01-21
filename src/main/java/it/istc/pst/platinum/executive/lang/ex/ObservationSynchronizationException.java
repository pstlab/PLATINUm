package it.istc.pst.platinum.executive.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public class ObservationSynchronizationException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param cause
	 */
	public ObservationSynchronizationException(String msg, ExecutionFailureCause cause) {
		super(msg, cause);
	}
}
