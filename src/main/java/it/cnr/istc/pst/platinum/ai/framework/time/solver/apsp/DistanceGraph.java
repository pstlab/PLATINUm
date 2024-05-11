package it.cnr.istc.pst.platinum.ai.framework.time.solver.apsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandro
 *
 */
public class DistanceGraph 
{
	private long infty;												// temporal horizon
	private Map<Integer, TimePoint> nodes;							// list of points
	private Map<TimePoint, Map<TimePoint, Long>> edges;				// list of weighted edges
	
	/**
	 * 
	 */
	protected DistanceGraph() {
		this.nodes = new HashMap<>();
		this.edges = new HashMap<>();
	}
	
	/**
	 * 
	 * @param horizon
	 */
	public void setInfity(long horizon) {
		this.infty = horizon + 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getInfity() {
		return this.infty;
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.nodes.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimePoint> getPoints() {
		return new ArrayList<>(this.nodes.values());
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	public List<TimePoint> getAdjacents(TimePoint point) {
		
		// list of adjacent time points
		List<TimePoint> list = new ArrayList<>();
		if (this.edges.containsKey(point)) {
			// add adjacent time points
			list.addAll(this.edges.get(point).keySet());
		}
		
		// get list
		return list;
	}
	
	/**
	 * 
	 * @param point
	 */
	public void add(TimePoint point) {
		this.nodes.put(point.getId(), point);
		this.edges.put(point, new HashMap<TimePoint, Long>());
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param weight
	 */
	public void add(TimePoint source, TimePoint target, long weight) {
		// check graph
		if (!this.edges.containsKey(source)) {
			this.edges.put(source, new HashMap<TimePoint, Long>());
		}
		
		// update distance
		this.edges.get(source).put(target, weight);
	}
	
	/**
	 * Delete the time point from the distance graph and all 
	 * the related edges
	 * 
	 * @param point
	 */
	public void delete(TimePoint point) {
		// remove from points
		this.nodes.remove(point.getId());
		// remove outgoing edges
		this.edges.remove(point);
		// remove incoming edges
		for (TimePoint i : this.nodes.values()) {
			// remove edges that involve the node just deleted
			this.edges.get(i).remove(point);
		}
	}
	
	/**
	 * Delete the edge from the distance graph
	 * 
	 * @param source
	 * @param target
	 */
	public void delete(TimePoint source, TimePoint target) {
		// remove edge
		if (this.edges.containsKey(source)) {
			this.edges.get(source).remove(target);
		}
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public long getDistance(TimePoint source, TimePoint target) {
		
		// initialize to infinity
		long distance = this.infty;
		// check if an edge exists
		if (this.edges.containsKey(source) && this.edges.get(source).containsKey(target)) {
			// get distance
			distance = this.edges.get(source).get(target);
		}
		// return distance
		return distance;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// print the distance graph
		String string = "{\n";
		
		for (TimePoint reference : this.edges.keySet()) {
			string += "\tpoint: " + reference.getId() + ",\n"
					+ "\tedges: [\n";

			for (TimePoint target : this.edges.get(reference).keySet()) {
				string += "\t\t{ point: " + target.getId() +", distance: " + this.edges.get(reference).get(target) + "},\n";
			}
			
			string += "\t]\n";
			
		}
		
		string += "}\n";
		return string;
	}
	
}
