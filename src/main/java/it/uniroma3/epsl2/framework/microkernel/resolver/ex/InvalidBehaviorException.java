package it.uniroma3.epsl2.framework.microkernel.resolver.ex;

/**
 * 
 * @author anacleto
 *
 */
public class InvalidBehaviorException extends UnsolvableFlawFoundException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public InvalidBehaviorException(String msg) {
		super(msg);
	}
}
