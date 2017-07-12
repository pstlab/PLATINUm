package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileManager;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ProfileSample;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEventType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceProfile;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.resolver.scheduling.SchedulingResolver;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.FixTimePointConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.query.IntervalScheduleQuery;
import it.istc.pst.platinum.framework.time.tn.TimePoint;
import it.istc.pst.platinum.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver <T extends DomainComponent & ResourceProfileManager> extends SchedulingResolver 
{ 
	@ComponentPlaceholder
	protected T component;

	/**
	 * 
	 */
	protected DiscreteResourceSchedulingResolver() {
		super(ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getFlawType());
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
			// get consumption events 
			List<ResourceEvent> events = this.component.getConsumptions();
			// get production events
			events.addAll(this.component.getProductions());
			
			// compute optimistic resource profile
			ResourceProfile orp = this.computeOptimisticResourceProfile(events);
			// compute flaws
			flaws.addAll(this.computeProfilePeaks(orp));
			
			// check results
			if (flaws.isEmpty()) {
				// compute pessimistic resource profile
				ResourceProfile prp = this.computePessimisticResourceProfile(events);
				// compute flaws
				flaws.addAll(this.computeProfilePeaks(prp));
			}
		}
		catch (ResourceProfileComputationException ex) {
			// profile computation error
			throw new RuntimeException("Resource profile computation error:\n- " + ex.getMessage() + "\n");
		}
		
		// get computed flaws
		return flaws;
	}

	/**
	 * 	FIXME -> Leverage MCS to compute peak solutions 
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
			try
			{
				// propagate solution and compute the resulting makespan
				double makespan = this.checkScheduleFeasibility(ordering);
				// set resulting makespan
				solution.setMakespan(makespan);
				profileFlaw.addSolution(solution);
				this.logger.debug("Feasible solution of the peak:\n- solution= " + solution + "\n");
			}
			catch (TemporalConstraintPropagationException ex) {
				// not feasible schedule, discard the related schedule
				this.logger.debug("Not feasible solution of the peak:\n- solution= " + solution + "\n... discarding solution\n");
			}
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
			// add activated relations to solution
			solution.addActivatedRelations(relations);
		}
		catch (RelationPropagationException ex) 
		{
			// free all relations
			for (Relation rel : relations) {
				// clear memory from relation
				this.component.free(rel);
			}
			
			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws Exception 
	{
		// get created decisions 
		List<Decision> dCreated = solution.getCreatedDecisions();
		// restore created decisions
		for (Decision dec : dCreated) {
			// restore decision
			this.component.restore(dec);
		}
		
		// get activated decisions
		List<Decision> dActivated = solution.getActivatedDecisisons();
		List<Decision> commitDecs = new ArrayList<>();
		// activate decisions
		for (Decision dec : dActivated) 
		{
			try
			{
				// activate decision
				this.component.add(dec);
				commitDecs.add(dec);
			}
			catch (DecisionPropagationException ex) 
			{
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate decision
					this.component.delete(d);
				}

				// error while resetting flaw solution
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
		
		// get activated relations
		List<Relation> rActivated = solution.getActivatedRelations();
		// list of committed relations
		List<Relation> commitRels = new ArrayList<>();
		// activate relations
		for (Relation rel : rActivated) 
		{
			try
			{
				// activate relation
				this.component.add(rel);
				commitRels.add(rel);
			}
			catch (RelationPropagationException ex) {
				// deactivate committed relations
				for (Relation r : commitRels) {
					// deactivate relation
					this.component.delete(r);
				}
				
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate 
					this.component.delete(d);
				}
				
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// manage activated relations
		for (Relation rel : solution.getActivatedRelations()) {
			// deactivate relation
			this.component.delete(rel);
		}
		
		// delete activated decisions: ACTIVE -> PENDING
		for (Decision dec : solution.getActivatedDecisisons()) {
			// deactivate decision
			this.component.delete(dec);
		}
		
		// delete pending decisions: PENDING -> SILENT
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
			// sort events according to the lower bound of the time points
			Collections.sort(events);
			// check resource events
			for (ResourceEvent event : events) 
			{
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
						cons.setTime(point.getUpperBound());	// ORP - schedule consumption events as late as possible
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
						cons.setTime(point.getLowerBound());	// ORP - schedule production events as soon as possible
						// propagate constraint
						this.tdb.propagate(cons);
						
						// add sample
						orp.addSample(event, point.getLowerBound());
						// add constraint
						toRetract.add(cons);
					}
					break;
				}
				
				// check updated schedules
				for (ResourceEvent e : events) {
					// check event schedule
					TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
					query.setTimePoint(e.getEvent());
					// process query
					this.tdb.process(query);
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

		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// prepare a peak
		ResourceProfileFlaw peak = new ResourceProfileFlaw(this.component);
		// check profile samples
		List<ProfileSample> samples = profile.getProfileSamples();
		for (ProfileSample sample : samples) 
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
				// check entering condition to peak modality
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
				// check exiting condition from peak modality
				isPeak = currentLevel != this.component.getInitialCapacity();
				
				// close peak if exit
				if (!isPeak) {
					// peak found
					flaws.add(peak);
					// reset new peak
					peak = new ResourceProfileFlaw(this.component);
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
}
