package it.istc.pst.platinum.framework.protocol.lang;

import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.protocol.lang.relation.RelationProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.relation.RelationProtocolLanguageFactory;


/**
 * 
 * @author alessandroumbrico
 *
 */
public class ProtocolLanguageFactory 
{
	private int tokenIdCounter;
	private int tokenParameterIdCounter;
	private int timelineIdCounter;
	private RelationProtocolLanguageFactory relFactory;
	
	/**
	 * Private Factory instance constructor
	 */
	public ProtocolLanguageFactory(long horizon) {
		this.tokenIdCounter = 0;
		this.tokenParameterIdCounter = 0;
		this.timelineIdCounter = 0;
		this.relFactory = new RelationProtocolLanguageFactory(horizon);
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
	 * @param status
	 * @return
	 */
	public TokenProtocolDescriptor createTokenDescriptor(TimelineProtocolDescriptor timeline, String predicate,
			long[] startTimeBounds, long[] endTimeBounds, long[] durationBounds,
			String[] paramNames, ParameterTypeDescriptor[] paramTypes, long[][] paramBounds, String[][] paramValues, ExecutionNodeStatus status) 
	{
		// initialize token
		TokenProtocolDescriptor token = new TokenProtocolDescriptor(this.getNextTokenId(), timeline, predicate, status);
		
		
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
		UnallocatedTokenDescription token = new UnallocatedTokenDescription(this.getNextTokenId(), timeline);
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
	 * @param name
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public PlanProtocolDescriptor createPlanDescriptor(String name, long origin, long horizon) {
		return new PlanProtocolDescriptor(name, origin, horizon);
	}
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @param bounds
	 * @param values
	 * @return
	 */
	private ParameterDescriptor createParameterDescription(ParameterTypeDescriptor type, String name, long[] bounds, String[] values) {
		ParameterDescriptor param = new ParameterDescriptor(getNextParameterId(), type);
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
	protected synchronized int getNextTokenId() {
		return this.tokenIdCounter++;
	}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized int getNextParameterId() {
		return this.tokenParameterIdCounter++;
	}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized int getNextTimelineId() {
		return this.timelineIdCounter++;
	}
}
