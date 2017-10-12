package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ProductionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ProductionFlaw extends Flaw 
{
	protected ProductionResourceEvent production;			// production event
	protected double delta;
	
	/**
	 * 
	 * @param resource
	 * @param production
	 * @param delta
	 */
	protected ProductionFlaw(ReservoirResource resource, ProductionResourceEvent production, double delta) {
		super(resource, FlawType.RESOURCE_PRODUCTION_UPDATE);
		this.production = production;
		this.delta = delta;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getProduction() {
		return this.production.getDecision();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getProducedAmount() {
		return production.getAmount();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getDelta() {
		return delta;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ProductionFlaw produced= " + this.production.getAmount() + " delta= " + delta + "]";
	}
}
