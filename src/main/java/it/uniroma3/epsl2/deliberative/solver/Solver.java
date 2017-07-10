package it.uniroma3.epsl2.deliberative.solver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.OperatorPropagationException;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Operator;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Solver extends ApplicationFrameworkObject 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER)
	protected FrameworkLogger logger;
	
	@PlanDataBasePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE)
	protected PlanDataBase pdb;
	
	protected long time;
	protected long stepCounter;
	protected String label;
	
	/**
	 * 
	 * @param label
	 */
	protected Solver(String label) {
		super();
		this.label = label;
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
					// create child node
					SearchSpaceNode child = new SearchSpaceNode(current, op);
					// inherit the makespan from the parent node
					List<ComponentValue> goals = current.getAgenda().getGoals();
					// update list of goals 
					for (ComponentValue solved : solution.getSolvedGoals()) {
						goals.remove(solved);
					}
					for (ComponentValue subgoal : solution.getCreatedSubGoals()) {
						goals.add(subgoal);
					}

					// set the resulting agenda
					Agenda agenda = new Agenda();
					for (ComponentValue goal : goals) {
						agenda.add(goal);
					}
					child.setAgenda(agenda);
					// add child
					list.add(child);
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
					// create child node
					SearchSpaceNode child = new SearchSpaceNode(current, op);
					// inherit the agenda from the parent node
					double mk = solution.getMakespan();
					// update the node
					child.setMakespan(mk);
					// add child
					list.add(child);
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
