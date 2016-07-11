package it.uniroma3.epsl2.executive;

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
	 * @param exec
	 */
	public PlanMonitor(Executive<?,?> exec) {
		super();
		// set clock manager
		this.clock = exec.clock;
		// set executive plan data base
		this.pdb = exec.pdb;
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract void start() 
			throws InterruptedException;
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract void stop() 
			throws InterruptedException;
}
