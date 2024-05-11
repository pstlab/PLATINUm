package it.cnr.istc.pst.platinum.ai.framework.time.ex;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConstraintPropagationException;

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
