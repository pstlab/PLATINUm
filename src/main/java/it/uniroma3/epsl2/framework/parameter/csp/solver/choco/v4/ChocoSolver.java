package it.uniroma3.epsl2.framework.parameter.csp.solver.choco.v4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.iterators.DisposableValueIterator;

import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.AddParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.DelParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.solver.ParameterSolver;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameter;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BinaryParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BindParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.EqualParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ExcludeParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.NotEqualParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class ChocoSolver extends ParameterSolver 
{
	private static ChocoSolver INSTANCE = null;
	
	private boolean clean;											// clean flag
	private Model model;											// CSP model
	
	private Map<Parameter<?>, IntVar> variables;					// variables of the model
	private Map<ParameterConstraint, Constraint> constraints;		// constraints of the model
	
	/**
	 * Singleton constructor for testing purposes only
	 * 
	 * @return
	 */
	public static final ChocoSolver getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ChocoSolver();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 */
	protected ChocoSolver() {
		super();
		// setup data structures
		this.variables = new HashMap<>();
		this.constraints = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	private void init() {
		// build the model
		this.build();
	}

	/**
	 * 
	 */
	@Override
	public boolean isConsistent() 
	{
		// check if clean
		if (!this.clean) {
			// rebuild the model
			this.build();
		}
		
		// consistency flag
		boolean consistent = true;
		// try to find a solution
		this.model.getSolver().solve();
		// check feasibility
		switch (this.model.getSolver().isFeasible()) 
		{
			// not feasible
			case FALSE : 
			case UNDEFINED : {
				// no feasible solutions exist
				consistent = false;
				this.model.getSolver().reset();
			}
			break;
			
			// feasible or undefined
			case TRUE : {
				// a feasible solution exists at least
				consistent = true;
				this.model.getSolver().reset();
			}
			break;
		}
		
		// get consistency flag
		return consistent;
	}
	
	/**
	 * 
	 */
	@Override
	public void computeSolution()
	{
		// check clean flag
		if (!this.clean) {
			// build the model
			this.build();
		}
		
		// compute values for each parameter
		for (Parameter<?> param : this.variables.keySet()) {
			this.computeValues(param);
		}
	}

	/**
	 * 
	 */
	@Override
	public void computeValues(Parameter<?> param) 
	{
		// get variable
		if (!this.variables.containsKey(param)) {
			throw new RuntimeException("Parameter not found in the CSP\n- " + param);
		}
		
		// check clean flag
		if (!this.clean) {
			// build the model
			this.build();
		}
	
		// check parameter type
		switch (param.getType())
		{
			// enumeration parameter
			case ENUMERATION_PARAMETER_TYPE : 
			{
				// get numeric parameter
				EnumerationParameter ep = (EnumerationParameter) param;
				// compute allowed values
				Set<Integer> vals = new HashSet<>();
				// check possible solutions
				while (this.model.getSolver().solve())
				{
					// get variable
					IntVar var = this.variables.get(ep);
					DisposableValueIterator it = var.getValueIterator(true);
					while (it.hasNext()) {
						// add value
						vals.add(it.next());
					}
					// dispose iterator
					it.dispose();
				}
				
				// set allowed values
				int[] values = ArrayUtils.toPrimitive(vals.toArray(new Integer[vals.size()]));
				// set values
				ep.setValues(values);
				
				// reset solver
				this.model.getSolver().reset();
			}
			break;
			
			// numeric parameter
			case NUMERIC_PARAMETER_TYPE : 
			{
				// get numeric parameter
				NumericParameter np = (NumericParameter) param;
				int lb = Integer.MIN_VALUE;
				int ub = Integer.MAX_VALUE;
				// check all solutions
				while (this.model.getSolver().solve()) {
					// check variable bounds
					IntVar var = this.variables.get(np);
					lb = Math.max(lb, var.getLB());
					ub = Math.min(ub, var.getUB());
				}
				
				// set bounds
				np.setLowerBound(lb);
				np.setUpperBound(ub);
				// reset solver
				this.model.getSolver().reset();
			}
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	public void update(ParameterNotification info) 
	{
		// check notification type
		switch (info.getType())
		{
			case ADD_PARAM : 
			{
				// get notification
				AddParameterNotification notif = (AddParameterNotification) info;
				// create CSP variable
				IntVar variable = this.doCreateCSPVariable(notif.getParameter());
				// add variable to the model
				this.variables.put(notif.getParameter(), variable);
			}
			break;
		
			case ADD_CONSTRAINT : 
			{
				// get notification
				AddConstraintParameterNotification notif = (AddConstraintParameterNotification) info;
				// create CSP constraint
				Constraint cons = this.doCreateCSPConstraint(notif.getParameterConstraint());
				// check model status to post constraint
				if (this.clean) {
					cons.post();
				}
				// add constraint
				this.constraints.put(notif.getParameterConstraint(), cons);
			}
			break;
			
			case DEL_CONSTRAINT : 
			{
				// get notification 
				DelConstraintParameterNotification notif = (DelConstraintParameterNotification) info;
				// get constraint 
				ParameterConstraint cons = notif.getParameterConstraint();
				// remove constraint
				this.constraints.remove(cons);
				// set the clean flag to false
				this.clean = false;
			}
			break;
			
			case DEL_PARAM : 
			{
				// get notification 
				DelParameterNotification notif = (DelParameterNotification) info;
				// get parameter to remove
				Parameter<?> param = notif.getParameter();
				// remove variable from the model
				this.variables.remove(param);
				
				// remove related constraints
				List<ParameterConstraint> toRemove = this.doFindParameterRelatedConstraints(param);				
				// remove constraints from the model
				for (ParameterConstraint cons : toRemove) {
					this.constraints.remove(cons);
				}
				
				// set the clan flag to false
				this.clean = false;
			}
			break;
				
			default: {
				// unknown notification
				throw new RuntimeException("Unknown parameter notification type - " + info.getType());
			}		
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.model.toString();
	}
	
	/**
	 * 
	 */
	private void build() 
	{
		// setup new model
		this.model = new Model("Chocho CSP model");
		this.model.getSolver().setDFS();
		
		// update variables associated to parameters
		for (Parameter<?> param : this.variables.keySet()) {
			// create parameter
			IntVar var = this.doCreateCSPVariable(param);
			this.variables.put(param, var);
		}
		
		
		// update constraints between variables
		for (ParameterConstraint constraint : this.constraints.keySet()) {
			// create constraint
			Constraint cons = this.doCreateCSPConstraint(constraint);
			this.constraints.put(constraint, cons);
		}
		
		// post constraints
		for (ParameterConstraint constraint : this.constraints.keySet()) {
			// post CSP constraint
			this.constraints.get(constraint).post();
		}
		
		// set clean flag
		this.clean = true;
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	private IntVar doCreateCSPVariable(Parameter<?> param)
	{
		// CSP variable
		IntVar var;
		// check parameter type
		switch (param.getType())
		{
			// enumeration parameter
			case ENUMERATION_PARAMETER_TYPE : 
			{
				// get enumeration parameter
				EnumerationParameter p = (EnumerationParameter) param;
				// get value indexes
				int[] values = p.getValueIndexes();
				// create variable
				var = this.model.intVar(p.getLabel(), values);
			}
			break;
			
			// numeric parameter
			case NUMERIC_PARAMETER_TYPE : 
			{
				// get numeric parameter
				NumericParameter p = (NumericParameter) param;
				// create variable
				var = this.model.intVar(p.getLabel(), p.getLowerBound(), p.getUpperBound());
			}
			break;
			
			// unknown parameter
			default : {
				throw new RuntimeException("Unknown parameter type - " + param.getType());
			}
		}
		
		// get CSP variable
		return var;
	}
	
	/**
	 * 
	 * @param constraint
	 * @return
	 */
	private Constraint doCreateCSPConstraint(ParameterConstraint constraint)
	{
		Constraint cons;
		// check constraint type
		switch (constraint.getType())
		{
			// bind parameter constraint
			case BIND : 
			{
				// get bind constraint
				BindParameterConstraint bind = (BindParameterConstraint) constraint;
				cons = this.doCreateBindCSPConstraint(bind);
			}
			break;
			
			case EXCLUDE :
			{
				// get exclude constraint
				ExcludeParameterConstraint ex = (ExcludeParameterConstraint) constraint;
				cons = this.doCreateExcludeCSPConstraint(ex);
			}
			break;
				
			// equal parameter constraint
			case EQUAL : 
			{
				// get equal constraint
				EqualParameterConstraint eq = (EqualParameterConstraint) constraint;
				cons = this.doCreateEqualCSPConstraint(eq);
			}
			break;
			
			// not equal parameter constraint
			case NOT_EQUAL : 
			{
				// get not equal constraint
				NotEqualParameterConstraint neq = (NotEqualParameterConstraint) constraint;
				cons = this.doCreateNotEqualCSPConstraint(neq);
			}
			break;
			
			default : {
				throw new RuntimeException("Unknown parameter constraint type - " + constraint.getType());
			}
		}
		
		// get CSP constraint
		return cons;
	}
	
	/**
	 * 
	 * @param bind
	 * @return
	 */
	private Constraint doCreateBindCSPConstraint(BindParameterConstraint bind)
	{
		// CSP constraint
		Constraint cons;
		// get reference parameter
		Parameter<?> param = bind.getReference();
		// check if parameter variable exists
		if (!this.variables.containsKey(param)) {
			throw new RuntimeException("Unknown parameter variable - " + param);
		}
		
		// check parameter type 
		switch(param.getType())
		{
			// binding to enumeration parameter
			case ENUMERATION_PARAMETER_TYPE : 
			{
				// get parameter
				EnumerationParameter p = (EnumerationParameter) param;
				// get variable
				IntVar var = this.variables.get(p);
				// get binding value
				String value = (String) bind.getValue();
				// get related index
				int index = p.getDomain().getIndex(value);
				// create constraint 
				cons = this.model.arithm(var, "=", index);
			}	
			break;
				
			// binding numeric variable
			case NUMERIC_PARAMETER_TYPE : 
			{
				// get parameter 
				NumericParameter p = (NumericParameter) param;
				// get variable 
				IntVar var = this.variables.get(p);
				// get binding value
				int value = Integer.parseInt(bind.getValue().toString());
				// create constraint
				cons = this.model.arithm(var, "=", value);
			}
			break;
				
			default : {
				throw new RuntimeException("Unknown parameter type - " + param.getType());
			}
		}
		
		// get create CSP constraint
		return cons;
	}
	
	/**
	 * 
	 * @param bind
	 * @return
	 */
	private Constraint doCreateExcludeCSPConstraint(ExcludeParameterConstraint bind)
	{
		// CSP constraint
		Constraint cons;
		// get reference parameter
		Parameter<?> param = bind.getReference();
		// check if parameter variable exists
		if (!this.variables.containsKey(param)) {
			throw new RuntimeException("Unknown parameter variable - " + param);
		}
		
		// check parameter type 
		switch(param.getType())
		{
			// binding to enumeration parameter
			case ENUMERATION_PARAMETER_TYPE : 
			{
				// get parameter
				EnumerationParameter p = (EnumerationParameter) param;
				// get variable
				IntVar var = this.variables.get(p);
				// get binding value
				String value = (String) bind.getValue();
				// get related index
				int index = p.getDomain().getIndex(value);
				// create constraint 
				cons = this.model.arithm(var, "!=", index);
			}	
			break;
				
			// binding numeric variable
			case NUMERIC_PARAMETER_TYPE : 
			{
				// get parameter 
				NumericParameter p = (NumericParameter) param;
				// get variable 
				IntVar var = this.variables.get(p);
				// get binding value
				int value = (int) bind.getValue();
				// create constraint
				cons = this.model.arithm(var, "!=", value);
			}
			break;
				
			default : {
				throw new RuntimeException("Unknown parameter type - " + param.getType());
			}
		}
		
		// get create CSP constraint
		return cons;
	}
	
	/**
	 * 
	 * @param constraint
	 * @return
	 */
	private Constraint doCreateEqualCSPConstraint(EqualParameterConstraint constraint)
	{
		// check if parameter variables exist
		if (!this.variables.containsKey(constraint.getReference()) || 
				!this.variables.containsKey(constraint.getTarget())) 
		{
			throw new RuntimeException("Unknownw parameter variables\n- reference= " + constraint.getReference() + "\n- target= " + constraint.getTarget() + "\n");
		}
		
		// get variables
		IntVar ref = this.variables.get(constraint.getReference());
		IntVar tar = this.variables.get(constraint.getTarget());
		
		// create constraint
		Constraint cons = this.model.allEqual(ref, tar);
		return cons;
	}
	
	/**
	 * 
	 * @param constraint
	 * @return
	 */
	private Constraint doCreateNotEqualCSPConstraint(NotEqualParameterConstraint constraint)
	{
		// check if parameter variables exist
		if (!this.variables.containsKey(constraint.getReference()) || 
				!this.variables.containsKey(constraint.getTarget())) 
		{
			throw new RuntimeException("Unknownw parameter variables\n- reference= " + constraint.getReference() + "\n- target= " + constraint.getTarget() + "\n");
		}
		
		// get variables
		IntVar ref = this.variables.get(constraint.getReference());
		IntVar tar = this.variables.get(constraint.getTarget());
		
		// create constraint
		Constraint cons = this.model.allDifferent(ref, tar);
		return cons;
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	private List<ParameterConstraint> doFindParameterRelatedConstraints(Parameter<?> param)
	{
		// list of parameter related constraints
		List<ParameterConstraint> list = new ArrayList<>();
		// check constraints
		for (ParameterConstraint cons : this.constraints.keySet()) 
		{
			// check constraint type
			switch (cons.getType()) 
			{
				// bind constraint
				case BIND : 
				case EXCLUDE : 
				{
					// check reference
					if (cons.getReference().equals(param)) {
						// remove constraint
						list.add(cons);
					}
				}
				break;
			
				// binary constraint
				case EQUAL : 
				case NOT_EQUAL : 
				{
					// get binary constraint
					BinaryParameterConstraint binary = (BinaryParameterConstraint) cons;
					if (binary.getReference().equals(param) || binary.getTarget().equals(param)) {
						// add constraint
						list.add(cons);
					}
				}
				break;
				
				default : {
					// unknown 
					throw new RuntimeException("Unknownw parameter constraint type - " + cons.getType());
				}
			}
		}
		
		// get list
		return list;
	}
}
