package it.uniroma3.epsl2.framework.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalNetworkReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalReasonerReference;
import it.uniroma3.epsl2.framework.microkernel.query.QueryManager;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQuery;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.ex.InconsistentIntervaEndTimeException;
import it.uniroma3.epsl2.framework.time.ex.InconsistentIntervalDurationException;
import it.uniroma3.epsl2.framework.time.ex.InconsistentIntervalStartTimeException;
import it.uniroma3.epsl2.framework.time.ex.TemporalConstraintPropagationException;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.ex.TimePointCreationException;
import it.uniroma3.epsl2.framework.time.lang.FixDurationIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixEndTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.FixStartTimeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.AfterIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.BeforeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.ContainsIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.DuringIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.EndsDuringIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.EqualsIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.MeetsIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.MetByIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.allen.StartsDuringIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalDistanceQuery;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalScheduleQuery;
import it.uniroma3.epsl2.framework.time.lang.query.CheckPseudoControllabilityQuery;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentTpValueException;
import it.uniroma3.epsl2.framework.time.tn.ex.TemporalConsistencyCheckException;
import it.uniroma3.epsl2.framework.time.tn.solver.TemporalSolver;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointBoundQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.solver.lang.query.TimePointQuery;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 *
 */
public abstract class TemporalDataBaseFacade extends ApplicationFrameworkObject implements QueryManager<TemporalQuery>
{
	@TemporalNetworkReference
	protected TemporalNetwork tn;							// temporal network

	@TemporalReasonerReference
	protected TemporalSolver<TimePointQuery> solver;		// time point reasoner
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	protected TemporalQueryFactory qf;						// temporal query factory
	
