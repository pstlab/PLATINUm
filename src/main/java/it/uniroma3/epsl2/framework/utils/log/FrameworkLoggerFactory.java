package it.uniroma3.epsl2.framework.utils.log;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class FrameworkLoggerFactory extends ApplicationFrameworkFactory 
{
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
	public FrameworkLogger createDeliberativeLogger(FrameworkLoggingLevel level) {
		// create logger
		FrameworkLogger logger = new FrameworkLogger(level);
		// register instance
		this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER, logger);
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
		this.register(ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER, logger);
		return logger;
	}
}
