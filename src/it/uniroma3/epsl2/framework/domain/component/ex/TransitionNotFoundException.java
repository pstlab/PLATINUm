package it.uniroma3.epsl2.framework.domain.component.ex;

/**
 * 
 * @author anacleto
 *
 */
public class TransitionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public TransitionNotFoundException(String msg) {
		super(msg);
	}
}
