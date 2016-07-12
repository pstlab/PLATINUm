package it.uniroma3.epsl2.executive;

import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ClockReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ExecutivePlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.PlanDispatcherReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.PlanMonitorReference;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Executive <C extends ClockManager, D extends PlanDispatcher, M extends PlanMonitor> extends ApplicationFrameworkObject {

	@ClockReference
	protected C clock;					// execution clock controller
	
	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// the plan to execute
	
	@PlanMonitorReference
	protected M monitor;					// plan monitor
	
	@PlanDispatcherReference
	protected D dispatcher;					// dispatching process
	
	/**
	 * This method allows to initialize the executive system on a already defined
	 * plan data-base and clock instances.
	 * 
	 * It could be useful if several executive systems are needed to run in parallel
	 * on the same plan data-base (e.g. see the Robot and Human executive systems for 
	 * the FourByThree project).
	 * 
	 * 
	 * @param pdb
	 * @param clock
	 */
	public abstract void init(ExecutivePlanDataBaseManager pdb, C clock);
	
	/**
	 * This method allows to initialize an executive system on a generated plan.
	 * 
	 * It builds the plan data-based related to the generated plan and initializes
	 * the clock, the dispatcher and the monitor processes.
	 * 
	 * @param plan
	 */
	public abstract void init(EPSLPlanDescriptor plan);
	
	/**
	 * Blocking method which starts the execution of the plan
	 * 
	 * @throws InterruptedException
	 */
	public abstract void start() 
			throws InterruptedException;
	
	/**
	 * Blocking method which ends the execution of the plan
	 *  
	 * @throws InterruptedException
	 */
	public abstract void stop() 
			throws InterruptedException;
	
	/**
	 * Blocking method which start the execution of the plan and
	 *  waits for completion.
	 */
	public abstract void execute() 
			throws InterruptedException;
}
