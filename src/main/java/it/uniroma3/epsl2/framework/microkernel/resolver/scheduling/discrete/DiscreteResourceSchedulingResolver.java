package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.discrete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.ResourceProfileManager;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.BeforeRelation;
import it.uniroma3.epsl2.framework.lang.plan.resource.ProfileSample;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceProfile;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.ResourceProfileComputationException;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.ex.TemporalConstraintPropagationException;
import it.uniroma3.epsl2.framework.time.lang.FixTimePointConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalScheduleQuery;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver <T extends DomainComponent & ResourceProfileManager> extends Resolver 
{ 
	@ComponentReference
	protected T component;

	/**
	 * 
	 */
	protected DiscreteResourceSchedulingResolver() {
		super(ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER);
	}

	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		try
		{
			// get pessimistic resource profile 
			List<ResourceEvent> events = this.component.getConsumptions();
			// get optimistic resource profile
			events.addAll(this.component.getProductions());
			
			// get optimistic resource profile
			ResourceProfile orp = this.computeOptimisticResourceProfile(events);
			// compute flaws
			flaws.addAll(this.computeProfilePeaks(orp));
			
			// check results
			if (flaws.isEmpty()) {
				// get pessimistic resource profile
				ResourceProfile prp = this.computePessimisticResourceProfile(events);
				// compute flaws
				flaws.addAll(this.computeProfilePeaks(prp));
			}
		}
		catch (ResourceProfileComputationException ex) {
			// profile computation error
			this.logger.error(ex.getMessage());
		}
		
		// get computed flaws
		return flaws;
	}

	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// cast flaw
		ResourceProfileFlaw profileFlaw = (ResourceProfileFlaw) flaw;
		// compute all possible orderings of the tokens
		List<List<Decision>> orderings = this.schedule(profileFlaw.getPeak());
		// compute solutions
		for (List<Decision> ordering : orderings) 
		{
			// add a possible 
			FlawSolution solution = new PrecedenceConstraintPosting(profileFlaw, ordering);
			profileFlaw.addSolution(solution);
		}
		
		// check available solutions
		if (!profileFlaw.isSolvable()) {
			// throw exception
			throw new UnsolvableFlawFoundException("Unsolvable Peak found on state variable " + this.component.getName() + ":\n" + flaw);
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get the flaw solution to consider
		PrecedenceConstraintPosting pcp = (PrecedenceConstraintPosting) solution;
		// prepare relations
		Set<Relation> relations = new HashSet<>();
		// setup relations
		for (int index = 0; index <= pcp.getPrecedences().size() - 2; index++) 
		{
			// get adjacent decisions
			Decision reference = pcp.getPrecedences().get(index);
			Decision target = pcp.getPrecedences().get(index + 1);
			
			// create relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, reference, target);
			// set bounds
			rel.setBound(new long[] {1, this.component.getHorizon()});
			// add reference, target and constraint
			relations.add(rel);
			
			this.logger.debug("Applying flaw solution\n- " + solution + "\nthrough before constraint " + rel);
		}
		
		try 
		{
			// propagate relations
			this.component.add(relations);
			// set added relations
			solution.addAddedRelations(relations);
		}
		catch (RelationPropagationException ex) {
			// free all relations
			for (Relation rel : relations) {
				this.component.free(rel);
			}
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// manage added relations
		for (Relation rel : solution.getAddedRelations()) {
			// completely delete relation 
			this.component.free(rel);
		}
		
		// manage activated relations
		for (Relation rel : solution.getActivatedRelations()) {
			// deactivate relation
			this.component.delete(rel);
		}
		
		
		// manage created relations
		for (Relation rel : solution.getCreatedRelations()) {
			// delete pending relation
			this.component.delete(rel);
		}
		
		// delete activated decisions
		for (Decision dec : solution.getActivatedDecisisons()) {
			// deactivate decision
			this.component.delete(dec);
		}
		
		// delete pending decisions
		for (Decision dec : solution.getCreatedDecisions()) {
			// delete pending decisions
			this.component.delete(dec);
		}
		
	}
	
	/**
	 * 
	 * @param events
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	private ResourceProfile computeOptimisticResourceProfile(List<ResourceEvent> events) 
			throws ResourceProfileComputationException
	{
		// initialize resource profile
		ResourceProfile orp = new ResourceProfile();
		// list of constraint to retract 
		List<TemporalConstraint> toRetract = new ArrayList<>();
		try
		{
			// check resource events
			for (ResourceEvent event : events) 
			{
				// check decision schedule
				IntervalScheduleQuery query = this.tdb.createTemporalQuery(
						TemporalQueryType.INTERVAL_SCHEDULE);
				// set interval
				query.setInterval(event.getDecision().getToken().getInterval());
				// process query
				this.tdb.process(query);
				
				// check event and set the optimistic schedule
				switch (event.getType())
				{
					// schedule consumption as late as possible
					case CONSUMPTION : 
					{
						// get time point to schedule
						TimePoint point = event.getEvent();
						// prepare constraint
						FixTimePointConstraint cons = this.tdb.createTemporalConstraint(
								TemporalConstraintType.FIX_TIME_POINT);
						// set point 
						cons.setReference(point);
						// set time
						cons.setTime(point.getUpperBound());
						// propagate constraint
						this.tdb.propagate(cons);
						
						// add sample
						orp.addSample(event, point.getUpperBound());
						
						// add constraint
						toRetract.add(cons);
					}
					break;
					
					// schedule production as soon as possible
					case PRODUCTION : 
					{
						// get time point to schedule
						TimePoint point = event.getEvent();
						// prepare constraint
						FixTimePointConstraint cons = this.tdb.createTemporalConstraint(
								TemporalConstraintType.FIX_TIME_POINT);
						// set point
						cons.setReference(point);
						// set time
						cons.setTime(point.getLowerBound());
						// propagate constraint
						this.tdb.propagate(cons);
						
						// add sample
						orp.addSample(event, point.getLowerBound());
						
						// add constraint
						toRetract.add(cons);
					}
					break;
				}
			}
		}
		catch (TemporalConstraintPropagationException ex) {
			// profile computation error
			throw new ResourceProfileComputationException(ex.getMessage());
		}
		finally 
		{
			// retract propagated constraints
			for (TemporalConstraint constraint : toRetract) {
				// retract constraints propagated for profile computation
				this.tdb.retract(constraint);;
			}
		}
		
		// get profile
		return orp;
	}
	
	/**
	 * 
	 * @param events
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	private ResourceProfile computePessimisticResourceProfile(List<ResourceEvent> events) 
			throws ResourceProfileComputationException
	{
		// create resource profile
		ResourceProfile prp = new ResourceProfile();
		// constraints to retract
		List<TemporalConstraint> toRetract = new ArrayList<>();
		try
		{
			// check resource events
			for (ResourceEvent event : events) 
			{
				// check decision schedule
				IntervalScheduleQuery query = this.tdb.createTemporalQuery(
						TemporalQueryType.INTERVAL_SCHEDULE);
				// set interval
				query.setInterval(event.getDecision().getToken().getInterval());
				// process query
				this.tdb.process(query);
				
				// check event and set the optimistic schedule
				switch (event.getType())
				{
					// schedule consumption as late as possible
					case CONSUMPTION : 
					{
						// get time point to schedule
						TimePoint point = event.getEvent();
						// prepare constraint
						FixTimePointConstraint cons = this.tdb.createTemporalConstraint(
								TemporalConstraintType.FIX_TIME_POINT);
						// set point 
						cons.setReference(point);
						// set time
						cons.setTime(point.getLowerBound());
						// propagate constraint
						this.tdb.propagate(cons);
						
						// add sample
						prp.addSample(event, point.getLowerBound());
						
						// add constraint
						toRetract.add(cons);
					}
					break;
					
					// schedule production as soon as possible
					case PRODUCTION : 
					{
						// get time point to schedule
						TimePoint point = event.getEvent();
						// prepare constraint
						FixTimePointConstraint cons = this.tdb.createTemporalConstraint(
								TemporalConstraintType.FIX_TIME_POINT);
						// set point
						cons.setReference(point);
						// set time
						cons.setTime(point.getUpperBound());
						// propagate constraint
						this.tdb.propagate(cons);
						
						// add sample
						prp.addSample(event, point.getUpperBound());
						
						// add constraint
						toRetract.add(cons);
					}
					break;
				}
			}
		}
		catch (TemporalConstraintPropagationException ex) {
			// profile computation error
			throw new ResourceProfileComputationException(ex.getMessage());
		}
		finally 
		{
			// retract propagated constraints
			for (TemporalConstraint constraint : toRetract) {
				// retract constraints propagated for profile computation
				this.tdb.retract(constraint);;
			}
		}
		// get profile
		return prp;
	}
	
	/**
	 * 
	 * @param initialCapacity
	 * @param profile
	 * @return
	 */
	private List<Flaw> computeProfilePeaks(ResourceProfile profile)
	{
		// initialize resource capacity level
		long currentLevel = this.component.getInitialCapacity();
		// peak flag
		boolean isPeak = false;
		// prepare a peak
		ResourceProfileFlaw peak = new ResourceProfileFlaw(this.component);
		
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check profile samples
		for (ProfileSample sample : profile.getProfileSamples()) 
		{
			// get event
			ResourceEvent event = sample.getEvent();
			// add event to peak 
			peak.addEvent(event);
			
			// update resource level
			currentLevel = event.getType().equals(ResourceEventType.PRODUCTION) ? 
					(currentLevel + event.getAmount())	:		// increase resource level 
					(currentLevel - event.getAmount());			// decrease resource level
			
			// check peak flag
			if (!isPeak) 
			{
				// check if entering peak modality
				isPeak = currentLevel < this.component.getMinCapacity() || 
						currentLevel > this.component.getMaxCapacity();
						
				// if not peak check if flaws can be discarded
				if (!isPeak && currentLevel == this.component.getInitialCapacity()) {
					// reset peak
					peak = new ResourceProfileFlaw(this.component);
				}
			}
			else if (isPeak)
			{
				// check current level of resource
				if (currentLevel >= this.component.getMinCapacity() && 
						currentLevel <= this.component.getMaxCapacity())
				{
					// a peak found
					flaws.add(peak);
					// reset new peak
					peak = new ResourceProfileFlaw(this.component);
					isPeak = false;
				}
			}
		}
		
		
		// check pending peak
		if (isPeak) {
			// add peak
			flaws.add(peak);
		}
		
		// get flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	private List<List<Decision>> schedule(Collection<Decision> decisions) 
	{
		// initialize permutations
		List<List<Decision>> result = new ArrayList<>();
		// compute permutations
		this.computePermutations(decisions, new ArrayList<Decision>(), result);
		// get permutations
		return result;
	}
	
	/**
	 * 
	 * @param decisions
	 * @param current
	 * @param result
	 */
	private void computePermutations(Collection<Decision> decisions, List<Decision> current, List<List<Decision>> result) 
	{
		// base step
		if (current.size() == decisions.size()) {
			result.add(current);
		}
		else {
			// recursive step
			for (Decision dec : decisions) {
				if (!current.contains(dec)) {
					// create a new current list
					List<Decision> list = new ArrayList<>(current);
					list.add(dec);
					// recursive call
					this.computePermutations(decisions, list, result);
				}
			}
		}
	}
}
