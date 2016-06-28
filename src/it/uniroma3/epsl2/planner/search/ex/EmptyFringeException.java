package it.uniroma3.epsl2.planner.search.ex;

/**
 * 
 * @author anacleto
 *
 */
public class EmptyFringeException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public EmptyFringeException(String msg) {
		super(msg);
	}
}
