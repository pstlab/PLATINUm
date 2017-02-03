package it.uniroma3.epsl2.testing.framework.parameter.facade.csp.v4;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.AddParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationFactory;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationType;
import it.uniroma3.epsl2.framework.parameter.csp.solver.choco.v4.ChocoSolver;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BindParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class ChochoSolverUnitTest 
{
	private ChocoSolver solver;
	private ParameterTestFactory paramFactory;
	private ParameterNotificationFactory notiFactory;
	
	/**
	 * 
	 */
	@Before
	public void init() {
		this.paramFactory = new ParameterTestFactory();
		this.notiFactory = ParameterNotificationFactory.getInstance();
		this.solver = ChocoSolver.getInstance();
	}

	/**
	 * 
	 */
	@Test
	public void choch4Test()
	{
		// 1. create a model
		Model model = new Model("Chocho4 first model");
		
		// 2. create variables
		IntVar x = model.intVar("x", 0, 5);
		IntVar y = model.intVar("y", new int[] {2, 3, 8});
		
		// 3. post constraints
//		model.arithm(x, "+", y, "<", 5).post();
//		model.times(x, y, 4).post();
//		model.arithm(x, "=", y).post();
		model.allDifferent(x, y).post();
		
		// 4. solve the problem
		if (model.getSolver().solve()) {
			System.out.println("Feasible");
			// 5. print the solution
			System.out.println(x);
			System.out.println(y);
		}
		else {
			System.out.println("Unfeasible...");
		}
		
		model.arithm(x, "=", 3).post();
		if (model.getSolver().solve()) {
			System.out.println("Feasible");
			// 5. print the solution
			System.out.println(x);
			System.out.println(y);
		}
		else {
			System.out.println("Unfeasible...");
		}
		
		model.arithm(y, "=", 3).post();
		if (model.getSolver().solve()) {
			System.out.println("Feasible");
			// 5. print the solution
			System.out.println(x);
			System.out.println(y);
		}
		else {
			System.out.println("Unfeasible...");
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void createVariablesTest() 
			throws Exception
	{
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		
		
		// create numeric parameter domain
		NumericParameterDomain ndom = this.paramFactory.createParameterDomain("floor", 
				ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		// set value bounds
		ndom.setLowerBound(0);
		ndom.setUpperBound(100);
		
		// create numeric parameter
		Parameter<?> np = this.paramFactory.createParameter("x", ndom);
		
		
		// create notification
		AddParameterNotification info = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		info.setParameter(np);
		// send notification
		this.solver.update(info);
		
		// create enumeration parameter
		EnumerationParameterDomain edom = this.paramFactory.createParameterDomain("locations", 
				ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
		edom.setValues(new String[] {
				"kitchen",
				"bathroom",
				"bedroom",
				"corridor"
		});
		
		// create enumeration parameter
		Parameter<?> ep = this.paramFactory.createParameter("position", edom);
		
		// create notification
		info = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		info.setParameter(ep);
		// send notification
		this.solver.update(info);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void createVariablesAndBindValuesTest() 
			throws Exception
	{
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		
		
		// create numeric parameter domain
		NumericParameterDomain ndom = this.paramFactory.createParameterDomain("floor", 
				ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		// set value bounds
		ndom.setLowerBound(0);
		ndom.setUpperBound(100);
		
		// create numeric parameter
		Parameter<?> np = this.paramFactory.createParameter("x", ndom);
		
		
		// create notification
		AddParameterNotification info = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		info.setParameter(np);
		// send notification
		this.solver.update(info);
		
		// create enumeration parameter
		EnumerationParameterDomain edom = this.paramFactory.createParameterDomain("locations", 
				ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
		edom.setValues(new String[] {
				"kitchen",		// value: 0
				"bathroom",		// value: 1
				"bedroom",		// value: 2
				"corridor"		// value: 3
		});
		
		
		
		// create enumeration parameter
		Parameter<?> ep = this.paramFactory.createParameter("position", edom);
		
		// create notification
		info = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		info.setParameter(ep);

		// send notification
		this.solver.update(info);
		
		Assert.assertTrue(this.solver.isConsistent());
		
		// create bind constraint
		BindParameterConstraint bind = this.paramFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(ep);
		bind.setValue("bedroom");
		
		// create notification
		AddConstraintParameterNotification acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		
		// create bind constraint
		bind = this.paramFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(np);
		bind.setValue(50);
		
		// create notification 
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addVariablesAndInconsistentConstraintsTest() 
			throws Exception
	{
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		
		// create enumeration parameter
		EnumerationParameterDomain edom = this.paramFactory.createParameterDomain("locations", 
				ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
		edom.setValues(new String[] {
				"kitchen",		// value: 0
				"bathroom",		// value: 1
				"bedroom",		// value: 2
				"corridor"		// value: 3
		});
		
		// create enumeration parameter
		Parameter<?> ep = this.paramFactory.createParameter("position", edom);
		
		// create notification
		AddParameterNotification info = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		info.setParameter(ep);

		// send notification
		this.solver.update(info);
		
		Assert.assertTrue(this.solver.isConsistent());
		
		// create bind constraint
		BindParameterConstraint bind = this.paramFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(ep);
		bind.setValue("bedroom");
		
		// create notification
		AddConstraintParameterNotification acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		
		// create bind constraint
		bind = this.paramFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(ep);
		bind.setValue("corridor");
		
		// create notification
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertFalse(this.solver.isConsistent());
		
		
		// retract last propagated constraint
		DelConstraintParameterNotification delInfo = this.notiFactory.create(ParameterNotificationType.DEL_CONSTRAINT);
		delInfo.setConstraint(bind);
		
		// notify solver
		this.solver.update(delInfo);
		
		// check model 
		Assert.assertTrue(this.solver.isConsistent());
	}
}
