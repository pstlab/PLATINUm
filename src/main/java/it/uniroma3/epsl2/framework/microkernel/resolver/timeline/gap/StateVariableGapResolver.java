package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.gap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.TransitionNotFoundException;
import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.Transition;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.EqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.MeetsRelation;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalDistanceQuery;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalOverlapQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableGapResolver <T extends StateVariable> extends Resolver implements Comparator<Decision> 
{
	@ComponentReference
	protected T component;
	
	/**
	 * 
	 */
	protected StateVariableGapResolver() {
		super(ResolverType.SV_GAP_RESOLVER);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Decision d1, Decision d2) 
	{
		// check distance again between temporal intervals
		IntervalDistanceQuery query = this.tdb.
				createTemporalQuery(TemporalQueryType.INTERVAL_DISTANCE);
		// set intervals
		query.setSource(d1.getToken().getInterval());
		query.setTarget(d2.getToken().getInterval());
		// process query
		this.tdb.process(query);
		// get result
		long dmin = query.getDistanceLowerBound();
		long dmax = query.getDistanceUpperBound();
		
		// check temporal ordering
		if (dmin >= 0 && dmax >= 0) { return -1;}
		else if (dmin < 0 && dmax < 0) { return 1; }
		else { throw new RuntimeException("Unknown ordering of decisions\n"
				+ "- distance= [" + dmin + ", " + dmax + "]\n"
				+ "- d1= " + d1 + "" + d1.getToken() +"\n"
				+ "- d2= " + d2 + "" + d2.getToken() + "\n"); } 
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
				for (Relation cr : committed) {
					// deactivate relation
					this.component.delete(cr);
					// clear memory from relation
					this.component.free(cr);
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
	 * @return
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of gaps
		List<Flaw> flaws = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> list = this.component.getActiveDecisions();
		this.logger.debug("Check timeline gaps on component= " + this.component.getName() + "\n"
				+ "- " + list + "\n");
		// check scheduled decisions
		boolean isScheduled = true;
		for (int index = 0; index < list.size() - 1 && isScheduled; index++) 
		{
			// get decision A
			Decision a = list.get(index);
			for (int jndex = index +1; jndex < list.size() && isScheduled; jndex++)
			{
				// get a decision B
				Decision b = list.get(jndex);
				// check overlapping intervals
				IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
				// set intervals
				query.setReference(a.getToken().getInterval());
				query.setTarget(b.getToken().getInterval());
				// process query
				this.tdb.process(query);
				// check if scheduled
				isScheduled = !query.isOverlapping();
			}
		}
		
		// if scheduled decisions look for gaps
		if (isScheduled) 
		{
			// sort decisions
			Collections.sort(list, this);
			// look for gaps
			for (int index = 0; index < list.size() - 1; index++) 
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
				
				// check gap
				if (dmin >= 0 && dmax > 0) 
				{
					// we've got a gap
					Gap gap = new Gap(this.component, left, right, new long[] {dmin, dmax});
					// add the gap
					flaws.add(gap);
					this.logger.debug("Gap found on component= " + this.component.getName() + ":\n-distance= [dmin= " + dmin + ", dmax= " + dmax + "]\n- left-decision= " + left + "\n- right-deicision= " + right + "\n");
				}
				else if (dmin == 0 && dmax == 0) 
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
					if (!connected) {
						// create semantic compliant flaw
						this.logger.debug("Ensure timeline semantics on " + this.component + " between:\n- " + left + "\n- " + right);
						Gap gap = new Gap(this.component, left, right);
						flaws.add(gap);
					}
				}
			}
		}
		else {
			// not scheduled decisions on component
			this.logger.debug("No gaps can be detected\nNot scheduled decisions on component= " + this.component.getName() + "\n");
			// clear gaps detected till now
			flaws = new ArrayList<>();
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
				// get available paths
				for (List<ComponentValue> path : this.component.
						getPaths(left.getPredicate().getValue(), right.getPredicate().getValue())) 
				{
					// remove the source and destination values from the path
					path.remove(path.size() - 1);
					path.remove(0);
					// feasible solution
					GapCompletion sol = new GapCompletion(gap, path);
					// add solution to the flaw
					gap.addSolution(sol);
				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : 
			{
				// direct connection between decisions
				GapCompletion sol = new GapCompletion(gap, new ArrayList<ComponentValue>());
				gap.addSolution(sol);
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
}
