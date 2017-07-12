package it.istc.pst.platinum.framework.microkernel.resolver.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SchedulingResolver extends Resolver 
{
	/**
	 * 
	 * @param label
	 * @param flawType
	 */
	protected SchedulingResolver(String label, FlawType flawType) {
		super(label, flawType);
	}
	
	/**
	 * 
	 * @param decisions
	 * @return
	 */
	protected List<List<Decision>> schedule(Collection<Decision> decisions) 
	{
		// initialize permutations
		List<List<Decision>> result = new ArrayList<>();
		// initialize tail
		List<Decision> tail = new ArrayList<Decision>(decisions);
		Collections.sort(tail);
		// compute permutations
		this.computePermutation(new ArrayList<Decision>(), tail, result);
		// get permutations
		return result;
	}
	
	/**
	 * 
	 * @param permutation
	 * @param tail
	 */
	protected void computePermutation(List<Decision> permutation, List<Decision> tail, List<List<Decision>> result)
	{
		// base step
		if (tail.isEmpty()) {
			// add complete permutation to results
			result.add(new ArrayList<>(permutation));
		}
		else 
		{
			// recursive step
			for (int index = 0; index < tail.size(); index++) 
			{
				// get decision 
				Decision dec = tail.remove(index);
				// add decision to permutation
				permutation.add(dec);
				// recursive call
				this.computePermutation(permutation, tail, result);
				// prepare data for next recursive call
				tail.add(index, dec);
				permutation.remove(dec);
			}
		}
	}
	
	/**
	 * 
	 * @param schedule
	 * @return
	 * @throws TemporalConstraintPropagationException
	 */
	protected double checkScheduleFeasibility(List<Decision>schedule) 
			throws TemporalConstraintPropagationException
	{
		// initialize the makespan
		double makespan = Double.MIN_VALUE + 1;
		// list of propagate precedence constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			for (int index = 0; index < schedule.size() - 1; index++) 
			{
				// get decisions
				Decision a = schedule.get(index);
				Decision b = schedule.get(index + 1);
				
				// create temporal constraint
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
				before.setReference(a.getToken().getInterval());
				before.setTarget(b.getToken().getInterval());
				// propagate temporal constraint
				this.tdb.propagate(before);
				// add committed constraint
				committed.add(before);
			}
			
			// check feasibility 
			this.tdb.checkConsistency();
			
			// feasible solution - compute the resulting makespan
			ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
			this.tdb.process(query);
			// get computed makespan
			makespan = query.getMakespan();
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// not feasible schedule
			this.logger.debug("Not feasible schedule constraint found\n- " + ex.getMessage() + "\n");
			// forward exception
			throw new TemporalConstraintPropagationException(ex.getMessage());
		}
		finally {
			// restore initial state
			for (TemporalConstraint cons : committed) {
				// remove constraint from network
				this.tdb.retract(cons);
			}
		}
		
		// get resulting makespan
		return makespan;
	}
}
