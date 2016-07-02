package it.uniroma3.epsl2.executive.pdb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacade;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.lang.FixDurationIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixEndTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixStartTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.IntervalConstraintFactory;
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
	// plan locks
	private final Object[] locks = {
		new Object(),	// wait status lock
		new Object(),	// ready status lock
		new Object(),	// scheduled status lock
		new Object(),	// in_execution status lock
		new Object(),	// executed status lock
	};
	
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
	public ExecutivePlanDataBaseManager(long origin, long horizon) {
		
		super();
		// create factories
		this.iFactory = IntervalConstraintFactory.getInstance();
		this.qFactory = TemporalQueryFactory.getInstance();
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.OFF);
		// create temporal facade
		TemporalDataBaseFacadeFactory factory = new TemporalDataBaseFacadeFactory();
		this.facade = factory.createSingleton(TemporalDataBaseFacadeType.UNCERTAINTY_TEMPORAL_FACADE, origin, horizon);

		// initialize data structures
		this.nodes = new HashMap<>();
		this.nodes.put(ExecutionNodeStatus.WAIT, new LinkedList<>());
		this.nodes.put(ExecutionNodeStatus.READY, new LinkedList<>());
		this.nodes.put(ExecutionNodeStatus.SCHEDULED, new LinkedList<>());
		this.nodes.put(ExecutionNodeStatus.IN_EXECUTION, new LinkedList<>());
		this.nodes.put(ExecutionNodeStatus.EXECUTED, new LinkedList<>());
		
		// initialize the dependency graph
		this.sdg = new HashMap<>();
		this.edg = new HashMap<>();
	}
	
	/**
	 * 
	 * @param plan
	 */
	public abstract void init(EPSLPlanDescriptor plan);
	
	/**
	 * 
	 * @return
	 */
	public List<ExecutionNode> getNodesByStatus(ExecutionNodeStatus status) {
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
	public void updateNodeStatus(ExecutionNode node, ExecutionNodeStatus status) {
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
	public void checkSchedule(ExecutionNode node) {
		// check resulting schedule of the interval
		CheckIntervalScheduleQuery query = this.qFactory.create(TemporalQueryType.CHECK_SCHEDULE);
		query.setInterval(node.getInterval());
		this.facade.process(query);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addNode(ExecutionNode node) {
		// insert node
		node.setStatus(ExecutionNodeStatus.WAIT);
		this.nodes.get(ExecutionNodeStatus.WAIT).add(node);
		this.sdg.put(node, new HashMap<>());
		this.edg.put(node, new HashMap<>());
	}
	
	/**
	 * 
	 * @param predicate
	 * @param start
	 * @param end
	 * @param duration
	 * @param controllability
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	protected ExecutionNode createNode(String predicate, long[] start, long[] end, long[] duration, ControllabilityType controllability) 
			throws TemporalIntervalCreationException  {
		// check interval controllability
		boolean controllableInterval = controllability.equals(ControllabilityType.EXTERNAL_TOKEN) || 
				controllability.equals(ControllabilityType.UNCONTROLLABLE_DURATION) ? false : true;
		// create temporal interval
		TemporalInterval interval = this.facade.createTemporalInterval(start, end, duration, controllableInterval);
		// create execution node
		return new ExecutionNode(predicate, interval, controllability);
	}
	
	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	public void addStartExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus condition) {
		// add node's start dependency and related condition
		this.sdg.get(node).put(dependency, condition);
	}

	/**
	 * 
	 * @param node
	 * @param dependency
	 * @param condition
	 */
	public void addEndExecutionDependency(ExecutionNode node, ExecutionNode dependency, ExecutionNodeStatus condition) {
		// add node's end dependency and related condition
		this.edg.get(node).put(dependency, condition);
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean canEndExecution(ExecutionNode node) {
		boolean canEnd = true;
		Map<ExecutionNode, ExecutionNodeStatus> conditions = this.getNodeEndDependencies(node);
		Iterator<ExecutionNode> it = conditions.keySet().iterator();
		while (it.hasNext() && canEnd) {
			// get next dependency
			ExecutionNode d = it.next();
			canEnd = d.getStatus().equals(conditions.get(d));
		}
		
		// true if the node can end execution
		return canEnd;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public boolean canStartExecution(ExecutionNode node) {
		// flag
		boolean ready = true;
		// get node's start dependencies
		Map<ExecutionNode, ExecutionNodeStatus> dependencies = this.getNodeStartDependencies(node);
		if (!dependencies.isEmpty()) {
			// check if conditions are satisfied
			Iterator<ExecutionNode> it = dependencies.keySet().iterator();
			while (it.hasNext() && ready) {
				// get a dependency parent
				ExecutionNode d = it.next();
				// check condition
				ready = d.getStatus().equals(dependencies.get(d));
			}
		}
		// true if ready
		return ready;
	}

	/**
	 * 
	 * @param node
	 * @param duration
	 * @throws Exception
	 */
	public void scheduleDuration(ExecutionNode node, long duration) 
			throws Exception {
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
			throws Exception {
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
			throws Exception {
		// create constraint
		FixEndTimeIntervalConstraint fix = this.iFactory.create(TemporalConstraintType.FIX_END_TIME);
		fix.setReference(node.getInterval());
		fix.setEnd(end);
		// propagate constraint
		this.facade.propagate(fix);
	}
}