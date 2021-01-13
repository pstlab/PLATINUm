package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;

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
