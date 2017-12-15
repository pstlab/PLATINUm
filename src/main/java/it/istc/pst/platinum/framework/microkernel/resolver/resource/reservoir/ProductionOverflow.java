package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ProductionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionOverflow extends ProductionFlaw 
{
	/**
	 * 
	 * @param id
	 * @param resource
	 * @param event
	 * @param delta
	 */
	protected ProductionOverflow(int id, ReservoirResource resource, ProductionResourceEvent event, double delta) {
		super(id, resource, event, delta);
	}
}
