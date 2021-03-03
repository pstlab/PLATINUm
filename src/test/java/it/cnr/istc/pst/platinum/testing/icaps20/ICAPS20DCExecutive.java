package it.cnr.istc.pst.platinum.testing.icaps20;

import it.cnr.istc.pst.platinum.executive.dc.DCDispatcher;
import it.cnr.istc.pst.platinum.executive.dc.DCExecutive;
import it.cnr.istc.pst.platinum.executive.dc.DCMonitor;
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
		level = FrameworkLoggingLevel.DEBUG
)
@MonitorConfiguration(
		monitor = DCMonitor.class
)
@DispatcherConfiguration(
		dispatcher = DCDispatcher.class
)
public class ICAPS20DCExecutive extends DCExecutive 
{
	/**
	 * 
	 */
	protected ICAPS20DCExecutive() {
		super();
	}
}	