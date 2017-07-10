package it.uniroma3.epsl2.framework.parameter.ex;

import it.uniroma3.epsl2.framework.microkernel.lang.ex.ConstraintPropagationException;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterConstraintPropagationException extends ConstraintPropagationException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public ParameterConstraintPropagationException(String msg) {
		super(msg);
	}
}
