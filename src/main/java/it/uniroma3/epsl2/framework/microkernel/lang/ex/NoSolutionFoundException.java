package it.uniroma3.epsl2.framework.microkernel.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public class NoSolutionFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public NoSolutionFoundException(String msg) {
		super(msg);
	}
}
