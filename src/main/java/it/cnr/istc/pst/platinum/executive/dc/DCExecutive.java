package it.cnr.istc.pst.platinum.executive.dc;

import it.cnr.istc.pst.platinum.control.lang.Goal;
import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.cnr.istc.pst.platinum.executive.dc.strategy.loader.StrategyLoader;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 */
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.INFO
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
			throws Exception
	{
		// export plan to a file according to the expected encoding
		String path = this.pdb.export();
		// load strategy manager
		System.out.println("Starting strategy loader...\n");
		StrategyLoader loader = new StrategyLoader(path, this.pdb.getHorizon());
		// read computed strategy
		loader.readStrategy();
		//EDIT: retrieve time
		goal.setUppaalTime(loader.getTigaTime());
		goal.setExistsStrategy(loader.getExistsStrategy());
		goal.setManagementStrategyTime(loader.getManagementStrategyTime());
		goal.setOutOfBounds(loader.getStrategy().isOutOfBounds());
		goal.setMaxOutOfBounds(loader.getStrategy().getStrikeMaxReached());
		
		// get strategy
		this.checker = loader.getStrategy();
	}
}
