package it.istc.pst.platinum.executive.dc;

import java.io.IOException;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.istc.pst.platinum.executive.dc.strategy.loader.StrategyLoader;
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
	protected Strategy dcs;					// DC strategy manager
	
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
		try
		{
			// strategy loader
	//		StrategyLoader loader = new StrategyLoader(plan2tiga, verifytga, pathPlan, horizon);
			
			// load strategy manager
			StrategyLoader loader = new StrategyLoader("", pdb.getHorizon());
			// read computed strategy
			loader.readStrategy();
			
			// get strategy
			this.dcs = loader.getStrategy();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
