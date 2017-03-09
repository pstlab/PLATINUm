package it.uniroma3.epsl2.framework.domain.component.ex;

/**
 * 
 * @author anacleto
 *
 */
public class DecisionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public DecisionNotFoundException(String msg) {
		super(msg);
	}
}
