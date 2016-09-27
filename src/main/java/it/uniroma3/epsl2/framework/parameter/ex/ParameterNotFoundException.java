package it.uniroma3.epsl2.framework.parameter.ex;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public ParameterNotFoundException(String msg) {
		super(msg);
	}
}
