package it.istc.pst.platinum.executive;

/**
 * 
 * @author anacleto
 *
 */
public interface ExecutivePlatformProxy 
{
	/**
	 * This method registers an observer to a particular (physical) platform.
	 * 
	 *  The interactions between the executive and the physical platform are 
	 *  managed through a dedicated PROXY which must implement this interface
	 * 
	 * @param executive
	 */
	public void register(ExecutivePlatformObserver observer);
	
	/**
	 * Unregister a (previously) registered observer to a particular (physical) platform.
	 * 
	 * 
	 * @param observer
	 */
	public void unregister(ExecutivePlatformObserver observer);
	
	/**
	 * This method sends commands to the real platform. 
	 * 
	 * Commands are marshaled according to the following protocol
	 * 
	 * 		command#param0#param1#...#paramN
	 * 
	 * The method must return an unique ID of the command sent to the platform, and it must be 
	 * implemented as a non-blocking call. See other methods for blocking calls if they are 
	 * strictly needed
	 * 
	 * @param cmd
	 * @return
	 */
	public String sendCommand(String cmd);
}
