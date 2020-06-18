package it.istc.pst.platinum.framework.parameter.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;

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
