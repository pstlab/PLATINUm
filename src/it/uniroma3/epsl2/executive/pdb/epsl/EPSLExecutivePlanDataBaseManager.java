package it.uniroma3.epsl2.executive.pdb.epsl;

import java.util.HashMap;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.EPSLParameterDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLParameterTypes;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLTimelineDescriptor;
import it.istc.pst.epsl.pdb.lang.EPSLTokenDescriptor;
import it.istc.pst.epsl.pdb.lang.rel.EPSLRelationDescriptor;
import it.uniroma3.epsl2.executive.pdb.ControllabilityType;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class EPSLExecutivePlanDataBaseManager extends ExecutivePlanDataBaseManager {

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
	public void init(EPSLPlanDescriptor plan) 
	{
		try 
		{
			// map token descriptor to nodes
			Map<EPSLTokenDescriptor, ExecutionNode> dictionary = new HashMap<>();
			// check time-lines
			for (EPSLTimelineDescriptor tl : plan.getTimelines()) {
				// create an execution node for each token
				for (EPSLTokenDescriptor token : tl.getTokens()) {
					
					// check predicate
					if (!token.getPredicate().equals("unallocated")) {
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// set controllability type
						ControllabilityType controllability = tl.isExternal() ? ControllabilityType.EXTERNAL_TOKEN : 
							token.isControllable() ? ControllabilityType.CONTROLLABLE : ControllabilityType.UNCONTROLLABLE_DURATION;
						
						// set parameter information
						String signature = tl.getComponent() + "." + token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) {
							// get parameter
							EPSLParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(EPSLParameterTypes.NUMERIC)) {
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else {
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(signature, paramTypes, paramValues, start, end, duration, controllability);
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check observations
			for (EPSLTimelineDescriptor tl : plan.getObservations()) {
				// create an execution node for each token
				for (EPSLTokenDescriptor token : tl.getTokens()) {
					// check predicate
					if (!token.getPredicate().equals("unallocated")) {
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// check controllability type
						ControllabilityType controllability = tl.isExternal() ? ControllabilityType.EXTERNAL_TOKEN :
							token.isControllable() ? ControllabilityType.CONTROLLABLE : ControllabilityType.UNCONTROLLABLE_DURATION;
						
						// set parameter information
						String signature = tl.getComponent() + "." + token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) {
							// get parameter
							EPSLParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(EPSLParameterTypes.NUMERIC)) {
								// set type
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = new Long(param.getBounds()[0]).toString();
							}
							else {
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(signature, paramTypes, paramValues, start, end, duration, controllability);
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
					}
				}
			}
			
			// check relations
			for (EPSLRelationDescriptor rel : plan.getRelations()) {
				// get related nodes
				ExecutionNode reference = dictionary.get(rel.getFrom());
				ExecutionNode target = dictionary.get(rel.getTo());
				
				// add temporal constraints and related execution dependencies
				this.createConstraintsAndDependencies(reference, target, rel);
			}
			
			// check consistency
			this.facade.checkConsistency();
			
			// check the schedule for all temporal intervals
			for (ExecutionNode node : dictionary.values()) {
				// check node schedule
				CheckIntervalScheduleQuery query = this.qFactory.create(TemporalQueryType.CHECK_SCHEDULE);
				query.setInterval(node.getInterval());
				this.facade.process(query);
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
	 */
	private void createConstraintsAndDependencies(ExecutionNode reference, ExecutionNode target, EPSLRelationDescriptor rel) {
		// check relation type
		switch (rel.getType()) {
		
			case "meets" : {
				// create constraint
				TemporalConstraint c = this.iFactory.create(TemporalConstraintType.MEETS);
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// add start dependency
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "before" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.BEFORE);
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// set bound
				c.setBounds(new long[][] {
					rel.getFirstBound()
				});
				
				// add start dependency
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "after" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.BEFORE);
				c.setReference(target.getInterval());
				c.setTarget(reference.getInterval());
				// set bounds
				c.setBounds(new long[][] {
					rel.getFirstBound()
				});
				
				// add start dependency
				this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "during" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.DURING);
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// set bounds
				c.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// add start dependency
				this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
				// add end dependency
				this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "contains" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.CONTAINS);
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// set bounds
				c.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// add start dependency
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
				this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "starts_during" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.STARTS_DURING);
				// set intervals
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// set bounds
				c.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// add start dependency
				this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
			
			case "ends_during" : {
				// create constraint
				TemporalConstraint c = this.iFactory.
						create(TemporalConstraintType.ENDS_DURING);
				// set intervals
				c.setReference(reference.getInterval());
				c.setTarget(target.getInterval());
				// set bounds
				c.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// add end dependency
				this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
			
			default : {
				throw new RuntimeException("Unknown relation " + rel.getType());
			}
		}
	}
}
