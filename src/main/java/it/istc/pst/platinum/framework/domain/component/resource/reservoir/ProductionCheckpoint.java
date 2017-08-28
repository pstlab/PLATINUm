package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionCheckpoint implements Comparable<ProductionCheckpoint> 
{
	private ConsumptionResourceEvent lastConsumption; 	// last consumption before production
	private ProductionResourceEvent production;			// production event
	private double potential;								// potential "energy" available before the production event
	
	/**
	 * 
	 * @param event
	 * @param potential
	 * @param last
	 */
	protected ProductionCheckpoint(ProductionResourceEvent event, double potential, ConsumptionResourceEvent last) {
		this.production = event;
		this.potential = potential;
		this.lastConsumption = last;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConsumptionResourceEvent getLastConsumption() {
		return lastConsumption;
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
		// compare potential energies
		return this.potential >= o.potential ? -1 : 1;
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
