package it.istc.pst.platinum.executive.dc;

import it.istc.pst.platinum.executive.monitor.Monitor;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public class DCMonitor extends Monitor
{
	@ExecutivePlaceholder
	private DCExecutive executive;
	
	/**
	 * 
	 */
	protected DCMonitor() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public void handleTick(long tick) 
	{
		// convert tick to tau
		long tau = this.executive.convertTickToTau(tick);
		
		/*
		 * TODO : check received notifications 
		 */
		
		// create notification status
		PlanExecutionStatus status = new PlanExecutionStatus(tau);
		
		/*
		 * TODO : prepare notification data
		 */
		
		// notify DC checker
		if (!this.executive.checker.notify(status)) {
			
			/*
			 * TODO : handle failure
			 */
		}
		
	}
}
