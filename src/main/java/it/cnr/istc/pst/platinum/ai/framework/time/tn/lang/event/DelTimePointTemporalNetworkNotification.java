package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DelTimePointTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePoint> points;
	
	/**
	 * 
	 */
	protected DelTimePointTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.DEL_TP);
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
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.points.isEmpty();
	}
}
