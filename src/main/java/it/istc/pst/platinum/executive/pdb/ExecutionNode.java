package it.istc.pst.platinum.executive.pdb;

import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.platinum.framework.parameter.lang.ParameterType;
import it.istc.pst.platinum.framework.time.TemporalInterval;

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
//	private boolean virtual;								// a flag denoting whether the temporal information of a tokne must be actually propagated or not 
	
	private final Object lock = new Object();
	private ExecutionNodeStatus status;						// execution node state
	private ExecutionNode prev;								// link to the execution node encapsulating the previous token of the timeline
	private ExecutionNode next;								// link to the execution node encapsulating the next token of the timeline
	
	private ExecutionNodeStatus startExecutionState;		// start execution knowledge
	
	
	/**
	 * 
	 * @param predicate
	 * @param interval
	 * @param type
	 * @param virtual
	 * @param toExecute
	 */
	protected ExecutionNode(NodePredicate predicate, TemporalInterval interval, ControllabilityType type, boolean virtual, ExecutionNodeStatus status) 
	{
		this.id = COUNTER.getAndIncrement();
		this.controllability = type;
		this.status = ExecutionNodeStatus.WAITING;
		this.interval = interval;
		this.predicate = predicate;
		// set virtual flag
//		this.virtual = virtual;
		// set initial execution status
		this.startExecutionState = status;
		// set next and previous
		this.next = null;
		this.prev = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getNext() {
		return next;
	}
	
	/**
	 * 
	 * @param next
	 */
	public void setNext(ExecutionNode next) {
		this.next = next;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getPrev() {
		return prev;
	}
	
	/**
	 * 
	 * @param prev
	 */
	public void setPrev(ExecutionNode prev) {
		this.prev = prev;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNodeStatus getStartExecutionState() {
		return startExecutionState;
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public boolean isVirtual() {
//		return virtual;
//	}
	
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
	 * @param type
	 */
	public void setControllabilityType(ControllabilityType type) {
		this.controllability = type;
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
