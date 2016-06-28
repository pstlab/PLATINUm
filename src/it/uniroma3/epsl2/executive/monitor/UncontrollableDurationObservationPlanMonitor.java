package it.uniroma3.epsl2.executive.monitor;

import it.uniroma3.epsl2.executive.ClockManager;
import it.uniroma3.epsl2.executive.pdb.ControllabilityType;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;

/**
 * 
 * @author anacleto
 *
 */
public class UncontrollableDurationObservationPlanMonitor extends PlanMonitor 
{
	private final Thread process;
	
	/**
	 * 
	 * @param clock
	 * @param pdb
	 */
	public UncontrollableDurationObservationPlanMonitor(ClockManager c, ExecutivePlanDataBaseManager pdb) {
		super(c, pdb);

		// create thread
		this.process = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				boolean running = true;
				while (running) {
					try {
						// wait tick
						long tick = clock.waitTick();
						// manage the observation
						onTick(tick);
					}
					catch (InterruptedException ex) {
						// stop thread
						running = false;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 */
	public void start() {
		synchronized (this.process) {
			if (!this.process.isAlive()) {
				// start process
				this.process.start();
			}
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException {
		synchronized (this.process) {
			// check process
			if (this.process.isAlive()) {
				// stop process
				this.process.interrupt();
				this.process.join();
			}
		}
	}
	
	/**
	 * 
	 * @param tick
	 */
	protected void onTick(long tick) 
	{
		// check nodes in execution
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION)) 
		{
			// check schedule
			this.pdb.checkSchedule(node);
			
			// check controllability type duration
			if (node.getControllabilityType().equals(ControllabilityType.UNCONTROLLABLE_DURATION) && 
					node.getDuration()[0] != node.getDuration()[1]) 
			{
				try {
					// observation duration (set the lower bound)
					this.pdb.scheduleDuration(node, node.getDuration()[0]);
					// check schedule
					this.pdb.checkSchedule(node);
				}
				catch (Exception ex) {
					System.err.println("ERROR {\n- Failure while propagating duration (" + node.getDuration()[0] + ") observation on node " + node + "\n}\n" + ex.getMessage());
				}
			}
			
			// compute end time (in seconds from the origin)
			long endTime = node.getEnd()[0];
			long seconds = this.clock.getSecondsFromTheOrigin(tick);
			// check execution condition
			if (this.pdb.canEndExecution(node) && seconds == endTime) {
				// update status
				this.pdb.updateNodeStatus(node, ExecutionNodeStatus.EXECUTED);
				System.out.println("{tick = " + tick + "} {Monitor} -> End executing node " + node);
			}
			
			// check delay
			if (this.pdb.canEndExecution(node) && seconds > endTime) {
				// get delay
				double delay = seconds - endTime;
				this.pdb.updateNodeStatus(node, ExecutionNodeStatus.EXECUTED);
				System.err.println("WARNING {\n- {tick = " + tick + "} {Monitor} -> Execution of node " + node + " ended with delay " + delay + " (seconds)\n}\n");
			}
		}
	}
}
