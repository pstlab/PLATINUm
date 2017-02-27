package it.uniroma3.epsl2.framework.time.tn.solver.lang.event;

import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DelRelationTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePointDistanceConstraint> rels;
	
	/**
	 * 
	 */
	protected DelRelationTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.DEL_REL);
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
		return this.rels;
	}
}
