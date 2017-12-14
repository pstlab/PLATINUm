package it.istc.pst.platinum.framework.utils.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author anacleto
 *
 */
public class FrameworkLogger
{
	private Logger logger;		// the logger
	
	/**
	 * 
	 * @param lev
	 */
	protected FrameworkLogger(FrameworkLoggingLevel lev) {
		super();
		// create logger
		this.logger = Logger.getLogger(this.getClass().getName());
				
		// create logger default handler (console handler)
		ConsoleHandler console = new ConsoleHandler();
		console.setLevel(lev.getLevel());
		// do not use parent handler
		this.logger.setUseParentHandlers(false);
		// add default handler
		this.logger.addHandler(console);
		// set logger level
		this.logger.setLevel(lev.getLevel());
	}
	
	/**
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void setLogFile(String filePath) 
			throws IOException 
	{
		// clean logger handlers
		Handler[] handlers = this.logger.getHandlers();
		for (Handler handler : handlers) {
			// remove handler
			this.logger.removeHandler(handler);
		}
		
		// create file handler
		FileHandler fileHandler = new FileHandler(filePath);
		// set output level
		fileHandler.setLevel(this.logger.getLevel());
		this.logger.addHandler(fileHandler);
	}
	
	/**
	 * 
	 * @return
	 */
	public void info(String msg) {
		this.logger.log(Level.INFO, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void info(Object obj) {
		this.logger.log(Level.INFO, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void debug(String msg) {
		this.logger.log(Level.FINE, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void debug(Object obj) {
		this.logger.log(Level.FINE, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void warning(String msg) {
		this.logger.log(Level.WARNING, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void warning(Object obj) {
		this.logger.log(Level.WARNING, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void error(String msg) {
		this.logger.log(Level.SEVERE, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void error(Object obj) {
		this.logger.log(Level.SEVERE, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	private String buildMsg(String msg) {
		return "\n----------------------------------------------------\n"
				+ "" + msg + ""
				+ "\n----------------------------------------------------\n";
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String buildMsg(Object obj) {
		return "\n----------------------------------------------------\n"
				+ "" + obj.toString() + ""
				+ "\n----------------------------------------------------\n";
	}
}
