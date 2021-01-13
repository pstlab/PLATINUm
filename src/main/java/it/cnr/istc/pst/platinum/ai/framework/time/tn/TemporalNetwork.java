package it.cnr.istc.pst.platinum.ai.framework.time.tn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.DistanceConstraintNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentTpValueException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.TemporalNetworkTransactionFailureException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.TimePointNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.AddRelationTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.AddTimePointTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.DelRelationTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.DelTimePointTemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkNotificationFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkNotificationTypes;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalNetwork extends FrameworkObject 
{
	private final AtomicInteger COUNTER = new AtomicInteger(0);				// time point ID counter
	protected long origin;
	protected long horizon;
	
	// origin time point
	protected TimePoint tpOrigin;
	// horizon time point
	protected TimePoint tpHorizion;
	
	// data structures
	protected Set<TimePoint> recyclePoints;
	
	// temporal network observers
	private final List<TemporalNetworkObserver> observers;
	
	/**
	 * Creates and initialize a Temporal Network instance.
	 * 
	 * The constructor takes as input a temporal origin and a 
	 * temporal horizon. These values determine the lower and the 
	 * upper bounds of time points variables respectively.
	 * 
	 * @param origin
	 * @param horizon
	 */
	protected TemporalNetwork(long origin, long horizon) {
		super();
		// initialize data
		this.origin = origin;
		this.horizon = horizon;
		
		// create the origin time point
		this.tpOrigin = new TimePoint(COUNTER.getAndIncrement(), origin, origin);
		this.tpOrigin.setLowerBound(origin);
		this.tpOrigin.setUpperBound(origin);
		
		// create the horizon time point
		this.tpHorizion = new TimePoint(COUNTER.getAndIncrement(), horizon, horizon);
		this.tpHorizion.setLowerBound(horizon);
		this.tpHorizion.setUpperBound(horizon);
		
		// setup recycle points and observers
		this.recyclePoints = new HashSet<TimePoint>();
		this.observers = new LinkedList<TemporalNetworkObserver>();
	}

	/**
	 * Subscribes a new observer to the Temporal Network
	 * 
	 * @param obs
	 */
	public final void subscribe(TemporalNetworkObserver obs) {
		synchronized (this.observers) {
			// add observer
			this.observers.add(obs);
		}
	}
	
	/**
	 * Returns the value of the origin
	 * 
	 * @return
	 */
	public final long getOrigin() {
		return origin;
	}
	
	/**
	 * Returns the value of the horizon
	 * 
	 * @return
	 */
	public final long getHorizon() {
		return this.horizon;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract int size();
	
	/**
	 * Returns the temporal origin reference time point
	 * 
	 * @return
	 */
	public final TimePoint getOriginTimePoint() {
		return this.tpOrigin;
	}
	
	/**
	 * 
	 * @return
	 */
	public final TimePoint getHorizionTimePoint() {
		return this.tpHorizion;
	}
	
	/**
	 * Returns the time point with selected id
	 * 
	 * @param id
	 * @return
	 * @throws TimePointNotFoundException
	 */
	public abstract TimePoint getTimePoint(int id) 
			throws TimePointNotFoundException;
	
	/**
	 * Returns all the time points of the temporal network
	 * 
	 * @return
	 */
	public abstract List<TimePoint> getTimePoints();
	
	/**
	 * Returns all distance constraints where the specified 
	 * time point is the source of the constraint
	 * 
	 * @param tp
	 * @return
	 */
	public abstract List<TimePointDistanceConstraint> getConstraints(TimePoint tp);
	
	/**
	 * Returns the list of all distance constraints of the network
	 * 
	 * @return
	 */
	public List<TimePointDistanceConstraint> getConstraints() {
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		for (TimePoint tp : this.getTimePoints()) {
			// add constraints
			set.addAll(this.getConstraints(tp));
		}
		return new ArrayList<>(set);
	}
	
	/**
	 * Returns all distance constraints involving time points tp1 and tp2. The 
	 * method returns both constraints (tp1,tp2) and constraints (tp2,tp1) if
	 * they exist
	 * 
	 * @param tp1
	 * @param tp2
	 * @return
	 */
	public abstract List<TimePointDistanceConstraint> getConstraints(TimePoint tp1, TimePoint tp2);
	
	/**
	 * Returns the distance constraint between the origin of the network and the time point.
	 * 
	 * The method returns null if no constraint is found
	 * 
	 * @param point
	 * @return
	 */
	public abstract TimePointDistanceConstraint getConstraintFromOrigin(TimePoint point);
	
	/**
	 * 
	 * @param tp
	 * @return
	 */
	public abstract TimePointDistanceConstraint getConstraintToHorizon(TimePoint tp);
	
	/**
	 * Creates a new flexible TimePoint
	 * 
	 * The method can also use a previously created (and 
	 * deleted) time point rather than create a new instance
	 * 
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	public final TimePoint addTimePoint() 
			throws InconsistentDistanceConstraintException 
	{
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check among recycled time points first
		if (it.hasNext()) {
			// extract next time point
			tp = it.next();
			// remove time point from the set
			it.remove();
		}
		else {
			// actually create flexible time point
			tp = new TimePoint(COUNTER.getAndIncrement(), this.origin, this.horizon);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);
		
		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		
		// check observers
		synchronized (this.observers) 
		{
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) 
			{
				try 
				{
					// do notify 
					obs.notify(info);
				} catch (NotificationPropagationFailureException ex) {
					error(ex.getMessage());
				}
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * Creates N flexible time points 
	 * 
	 * The method can also use previously created (and 
	 * deleted) time points rather than create new instances
	 * 
	 * @param n
	 * @return
	 * @throws TemporalNetworkTransactionFailureException
	 * 
	 */
	public final List<TimePoint> addTimePoints(int n) 
			throws TemporalNetworkTransactionFailureException 
	{
		// notify flag
		boolean notify = false;
		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		
		try 
		{
			// create time points
			for (int i= 0; i < n; i++) {
				// create a flexible time point
				TimePoint tp = this.addTimePoint();	
				info.addTimePoint(tp);
				notify = true;
			}
		}
		catch (InconsistentDistanceConstraintException ex) 
		{
			// remove created time points
			for (TimePoint tp : info.getPoints()) {
				try {
					// remove created time point
					this.doRemoveTimePoint(tp);
				}
				catch (TimePointNotFoundException exx) {
					error(exx.getMessage());
				}
			}
			
			// throw exception
			throw new TemporalNetworkTransactionFailureException(ex.getMessage());
		}
		
		// check if notification is necessary
		if (notify) 
		{
			// check observers
			synchronized (this.observers) 
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					} catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
		
		// get time points
		return info.getPoints();
	}
	
	/**
	 * Creates a new TimePoint and fix it into the network at
	 * the desired time (at)
	 * 
	 * The method throws an exception if the specified value (at)
	 * is out of the temporal network domain
	 * 
	 * The method can also use a previously created (and deleted) time
	 * point rather than create a new instance
	 * 
	 * @param at
	 * @return
	 * @throws InconsistentTpValueException
	 */
	public final TimePoint addTimePoint(long at) 
			throws InconsistentTpValueException, InconsistentDistanceConstraintException
	{
		// check desired value
		if (at < this.origin || at > this.horizon) {
			// inconsistent value
			throw new InconsistentTpValueException("TimePoint value (= " + at +") out of the domain [origin= " + this.origin + ", horizon= " + this.horizon);
		}
		
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check recycled time points first
		if (it.hasNext()) 
		{
			// extract next time point
			tp = it.next();
			// set bounds
			tp.setDomLb(at);
			tp.setDomUb(at);
			// remove time point from the underlying set
			it.remove();
		}
		else {
			// create time point
			tp = new TimePoint(COUNTER.getAndIncrement(), at, at);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);

		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		
		// check observers
		synchronized (this.observers)
		{
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) 
			{
				try 
				{
					// do notify
					obs.notify(info);
				}
				catch (NotificationPropagationFailureException ex) {
					error(ex.getMessage());
				}
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * 
	 * @param lb
	 * @param ub
	 * @return
	 * @throws InconsistentTpValueException
	 * @throws InconsistentDistanceConstraintException
	 */
	public final TimePoint addTimePoint(long lb, long ub) 
			throws InconsistentTpValueException, InconsistentDistanceConstraintException
	{
		// check domain
		if (lb > ub || lb < this.origin || ub > this.horizon) {
			// inconsistent value
			throw new InconsistentTpValueException("TimePoint (lb= " + lb +", ub= " + ub + ") is inconsistent or it is out of the domain [origin= " + this.origin + ", horizon= " + this.horizon);
		}
		
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check recycled time points first
		if (it.hasNext()) {
			// extract next time point
			tp = it.next();
			// set desired bounds
			tp.setDomLb(lb);
			tp.setDomUb(ub);
			// remove time point from the set
			it.remove();
		}
		else {
			// create time point
			tp = new TimePoint(COUNTER.getAndIncrement(), lb, ub);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);

		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		
		// check observers
		synchronized (this.observers) 
		{
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) 
			{
				try 
				{
					// do notify
					obs.notify(info);
				}
				catch (NotificationPropagationFailureException ex) {
					error(ex.getMessage());
				}
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * 
	 * @param tp
	 * @throws InconsistentDistanceConstraintException
	 */
	protected abstract void doAddTimePoint(TimePoint tp) 
			throws InconsistentDistanceConstraintException;

	/**
	 * Removes a Time Point from the network
	 * 
	 * @param tp
	 */
	public final void removeTimePoint(TimePoint tp) 
	{
		try 
		{
			// delete time point
			this.doRemoveTimePoint(tp);
			// add time point to recycle list
			this.recyclePoints.add(tp);
			
			// create notification
			DelTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory
					.createNotification(TemporalNetworkNotificationTypes.DEL_TP);
			info.addTimePoint(tp);
			
			// check observers
			synchronized (this.observers) 
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					}
					catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		} 
		catch (TimePointNotFoundException ex) {
			error(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * Remove a list of timepoints from the temporal network 
	 * 
	 * @param tps
	 */
	public final synchronized void removeTimePoints(List<TimePoint> tps) 
	{
		// notify flag
		boolean notify = true;
		// create notification
		DelTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory
				.createNotification(TemporalNetworkNotificationTypes.DEL_TP);
		
		// map of time points the associated distance constraints that have been removed 
		Map<TimePoint, List<TimePointDistanceConstraint>> committed = new HashMap<>();
		try 
		{
			// check time points to remove
			for (TimePoint tp : tps) 
			{
				// delete time point
				List<TimePointDistanceConstraint> cRemoved = this.doRemoveTimePoint(tp);
				// add time point to recycle list
				this.recyclePoints.add(tp);
				// add deleted time point to the notification
				info.addTimePoint(tp);
				// add entry to the map
				committed.put(tp, cRemoved);
			}
		}
		
		catch (TimePointNotFoundException ex) 
		{
			// restore removed time points
			for (TimePoint p : committed.keySet()) {
				// remove time point from recycled ones
				this.recyclePoints.remove(p);
				try {
					// restore time point into the underlying network
					this.doAddTimePoint(p);
				}
				catch (InconsistentDistanceConstraintException exx) {
					error(exx.getMessage());
				}
			}
			
			// restore deleted distance constraints
			for (TimePoint p : committed.keySet()) {
				// restore deleted constraints
				for (TimePointDistanceConstraint c : committed.get(p)) {
					try {
						this.doAddConstraint(c);
					}
					catch (InconsistentDistanceConstraintException exx) {
						error(exx.getMessage());
					}
				}
			}
			
			// error message
			error(ex.getMessage());
			// set notify flag
			notify = false;
		}
		
		
		// check if a notification should be sent
		if (notify) 
		{
			// check observers 
			synchronized (this.observers)
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					} catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * Actually delete the selected time point from the network 
	 * and all related constraints
	 * 
	 * @param tp
	 * @return
	 * @throws TimePointNotFoundException
	 */
	protected abstract List<TimePointDistanceConstraint> doRemoveTimePoint(TimePoint tp) 
			throws TimePointNotFoundException;
	
	/**
	 * Creates a distance constraint between two time points with the 
	 * specified lower and upper bounds
	 * 
	 * Throws an exception when trying to create a distance constraint with
	 * lower bound greater than upper bound (lb > ub)
	 * 
	 * @param constraint
	 * @return
	 * @throws InconsistentTpDistanceRelation
	 */
	public final void addDistanceConstraint(TimePointDistanceConstraint constraint) 
			throws InconsistentDistanceConstraintException 
	{
		// check consistency of distance bound
		if (constraint.getDistanceLowerBound() > constraint.getDistanceUpperBound())  {
			// inconsistent value
			throw new InconsistentDistanceConstraintException("DistanceConstraint "
					+ "(dmin= " + constraint.getDistanceLowerBound() +", "
					+ "dmax= " + constraint.getDistanceUpperBound() + ") "
					+ "is inconsistent or it is not compatible wih the domain [origin= " + this.origin + ", horizon= " + this.horizon + "]");
		}
		
		// add constraint to the network
		boolean changed = this.doAddConstraint(constraint);
		// send notification if a change to the temporal information as be actually performed
		if (changed) 
		{
			// create notification
			AddRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory
					.createNotification(TemporalNetworkNotificationTypes.ADD_REL);
			// set constraint
			info.addRelation(constraint);
		
			// check observers
			synchronized (this.observers)
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					}
					catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Create N distance constraints all at once.
	 * 
	 * The method is atomic, i.e. if a constraint creation operation fails than all the 
	 * constraints previously created by the method are aborted. So if something goes wrong
	 * during the creation of constraints then the network is restored to the status before
	 * method invocation.   
	 * 
	 * @param constraints
	 * @return
	 * @throws TemporalNetworkTransactionFailureException
	 */
	public final synchronized void addDistanceConstraint(TimePointDistanceConstraint[] constraints) 
			throws TemporalNetworkTransactionFailureException 
	{
		// flag of successful operation and notification notification
		boolean notify = false;
		// create notification
		AddRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_REL);
		
		// list of committed distance constraints
		List<TimePointDistanceConstraint> committed = new ArrayList<>();
		try 
		{
			// propagate constraints
			for (int i = 0; i < constraints.length; i++) 
			{
				// get constraint
				TimePointDistanceConstraint constraint = constraints[i];
				// add constraint to network
				boolean changed = this.doAddConstraint(constraint);
				// add committed constraint
				committed.add(constraint);
				if (changed) {
					// add constraint to notification
					info.addRelation(constraint);
					// set notify flag
					notify = true;
				}
			}
		}
		catch (InconsistentDistanceConstraintException ex ) 
		{
			// remove all committed constraints
			for (TimePointDistanceConstraint tpd : committed) {
				try {
					// remove added constraint
					this.doRemoveDistanceConstraint(tpd);
				}
				catch (DistanceConstraintNotFoundException exx) {
					error(exx.getMessage());
				}
			}
			
			// throw exception
			throw new TemporalNetworkTransactionFailureException("Error while creating temporal constraints - Rollback Transaction");
		}

		
		// check if transaction has been successfully done and a notification should be sent
		if (notify) 
		{
			// check observers
			synchronized (this.observers)
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					} catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param rel
	 * @throws InconsistentDistanceConstraintException
	 */
	protected abstract boolean doAddConstraint(TimePointDistanceConstraint rel) 
			throws InconsistentDistanceConstraintException;
	
	/**
	 * Removes the distance constraint from the network
	 * 
	 * @param rel
	 */
	public final void removeConstraint(TimePointDistanceConstraint rel)  
	{
		try 
		{
			// remove temporal relation and check if a notification should be sent
			this.doRemoveDistanceConstraint(rel);
			// create notification
			DelRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.DEL_REL);
			info.addRelation(rel);
			
			// check observers
			synchronized (this.observers)
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					}
					catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
		catch (DistanceConstraintNotFoundException ex) {
			// error message
			error(ex.getMessage());
		}
	}
	
	/**
	 * Removes N distance constraints all at once
	 *
	 * @param rel
	 */
	public final void removeDistanceConstraint(List<TimePointDistanceConstraint> rels) 
	{
		// notification flag
		boolean notify = true;
		// create notification
		DelRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.DEL_REL);
		
		// list of deleted constraints
		List<TimePointDistanceConstraint> committed = new ArrayList<>();
		// get constraints to delete
		for (TimePointDistanceConstraint rel : rels) 
		{
			try 
			{
				// remove temporal relation
				this.doRemoveDistanceConstraint(rel);
				// add constraint to the list of committed ones
				committed.add(rel);
				// add constraint to notify
				info.addRelation(rel);
			}
			catch (DistanceConstraintNotFoundException ex) {
				
				// restore constraints
				for (TimePointDistanceConstraint cons : committed) {
					try {
						// add constraint
						this.doAddConstraint(cons);
					}
					catch (InconsistentDistanceConstraintException exx) {
						error(ex.getMessage());
					}
				}
				
				// error message
				error(ex.getMessage());
				// set notification flag
				notify = false;
				throw new RuntimeException(ex.getMessage());
			}
		}
		
		// check if a notification is necessary
		if (notify)
		{
			// check observers
			synchronized (this.observers)
			{
				// notify observers
				for (TemporalNetworkObserver obs : this.observers) 
				{
					try 
					{
						// do notify
						obs.notify(info);
					}
					catch (NotificationPropagationFailureException ex) {
						error(ex.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * Actually remove constraint from network
	 * 
	 * @param rel
	 * @throws DistanceConstraintNotFoundException
	 */
	protected abstract void doRemoveDistanceConstraint(TimePointDistanceConstraint rel) 
			throws DistanceConstraintNotFoundException;
	
	/**
	 * 
	 * @param lb
	 * @param ub
	 * @return
	 */
	protected TimePoint createTimePoint(long lb, long ub) {
		return new TimePoint(COUNTER.getAndIncrement(), lb, ub);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 * @param controllable
	 * @return
	 */
	protected final TimePointDistanceConstraint createDistanceConstraint(TimePoint from, TimePoint to, long[] distance, boolean controllable) 
	{
		// create constraint
		TimePointDistanceConstraint c = new TimePointDistanceConstraint();
		c.setReference(from);
		c.setTarget(to);
		c.setDistanceLowerBound(distance[0]);
		c.setDistanceUpperBound(distance[1]);
		c.setControllable(controllable);
		// get constraint
		return c;
	}
	
}