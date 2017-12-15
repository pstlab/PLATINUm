package it.istc.pst.platinum.deliberative.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.deliberative.heuristic.FlawSelectionHeuristic;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategy;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.DeliberativeObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.FlawSelectionHeuristicPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.SearchStrategyPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.OperatorPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.PlanRefinementException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlannerSolver extends DeliberativeObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	@SearchStrategyPlaceholder
	protected SearchStrategy fringe;
	
	@FlawSelectionHeuristicPlaceholder
	protected FlawSelectionHeuristic heuristic;
	
	protected long timeout;
	protected long time;
	protected long stepCounter;
	protected String label;
	
	/**
	 * 
	 * @param label
	 * @param timeout
	 */
	protected PlannerSolver(String label, long timeout) {
		super();
		this.label = label;
		this.timeout = timeout;
	}
	
	/**
	 * 
	 * @return
	 */
	protected SearchSpaceNode createSearchSpaceNode() {
		return new SearchSpaceNode();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @throws NoSolutionFoundException
	 */
	public abstract SolutionPlan solve() 
			throws NoSolutionFoundException;
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "[Solver label= " + this.label + "]";
	}
	
	/**
	 * 
	 * @param node
	 */
	protected void backtrack(SearchSpaceNode node) 
	{
		// list of operators that have been applied to generate the node
		List<Operator> operators = node.getOperators();
		// retract operators starting from the more recent ones
		Collections.reverse(operators);
		// retract all operators
		for (Operator operator : operators) {
			// retract operator
			this.pdb.retract(operator);
		}
	}
	
	/**
	 * 
	 * @param node
	 * @throws OperatorPropagationException
	 */
	protected void propagate(SearchSpaceNode node) 
			throws PlanRefinementException 
	{
		// get the list of applied operators
		List<Operator> operators = node.getOperators();
		// list of committed operators
		List<Operator> committed = new ArrayList<>();
		try 
		{
			// propagate operators in chronological order
			for (Operator operator : operators) {
				// propagate operator
				this.pdb.propagate(operator);
				// add committed operator
				committed.add(operator);
			}
		}
		catch (OperatorPropagationException ex) {
			// retract committed operators in reverse order
			Collections.reverse(committed);
			for (Operator operator : committed) {
				this.pdb.retract(operator);
			}

			// throw exception
			throw new PlanRefinementException("Error while propagating node:\n" + node + "\n- message: " + ex.getMessage() + "\n");
		}
	}
	
	/**
	 * 
	 * @param last
	 * @param extracted
	 * @throws PlanRefinementException
	 */
	protected void contextSwitch(SearchSpaceNode last, SearchSpaceNode extracted) 
			throws PlanRefinementException 
	{
		// check if a backtrack is needed
		if (last != null) {
			// backtrack last propagated node
			this.backtrack(last);
		}
		
		// propagate extracted node
		this.propagate(extracted);
	}
	
	/**
	 * 
	 * @param current
	 * @param flaw
	 * @return
	 */
	protected List<SearchSpaceNode> expand(SearchSpaceNode current, Flaw flaw)
	{
		// list of child nodes
		List<SearchSpaceNode> list = new ArrayList<>();
		// check flaw solutions
		for (FlawSolution solution : flaw.getSolutions()) 
		{
			// create operator
			Operator op = new Operator(solution);
			// create child node
			SearchSpaceNode child = new SearchSpaceNode(current, op);
			// set computed makespan
			child.setMakespan(solution.getMakespan());
			// set resulting agenda
			List<ComponentValue> goals = new ArrayList<>(current.getAgenda().getGoals());
			// remove solved goals in the solution
			for (Decision decision : solution.getActivatedDecisisons()) {
				goals.remove(decision.getValue());
			}
//			for (ComponentValue solved : solution.getSolvedGoals()) {
//				goals.remove(solved);
//			}
			// add added pending goals in the solution
//			for (ComponentValue subgoal : solution.getCreatedSubGoals()) {
//				goals.add(subgoal);
//			}
			for (Decision decision : solution.getCreatedDecisions()) {
				goals.add(decision.getValue());
			}

			// set solution agenda
			Agenda agenda = new Agenda();
			for (ComponentValue goal : goals) {
				agenda.add(goal);
			}
			
			// set agenda to node
			child.setAgenda(agenda);
			// add child
			list.add(child);
		}
		
		// get children
		return list;
	}
}
