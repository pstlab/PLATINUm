package it.uniroma3.epsl2.executive.pdb.apsi;

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
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
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
	 */
	@Override
	public void init(SolutionPlan plan) {
		
		/*
		 *  FIXME <---- IMPLEMENTARE
		 */
		
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
						ControllabilityType controllability = tl.isExternal() ? ControllabilityType.EXTERNAL_TOKEN : 
							token.isControllable() ? ControllabilityType.CONTROLLABLE : ControllabilityType.UNCONTROLLABLE_DURATION;
						
						// set parameter information
//						String signature = tl.getComponent() + "." + token.getPredicate();
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
						ExecutionNode node = this.createNode(tl.getComponent() + "." + tl.getName(), signature, 
								paramTypes, paramValues, 
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
						ControllabilityType controllability = tl.isExternal() ? ControllabilityType.EXTERNAL_TOKEN :
							token.isControllable() ? ControllabilityType.CONTROLLABLE : ControllabilityType.UNCONTROLLABLE_DURATION;
						
						// set parameter information
//						String signature = tl.getComponent() + "." + token.getPredicate();
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
						ExecutionNode node = this.createNode(tl.getComponent() + "." + tl.getName(), signature, 
								paramTypes, paramValues, 
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
	private void createConstraintsAndDependencies(ExecutionNode reference, ExecutionNode target, EPSLRelationDescriptor rel) 
			throws Exception {
		
		// check relation type
		switch (rel.getType()) {
		
			case "meets" : {
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.MEETS);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				constraint.setBounds(new long[][] {
					new long[] {0, 0}
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add start dependency
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "before" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.BEFORE);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// check bound
				long[] firstBound = rel.getFirstBound();
				if (firstBound[1] > this.getHorizon()) {
					firstBound[1] = this.getHorizon();
				}
				// set bound
				constraint.setBounds(new long[][] {
					firstBound
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add start dependency
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "after" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.AFTER);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// set bounds
				constraint.setBounds(new long[][] {
					rel.getFirstBound()
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add execution dependencies
				this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "during" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.DURING);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// set bounds
				constraint.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add execution dependencies
				this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
				this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
			
			case "contains" : {
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.CONTAINS);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// set bounds
				constraint.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add execution dependencies
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
				this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
				this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
			}
			break;
			
			case "equals" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.EQUALS);
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add execution dependencies
				this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
				this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
			
			case "starts_during" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.STARTS_DURING);
				// set intervals
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// set bounds
				constraint.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
				// add start dependency
				this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
			}
			break;
			
			case "ends_during" : {
				
				// create constraint
				TemporalConstraint constraint = this.iFactory.
						create(TemporalConstraintType.ENDS_DURING);
				// set intervals
				constraint.setReference(reference.getInterval());
				constraint.setTarget(target.getInterval());
				// set bounds
				constraint.setBounds(new long[][] {
					rel.getFirstBound(),
					rel.getSecondBound()
				});
				
				// propagate temporal constraint
				this.facade.propagate(constraint);
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
