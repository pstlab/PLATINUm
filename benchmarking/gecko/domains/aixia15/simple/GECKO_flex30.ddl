DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 200], 500;
	
	COMP_TYPE RenewableResource EnergyConsumptionTraceType(10)
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (Idle(), Channel_F_B(), Channel_B_F())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_B_F();
		}
		
		VALUE Channel_F_B() [10, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [10, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ChangeOverEngineType (CO_FB(), Changing(), CO_BF())
	{
		VALUE CO_FB() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_BF() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [15, 45]
		MEETS {
			CO_FB();
			CO_BF();
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
	COMPONENT TM_EnergyTrace {ESTA_LIGHT_MAX_CONSUMPTION energy(trex_external)} : EnergyConsumptionTraceType;
	COMPONENT TM_ChangeOverTrace {FLEXIBLE change_over(trex_internal_dispatch_asap)} : ChangeOverEngineType;
	COMPONENT TM_NeighborF {FLEXIBLE neighborF(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborB {FLEXIBLE neighborB(uncontrollable)} : NeighborStatusType;
	
	SYNCHRONIZE TM_Channel.channel
	{
		VALUE Channel_F_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_B_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
	}
}