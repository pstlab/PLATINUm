package it.uniroma3.epsl2.framework.time;

import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.solver.TemporalSolverType;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetworkType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointDistanceConstraint;
import it.uniroma3.epsl2.framework.time.tn.lang.query.TimePointDistanceQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class UncertaintyTemporalFacade extends TemporalFacade 
{
	/**
	 * 
	 */
	@TemporalFacadeConfiguration(network= TemporalNetworkType.STNU, solver= TemporalSolverType.APSP)
	protected UncertaintyTemporalFacade() {
		super();
	}

	/**
	 * This method checks if the network is temporally consistent.
	 * 
	 *	The procedure relies on a relaxed representation of the STNU 
	 *  by considering contingent links as requirement links (i.e. controllable)
	 * 
	 * @return
	 */
	public boolean isConsistent() {
		// ask the reasoner for temporal consistency
		return this.solver.isConsistent();
	}
	
	/**
	 * This method checks if the STNU is pseudo-controllable. 
	 * 
	 *	This procedure checks if contingent links have been "squeezed"
	 *	so, the STNU is pseudo-controllable if no assumption has been 
	 *	made on the duration of uncontrollable constraints (i.e. contingent
	 *	links).
	 *
	 * @return
	 */
	public boolean isPseudoControllable() 
	{
		// hypothesis
		boolean pseudoControllable = true;
		// check contingent constraints
		for (TimePointDistanceConstraint c : this.tn.getConstraints()) 
		{
			// check controllability
			if (!c.isControllable()) {
				// get related time points
				TimePoint source = c.getReference();
				TimePoint target = c.getTarget();
				
				// create query
				TimePointDistanceQuery query = this.qf.create(TemporalQueryType.TP_DISTANCE);
				query.setSource(source);
				query.setTarget(target);
				// process query
				this.solver.process(query);

				// get actual bounds
				long dmin = query.getDistanceLowerBound();
				long dmax = query.getDistanceUpperBound();
				// check duration
				if (dmin < c.getDistanceLowerBound() && dmax < c.getDistanceUpperBound()) {
					// contingent link change
					pseudoControllable = false;
					break;
				}
			}
		}
		// get result
		return pseudoControllable;
	}
}
