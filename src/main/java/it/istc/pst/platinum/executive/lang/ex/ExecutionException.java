package it.istc.pst.platinum.executive.lang.ex;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected ExecutionFailureCause cause; 
	
	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ExecutionException(String msg, ExecutionFailureCause cause) {
		super(msg);
		this.cause = cause;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCause getFailureCause() {
		return this.cause;
	}
}
