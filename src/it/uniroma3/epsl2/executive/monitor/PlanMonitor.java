package it.uniroma3.epsl2.executive.monitor;

import it.uniroma3.epsl2.executive.ClockManager;
import it.uniroma3.epsl2.executive.pdb.ExecutivePlanDataBaseManager;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ClockReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.executive.inject.ExecutivePlanDataBaseReference;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlanMonitor extends ApplicationFrameworkObject {

	@ExecutivePlanDataBaseReference
	protected ExecutivePlanDataBaseManager pdb;		// plan data base
	
	@ClockReference
	protected ClockManager clock;					// clock 
	
	/**
	 * 
	 * @param clock
	 * @param pdb
	 */
	public PlanMonitor(ClockManager clock, ExecutivePlanDataBaseManager pdb) {
		super();
		// set clock manager
		this.clock = clock;
		// set executive plan data base
		this.pdb = pdb;
	}
	
	/**
	 * 
	 */
	public abstract void start();
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract void stop() 
			throws InterruptedException;
}
