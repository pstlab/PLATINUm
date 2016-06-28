package it.uniroma3.epsl2.framework.parameter.solver.csp.chocho3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConsistencyException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConstraintNotFoundException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterNotFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.NumericConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.solver.ParameterSolver;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.AddConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.AddParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.DelConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.DelParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.ParameterNotification;

/**
 * 
 * @author anacleto
 *
 */
public final class Chocho3CSPSolver extends ParameterSolver 
{
	private Solver solver;
	private boolean buildSolver;
	
	private Map<Parameter, IntVar> vars;						// existing variables
	private Map<Parameter, List<ParameterConstraint>> out;		// outgoing constraints
	private Map<Parameter, List<ParameterConstraint>> in;		// incoming constraints
	private Map<ParameterConstraint, Constraint> constraints;	// all constraints
	
	/**
	 * 
	 */
	protected Chocho3CSPSolver() {
		super();
		// initialize data structures
		this.vars = new HashMap<>();
		this.out = new HashMap<>();
		this.in = new HashMap<>();
		this.constraints = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// setup solver
		this.solver = new Solver();
		this.buildSolver = false;
	}
	
	/**
	 * 
	 */
	private void rebuild() {
		// clean existing solver instance
		for (Constraint c : this.constraints.values()) {
			this.solver.unpost(c);
		}
		
		for (IntVar var : this.vars.values()) {
			this.solver.unassociates(var);
		}
		
		// reset solver
		this.solver = new Solver();
		System.gc();
		
		// reset variables and constraints
		
		
		
		// set rebuild flag
		this.buildSolver = false;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isConsistent() {
		
		// consistency flag
		boolean consistent = true;
		try {

			// try to propagate constraints
			this.solver.propagate();
			this.solver.getEngine().flush();
		}
		catch (ContradictionException ex) {
			// inconsistent constraint network
			consistent = false;
		}
		
		// get consistency result
		return consistent;
	}
	
	/**
	 * 
	 */
	@Override
	public void computeValues(Parameter param) {
		
		try {
			
			// check parameter type
			switch (param.getType()) {
			
				// enumeration parameter
				case ENUMERATION_PARAMETER_TYPE : {
					
					// get enumeration 
					EnumerationParameter enu = (EnumerationParameter) param;
					// get domain
					EnumerationParameterDomain edom = (EnumerationParameterDomain) enu.getDomain();
					
					// compute enumeration's values
					int value = this.doComputeVariableValue(enu);
					// get enumeration
					String eval = edom.getValue(value);
					
					// set value to variable
					enu.setValue(new String[] {eval});
				}
				break;
				
				// numeric parameter
				case NUMERIC_PARAMETER_TYPE : {
					
					// get numeric 
					NumericParameter num = (NumericParameter) param;
					
					// compute numeric's value
					int value = this.doComputeVariableValue(num);
					// set value
					num.setLowerBound(value);
					num.setUpperBound(value);
				}
				break;
				
				case CONSTANT_ENUMERATION_PARAMETER_TYPE : 
				case CONSTANT_NUMERIC_PARAMETER_TYPE : {
					
					// constant parameters
					this.logger.debug("Constant paramets' values cannot change");
				}
				break;
			}
		}
		catch (ParameterConsistencyException ex) {
			this.logger.warning(ex.getMessage());
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void notify(ParameterNotification info) {
		
		try {
			
			// check notification type
			switch (info.getType()) {
			
				// new parameter added
				case ADD_PARAM : {

					// get notification
					AddParameterNotification notif = (AddParameterNotification) info;
					// create variable
					this.doCreateVariable(notif.getParameter());
				}
				break;
				
				// existing parameter deleted
				case DEL_PARAM : {
					
					// get notification 
					DelParameterNotification notif = (DelParameterNotification) info;
					// remove variable
					this.doDeleteVariable(notif.getParameter());
				}
				break;
				
				// add parameter constraint
				case ADD_CONSTRAINT : {

					// get notification
					AddConstraintParameterNotification notif = (AddConstraintParameterNotification) info;
					// create constraint
					this.doCreateParameterConstraint(notif.getParameterConstraint());
				}
				break;
				
				// delete parameter constraint
				case DEL_CONSTRAINT : {

					// get notification
					DelConstraintParameterNotification notif = (DelConstraintParameterNotification) info;
					// delete constraint
					this.doDeleteParameterConstraint(notif.getParameterConstraint());
				}
				break;
			}
		}
		catch (ParameterNotFoundException | ParameterConstraintNotFoundException ex) {
			this.logger.warning(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param param
	 * @throws ParameterNotFoundException
	 */
	protected void doDeleteVariable(Parameter param) 
			throws ParameterNotFoundException {
		
		// check if parameter exists
		if (!this.vars.containsKey(param)) {
			throw new ParameterNotFoundException("Parameter not found " + param);
		}
		
		// remove all related constraints
		List<ParameterConstraint> list = this.out.remove(param);
		list.addAll(this.in.remove(param));
		// remove constraints
		for (ParameterConstraint cons : this.constraints.keySet()) {
			
			// get constraint
			Constraint cspConstraint = this.constraints.get(cons);
			// remove from solver
			this.solver.unpost(cspConstraint);
			// remove constraint
			this.constraints.remove(cons);
		}
		
		
		// get related variable
		IntVar var = this.vars.get(param);
		// remove variable from solver
		this.solver.unassociates(var);
		// remove parameter
		this.vars.remove(param);
	}
	
	/**
	 * 
	 * @param param
	 */
	protected void doCreateVariable(Parameter param) {

		// check parameter type
		switch (param.getType()) {
		
			// numeric parameter
			case NUMERIC_PARAMETER_TYPE : {
				
				// get numeric parameter
				NumericParameter nparam = (NumericParameter) param;
				// get domain 
				NumericParameterDomain dom = (NumericParameterDomain) param.getDomain();
				
				// create variable
				IntVar var = VF.bounded(
						nparam.getLabel(), 
						dom.getLowerBound(), 
						dom.getUpperBound(), 
						this.solver);
				
				// add variable
				this.vars.put(nparam, var);
				// add edges
				this.out.put(nparam, new ArrayList<>());
				this.in.put(nparam, new ArrayList<>());
			}
			break;
		
			// enumeration parameter
			case ENUMERATION_PARAMETER_TYPE : {
				
				// get parameter
				EnumerationParameter eparam = (EnumerationParameter) param;
				// get domain 
				EnumerationParameterDomain dom = (EnumerationParameterDomain) param.getDomain();
				
				// create variable
				IntVar var = VF.enumerated(eparam.getLabel(), 
						0,
						dom.getValues().length - 1, 
						this.solver);
				
				// add variable
				this.vars.put(eparam, var);
				// add edges
				this.out.put(eparam, new ArrayList<>());
				this.in.put(eparam, new ArrayList<>());
			}
			break;
			
			// constant enumeration parameter
			case CONSTANT_ENUMERATION_PARAMETER_TYPE : {
				
				// get parameter
				EnumerationConstantParameter eparam = (EnumerationConstantParameter) param;
				// get domain
				EnumerationParameterDomain dom = (EnumerationParameterDomain) param.getDomain();
				
				// get value
				int value = dom.getIndex(eparam.getValue());
				// create fixed variable
				IntVar var = VF.fixed(value, this.solver);
				
				// add variable
				this.vars.put(eparam, var);
				// add edges
				this.out.put(eparam, new ArrayList<>());
				this.in.put(eparam, new ArrayList<>());
			}
			break;
			
			// constant numeric parameter
			case CONSTANT_NUMERIC_PARAMETER_TYPE : {
				
				// get parameter
				NumericConstantParameter nparam = (NumericConstantParameter) param;
				
				// create fixed variable
				IntVar var = VF.fixed((int) nparam.getValue(), this.solver);
				
				// add variable
				this.vars.put(nparam, var);
				// add edges
				this.out.put(nparam, new ArrayList<>());
				this.in.put(nparam, new ArrayList<>());
			}
		}
	}
	
	/**
	 * 
	 * @param cons
	 * @throws ParameterConstraintNotFoundException
	 */
	protected void doDeleteParameterConstraint(ParameterConstraint cons) 
			throws ParameterConstraintNotFoundException {
		
		// check if constraint exists
		if (!this.constraints.containsKey(cons)) {
			throw new ParameterConstraintNotFoundException("ParameterConstraint not found " + cons);
		}
		
		// get CSP constraint
		Constraint cspConstraint = this.constraints.get(cons);
		// remove from solver
		this.solver.unpost(cspConstraint);
		
		// remove constraint
		this.constraints.remove(cons);
		this.out.get(cons.getReference()).remove(cons);
		this.in.get(cons.getTarget()).remove(cons);
	}
	
	/**
	 * 
	 * @param cons
	 * @throws ParameterNotFoundException
	 */
	protected void doCreateParameterConstraint(ParameterConstraint cons) 
			throws ParameterNotFoundException {
		
		// check if parameters exists
		if (!this.vars.containsKey(cons.getReference()) || !this.vars.containsKey(cons.getTarget())) {
			throw new ParameterNotFoundException("Constraint's parameters not found reference= " + cons.getReference() + " target= " + cons.getTarget());
		}
		
		// get parameters
		Parameter reference = cons.getReference();
		Parameter target = cons.getTarget();
		// get related variables
		IntVar vref = this.vars.get(reference);
		IntVar vtarg = this.vars.get(target);
		
		// create constraint
		Constraint c = IntConstraintFactory.arithm(vref, cons.getType().getSymbol(), vtarg);
		// add constraint
		this.out.get(reference).add(cons);
		this.in.get(target).add(cons);
		this.constraints.put(cons, c);
		// post constraint
		this.solver.post(c);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws ParameterConsistencyException
	 */
	protected int doComputeVariableValue(Parameter param) 
			throws ParameterConsistencyException {
		
		// get variable
		IntVar var = this.vars.get(param);
		// configure solver
		this.solver.set(IntStrategyFactory.lexico_LB(var));
		if (!this.solver.findSolution()) {
			
			// inconsistent problem
			throw new ParameterConsistencyException("Inconsistent constraints. Impossible to find solutions");
		}
		
		// get variable's value for the computed solution
		int value = var.getValue();
		
		// get values
		return value;
		
		
	}
}
