package it.uniroma3.epsl2.deliberative.solver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristic;
import it.uniroma3.epsl2.deliberative.search.SearchStrategy;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.ex.OperatorPropagationException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Operator;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.FlawSelectionHeuristicReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.SearchStrategyReference;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Solver extends ApplicationFrameworkObject 
{
	@PlanDataBaseReference
	protected PlanDataBase pdb;
	
	@SearchStrategyReference
	protected SearchStrategy strategy;
	
	@FlawSelectionHeuristicReference
	protected FlawSelectionHeuristic heuristic;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	private SolverType type;
	
	/**
	 * 
	 * @param type
	 */
	protected Solver(SolverType type) {
		super();
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public SolverType getType() {
		return type;
	}
	
	/**
	 * 
	 * @throws NoSolutionFoundException
	 */
	public abstract SolutionPlan solve() 
			throws NoSolutionFoundException;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Solver type= " + this.type + " strategy= " + this.strategy + " heuristic= " + this.heuristic + "]";
	}
	
//	/**
//	 * 
//	 * @param depth
//	 * @return
//	 */
//	protected SearchSpaceNode createNode() {
//		return new SearchSpaceNode();
//	}
	
	/**
	 * 
	 * @param last
	 * @param extracted
	 * @throws PlanRefinementException
	 */
	protected void contextSwitch(SearchSpaceNode last, SearchSpaceNode extracted) 
			throws PlanRefinementException 
	{
		// get operators of the last propagated node
		List<Operator> from = new ArrayList<>();
		// check last node 
		if (last != null) {
			// get operators from the last propagated node
			from = last.getOperators();
		}
		List<Operator> to = extracted.getOperators();
		
		// check if backtrack is needed
		boolean backtrack = false;
		int threshold = Math.min(from.size(), to.size());
		Operator lastCommonOperator = null;
		for (int index = 0; index < threshold && !backtrack; index++) {
			// compare operators
			if (from.get(index).equals(to.get(index))) {
				// update last common operator
				lastCommonOperator = from.get(index);
			} else {
				// different operators - retraction is needed
				backtrack = true;
			}
		}

		// check if to retract
		List<Operator> toRetract = new ArrayList<>();
		if (backtrack) 
		{
			// retract operators of the last propagate node till the last common operator
			toRetract.addAll(last.getOperatorsUpTo(lastCommonOperator));
			for (Operator op : toRetract) {
				// undo applied flaw solution
				this.pdb.retract(op);
			}
		}
		
		// propagate operators of the extracted node (starting from the last common operator)
		List<Operator> toPropagate = extracted.getOperatorsFrom(lastCommonOperator);
		List<Operator> committed = new ArrayList<>();
		for (Operator op : toPropagate) 
		{
			try 
			{
				// propagate operator
				this.pdb.propagate(op);
				committed.add(op);
			}
			catch (OperatorPropagationException ex) {
				// undo committed operators
				for (Operator comm : committed) {
					this.pdb.retract(comm);
				}
				
				// forward exception
				throw new PlanRefinementException(ex.getMessage());
			}
		}
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
		// check flaw category
		switch (flaw.getCategory())
		{
			// planning flaw 
			case PLANNING : 
			{
				// check flaw solutions
				for (FlawSolution solution : flaw.getSolutions()) 
				{
					// create operator
					Operator op = new Operator(solution);
					// set the makespan according to the current node
					op.setMakespan(current.getMakespan());
					// set agenda according to the solution
					op.setAgenda(solution.getAgenda());
					// add child node
					list.add(new SearchSpaceNode(current, op));
				}
			}
			break;
			
			// scheduling flaw
			case SCHEDULING : 
			{
				// check flaw solutions
				for (FlawSolution solution : flaw.getSolutions()) 
				{
					// create operator
					Operator op = new Operator(solution);
					// set the agenda according to the current node
					op.setAgenda(current.getAgenda());
					// set the makespan according to the solution
					op.setMakespan(op.getMakespan());
					// add child node
					list.add(new SearchSpaceNode(current, op));
					
				}
			}
			break;
			
			// unsolvable flaw 
			case UNSOLVABLE : {
				throw new RuntimeException("Impossible to expand the search space with unsolvable flaws\n- flaw= " + flaw + "\n");
			}
		}
		
		// get children
		return list;
	}
}
