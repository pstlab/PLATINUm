package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.checking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.Resolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.InvalidBehaviorException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandro
 *
 */
public final class ObservationBehaviorCheckingResolver extends Resolver<StateVariable> implements Comparator<Decision> 
{
	/**
	 * 
	 */
	protected ObservationBehaviorCheckingResolver() {
		super(ResolverType.OBSERVATION_CHECKING_RESOLVER.getLabel(),
				ResolverType.OBSERVATION_CHECKING_RESOLVER.getFlawTypes());
	}
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException {
		// nothing to do
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) {
		// nothing to do
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws RelationPropagationException, DecisionPropagationException
	{
		// nothing to do
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException {
		// simply throw exception
		throw new InvalidBehaviorException("Incomplete behavior description of component " + this.component.getName() + ":\n" + flaw);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	protected List<Flaw> doFindFlaws() {
		
		// list of gaps
		List<Flaw> issues = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> decs = this.component.getActiveDecisions();
		// check decisions
		if (decs.isEmpty()) {
			// missing external variable observations
			issues.add(new MissingObservation(FLAW_COUNTER.getAndIncrement(), this.component));
			
		} else {
			
			// sort decisions
			Collections.sort(decs, this);
			// look for gaps
			for (int index = 0; index <= decs.size() - 2; index++) {
				
				// get two adjacent tokens
				Decision left = decs.get(index);
				Decision right = decs.get(index + 1);
				
				// check distance between related temporal intervals
				IntervalDistanceQuery query = this.tdb.
						createTemporalQuery(TemporalQueryType.INTERVAL_DISTANCE);
				
				// set intervals
				query.setReference(left.getToken().getInterval());
				query.setTarget(right.getToken().getInterval());
				// process query
				this.tdb.process(query);
				// get result
				long lb = query.getDistanceLowerBound();
				long ub = query.getDistanceUpperBound();
				
				// check distance bounds
				if (lb > 0 || (lb == 0 && ub > 0)) {
					
					// we've got a gap
					IncompleteBehavior issue = new IncompleteBehavior(FLAW_COUNTER.getAndIncrement(), this.component, left, right);
					// add the gap
					issues.add(issue);
				}
			}
		}
		
		// get detected gaps
		return issues;
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Decision d1, Decision d2) {
		// get start times
		TimePoint s1 = d1.getToken().getInterval().getStartTime();
		TimePoint s2 = d2.getToken().getInterval().getStartTime();
		return s1.getLowerBound() <= s2.getLowerBound() ? -1 : 1;
	}
}
