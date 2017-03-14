package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class PrecedenceConstraintPosting extends FlawSolution 
{
	private List<Decision> precedence;
	
	/**
	 * 
	 * @param flaw
	 * @param precedences
	 */
	protected PrecedenceConstraintPosting(ResourceProfileFlaw flaw, List<Decision> precedences) {
		super(flaw);
		this.precedence = new ArrayList<>(precedences);
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
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getPrecedences() {
		return new ArrayList<>(this.precedence);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PrecedenceConstraintPosting\n- constraint= " + this.precedence + "\n]";
	}
}
