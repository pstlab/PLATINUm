package it.istc.pst.platinum.framework.microkernel.resolver.ex;

/**
 * 
 * @author anacleto
 *
 */
public class UnsolvableFlawException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public UnsolvableFlawException(String msg) {
		super(msg);
	}

}
