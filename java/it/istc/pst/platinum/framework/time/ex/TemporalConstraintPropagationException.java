package it.istc.pst.platinum.framework.time.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;

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
