package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceProduction extends FlawSolution 
{
	private List<Decision> before;			// list of decisions before production
	private List<Decision> after;			// list of decisions after production
	
	/**
	 * 
	 * @param flaw
	 */
	protected ResourceProduction(ResourceProfileFlaw flaw) {
		super(flaw);
		this.before = new ArrayList<>();
		this.after = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addBeforeDecision(Decision dec) {
		this.before.add(dec);
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addAfterDecision(Decision dec) {
		this.after.add(dec);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getBeforeDecisions() {
		return new ArrayList<>(this.before);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getAfterDecisions() {
		return new ArrayList<>(this.after);
	}

	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("scheduling-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
}
