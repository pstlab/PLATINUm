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
public class ReservoirResourceSchedulingResolver extends Resolver<ReservoirResource> 
{
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
			ReservoirResourceProfile prp = this.component.computePessimisticResourceProfile();
			// analyze the pessimistic profile and find peaks if any and generate production checkpoints
			flaws = this.computeProfilePeaks(prp);
			// check if any flaw has been found
			if (flaws.isEmpty()) 
			{
				// check optimistic resource profile
				ReservoirResourceProfile orp = this.component.computeOptimisticResourceProfile();
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
			throw new UnsolvableFlawException("No feasible solutions found the following peak on reservoir resource \"" + this.component.getName() + "\":\n- peak: " + peak + "\n");
		}
	}
	
	/**
	 * 
	 * @param peak
	 * @return
	 */
	private List<MCS> samplePeak(Peak peak) 
	{
		// list of MCS
		List<MCS> list = new ArrayList<>();
//		for (ConsumptionResourceEvent reference : peak.getConsumptions())
		for (int i= 0; i < peak.getConsumptions().size() - 1; i++)
		{
			// get reference
			ConsumptionResourceEvent reference = peak.getConsumptions().get(i);
			// initialize MCS
			MCS mcs = new MCS();
			mcs.addEvent(reference);
//			for (ConsumptionResourceEvent event : peak.getConsumptions()) 
			for (int j= i; j < peak.getConsumptions().size(); j++)
			{
				// get event 
				ConsumptionResourceEvent event = peak.getConsumptions().get(j);
				if (!event.equals(reference)) 
				{
					// add event to MCS
					mcs.addEvent(event);
					// check amount 
					if (mcs.getResourceConsumption() > this.component.getMaxCapacity()) 
					{
						// add MCS to list
						list.add(mcs);
						// clear and back reference
						mcs = new MCS();
						mcs.addEvent(reference);
					}
				}
			}
		}
		
		// get the list
		return list;
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
		// sample peak consumptions
		List<MCS> MCSs = this.samplePeak(peak);
		// get the most constraining MCS
		Collections.sort(MCSs);
		// get consumptions 
		List<ConsumptionResourceEvent> consumptions = MCSs.get(0).getConsumptions();
		
//		
//		// get the consumptions that generate the peak
//		List<ConsumptionResourceEvent> consumptions = peak.getConsumption();
		
		
		
		
		// list of production checkpoints
		List<ProductionCheckpoint> checkpoints = peak.getProductionCheckpoints();
		// compute production planning solutions
		for (int i = consumptions.size() - 1; i > 0; i--)
		{
			// list of decisions to schedule before production
			List<Decision> beforeProduction = new ArrayList<>();
			// list of decisions after production
			List<Decision> afterProduction = new ArrayList<>();
			// compute the amount of resource consumed
			int amount = 0;
			// set activities to schedule before production (< i)
			for (int k = 0; k < i; k++) {
				beforeProduction.add(consumptions.get(k).getDecision());
				amount += consumptions.get(k).getAmount();
			}
			
			// set activities to schedule after production (>= i)
			for (int k = i; k < consumptions.size(); k++) {
				afterProduction.add(consumptions.get(k).getDecision());
			}
			
			// add activities of the peak not considered in the MCS
			for (ConsumptionResourceEvent consumption : peak.getConsumptions()) {
				if (!consumptions.contains(consumption)) {
					afterProduction.add(consumption.getDecision());
				}
			}

			// add last production checkpoints if any
			if (!checkpoints.isEmpty()) {
				beforeProduction.add(checkpoints.get(checkpoints.size() - 1).getProduction().getDecision());
			}
			
			// create temporal constraints and check temporal feasibility
			List<BeforeIntervalConstraint> constraints = new ArrayList<>();
			// production interval
			TemporalInterval iProduction = null; 
			try
			{
				// create temporal interval
				iProduction = this.tdb.createTemporalInterval(true);
				// propagate "before" contains
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
			
				// propagate "after" constraints
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
				
				// retract created production interval if necessary
				if (iProduction != null) {
					// delete temporal interval
					this.tdb.deleteTemporalInterval(iProduction);
				}
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
		// sample peak consumptions
		List<MCS> MCSs = this.samplePeak(peak);
		// get the most constraining MCS
		Collections.sort(MCSs);
		// get consumptions 
		List<ConsumptionResourceEvent> consumptions = MCSs.get(0).getConsumptions();
		// get the consumptions that generate the peak
//		List<ConsumptionResourceEvent> consumptions = peak.getConsumption();
		
		// get production checkpoints
		List<ProductionCheckpoint> checkpoints = peak.getProductionCheckpoints();
		
		// analyze peak consumptions
		for (int i = 0; i < consumptions.size(); i++) 
		{
			// get current considered consumption
			ConsumptionResourceEvent consumption = consumptions.get(i);
			// get the rest of consumptions composing the current peak
			List<ConsumptionResourceEvent> rest = new ArrayList<>();
			for (ConsumptionResourceEvent event : consumptions) {
				if (!consumption.equals(event)) {
					rest.add(event);
				}
			}
			
			// analyze checkpoints
			for (int index = checkpoints.size() -1; index >= 0; index--) 
			{
				// get checkpoint 
				ProductionCheckpoint checkpoint = checkpoints.get(index);
				// get potential "energy" of the checkpoint
				double potential = this.component.getMaxCapacity() - checkpoint.getResourceConsumed();
				// check the potential energy of the checkpoint is enough for the consumption
				if (potential >= consumption.getAmount())
				{
					// prepare solution map
					Map<Decision, Decision> solmap = new HashMap<>();
					// create precedence constraint and check feasibility
					List<BeforeIntervalConstraint> constraints = new ArrayList<>();
					try
					{
						// schedule consumption before current checkpoint
						BeforeIntervalConstraint before1 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
						before1.setReference(consumption.getDecision().getToken().getInterval());
						before1.setTarget(checkpoint.getProduction().getDecision().getToken().getInterval());
						// set bounds
						before1.setLowerBound(0);
						before1.setUpperBound(this.tdb.getHorizon());
						constraints.add(before1);
						// add solution map entry
						solmap.put(consumption.getDecision(), checkpoint.getProduction().getDecision());
						
						// check if after constraint is needed
						if (index > 0) 
						{
							// "second-last" checkpoint
							ProductionCheckpoint secondLast = checkpoints.get(index - 1);
							// schedule consumption after second last checkpoint
							BeforeIntervalConstraint before2 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
							before2.setReference(secondLast.getProduction().getDecision().getToken().getInterval());
							before2.setTarget(consumption.getDecision().getToken().getInterval());
							before2.setLowerBound(0);
							before2.setUpperBound(this.tdb.getHorizon());
							constraints.add(before2);
							// add solution map entry
							solmap.put(secondLast.getProduction().getDecision(), consumption.getDecision());
						}
						
						// try to propagate computed constraints
						for (BeforeIntervalConstraint constraint : constraints) {
							// propagate constraint
							this.tdb.propagate(constraint);
						}
						
						// check the feasibility of the solution
						this.tdb.checkConsistency();
						// compute the preserved space of the involved time points
						double preserved = 0;
						for (BeforeIntervalConstraint c : constraints) {
							// compute preserved space heuristic value
							preserved += this.computePreservedSpaceHeuristicValue(c.getReference().getEndTime(), c.getTarget().getStartTime());
							
						}
						// get average
						preserved = preserved / constraints.size();
						
						// compute the resulting makespan of the temporal network
						ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
						// process query
						this.tdb.process(query);
						// get computed makespan
						double makespan = query.getMakespan();
						
						// add consumption scheduling solution
						ConsumptionScheduling scheduling = new ConsumptionScheduling(
								peak, 
								checkpoint.getProduction().getDecision(),
								consumption.getAmount(),
								solmap, 
								preserved);
						
						// set solution makespan
						scheduling.setMakespan(makespan);
						// add solution to the peak
						peak.addSolution(scheduling);
					}
					catch (ConsistencyCheckException | TemporalConstraintPropagationException ex) {
						this.logger.debug("Not valid schedule found to solve peak:\n- peak: " + peak + "\n"
								+ "- schedule: " + consumption + " < " + checkpoint.getProduction() + "\n");
					}
					finally 
					{
						// retract constraints
						for (BeforeIntervalConstraint constraint : constraints) {
							this.tdb.retract(constraint);
						}
						// clear constraints
						constraints.clear();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution flawSolution) throws Exception 
	{
		// perform "default" operations
		super.doRestore(flawSolution);
		// get general solution
		ResourceOverConsumptionSolution overconsumption = (ResourceOverConsumptionSolution) flawSolution;
		// check type
		if (overconsumption.getType().equals(ResourceOverConsumptionSolutionType.CONSUMPTION_SCHEDULING)) 
		{
			// restore the production binding constraint
			ConsumptionScheduling solution = (ConsumptionScheduling) overconsumption;
			// get production activity
			Decision production = solution.getProduction();
			// get parameter relation
			for (Relation rel : this.component.getRelations(production)) 
			{
				// check parameter relation
				if (rel.getType().equals(RelationType.BIND_PARAMETER)) 
				{
					try
					{
						// bind parameter relation
						BindParameterRelation bind = (BindParameterRelation) rel;
						// retract binding constraint
						this.component.deactivate(bind);
						// decrease the amount of resource to produce
						int value = Integer.parseInt(bind.getValue());
						value -= solution.getProductionDelta();
						// update binding constraint
						bind.setValue(Integer.toString(value));
						// propagate bind constraint
						this.component.activate(bind);
					}
					catch (RelationPropagationException ex) {
						throw new RuntimeException("Error while retracting consumption scheduling solution\n:- mesasge= " + ex.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution flawSolution) 
	{
		// perform "default" operations
		super.doRetract(flawSolution);
		// get general solution
		ResourceOverConsumptionSolution overconsumption = (ResourceOverConsumptionSolution) flawSolution;
		// check type
		if (overconsumption.getType().equals(ResourceOverConsumptionSolutionType.CONSUMPTION_SCHEDULING)) 
		{
			// restore the production binding constraint
			ConsumptionScheduling solution = (ConsumptionScheduling) overconsumption;
			// get production activity
			Decision production = solution.getProduction();
			// get parameter relation
			for (Relation rel : this.component.getRelations(production)) 
			{
				// check parameter relation
				if (rel.getType().equals(RelationType.BIND_PARAMETER)) 
				{
					try
					{
						// bind parameter relation
						BindParameterRelation bind = (BindParameterRelation) rel;
						// retract binding constraint
						this.component.deactivate(bind);
						// decrease the amount of resource to produce
						int value = Integer.parseInt(bind.getValue());
						value -= solution.getProductionDelta();
						// update binding constraint
						bind.setValue(Integer.toString(value));
						// propagate bind constraint
						this.component.activate(bind);
					}
					catch (RelationPropagationException ex) {
						throw new RuntimeException("Error while retracting consumption scheduling solution\n:- mesasge= " + ex.getMessage());
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
		try
		{
			// create and activate precedence constraints
			for (Decision reference : constraints.keySet())
			{
				// get target
				Decision target = constraints.get(reference);
				// create constraint
				BeforeRelation before = this.component.create(RelationType.BEFORE, reference, target);
				// set bounds
				before.setBound(new long[] {
						0, this.tdb.getHorizon()
				});
				
				// set relation as created
				solution.addCreatedRelation(before);
				// add relation to component
				this.component.activate(before);
				// set relation as activated
				solution.addActivatedRelation(before);
			}
			
			// get production activity
			Decision production = solution.getProduction();
			// get parameter relation
			for (Relation rel : this.component.getRelations(production)) 
			{
				// check parameter relation
				if (rel.getType().equals(RelationType.BIND_PARAMETER)) 
				{
					// bind parameter relation
					BindParameterRelation bind = (BindParameterRelation) rel;
					// deactivate binding constraint
					this.component.deactivate(bind);
					// increase the amount of resource to produce
					int value = Integer.parseInt(bind.getValue());
					value += solution.getProductionDelta();
					// update binding constraint
					bind.setValue(Integer.toString(value));
					// propagate bind constraint
					this.component.activate(bind);
				}
			}
		}
		catch (RelationPropagationException ex) {
			// retract activated relations
			for (Relation relation : solution.getActivatedRelations()) {
				this.component.deactivate(relation);
			}
			// retract created relations
			for (Relation relation : solution.getCreatedRelations()) {
				this.component.delete(relation);
			}
			
			// throw exception
			throw new FlawSolutionApplicationException("Error while applying solution to resource peak:\n- solution: " + solution + "\n- message: " + ex.getMessage() + "\n");
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
		ResourceProductionValue value = this.component.getProductionValue();
		// create production decision (it represents a planning goal) 
		Decision goal = this.component.create(value, new String[] {
			"?amount"
		});
		// add created decision to flaw solution
		solution.addCreatedDecision(goal);
		// set as mandatory expansion goal
		goal.setMandatoryExpansion();
		
		// add parameter (pending) relation to bind the production parameter
		BindParameterRelation bind = this.component.create(RelationType.BIND_PARAMETER, goal, goal);
		// set the desired amount of resource to produce
		bind.setValue(Integer.toString(solution.getAmount()));
		bind.setReferenceParameterLabel("?amount");
		// add created relations
		solution.addCreatedRelation(bind);
		
		// create temporal (pending) relations
		for (Decision dec : solution.getDecisionsBeforeProduction()) {
			// create precedence relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, dec, goal);
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
			BeforeRelation rel = this.component.create(RelationType.BEFORE, goal, dec);
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
		long currentLevel = this.component.getInitialLevel();
		// initialize the set of production checkpoints
		Set<ProductionCheckpoint> checkpoints = new HashSet<>();
		// set of consumptions that may generate a peak
		List<ConsumptionResourceEvent> consumptions = new ArrayList<>();
		// get profile samples
		List<ResourceUsageProfileSample> samples = profile.getSamples();
		boolean peakMode = false;
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
				double consumed = this.component.getMaxCapacity() - currentLevel;
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
			peakMode = currentLevel < this.component.getMinCapacity();
			// check if a peak must be closed
			if (sample.getAmount() > 0 && peakMode) 
			{
				// exit peak mode
				peakMode = false;
				// compute the delta value of the peak
				long delta = this.component.getMinCapacity() - currentLevel;
				// create a peak
				Peak peak = new Peak(this.component, consumptions, delta, checkpoints);
				// add the peak
				peaks.add(peak);
				// clear consumptions
				consumptions = new ArrayList<>();
			}
		}
		
		// check if a peak must be closed
		if (peaks.isEmpty() && peakMode) {
			// create a peak
			// compute the delta value of the peak
			long delta = this.component.getMinCapacity() - currentLevel;
			// create a peak
			Peak peak = new Peak(this.component, consumptions, delta, checkpoints);
			// add the peak
			peaks.add(peak);
			// clear consumptions
			consumptions = new ArrayList<>();
		}
		
		// prepare the list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check if any peak has been found
		if (!peaks.isEmpty()) 
		{
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

/**
 * 
 * @author anacleto
 *
 */
class MCS implements Comparable<MCS>
{
	private List<ConsumptionResourceEvent> consumptions;
	
	/**
	 * 
	 * @param events
	 */
	protected MCS() {
		this.consumptions = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param event
	 */
	public void addEvent(ConsumptionResourceEvent event) {
		this.consumptions.add(event);
	}
	
	public List<ConsumptionResourceEvent> getConsumptions() {
		return consumptions;
	}
	
	public double getResourceConsumption() {
		double total = 0;
		for (ConsumptionResourceEvent event : consumptions) {
			total += event.getAmount();
		}
		return total;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(MCS o) {
		return this.getResourceConsumption() >= o.getResourceConsumption() ? -1 : 1;
	}
}




