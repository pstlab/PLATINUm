package it.cnr.istc.pst.platinum.ai.framework.protocol.query.get;

import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TimelineProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.ProtocolQuery;
import it.cnr.istc.pst.platinum.ai.framework.protocol.query.ProtocolQueryType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class GetSingleFlexibleTimelineProtocolQuery extends ProtocolQuery 
{
	private String component;
	private String name;
	private TimelineProtocolDescriptor timeline;

	/**
	 * 
	 */
	protected GetSingleFlexibleTimelineProtocolQuery() {
		super(ProtocolQueryType.GET_SINGLE_FLEXIBLE_TIMELINE);
	}
	
	/**
	 * 
	 * @param component
	 */
	public void setQueryComponent(String component) {
		this.component = component;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setQueryTimelineName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimelineName() {
		return name;
	}
	
	/**
	 * 
	 * @param tl
	 */
	public void setTimelineDescriptor(TimelineProtocolDescriptor tl) {
		this.timeline = tl;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimelineProtocolDescriptor getTimelineDescriptor() {
		return this.timeline;
	}
}
