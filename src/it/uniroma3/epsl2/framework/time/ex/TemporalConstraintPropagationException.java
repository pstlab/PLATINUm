package it.uniroma3.epsl2.framework.time.ex;

import it.uniroma3.epsl2.framework.lang.ex.ConstraintPropagationException;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalConstraintPropagationException extends ConstraintPropagationException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public TemporalConstraintPropagationException(String msg) {
		super(msg);
	}
}
