package it.istc.pst.platinum.executive.dc;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.istc.pst.platinum.executive.dc.strategy.loader.StrategyLoader;
import it.istc.pst.platinum.executive.monitor.ConditionCheckingMonitor;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.OFF
)
@MonitorConfiguration(
		monitor = ConditionCheckingMonitor.class
)
@DispatcherConfiguration(
		dispatcher = DCDispatcher.class
)
public class DCExecutive extends Executive 
{
	protected Strategy checker;					// DC strategy manager
	
	/**
	 * 
	 */
	protected DCExecutive() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	protected void doPrepareExecution() 
	{
		try
		{
			// strategy loader
	//		StrategyLoader loader = new StrategyLoader(plan2tiga, verifytga, pathPlan, horizon);
			
			// export plan to a file according to the expected encoding
			String path = this.pdb.export();
			// load strategy manager
			StrategyLoader loader = new StrategyLoader(path, this.pdb.getHorizon());
			// read computed strategy
			loader.readStrategy();
			
			// get strategy
			this.checker = loader.getStrategy();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
