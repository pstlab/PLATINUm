package it.uniroma3.epsl2.framework.microkernel.resolver.sv.gap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.Token;
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
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableGapResolver extends Resolver<StateVariable> implements Comparator<Decision> 
{
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
		// get start times
		TimePoint s1 = d1.getToken().getInterval().getStartTime();
		TimePoint s2 = d2.getToken().getInterval().getStartTime();
		// compare start times w.r.t. lower and upper bounds
		return s1.getLowerBound() < s2.getLowerBound() ? -1 :
			s1.getLowerBound() == s2.getLowerBound() && s1.getUpperBound() <= s2.getUpperBound() ? -1 : 1;
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
				MeetsRelation meets = this.component.createRelation(RelationType.MEETS, reference, target);
				// propagate relation
				this.component.addRelation(meets);
				committed.add(meets);
				// add added relation
				solution.addAddedRelation(meets);
				
				// check if parameter constraints must be added
				try 
				{
					// create parameter relations
					List<Relation> pRels = this.createParameterRelations(reference, target);
					// propagate relation
					this.component.addRelations(pRels);
					committed.addAll(pRels);
					// add relation to solution
					solution.addAddedRelations(pRels);
				}
				catch (TransitionNotFoundException ex) {
					this.logger.error(ex.getMessage());
				}
			}
			catch (RelationPropagationException ex) {
				// retract committed relations if needed
				for (Relation cr : committed) {
					// free added relation
					this.component.free(cr);
				}
				
				// throw exception
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
				Decision dec = this.component.createDecision(value, labels);
				transition.add(dec);
				// add pending decision
				solution.addCreatedDecision(dec);
			}
			
			// add the gap-right decision
			transition.add(completion.getRightDecision());
			
			// prepare relations
			for (int index = 0; index <= transition.size() - 2; index++) {
				
				// get adjacent decisions
				Decision reference = transition.get(index);
				Decision target = transition.get(index + 1);
				
				// create pending relation
				MeetsRelation meets = this.component.createRelation(RelationType.MEETS, reference, target);
				solution.addCreatedRelation(meets);
				
				try {
					
					// create parameter relations
					List<Relation> pRels = this.createParameterRelations(reference, target);
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
	 * @return
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of gaps
		List<Flaw> flaws = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> decs = this.component.getActiveDecisions();
		
		// sort decisions
		Collections.sort(decs, this);
		// hypothesis for gap detection
		boolean scheduled = true;
		// look for gaps
		for (int index = 0; index <= decs.size() - 2 && scheduled; index++) 
		{
			// get two adjacent decisions
			Decision left = decs.get(index);
			Decision right = decs.get(index + 1);
			
			// check distance between related temporal intervals
			CheckIntervalDistanceQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.CHECK_INTERVAL_DISTANCE);
			
			// set intervals
			query.setSource(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// get result
			long dmin = query.getDistanceLowerBound();
			long dmax = query.getDistanceUpperBound();
			
			// check distance bound
			if (dmin < 0 && dmax <= 0) 
			{
				// we've got an inverted gap
				Gap gap = new Gap(this.component, right, left, new long[] {
						-dmax, -dmin
				});
				// add the gap
				flaws.add(gap);
				this.logger.error("(Inverted) Gap found on [dmin= " + -dmax + ", dmax= " + -dmin + "] " + this.component + " between:\n- " + right + "\n- " + left);
			}
			else if (dmin >= 0 && dmax > 0) 
			{
				// we've got a gap
				Gap gap = new Gap(this.component, left, right, new long[] {dmin, dmax});
				// add the gap
				flaws.add(gap);
				this.logger.debug("Gap found on [dmin= " + dmin + ", dmax= " + dmax + "] " + this.component + " between:\n- " + left + "\n- " + right);
			}
			else if (dmin == 0 && dmax == 0) 
			{
				// ensure that adjacent tokens of the time-line are connected each other according to the time-line semantic
				boolean connected = false;
				Iterator<Relation> it = this.component.getExistingRelations(left, right).iterator();
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
			else 
			{
				// set scheduling flag to false
				scheduled = false;
				// clear gaps detected till now
				flaws = new ArrayList<>();
				// not scheduled decisions
				this.logger.info("Not scheduled decisions [" + dmin +  ", " + dmax + "] gaps cannot be detected:\n- " + left + "\n- " + right);
			}
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
		switch (gap.getGapType()) {
		
			// incomplete time-line
			case INCOMPLETE_TIMELINE : {
				
				// get gap's tokens
				Token left = gap.getLeftDecision().getToken();
				Token right = gap.getRightDecision().getToken();
				// get available paths
				for (List<ComponentValue> path : this.component.
						getPaths(left.getPredicate().getValue(), right.getPredicate().getValue())) {
					
					// remove the source and destination values from the path
					path.remove(path.size() - 1);
					path.remove(0);
					
					// estimate path duration
					long pathDmin = 0;
					long pathDmax = 0;
					for (ComponentValue pathValue : path) { 
						pathDmin += pathValue.getDurationLowerBound();
						pathDmax += pathValue.getDurationUpperBound();
					}
					
					// check feasibility of the path
					if (gap.getDmin() <= pathDmin || gap.getDmax() <= pathDmax) { 
						// feasible solution
						GapCompletion sol = new GapCompletion(gap, path);
						// add solution to the flaw
						gap.addSolution(sol);
					}
					else {
						// path too long to be a feasible solution for the gap
						this.logger.warning("The found path is too long to be a feasible solution for the gap "
								+ "[dmin= " + gap.getDmin() + ", dmax= " + gap.getDmax() + "]:\n"
								+ "- left= " + gap.getLeftDecision() + "\n"
								+ "- right= " + gap.getRightDecision() + "\n"
								+ "- path[dmin= " + pathDmin + ",dmax= " + pathDmax + "]= " + path);
					}
				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : {
				
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
	private List<Relation> createParameterRelations(Decision reference, Decision target) 
			throws TransitionNotFoundException {
		
		// relations
		List<Relation> rels = new ArrayList<>();
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
							EqualParameterRelation equal = (EqualParameterRelation) this.component.createRelation(RelationType.EQUAL_PARAMETER, reference, target);
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
							NotEqualParameterRelation notEqual = (NotEqualParameterRelation) this.component.createRelation(RelationType.NOT_EQUAL_PARAMETER, reference, target);
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
