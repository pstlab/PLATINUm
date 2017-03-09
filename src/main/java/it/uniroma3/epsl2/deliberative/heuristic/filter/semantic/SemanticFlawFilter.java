package it.uniroma3.epsl2.deliberative.heuristic.filter.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.domain.PlanDataBaseObserver;
import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseEvent;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.resolver.planning.Goal;

/**
 * 
 * @author anacleto
 *
 */
public class SemanticFlawFilter extends FlawFilter implements Runnable, PlanDataBaseObserver
{
	private BlockingQueue<PlanDataBaseEvent> queue;			// event queue
	private Thread process;									// knowledge update process
	private final TemporalSemanticReasoner reasoner;		// temporal reasoner
	
	/**
	 * 
	 */
	protected SemanticFlawFilter() {
		super(FlawFilterType.SFF);
		this.process = new Thread(this);
		this.queue = new LinkedBlockingQueue<>();
		// setup knowledge reasoner
		this.reasoner = new JenaTemporalSemanticReasoner();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// subscribe to the plan data base
		this.pdb.subscribe(this);
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
		
		// start knowledge update process
		this.process.start();
	}
	
	/**
	 * 
	 */
	@Override
	public void run() 
	{
		// running flag
		boolean running = true;
		while (running)
		{
			try
			{
				// wait an event
				PlanDataBaseEvent event = this.queue.take();
				// check type
				switch (event.getType()) 
				{
					case PROPAGATE : {
						// handle event
						this.handlePropagateEvent(event.getSolution());
					}
					break;
					
					case RETRACT : {
						// handle event
						this.handleRetractEvent(event.getSolution());
					}
					break;
				}
				
			}
			catch (InterruptedException ex) {
				// stop running
				running = false;
				System.err.println(ex.getMessage());
			}
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void notify(PlanDataBaseEvent event) 
	{
		try 
		{
			// simply write into the queue
			this.queue.put(event);
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * FIXME 
	 */
	@Override
	public Set<Flaw> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// check type of flaws
		Set<Goal> goals = new HashSet<Goal>();
		for (Flaw flaw : flaws) {
			if (flaw.getType().equals(FlawType.PLAN_REFINEMENT)) {
				// add to goal to flaws to be filtered
				goals.add((Goal) flaw);
			}
		}
		
		// next flaws to filter
		Set<Flaw> toFilter = new HashSet<>();
		// check goals to analyze
		if (!goals.isEmpty()) 
		{
			// list planning goals to analyze
			String str = "Planning goals to analyze:\n";
			for (Goal goal : goals) {
				str += "- " + goal + "\n";
			}
			this.logger.debug(str);
			
			// get the inferred "ordering" graph
			Map<Decision, List<Decision>> graph;
			// mutually access to the reasoner
			synchronized (this.reasoner) {
				// read the graph
				graph = this.reasoner.getOrderingGraph();
				// release lock
				this.reasoner.notifyAll();
			}
			
			
			str = "\n************************************************\n";
			str += "Inferred Ordering Graph:\n";
			for (Decision i : graph.keySet()) {
				str += "\tDecision: " + i.getId() +"-" + i.getComponent().getName() + "." + i.getValue().getLabel() + "\n";
				for (Decision j : graph.get(i)) {
					str += "\t\t-> " + j.getId() + "-" + j.getComponent().getName() + "." + j.getValue().getLabel() + "\n";
				}
			}
			str += "************************************************\n";
			System.out.println(str);
			
			
			// compute the hierarchy
			List<Decision>[] hierarchy = this.computeHierarchy(graph);
			// do filter flaws
			for (int hlevel = 0; hlevel < hierarchy.length && toFilter.isEmpty(); hlevel++)
			{
				// check flaws belonging to the current hierarchical level
				for (Goal goal : goals) {
					// check hierarchy of goal
					if (hierarchy[hlevel].contains(goal.getDecision())) {
						// add flaw to filtered
						toFilter.add(goal);
					}
				}
			}
			
			// print filtered flaws
			str = "Resulting goals after the analysis:\n";
			for (Flaw flaw : toFilter) {
				str += "- " + flaw;
			}
			this.logger.debug(str);
		}
		else {
			// no goals to analyze
			toFilter.addAll(flaws);
			this.logger.debug("No planning goals to analyze");
		}
		
		// get resulting sub-set of flaws
		return toFilter;
	}
	
	/**
	 * Given an acyclic ordering graph, the method builds the 
	 * hierarchy by means of a topological sort algorithm
	 * 
	 * @param graph
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Decision>[] computeHierarchy(Map<Decision, List<Decision>> graph) 
	{
		// invert the graph
		Map<Decision, Set<Decision>> inverted = new HashMap<>();
		for (Decision key : graph.keySet()) 
		{
			// add entry
			if (!inverted.containsKey(key)) {
				inverted.put(key, new HashSet<Decision>());
			}
			// check edges
			for (Decision target : graph.get(key)) 
			{
				// add entry to the inverted graph if needed
				if (!inverted.containsKey(target)) {
					inverted.put(target, new HashSet<Decision>());
				}
				
				// add edge to the inverted graph
				inverted.get(target).add(key);
			}
		}
		
		// compute hierarchy by means of topological sort algorithm on the inverted graph
		List<Decision> S = new ArrayList<>();	// root decisions
		for (Decision dec : inverted.keySet()) 
		{
			// check if root
			if (inverted.get(dec).isEmpty()) {
				S.add(dec);
			}
 		}

		// setup the hierarchy
		List<Decision>[] hierarchy = new ArrayList[inverted.keySet().size()];
		// initialize the hierarchy
		for (int index = 0; index < hierarchy.length; index++) {
			hierarchy[index] = new ArrayList<>();
		}
		
		// top hierarchy level
		int hlevel = 0;
		// start building hierarchy
		while (!S.isEmpty()) 
		{
			// get current root decision
			Decision root = S.remove(0);
			// add decision
			hierarchy[hlevel].add(root);
			// update hierarchy degree
			hlevel++;
			
			// update the graph and look for new roots
			inverted.remove(root);
			for (Decision dec : inverted.keySet()) 
			{
				// remove edge
				if (inverted.get(dec).contains(root)) {
					inverted.get(dec).remove(root);
				}
				
				// check if new root
				if (inverted.get(dec).isEmpty() && !S.contains(dec)) {
					// add root
					S.add(dec);
				}
			}
		}
		
		// get the hierarchy
		return hierarchy;
	}
	
	/**
	 * 
	 * @param solution
	 */
	private void handlePropagateEvent(FlawSolution solution) 
	{
		// mutually access to reasoner
		synchronized(this.reasoner) 
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
			
			// release lock
			this.reasoner.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param solution
	 */
	private void handleRetractEvent(FlawSolution solution) 
	{
		// mutually access to reasoner
		synchronized (this.reasoner)
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
			
			// release lock
			this.reasoner.notifyAll();
		}
	}
}
