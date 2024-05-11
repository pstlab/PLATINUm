package it.cnr.istc.pst.platinum.ai.executive.lang.ex;

import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author alessandro
 *
 */
public class ExecutionPreparationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected ExecutionFailureCause cause; 
	
	/**
	 * 
	 * @param msg
	 */
	public ExecutionPreparationException(String msg) {
		super(msg);
	}
	
}
