package it.cnr.istc.pst.platinum.control.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.PlanProtocolDescriptor;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class Goal implements Comparable<Goal>, Comparator<ExecutionNode>
{
	private static AtomicLong GoalIdCounter = new AtomicLong(0);
	private long id;
	private long priority; 								// goal priority
	private long timestamp;
	private AgentTaskDescription description;			// goal and facts description 
	private GoalStatus status;
	private SolutionPlan plan;							// (current) solution plan for the goal
	
	private List<Long> planningAttempts;				// array counting planning attempts and keeping track of planning times
	private List<Long> executionAttempts;				// array counting execution attempts and keeping track of execution times
	private List<Long> contingencyAttempts;				// array counting contingency attempts and keeping track of their handling times

	private List<PlanProtocolDescriptor> plans;			// trace of generated plans
	private ExecutionFailureCause failureCause;			// goal execution failure cause
	private Map<String, List<ExecutionNode>> trace;		// execution trace
	
	private boolean repaired;
	private long executionTick;
	//EDIT POINTER
	private long uppaalTime;
	private boolean existsStrategy;
	private long managementStrategyTime;
	private boolean isOutOfBounds;
	private int maxOutOfBounds;
	
	
	/**
	 * 
	 * @param description
	 */
	public Goal(AgentTaskDescription description) 
	{ 
		this.id = GoalIdCounter.getAndIncrement();
		this.timestamp = System.currentTimeMillis();
		this.description = description;
		this.status = null;
		this.plan = null;
		this.planningAttempts = new ArrayList<>();
		this.executionAttempts = new ArrayList<>();
		this.contingencyAttempts = new ArrayList<>();
		this.plans = new ArrayList<>();
		this.trace = new HashMap<>();
		this.failureCause = null;
		this.repaired = false;
		this.executionTick = 0;
		//EDIT POINTER
		this.uppaalTime = -1;
		this.existsStrategy = false;
		this.managementStrategyTime = -1;
		this.isOutOfBounds = false;
		this.maxOutOfBounds = -1;
	}
	
	/**
	 * 
	 * @param priority
	 */
	public void setPriority(long priority) {
		this.priority = priority;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getPriority() {
		return priority;
	}
	
	/**
	 * 
	 * @param repaired
	 */
	public void setRepaired(boolean repaired) {
		this.repaired = repaired;
	}
	
	/**
	 * 
	 */
	public void clearExecutionTrace() {
		// clear trace
		this.trace.clear();
		// clear failure cause
		this.failureCause = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isRepaired() {
		return this.repaired;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getExecutionTick() {
		return executionTick;
	}
	
	public boolean isOutOfBounds() {
		return isOutOfBounds;
	}

	public void setOutOfBounds(boolean isOutOfBounds) {
		this.isOutOfBounds = isOutOfBounds;
	}

	public int getMaxOutOfBounds() {
		return maxOutOfBounds;
	}

	public void setMaxOutOfBounds(int maxOutOfBounds) {
		this.maxOutOfBounds = maxOutOfBounds;
	}

	/**
	 * 
	 * @param executionTick
	 */
	public void setExecutionTick(long executionTick) {
		this.executionTick = executionTick;
	}
	
	/**
	 * 
	 * @param failureCause
	 */
	public void setFailureCause(ExecutionFailureCause failureCause) {
		this.failureCause = failureCause;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCause getFailureCause() {
		return failureCause;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addNodeToExecutionTrace(ExecutionNode node) {
		if (!this.trace.containsKey(node.getComponent())) {
			this.trace.put(node.getComponent(), new ArrayList<>());
		}
		
		this.trace.get(node.getComponent()).add(node);
	}
	
	/**
	 * 
	 * @param name
	 */
	public List<ExecutionNode> getExecutionTraceByComponentName(String name) {
		// list of executed nodes
		List<ExecutionNode> list = new ArrayList<>();
		if (this.trace.containsKey(name)) {
			// add nodes
			list.addAll(this.trace.get(name));
		}
		
		// sort the trace
		Collections.sort(list, this);
		// get the list
		return list;
	}
	
	/**
	 * Add planning attempt time in milliseconds
	 * 
	 * @param time
	 */
	public void addPlanningAttempt(long time) {
		// add planning time
		this.planningAttempts.add(time);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPlanningAttempts() {
		return this.planningAttempts.size();
	}
	
	/**
	 * Get the total planning time in milliseconds
	 * 
	 * @return
	 */
	public long getTotalPlanningTime() 
	{
		// compute total planning time 
		long total = 0;
		for (Long time : this.planningAttempts) {
			total += time;
		}
		
		// get total planning time
		return total;
	}
	
	/**
	 * Add execution attempt time in milliseconds
	 * 
	 * @param time
	 */
	public void addExecutionAttempt(long time) {
		// add execution time 
		this.executionAttempts.add(time);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getExecutionAttempts() {
		return this.executionAttempts.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTotalExecutionTime() {
		// compute total execution time 
		long total = 0;
		for (Long time : this.executionAttempts) {
			total += time;
		}
		
		// get total planning time
		return total;
	}
	
	/**
	 * Add contingency handling time in milliseconds
	 * 
	 * @param time
	 */
	public void addContingencyHandlingAttempt(long time) {
		// add contingency time
		this.contingencyAttempts.add(time);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getContingencyHandlingAttempts() {
		return this.contingencyAttempts.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTotalContingencyHandlingTime() {
		// compute total contingency handling time 
		long total = 0;
		for (long time : this.contingencyAttempts) {
			total += time;
		}
		
		// get total planning time
		return total;
	}
	
	/**
	 * 
	 * @param plan
	 */
	public void setPlan(SolutionPlan plan) {
		this.plan = plan;
		// add exported plan 
		this.plans.add(plan.export());
	}
	
	/**
	 * 
	 * @return
	 */
	public AgentTaskDescription getTaskDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return
	 */
	public SolutionPlan getPlan() {
		return plan;
	}
	
	/**
	 * 
	 * @return
	 */
	public GoalStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(GoalStatus status) {
		this.status = status;
	}
	
	//EDIT POINTER
	
	public long getUppaalTime() {
		return uppaalTime;
	}

	public void setUppaalTime(long uppaalTime) {
		this.uppaalTime = uppaalTime;
	}

	public long getManagementStrategyTime() {
		return managementStrategyTime;
	}

	public void setManagementStrategyTime(long managementStrategyTime) {
		this.managementStrategyTime = managementStrategyTime;
	}
	
	public boolean isExistsStrategy() {
		return existsStrategy;
	}

	public void setExistsStrategy(boolean existsStrategy) {
		this.existsStrategy = existsStrategy;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Goal other = (Goal) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Goal o) {
		
		// compare priority and time
		return this.priority > o.priority ? -1 : 
			this.priority == o.priority && this.timestamp < o.timestamp ? -1 : 
				this.priority == o.priority && this.timestamp == o.timestamp ? 0 : 1;
	}
	
	/**
	 * Completely instantiated nodes are expected
	 */
	@Override
	public int compare(ExecutionNode o1, ExecutionNode o2) {
		
		// compare start times
		TimePoint s1 = o1.getInterval().getStartTime();
		TimePoint s2 = o2.getInterval().getStartTime();
		// compare lower bounds
		return s1.getLowerBound() < s2.getLowerBound() ? -1 : s1.getLowerBound() > s2.getLowerBound() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Goal id: " + this.id +", priority: " + this.priority + ",  status: " + this.status + "]";
	}

}
