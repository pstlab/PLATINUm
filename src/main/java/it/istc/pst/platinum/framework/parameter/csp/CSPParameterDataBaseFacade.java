package it.istc.pst.platinum.framework.parameter.csp;

import java.util.LinkedList;
import java.util.List;

import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQuery;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.AddParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.DelParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationFactory;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationObserver;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationType;
import it.istc.pst.platinum.framework.parameter.csp.solver.ParameterSolverType;
import it.istc.pst.platinum.framework.parameter.ex.ParameterConsistencyException;
import it.istc.pst.platinum.framework.parameter.ex.ParameterConstraintPropagationException;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.query.CheckValuesParameterQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class CSPParameterDataBaseFacade extends ParameterFacade 
{
	private List<ParameterNotificationObserver> observers;
	
	private ParameterNotificationFactory factory;

	/**
	 * 
	 */
	@ParameterFacadeConfiguration(solver = ParameterSolverType.CHOCHO_SOLVER)
	protected CSPParameterDataBaseFacade() {
		super();
		// initialize observers
		this.observers = new LinkedList<>();
		// get factory
		this.factory = ParameterNotificationFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// subscribe observer
		this.observers.add(this.solver);
	}
	
	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	@Override
	public void checkConsistency() 
			throws ConsistencyCheckException 
	{
		// check temporal network consistency
		if (!this.solver.isConsistent()) {
			throw new ParameterConsistencyException("Inconsistent parameter constraints found");
		}
	}

	/**
	 * 
	 */
	@Override
	public void process(ParameterQuery query) 
	{
		// check query type
		switch (query.getType()) 
		{
			// check parameter values
			case CHECK_PARAMETER_VALUES : 
			{
				// get query
				CheckValuesParameterQuery pQuery = (CheckValuesParameterQuery) query;
				// get parameter
				Parameter<?> param = pQuery.getParameter();
				// get values
				this.solver.computeValues(param);
			}
			break;
			
			// compute values of all variables
			case COMPUTE_SOLUTION :
			{
				// compute CSP solution
				this.solver.computeSolution();
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doAddParameter(Parameter<?> param) 
	{
		// create notification
		AddParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_PARAM);
		// set added parameter
		notif.setParameter(param);
		// publish notification
		this.publish(notif);
	}

	/**
	 * 
	 */
	@Override
	protected void doDeleteParameter(Parameter<?> param) 
	{
		// create notification
		DelParameterNotification notif = this.factory.create(ParameterNotificationType.DEL_PARAM);
		// set added parameter
		notif.setParameter(param);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 */
	@Override
	protected void doPropagateConstraint(ParameterConstraint constraint) 
			throws ParameterConstraintPropagationException 
	{	
		// create notification
		AddConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_CONSTRAINT);
		notif.setConstraint(constraint);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetractConstraint(ParameterConstraint constraint) 
	{
		// create notification
		DelConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.DEL_CONSTRAINT);
		notif.setConstraint(constraint);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 * @param notif
	 */
	private void publish(ParameterNotification notif) {
		for (ParameterNotificationObserver observer : this.observers) {
			observer.update(notif);
		}
	}
}
