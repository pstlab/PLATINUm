package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
public class FunctionalStateVariable extends StateVariable 
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// planning resolver
			ResolverType.PLAN_REFINEMENT
	})
	protected FunctionalStateVariable(String name) {
		super(name, DomainComponentType.SV_FUNCTIONAL);
	}
}
