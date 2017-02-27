package it.uniroma3.epsl2.framework.time.tn.solver.lang.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;

/**
 * 
 * @author alessandroumbrico
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
