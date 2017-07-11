package it.istc.pst.platinum.framework.microkernel.resolver.ex;

/**
 * 
 * @author anacleto
 *
 */
public class UnsolvableFlawFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public UnsolvableFlawFoundException(String msg) {
		super(msg);
	}

}
