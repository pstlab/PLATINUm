package it.cnr.istc.pst.platinum.ai.framework.parameter.ex;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConstraintPropagationException;

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
