package it.istc.pst.platinum.testing.framework.parameter.csp.v4;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.istc.pst.platinum.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.AddParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.DelParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationFactory;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationType;
import it.istc.pst.platinum.framework.parameter.csp.solver.choco.v4.ChocoSolver;
import it.istc.pst.platinum.framework.parameter.lang.EnumerationParameter;
import it.istc.pst.platinum.framework.parameter.lang.EnumerationParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.BindParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.EqualParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ExcludeParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;

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
		System.out.println("**********************************************************************************");
		System.out.println("****************************** Choco Solver Test Case ****************************");
		System.out.println("**********************************************************************************");
		this.paramFactory = new ParameterTestFactory();
		this.notiFactory = ParameterNotificationFactory.getInstance();
		this.solver = ChocoSolver.getInstance();
	}
	
	/**
	 * 
	 */
	@After
	public void clear() {
		this.solver = null;
		System.gc();
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}

	/**
	 * 
	 */
	@Test
	public void choco4Test()
	{
		System.out.println("[Test]: choco4Test() --------------------");
		System.out.println();
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
		System.out.println("[Test]: createVariablesTest() --------------------");
		System.out.println();
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
		System.out.println("[Test]: addVariablesAndInconsistentConstraintsTest() --------------------");
		System.out.println();
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
		System.out.println(this.solver);
		
		// delete parameter
		DelParameterNotification dpInfo = this.notiFactory.create(ParameterNotificationType.DEL_PARAM);
		dpInfo.setParameter(ep);
		
		// notify solver
		this.solver.update(dpInfo);
		
		// check model 
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addVariablesAndConstraintsTest() 
			throws Exception
	{
		System.out.println("[Test]: addVariablesAndConstraintsTest() --------------------");
		System.out.println();
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
		Parameter<?> p1 = this.paramFactory.createParameter("p1", edom);
		Parameter<?> p2 = this.paramFactory.createParameter("p2", edom);
		
		// create notification
		AddParameterNotification apInfo = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		apInfo.setParameter(p1);
		// send notification
		this.solver.update(apInfo);
		apInfo.setParameter(p2);
		this.solver.update(apInfo);
		
		// check consistency
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		// create equal constraint
		EqualParameterConstraint eq = this.paramFactory.createParameterConstraint(ParameterConstraintType.EQUAL);
		eq.setReference(p1);
		eq.setTarget(p2);
		
		// create notification
		AddConstraintParameterNotification acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(eq);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		// create bind constraint
		BindParameterConstraint bind = this.paramFactory.createParameterConstraint(ParameterConstraintType.BIND);
		bind.setReference(p1);
		bind.setValue("corridor");
		// create notification
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		// add inconsistent constraint
		bind.setReference(p2);
		bind.setValue("bathroom");
		// create notification
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(bind);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertFalse(this.solver.isConsistent());
		System.out.println(this.solver);
		
		// retract last propagated constraint
		DelConstraintParameterNotification dcInfo = this.notiFactory.create(ParameterNotificationType.DEL_CONSTRAINT);
		dcInfo.setConstraint(bind);
		// notify solver
		this.solver.update(dcInfo);
		
		// check model 
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void addVariableExcludeConstraintAndQueryTest() 
			throws Exception
	{
		System.out.println("[Test]: addVariableExcludeConstraintAndQueryTest() --------------------");
		System.out.println();
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
		EnumerationParameter p1 = this.paramFactory.createParameter("p1", edom);
		
		// create notification
		AddParameterNotification apInfo = this.notiFactory.create(ParameterNotificationType.ADD_PARAM);
		apInfo.setParameter(p1);
		// send notification
		this.solver.update(apInfo);
		
		// check consistency
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		// create bind constraint
		ExcludeParameterConstraint ex = this.paramFactory.createParameterConstraint(ParameterConstraintType.EXCLUDE);
		ex.setReference(p1);
		ex.setValue("kitchen");
		// create notification
		AddConstraintParameterNotification acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(ex);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		this.solver.computeValues(p1);
		String[] vals = p1.getValues();
		Assert.assertTrue(vals.length < p1.getDomainValues().length);
		// verify that the value "kitchen" has been actually excluded
		boolean found = false;
		for (int i= 0; i < vals.length && !found; i++) { 
			found = vals[i].equals("kitchen");
			System.out.println("- val: " + vals[i]);
		}
		Assert.assertFalse(found);
		
		// exclude another value
		ex = this.paramFactory.createParameterConstraint(ParameterConstraintType.EXCLUDE);
		ex.setReference(p1);
		ex.setValue("bedroom");
		// create notification
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(ex);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		this.solver.computeValues(p1);
		vals = p1.getValues();
		Assert.assertTrue(vals.length < p1.getDomainValues().length);
		// verify that the value "kitchen" has been actually excluded
		found = false;
		for (int i= 0; i < vals.length && !found; i++) { 
			found = vals[i].equals("kitchen");
			System.out.println("- val: " + vals[i]);
		}
		Assert.assertFalse(found);
		
		// redundant constraint
		ex = this.paramFactory.createParameterConstraint(ParameterConstraintType.EXCLUDE);
		ex.setReference(p1);
		ex.setValue("bedroom");
		// create notification
		acInfo = this.notiFactory.create(ParameterNotificationType.ADD_CONSTRAINT);
		acInfo.setConstraint(ex);
		// notify solver
		this.solver.update(acInfo);
		
		// check model
		Assert.assertTrue(this.solver.isConsistent());
		System.out.println(this.solver);
		
		this.solver.computeValues(p1);
		vals = p1.getValues();
		Assert.assertTrue(vals.length < p1.getDomainValues().length);
		// verify that the value "kitchen" has been actually excluded
		found = false;
		for (int i= 0; i < vals.length && !found; i++) { 
			found = vals[i].equals("kitchen");
			System.out.println("- val: " + vals[i]);
		}
		Assert.assertFalse(found);
	}
}
