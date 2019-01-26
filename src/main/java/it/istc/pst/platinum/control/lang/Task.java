package it.istc.pst.platinum.control.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;

/**
 * 
 * @author anacleto
 *
 */
public class Task implements Comparable<Task> 
{
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	private long id;
	private long timestamp;
	private int attempts;								// number of deliberation attempts
	private TaskType type;
	private TaskStatus status;
	private EPSLPlanDescriptor plan;
	private long tickStart;
	private long tickInterrupt;
	private ExecutionNode interruptNode;
	private List<ExecutionNode> trace;
	private String assignmentStrategy;
	
	private PlanProtocolDescriptor pp;
	
	/**
	 * 
	 * @param type
	 */
	public Task(TaskType type) 
	{ 
		this.attempts = 0;
		this.id = ID_COUNTER.getAndIncrement();
		this.timestamp = System.currentTimeMillis();
		this.type = type;
		this.status = null;
		this.plan = null;
		this.tickStart = 0;
		this.tickInterrupt = 0;
		this.trace = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param assignmentStrategy
	 */
	public void setScenario(String assignmentStrategy) {
		this.assignmentStrategy = assignmentStrategy;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getScenario() {
		return assignmentStrategy;
	}
	
	/**
	 * 
	 */
	public void incrementAttempt() {
		this.attempts++;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAttempts() {
		return attempts;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 
	 * @param tickStart
	 */
	public void setStartTick(long tickStart) {
		this.tickStart = tickStart;
	}
	
	/**
	 * 
	 * @param interruptTick
	 */
	public void setInterruptTick(long interruptTick) {
		this.tickInterrupt = interruptTick;
	}
	
	/**
	 * 
	 * @param interruptNode
	 */
	public void setInterruptNode(ExecutionNode interruptNode) {
		this.interruptNode = interruptNode;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getInterruptNode() {
		return interruptNode;
	}

	/**
	 * 
	 * @return
	 */
	public long getInterruptTick() {
		return this.tickInterrupt;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTickStart() {
		return tickStart;
	}
	
	/**
	 * 
	 * @return
	 */
	public TaskType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public TaskStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @return
	 */
	public EPSLPlanDescriptor getPlan() {
		return plan;
	}
	
	public PlanProtocolDescriptor getPlanProtocol() {
		return pp;
	}
	
	public void setPlanProtocol(PlanProtocolDescriptor pp) {
		this.pp = pp;
	}
	
	/**
	 * 
	 * @param plan
	 */
	public void setPlan(EPSLPlanDescriptor plan) {
		this.plan = plan;
	}
	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	/**
	 * 
	 * @param trace
	 */
	public void setExecutionTrace(List<ExecutionNode> trace) {
		this.trace = new ArrayList<>(trace);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ExecutionNode> getExecutionTrace() {
		return new ArrayList<>(this.trace);
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
		Task other = (Task) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Task o) {
		// chronological order
		return this.timestamp <= o.timestamp ? -1 : 1;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Task id= " + this.id +" type= " + this.type + " status= " + this.status + "]";
	}
	
}
