package it.uniroma3.epsl2.framework.parameter.ex;

import it.uniroma3.epsl2.framework.microkernel.lang.ex.ConsistencyCheckException;

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
