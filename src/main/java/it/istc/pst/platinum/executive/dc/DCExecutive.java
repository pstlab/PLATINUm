package it.istc.pst.platinum.executive.dc;

import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.istc.pst.platinum.executive.dc.strategy.loader.StrategyLoader;
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
		monitor = DCMonitor.class
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
	protected void doPrepareExecution(Goal goal) 
	{
		try
		{
			// export plan to a file according to the expected encoding
			String path = this.pdb.export();
			// load strategy manager
			StrategyLoader loader = new StrategyLoader(path, this.pdb.getHorizon());
			// read computed strategy
			loader.readStrategy();
			//EDIT: retrieve time
			goal.setUppaalTime(loader.getTigaTime());
			goal.setManagementStrategyTime(loader.getManagementStrategyTime());
			
			// get strategy
			this.checker = loader.getStrategy();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
