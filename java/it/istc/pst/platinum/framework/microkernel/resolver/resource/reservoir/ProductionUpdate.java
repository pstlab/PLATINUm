package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionUpdate extends FlawSolution 
{
	private Decision production;
	private double previousAmount;
	private double amount;
	
	/**
	 * 
	 * @param flaw
	 * @param amount
	 */
	protected ProductionUpdate(ProductionFlaw flaw, double amount) {
		super(flaw);
		this.previousAmount = flaw.getProducedAmount();
		this.amount = amount;
		this.production = flaw.getProduction();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPreviousAmount() {
		return previousAmount;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getProduction() {
		return production;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("unification-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ProductionUpdate new-amount: " + this.amount + " old-amount: " + this.previousAmount + " production: " + this.production  + "]";
	}
}
