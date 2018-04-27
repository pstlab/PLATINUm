package it.istc.pst.platinum.executive.dc;

/**
 * 
 * @author anacleto
 *
 */
public interface DCChecker 
{
	/**
	 * 
	 * @param status
	 * @return
	 * 
	 */
	public DCResult evaluate(PlanExecutionStatus status);
	
	/**
	 * 
	 * @param status
	 * @return
	 * 
	 */
	public boolean notify(PlanExecutionStatus status);
	
}
