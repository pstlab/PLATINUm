package it.istc.pst.platinum.framework.protocol.lang;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.protocol.lang.relation.RelationProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class PlanProtocolDescriptor 
{
	private long origin;
	private long horizon;
	private List<TimelineProtocolDescriptor> timelines;
	private List<RelationProtocolDescriptor> relations;
	private List<TimelineProtocolDescriptor> observations;
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	public PlanProtocolDescriptor(long origin, long horizon) {
		this.origin = origin;
		this.horizon = horizon;
		this.timelines = new ArrayList<>();
		this.relations = new ArrayList<>();
		this.observations = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin() {
		return this.origin;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return horizon;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimelineProtocolDescriptor> getTimelines() {
		return timelines;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimelineProtocolDescriptor> getObservations() {
		return observations;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TimelineProtocolDescriptor> getAllTimelines() {
		List<TimelineProtocolDescriptor> list = new ArrayList<>();
		list.addAll(this.getTimelines());
		list.addAll(this.getObservations());
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RelationProtocolDescriptor> getRelations() {
		return relations;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public List<RelationProtocolDescriptor> getRelations(TokenProtocolDescriptor reference, TokenProtocolDescriptor target) {
		// list of relation between tokens
		List<RelationProtocolDescriptor> list = new ArrayList<>();
		for (RelationProtocolDescriptor rel : this.relations) {
			if (rel.getFrom().equals(reference) && rel.getTo().equals(target)) {
				list.add(rel);
			}
		}
		// get list
		return list;
	}
	
	/**
	 * 
	 * @param timeline
	 */
	public void addTimeline(TimelineProtocolDescriptor timeline) {
		if (timeline.isExternal()) { 
			this.observations.add(timeline); 
		}
		else {
			this.timelines.add(timeline);
		}
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addRelation(RelationProtocolDescriptor rel) {
		this.relations.add(rel);
	}
	
	/**
	 * 
	 * @return
	 */
	public String export() {
		String plan = "horizon = " + this.horizon + "\n";
		plan += "plan {\n";
		
		// print timelines
		plan += "\ttimelines {\n";
		for (TimelineProtocolDescriptor tl : this.timelines) {
			plan += "\t\t" + tl.getName() + " {\n";
			for (TokenProtocolDescriptor token : tl.getTokens()) {
				plan += "\t\t\t" + token.export() + "\n";
			}
			plan += "\t\t}\n";
		}
		plan += "\t}\n";
		
		// print relations
		plan += "\trelations {\n";
		for (RelationProtocolDescriptor rel : this.relations) {
			// avoid meets on tokens of the same timeline
			if (!(rel.getFromTimelineName().equals(rel.getToTimelineName()) && rel.getType().equals("meets"))) {
				plan += "\t\t" + rel.export() + "\n";
			}
		}
		plan += "\t}\n";
		
		// close plan
		plan += "}\n";
		
		// check observations
		if (!this.observations.isEmpty()) {
			// print observations
			plan += "observation {\n";
			plan += "\ttimelines {\n";
			for (TimelineProtocolDescriptor tl : this.observations) {
				plan += "\t\t" + tl.getName() + " {\n";
				for (TokenProtocolDescriptor token : tl.getTokens()) {
					plan += "\t\t\t" + token.export() + "\n";
				}
				plan += "\t\t}\n";
			}
			plan += "\t}\n";
			// end observations
			plan += "}\n";
		}
		// get the plan
		return plan;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.export();
	}
}
