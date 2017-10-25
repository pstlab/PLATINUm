package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
public final class PrimitiveStateVariable extends StateVariable
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// planning resolver
			ResolverType.PLAN_REFINEMENT,
			// scheduling resolver
			ResolverType.TIMELINE_SCHEDULING_RESOLVER,
			// time-line gap resolver
			ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER,
	})
	protected PrimitiveStateVariable(String name) {
		super(name, DomainComponentType.SV_PRIMITIVE);
	}
}
