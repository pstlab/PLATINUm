package it.istc.pst.platinum.framework.time.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public PseudoControllabilityException(String msg) {
		super(msg);
	}
}
