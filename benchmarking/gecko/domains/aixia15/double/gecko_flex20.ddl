DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 500], 500;
	
	COMP_TYPE RenewableResource EnergyConsumptionTraceType(10)
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (
		Idle(), 
		Channel_F_B(), Channel_F_L1(), Channel_F_L2(), Channel_F_R1(), Channel_F_R2(), 
		Channel_B_F(), Channel_B_L1(), Channel_B_L2(), Channel_B_R1(), Channel_B_R2(), 
		Channel_L1_F(), Channel_L1_B(), Channel_L1_L2(), Channel_L1_R1(), Channel_L1_R2(),
		Channel_L2_F(), Channel_L2_B(), Channel_L2_L1(), Channel_L2_R1(), Channel_L2_R2(), 
		Channel_R1_F(), Channel_R1_B(), Channel_R1_L1(), Channel_R1_L2(), Channel_R1_R2(),
		Channel_R2_F(), Channel_R2_B(), Channel_R2_L1(), Channel_R2_L2(), Channel_R2_R1())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_F_L1();
			Channel_F_L2();
			Channel_F_R1();
			Channel_F_R2();
			
			Channel_B_F();
			Channel_B_L1();
			Channel_B_L2();
			Channel_B_R1();
			Channel_B_R2();
			
			Channel_L1_F();
			Channel_L1_B();
			Channel_L1_L2();
			Channel_L1_R1();
			Channel_L1_R2();
			
			Channel_L2_F();
			Channel_L2_B();
			Channel_L2_L1();
			Channel_L2_R1();
			Channel_L2_R2();
			
			Channel_R1_F();
			Channel_R1_B();
			Channel_R1_L1();
			Channel_R1_L2();
			Channel_R1_R2();
			
			Channel_R2_F(); 
			Channel_R2_B();
			Channel_R2_L1();
			Channel_R2_L2();
			Channel_R2_R1();
		}
		
		VALUE Channel_F_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		
		VALUE Channel_B_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		
		
		VALUE Channel_L1_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_L1() [1, +INF]
		MEETS {
			Idle();
		}
			
		VALUE Channel_L2_R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		
		VALUE Channel_R1_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_L1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_R2() [1, +INF]
		MEETS {
			Idle();
		}
			
		VALUE Channel_R2_F() [1, +INF]
		MEETS {
			Idle();
		}
		 
		VALUE Channel_R2_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_L1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_R1() [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ChangeOverEngineType (Changing(), 
		CO_FB(), CO_BF(), 
		CO_R1L1(), CO_L1R1(),
		CO_R2L2(), CO_L2R2())
	{
		VALUE CO_FB() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_BF() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_R1L1() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_L1R1() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE CO_R2L2() [1, +INF]
		MEETS {
			Changing();
		} 
		
		VALUE CO_L2R2() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [5, 25]
		MEETS {
			CO_FB(); 
			CO_BF(); 
			CO_R1L1();
			CO_L1R1();
			CO_R2L2();
			CO_L2R2();
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
		
		VALUE Channel_F_R1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_L1R1();
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
		
		VALUE Channel_F_R2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_L2R2();
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd0 BEFORE [0, +INF] cd1;			
		}
		
		VALUE Channel_F_L1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_R1L1();
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
		
		VALUE Channel_F_L2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_FB();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_R2L2();
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
		
		VALUE Channel_B_R1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_L1R1();
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
		
		VALUE Channel_B_R2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_L2R2();
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
		
		VALUE Channel_B_L1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_R1L1();
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
		
		VALUE Channel_B_L2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_BF();
			cd1 <!> TM_ChangeOverTrace.change_over.CO_R2L2();
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
		
		
		
		
		
		
		VALUE Channel_R1_L1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R1_L2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();
			cd4 <!> TM_ChangeOverTrace.change_over.CO_R2L2();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd4;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_R1_R2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();
			cd3 <!>	TM_ChangeOverTrace.change_over.CO_L2R2();
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd3;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd0 BEFORE [0, +INF] cd3;
		}
		
		VALUE Channel_R1_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();
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
		
		VALUE Channel_R1_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();
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
		
		
		
		VALUE Channel_R2_L1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R2L2();
			cd4 <!>	TM_ChangeOverTrace.change_over.CO_R1L1();
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd4;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_R2_L2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R2L2();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R2_R1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R2L2();
			cd3 <!>	TM_ChangeOverTrace.change_over.CO_L1R1();
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd3;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd0 BEFORE [0, +INF] cd3;
		}
		
		VALUE Channel_R2_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R2L2();
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
		
		VALUE Channel_R2_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R2L2();
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
		
		
		
		
		
		VALUE Channel_L1_R1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L1R1();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L1_R2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L1R1();
			cd4 <!> TM_ChangeOverTrace.change_over.CO_L2R2();
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd4;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_L1_L2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L1R1();
			cd2 <!> TM_ChangeOverTrace.change_over.CO_R2L2();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd2;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd2;
		}
		
		VALUE Channel_L1_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L1R1();
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
		
		VALUE Channel_L1_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_R1L1();
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
		
		
		VALUE Channel_L2_R1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L2R2();
			cd4 <!> TM_ChangeOverTrace.change_over.CO_L1R1();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd4;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_L2_R2()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L2R2();
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L2_L1()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L2R2();
			cd2 <!> TM_ChangeOverTrace.change_over.CO_R1L1();	
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd2;
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd0 BEFORE [0, +INF] cd2;
		}
		
		VALUE Channel_L2_F()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L2R2();
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
		
		VALUE Channel_L2_B()
		{
			cd0 TM_ChangeOverTrace.change_over.CO_L2R2();
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