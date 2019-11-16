package it.istc.pst.platinum.testing.icaps20;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dispatcher.ConditionCheckingDispatcher;
import it.istc.pst.platinum.executive.monitor.ConditionCheckingMonitor;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;


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