package it.uniroma3.epsl2.planner.solver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.FlawSelectionHeuristicReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.SearchStrategyReference;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;
import it.uniroma3.epsl2.planner.heuristic.fsh.FlawSelectionHeuristic;
import it.uniroma3.epsl2.planner.search.SearchStrategy;

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
	public abstract Plan solve() 
			throws NoSolutionFoundException;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Solver type= " + this.type + " strategy= " + this.strategy + " heuristic= " + this.heuristic + "]";
	}
	
	/**
	 * 
	 * @param depth
	 * @return
	 */
	protected SearchSpaceNode createNode() {
		return new SearchSpaceNode();
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
		// get operators of the last propagated node
		List<Operator> from = last.getOperators();
		List<Operator> to = extracted.getOperators();
		
		// check if retraction is needed
		boolean retract = false;
		int threshold = Math.min(from.size(), to.size());
		Operator lastCommonOperator = null;
		for (int index = 0; index < threshold && !retract; index++) {
			// compare operators
			if (from.get(index).equals(to.get(index))) {
				// update last common operator
				lastCommonOperator = from.get(index);
			} else {
				// different operators - retraction is needed
				retract = true;
			}
		}

		// check if to retract
		List<Operator> toRetract = new ArrayList<>();
		if (retract) {
			// retract operators of the last propagate node till the last common operator
			toRetract.addAll(last.getOperatorsUpTo(lastCommonOperator));
			for (Operator op : toRetract) {
				try {
					// undo applied flaw solution
					this.pdb.retract(op.getFlawSolution());
				}
				catch (FlawSolutionApplicationException ex) {
					// error while backtracking
					this.logger.error(ex.getMessage());
					throw new RuntimeException(ex.getMessage());
				}
			}
		}
		
		// propagate operators of the extracted node (starting from the last common operator)
		List<Operator> toPropagate = extracted.getOperatorsFrom(lastCommonOperator);
		List<Operator> committed = new ArrayList<>();
		for (Operator op : toPropagate) {
			try {
				// apply flaw solution
				this.pdb.propagete(op.getFlawSolution());
				committed.add(op);
			}
			catch (PlanRefinementException ex) {
				try {
					// undo committed operators
					for (Operator comm : committed) {
						this.pdb.retract(comm.getFlawSolution());
					}
				} 
				catch (FlawSolutionApplicationException exx) {
					this.logger.error(exx.getMessage());
					throw new RuntimeException(exx.getMessage());
				}
				
				// forward exception
				throw new PlanRefinementException(ex.getMessage());
			}
		}
	}
}
