package it.istc.pst.platinum.framework.microkernel.resolver.resource.discrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceEvent;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.IntervalOverlapQuery;
import it.istc.pst.platinum.framework.time.tn.TimePoint;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver extends Resolver<DiscreteResource> 
{ 
	private double cost;
	
	/**
	 * 
	 */
	protected DiscreteResourceSchedulingResolver() {
		super(ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getFlawTypes());
	
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		this.cost = Double.parseDouble(properties.getProperty("scheduling-cost"));
	}
	
	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws()
	{
		// list of critical sets found
		List<Flaw> CSs = new ArrayList<>();
		// list of requirement events
		List<RequirementResourceEvent> requirements = this.component.getRequirements();
		// compute "pessimistic resource profiles"
		for (int index = 0; index < requirements.size() - 1; index++)
		{
			// get current requirement event
			RequirementResourceEvent event = requirements.get(index);
			// prepare critical set
			CriticalSet cs = new CriticalSet(FLAW_COUNTER.getAndIncrement(), (DiscreteResource) this.component);
			// add the current decision 
			cs.addRequirementDecision(event);
			// find possibly conflicting events
			for (int jndex = index + 1; jndex < requirements.size(); jndex++)
			{
				// get possibly conflicting event
				RequirementResourceEvent conflicting = requirements.get(jndex);
				// check if events conflict
				debug("Checking possibly conflicting resource requirement events:\n"
						+ "- component: " + this.component + "\n"
						+ "- current critical set : " + cs + "\n"
						+ "- possibly conflicting event: " + conflicting + "\n");
				
				// check if the current critical set can temporally overlap with the conflicting one
				if (this.conflict(cs, conflicting))
				{
					// add the event to the critical set
					cs.addRequirementDecision(conflicting);
					// conflicting decision found
					debug("Conflicting event found:\n"
							+ "- component: " + this.component + "\n"
							+ "- current critical set : " + cs + "\n"
							+ "- possibly conflicting event: " + conflicting + "\n");
				}
			}
			
			// check the amount of requirement of the critical set
			if (cs.getAmountOfRequirement() > this.component.getMaxCapacity()) {
				// add the critical set to the flaws
				CSs.add(cs);
				// a peak has been found
				debug("A discrete resource peak has been found:\n"
						+ "- component: " + this.component + "\n"
						+ "- critical set: " + cs + "\n"
						+ "- amount required: " + cs.getAmountOfRequirement() + "\n");
			}
		}
		
		// get the list of critical sets found
		return CSs;
	}
	
	
	/**
	 * 
	 * @param set
	 * @param event
	 * @return
	 */
	private boolean conflict(CriticalSet set, RequirementResourceEvent event) 
	{
		// conflicting flag
		boolean conflict = true;
		// get events of the critical set
		List<RequirementResourceEvent> events = set.getRequirementEvents();
		// check set of events
		for (int index = 0; index < events.size() && conflict; index++)
		{
			// get an event from the set
			RequirementResourceEvent e = events.get(index);
			// check if current decision and target overlap
			IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
			// set intervals
			query.setReference(e.getEvent());
			query.setTarget(event.getEvent());
			// process query
			this.tdb.process(query);
			// check whether the (flexible) temporal interval can overlap or not
			conflict = query.canOverlap();
		}
		
		// get result
		return conflict;
	}
	
	/**
	 * Å critical set (CS) is not necessary minimal. 
	 * 
	 * This method samples a critical set in order to find all the minimal critical sets (MCSs) available.
	 * 
	 * A minimal critical set is a "sub-peak" which can be solved by posting a precedence constraint between 
	 * any pair of activities.
	 * 
	 * @param cs
	 * @return
	 */
	private List<MinimalCriticalSet> sampleMCSs(CriticalSet cs)
	{
		// list of MCSs that can be extracted from the critical set
		List<MinimalCriticalSet> mcss = new ArrayList<>();
		// get the events composing the critical set 
		List<RequirementResourceEvent> events = cs.getRequirementEvents();
		// sort requirements in decreasing order of resource amount needed
		Collections.sort(events);
		
		// sample MCSs from the CS
		for (int index = 0; index < events.size() -1; index++)  
		{
			// get current event
			RequirementResourceEvent reference = events.get(index);
			// initialize an MCS
			MinimalCriticalSet mcs = new MinimalCriticalSet(cs, this.cost);
			// add the current event to the MCS
			mcs.addEvent(reference);
			
			// check other samples
			for (int jndex = index + 1; jndex < events.size(); jndex++) 
			{
				// get other event
				RequirementResourceEvent other = events.get(jndex);
				// check the resulting amount 
				double amount = mcs.getTotalAmount() + other.getAmount();
				// an MCS is minimal so check the amount of resource required  (minimal condition)
				if (amount > this.component.getMaxCapacity()) 
				{
					// copy current MCS
					MinimalCriticalSet copy = new MinimalCriticalSet(mcs, this.cost);
					// add sample to the MCS
					mcs.addEvent(other);
					// add to the list of MCSs
					mcss.add(mcs);
					debug("Minimal Critical Set sampled:\n"
							+ "- component: " + this.component + "\n"
							+ "- critical set: " + cs + "\n"
							+ "- sampled minimial critical set: " + mcs + "\n");
					
					// go on with the search by using the copy 
					mcs = copy;
				}
				else {
					// simply add the event and go on
					mcs.addEvent(other);
				}
			}
		}
		
		// get sampled MCSs
		return mcss;
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

	
	/**
	 * 
	 * @param mcs
	 * @throws Exception - contains information concerning the unsolvable MCS
	 */
	private void computeMinimalCriticalSetSolutions(MinimalCriticalSet mcs) 
			throws UnsolvableFlawException
	{
		// list of events
		List<RequirementResourceEvent> list = mcs.getEvents();
		// for each pair of decisions check the feasibility of a precedence constraint and compute the resulting preserved heuristic value
		for (int index = 0; index < list.size() - 1; index++)
		{
			// get reference decision
			Decision reference = list.get(index).getDecision();
			for (int jndex = index + 1; jndex < list.size(); jndex++)
			{
				// get target decision
				Decision target = list.get(jndex).getDecision();
				// prepare precedence constraint
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);

				try
				{
					/*
					 *  check feasibility of precedence constraint "reference < target" and compute the resulting preserved heuristic value
					 */
					
					// set reference interval
					before.setReference(reference.getToken().getInterval());
					// set target interval
					before.setTarget(target.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility through constraint propagation
					this.tdb.propagate(before);
					// check temporal consistency
					this.tdb.verify();
					

					// compute the preserved space heuristic value resulting after constraint propagation
					double preserved = this.computePreservedSpaceHeuristicValue(
							reference.getToken().getInterval().getEndTime(), 
							target.getToken().getInterval().getStartTime());
					
					// create and add solution to the MCS
					PrecedenceConstraint pc = mcs.addSolution(reference, target, preserved);
					// print some debugging information
					debug("Feasible solution of MCS found:\n"
							+ "- mcs: " + mcs + "\n"
							+ "- precedence constraint: " + pc + "\n");
				}
				catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
					// warning message
					debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + reference + " < " + target + "\n");
				}
				finally {
					// retract propagated constraint
					this.tdb.retract(before);
					// clear temporal relation
					before.clear();
				}
				
				
				try
				{
					/*
					 *  check feasibility of precedence constraint "target < reference" and compute the resulting preserved heuristic value
					 */
					
					// set reference interval
					before.setReference(target.getToken().getInterval());
					// set target interval
					before.setTarget(reference.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility through constraint propagation
					this.tdb.propagate(before);
					// check temporal consistency
					this.tdb.verify();
					
					
					// compute the preserved space heuristic value resulting after constraint propagation
					double preserved = this.computePreservedSpaceHeuristicValue(
							target.getToken().getInterval().getEndTime(), 
							reference.getToken().getInterval().getStartTime());
					
					// create and add solution to the MCS
					PrecedenceConstraint pc = mcs.addSolution(target, reference, preserved);
					// print some debugging information
					debug("Feasible solution of MCS found:\n"
							+ "- mcs: " + mcs + "\n"
							+ "- precedence constraint: " + pc + "\n");
				}
				catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
					// warning message
					debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + target + " < " + reference + "\n");
				}
				finally {
					// retract (inverted) precedence constraint
					this.tdb.retract(before);
					// clear temporal relation
					before.clear();
				}
			}
		}
		
		// check MCS solutions
		if (mcs.getSolutions().isEmpty()) {
			// unsolvable MCS found
			throw new UnsolvableFlawException("Unsolvable MCS found on discrete resource " + this.component.getName() + "\n- mcs: " + mcs + "\n");
		}
	}
	
	/**
	 * 	Given a set of overlapping activities that exceed the resource capacity (i.e. a peak) this method computes sets of 
	 * precedence constraints among activities composing the critical set (CS) in order to solve the peak 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException 
	{
		// cast flaw
		CriticalSet cs = (CriticalSet) flaw;
		
		/*
		 * A critical set (CS) is not necessary minimal, so sample MCSs from the CS
		 */
		
		// sample the critical set in order to find minimal critical sets
		List<MinimalCriticalSet> mcss = this.sampleMCSs(cs);
		
		/*
		 * An MCS can be solved by posting a precedence constraint between any pair of activities. 
		 * 
		 * Thus, each MCS can have several solutions depending on the number of activities involved.
		 * 
		 * For each MCS compute the set of feasible solutions and the related preserved heuristic value 
		 */
		
		try
		{
			
			// sort MCSs according to the total requirement
			Collections.sort(mcss);	
			// get the "best" MCS 
			MinimalCriticalSet best = mcss.get(0);
			// compute (minimal) critical set solutions 
			this.computeMinimalCriticalSetSolutions(best);
			
			/*
			 * Rate MCSs according to the computed preserved heuristic value and select the best one for expansion
			 */
			
			// add computed solutions to the flow
			for (PrecedenceConstraint pc : best.getSolutions()) {
				// add this precedence constraint as a possible solution of the peak
				flaw.addSolution(pc);
			}
		}
		catch (Exception ex) {
			// unsolvable MCS found
			throw new UnsolvableFlawException("Unsolvable MCS found on discrete resourc e" + this.component.getName() + ":\n" + flaw);
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
		PrecedenceConstraint pc = (PrecedenceConstraint) solution;
		try 
		{
			// get reference and target decisions
			Decision reference = pc.getReference();
			Decision target = pc.getTarget();
			// create relation
			BeforeRelation before = this.component.create(RelationType.BEFORE, reference, target);
			// set bounds
			before.setBound(new long[] {1, this.tdb.getHorizon()});
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
		catch (RelationPropagationException ex) 
		{
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

			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
}

/**
 * 
 * @author anacleto
 *
 */
class MinimalCriticalSet implements Comparable<MinimalCriticalSet>
{
	protected CriticalSet cs;									// the set of overlapping activities
	private Set<RequirementResourceEvent> events;				// activities composing the MCS
	private List<PrecedenceConstraint> solutions;				// a MCS can be solved by posting a simple precedence constraint
	private double cost;
	
	/**
	 * 
	 * @param cs
	 * @param cost
	 */
	protected MinimalCriticalSet(CriticalSet cs, double cost) {
		this.cs = cs;
		this.events = new HashSet<>();
		this.solutions = new ArrayList<>();
		this.cost = cost;
	}
	
	/**
	 * 
	 * @param mcs
	 */
	protected MinimalCriticalSet(MinimalCriticalSet mcs, double cost) {
		this.cs = mcs.cs;
		this.events = new HashSet<>(mcs.events);
		this.solutions = new ArrayList<>(mcs.solutions);
		this.cost = cost;
	}
	
	/**
	 * Get the total amount of resource required
	 * 
	 * @return
	 */
	public double getTotalAmount() {
		double amount = 0;
		for (RequirementResourceEvent event : this.events) {
			amount += event.getAmount();
		}
		// get the computed total
		return amount;
	}
	
	/**
	 * 
	 * @param sample
	 * @return
	 */
	public boolean contains(RequirementResourceEvent event) {
		// check whether the MCS already contains the event 
		return this.events.contains(event);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RequirementResourceEvent> getEvents() {
		return new ArrayList<>(this.events);
	}
	
	/**
	 * 
	 * @param sample
	 */
	public void addEvent(RequirementResourceEvent event) {
		this.events.add(event);
	}
	
	/**
	 * 
	 * @return
	 */
	public CriticalSet getCriticalSet() {
		return cs;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PrecedenceConstraint> getSolutions() {
		return new ArrayList<>(this.solutions);
	}
	
	/**
	 * The preserved value of a MCS is given by the average preserved values of its solutions
	 * 
	 * See [Laborie 2005] for further details about the preserved value heuristics 
	 * 
	 * @return
	 */
	public double getPreservedValue() 
	{
		// initialize preserved value
		double preserved = 0;
		// take into account the preserved values of solutions
		for (PrecedenceConstraint pc : this.solutions) {
			preserved += pc.getPreservedValue();
		}
		
		// get the average value
		preserved = preserved / this.solutions.size();
		// get computed value
		return preserved;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param preserved
	 * @return
	 */
	protected PrecedenceConstraint addSolution(Decision reference, Decision target, double preserved) 
	{
		// create a precedence constraint
		PrecedenceConstraint pc = new PrecedenceConstraint(this.cs, reference, target, this.cost);
		// set the value of resulting preserved space
		pc.setPreservedSpace(preserved);
		// add solution to the original flaw
		this.solutions.add(pc);
		// get constraint
		return pc;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(MinimalCriticalSet o) {
		
		// compare MCSs according to the total amount of resource required
		return this.getTotalAmount() > o.getTotalAmount() ? -1 : this.getTotalAmount() < o.getTotalAmount() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"requirement\": " + this.getTotalAmount() + ", \"events\": " + this.events + " }";
	}
}
