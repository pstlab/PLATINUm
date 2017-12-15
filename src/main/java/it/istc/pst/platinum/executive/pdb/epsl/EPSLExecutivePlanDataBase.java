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
import it.istc.pst.platinum.executive.pdb.ExecutivePlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.parameter.lang.ParameterType;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.query.IntervalScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class EPSLExecutivePlanDataBase extends ExecutivePlanDataBase 
{
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	public EPSLExecutivePlanDataBase() {
		super();
	}

	/**
	 * 
	 * @param plan
	 */
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
			this.facade.verify();
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
	
}
