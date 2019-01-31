package it.istc.pst.platinum.framework.protocol.lang.relation;

import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class RelationProtocolLanguageFactory 
{
	private static RelationProtocolLanguageFactory INSTANCE = null;
	private long horizon;
	
	/**
	 * 
	 * @param horizon
	 */
	private RelationProtocolLanguageFactory(long horizon) {
		this.horizon = horizon;
	}
	
	/**
	 * 
	 * @param horizon
	 * @return
	 */
	public static RelationProtocolLanguageFactory getSingletonInstance(long horizon) {
		if (INSTANCE == null) {
			INSTANCE = new RelationProtocolLanguageFactory(horizon);
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param type
	 * @param from
	 * @param to
	 * @return
	 */
	public RelationProtocolDescriptor create(String type, TokenProtocolDescriptor from, TokenProtocolDescriptor to) {
		// check type
		switch (type.toLowerCase()) {
		
			case "meets" : {
				// get meets relation
				return new MeetsRelationProtocolDescriptor(from, to);
			}
			
			case "before" : {
				// get before relation
				return new BeforeRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "during" : {
				// get during relation
				return new DuringRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "contains" : {
				// get contains relation
				return new ContainsRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "after" : {
				// get after relation
				return new AfterRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "equals" : {
				// get equals relation
				return new EqualsRelationProtocolDescriptor(from, to);
			}
			
			case "end_end" : {
				// get end-end relation
				return new EndEndRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "start_start" : {
				// get start-start relation
				return new StartStartRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "starts_during" : {
				// get starts-during relation
				return new StartsDuringRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "ends_during" : {
				// get ends-during relation
				return new EndsDuringRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "overlappedby" : {
				// get overlapped-by relation
				return new OverlappedByRelationProtocolDescriptor(from, to, this.horizon);
			}
			
			case "met_by" : {
				// get met by relation
				return new MeetsRelationProtocolDescriptor(to, from);
//				return new MetByRelationProtocolDescriptor(to, from);
			}
			
			case "starts" : {
				// get starts relation
				return new StartsRelationProtocolDescriptor(to, from);
			}
 			
			default : {
				throw new RuntimeException("Unknown relation type= " + type);
			}
		}
	}
}
