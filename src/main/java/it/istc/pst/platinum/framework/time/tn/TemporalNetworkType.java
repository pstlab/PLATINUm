package it.istc.pst.platinum.framework.time.tn;

/**
 * 
 * @author anacleto
 *
 */
public enum TemporalNetworkType 
{
	/**
	 * 
	 */
	STNU(SimpleTemporalNetworkWithUncertainty.class.getName());
	
	private String name;
	
	private TemporalNetworkType(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return this.name;
	}
}
