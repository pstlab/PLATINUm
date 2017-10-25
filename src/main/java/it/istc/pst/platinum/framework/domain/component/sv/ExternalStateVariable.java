package it.istc.pst.platinum.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.time.lang.query.IntervalPseudoControllabilityQuery;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
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
		return super.addStateVariableValue(label, duration, false);
	}
	
	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException 
	{
		// list of squeezed values
		List<Decision> issues = new ArrayList<>();
		// check every value of the state variable
		for (Decision dec : this.getActiveDecisions()) {
			// create query
			IntervalPseudoControllabilityQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.INTERVAL_PSEUDO_CONTROLLABILITY);
			
			// set related temporal interval
			query.setInterval(dec.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// check
			if (!query.isPseudoControllable()) {
				// add issue
				issues.add(dec);
			}
		}
		
		// check issues
		if (!issues.isEmpty()) {
			// create exception
			PseudoControllabilityCheckException ex = new PseudoControllabilityCheckException("Controllability issues found on component " + this.name);
			// add issues
			for (Decision issue : issues) {
				ex.addIssue(issue);
			}
			// throw exception
			throw ex;
		}
	}
}
