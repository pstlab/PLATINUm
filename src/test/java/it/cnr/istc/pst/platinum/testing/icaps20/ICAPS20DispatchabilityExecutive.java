package it.cnr.istc.pst.platinum.testing.icaps20;

import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.ai.executive.dispatcher.ConditionCheckingDispatcher;
import it.cnr.istc.pst.platinum.ai.executive.monitor.ConditionCheckingMonitor;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.utils.log.FrameworkLoggingLevel;


/**
 * 
 * @author alessandroumbrico
 *
 */
@FrameworkLoggerConfiguration(
		level = FrameworkLoggingLevel.OFF
)
@MonitorConfiguration(
		monitor = ConditionCheckingMonitor.class
)
@DispatcherConfiguration(
		dispatcher = ConditionCheckingDispatcher.class
)
public class ICAPS20DispatchabilityExecutive extends Executive 
{
	/**
	 * 
	 */
	protected ICAPS20DispatchabilityExecutive() {
		super();
	}
}	