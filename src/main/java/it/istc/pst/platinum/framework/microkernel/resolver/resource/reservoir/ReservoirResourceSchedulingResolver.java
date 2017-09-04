package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ConsumptionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ProductionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResourceProfile;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ResourceProductionValue;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ResourceUsageProfileSample;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResourceSchedulingResolver extends Resolver 
{
	@ComponentPlaceholder
	protected ReservoirResource resource;
	
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
			// check pessimistic resource profile
			ReservoirResourceProfile prp = this.resource.computePessimisticResourceProfile();
			// analyze the pessimistic profile and find peaks if any and generate production checkpoints
			flaws = this.computeProfilePeaks(prp);
			// check if any flaw has been found
			if (flaws.isEmpty()) 
			{
				// check optimistic resource profile
				ReservoirResourceProfile orp = this.resource.computeOptimisticResourceProfile();
				// analyze the optimistic profile and find peaks if any and generate production checkpoints
				flaws = this.computeProfilePeaks(orp);
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
			throws UnsolvableFlawException 
	{
		// get peak
		Peak peak = (Peak) flaw;
		
		// try to solve the peak through scheduling if possible
		if (!peak.getProductionCheckpoints().isEmpty()) {
			// analyze possible schedules
			this.doComputePeakSchedulingSolutions(peak);
		}
		
		/*
		 * Now I'm assuming that a production always generate the amount of resource needed to reach the maximum capacity,
		 * independently from the duration of the production activity
		 */
		this.doComputePeakPlanningSolutions(peak);
		
		// check solutions found
		if (peak.getSolutions().isEmpty()) {
			throw new UnsolvableFlawException("No feasible solutions found the following peak on reservoir resource \"" + this.resource.getName() + "\":\n- peak: " + peak + "\n");
		}
	}
	
	/**
	 * Solve a resource over consumption by adding a production activity.
	 * This method tries to introduce a new activity into the plan in order to generate the 
	 * amount of resource needed to execute the set of activities generating the peak. 
	 * 
	 * @param peak
	 */
	private void doComputePeakPlanningSolutions(Peak peak)
	{
		// get the consumptions that generate the peak
		List<ConsumptionResourceEvent> consumptions = peak.getConsumption();
		// compute the amount of resource to produce in order to set the maximum capacity
		int amount = 0;
		for (int index = 0; index < consumptions.size() - 1; index++)
		{
			// get consumption activity
			ConsumptionResourceEvent consumption = consumptions.get(index);
			amount += Math.abs(consumption.getAmount());
		}
		
		
		// initialize the list of decisions to schedule before production
		List<Decision> beforeProduction = new ArrayList<>();
		// initialize the list of decisions to schedule after production
		List<Decision> afterProduction = new ArrayList<>();
	
		// get last production checkpoint if any
		if (!peak.getProductionCheckpoints().isEmpty()) {
			// get last production checkpoint 
			ProductionCheckpoint checkpoint = peak.getProductionCheckpoints().get(peak.getProductionCheckpoints().size() - 1);
			 // schedule production after last introduced production
			beforeProduction.add(checkpoint.getProduction().getDecision());
		}
		
		// get consumptions before peak (assuming minimal peak)
		for (int index = consumptions.size() - 2; index >= 0; index--) {
			ConsumptionResourceEvent before = consumptions.get(index);
			// schedule production after last consumption
			beforeProduction.add(before.getDecision());
		}
		
		// get consumption generating the peak
		ConsumptionResourceEvent cause = consumptions.get(consumptions.size() - 1);
		// schedule production before the consumption generating the peak
		afterProduction.add(cause.getDecision());
		
		// check the feasibility of the schedule by propagating constraints
		List<BeforeIntervalConstraint> constraints = new ArrayList<>();
		// production interval
		TemporalInterval iProduction = null; 
		try
		{
			// create temporal interval
			iProduction = this.tdb.createTemporalInterval(true);
			// propagate "before" constaints
			for (Decision decision : beforeProduction) 
			{
				// propagate constraint "decision < production"
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
				// set reference and target intervals
				before.setReference(decision.getToken().getInterval());
				before.setTarget(iProduction);
				// set bounds
				before.setLowerBound(0);
				before.setUpperBound(this.tdb.getHorizon());
				
				// propagate constraint
				this.tdb.propagate(before);
				// add constraint to the list
				constraints.add(before);
			}
		
			// propagate "after" cosntraints
			for (Decision decision : afterProduction)
			{
				// propagate constraint "production < decision"
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
				// set reference and target intervals
				before.setReference(iProduction);
				before.setTarget(decision.getToken().getInterval());
				// set bounds
				before.setLowerBound(0);
				before.setUpperBound(this.tdb.getHorizon());
				
				// propagate constraint
				this.tdb.propagate(before);
				// add constraint to the list
				constraints.add(before);
			}
			
			// compute the resulting makespan
			ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
			this.tdb.process(query);
			// get resulting makespan
			double makespan = query.getMakespan();
			
			// create resource planning solution
			ProductionPlanning pp = new ProductionPlanning(peak, amount, beforeProduction, afterProduction);
			// set resulting makespan
			pp.setMakespan(makespan);
			// add solution to the peak
			peak.addSolution(pp);
		}
		catch (TemporalConstraintPropagationException ex) {
			this.logger.debug("It is not possible to schedule new production in order to solve resource over consumption:\n- before-production: " + beforeProduction + "\n- after-production: " + afterProduction + "\n");
		}
		catch (TemporalIntervalCreationException ex) {
			this.logger.debug("Erorr while creating temporal interval for checking the temporal feasibility of production planning\n");
		}
		finally 
		{
			// retract all temporal constraints
			for (BeforeIntervalConstraint constraint : constraints) {
				this.tdb.retract(constraint);
			}
			
			// check if not null
			if (iProduction != null) {
				// delete temporal interval
				this.tdb.deleteTemporalInterval(iProduction);
			}
		}
	}
	
	/**
	 * Solve a reservoir resource peak by scheduling before a previous production if any.
	 * This method tries to leverages past production activities in order to avoid new productions. The consumption events 
	 * that compose the peak are scheduled according to the available productions if the resulting plan is temporally consistent 
	 * 
	 * @param peak
	 */
	private void doComputePeakSchedulingSolutions(Peak peak)
	{
		// get the consumptions that generate the peak
		List<ConsumptionResourceEvent> consumptions = peak.getConsumption();
		// get "past" productions
		List<ProductionCheckpoint> checkpoints = peak.getProductionCheckpoints();
		
		// analyze peak consumptions
		for (ConsumptionResourceEvent event : consumptions) 
		{
			// get consumption decision
			Decision consumption = event.getDecision();
			// get amount of resource needed
			double amount = event.getAmount();
			// analyze checkpoints
			for (int index = 0; index < checkpoints.size(); index++) 
			{
				// get checkpoint 
				ProductionCheckpoint checkpoint = checkpoints.get(index);
				// get production decision
				Decision production = checkpoint.getProduction().getDecision();
				// get potential "energy" of the checkpoint
				double potential = this.resource.getMaxCapacity() - checkpoint.getResourceConsumed();
				// check the potential energy of the checkpoint is enough for the consumption
				if (potential >= amount)
				{
					// check temporal feasibility of the possible schedule: "consumption < production(i)"
					BeforeIntervalConstraint c1 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
					// add also constraint "production(i-1) < consumption" if possible
					BeforeIntervalConstraint c2 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
					// initialize solution relationships
					Map<Decision, Decision> constraints = new HashMap<>();
					try
					{
						// set decisions
						c1.setReference(consumption.getToken().getInterval());
						c1.setTarget(production.getToken().getInterval());
						// set bounds
						c1.setLowerBound(0);
						c1.setUpperBound(this.tdb.getHorizon());
						
						// try to propagate constraint
						this.tdb.propagate(c1);
						
						// add entry to constraints: "consumption < production"
						constraints.put(consumption, production);
						
						// check if constraint c2 is necessary
						if (index > 0) {
							// get previous production decision
							Decision previous = checkpoints.get(index - 1).getProduction().getDecision();
							// set reference
							c2.setReference(previous.getToken().getInterval());
							c2.setTarget(consumption.getToken().getInterval());
							// set bounds 
							c2.setLowerBound(0);
							c2.setUpperBound(this.tdb.getHorizon());
							
							// try to propagate constraint
							this.tdb.propagate(c2);
							// add entry to constraints: "previous < consumption"
							constraints.put(previous, consumption);
						}
						
						// check the feasibility of the solution
						this.tdb.checkConsistency();
						// compute the preserved space of the involved time points
						double preserved = this.computePreservedSpaceHeuristicValue(
								c1.getReference().getEndTime(), 
								c1.getTarget().getStartTime());
						// update preserved space with constraint c2 if necessary
						if (index > 0) {
							// compute the resulting (average) preserved space heuristics
							preserved = (preserved + this.computePreservedSpaceHeuristicValue(
									c2.getReference().getEndTime(), 
									c2.getTarget().getStartTime())) / 2.0;
						}
						
						// compute the resulting makespan of the temporal network
						ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
						// process query
						this.tdb.process(query);
						// get computed makespan
						double makespan = query.getMakespan();
						
						// add consumption scheduling solution
						ConsumptionScheduling scheduling = new ConsumptionScheduling(peak, constraints, preserved);
						scheduling.setMakespan(makespan);
						// add solution to the peak
						peak.addSolution(scheduling);
					}
					catch (ConsistencyCheckException | TemporalConstraintPropagationException ex) {
						this.logger.debug("Not valid schedule found to solve peak:\n- peak: " + peak + "\n"
								+ "- schedule: " + consumption + " < " + production + "\n");
					}
					finally {
						// retract constraints
						this.tdb.retract(c1);
						this.tdb.retract(c2);
						// clear constraints
						constraints.clear();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param solution
	 * @throws FlawSolutionApplicationException
	 */
	private void doApplyResourceSchedulingSolution(ConsumptionScheduling solution) 
			throws FlawSolutionApplicationException 
	{
		// get the set of constraint to propagate
		Map<Decision, Decision> constraints = solution.getPrecedenceConstraints();
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try
		{
			// create and activate precedence constraints
			for (Decision reference : constraints.keySet())
			{
				// get target
				Decision target = constraints.get(reference);
				// create constraint
				BeforeRelation before = this.resource.create(RelationType.BEFORE, reference, target);
				// set bounds
				before.setBound(new long[] {
						0, this.tdb.getHorizon()
				});
				
				// add relation to component
				this.resource.add(before);
				// add to committed relations
				committed.add(before);
			}
			
			/*
			 * FIXME : when rescheduling a consumption it is necessary to update the amount of 
			 * produced resource by the following production (checkpoint) in order to set 
			 * the level to the maximum capacity
			 */
		}
		catch (RelationPropagationException ex) {
			// retract committed relations
			for (Relation relation : committed) {
				this.resource.free(relation);
			}
			
			// throw exception
			throw new FlawSolutionApplicationException("Error while applying solutio to resource peak:\n- solution: " + solution + "\n- message: " + ex.getMessage() + "\n");
		}
	}
	
	/**
	 * 
	 * @param solution
	 * @throws FlawSolutionApplicationException
	 */
	private void doApplyResourcePlanningSolution(ProductionPlanning solution) 
			throws FlawSolutionApplicationException 
	{
		// get production value
		ResourceProductionValue value = this.resource.getProductionValue();
		// create production decision (it represents a planning goal) 
		Decision goal = this.resource.create(value, new String[] {
			"?amount"
		});
		// add created decision to flaw solution
		solution.addCreatedDecision(goal);
		// set as mandatory expansion goal
		goal.setMandatoryExpansion();
		
		// add parameter (pending) relation to bind the production parameter
		BindParameterRelation bind = this.resource.create(RelationType.BIND_PARAMETER, goal, goal);
		// set the desired amount of resource to produce
		bind.setValue(Integer.toString(solution.getAmount()));
		bind.setReferenceParameterLabel("?amount");
		// add created relations
		solution.addCreatedRelation(bind);
		
		// create temporal (pending) relations
		for (Decision dec : solution.getDecisionsBeforeProduction()) {
			// create precedence relation
			BeforeRelation rel = this.resource.create(RelationType.BEFORE, dec, goal);
			// set relation bounds
			rel.setBound(new long[] {
					0,
					this.tdb.getHorizon()
			});
			// add created relation to flaw solution
			solution.addCreatedRelation(rel);
		}
		
		// create (pending) relations
		for (Decision dec : solution.getDecisionsAfterProduction()) {
			// create precedence relation
			BeforeRelation rel = this.resource.create(RelationType.BEFORE, goal, dec);
			// set relation bounds
			rel.setBound(new long[] {
					0, 
					this.tdb.getHorizon()
			});
			// add created relation to flaw solution
			solution.addCreatedRelation(rel);
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get general solution
		ResourceOverConsumptionSolution overconsumption = (ResourceOverConsumptionSolution) solution;
		// check over consumption type
		switch (overconsumption.getType())
		{
			// scheduling solution
			case CONSUMPTION_SCHEDULING : {
				// get scheduling solution
				ConsumptionScheduling scheduling = (ConsumptionScheduling) overconsumption;
				this.doApplyResourceSchedulingSolution(scheduling);
			}
			break;
			
			// planning solution
			case PRODUCTION_PLANNING : {
				// get planning solution
				ProductionPlanning planning = (ProductionPlanning) overconsumption;
				this.doApplyResourcePlanningSolution(planning);
			}
			break;
			
			default : {
				throw new RuntimeException("Unknownw reservoir resource peak solution type: " + overconsumption.getType() + "\n");
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// completely remove activated relations
		for (Relation relation : solution.getActivatedRelations()) {
			this.resource.free(relation);
		}
		
		// delete created relations: PENDING -> SILENT
		for (Relation relation : solution.getCreatedRelations()) {
			this.resource.delete(relation);
		}
		
		// delete created decision: PENDING -> SILENT
		for (Decision decision : solution.getCreatedDecisions()) {
			this.resource.delete(decision);
		}
	}

	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws Exception 
	{
		// restore created decisions
		for (Decision decision : solution.getCreatedDecisions()) {
			// restore decision
			this.resource.restore(decision);
		}
		
		// restore created relations
		for (Relation relation : solution.getCreatedRelations()) {
			// restore relation
			this.resource.restore(relation);
		}
		
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		// restore directly activated relations
		for (Relation relation : solution.getActivatedRelations()) 
		{
			try
			{
				// restore relation
				this.resource.restore(relation);
				// activate relation
				this.resource.add(relation);
				committed.add(relation);
			}
			catch (RelationPropagationException ex) {
				// deactivate committed relations
				for (Relation r : committed) {
					// deactivate relation
					this.resource.free(r);
				}
				
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
	}
	
	/**
	 * Analyze the profile of a reservoir resource in order to find peaks and compute production checkpoints
	 * 
	 * @param profile
	 * @return
	 */
	private List<Flaw> computeProfilePeaks(ReservoirResourceProfile profile)
	{
		// list of peak found
		List<Peak> peaks = new ArrayList<>();
		// initialize resource capacity level
		long currentLevel = this.resource.getInitialLevel();
		// initialize the set of production checkpoints
		Set<ProductionCheckpoint> checkpoints = new HashSet<>();
		// set of consumptions that may generate a peak
		List<ConsumptionResourceEvent> consumptions = new ArrayList<>();
		// get profile samples
		List<ResourceUsageProfileSample> samples = profile.getSamples();
		// analyze the resource profile until a peak is found
		for (int index = 0; index < samples.size() && peaks.isEmpty(); index++)
		{
			// current sample
			ResourceUsageProfileSample sample = samples.get(index);
			// check production event
			if (sample.getAmount() > 0)
			{
				// get production event
				ProductionResourceEvent production = (ProductionResourceEvent) sample.getEvent();
				// get potential resource usage
				double consumed = this.resource.getMaxCapacity() - currentLevel;
				// create a production checkpoint
				ProductionCheckpoint point = new ProductionCheckpoint(production, consumed, sample.getSchedule());
				// add to the set
				checkpoints.add(point);
				// clear the list of peak consumptions
				consumptions = new ArrayList<>();
			}
			
			// check consumption event
			if (sample.getAmount() < 0) {
				// get consumption
				ConsumptionResourceEvent consumption = (ConsumptionResourceEvent) sample.getEvent();
				// add event to possible peak consumptions
				consumptions.add(consumption);
			}
			
			// update the current level of resource
			currentLevel += sample.getAmount();
			
			// check resource over consumption
			if (currentLevel < this.resource.getMinCapacity())
			{
				// compute the delta value of the peak
				long delta = this.resource.getMinCapacity() - currentLevel;
				// create a peak
				Peak peak = new Peak(this.resource, consumptions, delta, checkpoints);
				// add the peak
				peaks.add(peak);
				// clear consumptions
				consumptions = new ArrayList<>();
			}
		}
		
		// prepare the list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check if any peak has been found
		if (!peaks.isEmpty()) {
			// sort peaks according to the value of the delta
			Collections.sort(peaks);
			// get the "best" peak to solve
			Peak peak = peaks.get(0);
			// add to flaws
			flaws.add(peak);
		}
		
		// get found peaks - only one element expected
		return flaws;
	}


	/**
	 * Estimate the preserved values of time point domains after propagation of a precedence constraint "tp1 < tp2".
	 * 
	 * The method assumes that the precedence constraint is feasible and that the bounds of the time points have been updated 
	 * according to precedence constraint (i.e. the underlying temporal must encapsulate additional information coming from 
	 * the temporal constraint) 
	 * 
	 * @param tp1
	 * @param tp2
	 * @return
	 */
	private double computePreservedSpaceHeuristicValue(TimePoint tp1, TimePoint tp2)
	{
		// initialize value
		double preserved = 0;
		// compute parameters
		double A = (tp2.getUpperBound() - tp2.getLowerBound() + 1) * (tp1.getUpperBound() - tp1.getLowerBound() + 1);
		double B = (tp2.getUpperBound() - tp1.getLowerBound() + 1) * (tp2.getUpperBound() - tp1.getLowerBound() + 2);
		double Cmin = Math.max(0, (tp2.getLowerBound() - tp1.getLowerBound()) * (tp2.getLowerBound() - tp1.getLowerBound() + 1));
		double Cmax = Math.max(0, (tp2.getUpperBound() - tp1.getUpperBound() * (tp2.getUpperBound() - tp1.getUpperBound() + 1)));

		// compute preserved space value
		preserved = (B - Cmin - Cmax) / (2 * A);
		
		// get computed heuristic value
		return preserved;
	}
}
