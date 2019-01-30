package it.istc.pst.platinum.control.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.platinum.executive.lang.ex.ExecutionFailureCause;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;

/**
 * 
 * @author anacleto
 *
 */
public class Goal implements Comparable<Goal> 
{
	private static AtomicLong GoalIdCounter = new AtomicLong(0);
	private long id;
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
		this.planningAttempts.add(new Long(time));
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
		this.executionAttempts.add(new Long(time));
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
		this.contingencyAttempts.add(new Long(time));
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
		// chronological order
		return this.timestamp < o.timestamp ? -1 : this.timestamp > o.timestamp ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Goal id: " + this.id +", status: " + this.status + "]";
	}
}
