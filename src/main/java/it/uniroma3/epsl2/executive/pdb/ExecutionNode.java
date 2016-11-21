package it.uniroma3.epsl2.executive.pdb;

import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutionNode implements Comparable<ExecutionNode> 
{
	private static AtomicLong COUNTER = new AtomicLong(0);

	private long id;
	private NodePredicate predicate;
	private ControllabilityType controllability;
	private TemporalInterval interval;
	
	private final Object lock = new Object();
	private ExecutionNodeStatus status;
	
	/**
	 * 
	 * @param predicate
	 * @param interval
	 * @param type
	 */
	protected ExecutionNode(NodePredicate predicate, TemporalInterval interval, ControllabilityType type) 
	{
		this.id = COUNTER.getAndIncrement();
		this.controllability = type;
		this.status = ExecutionNodeStatus.WAITING;
		this.interval = interval;
		this.predicate = predicate;
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
	 * @return
	 */
	public String getGroundSignature() {
		return this.predicate.getGroundSignature();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSignature() {
		return this.predicate.getSignature();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponent() {
		return this.predicate.getComponent();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimeline() {
		return this.predicate.getTimeline();
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getValueOfParameter(int index) {
		return this.predicate.getValueOfParameter(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParameters() {
		return this.predicate.getParameters();
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public ParameterType getTypeOfParameter(int index) {
		return this.predicate.getTypeOfParameter(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public ControllabilityType getControllabilityType() {
		return this.controllability;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getInterval() {
		return interval;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDuration() {
		return new long[] {
				this.interval.getDurationLowerBound(),
				this.interval.getDurationUpperBound()
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStart() {
		return new long[] {
			this.interval.getStartTime().getLowerBound(),
			this.interval.getStartTime().getUpperBound()
				
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEnd() {
		return new long[] {
			this.interval.getEndTime().getLowerBound(),
			this.interval.getEndTime().getUpperBound()
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNodeStatus getStatus() {
		ExecutionNodeStatus s;
		synchronized (this.lock) {
			// get current status
			s = this.status;
		}
		// get current status detected
		return s;
	}
	
	/**
	 * 
	 * @param status
	 */
	protected void setStatus(ExecutionNodeStatus status) {
		synchronized (this.lock) {
			// update status
			this.status = status;
			// notify 
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public NodePredicate getPredicate() {
		return this.predicate;
	}
	
	/**
	 * Wait a change of the execution status and returns the detected status
	 * 
	 * @throws InterruptedException
	 */
	public ExecutionNodeStatus waitStatusChange() 
			throws InterruptedException {
		// token's execution status
		ExecutionNodeStatus s;
		synchronized (this.lock) {
			// wait a status change
			this.lock.wait();
			s = this.status;
		}
		// get current status
		return s;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ExecutionNode o) {
		return this.id <= o.id ? -1 : 1;
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
		ExecutionNode other = (ExecutionNode) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ExecNode id= " + this.id + " predicate=" + this.predicate.getComponent() + "." + this.predicate.getGroundSignature() + " status= " + this.status + " "
				+ "start= [" + this.interval.getStartTime().getLowerBound() + ", " + this.interval.getStartTime().getUpperBound() + "] "
				+ "duration= [" + this.interval.getDurationLowerBound() + ", " + this.interval.getDurationUpperBound() + "]]";
	}
}
