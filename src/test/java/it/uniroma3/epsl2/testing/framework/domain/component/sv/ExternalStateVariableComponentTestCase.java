package it.uniroma3.epsl2.testing.framework.domain.component.sv;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentFactory;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.sv.ExternalStateVariable;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacade;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeFactory;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.lang.FixIntervalDurationConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalScheduleQuery;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggerFactory;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class ExternalStateVariableComponentTestCase {
	
	private static final int ORIGIN = 0;
	private static final int HORIZON = 100;
	private TemporalDataBaseFacade facade;
	private ExternalStateVariable psv;

	/**
	 * 
	 */
	@Before
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* State Variable Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create logger
		FrameworkLoggerFactory lf = new FrameworkLoggerFactory();
		lf.createFrameworkLogger(FrameworkLoggingLevel.DEBUG);
		
		// get temporal facade
		TemporalDataBaseFacadeFactory factory = new TemporalDataBaseFacadeFactory();
		// create temporal facade
		this.facade = factory.createSingleton(TemporalDataBaseFacadeType.UNCERTAINTY_TEMPORAL_FACADE, ORIGIN, HORIZON);
		
		// get parameter facade
		ParameterDataBaseFacadeFactory pf = new ParameterDataBaseFacadeFactory();
		pf.createSingleton(ParameterDataBaseFacadeType.CSP_PARAMETER_FACADE);
		
		// create State Variable
		DomainComponentFactory df = new DomainComponentFactory();
		this.psv = df.create("EX_SV1", DomainComponentType.SV_EXTERNAL);
	}
	
	/**
	 * 
	 */
	@After
	public void clear() {
		this.psv = null;
		this.facade = null;
		System.gc();
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}
	
	/**
	 * 
	 */
	@Test
	public void createExternalStateVariableTest() {
		System.out.println("[Test]: createExternalStateVariableTest() --------------------");
		System.out.println();
		
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long [] {10, 30}, true);
		ComponentValue v3 = this.psv.addValue("Val-3", new long[] {11, 25}, false);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v3);
		System.out.println(this.psv);
	}
	
	/**
	 * 
	 */
	@Test
	public void addDecisionsTest() {
		System.out.println("[Test]: addDecisionsTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", new long[] {5, 5}, true);
		ComponentValue v2 = this.psv.addValue("Val-2", new long[] {10, 30}, true);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, 
					new long[] {10, 10}, new long[] {15, 15}, v1.getDurationBounds());
			this.psv.add(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.add(d2);
			Decision d3 = this.psv.create(v1, new String[] {});
			this.psv.add(d3);
			
			// check consistency
			this.facade.checkConsistency();
			
			// check flexible schedule
			TemporalQueryFactory qFactory = TemporalQueryFactory.getInstance();
			// create flexible schedule query
			IntervalScheduleQuery query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.facade.process(query);
			
			// print decision
			System.out.println(d1);
			// check bounds
			Assert.assertTrue(d1.getStart()[0] == 10);
			Assert.assertTrue(d1.getStart()[1] == 10);
			Assert.assertTrue(d1.getDuration()[0] == 5);
			Assert.assertTrue(d1.getDuration()[1] == 5);
			
			// create query
			query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d2.getToken().getInterval());
			// process query
			this.facade.process(query);
			
			// print decision
			System.out.println(d2);
			// check bounds
			Assert.assertTrue(d2.getStart()[0] == this.facade.getOrigin());
			Assert.assertTrue(d2.getStart()[1] == (this.facade.getHorizon() - v2.getDurationBounds()[0]));
			Assert.assertTrue(d2.getDuration()[0] == v2.getDurationBounds()[0]);
			Assert.assertTrue(d2.getDuration()[1] == v2.getDurationBounds()[1]);
			
			// print state variable information
			System.out.println();
			System.out.println(this.psv);
			System.out.println(this.facade);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addObservationTest() {
		System.out.println("[Test]: addDecisionsTest() --------------------");
		System.out.println();
		// check state variable object
		Assert.assertNotNull(this.psv);
		// create the state variable description
		ComponentValue v1 = this.psv.addValue("Val-1", new long[] {2, 10}, true);
		System.out.println(this.psv);
		System.out.println(this.facade);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, new long[] {15, 15}, v1.getDurationBounds());
			this.psv.add(d1);
			// print decision
			System.out.println(d1);
			
			// check consistency
			this.facade.checkConsistency();
			
			// check flexible schedule
			TemporalQueryFactory qFactory = TemporalQueryFactory.getInstance();
			// create flexible schedule query
			IntervalScheduleQuery query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.facade.process(query);
			
			// print decision
			System.out.println(d1);
			
			
			TemporalConstraintFactory iFactory = TemporalConstraintFactory.getInstance();
			FixIntervalDurationConstraint constraint = iFactory.create(TemporalConstraintType.FIX_INTERVAL_DURATION);
			constraint.setReference(d1.getToken().getInterval());
			constraint.setDuration(7);
			// propagate observation
			this.facade.propagate(constraint);
			
			// check consistency
			this.facade.checkConsistency();
			
			// check schedule
			query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.facade.process(query);
			System.out.println(d1);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}
	
}
