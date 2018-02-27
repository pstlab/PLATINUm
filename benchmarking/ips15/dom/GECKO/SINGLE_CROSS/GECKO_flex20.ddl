DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 200], 500;
	
	COMP_TYPE RenewableResource EnergyConsumptionTraceType(10)
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (Idle(), Channel_F_B(), Channel_B_F(), Channel_L_R(), Channel_R_L(), Channel_F_R(), Channel_F_L(), Channel_B_L(), Channel_B_R(), Channel_R_F(), Channel_R_B(), Channel_L_F(), Channel_L_B())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_F_R();
			Channel_F_L();
			
			Channel_B_F();
			Channel_B_L();
			Channel_B_R();
			
			Channel_L_R();
			Channel_L_F();
			Channel_L_B();
			
			Channel_R_L();
			Channel_R_F();
			Channel_R_B();
		}
		
		VALUE Channel_F_B() [10, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [10, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_R() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_L() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_B() [7, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ChangeOverEngineType (CO_FB(), CO_BF(), Changing(), CO_RL(), CO_LR())
	{
		VALUE CO_FB() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_BF() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_RL() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_LR() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [5, 25]
		MEETS {
			CO_FB();
			CO_BF();
			CO_RL();
			CO_LR();
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
	COMPONENT TM_NeighborR {FLEXIBLE neighborR(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborL {FLEXIBLE neighborL(uncontrollable)} : NeighborStatusType;
	
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
		
		VALUE Channel_F_R()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_LR();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd0 BEFORE [0, +INF] cd1;			
		}
		
		VALUE Channel_F_L()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_RL();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd0 BEFORE [0, +INF] cd1;			
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
		
		VALUE Channel_B_R()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_LR();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd0 BEFORE [0, +INF] cd1;			
		}
		
		VALUE Channel_B_L()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_RL();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd0 BEFORE [0, +INF] cd1;			
		}
		
		VALUE Channel_R_L()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_RL();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_RL();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_BF();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd0 BEFORE [0, +INF] cd1;	
		}
		
		VALUE Channel_R_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_RL();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_FB();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd0 BEFORE [0, +INF] cd1;	
		}
		
		VALUE Channel_L_R()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_LR();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_LR();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_BF();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd0 BEFORE [0, +INF] cd1;	
		}
		
		VALUE Channel_L_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_RL();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_FB();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;			
			
			cd0 BEFORE [0, +INF] cd1;
		}
	}
}