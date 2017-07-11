package it.istc.pst.platinum.framework.parameter.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;

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
