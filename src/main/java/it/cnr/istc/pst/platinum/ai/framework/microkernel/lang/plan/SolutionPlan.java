package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Token;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.Resource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.EnumerationParameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.NumericParameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterType;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.ParameterTypeDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.ProtocolLanguageFactory;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TimelineProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TokenProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.relation.RelationProtocolDescriptor;

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
	private SearchSpaceNode solutionNode;
	private Set<Timeline> timelines;
	private Set<Timeline> observations;
	private List<Relation> relations;
	private PlanControllabilityType controllability;
	private Set<Profile> profiles;
	
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
		this.profiles = new HashSet<>();
		this.controllability = PlanControllabilityType.UNKNOWN;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void setSolutionNode(SearchSpaceNode node) {
		this.solutionNode = node;
	}
	
	/**
	 * 
	 * @return
	 */
	public SearchSpaceNode getSolutionNode() {
		return this.solutionNode;
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
	 * @return
	 */
	public double[] getMakespan() 
	{
		// set makespan 
		double[] mk = new double[] {
				Double.MIN_VALUE + 1,  
				Double.MAX_VALUE - 1
		};
		
		// check timelines
		for (Timeline tl : this.timelines) 
		{
			// check primitive components only 
			if (tl.getComponent().getType().equals(DomainComponentType.SV_PRIMITIVE))
			{
				// check tokens
				if (!tl.getTokens().isEmpty()) {
					// get last token of the timeline
					Token last = tl.getTokens().get(0);
					for (int index = 1; index < tl.getTokens().size(); index++) {
						// get token 
						Token t = tl.getTokens().get(index);
						// check last token 
						if (t.getInterval().getEndTime().getLowerBound() > last.getInterval().getEndTime().getLowerBound()) {
							// update last token
							last = t;
						}
					}
					
					// update max end time
					mk[0] = Math.max(mk[0], last.getInterval().getEndTime().getLowerBound());
					mk[1] = Math.min(mk[1], last.getInterval().getEndTime().getUpperBound());
				}
			}
		}
		
		
		// get makespan
		return mk;
	}

	/**
	 * 
	 * @param compName
	 * @return
	 */
	public double[] getMakespan(String name) 
	{
		// set makespan 
		double[] mk = new double[] {
				0.0,
				0.0
		};
		
		// check timelines
		for (Timeline tl : this.timelines) 
		{
			// check primitive components only 
			if (tl.getComponent().getName().equals(name))
			{
				// preapre makespan
				mk = new double[] {
						Double.MIN_VALUE + 1,  
						Double.MAX_VALUE - 1
				};
				
				
				// get last token of the timeline
				Token last = tl.getTokens().get(0);
				for (int index = 1; index < tl.getTokens().size(); index++) {
					// get token 
					Token t = tl.getTokens().get(index);
					// check last token 
					if (t.getInterval().getEndTime().getLowerBound() > last.getInterval().getEndTime().getLowerBound()) {
						// update last token
						last = t;
					}
				}
				
				// update max end time
				mk[0] = Math.max(mk[0], last.getInterval().getEndTime().getLowerBound());
				mk[1] = Math.min(mk[1], last.getInterval().getEndTime().getUpperBound());
				
				// stop search
				break;
			}
		}
		
		
		// get makespan
		return mk;
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
			
			case RESOURCE_DISCRETE : 
			case RESOURCE_RESERVOIR : {
				// get component as a resource
				Resource res = (Resource) component;
				// get profile
				Profile profile = new Profile(res);
				// add profile
				this.profiles.add(profile);
			}
			break;
			
			case PLAN_DATABASE : {
				// ignore this type of components
			}
			break;
			
			
			default : {
				throw new RuntimeException("Unknown component type " + component.getType() + "\n");
			}
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
		PlanProtocolDescriptor plan = factory.createPlanDescriptor(this.name, 0, this.horizion);
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
								enuPar.getValues().toString()
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
			
			// add timeline to plan
			plan.addTimeline(timelineDescriptor);
		}
		
		// create relation descriptors
		for (Relation relation : this.relations)
		{
			// export temporal relations only
			if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
			{
				// consider only relations between values of state variables
				if (relation.getReference().getComponent() instanceof StateVariable && 
						relation.getTarget().getComponent() instanceof StateVariable)
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
		// get plan makespan 
		double[] mk = this.getMakespan();
		// initialize solution plan description
		String description = "{\n"
				+ "\t\"horizon\": " + this.horizion + ",\n"
				+ "\t\"controllability\": \"" + this.controllability.toString().toLowerCase() + "\",\n"
				+ "\t\"makespan\": [" + mk[0] + ", " + mk[1] + "],\n"
				+ "\t\"solving_time\": " + solvingTime + ",\n";
		
		// check domain specific metric if any
		if (solutionNode.getDomainSpecificMetric() != null) {
			description += "\n";
			description += "\t\"metric\": " + solutionNode.getDomainSpecificMetric().toString();
			description += "\n";
		}
		
		// start description of timelines 
		description += "\t\"timelines\": [\n";
		for (Timeline tl : this.timelines) 
		{
			description += "\t\t{\n"
					+ "\t\t\t\"name\": \"" + tl.getComponent().getName() + "\",\n"
					+ "\t\t\t\"tokens\": [\n";
			// get tokens
			for (Token token : tl.getTokens()) {
				description += "\t\t\t\t" + token + ",\n";
			}
			description += "\t\t\t]\n"
					+ "\t\t},\n";
 		}
		
		// end description of timelines
		description	+= "\t],\n\n";
		
		// print profiles
		description += "\t\"profiles\": [\n";
		for (Profile pro : this.profiles)
		{
			// open description
			description += "\t\t{\n"
					+ "\t\t\t\"name\": \"" + pro.getName() + "\",\n"
					+ "\t\t\t\"initial-level\": " + pro.getInitCapacity() + ",\n"
					+ "\t\t\t\"min\": " + pro.getMinCapacity() + ",\n"
					+ "\t\t\t\"max\": " + pro.getMaxCapacity() + ",\n"
					+ "\t\t\t\"events\": [\n";
			
			// get events
			for (ResourceEvent<?> event : pro.getResourceEvents()) {
				description += "\t\t\t\t[\"update\": " + event.getAmount() + "  " + event.getEvent() + "],\n";
			}
			
			// close description
			description += "\t\t\t]\n"
					+ "\t\t},\n";
		}
		
		// description of profiles
		description += "\t],\n\n";
		
		// start description of observations
		description += "\tobservations: [\n";
		for (Timeline tl : this.observations) {
			description += "\t\t{\n"
					+ "\t\t\tname: \"" + tl.getComponent().getName() + "\",\n"
					+ "\t\t\ttokens: [\n";
			// get tokens 
			for (Token token : tl.getTokens()) {
				description += "\t\t\t\t" + token + ",\n";
			}
			description += "\t\t\t]\n"
					+ "\t\t},\n";
		}
		
		// end description of observations
		description += "\t],\n\n";
		
		// start description of relations
		description += "\trelations: [\n";
		for (Relation rel : this.relations) {
			description += "\t\t" + rel +  ",\n";
		}
		
		// end description of relations
		description += "\t]\n\n";
		
		// close plan description
		description += "}\n\n";
		// get description
		return description;
	}
	
	
}
