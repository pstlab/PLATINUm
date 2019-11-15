package it.istc.pst.platinum.testing.icaps20;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.dc.DCDispatcher;
import it.istc.pst.platinum.executive.dc.DCMonitor;
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
		level = FrameworkLoggingLevel.DEBUG
)
@MonitorConfiguration(
		monitor = DCMonitor.class
)
@DispatcherConfiguration(
		dispatcher = DCDispatcher.class
)
public class ICAPS20DCExecutive extends Executive 
{
	/**
	 * 
	 */
	protected ICAPS20DCExecutive() {
		super();
	}
}	