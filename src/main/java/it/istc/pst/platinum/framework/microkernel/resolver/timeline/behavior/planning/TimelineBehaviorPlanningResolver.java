package it.istc.pst.platinum.framework.microkernel.resolver.timeline.behavior.planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.TransitionNotFoundException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.Transition;
import it.istc.pst.platinum.framework.domain.component.sv.ValuePath;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;
import it.istc.pst.platinum.framework.time.lang.query.IntervalDistanceQuery;
import it.istc.pst.platinum.framework.time.lang.query.IntervalOverlapQuery;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public final class TimelineBehaviorPlanningResolver extends Resolver<StateVariable> 
{
	private double cost;
	
	/**
	 * 
	 */
	protected TimelineBehaviorPlanningResolver() {
		super(ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getLabel(), 
				ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getFlawTypes());
		
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		this.cost = Double.parseDouble(properties.getProperty("completion-cost"));
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get the flaw solution to apply
		GapCompletion completion = (GapCompletion) solution;
		// check solution path
		if (completion.getPath().isEmpty()) 
		{
			try 
			{
				// direct token transition between active decisions
				Decision reference = completion.getLeftDecision();
				Decision target = completion.getRightDecision();
				
				// create meets constraint to enforce the desired transition
				MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
				// add created relation
				solution.addCreatedRelation(meets);
				// propagate relation
				this.component.activate(meets);
				// add activated relation to solution
				solution.addActivatedRelation(meets);
				
				
				// create parameter relations
				Set<Relation> pRels = this.createParameterRelations(reference, target);
				// add created relation
				solution.addCreatedRelations(pRels);
				// propagate relation
				this.component.activate(pRels);
				// add activated relation to solution
				solution.addActivatedRelations(pRels);
			}
			catch (TransitionNotFoundException | RelationPropagationException ex) 
			{
				// deactivate activated relations
				for (Relation rel : solution.getActivatedRelations()) {
					// get reference component
					DomainComponent refComp = rel.getReference().getComponent();
					refComp.deactivate(rel);
				}
				
				// delete created relations
				for (Relation rel : solution.getCreatedRelations()) {
					// get reference component
					DomainComponent refComp = rel.getReference().getComponent();
					// delete relation
					refComp.delete(rel);
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
			try
			{
				// create intermediate decisions associated to the transition to be enforced 
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
					// add created decision to transition
					transition.add(dec);
					// add pending decision
					solution.addCreatedDecision(dec);
				}
				
				
				// add the gap-right decision
				transition.add(completion.getRightDecision());
				
				// create relations needed to enforce the transition
				for (int index = 0; index < transition.size() - 1; index++) 
				{
					// get adjacent decisions
					Decision reference = transition.get(index);
					Decision target = transition.get(index + 1);
					
					// create pending relation
					MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
					// add created relation
					solution.addCreatedRelation(meets);

					// create parameter relations
					Set<Relation> pRels = this.createParameterRelations(reference, target);
					for (Relation prel : pRels) {
						// add relation to solution
						solution.addCreatedRelation(prel);
						// check i
					}
				}
			}
			catch (TransitionNotFoundException ex) {
				// deactivate activated relations
				for (Relation rel : solution.getActivatedRelations()) {
					// get reference component
					DomainComponent refComp = rel.getReference().getComponent();
					refComp.deactivate(rel);
				}
				
				// delete created relations
				for (Relation rel : solution.getCreatedRelations()) {
					// get reference component
					DomainComponent refComp = rel.getReference().getComponent();
					refComp.delete(rel);
				} 
				
				// deactivate activated decisions
				for (Decision dec : solution.getActivatedDecisions()) {
					// get component
					DomainComponent dComp = dec.getComponent();
					dComp.deactivate(dec);
				}
				
				// delete created decisions 
				for (Decision dec : solution.getCreatedDecisions()) {
					// get component
					DomainComponent dComp = dec.getComponent();
					dComp.free(dec);
				} 
				
				// throw exception
				throw new FlawSolutionApplicationException(ex.getMessage());
			}
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
		
		// check gaps between adjacent decisions
		for (int index = 0; index < list.size() - 1; index++) 
		{
			// get two adjacent decisions
			Decision left = list.get(index);
			Decision right = list.get(index + 1);
			
			// check if scheduled
			IntervalOverlapQuery query = this.tdb.createTemporalQuery(
					TemporalQueryType.INTERVAL_OVERLAP);
			// set time points
			query.setReference(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			
			// process query
			this.tdb.process(query);
			// check if they intervals can overlap
			if (query.canOverlap())
			{
				// precondition not satisfied
				debug("No timeline behavior flaw can be detected as tokens are not scheduled:\n"
						+ "- component: " + this.component + "\n"
						+ "- [reason] Behaviors cannot be plant due to potentially overlapping tokens:\n"
						+ "\t- reference: " + left + "\n"
						+ "\t- target: " + right + "\n");
				
				// clear data structures and stop inspecting this component for this type of flaws
				flaws = new ArrayList<>();
				break;
			}
			else 
			{
				// check if scheduled
				IntervalDistanceQuery distance = this.tdb.createTemporalQuery(
						TemporalQueryType.INTERVAL_DISTANCE);
				// set time points
				distance.setReference(left.getToken().getInterval());
				distance.setTarget(right.getToken().getInterval());
				// process query
				this.tdb.process(distance);
				
				// check the distance between the two decisions 
				if (distance.getDistanceLowerBound() >= 0 && distance.getDistanceUpperBound() > 0) 
				{
					// a gap has been found on the timeline 
					Gap gap = new Gap(FLAW_COUNTER.getAndIncrement(),
							this.component, 
							left, 
							right, 
							new long[] {
									distance.getDistanceLowerBound(), 
									distance.getDistanceUpperBound()});
					
					// add gap
					flaws.add(gap);
					debug("Gap found on component: "
							+ "- component: " + this.component + "\n"
							+ "- reference: " + left + "\n"
							+ "- target: " + right + "\n"
							+ "- distance: [" + distance.getDistanceLowerBound() + ", " + distance.getDistanceUpperBound() + "]\n");
				}
			}
		}
		
		
		// get flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException 
	{
		// get the gap
		Gap gap = (Gap) flaw;
		// check gap type
		switch (gap.getGapType()) 
		{
			// incomplete time-line
			case INCOMPLETE_TIMELINE : 
			{
				// check all (acyclic) paths among tokens
				List<ValuePath> paths = this.component.getPaths(
						gap.getLeftDecision().getValue(), 
						gap.getRightDecision().getValue());
				
				// check found solutions
				if (paths.isEmpty()) {
					// not gap completion found between the two tokens
					throw new UnsolvableFlawException("Not gap completion found:\n"
							+ "- gap: " + gap + "\n");
				}
				else 
				{
					// compute a solution for each possible value path
					for (ValuePath path : paths) 
					{
						// get steps
						List<ComponentValue> steps = path.getSteps();
						// remove the source and destination values from the path
						steps.remove(0);
						steps.remove(steps.size() - 1);
						
						
						// compute path duration bounds
						long tranMinDuration = 0;
						long tranMaxDuration = 0;
						// compute transition duration
						for (ComponentValue step : steps) {
							// increment min and maximum duration of the transition
							tranMinDuration += step.getDurationLowerBound();
							tranMaxDuration += step.getDurationUpperBound();
						}
						
						// check the feasibility of the path with respect to the available time 
							if (tranMinDuration >= gap.getDmin() && 
									tranMinDuration <= gap.getDmax())
						{
							// gap solution
							GapCompletion solution = new GapCompletion(gap, steps, this.cost);
							// add solution to the flaw
							gap.addSolution(solution);
							// print gap information
							debug("Gap found on component " + this.component.getName() + ":\n"
									+ "- gap distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
									+ "- left-side decision: " + gap.getLeftDecision() + "\n"
									+ "- right-side decision: " + gap.getRightDecision() + "\n"
									+ "- solution path: " + path + "\n");
						}
						else {
							// discard this path as not temporally feasible
							debug("Unfeasible transition path for timelime behavior completion:\n"
									+ "- gap distance: [dmin= " + gap.getDmin() +", " + gap.getDmax() + "]\n"
									+ "- path: " + path + "\n"
									+ "- path duration: [dmin= " + tranMinDuration + ", " + tranMaxDuration + "]");
						}
					}
				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : 
			{
				// direct connection between decisions
				GapCompletion solution = new GapCompletion(gap, new ArrayList<ComponentValue>(), this.cost);
				// add gap solution
				gap.addSolution(solution);
			}
			break;
		}
		
		// check if solvable
		if (!gap.isSolvable()) {
			throw new UnsolvableFlawException("Unsolvable gap found on component " + this.component.getName() + ":"
					+ "\n- gap: " + flaw + "\n");
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
}
