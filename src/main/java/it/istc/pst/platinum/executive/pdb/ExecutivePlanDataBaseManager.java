package it.istc.pst.platinum.executive.pdb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.parameter.lang.ParameterType;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.TemporalFacadeFactory;
import it.istc.pst.platinum.framework.time.TemporalFacadeType;
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
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggerFactory;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;
import it.istc.pst.platinum.protocol.lang.PlanProtocolDescriptor;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutivePlanDataBaseManager extends ApplicationFrameworkObject 
{
	private long origin;
	private long horizon;
	
	// plan locks
	private final Object[] locks;
	protected TemporalFacade facade;			// temporal data base
	
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
	public ExecutivePlanDataBaseManager(long origin, long horizon) 
	{
		super();
		// set origin and horizon
		this.origin = origin;
		this.horizon = horizon;
		
		// initialize array of locks
		this.locks = new Object[ExecutionNodeStatus.values().length];
		// add locks 
		for (ExecutionNodeStatus s : ExecutionNodeStatus.values()) {
			// set lock
			this.locks[s.getIndex()] = new Object();
		}
		
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.OFF);
		// create temporal facade
		TemporalFacadeFactory factory = new TemporalFacadeFactory();
		this.facade = factory.create(TemporalFacadeType.UNCERTAINTY_TEMPORAL_FACADE, origin, horizon);
		// unregister facade
		factory.unregister(this.facade);

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
		return this.origin;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.horizon;
	}
	
	/**
	 * 
	 * @param plan
	 */
	public abstract void setup(PlanProtocolDescriptor plan);
	
	/**
	 * 
	 * @param plan
	 */
	public abstract void setup(EPSLPlanDescriptor plan);
	
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
		// insert node
		node.setStatus(ExecutionNodeStatus.WAITING);
		this.nodes.get(ExecutionNodeStatus.WAITING).add(node);
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
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	protected ExecutionNode createNode(String component, String timeline, 
			String signature, ParameterType[] pTypes, String[] pValues, 
			long[] start, long[] end, long[] duration, 
			ControllabilityType controllability) 
			throws TemporalIntervalCreationException  
	{
//		// check interval controllability
//		boolean controllableInterval = controllability.equals(ControllabilityType.UNCONTROLLABLE) || 
//				controllability.equals(ControllabilityType.PARTIALLY_CONTROLLABLE) ? false : true;
		// create temporal interval - consider all intervals as controllable during execution
		TemporalInterval interval = this.facade.createTemporalInterval(start, end, duration, true);
		
		// create predicate
		NodePredicate predicate = new NodePredicate(component, timeline, signature, pTypes, pValues); 
		// create execution node
		return new ExecutionNode(predicate, interval, controllability);
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
		// check node controllability and duration
		if ((node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE) || 
				node.getControllabilityType().equals(ControllabilityType.PARTIALLY_CONTROLLABLE)) && 
				(duration < node.getDuration()[0] || duration > node.getDuration()[1]))
		{
			// inconsistent duration to schedule
			throw new TemporalConstraintPropagationException("Invalid duration constraint for node\n- duration= " + duration +"\n- node= " + node + "\n");
		}
			
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
			this.facade.checkConsistency();
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
		try {
			// check consistency of the resulting network
			this.facade.checkConsistency();
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
			this.facade.checkConsistency();
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
		
		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.WAITING);
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
		
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
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
		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.IN_EXECUTION);
		
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.WAITING);
		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.EXECUTED);
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