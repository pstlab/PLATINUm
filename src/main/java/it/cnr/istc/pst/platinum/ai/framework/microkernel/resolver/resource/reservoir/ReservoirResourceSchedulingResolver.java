package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.reservoir;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.ResourceProfileComputationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir.ReservoirResource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir.ReservoirResourceProfile;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir.ResourceUsageProfileSample;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.Resolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.BeforeIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author alessandro
 *
 */
public class ReservoirResourceSchedulingResolver extends Resolver<ReservoirResource> 
{
	private double schedulingCost;
	
	/**
	 * 
	 */
	protected ReservoirResourceSchedulingResolver() {
		super(ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER.getLabel(),
				ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER.getFlawTypes());
		
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		
		this.schedulingCost = Double.parseDouble(properties.getProperty("scheduling-cost"));
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
			ReservoirResourceProfile prp = this.component.
					computePessimisticResourceProfile();
			/*
			 * Analyze the pessimistic profile and find peaks if 
			 * any and generate production checkpoints
			 */
			flaws = this.doComputeProfileOverflows(prp);
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
		// check flaw type
		switch (flaw.getType()) 
		{
			// resource peak
			case RESERVOIR_OVERFLOW : 
			{
				// get peak
				ReservoirOverflow overflow = (ReservoirOverflow) flaw;
				// solvable condition
				boolean solvable = false;
				// check the size of the critical set
				if (overflow.getCriticalSet().size() > 1) {
					// check if solvable through scheduling - at least one production and one consumption are needed
					solvable = !overflow.getProductions().isEmpty() && !overflow.getConsumptions().isEmpty();
				}
				
				// check if solvable
				if (solvable) {
					// find a feasible solution if any
					this.findFeasibleSchedule(overflow);
				}
			}
			break;
			
			default : {
				warning("Resolver [" + this.getClass().getName() + "] cannor resolver flaw of type " + flaw.getType() + "\n");
			}
		}
		
		// check solutions found
		if (flaw.getSolutions().isEmpty()) {
			throw new UnsolvableFlawException("No feasible solutions found the following peak on reservoir resource \"" + this.component.getName() + "\":\n- flaw: " + flaw + "\n");
		}
	}
	
	
	/**
	 * 
	 * @param schedule
	 * @param initialLevel
	 * @return
	 */
	private boolean checkCapacityFeasibility(List<ResourceEvent<?>> schedule, double initialLevel)
	{
		// feasibility flag
		boolean feasible = true;
		// level of resource
		double currentLevel = initialLevel;
		// check resource level resulting from the schedule
		for (int index = 0; index < schedule.size() && feasible; index++) {
			// get event
			ResourceEvent<?> event = schedule.get(index);
			// update the current level
			currentLevel += event.getAmount();
			
			// check feasibility
			feasible = currentLevel >= this.component.getMinCapacity() && 
					currentLevel <= this.component.getMaxCapacity();
		}
		
		// check feasibility
		if (!feasible) {
			// log data
			debug("Component [" + this.label + "] capacity unfeasible schedule:\n"
					+ "- potential schedule critical set: " + schedule + "\n");
		}
		
		// get feasibility flag
		return feasible;
	}
	
	
	/**
	 * 
	 * @param schedule
	 * @return
	 */
	private boolean checkTemporalFeasibility(List<ResourceEvent<?>> schedule) {
		// feasibility flag
		boolean feasible = true;
		// list of propagated constraints
		List<BeforeIntervalConstraint> committed = new ArrayList<>();
		
		// check pairs of events 
		for (int index = 0; index < schedule.size() - 1 && feasible; index++)
		{
			try
			{
				// get events
				ResourceEvent<?> e1 = schedule.get(index);
				ResourceEvent<?> e2 = schedule.get(index + 1);
				
				// get associated tokens and temporal intervals to check schedule feasibility
				TemporalInterval i1 = e1.getDecision().getToken().getInterval();
				TemporalInterval i2 = e2.getDecision().getToken().getInterval();
				
				// create precedence constraint "i1 < i2"
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(
						TemporalConstraintType.BEFORE);
				
				// set constraint data
				before.setReference(i1);
				before.setTarget(i2);
				before.setLowerBound(1);							// TODO : check constraint about the minimum distance
				before.setUpperBound(this.tdb.getHorizon());
				
				// propagate constraint
				this.tdb.propagate(before);
				// add constraints to committed
				committed.add(before);
				
				// check temporal feasibility
				this.tdb.verify();
			}
			catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
				// not feasible schedule
				feasible = false;
				// log data
				debug("Component [" + this.label + "] temporally unfeasible schedule:\n"
						+ "- potential schedule critical set: " + schedule + "\n");
			}
			finally 
			{
				// retract all committed constraints
				for (BeforeIntervalConstraint before : committed) {
					// retract temporal constraint
					this.tdb.retract(before);
				}
			}
		}
		
