package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.InvalidBehaviorException;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class BehaviorCheckingResolver <T extends StateVariable> extends Resolver implements Comparator<Decision> 
{
	@ComponentReference
	protected T component;
	
	/**
	 * 
	 */
	protected BehaviorCheckingResolver() {
		super(ResolverType.BEHAVIOR_CHECKING_RESOLVER);
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
	
	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of gaps
		List<Flaw> issues = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> decs = this.component.getActiveDecisions();
		
		// sort decisions
		Collections.sort(decs, this);
		
		// look for gaps
		for (int index = 0; index <= decs.size() - 2; index++) {
			// get two adjacent tokens
			Decision left = decs.get(index);
			Decision right = decs.get(index + 1);
			
			// check distance between related temporal intervals
			CheckIntervalDistanceQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.CHECK_INTERVAL_DISTANCE);
			
			// set intervals
			query.setSource(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// get result
			long lb = query.getDistanceLowerBound();
			long ub = query.getDistanceUpperBound();
			// check distance bounds
			if (lb == 0 && ub == 0) {
				// get values
				ComponentValue vi = left.getValue();
				ComponentValue vj = right.getValue();
				// get direct successors of value vi
				List<ComponentValue> successors = this.component.getDirectSuccessors(vi);
				if (!successors.contains(vj)) {
					// inconsistent behavior
					InvalidTransition issue = new InvalidTransition(this.component, left, right);
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
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException {
		// simply throw exception
		throw new InvalidBehaviorException("Inconsistent temporal behavior of component " + this.component.getName() + ":\n" + flaw);
	}
}