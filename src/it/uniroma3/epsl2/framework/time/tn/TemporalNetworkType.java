package it.uniroma3.epsl2.framework.time.tn;

import it.uniroma3.epsl2.framework.time.tn.stn.SimpleTemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.stnu.SimpleTemporalNetworkWithUncertainty;

/**
 * 
 * @author anacleto
 *
 */
public enum TemporalNetworkType {

	/**
	 * 
	 */
	STN(SimpleTemporalNetwork.class.getName()),
	
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
	public String getNetworkClassName() {
		return this.name;
	}
}
