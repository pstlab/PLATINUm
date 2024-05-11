package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author alessandro
 *
 */
public final class ExternalStateVariable extends StateVariable 
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// planning resolver is needed also for external variables
			ResolverType.PLAN_REFINEMENT,
			// observation checking resolver
			ResolverType.OBSERVATION_CHECKING_RESOLVER,
	})
	protected ExternalStateVariable(String name) {
		super(name, DomainComponentType.SV_EXTERNAL);
	}

	/**
	 * 
	 */
	@Override
	public boolean isExternal() {
		// external component
		return true;
	}

	/**
	 * 
	 */
	@Override
	public StateVariableValue addStateVariableValue(String label, long[] duration, boolean controllable) {
		// force values of external variable to be not controllable
		return super.addStateVariableValue(label, duration, controllable);
	}
}
