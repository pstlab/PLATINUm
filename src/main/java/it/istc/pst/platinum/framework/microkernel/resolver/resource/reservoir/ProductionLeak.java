package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ProductionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionLeak extends ProductionFlaw 
{
	/**
	 * 
	 * @param id
	 * @param resource
	 * @param event
	 * @param delta
	 */
	protected ProductionLeak(int id, ReservoirResource resource, ProductionResourceEvent event, double delta) {
		super(id, resource, event, delta);
	}
}
