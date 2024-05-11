package it.cnr.istc.pst.platinum.ai.framework.parameter.ex;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterConsistencyException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public ParameterConsistencyException(String msg) {
		super(msg);
	}
}
