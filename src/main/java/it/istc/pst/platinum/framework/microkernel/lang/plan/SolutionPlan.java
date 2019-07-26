package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.parameter.lang.EnumerationParameter;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameter;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.parameter.lang.ParameterType;
import it.istc.pst.platinum.framework.protocol.lang.ParameterTypeDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.ProtocolLanguageFactory;
import it.istc.pst.platinum.framework.protocol.lang.TimelineProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.relation.RelationProtocolDescriptor;

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
			
			case PLAN_DATABASE : {
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
	 * @param plan
	 * @return
	 */
	public PlanProtocolDescriptor export() {
		// generate protocol plan descriptor
		PlanProtocolDescriptor exported = this.generatePlanDescriptor();
		// get exported plan
		return exported;
	}
	
	/**
	 * 
	 * @return
	 */
	protected PlanProtocolDescriptor generatePlanDescriptor() 
	{
		// get language factory
		ProtocolLanguageFactory factory = new ProtocolLanguageFactory(this.horizion);
		
		// create plan descriptor
		PlanProtocolDescriptor plan = factory.createPlanDescriptor(0, this.horizion);
		// create an index
		Map<Token, TokenProtocolDescriptor> index = new HashMap<>();
		// create timeline descriptors
		for (Timeline tl : this.timelines) 
		{
			// get the state variable related to the timeline
			StateVariable comp = tl.getComponent();
			// initialize descriptor
			TimelineProtocolDescriptor timelineDescriptor = factory.createTimelineDescriptor(
					comp.getName(), 
					tl.getName(), 
					tl.isObservation());
			
			// get tokens of the timeline
			for (Token token : tl.getTokens()) 
			{
				// prepare the array of parameter names, values, and types
				String[] paramNames = new String[token.getPredicate().getParameters().length];
				ParameterTypeDescriptor[] paramTypes = new ParameterTypeDescriptor[token.getPredicate().getParameters().length];
				long[][] paramBounds = new long[token.getPredicate().getParameters().length][];
				String[][] paramValues = new String[token.getPredicate().getParameters().length][];
				for (int i = 0; i < token.getPredicate().getParameters().length; i++)
				{
					// get parameter
					Parameter<?> param = token.getPredicate().getParameterByIndex(i);
					// set parameter name
					paramNames[i] = param.getLabel();
					
					// check parameter type
					if (param.getType().equals(ParameterType.NUMERIC_PARAMETER_TYPE)) 
					{
						// get numeric parameter
						NumericParameter numPar = (NumericParameter) param;
						// set lower bound and upper bound
						paramBounds[i] = new long[] {
								numPar.getLowerBound(),
								numPar.getUpperBound()
						};
						// set default value to parameter values
						paramValues[i] = new String[] {};
						// set parameter type
						paramTypes[i] = ParameterTypeDescriptor.NUMERIC;
					}
					else if (param.getType().equals(ParameterType.ENUMERATION_PARAMETER_TYPE)) 
					{
						// enumeration parameter
						EnumerationParameter enuPar = (EnumerationParameter) param;
						// one single value is expected
						paramValues[i] = new String[] {
								enuPar.getValues()[0]
						};
						// set default value to parameter bounds
						paramBounds[i] = new long[] {};
						// set parameter type
						paramTypes[i] = ParameterTypeDescriptor.ENUMERATION;
					}
					else {
						throw new RuntimeException("Unknown parameter type:\n- type: " + param.getType() + "\n");
					}
				}
			
				// create token descriptor
				TokenProtocolDescriptor tokenDescriptor = factory.createTokenDescriptor(
						timelineDescriptor,
						token.getPredicate().getValue().getLabel(),
						new long [] {
								token.getInterval().getStartTime().getLowerBound(), 
								token.getInterval().getStartTime().getUpperBound()
						}, 
						new long[] {
								token.getInterval().getEndTime().getLowerBound(),
								token.getInterval().getEndTime().getUpperBound()
						}, 
						new long[] {
								token.getInterval().getDurationLowerBound(),
								token.getInterval().getDurationUpperBound()
						}, 
						paramNames, paramTypes, paramBounds, paramValues, token.getStartExecutionState());

				// update index
				index.put(token, tokenDescriptor);
			}
			
//			// add an undefined gap for the last token if necessary
//			Token last = tl.getTokens().get(tl.getTokens().size() - 1);
//			// check schedule
//			if (last.getInterval().getEndTime().getLowerBound() < this.horizion) {
//				// create "empty" token description
//				factory.createUndefinedTokenDescriptor(timelineDescriptor, 
//						new long[] {
//								last.getInterval().getEndTime().getLowerBound(),
//								last.getInterval().getEndTime().getUpperBound()
//						}, 
//						new long [] {
//								this.horizion,
//								this.horizion
//						}, 
//						new long [] {
//								(this.horizion - last.getInterval().getEndTime().getUpperBound()),
//								(this.horizion - last.getInterval().getEndTime().getLowerBound())
//						});
//			}
			
			// add timeline to plan
			plan.addTimeline(timelineDescriptor);
		}
		
		// create timeline descriptors
		for (Timeline tl : this.observations) 
		{
			// get the state variable related to the timeline
			StateVariable comp = tl.getComponent();
			// initialize descriptor
			TimelineProtocolDescriptor timelineDescriptor = factory.createTimelineDescriptor(
					comp.getName(), 
					tl.getName(), 
					tl.isObservation());
			
			// get tokens of the timeline
			for (Token token : tl.getTokens()) 
			{
				// prepare the array of parameter names, values, and types
				String[] paramNames = new String[token.getPredicate().getParameters().length];
				ParameterTypeDescriptor[] paramTypes = new ParameterTypeDescriptor[token.getPredicate().getParameters().length];
				long[][] paramBounds = new long[token.getPredicate().getParameters().length][];
				String[][] paramValues = new String[token.getPredicate().getParameters().length][];
				for (int i = 0; i < token.getPredicate().getParameters().length; i++)
				{
					// get parameter
					Parameter<?> param = token.getPredicate().getParameterByIndex(i);
					// check parameter type
					if (param.getType().equals(ParameterType.NUMERIC_PARAMETER_TYPE)) {
						// get numeric parameter
						NumericParameter numPar = (NumericParameter) param;
						// set lower bound and upper bound
						paramBounds[i] = new long[] {
								numPar.getLowerBound(),
								numPar.getUpperBound()
						};
						// set default value to parameter values
						paramValues[i] = new String[] {};
					}
					else if (param.getType().equals(ParameterType.ENUMERATION_PARAMETER_TYPE)) {
						// enumeration parameter
						EnumerationParameter enuPar = (EnumerationParameter) param;
						// one single value is expected
						paramValues[i] = new String[] {
								enuPar.getValues()[0]
						};
						// set default value to parameter bounds
						paramBounds[i] = new long[] {};
					}
					else {
						throw new RuntimeException("Unknown parameter type:\n- type: " + param.getType() + "\n");
					}
				}
			
				// create token descriptor
				TokenProtocolDescriptor tokenDescriptor = factory.createTokenDescriptor(
						timelineDescriptor, 
						token.getPredicate().getValue().getLabel(),
						new long [] {
								token.getInterval().getStartTime().getLowerBound(), 
								token.getInterval().getStartTime().getUpperBound()
						}, 
						new long[] {
								token.getInterval().getEndTime().getLowerBound(),
								token.getInterval().getEndTime().getUpperBound()
						}, 
						new long[] {
								token.getInterval().getDurationLowerBound(),
								token.getInterval().getDurationUpperBound()
						}, 
						paramNames, paramTypes, paramBounds, paramValues, token.getStartExecutionState());

				// update index
				index.put(token, tokenDescriptor);
			}
			
//			// add an undefined gap for the last token if necessary
//			Token last = tl.getTokens().get(tl.getTokens().size() - 1);
//			// check schedule
//			if (last.getInterval().getEndTime().getLowerBound() < this.horizion) {
//				// create "empty" token description
//				factory.createUndefinedTokenDescriptor(timelineDescriptor, 
//						new long[] {
//								last.getInterval().getEndTime().getLowerBound(),
//								last.getInterval().getEndTime().getUpperBound()
//						}, 
//						new long [] {
//								this.horizion,
//								this.horizion
//						}, 
//						new long [] {
//								(this.horizion - last.getInterval().getEndTime().getUpperBound()),
//								(this.horizion - last.getInterval().getEndTime().getLowerBound())
//						});
//			}
			
			// add timeline to plan
			plan.addTimeline(timelineDescriptor);
		}
		
		// create relation descriptors
		for (Relation relation : this.relations)
		{
			// export temporal relations only
			if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
			{
				// get temporal relation
				TemporalRelation trel = (TemporalRelation) relation;
				// create relation description 
				RelationProtocolDescriptor relDescriptor = factory.createRelationDescriptor(
						relation.getType().name().toUpperCase(), 
						index.get(relation.getReference().getToken()), 
						index.get(relation.getTarget().getToken()));
				
				// set bounds
				relDescriptor.setBounds(trel.getBounds());
				// add relation descriptor to plan
				plan.addRelation(relDescriptor);
			}
		}
		
		// return plan descriptor
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
