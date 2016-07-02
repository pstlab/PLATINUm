package it.uniroma3.epsl2.executive.est;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.ClockManager;
import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.executive.pdb.epsl.EPSLExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.cfg.ExecutiveConfiguration;

/**
 * 
 * @author anacleto
 *
 */
@ExecutiveConfiguration(

	// set dispatcher
	dispatcher = EarliesStartTimePlanDispatcher.class,
	
	// set monitor
	monitor = UncontrollableDurationObservationPlanMonitor.class
)
public class EarliestStartTimeExecutive extends Executive<EarliesStartTimePlanDispatcher, UncontrollableDurationObservationPlanMonitor> {

	private boolean sharedClock;
	
	/**
	 * 
	 */
	@Override
	public void init(ExecutivePlanDataBaseManager pdb, ClockManager clock) {
		// set the clock
		this.clock = clock;
		this.sharedClock = true;		
		// set the plan data-base
		this.pdb = pdb;
		
		// create plan monitor
		this.monitor = new UncontrollableDurationObservationPlanMonitor(this);
		// create dispatcher
		this.dispatcher = new EarliesStartTimePlanDispatcher(this);
	}
	
	/**
	 * 
	 */
	@Override
	public void init(EPSLPlanDescriptor plan)  {
		// create clock
		this.clock = new ClockManager();
		this.sharedClock = false;
		// create execution plan data-base
		this.pdb = new EPSLExecutivePlanDataBaseManager(plan.getOrigin(), plan.getHorizon());
		// initialize plan data-base
		this.pdb.init(plan);
		
		// create plan monitor
		this.monitor = new UncontrollableDurationObservationPlanMonitor(this);
		// create dispatcher
		this.dispatcher = new EarliesStartTimePlanDispatcher(this);
	}
	
	/**
	 * 
	 */
	@Override
	public void start() {
		
		// start execution
		System.out.println("Starting execution....");
		// start monitor
		this.monitor.start();
		// start dispatcher
		this.dispatcher.start();
		// start clock
		if (!this.sharedClock) {
			this.clock.start();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void complete() 
			throws InterruptedException {
		
		// wait completion
		this.waitCompletion();
		
		System.out.println("Stopping execution...");
		// stop monitor
		this.monitor.stop();
		// stop dispatcher
		this.dispatcher.stop();
		// stop clock
		if (!this.sharedClock) {
			this.clock.stop();
		}
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
