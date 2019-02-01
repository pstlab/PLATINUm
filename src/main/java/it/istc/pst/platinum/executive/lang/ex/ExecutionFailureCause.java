package it.istc.pst.platinum.executive.lang.ex;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutionFailureCause 
{
	private ExecutionFailureCauseType type;
	private long interruptionTick;
	private ExecutionNode interruptionNode;
	private List<PlanRepairInformation> repairInfo;
	
	
	/**
	 * 
	 * @param tick
	 * @param type
	 * @param node
	 */
	public ExecutionFailureCause(long tick, ExecutionFailureCauseType type, ExecutionNode node) {
		this.type = type;
		this.interruptionTick = tick;
		this.interruptionNode = node;
		this.repairInfo = new ArrayList<>();
	}

	/**
	 * 
	 * @param node
	 * @param duration
	 */
	public void addRepairInfo(ExecutionNode node, long duration) {
		this.repairInfo.add(new PlanRepairInformation(node, duration));
	}
	
	public List<PlanRepairInformation> getRepairInfo() {
		return new ArrayList<>(repairInfo);
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getInterruptionNode() {
		return interruptionNode;
	}
	
	/**
	 * 
	 * @param type
	 */
	public ExecutionFailureCause(ExecutionFailureCauseType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCauseType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getInterruptionTick() {
		return interruptionTick;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FailureCause tick: " + this.interruptionTick + ", type: " + this.type + "]";
	}
}
