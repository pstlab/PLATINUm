package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex;

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
