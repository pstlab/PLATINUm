package it.istc.pst.platinum.framework.microkernel.resolver.ex;

/**
 * 
 * @author anacleto
 *
 */
public class NotFeasibleExpansionException extends UnsolvableFlawFoundException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public NotFeasibleExpansionException(String msg) {
		super(msg);
	}
}
