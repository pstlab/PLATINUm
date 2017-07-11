package it.istc.pst.platinum.framework.time.ex;

/**
 * 
 * @author anacleto
 *
 */
public class InconsistentIntervalDurationException extends TemporalIntervalCreationException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public InconsistentIntervalDurationException(String msg) {
		super(msg);
	}
}
