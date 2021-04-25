package it.cnr.istc.pst.platinum.ai.framework.time.ex;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author alessandro
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
