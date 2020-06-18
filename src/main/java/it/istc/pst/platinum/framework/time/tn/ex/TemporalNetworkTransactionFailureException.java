package it.istc.pst.platinum.framework.time.tn.ex;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class TemporalNetworkTransactionFailureException extends InconsistentDistanceConstraintException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public TemporalNetworkTransactionFailureException(String msg) {
		super(msg);
	}
}
