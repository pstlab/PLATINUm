package it.cnr.istc.pst.platinum.ai.framework.domain.component.ex;

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
