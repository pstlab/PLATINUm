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
	private double potential;							// potential "energy" available before the production event
	private long schedule;								// sampling time of the checkpoint
	
	/**
	 * 
	 * @param event
	 * @param potential
	 * @param schedule
	 */
	protected ProductionCheckpoint(ProductionResourceEvent event, double potential, long schedule) {
		this.production = event;
		this.potential = potential;
		this.schedule = schedule;
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
	public double getPotential() {
		return potential;
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
