package it.istc.pst.platinum.framework.microkernel.resolver.timeline.gap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.TransitionNotFoundException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.Transition;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal.MeetsRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.NotFeasibleGapCompletionException;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.MeetsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;
import it.istc.pst.platinum.framework.time.lang.query.IntervalDistanceQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableGapResolver <T extends StateVariable> extends Resolver 
{
	@ComponentPlaceholder
	protected T component;
	
	/**
	 * 
	 */
	protected StateVariableGapResolver() {
		super(ResolverType.SV_GAP_RESOLVER.getLabel(), 
				ResolverType.SV_GAP_RESOLVER.getFlawType());
	}

	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws RelationPropagationException 
	{
		// get created decisions 
		List<Decision> decisions = solution.getCreatedDecisions();
		// restore created decisions
		for (Decision decision : decisions) {
			// restore decision
			this.component.restore(decision);
		}
		
		// restore created relations
		List<Relation> created = solution.getCreatedRelations();
		// restore created relations
		for (Relation relation : created) {
			// restore relation
			this.component.restore(relation);
		}
		
		// get activated relations
		List<Relation> activated = solution.getActivatedRelations();
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try
		{
			// activate relations
			for (Relation relation : activated) 
			{
				// restore relation
				this.component.restore(relation);
				// activate relation
				this.component.add(relation);
				committed.add(relation);
			}
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation relation : committed) {
				// free relation
				this.component.free(relation);
			}
			
			// remove also created relations and decisions
			for (Relation relation : created) {
				// free relation
				this.component.free(relation);
			}
			
			// remove created decisions
			for (Decision decision : decisions) {
				// move back decision to SILENT set
				this.component.delete(decision);
			}
			
			// exception while restoring a previously applied operator
			throw new RelationPropagationException("Error while resetting flaw solution:\n- " + solution + "\n");
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
		GapCompletion completion = (GapCompletion) solution;
		// check solution path
		if (completion.getPath().isEmpty()) 
		{
			// list of committed relations to retract if needed
			List<Relation> committed = new ArrayList<>();
			try 
			{
				// direct token transition between active decisions
				Decision reference = completion.getLeftDecision();
				Decision target = completion.getRightDecision();
				
				// create constraint
				MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
				// propagate relation
				this.component.add(meets);
				committed.add(meets);
				// add activated relation to solution
				solution.addActivatedRelation(meets);
				
				// check if parameter constraints must be added
				try 
				{
					// create parameter relations
					Set<Relation> pRels = this.createParameterRelations(reference, target);
					// propagate relation
					this.component.add(pRels);
					committed.addAll(pRels);
					// add activated relation to solution
					solution.addActivatedRelations(pRels);
				}
				catch (TransitionNotFoundException ex) {
					this.logger.error(ex.getMessage());
				}
			}
			catch (RelationPropagationException ex) 
			{
				// retract committed relations if needed
				for (Relation relation : committed) {
					// deactivate relation
					this.component.delete(relation);
					// clear memory from relation
					this.component.free(relation);
				}
				
				// not feasible solution
				throw new FlawSolutionApplicationException(ex.getMessage());
			}
		}
		else 
		{
			// create the list of the decisions
			List<Decision> transition = new ArrayList<>();
			// add the gap-left decision
			transition.add(completion.getLeftDecision());
			// create intermediate decisions
			for (ComponentValue value : completion.getPath()) 
			{
				// create parameters' labels
				String[] labels = new String[value.getNumberOfParameterPlaceHolders()];
				for (int index = 0; index < labels.length; index++) {
					labels[index] = "label-" + index;
				}
				
				// create pending decision
				Decision dec = this.component.create(value, labels);
				// these decisions can be set as mandatory expansion
				dec.setMandatoryExpansion();
				transition.add(dec);
				// add pending decision
				solution.addCreatedDecision(dec);
			}
			// add the gap-right decision
			transition.add(completion.getRightDecision());
			
			// prepare relations
			for (int index = 0; index <= transition.size() - 2; index++) 
			{
				// get adjacent decisions
				Decision reference = transition.get(index);
				Decision target = transition.get(index + 1);
				
				// create pending relation
				MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
				solution.addCreatedRelation(meets);
				try 
				{
					// create parameter relations
					Set<Relation> pRels = this.createParameterRelations(reference, target);
					// add relation to solution
					solution.addCreatedRelations(pRels);
				}
				catch (TransitionNotFoundException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// list of activated relations
		List<Relation> activated = solution.getActivatedRelations();
		// completely activated relations
		for (Relation relation : activated) {
			// free created and activated relations
			this.component.free(relation);
		}
		
		// list of all created relations
		List<Relation> created = solution.getCreatedRelations();
		// completely remove created relations
		for (Relation relation : created) {
			// free created relations
			this.component.free(relation);
		}
		
		// list of all pending decisions created
		List<Decision> decisions = solution.getCreatedDecisions();
		for (Decision decision : decisions) {
			// move to SILENT all added pending decisions
			this.component.delete(decision);
		}
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of gaps
		List<Flaw> flaws = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> list = this.component.getActiveDecisions();
		// check scheduled decisions
		boolean isScheduled = true;
		// check gaps between adjacent decisions
		for (int index = 0; index < list.size() - 1 && isScheduled; index++) 
		{
			// get two adjacent decisions
			Decision left = list.get(index);
			Decision right = list.get(index + 1);
			// check distance again between temporal intervals
			IntervalDistanceQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.INTERVAL_DISTANCE);
			// set intervals
			query.setSource(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// get result
			long dmin = query.getDistanceLowerBound();
			long dmax = query.getDistanceUpperBound();
			
			// the precondition is that decisions are schedules. 
			if (dmin == 0 && dmax == 0) 
			{
				// ensure that adjacent tokens of the time-line are connected each other according to the time-line semantic
				boolean connected = false;
				Iterator<Relation> it = this.component.getActiveRelations(left, right).iterator();
				while (it.hasNext() && !connected) {
					// next relation
					Relation rel = it.next();
					// check type
					connected = rel.getType().equals(RelationType.MEETS);
				}
				
				// check if decisions are linked
				if (!connected) 
				{
					// force transition through a MEETS constraint
					Gap gap = new Gap(this.component, left, right);
					// add gap
					flaws.add(gap);
				}
			}
			else if(dmin >= 0 && dmax > 0) 
			{
				// we've got a gap
				Gap gap = new Gap(this.component, left, right, new long[] {dmin, dmax});
				// add gap
				flaws.add(gap);
			}
			else {
				// not scheduled decisions
				isScheduled = false;
			}
		}
		
		// check if scheduled decisions
		if (!isScheduled) {
			// clear all gaps detected
			flaws = new ArrayList<>();
			// not scheduled decisions on component
			this.logger.debug("Not scheduled decisions found on component= " + this.component.getName() + " and therefore no gaps can be dected\n");
		}
		
		// get detected gaps
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// get the gap
		Gap gap = (Gap) flaw;
		// check gap type
		switch (gap.getGapType()) 
		{
			// incomplete time-line
			case INCOMPLETE_TIMELINE : 
			{
				// get gap's tokens
				Token left = gap.getLeftDecision().getToken();
				Token right = gap.getRightDecision().getToken();
				// check all (acyclic) paths among tokens
				List<List<ComponentValue>> paths = this.component.getPaths(left.getPredicate().getValue(), right.getPredicate().getValue());
				// each path represents a feasible solution for the gap 
				for (List<ComponentValue> path : paths) 
				{
					// remove the source and destination values from the path
					path.remove(0);
					path.remove(path.size() - 1);
					// feasible solution
					GapCompletion solution = new GapCompletion(gap, path);
					try
					{
						// check temporal feasibility of solution
						double makespan = this.checkGapCompletionTemporalFeasibility(solution);
						// set the resulting makespan
						solution.setMakespan(makespan);
						// add solution to the flaw
						gap.addSolution(solution);
						// print gap information
						this.logger.debug("Gap found on component " + this.component.getName() + ":\n"
								+ "- distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
								+ "- left-side decision: " + gap.getLeftDecision() + "\n"
								+ "- right-side decision: " + gap.getRightDecision() + "\n"
								+ "- solution path: " + path + "\n"
								+ "- resulting makespan: " + makespan + "\n");
						
					}
					catch (NotFeasibleGapCompletionException ex) {
						// not feasible gap completion found
						this.logger.debug("Not feasible gap completion found:\n"
								+ "- gap: " + gap + "\n"
								+ "- solution: " + path + "\n"
								+ "- message: \"" + ex.getMessage() + "\"\n");
					}
				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : 
			{
				// direct connection between decisions
				GapCompletion solution = new GapCompletion(gap, new ArrayList<ComponentValue>());
				try
				{
					// check solution feasibility
					double makespan = this.checkGapCompletionTemporalFeasibility(solution);
					// set the makespan
					solution.setMakespan(makespan);
					// add gap solution
					gap.addSolution(solution);
				}
				catch (NotFeasibleGapCompletionException ex) {
					this.logger.debug("Not feasible gap completion found:\n"
							+ "- gap: " + gap + "\n"
							+ "- solution: " + solution + "\n"
							+ "- message: \""+ ex.getMessage() + "\"\n");
				}
			}
			break;
		}
		
		// check if solvable
		if (!flaw.isSolvable()) {
			throw new UnsolvableFlawFoundException("Unsolvable gap found on component " + this.component.getName() + ":\n" + flaw);
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 * @throws TransitionNotFoundException
	 */
	private Set<Relation> createParameterRelations(Decision reference, Decision target) 
			throws TransitionNotFoundException 
	{
		// relations
		Set<Relation> rels = new HashSet<>();
		// get transition between values
		Transition t = this.component.getTransition(reference.getValue(), target.getValue());
		
		// reference parameter position index
		int referenceParameterIndex = 0;
		while (referenceParameterIndex < reference.getParameterLabels().length) {
			
			// target parameter position index
			int targetParameterIndex = 0;
			while (targetParameterIndex < target.getParameterLabels().length) {
				
				// check if a parameter constraint exists
				if (t.existsParameterConstraint(referenceParameterIndex, targetParameterIndex)) {
					// get parameter constraint type
					ParameterConstraintType pConsType = t.getParameterConstraintType(referenceParameterIndex, targetParameterIndex);
					
					// add (local) pending parameter constraint
					switch (pConsType) {
					
						// equal parameter constraint
						case EQUAL : {
							
							// create (pending) local relation
							EqualParameterRelation equal = (EqualParameterRelation) this.component.create(RelationType.EQUAL_PARAMETER, reference, target);
							// set reference parameter label
							equal.setReferenceParameterLabel(reference.getParameterLabelByIndex(referenceParameterIndex));
							// set target parameter label
							equal.setTargetParameterLabel(target.getParameterLabelByIndex(targetParameterIndex));
							// add create relation to solution
							rels.add(equal);
						}
						break;
						
						// not equal parameter
						case NOT_EQUAL : {
							
							// create (pending) local relation
							NotEqualParameterRelation notEqual = (NotEqualParameterRelation) this.component.create(RelationType.NOT_EQUAL_PARAMETER, reference, target);
							// set reference parameter label
							notEqual.setReferenceParameterLabel(reference.getParameterLabelByIndex(referenceParameterIndex));
							notEqual.setTargetParameterLabel(target.getParameterLabelByIndex(targetParameterIndex));
							// add create relation to solution
							rels.add(notEqual);
						}
						break;
						
						default : {
							
							/*
							 * TODO: <<<<----- VERIFICARE SE VANNO GESTITI ALTRI TIPI DI VINCOLI
							 */
							
							throw new RuntimeException("Unknown parameter constraint type in state variable transition " + pConsType);
						}
					}
				}
				
				// next target parameter index
				targetParameterIndex++;
			}
			
			// next index
			referenceParameterIndex++;
		}
		
		// get created relations
		return rels;
	}
	
	/**
	 * 
	 * @param completion
	 * @return
	 * @throws NotFeasibleGapCompletionException
	 */
	private double checkGapCompletionTemporalFeasibility(GapCompletion completion) 
			throws NotFeasibleGapCompletionException
	{
		// initialize makespan
		double makespan = Double.MIN_VALUE + 1;
		// list of committed relations
		List<TemporalConstraint> committedConstraints = new ArrayList<>();
		List<TemporalInterval> committedIntervals = new ArrayList<>();
		try
		{
			// check solution path
			if (completion.getPath().isEmpty()) 
			{
				// direct token transition between active decisions
				Decision reference = completion.getLeftDecision();
				Decision target = completion.getRightDecision();
				
				// create temporal constraint
				MeetsIntervalConstraint meets = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
				// set reference 
				meets.setReference(reference.getToken().getInterval());
				meets.setTarget(target.getToken().getInterval());
				
				// propagate constraint and check consistency
				this.tdb.propagate(meets);
				// add to committed constraint
				committedConstraints.add(meets);
				this.tdb.checkConsistency();
				
				// feasible solution, compute the resulting makespan
				ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
				this.tdb.process(query);
				// set the resulting makespan
				makespan = query.getMakespan();
			}
			else 
			{
				// prepare the sequence of temporal intervals
				List<TemporalInterval> intervals = new ArrayList<>();
				// add left interval
				intervals.add(completion.getLeftDecision().getToken().getInterval());
				
				// create intermediate intervals
				for (ComponentValue value : completion.getPath()) 
				{
					// create temporal interval
					TemporalInterval i = this.tdb.createTemporalInterval(value.getDurationBounds(), value.isControllable());
					// add intermediate interval
					intervals.add(i);
					// add committed interval
					committedIntervals.add(i);
					
					/*
					 * TODO: Take into account also parameter constraints/relations
					 */
				}
				
				// add last interval
				intervals.add(completion.getRightDecision().getToken().getInterval());
							
				// try to propagate temporal relations between adjacent intervals
				for (int index = 0; index <= intervals.size() - 2; index++) 
				{
					// get adjacent decisions
					TemporalInterval i = intervals.get(index);
					TemporalInterval j = intervals.get(index + 1);
					// create and activate temporal constraint
					MeetsIntervalConstraint meets = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
					// set reference
					meets.setReference(i);
					meets.setTarget(j);
					
					// propagate temporal constraint
					this.tdb.propagate(meets);
					committedConstraints.add(meets);
					
					/*
					 * TODO: Take into account also parameter constraints/relations
					 */
				}
				
				// check temporal feasibility
				this.tdb.checkConsistency();
				// feasible solution, compute the resulting makespan
				ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
				this.tdb.process(query);
				// set the resulting makespan
				makespan = query.getMakespan();
			}
		}
		catch (TemporalIntervalCreationException | TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// throw exception
			throw new NotFeasibleGapCompletionException(ex.getMessage());
		}
		finally 
		{
			// clear data structure if necessary
			for (TemporalConstraint constraint : committedConstraints) {
				// retract temporal constraint
				this.tdb.retract(constraint);
			}
			
			// clear data structure if necessary
			for (TemporalInterval interval : committedIntervals) {
				// delete temporal intervals and related time points
				this.tdb.deleteTemporalInterval(interval);
			}
		}
		
		// get computed makespan
		return makespan;
	}
}
