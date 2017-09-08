package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class SolutionPlan 
{
	private long horizion;
	private long solvingTime;
	private String name;
	private Set<Timeline> timelines;
	private Set<Timeline> observations;
	private List<Relation> relations;
	private PlanControllabilityType controllability;
	private double makespan;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public SolutionPlan(String name, long horizon) 
	{
		this.horizion = horizon;
		this.name = name;
		this.timelines = new HashSet<>();
		this.observations = new HashSet<>();
		this.relations = new ArrayList<>();
		this.controllability = PlanControllabilityType.UNKNOWN;
		this.makespan = horizon;
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
	 * @param makespan
	 */
	public void setMakespan(double makespan) {
		this.makespan = makespan;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMakespan() {
		return makespan;
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
	 * @return
	 */
	public List<Timeline> getObservations() {
		return new ArrayList<>(this.observations);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Timeline> getAllTimelines() {
		List<Timeline> list = new ArrayList<>();
		list.addAll(this.timelines);
		list.addAll(this.observations);
		return list;
	}
	
	/**
	 * 
	 * @param component
	 */
	public void add(DomainComponent component) 
	{
		// check component type
		switch (component.getType())
		{
			case SV_FUNCTIONAL : 
			case SV_PRIMITIVE : {
				// get the state variable
				StateVariable sv = (StateVariable) component;
				// get the timeline 
				Timeline tl = new Timeline(sv);
				// add to timeline
				this.timelines.add(tl);
			}
			break;
			
			case SV_EXTERNAL : {
				// get the state variable 
				StateVariable sv = (StateVariable) component;
				// get the timeline 
				Timeline tl = new Timeline(sv);
				// add to observations
				this.observations.add(tl);
			}
			break;
			
			case RESOURCE_DISCRETE : case RESOURCE_RESERVOIR : 
			{
				/*
				 * FIXME: How to manage resources? 
				 * 
				 * Such components should be represented through different data structures...
				 */
			}
			break;
			
			case PDB : {
				// ignore this type of components
			}
			break;
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
	public String export() 
	{
		// prepare plan description
		String plan = "horizon = " + this.horizion + "\n";
		plan += "plan {\n";
		
		// print timelines
		plan += "\ttimelines {\n";
		for (Timeline tl : this.timelines) {
			plan += "\t\t" + tl.getName() + " {\n";
			for (Token token : tl.getTokens()) {
				// get token's value
				String value = token.getPredicate().getValue().getLabel().replaceFirst("_", "");
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
				case TEMPORAL_CONSTRAINT : 
				{
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
					// get elements
					Decision ref = trel.getReference();
					Decision target = trel.getTarget();
					long[][] bounds = trel.getBounds();
					// add relation description
					plan += "\t\t" + ref.getComponent().getName() + " " + ref.getToken().getInterval().getId() + ""
							+ " " + trel.getType().name() + "";
					// check type
					if (trel.getType().equals(RelationType.MEETS) || 
							rel.getType().equals(RelationType.MET_BY) || 
							trel.getType().equals(RelationType.EQUALS)) {
						// do not print temporal bounds
						plan += " " + target.getComponent().getName() + " " + target.getToken().getInterval().getId() + ""
								+ "\n";
					} else {
						// print temporal bounds
						plan += "" + (bounds.length >= 1 && bounds[0] != null && bounds[0].length == 2 ? " [" + bounds[0][0] + ", " + bounds[0][1] + "] " : "") + ""
								+ "" + (bounds.length >= 2 && bounds[1] != null && bounds[1].length == 2 ? " [" + bounds[1][0] + "," + bounds[1][1] + "] " : "") + ""
								+ " " + target.getComponent().getName() + " " + target.getToken().getInterval().getId() + ""
								+ "\n";
					}
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
		SolutionPlan other = (SolutionPlan) obj;
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
	public String toString() 
	{
		// initialize solution plan description
		String description = "Plan (H= " + this.horizion + ", makespan= " + this.makespan +" controllability= " + this.controllability + " #timelines= " + this.timelines.size() + ", #observations= " + this.observations.size() + ", #relations= " + this.relations.size() + ")\n";
		// print timelines 
		description += "timelines {\n";
		for (Timeline tl : this.timelines) 
		{
			description += "\t" + tl.getComponent().getName() + " {\n";
			// get tokens
			for (Token token : tl.getTokens()) {
				description += "\t\t" + token + "\n";
			}
			description += "\t}\n";
 		}
		// end decisions
		description	+= "}\n\n";
		
		// print observations
		description += "observations {\n";
		for (Timeline tl : this.observations) {
			description += "\t\tobservation." + tl.getComponent().getName() + " {\n";
			// get tokens 
			for (Token token : tl.getTokens()) {
				description += "\t\t\t" + token + "\n";
			}
			description += "\t\t}\n";
		}
		description += "}\n";
		
		// print relations
		description += "relations {\n";
		for (Relation rel : this.relations) {
			description += "\t " + rel.getType() +  " {"
					+ "" + rel.getReference().getToken().getId() + ":" + rel.getReference().getToken().getPredicate().getGroundSignature()+", "
					+ "" + rel.getTarget().getToken().getId() + ":" + rel.getTarget().getToken().getPredicate().getGroundSignature() +"}\n";
		}
		description += "}\n";
		// get description
		return description;
	}
	
	
}