		// get feasibility flag
		return feasible;
		
	}
	
	/**
	 * 
	 * @param overflow
	 */
	protected void findFeasibleSchedule(ReservoirOverflow overflow) {
		// get the critical set
		List<ResourceEvent<?>> cs = overflow.getCriticalSet();
		// shuffle the list to increase the variability of considered schedules
		Collections.shuffle(cs);
		// start looking for a feasible schedule recursively 
		this.doFindFeasibleSchedule(new ArrayList<>(), cs, overflow);
	}
	
	/**
	 * 
	 * @param schedule
	 * @param cs
	 * @param overflow
	 */
	private void doFindFeasibleSchedule(List<ResourceEvent<?>> schedule, List<ResourceEvent<?>> cs, ReservoirOverflow overflow) 
	{
		// check if a schedule is ready
		if (cs.isEmpty()) 
		{
			// check built permutation as possible schedule of the critical set
			debug("Component [" + this.label + "] check feasibility of schedule:\n"
					+ "- scheduled critical set: " + schedule + "\n");
			
			// check schedule resource feasibility first and then temporal feasibility
			if (this.checkCapacityFeasibility(schedule, overflow.getInitialLevel()) && 
					this.checkTemporalFeasibility(schedule))
			{
				// create flaw solution
				ResourceEventSchedule solution = new ResourceEventSchedule(overflow, schedule, this.schedulingCost);
				// add solution to the flaw
				overflow.addSolution(solution);
				// feasible solution found
				debug("Component [" + this.label + "] feasible solution found:\n"
						+ "- overflow: " + overflow + "\n"
						+ "- scheduled critical set: " + schedule + "\n");
			}
		}
		else 
		{
			// check possible schedules until no solution is found 
			for (int index = 0; index < cs.size() && overflow.getSolutions().isEmpty(); index++) 
			{
				// get an event from the critical set
				ResourceEvent<?> ev = cs.remove(index);
				// add the event to the possible schedule
				schedule.add(ev);
				
				// recursively build the permutation
				this.doFindFeasibleSchedule(schedule, cs, overflow);
				
				// remove event from the permutation
				schedule.remove(ev);
				// restore data
				cs.add(index, ev);
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// check flaw type
		switch (solution.getFlaw().getType())
		{
			// check flaw type
			case RESERVOIR_OVERFLOW : 
			{
				// cast flaw solution
				ResourceEventSchedule schedule = (ResourceEventSchedule) solution;
				// get events
				List<ResourceEvent<?>> events = schedule.getSchedule();
				try
				{
					// create relation between associated decisions
					for (int index = 0; index < events.size() - 1; index++)
					{
						// get decisions
						Decision reference = events.get(index).getDecision();
						Decision target = events.get(index + 1).getDecision();
						
						// create relation
						BeforeRelation before = this.component.create(
								RelationType.BEFORE, reference, target);
						
						// set relation bounds
						before.setBound(new long[] {
								1, 
								this.tdb.getHorizon()});
						
						// add created relation
						solution.addCreatedRelation(before);
						debug("Applying flaw solution:\n"
								+ "- solution: " + solution + "\n"
								+ "- created temporal constraint: " + before + "\n");
						
						// propagate relations
						this.component.activate(before);
						// add activated relations to solution
						solution.addActivatedRelation(before);
					}
				}
				catch (RelationPropagationException ex) 
				{
					// failure while applying solution
					debug("Error while applying flaw solution:\n"
							+ "- solution: " + solution + "\n"
							+ "- message: " + ex.getMessage() + "\n");
					
					
					// deactivate created relation
					for (Relation rel : solution.getActivatedRelations()) {
						// get reference
						DomainComponent refComp = rel.getReference().getComponent();
						refComp.deactivate(rel);
					}
					
					// delete created relations
					for (Relation rel : solution.getCreatedRelations()) {
						// get reference component
						DomainComponent refComp = rel.getReference().getComponent();
						// delete relation from component
						refComp.delete(rel);
					}
					
					// throw exception
					throw new FlawSolutionApplicationException("Error while applying flaw solution:\n"
							+ "- solution: " + solution + "\n"
							+ "- message: " + ex.getMessage() + "\n");
				}
			}
			break;
			
			default : {
				throw new RuntimeException("Resolver [" + this.getClass().getSimpleName() +"] cannot handle flaws of type: " + solution.getFlaw().getType());
			}
		}
	}

	
	
	/**
	 * Analyze the profile of a reservoir resource in order to find peaks and compute production checkpoints
	 * 
	 * @param profile
	 * @return
	 */
	private List<Flaw> doComputeProfileOverflows(ReservoirResourceProfile profile)
	{
		// list of flaws found
		List<Flaw> flaws = new ArrayList<>();
		// get profile samples
		List<ResourceUsageProfileSample> samples = profile.getSamples();
		// long start peak level
		long startPeakLevel = 0;
		// reset the current level of resource
		long currentLevel = this.component.getInitialLevel();
		// set minimum and maximum level of resource within the critical set
		long minCriticalSetLevel = Long.MAX_VALUE - 1;
		long maxCriticalSetLevel = Long.MIN_VALUE + 1;
		// set of consumptions that may generate a peak
		List<ResourceEvent<?>> criticalSet = new ArrayList<>();
		// peak mode flag
		boolean peakMode = false;
		// analyze the resource profile until a peak is found
		for (int index = 0; index < samples.size() && flaws.isEmpty(); index++)
		{
			// current sample
			ResourceUsageProfileSample sample = samples.get(index);
			// get resource event
			ResourceEvent<?> event = sample.getEvent();
			
			// check peak mode
			if (!peakMode)
			{
				
				// update the start peak level
				startPeakLevel = currentLevel;
				
				// update the current level of the resource
				currentLevel += event.getAmount();					// positive amount in case of production, negative in case of consumption
				// check resource peak condition
				peakMode = currentLevel < this.component.getMinCapacity() || currentLevel > this.component.getMaxCapacity();
				
				// check if a peak is starting 
				if (peakMode) {
					// first event of the peak
					criticalSet.add(event);
					
					// update minimum and maximum level of resource within the critical set
					minCriticalSetLevel = Math.min(minCriticalSetLevel, currentLevel);
					maxCriticalSetLevel = Math.max(maxCriticalSetLevel, currentLevel);
				}
			}
			else		// peak mode  
			{
				// add the current event to the critical set
				criticalSet.add(event);
				// get current level of the resource
				currentLevel += event.getAmount();				// positive amount in case of production, negative in case of consumption
				
				// update minimum and maximum level of resource within the critical set
				minCriticalSetLevel = Math.min(minCriticalSetLevel, currentLevel);
				maxCriticalSetLevel = Math.max(maxCriticalSetLevel, currentLevel);
				
				// check peak condition
				peakMode = currentLevel < this.component.getMinCapacity() || currentLevel > this.component.getMaxCapacity();				
				
				// check if exit from peak condition
				if (!peakMode) 
				{
					// check over production
					if (maxCriticalSetLevel > this.component.getMaxCapacity()) 
					{
						// get the maximum (positive) amount of over production
						double delta = maxCriticalSetLevel - this.component.getMaxCapacity();
						
						// create reservoir overflow flaw
						ReservoirOverflow overflow = new ReservoirOverflow(
								FLAW_COUNTER.getAndIncrement(), 
								this.component, 
								criticalSet, 
								startPeakLevel,
								delta);
						
						// add flaw and stop searching 
						flaws.add(overflow);
						
					}
					
					// check over consumption
					if (minCriticalSetLevel < this.component.getMinCapacity())
					{
						// get (negative) amount of over consumption
						double delta = minCriticalSetLevel - this.component.getMinCapacity();
						
						// create reservoir overflow flaw
						ReservoirOverflow overflow = new ReservoirOverflow(
								FLAW_COUNTER.getAndIncrement(), 
								this.component, 
								criticalSet, 
								startPeakLevel,
								delta);
						
						// add flaw and stop searching 
						flaws.add(overflow);
					}
				}
			}
		}
		
		
		// check if a peak must be closed - "final peak"
		if (peakMode && flaws.isEmpty())	 
		{
			// check over production
			if (maxCriticalSetLevel > this.component.getMaxCapacity()) 
			{
				// get the maximum (positive) amount of over production
				double delta = maxCriticalSetLevel - this.component.getMaxCapacity();
				
				// create reservoir overflow flaw
				ReservoirOverflow overflow = new ReservoirOverflow(
						FLAW_COUNTER.getAndIncrement(), 
						this.component, 
						criticalSet, 
						startPeakLevel,
						delta);
				
				// add flaw and stop searching 
				flaws.add(overflow);
				
			}
			
			// check over consumption
			if (minCriticalSetLevel < this.component.getMinCapacity())
			{
				// get (negative) amount of over consumption
				double delta = minCriticalSetLevel - this.component.getMinCapacity();
				
				// create reservoir overflow flaw
				ReservoirOverflow overflow = new ReservoirOverflow(
						FLAW_COUNTER.getAndIncrement(), 
						this.component, 
						criticalSet, 
						startPeakLevel,
						delta);
				
				// add flaw and stop searching 
				flaws.add(overflow);
			}
			
		}
		
		// get found peaks - only one element expected
		return flaws;
	}
}


