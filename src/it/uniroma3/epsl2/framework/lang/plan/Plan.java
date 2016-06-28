package it.uniroma3.epsl2.framework.lang.plan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.lang.plan.timeline.Timeline;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class Plan {
	
	private long horizion;
	private long solvingTime;
	private String name;
	private Set<Timeline> timelines;
	private Set<Timeline> observations;
	private List<Relation> relations;
	private PlanControllabilityType controllability;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public Plan(String name, long horizon) {
		this.horizion = horizon;
		this.name = name;
		this.timelines = new HashSet<>();
		this.observations = new HashSet<>();
		this.relations = new ArrayList<>();
		this.controllability = PlanControllabilityType.UNKNOWN;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.horizion;
	}
	
	/**
	 * 
	 * @param solvingTime
	 */
	public void setSolvingTime(long solvingTime) {
		this.solvingTime = solvingTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSolvingTime() {
		return solvingTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public PlanControllabilityType getPlanControllabilityType() {
		return this.controllability;
	}
	
	/**
	 * 
	 * @param pseudo
	 */
	public void setControllability(PlanControllabilityType type) {
		this.controllability = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Timeline> getTimelines() {
		return new ArrayList<>(this.timelines);
	}
	
	/**
	 * 
	 * @param component
	 */
	public void add(DomainComponent component) {
		Timeline tl = new Timeline(component);
		if (component.isExternal()) {
			this.observations.add(tl);
		} else {
			this.timelines.add(tl);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getRelations() {
		return relations;
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void add(Relation rel) {
		this.relations.add(rel);
	}
	
	/**
	 * 
	 * @return
	 */
	public String export() {
		// prepare plan description
		String plan = "horizon = " + this.horizion + "\n";
		plan += "plan {\n";
		
		// print timelines
		plan += "\ttimelines {\n";
		for (Timeline tl : this.timelines) {
			plan += "\t\t" + tl.getName() + " {\n";
			for (Token token : tl.getTokens()) {
				// get token's value
				String value = token.getPredicate().getValue().getLabel();
				TimePoint end = token.getInterval().getEndTime();
				// add token description
				plan += "\t\t\t token " + token.getInterval().getId() + " " + (token.isControllable() ? "" : "uncontrollable") +  " {"
							+ " " + value + " [" + end.getLowerBound() + ", " + end.getUpperBound() + "] "
							+ "[" + token.getInterval().getDurationLowerBound() + "," + token.getInterval().getDurationUpperBound() + "] "
						+ "}\n";
			}
			plan += "\t\t}\n";
		}
		plan += "\t}\n";
		
		// print relations
		plan += "\trelations {\n";
		for (Relation rel : this.relations) {
			// check relation type
			switch (rel.getCategory()) 
			{
				// temporal relation
				case TEMPORAL_CONSTRAINT : {
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
				
					// get elements
					Decision ref = trel.getReference();
					Decision target = trel.getTarget();
					long[][] bounds = trel.getBounds();
					// add relation description
					plan += "\t\t" + ref.getComponent().getName() + " " + ref.getToken().getInterval().getId() + ""
							+ " " + trel.getType().name() + ""
							+ "" + (bounds.length >= 1 && bounds[0] != null && bounds[0].length == 2 ? " [" + bounds[0][0] + ", " + bounds[0][1] + "] " : "") + ""
							+ "" + (bounds.length >= 2 && bounds[1] != null && bounds[1].length == 2 ? " [" + bounds[1][0] + "," + bounds[1][1] + "] " : "") + ""
							+ " " + target.getComponent().getName() + " " + target.getToken().getInterval().getId() + ""
							+ "\n";
				}
				break;
				
				// parameter relation
				case PARAMETER_CONSTRAINT : {
					
					/*
					 * TODO <<<<----
					 */
				}
				break;
			}
		}
		plan += "\t}\n";
		
		// close plan
		plan += "}\n";
		
		// print observations
		plan += "observation {\n";
		plan += "\ttimelines {\n";
		for (Timeline tl : this.observations) {
			plan += "\t\t" + tl.getName() + " {\n";
			for (Token token : tl.getTokens()) {
				plan += "\t\t\t token " + token.getInterval().getId() + " {"
							+ " " + token.getPredicate().getValue().getLabel() + ""
							+ " [" + token.getInterval().getEndTime().getLowerBound() + ", " + token.getInterval().getEndTime().getUpperBound() + "]"
							+ " [" + token.getInterval().getNominalDurationLowerBound() + ", " + token.getInterval().getNominalDurationUpperBound() + "]"
						+ " }\n";
			}
			plan += "\t\t}\n";
		}
		plan += "\t}\n";
		// end observations
		plan += "}\n";
		return plan;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plan other = (Plan) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String description = "Plan (H= " + this.horizion + ", controllability= " + this.controllability + " #timelines= " + this.timelines.size() + ", #observations= " + this.observations.size() + ", #relations= " + this.relations.size() + ") {\n";
		// print decisions 
		description += "\tdecisions {\n";
		for (Timeline tl : this.timelines) {
			description += "\t\ttimeline." + tl.getComponent().getName() + " {\n";
			// get tokens
			for (Token token : tl.getTokens()) {
				description += "\t\t\t" + token + "\n";
			}
			description += "\t\t}\n";
 		}
		// end decisions
		description	+= "\t}\n";
		
		// print observations
		description += "\tobservations {\n";
		for (Timeline tl : this.observations) {
			description += "\t\tobservation." + tl.getComponent().getName() + " {\n";
			// get tokens 
			for (Token token : tl.getTokens()) {
				description += "\t\t\t" + token + "\n";
			}
			description += "\t\t}\n";
		}
		description += "\t}\n";
		
		// print relations
		description += "\trelations {\n";
		for (Relation rel : this.relations) {
			description += "\t\t" + rel + "\n";
		}
		description += "\t}\n";
		// close plan description
		description += "}\n";
		// get description
		return description;
	}
	
	
}
