package it.cnr.istc.pst.platinum.ai.framework.utils.log;

import java.io.IOException;
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
	private static Logger logger; 		// the logger
	
	/**
	 * 
	 * @param lev
	 */
	protected FrameworkLogger(FrameworkLoggingLevel lev) {
		if (logger == null) {
			// set logger
			logger = Logger.getLogger("FrameworkLogger");
			// set logger level
			logger.setLevel(lev.getLevel());
		}
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
		Handler[] handlers = logger.getHandlers();
		for (Handler handler : handlers) {
			// remove handler
			logger.removeHandler(handler);
		}
		
		// create file handler
		FileHandler fileHandler = new FileHandler(filePath);
		// set output level
		fileHandler.setLevel(logger.getLevel());
		logger.addHandler(fileHandler);
	}
	
	/**
	 * 
	 * @return
	 */
	public void info(String msg) {
		logger.log(Level.INFO, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void info(Object obj) {
		logger.log(Level.INFO, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void debug(String msg) {
		logger.log(Level.FINE, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void debug(Object obj) {
		logger.log(Level.FINE, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void warning(String msg) {
		logger.log(Level.WARNING, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void warning(Object obj) {
		logger.log(Level.WARNING, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void error(String msg) {
		logger.log(Level.SEVERE, this.buildMsg(msg));
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void error(Object obj) {
		logger.log(Level.SEVERE, this.buildMsg(obj));
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	private String buildMsg(String msg) {
		return "\n>>>> START\n"
				+ "" + msg + ""
				+ "\nEND <<<<\n";
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String buildMsg(Object obj) {
		return "\n>>>> START\n"
				+ "" + obj.toString() + ""
				+ "\nEND <<<<\n";
	}
}
