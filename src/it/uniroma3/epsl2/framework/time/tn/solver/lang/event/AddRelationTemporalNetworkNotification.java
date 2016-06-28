package it.uniroma3.epsl2.framework.time.tn.solver.lang.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class AddRelationTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePointConstraint> rels;
	
	/**
	 * 
	 */
	protected AddRelationTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.ADD_REL);
		this.rels = new LinkedList<TimePointConstraint>();
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addRelation(TimePointConstraint rel) {
		this.rels.add(rel);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimePointConstraint> getRels() {
		return new ArrayList<>(this.rels);
	}
}
