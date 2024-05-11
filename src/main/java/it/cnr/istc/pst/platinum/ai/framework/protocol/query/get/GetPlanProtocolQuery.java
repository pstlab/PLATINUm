package it.cnr.istc.pst.platinum.ai.framework.protocol.query.get;

import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.ProtocolQuery;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.ProtocolQueryType;

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
