package it.istc.pst.platinum.framework.domain.component.resource;

/**
 * 
 * @author anacleto
 *
 */
public interface ResourceProfileManager
{
	/**
	 * 
	 * @return
	 */
	public long getMinCapacity();
	
	/**
	 * 
	 * @return
	 */
	public long getMaxCapacity();
	
	/**
	 * 
	 * @return
	 */
	public long getInitialCapacity();
}
