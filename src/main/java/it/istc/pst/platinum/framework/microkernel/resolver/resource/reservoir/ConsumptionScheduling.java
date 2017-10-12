package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import java.util.HashMap;
import java.util.Map;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class ConsumptionScheduling extends FlawSolution implements ResourceOverConsumptionSolution 
{
	private Map<Decision, Decision> constraints;	// list of precedence constraints that solve the peak
	private double preserved;						// average value of the resulting preserved space
	private Decision production;					// considered production decision
	private double productionAmount;				// amount of resource to produce
	private double oldAmount;						// old amount of produced resourc
	
	/**
	 * 
	 * @param flaw
	 * @param production
	 * @param old
	 * @param amount
	 * @param constraints
	 * @param preserved
	 */
	protected ConsumptionScheduling(Peak flaw, Decision production, double old, double amount, Map<Decision, Decision> constraints, double preserved) {
		super(flaw);
		this.constraints = new HashMap<>(constraints);
		this.preserved = preserved;
		this.production = production;
		this.productionAmount = amount;
		this.oldAmount = old;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ResourceOverConsumptionSolutionType getType() {
		return ResourceOverConsumptionSolutionType.CONSUMPTION_SCHEDULING;
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
	public double getPreserved() {
		return preserved;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getOldAmount() {
		return oldAmount;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getProductionAmount() {
		return productionAmount;
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
	public Map<Decision, Decision> getPrecedenceConstraints() {
		return new HashMap<>(this.constraints);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ConsumptionScheduling preserved: " + this.preserved + " constraints: " + this.constraints + "]\n";
	}
}
