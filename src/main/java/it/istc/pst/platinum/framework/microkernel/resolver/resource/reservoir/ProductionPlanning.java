package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionPlanning extends FlawSolution implements ResourceOverConsumptionSolution
{
	private double amount;							// amount of resource to produce
	private List<Decision> beforeProduction;		// list of decisions before production
	private List<Decision> afterProduction;			// list of decisions after production
	
	/**
	 * 
	 * @param flaw
	 * @param amount
	 * @param bp
	 * @param ap
	 */
	protected ProductionPlanning(Peak flaw, double amount, Collection<Decision> bp, Collection<Decision> ap, double cost) {
		super(flaw, cost);
		this.amount = amount;
		this.beforeProduction = new ArrayList<>(bp);
		this.afterProduction = new ArrayList<>(ap);
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
	public String toString() { 
		return "{ \"type\": \"PRODUCTION_PLANNING\", "
				+ "\"amount\": " + this.amount + " }";
	}
}
