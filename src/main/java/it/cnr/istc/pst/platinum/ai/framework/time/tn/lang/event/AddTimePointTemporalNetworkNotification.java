package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandro
 *
 */
public class AddTimePointTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePoint> points;
	
	/**
	 * 
	 */
	protected AddTimePointTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.ADD_TP);
		this.points = new LinkedList<TimePoint>();
	}
	
	/**
	 * 
	 * @param tp
	 */
	public void addTimePoint(TimePoint tp) {
		this.points.add(tp);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimePoint> getPoints() {
		return new ArrayList<>(this.points);
	}
}
