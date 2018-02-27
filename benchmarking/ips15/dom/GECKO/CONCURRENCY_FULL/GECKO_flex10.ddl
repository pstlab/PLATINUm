DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 200], 500;
	
	COMP_TYPE RenewableResource EnergyConsumptionTraceType(10)
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (
		Idle(), 
		Channel_F_B(), Channel_F_L1(), Channel_F_L2(), Channel_F_L3(), Channel_F_R1(), Channel_F_R2(), Channel_F_R3(), 
		Channel_B_F(), Channel_B_L1(), Channel_B_L2(), Channel_B_L3(), Channel_B_R1(), Channel_B_R2(), Channel_B_R3(), 
		Channel_L1_F(), Channel_L1_B(), Channel_L1_L2(), Channel_L1_L3(), Channel_L1_R1(), Channel_L1_R2(), Channel_L1_R3(),
		Channel_L2_F(), Channel_L2_B(), Channel_L2_L1(), Channel_L2_L3(), Channel_L2_R1(), Channel_L2_R2(), Channel_L2_R3(),
		Channel_L3_F(), Channel_L3_B(), Channel_L3_L1(), Channel_L3_L2(), Channel_L3_R1(), Channel_L3_R2(), Channel_L3_R3(),
		Channel_R1_F(), Channel_R1_B(), Channel_R1_L1(), Channel_R1_L2(), Channel_R1_L3(), Channel_R1_R2(), Channel_R1_R3(),
		Channel_R2_F(), Channel_R2_B(), Channel_R2_L1(), Channel_R2_L2(), Channel_R2_L3(), Channel_R2_R1(), Channel_R2_R3(),
		Channel_R3_F(), Channel_R3_B(), Channel_R3_L1(), Channel_R3_L2(), Channel_R3_L3(), Channel_R3_R1(), Channel_R3_R2())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_F_L1();
			Channel_F_L2();
			Channel_F_L3();
			Channel_F_R1();
			Channel_F_R2();
			Channel_F_R3();
			
			Channel_B_F();
			Channel_B_L1();
			Channel_B_L2();
			Channel_B_L3();
			Channel_B_R1();
			Channel_B_R2();
			Channel_B_R3();
			
			Channel_L1_F();
			Channel_L1_B();
			Channel_L1_L2();
			Channel_L1_L3();
			Channel_L1_R1();
			Channel_L1_R2();
			Channel_L1_R3();
			
			Channel_L2_F();
			Channel_L2_B();
			Channel_L2_L1();
			Channel_L2_L3();
			Channel_L2_R1();
			Channel_L2_R2();
			Channel_L2_R3();
			
			Channel_L3_F();
			Channel_L3_B();
			Channel_L3_L1();
			Channel_L3_L2();
			Channel_L3_R1();
			Channel_L3_R2();
			Channel_L3_R3();
			
			Channel_R1_F();
			Channel_R1_B();
			Channel_R1_L1();
			Channel_R1_L2();
			Channel_R1_L3();
			Channel_R1_R2();
			Channel_R1_R3();
			
			Channel_R2_F(); 
			Channel_R2_B();
			Channel_R2_L1();
			Channel_R2_L2();
			Channel_R2_L3();
			Channel_R2_R1();
			Channel_R2_R3();
			
			Channel_R3_F(); 
			Channel_R3_B();
			Channel_R3_L1();
			Channel_R3_L2();
			Channel_R3_L3();
			Channel_R3_R1();
			Channel_R3_R2();
		}
		
		VALUE Channel_F_B() [10, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L1() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L2() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L3() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R1() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R2() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R3() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [10, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L1() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L2() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L3() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R1() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R2() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R3() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_L2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_L3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_L1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_L3() [5, +INF]
		MEETS {
			Idle();
		}
			
		VALUE Channel_L2_R1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_R2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L2_R3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_L1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_L2() [5, +INF]
		MEETS {
			Idle();
		}
			
		VALUE Channel_L3_R1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_R2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L3_R3() [5, +INF]
		MEETS {
			Idle();
		}
		
		
		
		VALUE Channel_R1_F() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_L1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_L2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_L3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_R2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_R3() [5, +INF]
		MEETS {
			Idle();
		}
			
		VALUE Channel_R2_F() [7, +INF]
		MEETS {
			Idle();
		}
		 
		VALUE Channel_R2_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_L1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_L2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_L3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_R1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R2_R3() [5, +INF]
		MEETS {
			Idle();
		}
		
		
		
		
		
		VALUE Channel_R3_F() [7, +INF]
		MEETS {
			Idle();
		}
		 
		VALUE Channel_R3_B() [7, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R3_L1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R3_L2() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R3_L3() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R3_R1() [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R3_R2() [5, +INF]
		MEETS {
			Idle();
		}
		
	}
	
	COMP_TYPE SingletonStateVariable CrossTransferType (Up(), Down(), Moving())
	{
		VALUE Up() [1, +INF]
		MEETS {
			Moving();
		}
		
		VALUE Down() [1, +INF]
		MEETS {
			Moving();
		}
		
		VALUE Moving() [5, 15]
		MEETS {
			Up();
			Down();
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
	
	COMPONENT TM_Channel_1 {FLEXIBLE channel1(trex_internal_dispatch_asap)} : ChannelFunctionalityType;
	COMPONENT TM_Channel_2 {FLEXIBLE channel2(trex_internal_dispatch_asap)} : ChannelFunctionalityType;
	COMPONENT TM_Channel_3 {FLEXIBLE channel3(trex_internal_dispatch_asap)} : ChannelFunctionalityType;
	
	COMPONENT TM_EnergyTrace {ESTA_LIGHT_MAX_CONSUMPTION energy(trex_external)} : EnergyConsumptionTraceType;
	
	COMPONENT TM_CrossTransfer_1 {FLEXIBLE cross1(trex_internal_dispatch_asap)} : CrossTransferType;
	COMPONENT TM_CrossTransfer_2 {FLEXIBLE cross2(trex_internal_dispatch_asap)} : CrossTransferType;
	COMPONENT TM_CrossTransfer_3 {FLEXIBLE cross3(trex_internal_dispatch_asap)} : CrossTransferType;
	
	COMPONENT TM_NeighborF {FLEXIBLE neighborF(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborB {FLEXIBLE neighborB(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborR {FLEXIBLE neighborR(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborL {FLEXIBLE neighborL(uncontrollable)} : NeighborStatusType;
	
	SYNCHRONIZE TM_Channel_1.channel1
	{
		VALUE Channel_F_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);

			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;	
			
			cd00 BEFORE [0, +INF] cd01;		
		}
		
		VALUE Channel_F_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;	
		}
		
		VALUE Channel_F_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_F_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_F_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_F()
		{
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd00 TM_CrossTransfer_3.cross3.Down();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_B_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_B_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_B_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);

			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_R1_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();	
			
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);

			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_R1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_R1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		VALUE Channel_R2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_L2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_R2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		
		
		
		VALUE Channel_R3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;

			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;	
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R3_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;			
		}
		
		VALUE Channel_R3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		
		
		
		VALUE Channel_L1_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_L1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		
		VALUE Channel_L2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();		
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_L2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_L3_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
	}
	
	
	
	SYNCHRONIZE TM_Channel_2.channel2
	{
		VALUE Channel_F_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);

			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;	
			
			cd00 BEFORE [0, +INF] cd01;		
		}
		
		VALUE Channel_F_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;	
		}
		
		VALUE Channel_F_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_F_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_F_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_F()
		{
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd00 TM_CrossTransfer_3.cross3.Down();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_B_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_B_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_B_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);

			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_R1_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();	
			
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);

			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_R1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_R1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		VALUE Channel_R2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_L2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_R2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		
		
		
		VALUE Channel_R3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;

			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;	
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R3_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;			
		}
		
		VALUE Channel_R3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		
		
		
		VALUE Channel_L1_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_L1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		
		VALUE Channel_L2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();		
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_L2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_L3_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
	}
	
	SYNCHRONIZE TM_Channel_3.channel3
	{
		VALUE Channel_F_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);

			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;	
			
			cd00 BEFORE [0, +INF] cd01;		
		}
		
		VALUE Channel_F_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;	
		}
		
		VALUE Channel_F_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_F_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_F_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_F_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborF.neighborF.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;		
			
			cd00 BEFORE [0, +INF] cd01;	
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_F()
		{
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd00 TM_CrossTransfer_3.cross3.Down();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd2 <?> TM_NeighborF.neighborF.Available();
			cd3 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;	
		}
		
		VALUE Channel_B_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_B_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_B_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 9);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_B_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 TM_CrossTransfer_2.cross2.Down();
			cd02 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);

			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		VALUE Channel_B_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Down();
			cd01 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborB.neighborB.Available();
			cd4 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_R1_L1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();	
			
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);

			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_R1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_R1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		VALUE Channel_R2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 6);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_L2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_R2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
		}
		
		
		
		
		VALUE Channel_R3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;

			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;	
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_L3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_R3_R1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_R3_R2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_R3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;			
		}
		
		VALUE Channel_R3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborR.neighborR.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		
		
		
		VALUE Channel_L1_R1()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L1_R2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_R3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_L2()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L1_L3()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			cd04 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L1_F()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 3);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;			
		}
		
		VALUE Channel_L1_B()
		{
			cd00 TM_CrossTransfer_1.cross1.Up();
			cd01 <!> TM_CrossTransfer_1.cross1.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 8);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		
		
		
		
		VALUE Channel_L2_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
				
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);

			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L2_R3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();		
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 10);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();	
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_L3()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			cd03 <!> TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L2_F()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd01;
		}
		
		VALUE Channel_L2_B()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;			
		}
		
		VALUE Channel_L3_R1()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_1.cross1.Down();
			cd03 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_R2()
		{
			cd00 TM_CrossTransfer_2.cross2.Up();
			cd01 <!> TM_CrossTransfer_2.cross2.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 4);
			
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_R3()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 2);
			cd2 <?> TM_NeighborR.neighborR.Available();
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			
			EQUALS cd1;
			
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L3_L1()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			cd04 <!> TM_CrossTransfer_1.cross1.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			CONTAINS [0, +INF] [0, +INF] cd04;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			cd03 BEFORE [0, +INF] cd04;
		}
		
		VALUE Channel_L3_L2()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 <!> TM_CrossTransfer_2.cross2.Up();
			
			cd1 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 5);
			cd3 <?> TM_NeighborL.neighborL.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd1;
			DURING [0, +INF] [0, +INF] cd3;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
			
		}
		
		VALUE Channel_L3_F()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			cd02 TM_CrossTransfer_2.cross2.Down();
			cd03 TM_CrossTransfer_1.cross1.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborF.neighborF.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			CONTAINS [0, +INF] [0, +INF] cd02;
			CONTAINS [0, +INF] [0, +INF] cd03;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
			cd01 BEFORE [0, +INF] cd02;
			cd02 BEFORE [0, +INF] cd03;
		}
		
		VALUE Channel_L3_B()
		{
			cd00 TM_CrossTransfer_3.cross3.Up();
			cd01 <!> TM_CrossTransfer_3.cross3.Down();
			
			cd2 <!> TM_EnergyTrace.energy.REQUIREMENT(?amount0 = 7);
			cd3 <?> TM_NeighborL.neighborL.Available();
			cd4 <?> TM_NeighborB.neighborB.Available();
			
			CONTAINS [0, +INF] [0, +INF] cd00;
			CONTAINS [0, +INF] [0, +INF] cd01;
			
			EQUALS cd2;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			
			cd00 BEFORE [0, +INF] cd01;
		}
	}
}