package it.cnr.istc.pst.platinum.ai.framework.utils.log;

import java.util.logging.Level;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum FrameworkLoggingLevel 
{
	/**
	 * 
	 */
	DEBUG(Level.ALL, 4),
	
	/**
	 * 
	 */
	INFO(Level.INFO, 3),
	
	/**
	 * 
	 */
	WARNING(Level.WARNING, 2),
	
	/**
	 * 
	 */
	ERROR(Level.SEVERE, 1),
	
	/**
	 * 
	 */
	OFF(Level.OFF, 0);
	
	private Level lev;
	private int index;
	
	/**
	 * 
	 * @param lev
	 * @param index
	 */
	private FrameworkLoggingLevel(Level lev, int index) {
		this.lev = lev;
		this.index = index;
	}
	
	/**
	 * 
	 * @return
	 */
	public Level getLevel() {
		return lev;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public static FrameworkLoggingLevel getLogger(int index) {
		FrameworkLoggingLevel level = null;
		for (FrameworkLoggingLevel l : values()) {
			if (l.index == index) {
				level = l;
			}
		}
		
		// check if found
		if (level == null) {
			throw new RuntimeException("Logging level " + index + " does not exists");
		}
		
		// get logger
		return level;
	}
}
