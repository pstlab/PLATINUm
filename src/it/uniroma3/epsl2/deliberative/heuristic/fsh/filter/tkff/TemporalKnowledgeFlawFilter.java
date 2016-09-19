package it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.tkff;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.PlanDataBaseObserver;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalKnowledgeFlawFilter extends FlawFilter implements PlanDataBaseObserver
{
	@PlanDataBaseReference
	private PlanDataBase pdb;
	
	private JenaTemporalKnowledgeReasoner reasoner;			// knowledge reasoner
	
	/**
	 * 
	 */
	protected TemporalKnowledgeFlawFilter() {
		super(FlawFilterType.TKFF);
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// subscribe to the plan data base
		this.pdb.subscribe(this);
		// setup knowledge reasoner
		this.reasoner = new JenaTemporalKnowledgeReasoner();
		// setup the knowledge on the initial plan and the initial agenda
		Plan plan = this.pdb.getPlan();
		for (Decision decision : plan.getDecisions()) {
			this.reasoner.add(decision);
		}
		for (Relation rel : plan.getRelations()) {
			this.reasoner.add(rel);
		}
		
		Agenda agenda = this.pdb.getAgenda();
		for (Decision goal : agenda.getGoals()) {
			this.reasoner.add(goal);
		}
		for (Relation rel : agenda.getRelations()) {
			this.reasoner.add(rel);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void propagated(FlawSolution solution) 
	{
		// check added decisions (either active or pending)
		List<Decision> decisions = solution.getCreatedDecisions();
		decisions.addAll(solution.getActivatedDecisisons());
		// add decisions to the knowledge
		for (Decision dec : decisions) {
			this.reasoner.add(dec);
		}
		
		// check added relations (either active or pending)
		List<Relation> relations = solution.getCreatedRelations();
		relations.addAll(solution.getActivatedRelations());			// this could be not necessary
		// add relations to the knowledge
		for (Relation rel : relations) {
			this.reasoner.add(rel);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void retracted(FlawSolution solution) 
	{
		// remove only created relations
		List<Relation> relations = solution.getCreatedRelations();
		// remove relations from the knowledge
		for (Relation rel : relations) {
			this.reasoner.delete(rel);
		}
		
		// remove only created decisions
		List<Decision> decisions = solution.getCreatedDecisions();
		// remove decisions from the knowledge
		for (Decision dec : decisions) {
			this.reasoner.delete(dec);
		}
	}
	
	/**
	 * TODO - IMPLEMENTARE FILTRO DEI FLAW
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		System.out.println("##################################################################################");
		System.out.println("Detected Flaws");
		for (Flaw flaw : flaws) {
			// check type
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) {
				System.out.println("--- flaw= " + flaw);
			}
		}
		System.out.println("---------------------------------------------------------------");
		
		// check inferred information
		System.out.println("The inferred \"ordering\" graph:");
		// the "ordering" graph
		Map<Decision, List<Decision>> graph = this.reasoner.getOrderingGraph();
		for (Decision dec : graph.keySet()) {
			System.out.println("Node: " + dec.getId() + "-" + dec.getValue().getLabel());
			for (Decision tar : graph.get(dec)) {
				System.out.println("\t-> " + tar.getId() + "-" + tar.getValue().getLabel());
			}
		}
		
		System.out.println("##################################################################################");
		// get back the entire set of flaws
		return new HashSet<>(flaws);
	}
}
