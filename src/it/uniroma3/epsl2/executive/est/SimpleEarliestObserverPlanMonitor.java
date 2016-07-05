package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.PlanMonitor;
import it.uniroma3.epsl2.executive.pdb.ExecutionNode;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;

/**
 * 
 * @author anacleto
 *
 */
public class SimpleEarliestObserverPlanMonitor extends PlanMonitor 
{
	private final Thread process;
	
	/**
	 * 
	 * @param exec
	 */
	protected SimpleEarliestObserverPlanMonitor(Executive<?,?> exec) {
		super(exec);

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
	protected void onTick(long tick) {
		
		// get seconds w.r.t. the temporal origin
		long tau = this.clock.getSecondsFromTheOrigin(tick);
		// check nodes in execution
		for (ExecutionNode node : this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION)) {
			
			// check end condition
			if (this.pdb.canEndExecution(node)) {
				// check schedule
				this.pdb.checkSchedule(node);
				try {
					
					// schedule duration if needed
					if (node.getDuration()[0] != node.getDuration()[1]) {
						
						// schedule duration to lower bound
						this.pdb.scheduleDuration(node, node.getDuration()[0]);
						this.pdb.checkSchedule(node);
					}
				}
				catch (Exception ex) {
					throw new RuntimeException("{tick = " + tick + "} {PlanMonitor:SimpleEarliestObserverMonitor} -> Failure scheduling duration of node " + node + " ERROR {\n- message= " + ex.getMessage() + "\n}");
				}
				
				// check if execution ended
				long end = node.getEnd()[0];
				// check if delay
				if (tau >= end) {
					// update status
					this.pdb.updateNodeStatus(node, ExecutionNodeStatus.EXECUTED);
					System.out.println("{tick = " + tick +"} {PlanMonitor:SimpleEarliestObserverMonitor} -> End executing node " + node);
				}
			}
		}
	}
}
