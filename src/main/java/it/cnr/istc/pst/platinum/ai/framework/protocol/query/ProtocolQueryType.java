package it.cnr.istc.pst.platinum.ai.framework.protocol.query;

import it.cnr.istc.pst.platinum.ai.framework.protocol.query.get.GetFlexibleTimelinesProtocolQuery;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.get.GetPlanProtocolQuery;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.get.GetSingleFlexibleTimelineProtocolQuery;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.show.ShowComponentProtocolQuery;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum ProtocolQueryType 
{
	/**
	 * The GET command allows users to dynamically access plan database
	 * information about current problem state
	 * 
	 * This command (get all) returns information about flexible
	 * tokens for each domain timeline. It returns a list
	 * of the tokens currently posted over domain timeline. For each token
	 * the command specifies flexible duration of token's start time, 
	 * end time and temporal duration. 
	 */
	GET_FLEXIBLE_TIMELINES(GetFlexibleTimelinesProtocolQuery.class),
	
	/**
	 * 
	 */
	GET_PLAN(GetPlanProtocolQuery.class),
	
	/**
	 * The GET command allows users to dynamically access plan database
	 * information about current problem state
	 * 
	 * This command (get Component.timeline) returns information about
	 * flexible tokens over the specified timeline. It returns a list
	 * of the tokens currently posted over the timeline. For each token
	 * the command specifies flexible duration of token's start time, 
	 * end time and temporal duration. 
	 */
	GET_SINGLE_FLEXIBLE_TIMELINE(GetSingleFlexibleTimelineProtocolQuery.class),
	
	/**
	 * The SHOW command allows users to read domain content of the 
	 * planning problem such as components or synchronization rules.
	 * 
	 * This command (show command) returns a description of 
	 * the components defined in the problem domain.
	 */
	SHOW_COMPONENTS(ShowComponentProtocolQuery.class),
	
	/**
	 * The SHOW command allows users to read domain content of the 
	 * planning problem such as components or synchronization rules.
	 * 
	 * This command (show synchronizations) returns a description 
	 * of the synchronization rules specified in the problem domain.
	 */
	SHOW_SYNCHRONIZATIONS(null);
	
	private Class<? extends ProtocolQuery> queryClass;
	
	/**
	 * 
	 * @param clazz
	 */
	private ProtocolQueryType(Class<? extends ProtocolQuery> clazz) {
		this.queryClass = clazz;
	}
	
	/**
	 * 
	 * @return
	 */
	public Class<? extends ProtocolQuery> getQueryClass() {
		return queryClass;
	}
}
