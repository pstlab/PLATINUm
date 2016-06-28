package it.uniroma3.epsl2.framework.microkernel.resolver.ex;

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
