package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.TransitionNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.Transition;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.ValuePath;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.Resolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalOverlapQuery;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public final class TimelineBehaviorPlanningResolver extends Resolver<StateVariable> 
{
	private boolean load;
	private double cost;
	
	/**
	 * 
	 */
	protected TimelineBehaviorPlanningResolver() {
		super(ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getLabel(), 
				ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getFlawTypes());
		// set load flag
		this.load = false;
	}
	
	/**
	 * 
	 */
	private void load() {
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		// get weight
		this.cost = Double.parseDouble(properties.getProperty("completion-cost"));
		// set flag
		this.load = true;
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
				if (this.component.activate(meets)) {
					// add activated relation to solution
					solution.addActivatedRelation(meets);
				}
				
				// create parameter relations
				Set<Relation> pRels = this.createParameterRelations(reference, target);
				// add created relation
				solution.addCreatedRelations(pRels);
				// propagate relation
				for (Relation pRel : pRels) {
					// activate relation if possible
					if (this.component.activate(pRel)) {
						// add activated relation to solution
						solution.addActivatedRelation(pRel);
					}
				}
				
				// check feasibility
				this.tdb.verify();
				this.pdb.verify();
			}
			catch (TransitionNotFoundException | RelationPropagationException | ConsistencyCheckException ex) 
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
			try {
				
				// intermediate values and related relations can be activated since no synchronization is entailed
				for (ComponentValue value : completion.getPath()) 
				{
					// create parameters' labels
					String[] labels = new String[value.getNumberOfParameterPlaceHolders()];
					for (int index = 0; index < labels.length; index++) {
						// set parameter label
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

					// activate the decision if possible
					if (!value.isComplex()) {
						
						// activated decision
						Set<Relation> list = this.component.activate(dec);
						// add activated decisions
						solution.addActivatedDecision(dec);
						// add activated relations
						solution.addActivatedRelations(list);
					}
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
					// activate relation if possible
					if (this.component.activate(meets)) {
						// add to activated relations
						solution.addActivatedRelation(meets);
					}
						
					// create parameter relations
					Set<Relation> pRels = this.createParameterRelations(reference, target);
					// check relations
					for (Relation prel : pRels) {
						// add relation to solution
						solution.addCreatedRelation(prel);
						// activate relation if possible
						if (this.component.activate(prel)) {
							// add to activated relations
							solution.addActivatedRelation(prel);
						}
					}
				}
				
				// check consistency
				this.tdb.verify();
				this.pdb.verify();
			}
			catch (Exception ex) {
			
				// deactivate relations
				for (Relation rel : solution.getActivatedRelations()) {
					this.component.deactivate(rel);
				}
				
				// delete created relations
				for (Relation rel : solution.getCreatedRelations()) {
					this.component.delete(rel);
				}
				
				// deactivate decisions
				for (Decision dec : solution.getActivatedDecisions()) {
					this.component.deactivate(dec);
				}
				
				// free created decisions
				for (Decision dec : solution.getCreatedDecisions()) {
					this.component.free(dec);
				}
				
				// throw exception
				throw new FlawSolutionApplicationException("Error while applying flaw solution:\n"
						+ "- behavior-completion: " + completion + "\n"
						+ "- message: " + ex.getMessage() + "\n");
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
		// load flat
		if (!this.load) {
			this.load();
		}
				
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
						// gap solution
						GapCompletion solution = new GapCompletion(gap, steps, this.cost);
						
						// list of created and activated decisions
						List<Decision> dCreated = new ArrayList<>();
						List<Decision> dActivated = new ArrayList<>();
						// list of created and activated relations
						List<Relation> rCreated = new ArrayList<>();
						List<Relation> rActivated = new ArrayList<>();
						
						try 
						{
							// check solution feasibility
							if (solution.getPath().isEmpty()) 
							{
								// direct token transition between active decisions
								Decision reference = solution.getLeftDecision();
								Decision target = solution.getRightDecision();
								
								// create meets constraint to enforce the desired transition
								MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
								// add created relation
								rCreated.add(meets);
								// propagate relation and activate it if possible
								if (this.component.activate(meets)) {
									// add activated relation to solution
									rActivated.add(meets);
								}
								
								// create parameter relations
								Set<Relation> pRels = this.createParameterRelations(reference, target);
								// add created relation
								rCreated.add(meets);
								// propagate relation
								for (Relation pRel : pRels) {
									// activate relation if possible
									if (this.component.activate(pRel)) {
										// add activated relation to solution
										rActivated.add(pRel);
									}
								}
							}
							else 
							{
								// create the list of the decisions
								List<Decision> transition = new ArrayList<>();
								// add the gap-left decision
								transition.add(solution.getLeftDecision());
									
								// intermediate values and related relations can be activated since no synchronization is entailed
								for (ComponentValue value : solution.getPath()) 
								{
									// create parameters' labels
									String[] labels = new String[value.getNumberOfParameterPlaceHolders()];
									for (int index = 0; index < labels.length; index++) {
										// set parameter label
										labels[index] = "label-" + index;
									}
									
									// create pending decision
									Decision dec = this.component.create(value, labels);
									// these decisions can be set as mandatory expansion
									dec.setMandatoryExpansion();
									// add created decision to transition
									transition.add(dec);
									// add pending decision
									dCreated.add(dec);

									// activate the decision if possible
									if (!value.isComplex()) {
										
										// activated decision
										Set<Relation> list = this.component.activate(dec);
										// add activated decisions
										dActivated.add(dec);
										// add activated relations
										rActivated.addAll(list);
									}
								}
									
								// add the gap-right decision
								transition.add(solution.getRightDecision());
								
								// create relations needed to enforce the transition
								for (int index = 0; index < transition.size() - 1; index++) 
								{
									// get adjacent decisions
									Decision reference = transition.get(index);
									Decision target = transition.get(index + 1);
									
									// create pending relation
									MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
									// add created relation
									rCreated.add(meets);
									// activate relation if possible
									if (this.component.activate(meets)) {
										// add to activated relations
										rActivated.add(meets);
									}
									
									// create parameter relations
									Set<Relation> pRels = this.createParameterRelations(reference, target);
									// check relations
									for (Relation prel : pRels) {
										// add relation to solution
										rCreated.add(prel);
										// activate relation if possible
										if (this.component.activate(prel)) {
											// add to activated relations
											rCreated.add(prel);
										}
									}
								}
							}

							// check feasibility
							this.tdb.verify();
							this.pdb.verify();
							
							// add solution to the flaw since everything is feasible
							gap.addSolution(solution);
							// print gap information
							debug("Gap found on component " + this.component.getName() + ":\n"
									+ "- gap distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
									+ "- left-side decision: " + gap.getLeftDecision() + "\n"
									+ "- right-side decision: " + gap.getRightDecision() + "\n"
									+ "- solution path: " + path + "\n");
						}
						catch (RelationPropagationException | TransitionNotFoundException | DecisionPropagationException |  ConsistencyCheckException ex) {
							// discard this path as not temporally feasible
							warning("Unfeasible transition path for timelime behavior completion:\n"
									+ "- gap distance: [dmin= " + gap.getDmin() +", " + gap.getDmax() + "]\n"
									+ "- path: " + path + "\n");
//										+ "- path duration: [dmin= " + tranMinDuration + ", " + tranMaxDuration + "]");
						}
						finally {
							
							// deactivate relations
							for (Relation rel : rActivated) {
								this.component.deactivate(rel);
							}
							
							// delete relation
							for (Relation rel : rCreated) {
								this.component.delete(rel);
							}
							
							// deactivate decision
							for (Decision dec : dActivated) {
								this.component.deactivate(dec);
							}
							
							// free and then delete decision
							for (Decision dec : dCreated) {
								// free decision
								this.component.free(dec);
								// completely remove decision reference
								this.component.delete(dec);
							}
						}
					}	
				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : {
				
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
