package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionPlanning extends FlawSolution implements ResourceOverConsumptionSolution
{
	private int amount;								// amount of resource to produce
	private List<Decision> beforeProduction;			// list of decisions before production
	private List<Decision> afterProduction;				// list of decisions after production
	
	/**
	 * 
	 * @param flaw
	 * @param amount
	 * @param bp
	 * @param ap
	 */
	protected ProductionPlanning(Peak flaw, int amount, List<Decision> bp, List<Decision> ap) {
		super(flaw);
		this.amount = amount;
		this.beforeProduction = new ArrayList<>(bp);
		this.afterProduction = new ArrayList<>(ap);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisionsBeforeProduction() {
		return new ArrayList<>(this.beforeProduction);
	}
	
	/**
	 * 
	 */
	@Override
	public ResourceOverConsumptionSolutionType getType() {
		return ResourceOverConsumptionSolutionType.PRODUCTION_PLANNING;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisionsAfterProduction() {
		return new ArrayList<>(this.afterProduction);
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
	 */
	@Override
	public String toString() { 
		return "[ProductionPlanning amount= " + this.amount + " ]";
	}
}
