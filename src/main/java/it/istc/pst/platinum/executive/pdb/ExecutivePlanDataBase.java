package it.istc.pst.platinum.executive.pdb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.rel.EPSLRelationDescriptor;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
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
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.FixIntervalDurationConstraint;
import it.istc.pst.platinum.framework.time.lang.FixTimePointConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.AfterIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.ContainsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.DuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.EndsDuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.EqualsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.MeetsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.StartsDuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.IntervalScheduleQuery;
import it.istc.pst.platinum.framework.time.solver.TemporalSolverType;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author anacleto
 *
 */
@TemporalFacadeConfiguration(
		
		// temporal network
		network = TemporalNetworkType.STNU,
		
		// temporal solver
		solver = TemporalSolverType.APSP
)
public class ExecutivePlanDataBase extends ExecutiveObject 
{
	@TemporalFacadePlaceholder
	protected TemporalFacade facade;			// temporal data base
	
	// plan locks
	private final Object[] locks;
	
	// plan's nodes
	protected Map<ExecutionNodeStatus, List<ExecutionNode>> nodes;
	
	// execution dependency graphs
	protected Map<ExecutionNode, Map<ExecutionNode, ExecutionNodeStatus>> sdg;		// start dependency graph
	protected Map<ExecutionNode, Map<ExecutionNode, ExecutionNodeStatus>> edg;		// end dependency graph
	
