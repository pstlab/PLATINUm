package it.istc.pst.platinum.framework.microkernel;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ApplicationFrameworkObject 
{
	@SuppressWarnings("unused")
	private ApplicationFrameworkContainer container;
	private String registryKey;
	
	/**
	 * 
	 */
	protected ApplicationFrameworkObject() {
		// get a reference to the application container
		this.container = ApplicationFrameworkContainer.getInstance();
	}
	
	/**
	 * 
	 * @param registryKey
	 */
	protected void setRegistryKey(String registryKey) {
		this.registryKey = registryKey;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRegistryKey() {
		return registryKey;
	}
}
