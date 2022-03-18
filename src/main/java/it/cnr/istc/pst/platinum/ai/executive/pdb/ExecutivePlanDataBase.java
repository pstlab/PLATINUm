package it.cnr.istc.pst.platinum.ai.executive.pdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.AfterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.ContainsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.DuringRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.EndsDuringRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.EqualsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.MetByRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.StartsDuringRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterType;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.ParameterDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.ParameterTypeDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TimelineProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TokenProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.relation.RelationProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConsistencyException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalIntervalCreationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixIntervalDurationConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixTimePointConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.AfterIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.BeforeIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.ContainsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.DuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.EndsDuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.EqualsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.MeetsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.StartsDuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author alessandro
 *
 */
@TemporalFacadeConfiguration(
		
		// temporal network
		network = TemporalNetworkType.STNU,
		
		// temporal solver
		solver = TemporalSolverType.APSP
)
public class ExecutivePlanDataBase extends FrameworkObject 
{
	private PlanProtocolDescriptor plan;				// the plan to execute								
	
	@TemporalFacadePlaceholder
	protected TemporalFacade facade;					// temporal data base
	
	// plan locks
	private final Object[] locks;
	
	// plan's nodes
	protected Map<ExecutionNodeStatus, List<ExecutionNode>> nodes;
	