	/**
	 * 
	 * @param origin
	 * @param horizon
	 */
	protected ExecutivePlanDataBase() {
		super();
		
		// initialize array of locks
		this.locks = new Object[ExecutionNodeStatus.values().length];
		// add locks 
		for (ExecutionNodeStatus s : ExecutionNodeStatus.values()) {
			// set lock
			this.locks[s.getIndex()] = new Object();
		}
		
		// initialize data structures
		this.nodes = new HashMap<>();
		for (ExecutionNodeStatus s : ExecutionNodeStatus.values()) {
			// initialize the index of execution nodes
			this.nodes.put(s, new LinkedList<ExecutionNode>());
		}
		
		// initialize the dependency graph
		this.sdg = new HashMap<>();
		this.edg = new HashMap<>();
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
	 * @param plan
	 */
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
					if (!token.getPredicate().toLowerCase().equals("unallocated")) 
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
						
						// check if virtual node
						boolean virtual = true;
						if (token.getPredicate().startsWith("_") || 
								token.getPredicate().startsWith("r")) {
							// not a virtual node
							virtual = false;
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
								start, end, duration, controllability, virtual, token.getStartExecutionState());
						
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
					if (!token.getPredicate().toLowerCase().equals("unallocated")) 
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
						
						// set virtual flag
						boolean virtual = true;
						if (token.getPredicate().startsWith("_") ||
								token.getPredicate().startsWith("r")) {
							// not a virtual node
							virtual = false;
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
								start, end, duration, controllability, virtual, token.getStartExecutionState());
						
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
			this.facade.verify();
			// check the schedule for all temporal intervals
			for (ExecutionNode node : dictionary.values()) 
			{
				// check node schedule
				IntervalScheduleQuery query = this.facade.createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
				query.setInterval(node.getInterval());
				this.facade.process(query);
			}
			
			// prepare log message
			String msg = "";
			// print execution dependency graph (for debug only)
			for (ExecutionNodeStatus status : this.nodes.keySet())
			{
				// get nodes by status
				for (ExecutionNode node : this.nodes.get(status))
				{
					// print node and the related execution conditions
					msg += "Execution node " + node + "\n";
					msg += "\tNode execution starting conditions:\n";
					Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeStartDependencies(node);
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
			logger.debug(msg);
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
	
	/**
	 * 
	 * @return
	 */
	public List<ExecutionNode> getNodesByStatus(ExecutionNodeStatus status) 
	{
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
	public synchronized void updateNodeStatus(ExecutionNode node, ExecutionNodeStatus status) 
	{
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
	public Map<ExecutionNode, ExecutionNodeStatus> getNodeStartDependencies(ExecutionNode node) {
		return new HashMap<>(this.sdg.get(node));
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public Map<ExecutionNode, ExecutionNodeStatus> getNodeEndDependencies(ExecutionNode node) {
		return new HashMap<>(this.edg.get(node));
	}
	
	/**
	 * 
	 * @param node
	 */
	public void checkSchedule(ExecutionNode node) 
	{
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
	public void addNode(ExecutionNode node) 
	{
		// check expected initial execution state
		ExecutionNodeStatus initial = node.getStartExecutionState();
		node.setStatus(initial);
		this.nodes.get(initial).add(node);
		// setup dependency graph data structures
		this.sdg.put(node, new HashMap<ExecutionNode, ExecutionNodeStatus>());
		this.edg.put(node, new HashMap<ExecutionNode, ExecutionNodeStatus>());
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
	 * @param virtual
	 * @param toExecute
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	protected ExecutionNode createNode(String component, String timeline, 
			String signature, ParameterType[] pTypes, String[] pValues, 
			long[] start, long[] end, long[] duration, 
			ControllabilityType controllability,
			boolean virtual, ExecutionNodeStatus state) 
			throws TemporalIntervalCreationException  
	{
		// create temporal interval - consider all intervals as controllable during execution
		TemporalInterval interval = this.facade.createTemporalInterval(start, end, duration, true);
		
		// create predicate
		NodePredicate predicate = new NodePredicate(component, timeline, signature, pTypes, pValues); 
		// create execution node
		return new ExecutionNode(predicate, interval, controllability, virtual, state);
	}
	
	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	protected void addStartExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus condition) {
		// add node's start dependency and related condition
		this.sdg.get(node).put(dependency, condition);
	}

	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	protected void addEndExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus condition) {
		// add node's end dependency and related condition
		this.edg.get(node).put(dependency, condition);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean checkEndExecutionDependencies(ExecutionNode node) 
	{
		// flag 
		boolean canEnd = true;
		Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeEndDependencies(node);
		if (!dependencies.isEmpty()) {
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			while (it.hasNext() && canEnd) {
				// get next dependency
				ExecutionNode d = it.next();
				canEnd = d.getStatus().equals(dependencies.get(d));
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
	public boolean checkStartExecutionDependencies(ExecutionNode node) 
	{
		// flag
		boolean canStart = true;
		// get node's start dependencies
		Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeStartDependencies(node);
		if (!dependencies.isEmpty()) {
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			while (it.hasNext() && canStart) {
				// get a dependency parent
				ExecutionNode d = it.next();
				// check condition
				canStart = d.getStatus().equals(dependencies.get(d));
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
			throws TemporalConstraintPropagationException 
	{
		// fix start time first
		FixIntervalDurationConstraint fix = this.facade.
				createTemporalConstraint(TemporalConstraintType.FIX_INTERVAL_DURATION);
		fix.setReference(node.getInterval());
		fix.setDuration(duration);
		// propagate constraint
		this.facade.propagate(fix);
		try 
		{
			// check the consistency of the resulting network
			this.facade.verify();
		}
		catch (ConsistencyCheckException ex) {
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
			throws TemporalConstraintPropagationException 
	{
		// create constraint
		FixTimePointConstraint fix = this.facade.
				createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
		// set time point
		fix.setReference(node.getInterval().getStartTime());
		fix.setTime(time);
		// propagate constraint
		this.facade.propagate(fix);
		try 
		{
			// check consistency of the resulting network
			this.facade.verify();
		}
		catch (ConsistencyCheckException ex) {
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
			throws TemporalConstraintPropagationException 
	{
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
			this.facade.verify();
		}
		catch (ConsistencyCheckException ex) {
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
			throws Exception
	{
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
		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
//		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareMeetsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception
	{
		// create and propagate temporal constraint
		MeetsIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.MEETS);
		// set data
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// propagate constraint
		this.facade.propagate(constraint);
		
		// set execution constraints
		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
//		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareAfterTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
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
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
//		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.WAITING);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
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
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
		
//		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.WAITING);
		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareContainsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
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
		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
		
//		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareEqualsTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
		// create constraint
		EqualsIntervalConstraint constraint = this.facade.
				createTemporalConstraint(TemporalConstraintType.EQUALS);
		// set references
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// propagate temporal constraint
		this.facade.propagate(constraint);
		// add execution dependencies
//		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
//		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
//		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
//		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareStartsDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
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
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param bounds
	 * @throws Exception
	 */
	protected void prepareEndsDuringTemporalConstraint(ExecutionNode reference, ExecutionNode target, long[][] bounds) 
			throws Exception 
	{
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
		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
	}
}