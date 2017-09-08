package it.istc.pst.platinum.protocol.query.get;

import it.istc.pst.platinum.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.protocol.query.ProtocolQuery;
import it.istc.pst.platinum.protocol.query.ProtocolQueryType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class GetPlanProtocolQuery extends ProtocolQuery 
{
	private PlanProtocolDescriptor plan;
	
	/**
	 * 
	 */
	protected GetPlanProtocolQuery() {
		super(ProtocolQueryType.GET_PLAN);
	}
	
	/**
	 * 
	 * @return
	 */
	public PlanProtocolDescriptor getPlan() {
		return plan;
	}
	
	/**
	 * 
	 * @param plan
	 */
	public void setPlan(PlanProtocolDescriptor plan) {
		this.plan = plan;
	}
}