	// execution dependency graphs
	protected Map<ExecutionNode, Map<ExecutionNode, ExecutionNodeStatus[]>> sdg;		// start dependency graph - array of disjunctive start conditions 
	protected Map<ExecutionNode, Map<ExecutionNode, ExecutionNodeStatus[]>> edg;		// end dependency graph - array of disjunctive end conditions
	protected Map<ExecutionNode, Map<ExecutionNode, ExecutionNodeStatus[]>> stop;		// stop dependency graph - array of disjunctive end conditions
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	protected ExecutivePlanDataBase() {
		super();
		
		// set array of locks
		this.locks = new Object[ExecutionNodeStatus.values().length];
		// add locks 
		for (ExecutionNodeStatus s : ExecutionNodeStatus.values()) {
			// set lock
			this.locks[s.getIndex()] = new Object();
		}
		
		// set data structures
		this.nodes = new HashMap<>();
		for (ExecutionNodeStatus s : ExecutionNodeStatus.values()) {
			// set the index of execution nodes
			this.nodes.put(s, new LinkedList<ExecutionNode>());
		}
		
		// set the dependency graph
		this.sdg = new HashMap<>();
		this.edg = new HashMap<>();
		this.stop = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin() {
		return this.facade.getOrigin();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.facade.getHorizon();
	}
	
	/**
	 * 
	 * @return
	 */
	public String export() throws IOException {
		
		// prepare file 
		//System.out.println("Prepare file plans/exported/plan.txt... ");
		File output = new File("plans/exported/plan.txt");
		// export current plan (if any) to a known file
		if (this.plan != null) 
		{
			// get plan encoding
			String encoding = this.plan.export();
			// write to a file
			System.out.println("Writing file:\n");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
				// get data
				//System.out.println(encoding +"\n");
				writer.write(encoding);
				//System.out.println("Done!");
				writer.close();
			}
		}
		
		// get absolute file 
		//System.out.println(output.getAbsolutePath());
		return output.getAbsolutePath();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.plan.getName();
	}
	
	
	/**
	 * 
	 * @param plan
	 */
	public void setup(PlanProtocolDescriptor plan) {
		
		try {
			
			// get plan descriptor
			this.plan = plan;
			// map token descriptor to nodes
			Map<TokenProtocolDescriptor, ExecutionNode> dictionary = new HashMap<>();
			// check time-lines
			for (TimelineProtocolDescriptor tl : plan.getTimelines()) {
				
				// setup node arrays
				ExecutionNode[] list = new ExecutionNode[tl.getTokens().size()];
				// list index
				int counter = 0;
				// create an execution node for each token
				for (TokenProtocolDescriptor token : tl.getTokens()) {
					// check predicate
					if (!token.getPredicate().toLowerCase().equals("unallocated")) {
						
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// set default controllability type
						ControllabilityType controllability = null;
						// check specific type
						if (tl.isExternal()) {
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
							
						} else if (token.getPredicate().startsWith("_")) {
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
							
						} else {
							// controllable
							controllability = ControllabilityType.CONTROLLABLE; 
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
								paramValues[index] = Long.toString(param.getBounds()[0]); 
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
								start, end, duration, controllability, token.getStartExecutionState());
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
						
						// add node to list
						list[counter] = node;
						counter++;
					}
				}
				
				// link subsequent nodes
				if (list.length > 1)  {
					for (int pos = 0; pos < list.length; pos++) {
						
						// check first node
						if (pos == 0) {
						
							// first node of the timeline
							ExecutionNode first = list[pos];
							// set next node
							first.setNext(list[pos+1]);
							
						} else if (pos == list.length - 1) {
							
							// last node of the timeline
							ExecutionNode last = list[pos];
							// set previous node
							last.setPrev(list[pos-1]);
							
						} else {
							
							// intermediate node
							ExecutionNode i = list[pos];
							// set previous
							i.setPrev(list[pos-1]);
							// set next
							i.setNext(list[pos+1]);
						}
					}
				}
			}
			
			// check observations
			for (TimelineProtocolDescriptor tl : plan.getObservations()) {
				
				// setup node arrays
				ExecutionNode[] list = new ExecutionNode[tl.getTokens().size()];
				// list index
				int counter = 0;
				// create an execution node for each token
				for (TokenProtocolDescriptor token : tl.getTokens()) {
					// check predicate
					if (!token.getPredicate().toLowerCase().equals("unallocated")) {
						
						// get token's bound
						long[] start = token.getStartTimeBounds();
						long[] end = token.getEndTimeBounds();
						long[] duration = token.getDurationBounds();
						
						// check controllability type
						// set default controllability type
						ControllabilityType controllability = null;
						// check specific type
						if (tl.isExternal()) {
							
							// uncontrollable 
							controllability = ControllabilityType.UNCONTROLLABLE;
							
						} else if (token.getPredicate().startsWith("_")) {
							
							// partially controllable token
							controllability = ControllabilityType.PARTIALLY_CONTROLLABLE;
							
						} else {
							
							// controllable token
							controllability = ControllabilityType.CONTROLLABLE;
						}
						
						// set parameter information
						String signature = token.getPredicate();
						String[] paramValues = new String[token.getParameters().size()];
						ParameterType[] paramTypes = new ParameterType[token.getParameters().size()];
						for (int index = 0; index < token.getParameters().size(); index++) {
							
							// get parameter
							ParameterDescriptor param= token.getParameter(index);
							// check type
							if (param.getType().equals(ParameterTypeDescriptor.NUMERIC)) {
								// set t							ype
								paramTypes[index] = ParameterType.NUMERIC_PARAMETER_TYPE;
								// set value
								paramValues[index] = Long.toString(param.getBounds()[0]);
								
							} else {
								
								// enumeration
								paramTypes[index] = ParameterType.ENUMERATION_PARAMETER_TYPE;
								// set value
								paramValues[index] = param.getValues()[0];
							}
						}
						
						// create a node
						ExecutionNode node = this.createNode(tl.getComponent(), tl.getName(), 
								signature, paramTypes, paramValues, 
								start, end, duration, controllability, token.getStartExecutionState());
						
						// add node
						this.addNode(node);
						// add entry to the dictionary
						dictionary.put(token, node);
						
						// add node to list
						list[counter] = node;
						counter++;
					}
					
				}
				
				// link subsequent nodes
				for (int pos = 0; pos < list.length; pos++) {
					
					// check first node
					if (pos == 0) {
						
						// first node of the timeline
						ExecutionNode first = list[pos];
						// set next node
						first.setNext(list[pos+1]);
						
					} else if (pos == list.length - 1) {
						// last node of the timeline
						ExecutionNode last = list[pos];
						// set previous ndoe
						last.setPrev(list[pos-1]);
						
					} else {
						
						// intermediate node
						ExecutionNode i = list[pos];
						// set prev
						i.setPrev(list[pos-1]);
						// set next
						i.setNext(list[pos+1]);
					}
				}
			}
			
			// check relations
			for (RelationProtocolDescriptor rel : plan.getRelations()) {
				try {
					
					// get related nodes
					ExecutionNode reference = dictionary.get(rel.getFrom());
					ExecutionNode target = dictionary.get(rel.getTo());
					// add temporal constraints and related execution dependencies
					this.createConstraintsAndDependencies(reference, target, rel);
					
				} catch (Exception ex) {
					
					// exception
					throw new TemporalConsistencyException("Error while propagating plan's relation " + rel + "\n" + ex.getMessage());
				}
			}
			
			// check consistency
			this.facade.verifyTemporalConsistency();
			// check the schedule for all temporal intervals
			for (ExecutionNode node : dictionary.values()) {
				// check node schedule
				IntervalScheduleQuery query = this.facade.createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
				query.setInterval(node.getInterval());
				this.facade.process(query);
			}
			
			// prepare log message
			String msg = "";
			// print execution dependency graph (for debug only)
			for (ExecutionNodeStatus status : this.nodes.keySet()) {
				// get nodes by status
				for (ExecutionNode node : this.nodes.get(status)) {
					
					// print node and the related execution conditions
					msg += "Execution node " + node + "\n";
					msg += "\tNode execution starting conditions:\n";
					Map<ExecutionNode, ExecutionNodeStatus[]> dependencies = this.getNodeStartDependencies(node);
					for (ExecutionNode dep : dependencies.keySet()) {
						msg += "\t\tCan start if -> " + dep.getId() + ":"+ dep.getGroundSignature() + " is in " + dependencies.get(dep) + "\n";
					}
					
					// get end conditions
					dependencies = this.getNodeEndDependencies(node);
					msg += "\tNode execution ending conditions:\n";
					for (ExecutionNode dep : dependencies.keySet()) {
						msg += "\t\tCan end if -> " + dep.getId() + ":" + dep.getGroundSignature() + " is in " + dependencies.get(dep) + "\n";
					}
				}
			}
			
			// print log message
			debug(msg);
			
		} catch (TemporalIntervalCreationException ex) {
			throw new RuntimeException(ex.getMessage());
			
		} catch (TemporalConsistencyException ex) {
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
			throws Exception {
		
		// check temporal category
		if (rel.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) {
			// check relation
			switch (rel.getType()) {
			
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
			throws Exception {
		
		// check relation type
		switch (rel.getType()) {
		
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
				this.prepareMeetsTemporalConstraint(target, reference, new long[][] {
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
	 * @return
	 */
	public List<ExecutionNode> getNodesByStatus(ExecutionNodeStatus status) {
		
		// list of nodes
		List<ExecutionNode> list = new LinkedList<>();
		synchronized (this.locks[status.getIndex()]) {
			// get all nodes with the desired status
			list.addAll(this.nodes.get(status));
		}
		
		// get the list of node with the desired status 
		return list;
	}
	
	/**
	 * 
	 * @param node
	 * @param status
	 */
	public synchronized void updateNodeStatus(ExecutionNode node, ExecutionNodeStatus status) {
		
		// remove node from the current status
		synchronized (this.locks[node.getStatus().getIndex()]) {
			// remove node from list
			this.nodes.get(node.getStatus()).remove(node);
		}
		
		// add node to the new status
		synchronized (this.locks[status.getIndex()]) {
			// add node
			this.nodes.get(status).add(node);
			// update status
			node.setStatus(status);
		}
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public Map<ExecutionNode, ExecutionNodeStatus[]> getNodeStartDependencies(ExecutionNode node) {
		return new HashMap<>(this.sdg.get(node));
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public Map<ExecutionNode, ExecutionNodeStatus[]> getNodeEndDependencies(ExecutionNode node) {
		return new HashMap<>(this.edg.get(node));
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public Map<ExecutionNode, ExecutionNodeStatus[]> getNodeStopDependencies(ExecutionNode node) {
		return new HashMap<>(this.stop.get(node));
	}
	
	
	/**
	 * 
	 * @param node
	 */
	public void checkSchedule(ExecutionNode node) {
		
		// check resulting schedule of the interval
		IntervalScheduleQuery query = this.facade.
				 createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
		query.setInterval(node.getInterval());
		this.facade.process(query);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addNode(ExecutionNode node) {
		
		// check expected initial execution state
		ExecutionNodeStatus initial = node.getStartExecutionState();
		node.setStatus(initial);
		this.nodes.get(initial).add(node);
		// setup dependency graph data structures
		this.sdg.put(node, new HashMap<ExecutionNode, ExecutionNodeStatus[]>());
		this.edg.put(node, new HashMap<ExecutionNode, ExecutionNodeStatus[]>());
		this.stop.put(node, new HashMap<ExecutionNode, ExecutionNodeStatus[]>());
	}
	
	/**
	 * 
	 */
	public void clearExecutedNodes() {
		
		// check executed nodes
		Set<ExecutionNode> nodes = new HashSet<>(this.nodes.get(ExecutionNodeStatus.EXECUTED));
		// clear nodes
		this.nodes.get(ExecutionNodeStatus.EXECUTED).clear();
		// clear execution graph
		for (ExecutionNode node : nodes) {
			// clear map entries
			this.sdg.remove(node);
			this.edg.remove(node);
			this.stop.remove(node);
			
			// clear edges
			for (ExecutionNode n : this.sdg.keySet()) {
				// remove edge
				this.sdg.get(n).remove(node);
				
			}
			
			// clear edges
			for (ExecutionNode n : this.edg.keySet()) {
				// remove edge
				this.edg.get(n).remove(node);
			}
			
			// clear edges
			for (ExecutionNode n : this.stop.keySet()) {
				// remove edge
				this.stop.get(n).remove(node);
			}
		}
	}
	
	/**
	 * 
	 * @param component
	 * @param timeline
	 * @param signature
	 * @param pTypes
	 * @param pValues
	 * @param start
	 * @param end
	 * @param duration
	 * @param controllability
	 * @param state
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	protected ExecutionNode createNode(String component, String timeline, 
			String signature, ParameterType[] pTypes, String[] pValues, 
			long[] start, long[] end, long[] duration, 
			ControllabilityType controllability,
			ExecutionNodeStatus state) 
			throws TemporalIntervalCreationException  {
		
		// create temporal interval - consider all intervals as controllable during execution
		TemporalInterval interval = this.facade.createTemporalInterval(start, end, duration, true);
		// create predicate
		NodePredicate predicate = new NodePredicate(component, timeline, signature, pTypes, pValues); 
		// create execution node
		return new ExecutionNode(predicate, interval, controllability, state);
	}
	
	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	protected void addStartExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus[] conditions) {
		// add node's start dependency and related condition
		this.sdg.get(node).put(dependency, conditions);
	}

	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	protected void addEndExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus[] conditions) {
		// add node's end dependency and related condition
		this.edg.get(node).put(dependency, conditions);
	}
	
	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param conditions
	 */
	protected void addStopExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus[] conditions) {
		// add node's end dependency and related condition
		this.stop.get(node).put(dependency, conditions);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean checkEndExecutionDependencies(ExecutionNode node) {
		
		Map<ExecutionNode, ExecutionNodeStatus[]> dependencies = this.getNodeEndDependencies(node);
		// set end execution flag 
		boolean canEnd = dependencies.isEmpty();
		// check execution flag
		if (!canEnd) {
			
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			// check all conditions
			while (it.hasNext() && !canEnd) {
				
				// get next dependency
				ExecutionNode d = it.next();
				// get end conditions
				ExecutionNodeStatus[] conditions = dependencies.get(d);
				// check if one of the disjunctive conditions is satisfied
				for (ExecutionNodeStatus condition : conditions) {
					// check condition
					if (d.getStatus().equals(condition)) {
						canEnd = true;
						break;
					}
					
				}
			}
		}
		
		// true if the node can end execution
		return canEnd;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean checkStopExecutionDependencies(ExecutionNode node) {
		
		Map<ExecutionNode, ExecutionNodeStatus[]> dependencies = this.getNodeStopDependencies(node);
		// set end execution flag 
		boolean canStop = dependencies.isEmpty();
		// check execution flag
		if (!canStop) {
			
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			// check all conditions
			while (it.hasNext() && !canStop) {
				
				// get next dependency
				ExecutionNode d = it.next();
				// get end conditions
				ExecutionNodeStatus[] conditions = dependencies.get(d);
				// check if one of the disjunctive conditions is satisfied
				for (ExecutionNodeStatus condition : conditions) {
					// check condition
					if (d.getStatus().equals(condition)) {
						canStop = true;
						break;
					}
					
				}
			}
		}
		
		// true if the node can stop execution
		return canStop;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean checkStartExecutionDependencies(ExecutionNode node) {
		
		// get node's start dependencies
		Map<ExecutionNode, ExecutionNodeStatus[]> dependencies = this.getNodeStartDependencies(node);
		// set execution flag
		boolean canStart = dependencies.isEmpty();
		// check execution flag
		if (!canStart) {
			
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			// check all conditions
			while (it.hasNext() && !canStart) {
				
				// get a dependency parent
				ExecutionNode d = it.next();
				// get start conditions
				ExecutionNodeStatus[] conditions = dependencies.get(d);
				// check if one of the disjunctive conditions is satisfied
				for (ExecutionNodeStatus condition : conditions) {
					// check condition
					if (d.getStatus().equals(condition)) {
						// node can start
						canStart = true;
						break;
					}
				}
			}
		}
		
		// true if ready
		return canStart;
	}

	/**
	 * 
	 * @param node
	 * @param duration
	 * @throws TemporalConstraintPropagationException
	 */
	public final void scheduleDuration(ExecutionNode node, long duration) 
			throws TemporalConstraintPropagationException {
		
		// fix start time first
		FixIntervalDurationConstraint fix = this.facade.
				createTemporalConstraint(TemporalConstraintType.FIX_INTERVAL_DURATION);
		fix.setReference(node.getInterval());
		fix.setDuration(duration);
		// propagate constraint
		this.facade.propagate(fix);
		try {
			
			// check the consistency of the resulting network
			this.facade.verifyTemporalConsistency();
			
		} catch (TemporalConsistencyException ex) {
			// retract propagated constraint and throw exception
			this.facade.retract(fix);
			throw new TemporalConstraintPropagationException("Error while propagating duration constraint for node\n- duration= " + duration +"\n- node= " + node + "\n");
		}
	}
	
	/**
	 * 
	 * @param node
	 * @param time
	 * @throws TemporalConstraintPropagationException
	 */
	public final void scheduleStartTime(ExecutionNode node, long time) 
			throws TemporalConstraintPropagationException {
		
		// create constraint
		FixTimePointConstraint fix = this.facade.
				createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
		// set time point
		fix.setReference(node.getInterval().getStartTime());
		fix.setTime(time);
		// propagate constraint
		this.facade.propagate(fix);
		try {
			
			// check consistency of the resulting network
			this.facade.verifyTemporalConsistency();
			
		} catch (TemporalConsistencyException ex) {
			// retract propagated constraint and throw exception
			this.facade.retract(fix);
			throw new TemporalConstraintPropagationException("Error while propagating start constraint for node\n- "
					+ "time= " + time+ "\n- node= " + node + "\n");
		}
	}

	/**
	 * 
	 * @param node
	 * @param end
	 * @throws TemporalConstraintPropagationException
	 */
	public void scheduleEndTime(ExecutionNode node, long time) 
			throws TemporalConstraintPropagationException {
		
		// create constraint
		FixTimePointConstraint fix = this.facade.
				createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
		// set time point
		fix.setReference(node.getInterval().getEndTime());
		// set time
		fix.setTime(time);
		// propagate constraint
		this.facade.propagate(fix);
		try {
			
			// check consistency of the resulting network
			this.facade.verifyTemporalConsistency();
			
		} catch (TemporalConsistencyException ex) {
			// retract propagated constraint and throw exception
			this.facade.retract(fix);
			throw new TemporalConstraintPropagationException("Error while propagating end constraint for node\n"
					+ "- time= " + time+ "\n- node= " + node + "\n");
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareBeforeTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create and propagate temporal constraint
		BeforeIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.BEFORE);
		
		// set data
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		constraint.setLowerBound(bounds[0][0]);
		constraint.setUpperBound(bounds[0][1]);
		// propagate constraint
		this.facade.propagate(constraint);
		
		// set execution constraints
		this.addStartExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareMeetsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create and propagate temporal constraint
		MeetsIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.MEETS);
		// set data
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// propagate constraint
		this.facade.propagate(constraint);
		
		// set execution constraints
		this.addStartExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareAfterTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		AfterIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.AFTER);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());

		// set bounds
		constraint.setLowerBound(bounds[0][0]);
		constraint.setUpperBound(bounds[0][1]);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		
		// add execution dependencies
		this.addStartExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		DuringIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.DURING);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		
		// set bounds
		constraint.setStartTimeBound(bounds[0]);
		constraint.setEndTimeBound(bounds[1]);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		
		
		// add execution dependencies
		this.addStartExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION
		});
		
		this.addEndExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION,
		});
		
		this.addEndExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED,
		});
		
		
		
		
		// add stop execution condition
		this.addStopExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION,
				ExecutionNodeStatus.FAILURE,
		});
		
		// add stop execution condition
		this.addStopExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED,
				ExecutionNodeStatus.FAILURE,
				ExecutionNodeStatus.WAITING
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareContainsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		ContainsIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.CONTAINS);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		
		// set bounds
		constraint.setStartTimeBound(bounds[0]);
		constraint.setEndTimeBound(bounds[1]);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		
		// add execution dependencies
		this.addEndExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED,
		});
		
		this.addStartExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION
		});
		
		this.addEndExecutionDependency(target, reference, new ExecutionNodeStatus[] { 
				ExecutionNodeStatus.IN_EXECUTION,
		});
		
		
		// add stop execution condition
		this.addStopExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.EXECUTED,
				ExecutionNodeStatus.FAILURE,
				ExecutionNodeStatus.WAITING
		});
		
		// add stop execution condition
		this.addStopExecutionDependency(target, reference, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION,
				ExecutionNodeStatus.FAILURE,
				
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareEqualsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		EqualsIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.EQUALS);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// propagate temporal constraint
		this.facade.propagate(constraint);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareStartsDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		StartsDuringIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.STARTS_DURING);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		
		// set bounds
		constraint.setFirstBound(bounds[0]);
		constraint.setSecondBound(bounds[1]);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		
		// add start dependency
		this.addStartExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION
		});
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareEndsDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception {
		
		// create constraint
		EndsDuringIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.ENDS_DURING);
		
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setFirstBound(bounds[0]);
		constraint.setSecondBound(bounds[1]);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		
		// add end dependency
		this.addEndExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION,
		});
		
		
		// add stop execution condition
		this.addStopExecutionDependency(reference, target, new ExecutionNodeStatus[] {
				ExecutionNodeStatus.IN_EXECUTION,
				ExecutionNodeStatus.FAILURE
		});
		
	}
}