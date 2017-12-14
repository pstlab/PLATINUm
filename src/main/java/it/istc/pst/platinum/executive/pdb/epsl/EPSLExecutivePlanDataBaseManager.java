package it.istc.pst.platinum.executive.pdb.epsl;

import java.util.HashMap;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.EPSLParameterDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLParameterTypes;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLTimelineDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLTokenDescriptor;
import it.istc.pst.epsl.pdb.lang.rel.EPSLRelationDescriptor;
import it.istc.pst.platinum.executive.pdb.ControllabilityType;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.executive.pdb.ExecutivePlanDataBaseManager;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.AfterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.ContainsRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.DuringRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.EndsDuringRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.EqualsRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.MetByRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.StartsDuringRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.parameter.lang.ParameterType;
import it.istc.pst.platinum.framework.protocol.lang.ParameterDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.ParameterTypeDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.TimelineProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.relation.RelationProtocolDescriptor;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.query.IntervalScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class EPSLExecutivePlanDataBaseManager extends ExecutivePlanDataBaseManager 
{
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	public EPSLExecutivePlanDataBaseManager(long origin, long horizon) {
		super(origin, horizon);
	}
	
	/**
	 * 
	 * @param plan
	 */
	@Override
	public void setup(PlanProtocolDescriptor plan) 
	{
		try 
		{
			// map token descriptor to nodes
			Map<TokenProtocolDescriptor, ExecutionNode> dictionary = new HashMap<>();
			// check time-lines
			for (TimelineProtocolDescriptor tl : plan.getTimelines()) 
			{
				// create an execution node for each token
				for (TokenProtocolDescriptor token : tl.getTokens()) 
				{
					// check predicate
					if (!token.getPredicate().equals("unallocated")) 
					{
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// set default controllability type
						ControllabilityType controllability = ControllabilityType.CONTROLLABLE;
						// check specific type
						if (tl.isExternal()) {
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
						}
						else if (token.getPredicate().startsWith("_")) {
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
						}
						
						// set parameter information
						String signature = token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) 
						{
							// get parameter
							ParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(ParameterTypeDescriptor.NUMERIC)) 
							{
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else 
							{
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(tl.getComponent(), tl.getName(), 
								signature, paramTypes, paramValues, 
								start, end, duration, controllability);
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check observations
			for (TimelineProtocolDescriptor tl : plan.getObservations()) 
			{
				// create an execution node for each token
				for (TokenProtocolDescriptor token : tl.getTokens()) 
				{
					// check predicate
					if (!token.getPredicate().equals("unallocated")) 
					{
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// check controllability type
						// set default controllability type
						ControllabilityType controllability = ControllabilityType.CONTROLLABLE;
						// check specific type
						if (tl.isExternal()) {
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
						}
						else if (token.getPredicate().startsWith("_")) {
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
						}
						
						// set parameter information
						String signature = token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) {
							
							// get parameter
							ParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(ParameterTypeDescriptor.NUMERIC)) 
							{
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else 
							{
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(tl.getComponent(), tl.getName(), 
								signature, paramTypes, paramValues, 
								start, end, duration, controllability);
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check relations
			for (RelationProtocolDescriptor rel : plan.getRelations()) 
			{
				try 
				{
					// get related nodes
					ExecutionNode reference = dictionary.get(rel.getFrom());
					ExecutionNode target = dictionary.get(rel.getTo());
					// add temporal constraints and related execution dependencies
					this.createConstraintsAndDependencies(reference, target, rel);
				}
				catch (Exception ex) {
					throw new ConsistencyCheckException("Error while propagating plan's relation " + rel + "\n" + ex.getMessage());
				}
			}
			
			// check consistency
			this.facade.checkConsistency();
			// check the schedule for all temporal intervals
			for (ExecutionNode node : dictionary.values()) 
			{
				// check node schedule
				IntervalScheduleQuery query = this.facade.createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
				query.setInterval(node.getInterval());
				this.facade.process(query);
			}
			
			// print execution dependency graph (for debug only)
			for (ExecutionNodeStatus status : this.nodes.keySet())
			{
				// get nodes by status
				for (ExecutionNode node : this.nodes.get(status))
				{
					// print node and the related execution conditions
					System.out.println("Execution node " + node);
					System.out.println("\tNode execution starting conditions:");
					Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeStartDependencies(node);
					for (ExecutionNode dep : dependencies.keySet()) {
						System.out.println("\t\tCan start if -> " + dep.getGroundSignature() + " is in " + dependencies.get(dep));
					}
					
					// get end conditions
					dependencies = this.getNodeEndDependencies(node);
					System.out.println("\tNode execution ending conditions:");
					for (ExecutionNode dep : dependencies.keySet()) {
						System.out.println("\t\tCan end if -> " + dep.getGroundSignature() + " is in " + dependencies.get(dep));
					}
				}
			}
		}
		catch (TemporalIntervalCreationException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		catch (ConsistencyCheckException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param plan
	 */
	@Override
	public void setup(EPSLPlanDescriptor plan) 
	{
		try 
		{
			// map token descriptor to nodes
			Map<EPSLTokenDescriptor, ExecutionNode> dictionary = new HashMap<>();
			// check time-lines
			for (EPSLTimelineDescriptor tl : plan.getTimelines()) 
			{
				// create an execution node for each token
				for (EPSLTokenDescriptor token : tl.getTokens()) 
				{
					// check predicate
					if (!token.getPredicate().equals("unallocated")) 
					{
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// set controllability type
						ControllabilityType controllability = ControllabilityType.CONTROLLABLE;
						// check specific type
						if (tl.isExternal()) {
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
						}
						else if (token.getPredicate().startsWith("_")) {
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
						}
						
						// set parameter information
						String signature = token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) 
						{
							// get parameter
							EPSLParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(EPSLParameterTypes.NUMERIC)) 
							{
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else 
							{
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(tl.getComponent(), tl.getName(), 
								signature, paramTypes, paramValues, 
								start, end, duration, controllability);
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check observations
			for (EPSLTimelineDescriptor tl : plan.getObservations()) 
			{
				// create an execution node for each token
				for (EPSLTokenDescriptor token : tl.getTokens()) 
				{
					// check predicate
					if (!token.getPredicate().equals("unallocated")) 
					{
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// check controllability type
						// set default controllability type
						ControllabilityType controllability = ControllabilityType.CONTROLLABLE;
						// check specific type
						if (tl.isExternal()) {
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
						}
						else if (token.getPredicate().startsWith("_")) {
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
						}
						
						// set parameter information
						String signature = token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) {
							
							// get parameter
							EPSLParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(EPSLParameterTypes.NUMERIC)) 
							{
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else 
							{
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(tl.getComponent(), tl.getName(), 
								signature, paramTypes, paramValues, 
								start, end, duration, controllability);
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check relations
			for (EPSLRelationDescriptor rel : plan.getRelations()) 
			{
				try 
				{
					// get related nodes
					ExecutionNode reference = dictionary.get(rel.getFrom());
					ExecutionNode target = dictionary.get(rel.getTo());
					// add temporal constraints and related execution dependencies
					this.createConstraintsAndDependencies(reference, target, rel);
				}
				catch (Exception ex) {
					throw new ConsistencyCheckException("Error while propagating plan's relation " + rel + "\n" + ex.getMessage());
				}
			}
			
			// check consistency
			this.facade.checkConsistency();
			// check the schedule for all temporal intervals
			for (ExecutionNode node : dictionary.values()) 
			{
				// check node schedule
				IntervalScheduleQuery query = this.facade.createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
				query.setInterval(node.getInterval());
				this.facade.process(query);
			}
			
			// print execution dependency graph (for debug only)
			for (ExecutionNodeStatus status : this.nodes.keySet())
			{
				// get nodes by status
				for (ExecutionNode node : this.nodes.get(status))
				{
					// print node and the related execution conditions
					System.out.println("Execution node " + node);
					System.out.println("\tNode execution starting conditions:");
					Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeStartDependencies(node);
					for (ExecutionNode dep : dependencies.keySet()) {
						System.out.println("\t\tCan start if -> " + dep.getGroundSignature() + " is in " + dependencies.get(dep));
					}
					
					// get end conditions
					dependencies = this.getNodeEndDependencies(node);
					System.out.println("\tNode execution ending conditions:");
					for (ExecutionNode dep : dependencies.keySet()) {
						System.out.println("\t\tCan end if -> " + dep.getGroundSignature() + " is in " + dependencies.get(dep));
					}
				}
			}
		}
		catch (TemporalIntervalCreationException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		catch (ConsistencyCheckException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param rel
	 * @throws Exception
	 */
	protected void createConstraintsAndDependencies(ExecutionNode reference, ExecutionNode target, Relation rel) 
			throws Exception
	{
		// check temporal category
		if (rel.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
		{
			// check relation
			switch (rel.getType())
			{
				// meets temporal relation
				case MEETS : {
					// get meets relation
					MeetsRelation meets = (MeetsRelation) rel;
					// prepare meets constraint
					this.prepareMeetsTemporalConstraint(reference, target, meets.getBounds());
				}
				break;
				
				// before temporal relation
				case BEFORE : {
					// get before relation
					BeforeRelation before = (BeforeRelation) rel;
					// prepare before constraint
					this.prepareBeforeTemporalConstraint(reference, target, before.getBounds());
				}
				break;
				
				case MET_BY : {
					// get met-by relation
					MetByRelation metby = (MetByRelation) rel;
					this.prepareAfterTemporalConstraint(reference, target, metby.getBounds());
				}
				break;
				
				case AFTER : {
					// get after relation
					AfterRelation after = (AfterRelation) rel;
					// prepare after constraint
					this.prepareAfterTemporalConstraint(reference, target, after.getBounds());
				}
				break;
				
				case CONTAINS : {
					// get contains relation
					ContainsRelation contains = (ContainsRelation) rel;
					// prepare contains constraint
					this.prepareContainsTemporalConstraint(reference, target, contains.getBounds());
				}
				break;
				
				case DURING : {
					// get during relation
					DuringRelation during = (DuringRelation) rel;
					// prepare during constraint
					this.prepareDuringTemporalConstraint(reference, target, during.getBounds());
				}
				break;
				
				case EQUALS : {
					// get equals relation
					EqualsRelation equals = (EqualsRelation) rel;
					// prepare equals constraint
					this.prepareEqualsTemporalConstraint(reference, target, equals.getBounds());
				}
				break;
				
				case STARTS_DURING : {
					// get starts-during relation
					StartsDuringRelation sduring = (StartsDuringRelation) rel;
					// prepare starts-during constraint
					this.prepareStartsDuringTemporalConstraint(reference, target, sduring.getBounds());
				}
				break;
				
				case ENDS_DURING : {
					// get ends-during relation
					EndsDuringRelation eduring = (EndsDuringRelation) rel;
					// prepare ends-during constraint
					this.prepareEndsDuringTemporalConstraint(reference, target, eduring.getBounds());
				}
				break;
				
				default : {
					throw new RuntimeException("Unknown relation " + rel.getType());
				}
				
			}
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param rel
	 */
	protected void createConstraintsAndDependencies(ExecutionNode reference, ExecutionNode target, RelationProtocolDescriptor rel) 
			throws Exception 
	{
		// check relation type
		switch (rel.getType()) 
		{
			// meets temporal relation
			case "meets" : {
				// prepare meets constraint
				this.prepareMeetsTemporalConstraint(reference, target, new long[][] {
					{0, 0}
				});
			}
			break;
			
			case "before" : {
				// prepare before constraint
				this.prepareBeforeTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound()
				});
			}
			break;
			
			case "met-by" : {
				// prepare after constraint
				this.prepareAfterTemporalConstraint(reference, target, new long[][] {
					{0, 0}
				});
			}
			break;
			
			case "after" : {
				// prepare after constraint
				this.prepareAfterTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound()
				});
			}
			break;
			
			case "during" : {
				// prepare during constraint
				this.prepareDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "contains" : {
				// prepare contains constraint
				this.prepareContainsTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "equals" : {
				// prepare equals constraint
				this.prepareEqualsTemporalConstraint(reference, target, new long[][] {
					{0, 0},
					{0, 0}
				});
			}
			break;
			
			case "starts_during" : {
				// prepare starts-during constraint
				this.prepareStartsDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "ends_during" : {
				// prepare ends-during constraint
				this.prepareEndsDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			default : {
				// unknown temporal relation
				throw new RuntimeException("Unknown relation " + rel.getType());
			}
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param rel
	 */
	protected void createConstraintsAndDependencies(ExecutionNode reference, ExecutionNode target, EPSLRelationDescriptor rel) 
			throws Exception 
	{
		// check relation type
		switch (rel.getType()) 
		{
			// meets temporal relation
			case "meets" : {
				// prepare meets constraint
				this.prepareMeetsTemporalConstraint(reference, target, new long[][] {
					{0, 0}
				});
			}
			break;
			
			case "before" : {
				// prepare before constraint
				this.prepareBeforeTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound()
				});
			}
			break;
			
			case "met_by" : {
				// prepare after constraint
				this.prepareAfterTemporalConstraint(reference, target, new long[][] {
					{0, 0}
				});
			}
			break;
			
			case "after" : {
				// prepare after constraint
				this.prepareAfterTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound()
				});
			}
			break;
			
			case "during" : {
				// prepare during constraint
				this.prepareDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "contains" : {
				// prepare contains constraint
				this.prepareContainsTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "equals" : {
				// prepare equals constraint
				this.prepareEqualsTemporalConstraint(reference, target, new long[][] {
					{0, 0},
					{0, 0}
				});
			}
			break;
			
			case "starts_during" : {
				// prepare starts-during constraint
				this.prepareStartsDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			case "ends_during" : {
				// prepare ends-during constraint
				this.prepareEndsDuringTemporalConstraint(reference, target, new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
			}
			break;
			
			default : {
				// unknown temporal relation
				throw new RuntimeException("Unknown relation " + rel.getType());
			}
		}
	}
}
