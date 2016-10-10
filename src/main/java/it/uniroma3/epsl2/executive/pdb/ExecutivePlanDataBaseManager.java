package it.uniroma3.epsl2.executive.pdb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacade;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.lang.FixDurationIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixEndTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixStartTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.IntervalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalScheduleQuery;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

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
	protected TemporalQueryFactory qFactory;			// interval query factory
	protected IntervalConstraintFactory iFactory;		// interval constraint factory
	protected TemporalDataBaseFacade facade;			// temporal data base
	
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
		
		// create factories
		this.iFactory = IntervalConstraintFactory.getInstance();
		this.qFactory = TemporalQueryFactory.getInstance();
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.OFF);
		// create temporal facade
		TemporalDataBaseFacadeFactory factory = new TemporalDataBaseFacadeFactory();
		this.facade = factory.create(TemporalDataBaseFacadeType.UNCERTAINTY_TEMPORAL_FACADE, origin, horizon);
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
	public abstract void setup(SolutionPlan plan);
	
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
		CheckIntervalScheduleQuery query = this.qFactory.
				 create(TemporalQueryType.CHECK_SCHEDULE);
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
	protected ExecutionNode createNode(String component, 
			String signature, ParameterType[] pTypes, String[] pValues, 
			long[] start, long[] end, long[] duration, 
			ControllabilityType controllability) 
			throws TemporalIntervalCreationException  
	{
		// check interval controllability
		boolean controllableInterval = controllability.equals(ControllabilityType.UNCONTROLLABLE) || 
				controllability.equals(ControllabilityType.UNCONTROLLABLE_DURATION) ? false : true;
		// create temporal interval
		TemporalInterval interval = this.facade.createTemporalInterval(start, end, duration, controllableInterval);
		
		// create predicate
		NodePredicate predicate = new NodePredicate(component, signature, pTypes, pValues); 
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
	public boolean canEndExecution(ExecutionNode node) 
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
	public boolean canStartExecution(ExecutionNode node) 
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
	 * @throws Exception
	 */
	public void scheduleDuration(ExecutionNode node, long duration) 
			throws Exception 
	{
		// fix start time first
		FixDurationIntervalConstraint fix = this.iFactory.create(TemporalConstraintType.FIX_DURATION);
		fix.setReference(node.getInterval());
		fix.setDuration(duration);
		// propagate constraint
		this.facade.propagate(fix);
	}
	
	/**
	 * 
	 * @param node
	 * @param start
	 * @throws Exception
	 */
	public void scheduleStartTime(ExecutionNode node, long start) 
			throws Exception 
	{
		// create constraint
		FixStartTimeIntervalConstraint fix = this.iFactory.create(TemporalConstraintType.FIX_START_TIME);
		fix.setReference(node.getInterval());
		fix.setStart(start);
		// propagate constraint
		this.facade.propagate(fix);
	}

	/**
	 * 
	 * @param node
	 * @param end
	 */
	public void scheduleEndTime(ExecutionNode node, long end) 
			throws Exception 
	{
		// create constraint
		FixEndTimeIntervalConstraint fix = this.iFactory.create(TemporalConstraintType.FIX_END_TIME);
		fix.setReference(node.getInterval());
		fix.setEnd(end);
		// propagate constraint
		this.facade.propagate(fix);
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
		TemporalConstraint constraint = this.iFactory.create(TemporalConstraintType.BEFORE);
		// set data
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.create(TemporalConstraintType.MEETS);
		// set data
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.AFTER);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.DURING);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.CONTAINS);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.EQUALS);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.STARTS_DURING);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		// add start dependency
		this.addStartExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
//		this.addStartExecutionDependency(target, reference, ExecutionNodeStatus.WAITING);
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
		TemporalConstraint constraint = this.iFactory.
				create(TemporalConstraintType.ENDS_DURING);
		constraint.setReference(reference.getInterval());
		constraint.setTarget(target.getInterval());
		// set bounds
		constraint.setBounds(bounds);
		// propagate temporal constraint
		this.facade.propagate(constraint);
		// add end dependency
		this.addEndExecutionDependency(reference, target, ExecutionNodeStatus.IN_EXECUTION);
//		this.addEndExecutionDependency(target, reference, ExecutionNodeStatus.EXECUTED);
	}
}