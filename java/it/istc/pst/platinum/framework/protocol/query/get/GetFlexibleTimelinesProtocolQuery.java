package it.istc.pst.platinum.framework.protocol.query.get;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.protocol.lang.TimelineProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.query.ProtocolQuery;
import it.istc.pst.platinum.framework.protocol.query.ProtocolQueryType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class GetFlexibleTimelinesProtocolQuery extends ProtocolQuery 
{
	private List<TimelineProtocolDescriptor> timelines;
	
	/**
	 * 
	 */
	protected GetFlexibleTimelinesProtocolQuery() {
		super(ProtocolQueryType.GET_FLEXIBLE_TIMELINES);
		this.timelines = new ArrayList<TimelineProtocolDescriptor>();
	}
	
	/**
	 * 
	 * @param tl
	 * @param tokens
	 */
	public void add(TimelineProtocolDescriptor tl) {
		this.timelines.add(tl);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimelineProtocolDescriptor> getTimelines() {
		return new ArrayList<TimelineProtocolDescriptor>(this.timelines);
	}
}
