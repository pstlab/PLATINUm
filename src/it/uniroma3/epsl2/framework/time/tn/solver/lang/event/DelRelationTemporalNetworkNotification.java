package it.uniroma3.epsl2.framework.time.tn.solver.lang.event;

import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DelRelationTemporalNetworkNotification extends TemporalNetworkNotification 
{
	private List<TimePointConstraint> rels;
	
	/**
	 * 
	 */
	protected DelRelationTemporalNetworkNotification() {
		super(TemporalNetworkNotificationTypes.DEL_REL);
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
		return this.rels;
	}
}
