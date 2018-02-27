package it.istc.pst.platinum.executive.dc;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dc.tga.TGADCChecker;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
@MonitorConfiguration(monitor = DCMonitor.class)
@DispatcherConfiguration(dispatcher = DCDispatcher.class)
public class DCExecutive extends Executive 
{
	protected DCChecker checker;			// dynamic controllability checker
	
	/**
	 * 
	 */
	protected DCExecutive() {
		super();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// create DC checker
		this.checker = new TGADCChecker();
	}
}
