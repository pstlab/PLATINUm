package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.resource.ReservoirResource;
import it.uniroma3.epsl2.framework.domain.component.resource.ResourceProductionValue;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Relation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.BeforeRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ProfileSample;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceProfile;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.ResourceProfileComputationException;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.ex.TemporalConstraintPropagationException;
import it.uniroma3.epsl2.framework.time.lang.FixTimePointConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.ComputeMakespanQuery;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalScheduleQuery;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResourceSchedulingResolver <T extends ReservoirResource> extends Resolver 
{
	@ComponentPlaceholder
	protected T component;
	
	/**
	 * 
	 */
	protected ReservoirResourceSchedulingResolver() {
		super(ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER.getLabel(),
				ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER.getFlawType());
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
			// get production events
			events.addAll(this.component.getProductions());
			
			// compute optimistic resource profile
			ResourceProfile orp = this.computeOptimisticResourceProfile(events);
			// compute flaws
			flaws.addAll(this.computeProfilePeaks(orp));
			// check pessimistic resource profile if necessary
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
		
		// get list of flaws detected
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// get peak
		ResourceProfileFlaw profileFlaw = (ResourceProfileFlaw) flaw;
		// get peak decisions
		List<Decision> peak = new ArrayList<>(profileFlaw.getPeak());
		// compute the makespan
		ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
		this.tdb.process(query);
		double mk = query.getMakespan();
		// compute possible solutions of the flaw
		for (int index = 0; index < peak.size() - 1; index++) 
		{
			// create solution
			ResourceProduction prod = new ResourceProduction(profileFlaw);
			// set the makespan
			prod.setMakespan(mk);
			// set current decision as before
			prod.addBeforeDecision(peak.get(index));
			// set next before decisions
			for (int jndex = index - 1; jndex >= 0; jndex--) {
				prod.addBeforeDecision(peak.get(jndex));
			}
			
			// set after decisions
			for (int jndex = index + 1; jndex < peak.size(); jndex++) {
				prod.addAfterDecision(peak.get(jndex));
			}
			
			// add solution
			profileFlaw.addSolution(prod);
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get solution 
		ResourceProduction prod = (ResourceProduction) solution;
		
		// get production value
		ResourceProductionValue value = this.component.getProductionValue();
		// create production decision 
		Decision subgoal = this.component.create(value, new String[] {
			"?amount"
		});
		// add created decision to flaw solution
		prod.addCreatedDecision(subgoal);
		
		// create temporal relations
		List<TemporalRelation> relations = new ArrayList<>();
		// create (pending) relations
		for (Decision dec : prod.getBeforeDecisions()) {
			// create precedence relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, dec, subgoal);
			relations.add(rel);
			// add created relation to flaw solution
			prod.addCreatedRelation(rel);
		}
		
		// create (pending) relations
		for (Decision dec : prod.getAfterDecisions()) {
			// create precedence relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, subgoal, dec);
			relations.add(rel);
			// add created relation to flaw solution
			prod.addCreatedRelation(rel);
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// deactivate relations: ACTIVE -> PENDING/SILENT
		for (Relation rel : solution.getActivatedRelations()) {
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
}
