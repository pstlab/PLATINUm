package it.istc.pst.platinum.framework.time.tn.ex;

/**
 * This exception is thrown when trying to add a requirement constraint between two
 * time points that are already constrained by a contingent link.
 * 
 * @author anacleto
 *
 */
public class NotCompatibleConstraintsFoundException extends InconsistentDistanceConstraintException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public NotCompatibleConstraintsFoundException(String msg) {
		super(msg);
	}
}
