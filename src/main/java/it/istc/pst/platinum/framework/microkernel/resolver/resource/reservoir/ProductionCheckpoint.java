package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ProductionResourceEvent;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionCheckpoint implements Comparable<ProductionCheckpoint> 
{
	private ProductionResourceEvent production;			// production event
	private double levelBeforeProduction;				// the resource level before production
	private double levelAfterProduction;				// the resource level after production
	private long schedule;								// sampling time of the checkpoint
	private double potentialConsumption;				// resource potential consumption before production
	private double potentialProduction;					// resource potential production after production
	
	/**
	 * 
	 * @param event
	 * @param schedule
	 * @param levelBefore
	 * @param levelAfter
	 * @param potentialConsumption
	 * @param potentialProduction
	 */
	protected ProductionCheckpoint(ProductionResourceEvent event, long schedule, double levelBefore, double levelAfter, double potentialConsumption, double potentialProduction) {
		this.production = event;
		this.schedule = schedule;
		this.levelBeforeProduction = levelBefore;
		this.levelAfterProduction = levelAfter;
		this.potentialConsumption = potentialConsumption;
		this.potentialProduction = potentialProduction;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSchedule() {
		return schedule;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getLevelBeforeProduction() {
		return this.levelBeforeProduction;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getLevelAfterProduction() {
		return levelAfterProduction;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProductionResourceEvent getProduction() {
		return production;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPotentialCapacity() {
		return potentialConsumption;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPotentialProduction() {
		return potentialProduction;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ProductionCheckpoint o) {
		// compare checkpoint schedules
		return this.schedule <= o.schedule ? -1 : 1;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((production == null) ? 0 : production.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductionCheckpoint other = (ProductionCheckpoint) obj;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}
}
