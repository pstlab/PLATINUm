package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePointDistanceConstraint;

/**
 * 
 * @author alessandro
 *
 */
public class AddRelationTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePointDistanceConstraint> rels;
	
	/**
	 * 
	 */
	protected AddRelationTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.ADD_REL);
		this.rels = new LinkedList<TimePointDistanceConstraint>();
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addRelation(TimePointDistanceConstraint rel) {
		this.rels.add(rel);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimePointDistanceConstraint> getRels() {
		return new ArrayList<>(this.rels);
	}
}
