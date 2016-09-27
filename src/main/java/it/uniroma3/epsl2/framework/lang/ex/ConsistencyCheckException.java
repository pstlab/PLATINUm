package it.uniroma3.epsl2.framework.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public class ConsistencyCheckException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public ConsistencyCheckException(String msg) {
		super(msg);
	}
}