	/**
	 * 
	 */
	protected TemporalDataBaseFacade() {
		// get query factory instance
		this.qf = TemporalQueryFactory.getInstance();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin() {
		return this.tn.getOrigin();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.tn.getHorizon();
	}
	
	/**
	 * Get the equivalent Minimal Network
	 * 
	 * @return
	 */
	public String getTemporalNetworkDescription() {
		return this.tn.toString();
	}
	
	/**
	 * Create a flexible time point
	 * 
	 * @return
	 * @throws TimePointCreationException
	 */
	public final TimePoint createTimePoint() 
			throws TimePointCreationException 
	{
		// time point to create
		TimePoint point = null;
		try {
			// create a time point
			point = this.tn.addTimePoint();
		} catch (InconsistentDistanceConstraintException ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get time point
		return point;
	}
	
	/**
	 * Create a scheduled time point
	 * 
	 * @param at
	 * @return
	 * @throws TimePointCreationException
	 */
	public final TimePoint createTimePoint(long at) 
			throws TimePointCreationException {
		// time point to create
		TimePoint point = null;
		try {
			// create a fixed time point
			point = this.tn.addTimePoint(at);
		} catch (InconsistentTpValueException | InconsistentDistanceConstraintException  ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get time point
		return point;
	}
	
	/**
	 * Create a flexible time point within the specified bound
	 * 
	 * @param bounds
	 * @return
	 * @throws TimePointCreationException
	 */
	public final TimePoint createTimePoint(long[] bounds) 
			throws TimePointCreationException {
		// time point to create
		TimePoint point = null;
		try {
			// create a time point within bound
			point = this.tn.addTimePoint(bounds[0], bounds[1]);
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get created time point
		return point;
	}

	/**
	 * Create a controllable temporal interval
	 * 
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public final TemporalInterval createTemporalInterval(boolean controllable) 
			throws TemporalIntervalCreationException {
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {1, this.getHorizon()}, 
				controllable);		
	}
	
	/**
	 * Create a flexible interval with duration within the specified bound
	 * 
	 * @param duration
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public final TemporalInterval createTemporalInterval(long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException 
	{
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {this.getOrigin(), this.getHorizon()}, 
				duration, 
				controllable);
	}
	
	/**
	 * 
	 * @param end
	 * @param duration
	 * @param controllable
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public final TemporalInterval createTemporalInterval(long[] end, long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException 
	{
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				end, 
				duration, 
				controllable);
	}
	
	/**
	 * 
	 * @param end
	 * @param duration
	 * @param controllable
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public final TemporalInterval createTemporalInterval(long[] start, long[] end, long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException 
	{
		// interval's start time
		TimePoint s = null;
		try {
			// create flexible start time
			s = this.tn.addTimePoint(start[0], start[1]);
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			throw new InconsistentIntervalStartTimeException(ex.getMessage());
		}
		
		// interval's end time
		TimePoint e = null;
		try {
			// create flexible end time
			e = this.tn.addTimePoint(end[0], end[1]);
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			// delete start time
			this.tn.removeTimePoint(s);
			throw new InconsistentIntervaEndTimeException(ex.getMessage());
		
		}
		
		// interval's duration
		TimePointConstraint d = null;
		try {
			// create distance constraint
			d = this.tn.addConstraint(s, e, duration, controllable);
		} catch (InconsistentDistanceConstraintException ex) {
			// remove start and end time points
			this.tn.removeTimePoint(s);
			this.tn.removeTimePoint(e);
			throw new InconsistentIntervalDurationException(ex.getMessage());
		}
		
		// create temporal interval 
		return new TemporalInterval(d);
	}
	
	/**
	 * 
	 * @param i
	 * @throws Exception
	 */
	public final void deleteTemporalInterval(TemporalInterval i) {
		// list of time points to remove
		List<TimePoint> list = new ArrayList<>();
		// get start time
		list.add(i.getStartTime());
		// get end time
		list.add(i.getEndTime());
		// the network will automatically remove all constraints concerning the two time points
		this.tn.removeTimePoints(list);
	}
	
	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	public final void checkConsistency() 
			throws ConsistencyCheckException {
		// check temporal network consistency
		if (!this.solver.isConsistent()) {
			throw new TemporalConsistencyCheckException("The network is not temporally consistent");
		}
	}
	
	/**
	 * 
	 */
	@Override
	public final void process(TemporalQuery query) 
	{
		// check query type
		switch (query.getType()) 
		{
			// check distance between intervals
			case CHECK_INTERVAL_DISTANCE : {
				// get query
				CheckIntervalDistanceQuery distanceQuery = (CheckIntervalDistanceQuery) query;
				// get time points to analyze
				TimePoint source = distanceQuery.getSource().getEndTime();
				TimePoint target = distanceQuery.getTarget().getStartTime();
				
				// create time point query
				TimePointDistanceQuery tpDistanceQuery = this.qf.create(TemporalQueryType.TP_DISTANCE);
				// set source and target
				tpDistanceQuery.setSource(source);
				tpDistanceQuery.setTarget(target);
				
				// process query
				this.solver.process(tpDistanceQuery);
				// set bounds
				distanceQuery.setIntervalDistance(tpDistanceQuery.getDistance());
			}
			break;
			
			// check interval schedule 
			case CHECK_SCHEDULE : {
				// get query
				CheckIntervalScheduleQuery scheduleQuery = (CheckIntervalScheduleQuery) query;
				// get interval
				TemporalInterval i = scheduleQuery.getInterval();
				
				// create time point bound query
				TimePointBoundQuery tpQuery = this.qf.create(TemporalQueryType.TP_BOUND);
				// set point 
				tpQuery.setTimePoint(i.getStartTime());
				// process query
				this.solver.process(tpQuery);
				// set result
				scheduleQuery.setStartTimeSchedule(new long[] {
						i.getStartTime().getLowerBound(),
						i.getStartTime().getUpperBound()
				});
				
				tpQuery.setTimePoint(i.getEndTime());
				// process query
				this.solver.process(tpQuery);
				// set result
				scheduleQuery.setEndTimeSchedule(new long[] {
						i.getEndTime().getLowerBound(),
						i.getEndTime().getUpperBound()
				});
				
				// check distance
				TimePointDistanceQuery dquery= this.qf.create(TemporalQueryType.TP_DISTANCE);
				// set points
				dquery.setSource(i.getStartTime());
				dquery.setTarget(i.getEndTime());
				// process query
				this.solver.process(dquery);
				// set result
				scheduleQuery.setDuration(dquery.getDistance());
			}
			break;
			
			// check if squeezed interval
			case CHECK_PSEUDO_CONTROLLABILITY : {
				// get query
				CheckPseudoControllabilityQuery squeezedQuery = (CheckPseudoControllabilityQuery) query;
				// get time points to analyze
				TimePoint source = squeezedQuery.getInterval().getStartTime();
				TimePoint target = squeezedQuery.getInterval().getEndTime();
				
				// create time point query
				TimePointDistanceQuery tpDistanceQuery = this.qf.create(TemporalQueryType.TP_DISTANCE);
				// set source and target
				tpDistanceQuery.setSource(source);
				tpDistanceQuery.setTarget(target);
				
				// process query
				this.solver.process(tpDistanceQuery);
				// set bounds
				squeezedQuery.setDuration(tpDistanceQuery.getDistance());
			}
			break;
			
			// time point queries
			case TP_BOUND :
			case TP_DISTANCE : 
			case TP_DISTANCE_FROM_ORIGIN : 
			case TP_DISTANCE_TO_HORIZON : {
				// propagate time point query to the reasoner
				this.solver.process((TimePointQuery) query);
			}
			break;
		}
	}
	
	/**
	 * This method propagates an interval constraint to the underlying temporal network.
	 * 
	 * When propagating constraints no contingent link can be affected or overwritten. Thus,
	 * the method throws an exception if a contingent link is affected by the propagated 
	 * constraint.
	 * 
	 * @param constraint
	 * @throws Exception
	 */
	public final void propagate(TemporalConstraint constraint) 
			throws TemporalConstraintPropagationException 
	{
		try 
		{
			// check temporal constraint type
			switch (constraint.getType()) 
			{
				// create BEFORE constraint into the temporal network
				case BEFORE : 
				{
					// get constraint
					BeforeIntervalConstraint before = (BeforeIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = new TimePointConstraint[] { 
						this.doPropagateBeforeConstraint(before.getReference(), before.getTarget(), new long[] {before.getLb(), before.getUb()})
					};
					// set propagated constraint
					before.setPropagatedConstraints(c);
				}
				break;
				
				// create AFTER constraint into the temporal network
				case AFTER : 
				{
					AfterIntervalConstraint after = (AfterIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = new TimePointConstraint[] {
						this.doPropagateBeforeConstraint(after.getTarget(), after.getReference(), new long[] {after.getLb(), after.getUb()})
					};
					// set propagated constraint
					after.setPropagatedConstraints(c);
				}
				break;
				
				// create MEETS constraint into the temporal network
				case MEETS : 
				{
					// get constraint
					MeetsIntervalConstraint meets = (MeetsIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = new TimePointConstraint[] { 
							this.doPropagateMeetsConstraint(meets.getReference(), meets.getTarget())
					};
					// set propagated constraints
					meets.setPropagatedConstraints(c);
				}
				break;
				
				// create MET-BY constraint into the temporal network
				case MET_BY : 
				{
					// get constraint
					MetByIntervalConstraint metby = (MetByIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = new TimePointConstraint[] {
							this.doPropagateMeetsConstraint(metby.getTarget(), metby.getReference())
					};
					// set propagated constraints
					metby.setPropagatedConstraints(c);
				}
				break;
				
				// create CONTAINS constraint into the temporal network
				case CONTAINS : 
				{
					// get constraint
					ContainsIntervalConstraint contains = (ContainsIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateContainsConstraint(contains.getReference(), contains.getTarget(), contains.getStartTimeBounds(), contains.getEndTimeBounds());
					// set propagated constraints
					contains.setPropagatedConstraints(c);
				}
				break;
				
				// create DURING constraint into the temporal network
				case DURING : 
				{
					// get constraint
					DuringIntervalConstraint during = (DuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateDuringConstraint(during.getReference(), during.getTarget(), during.getStartTimeBounds(), during.getEndTimeBounds());
					// set propagated constraints
					during.setPropagatedConstraints(c);
				}
				break;
				
				// create STARTS-DURING constraint into the network
				case STARTS_DURING : 
				{
					// get constraint
					StartsDuringIntervalConstraint sdc = (StartsDuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateStartsDuringConstraint(sdc.getReference(), sdc.getTarget(), sdc.getFirstTimeBound(), sdc.getSecondTimeBound());
					// set propagated constraints
					sdc.setPropagatedConstraints(c);
				}
				break;
				
				// create ENDS-DURING constraint into the network
				case ENDS_DURING : 
				{
					// get constraint
					EndsDuringIntervalConstraint edc = (EndsDuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateEndsDuringConstraint(edc.getReference(), edc.getTarget(), edc.getFirstTimeBound(), edc.getSecondTimeBound());
					// set propagated constraints
					edc.setPropagatedConstraints(c);
				}
				break;
				
				// create EQUALS constraint into the temporal network
				case EQUALS : 
				{
					// get constraint
					EqualsIntervalConstraint equals = (EqualsIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateEqualsConstraint(equals.getReference(), equals.getTarget());
					// set propagated constraint
					equals.setPropagatedConstraints(c);
				}
				break;
				
				// set the duration of a temporal interval
				case FIX_DURATION : {
					// get constraint
					FixDurationIntervalConstraint fix = (FixDurationIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateFixDurationConstraint(fix.getReference(), fix.getDuration());
					// set propagate constraints
					fix.setPropagatedConstraints(c);
				}
				break;
				
				// set the start time of a temporal interval
				case FIX_START_TIME : 
				{
					// get constraint
					FixStartTimeIntervalConstraint fix = (FixStartTimeIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateFixStartConstraint(fix.getReference(), fix.getStart());
					// set propagate constraints
					fix.setPropagatedConstraints(c);
				}
				break;
				
				// set the end time of a temporal interval
				case FIX_END_TIME : 
				{
					// get constraint
					FixEndTimeIntervalConstraint fix = (FixEndTimeIntervalConstraint) constraint;
					// propagate constraint
					TimePointConstraint[] c = this.doPropagateFixEndConstraint(fix.getReference(), fix.getEnd());
					// set propagate constraints
					fix.setPropagatedConstraints(c);
				}
				break;
			}
		}
		catch (InconsistentDistanceConstraintException ex) {
			throw new TemporalConstraintPropagationException(ex.getMessage());
		}
	}

	/**
	 * 
	 * @param constraint
	 * @throws Exception
	 */
	public final void retract(TemporalConstraint constraint) {
		// retract propagated constraints
		TimePointConstraint[] toRetract = constraint.getPropagatedConstraints();
		this.tn.removeDistanceConstraint(Arrays.asList(toRetract));
		// clear data structure
		constraint.clear();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.tn.toString();
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateEqualsConstraint(TemporalInterval reference, TemporalInterval target) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between start times
		TimePointConstraint c1 = this.tn.addConstraint(reference.getStartTime(), target.getStartTime(), new long[] {0, 0}, true);
		// create a distance constraint between end times
		TimePointConstraint c2 = this.tn.addConstraint(target.getEndTime(), reference.getEndTime(), new long[] {0, 0}, true);
		// get added distance constraints
		return new TimePointConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param reference
	 * @param target
	 * @param startTimeBounds
	 * @param endTimeBounds
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateContainsConstraint(TemporalInterval reference, TemporalInterval target, long[] startTimeBounds, long[] endTimeBounds) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between reference's start time and target's start time 
		TimePointConstraint c1 = this.tn.addConstraint(reference.getStartTime(), target.getStartTime(), startTimeBounds, true);
		// crate a distance constraint between target's end time and reference's end time
		TimePointConstraint c2 = this.tn.addConstraint(target.getEndTime(), reference.getEndTime(), endTimeBounds, true);
		// get added constraints
		return new TimePointConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param reference
	 * @param target
	 * @param startTimeBounds
	 * @param endTimeBounds
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateDuringConstraint(TemporalInterval reference, TemporalInterval target, long[] startTimeBounds, long[] endTimeBounds) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between target's start time and reference's start time
		TimePointConstraint c1 = this.tn.addConstraint(target.getStartTime(), reference.getStartTime(), startTimeBounds, true);
		// create a distance constraint between reference's end time and target's end time
		TimePointConstraint c2 = this.tn.addConstraint(reference.getEndTime(), target.getEndTime(), endTimeBounds, true);
		// get added constraints
		return new TimePointConstraint[] {c1, c2};
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param firstTimeBound
	 * @param secondTimeBound
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateEndsDuringConstraint(TemporalInterval reference, TemporalInterval target, long[] firstTimeBound, long[] secondTimeBound) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between target's start time and reference's start time
		TimePointConstraint c1 = this.tn.addConstraint(target.getStartTime(), reference.getEndTime(), firstTimeBound, true);
		// create a distance constraint between reference's end time and target's end time
		TimePointConstraint c2 = this.tn.addConstraint(reference.getEndTime(), target.getEndTime(), secondTimeBound, true);
		// propagate constraints
		return new TimePointConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param reference
	 * @param target
	 * @param firstTimeBound
	 * @param secondTimeBound
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateStartsDuringConstraint(TemporalInterval reference, TemporalInterval target, long[] firstTimeBound, long[] secondTimeBound) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between target's start time and reference's start time
		TimePointConstraint c1 = this.tn.addConstraint(target.getStartTime(), reference.getStartTime(), firstTimeBound, true);
		// create a distance constraint between reference's end time and target's end time
		TimePointConstraint c2 = this.tn.addConstraint(reference.getStartTime(), target.getEndTime(), secondTimeBound, true);
		// propagate constraints
		return new TimePointConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param reference
	 * @param target
	 * @param lb
	 * @param ub
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint doPropagateBeforeConstraint(TemporalInterval reference, TemporalInterval target, long[] distance) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between reference's end time and target's start time
		return this.tn.addConstraint(reference.getEndTime(), target.getStartTime(), distance, true);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 * @throws Exception
	 */
	protected TimePointConstraint doPropagateMeetsConstraint(TemporalInterval reference, TemporalInterval target) 
			throws InconsistentDistanceConstraintException 
	{
		// create a distance constraint between reference's end time and target's start time
		return this.tn.addConstraint(reference.getEndTime(), target.getStartTime(), 
				new long[] {0, 0}, true);
	}
	
	/**
	 * 
	 * @param reference
	 * @param start
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateFixStartConstraint(TemporalInterval reference, long start) 
			throws InconsistentDistanceConstraintException 
	{
		return new TimePointConstraint[] {
				this.tn.addConstraint(this.tn.getOriginTimePoint(), reference.getStartTime(), 
						new long[] {start, start}, true) 
		};
	}
	
	/**
	 * 
	 * @param reference
	 * @param start
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateFixEndConstraint(TemporalInterval reference, long end) 
			throws InconsistentDistanceConstraintException 
	{
		return new TimePointConstraint[] {
				this.tn.addConstraint(this.tn.getOriginTimePoint(), reference.getEndTime(), 
						new long[] {end, end}, true) 
		};
	}

	/**
	 * 
	 * @param reference
	 * @param duration
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointConstraint[] doPropagateFixDurationConstraint(TemporalInterval reference, long duration) 
			throws InconsistentDistanceConstraintException 
	{
		return new TimePointConstraint[] { 
				this.tn.addConstraint(reference.getStartTime(), reference.getEndTime(), 
						new long[] {duration, duration}, reference.isControllable()) 
		};
	}
}
