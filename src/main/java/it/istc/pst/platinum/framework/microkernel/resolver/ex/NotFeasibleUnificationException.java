package it.istc.pst.platinum.framework.microkernel.resolver.ex;

/**
 * 
 * @author anacleto
 *
 */
public class NotFeasibleUnificationException extends UnsolvableFlawFoundException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public NotFeasibleUnificationException(String msg) {
		super(msg);
	}
}
