package it.uniroma3.epsl2.framework.utils.log;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class FrameworkLoggerFactory extends ApplicationFrameworkFactory {

	/**
	 * 
	 */
	public FrameworkLoggerFactory() {
		super();
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public FrameworkLogger createPlannerLogger(FrameworkLoggingLevel level) {
		// create logger
		FrameworkLogger logger = new FrameworkLogger(level);
		// register instance
		this.register(SINGLETON_PLANNER_LOGGER_REFERENCE, logger);
		return logger;
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public FrameworkLogger createFrameworkLogger(FrameworkLoggingLevel level) {
		// create logger
		FrameworkLogger logger = new FrameworkLogger(level);
		// register instance
		this.register(SINGLETON_FRAMEWORK_LOGGER_REFERENCE, logger);
		return logger;
	}
}
