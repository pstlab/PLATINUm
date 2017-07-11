package it.istc.pst.platinum.framework.microkernel.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public class DomainComponentNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public DomainComponentNotFoundException(String msg) {
		super(msg);
	}
}
