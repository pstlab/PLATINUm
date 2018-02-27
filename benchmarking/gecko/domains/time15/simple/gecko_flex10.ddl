DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 500], 500;
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (Idle(), Channel_F_B(), Channel_B_F())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_B_F();
		}
		
		VALUE Channel_F_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable CrossTransferEngineType (Up(), Down(), Changing())
	{
		VALUE Down() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Up() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [5, 5]
		MEETS {
			Up();
			Down();
		}
	}
	
	COMP_TYPE SingletonStateVariable CoordinationPortType (Idle(), Sending(), Receiving())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Sending();
			Receiving();
		}
		
		VALUE Sending() [5, 15]
		MEETS {
			Idle();
		}
		
		VALUE Receiving() [5, 15]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ConveyorEngineType (Still(), Forward(), Backward())
	{
		VALUE Still() [1, +INF]
		MEETS {
			Forward();
			Backward();
		}
		
		VALUE Forward() [1, +INF]
		MEETS {
			Still();
		}
		
		VALUE Backward() [1, +INF]
		MEETS {
			Still();
		}
	}
	
	COMP_TYPE SingletonStateVariable NeighborStatusType (Available(), NotAvailable())
	{
		VALUE Available() [1, +INF]
		MEETS {
			NotAvailable();
		}
		
		VALUE NotAvailable() [1, +INF]
		MEETS {
			Available();
		}
	}
	
	COMPONENT TM_Channel {FLEXIBLE channel(trex_internal_dispatch_asap)} : ChannelFunctionalityType;
	COMPONENT TM_ConveyorMain {FLEXIBLE main(trex_internal_dispatch_asap)} : ConveyorEngineType;
	COMPONENT TM_CoordinationPortF {FLEXIBLE portF(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortB {FLEXIBLE portB(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_NeighborF {FLEXIBLE neighborF(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborB {FLEXIBLE neighborB(uncontrollable)} : NeighborStatusType;
	
	SYNCHRONIZE TM_Channel.channel
	{
		VALUE Channel_F_B()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd3 <!> TM_CoordinationPortB.portB.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			END-END [0, 0] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd3 DURING [0, +INF] [0, +INF] cd6;
		}
		
		VALUE Channel_B_F()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 <!> TM_CoordinationPortF.portF.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			END-END [0, 0] cd2;
			
			cd0 DURING [0, +INF] [0, +INF] cd6;
			cd2 DURING [0, +INF] [0, +INF] cd5;
		}
	}
}