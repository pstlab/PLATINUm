package it.istc.pst.platinum.protocol.lang;

import it.istc.pst.platinum.protocol.lang.relation.RelationProtocolDescriptor;
import it.istc.pst.platinum.protocol.lang.relation.RelationProtocolLanguageFactory;


/**
 * 
 * @author alessandroumbrico
 *
 */
public class ProtocolLanguageFactory 
{
	private static ProtocolLanguageFactory INSTANCE = null;
	private static int TOKEN_ID_COUNTER = 0;
	private static int TOKEN_PARAMETER_ID_COUNTER = 0;
	private static int TIMELINE_ID_COUNTER = 0;
	private RelationProtocolLanguageFactory relFactory;
	
	/**
	 * Private Factory instance constructor
	 */
	private ProtocolLanguageFactory(long horizon) {
		this.relFactory = RelationProtocolLanguageFactory.getSingletonInstance(horizon);
	}
	
	/**
	 * 
	 * @param horizon
	 * @return
	 */
	public static ProtocolLanguageFactory getSingletonInstance(long horizon) {
		if (INSTANCE == null) {
			INSTANCE = new ProtocolLanguageFactory(horizon);
		}
		
		return INSTANCE;
	}

	/**
	 * 
	 * @param timeline
	 * @param predicate
	 * @param startTimeBounds
	 * @param endTimeBounds
	 * @param durationBounds
	 * @param paramNames
	 * @param paramTypes
	 * @param paramBounds
	 * @param paramValues
	 * @return
	 */
	public TokenProtocolDescriptor createTokenDescriptor(TimelineProtocolDescriptor timeline, String predicate,
			long[] startTimeBounds, long[] endTimeBounds, long[] durationBounds,
			String[] paramNames, ParameterTypeDescriptor[] paramTypes, long[][] paramBounds, String[][] paramValues) 
	{
		// initialize token
		TokenProtocolDescriptor token = new TokenProtocolDescriptor(getNextTokenId(), timeline, predicate);
		
		
		// set temporal information
		token.setTemporalInformation(startTimeBounds, endTimeBounds, durationBounds);

		// check not mandatory arguments
		if (paramNames != null) {
			// create parameters
			for (int index= 0; index < paramNames.length; index++) {
				// get parameter name
				String name = paramNames[index];
				ParameterTypeDescriptor type = paramTypes[index];
				long[] bounds = new long[] {};
				if (paramBounds != null) {
					bounds = paramBounds[index];
				}
				
				String[] values = new String[] {};
				if (paramValues != null) {
					values = paramValues[index];
				}
				
				// create and add parameter description
				token.addParameter(this.createParameterDescription(type, name, bounds, values));
			}
		}
		
		
		// get token
		return token;
	}
	
	/**
	 * 
	 * @param timeline
	 * @param startTimeBounds
	 * @param endTimeBounds
	 * @param durationBounds
	 * @return
	 */
	public UnallocatedTokenDescription createUndefinedTokenDescriptor(TimelineProtocolDescriptor timeline, 
			long[] startTimeBounds, long[] endTimeBounds, long[] durationBounds) 
	{
		// initialize descriptor
		UnallocatedTokenDescription token = new UnallocatedTokenDescription(getNextTokenId(), timeline);
		// set temporal information
		token.setTemporalInformation(startTimeBounds, endTimeBounds, durationBounds);
		// get token
		return token;
	}
	
	/**
	 * 
	 * @param component
	 * @param name
	 * @param external
	 * @return
	 */
	public TimelineProtocolDescriptor createTimelineDescriptor(String component, String name, boolean external) {
		// create timeline
		return new TimelineProtocolDescriptor(getNextTimelineId(), component, name, external);
	}
	
	/**
	 * 
	 * @param horizon
	 * @return
	 */
	public PlanProtocolDescriptor createPlanDescriptor(long origin, long horizon) {
		return new PlanProtocolDescriptor(origin, horizon);
	}
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @param bounds
	 * @param values
	 * @return
	 */
	private EPSLParameterDescriptor createParameterDescription(ParameterTypeDescriptor type, String name, long[] bounds, String[] values) {
		EPSLParameterDescriptor param = new EPSLParameterDescriptor(getNextParameterId(), type);
		param.setName(name);
		param.setBounds(bounds);
		param.setValues(values);
		return param;
	}
	
	/**
	 * 
	 * @param type
	 * @param from
	 * @param to
	 * @return
	 */
	public RelationProtocolDescriptor createRelationDescriptor(String type, TokenProtocolDescriptor from, TokenProtocolDescriptor to) {
		return this.relFactory.create(type, from, to);
	}
	
	/**
	 * 
	 * @return
	 */
	protected static synchronized int getNextTokenId() {
		return TOKEN_ID_COUNTER++;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static synchronized int getNextParameterId() {
		return TOKEN_PARAMETER_ID_COUNTER++;
	}
	
	/**
	 * 
	 * @return
	 */
	protected static synchronized int getNextTimelineId() {
		return TIMELINE_ID_COUNTER++;
	}
}
