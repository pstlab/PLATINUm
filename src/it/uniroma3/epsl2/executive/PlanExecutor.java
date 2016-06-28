package it.uniroma3.epsl2.executive;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.dispatcher.EarliesStartTimePlanDispatcher;
import it.uniroma3.epsl2.executive.dispatcher.PlanDispatcher;
import it.uniroma3.epsl2.executive.monitor.PlanMonitor;
import it.uniroma3.epsl2.executive.monitor.UncontrollableDurationObservationPlanMonitor;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.executive.pdb.epsl.EPSLExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ClockReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ExecutivePlanDataBaseReference;

/**
 * 
 * @author anacleto
 *
 */
public class PlanExecutor extends ApplicationFrameworkObject {
	
	@ExecutivePlanDataBaseReference
	private ExecutivePlanDataBaseManager pdb;	// the plan to execute
	
	@ClockReference
	private ClockManager clock;						// execution clock controller
	
	private PlanMonitor monitor;					// plan monitor
	private PlanDispatcher dispatcher;				// dispatching process
	
	/**
	 * 
	 */
	public PlanExecutor() {
		super();
	}
	
	/**
	 * 
	 * @param plan
	 */
	public void init(EPSLPlanDescriptor plan) {
		// create execution plan data-base
		this.pdb = new EPSLExecutivePlanDataBaseManager(plan.getOrigin(), plan.getHorizon());
		// initialize plan data-base
		this.pdb.init(plan);
		// create clock
		this.clock = new ClockManager();
		// create plan monitor
		this.monitor = new UncontrollableDurationObservationPlanMonitor(this.clock, this.pdb);
		// create dispatcher
		this.dispatcher = new EarliesStartTimePlanDispatcher(this.clock, this.pdb);
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void executePlan() 
			throws InterruptedException {
		// start execution
		System.out.println("Starting execution...");
		this.monitor.start();
		this.dispatcher.start();
		this.clock.start();
		
		// wait completion
		this.waitCompletion();
		
		System.out.println("Stopping execution...");
		// stop execution
		this.monitor.stop();
		this.dispatcher.stop();
		this.clock.stop();
		// stop execution
		System.out.println("Stopped!");
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	private void waitCompletion() 
			throws InterruptedException {
		
		// create process
		Thread t = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() {
				boolean running = true;
				while (running) {
					try {
						// wait clock event
						clock.waitTick();
						// check if execution is complete
						running = !pdb.getNodesByStatus(ExecutionNodeStatus.WAIT).isEmpty() ||
								!pdb.getNodesByStatus(ExecutionNodeStatus.READY).isEmpty() ||
								!pdb.getNodesByStatus(ExecutionNodeStatus.SCHEDULED).isEmpty() ||
								!pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
					}
					catch (InterruptedException ex) {
						// stop process
						running = false;
					}
				}
			}
		});
		
		// start process
		t.start();
		t.join();
	}
}
