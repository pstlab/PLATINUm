DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 500], 500;
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (
		Idle(), 
		Channel_F_B(), Channel_F_R1(), Channel_F_R2(), Channel_F_R3(), Channel_F_L1(), Channel_F_L2(), Channel_F_L3(), 
		Channel_B_F(), Channel_B_R1(), Channel_B_R2(), Channel_B_R3(), Channel_B_L1(), Channel_B_L2(), Channel_B_L3(), 
		Channel_L1_R1(), Channel_L1_R2(), Channel_L1_R3(), Channel_L1_L2(), Channel_L1_L3(), Channel_L1_F(), Channel_L1_B(),
		Channel_R1_R2(), Channel_R1_R3(), Channel_R1_L1(), Channel_R1_L2(), Channel_R1_L3(), Channel_R1_F(), Channel_R1_B())
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
			
			Channel_L1_R1();
			Channel_L1_F();
			Channel_L1_R1();
			Channel_L1_R2();
			Channel_L1_R3();
			Channel_L1_L2();
			Channel_L1_L3();
			
			Channel_R1_L1();
			Channel_R1_L2();
			Channel_R1_L3();
			Channel_R1_R2();
			Channel_R1_R3();
			Channel_R1_F();
			Channel_R1_B();
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
		
		VALUE Channel_F_L3() [1, +INF]
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
		
		VALUE Channel_F_R3() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [1, +INF]
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
		
		VALUE Channel_B_R3() [1, +INF]
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
		
		VALUE Channel_B_L3() [1, +INF]
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
		
		VALUE Channel_R1_L3() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R1_R3() [1, +INF]
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
		
		VALUE Channel_L1_R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_R3() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_L2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L1_L3() [1, +INF]
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
	}
	
	COMP_TYPE SingletonStateVariable CrossTransferEngineType (Up(), Down(), MovingUp(), MovingDown())
	{
		VALUE Down() [1, +INF]
		MEETS {
			MovingUp();
		}
		
		VALUE Up() [1, +INF]
		MEETS {
			MovingDown();
		}
		
		VALUE MovingUp() [5, 5]
		MEETS {
			Up();
		}
		
		VALUE MovingDown() [5, 5]
		MEETS {
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
	
	COMPONENT TM_CrossTransfer1 {FLEXIBLE cross1(trex_internal_dispatch_asap)} : CrossTransferEngineType;
	COMPONENT TM_CrossTransfer2 {FLEXIBLE cross2(trex_internal_dispatch_asap)} : CrossTransferEngineType;
	COMPONENT TM_CrossTransfer3 {FLEXIBLE cross3(trex_internal_dispatch_asap)} : CrossTransferEngineType;
	
	COMPONENT TM_ConveyorMain {FLEXIBLE main(trex_internal_dispatch_asap)} : ConveyorEngineType;
	COMPONENT TM_ConveyorCross1 {FLEXIBLE cconveyor1(trex_internal_dispatch_asap)} : ConveyorEngineType;
	COMPONENT TM_ConveyorCross2 {FLEXIBLE cconveyor2(trex_internal_dispatch_asap)} : ConveyorEngineType;
	COMPONENT TM_ConveyorCross3 {FLEXIBLE cconveyor3(trex_internal_dispatch_asap)} : ConveyorEngineType;
	
	COMPONENT TM_CoordinationPortF {FLEXIBLE portF(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortB {FLEXIBLE portB(trex_internal_dispatch_asap)} : CoordinationPortType;
	
	COMPONENT TM_CoordinationPortL1 {FLEXIBLE portL1(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortL2 {FLEXIBLE portL2(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortL3 {FLEXIBLE portL3(trex_internal_dispatch_asap)} : CoordinationPortType;
	
	COMPONENT TM_CoordinationPortR1 {FLEXIBLE portR1(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortR2 {FLEXIBLE portR2(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortR3 {FLEXIBLE portR3(trex_internal_dispatch_asap)} : CoordinationPortType;
	
	COMPONENT TM_NeighborF {FLEXIBLE neighborF(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborB {FLEXIBLE neighborB(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborL {FLEXIBLE neighborL(uncontrollable)} : NeighborStatusType;
	COMPONENT TM_NeighborR {FLEXIBLE neighborR(uncontrollable)} : NeighborStatusType;
	
	SYNCHRONIZE TM_Channel.channel
	{
		VALUE Channel_F_B()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd21 TM_CrossTransfer1.cross1.Down();
			cd22 TM_CrossTransfer2.cross2.Down();
			cd23 TM_CrossTransfer3.cross3.Down();
			cd3 <!> TM_CoordinationPortB.portB.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd21;
			DURING [0, +INF] [0, +INF] cd22;
			DURING [0, +INF] [0, +INF] cd23;
			END-END [0, 0] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd3 DURING [0, +INF] [0, +INF] cd6;
		}
		
		VALUE Channel_F_R1()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd3 <!> TM_CrossTransfer1.cross1.Up();
			cd7 <!> TM_ConveyorCross1.cconveyor1.Forward();
			cd4 <!> TM_CoordinationPortR1.portR1.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			cd0 DURING [0, +INF] [0, +INF] cd2;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_F_R2()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 <!> TM_CrossTransfer2.cross2.Up();
			cd7 <!> TM_ConveyorCross2.cconveyor2.Forward();
			cd4 <!> TM_CoordinationPortR2.portR2.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd32;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
		}
		
		VALUE Channel_F_R3()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 TM_CrossTransfer3.cross3.Down();
			cd33 <!> TM_CrossTransfer3.cross3.Up();
			cd7 <!> TM_ConveyorCross3.cconveyor3.Forward();
			cd4 <!> TM_CoordinationPortR3.portR3.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd33;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			CONTAINS [0, +INF] [0, +INF] cd33;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
			cd32 BEFORE [0, +INF] cd33;
		}
		
		VALUE Channel_F_L1()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd3 <!> TM_CrossTransfer1.cross1.Up();
			cd7 <!> TM_ConveyorCross1.cconveyor1.Backward();
			cd4 <!> TM_CoordinationPortL1.portL1.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			cd0 DURING [0, +INF] [0, +INF] cd2;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_F_L2()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 <!> TM_CrossTransfer2.cross2.Up();
			cd7 <!> TM_ConveyorCross2.cconveyor2.Backward();
			cd4 <!> TM_CoordinationPortL2.portL2.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd32;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
		}
		
		VALUE Channel_F_L3()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 TM_CrossTransfer3.cross3.Down();
			cd33 <!> TM_CrossTransfer3.cross3.Up();
			cd7 <!> TM_ConveyorCross3.cconveyor3.Backward();
			cd4 <!> TM_CoordinationPortL3.portL3.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd33;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			CONTAINS [0, +INF] [0, +INF] cd33;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
			cd32 BEFORE [0, +INF] cd33;
		}
		
		VALUE Channel_B_F()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd21 TM_CrossTransfer1.cross1.Down();
			cd22 TM_CrossTransfer2.cross2.Down();
			cd23 TM_CrossTransfer3.cross3.Down();
			cd3 <!> TM_CoordinationPortF.portF.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd21;
			DURING [0, +INF] [0, +INF] cd22;
			DURING [0, +INF] [0, +INF] cd23;
			END-END [0, 0] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd6;
			cd3 DURING [0, +INF] [0, +INF] cd5;
		}
		
		VALUE Channel_B_R1()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd21 TM_CrossTransfer3.cross3.Down();
			cd22 TM_CrossTransfer2.cross2.Down();
			cd23 TM_CrossTransfer1.cross1.Down();
			cd3 <!> TM_CrossTransfer1.cross1.Up();
			cd4 <!> TM_ConveyorCross1.cconveyor1.Forward();
			cd5 <!> TM_CoordinationPortR1.portR1.Sending();
			
			cd6 <?> TM_NeighborR.neighborR.Available();
			cd7 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd5;
			
			CONTAINS [0, +INF] [0, +INF] cd21; 
			CONTAINS [0, +INF] [0, +INF] cd22;
			CONTAINS [0, +INF] [0, +INF] cd23;
			
			cd21 BEFORE [0, +INF] cd22;
			cd22 BEFORE [0, +INF] cd23;
			cd23 BEFORE [0, +INF] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd7;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd5 DURING [0, +INF] [0, +INF] cd6;
			cd5 DURING [0, +INF] [0, +INF] cd4;
			cd5 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_B_R2()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer3.cross3.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 <!> TM_CrossTransfer2.cross2.Up();
			cd7 <!> TM_ConveyorCross2.cconveyor2.Forward();
			cd4 <!> TM_CoordinationPortR2.portR2.Sending();
			
			cd5 <?> TM_NeighborB.neighborB.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd32;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
		}
		
		VALUE Channel_B_R3()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer3.cross3.Down();
			cd3 <!> TM_CrossTransfer3.cross3.Up();
			cd7 <!> TM_ConveyorCross3.cconveyor3.Forward();
			cd4 <!> TM_CoordinationPortR3.portR3.Sending();
			
			cd5 <?> TM_NeighborB.neighborB.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			cd0 DURING [0, +INF] [0, +INF] cd2;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_B_L1()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd21 TM_CrossTransfer3.cross3.Down();
			cd22 TM_CrossTransfer2.cross2.Down();
			cd23 TM_CrossTransfer1.cross1.Down();
			cd3 <!> TM_CrossTransfer1.cross1.Up();
			cd4 <!> TM_ConveyorCross1.cconveyor1.Backward();
			cd5 <!> TM_CoordinationPortL1.portL1.Sending();
			
			cd6 <?> TM_NeighborL.neighborL.Available();
			cd7 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd5;
			
			CONTAINS [0, +INF] [0, +INF] cd21; 
			CONTAINS [0, +INF] [0, +INF] cd22;
			CONTAINS [0, +INF] [0, +INF] cd23;
			
			cd21 BEFORE [0, +INF] cd22;
			cd22 BEFORE [0, +INF] cd23;
			cd23 BEFORE [0, +INF] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd7;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd5 DURING [0, +INF] [0, +INF] cd6;
			cd5 DURING [0, +INF] [0, +INF] cd4;
			cd5 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_B_L2()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 <!> TM_CrossTransfer2.cross2.Up();
			cd7 <!> TM_ConveyorCross2.cconveyor2.Backward();
			cd4 <!> TM_CoordinationPortL2.portL2.Sending();
			
			cd5 <?> TM_NeighborB.neighborB.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd32;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
		}
		
		VALUE Channel_B_L3()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer1.cross1.Down();
			cd31 TM_CrossTransfer2.cross2.Down();
			cd32 TM_CrossTransfer3.cross3.Down();
			cd33 <!> TM_CrossTransfer3.cross3.Up();
			cd7 <!> TM_ConveyorCross3.cconveyor3.Backward();
			cd4 <!> TM_CoordinationPortL3.portL3.Sending();
			
			cd5 <?> TM_NeighborB.neighborB.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd33;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd31;
			CONTAINS [0, +INF] [0, +INF] cd32;
			
			cd2 BEFORE [0, +INF] cd31;
			cd31 BEFORE [0, +INF] cd32;
			cd32 BEFORE [0, +INF] cd33;
		}
		
		VALUE Channel_L1_R1()
		{
			cd0 <!> TM_CoordinationPortL1.portL1.Receiving();
			cd1 <!> TM_CoordinationPortR1.portR1.Sending();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd4 TM_ConveyorCross1.cconveyor1.Forward();
			
			cd5 <?> TM_NeighborL.neighborL.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			DURING [0, +INF] [0, +INF] cd5;
			DURING [0, +INF] [0, +INF] cd6; 
			END-END [0, 0] cd1;
		}
		
		VALUE Channel_L1_F()
		{
			cd0 <!> TM_CoordinationPortL1.portL1.Receiving();
			cd1 <!> TM_CoordinationPortF.portF.Sending();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd4 <!> TM_CrossTransfer1.cross1.Down();
			
			cd5 TM_ConveyorCross1.cconveyor1.Forward();
			cd6 <!> TM_ConveyorMain.main.Backward();
			
			cd7 <?> TM_NeighborL.neighborL.Available();
			cd8 <?> TM_NeighborF.neighborF.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd7;
			
			cd3 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_L1_B()
		{
			cd0 <!> TM_CoordinationPortL1.portL1.Receiving();
			cd1 <!> TM_CoordinationPortB.portB.Sending();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd41 <!> TM_CrossTransfer1.cross1.Down();
			cd42 TM_CrossTransfer2.cross2.Down();
			cd43 TM_CrossTransfer3.cross3.Down();
			
			cd5 TM_ConveyorCross1.cconveyor1.Forward();
			cd6 <!> TM_ConveyorMain.main.Forward();
			
			cd7 <?> TM_NeighborL.neighborL.Available();
			cd8 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd41;
			CONTAINS [0, +INF] [0, +INF] cd42;
			CONTAINS [0, +INF] [0, +INF] cd43;
			
			cd3 BEFORE [0, +INF] cd41;
			cd41 BEFORE [0, +INF] cd42;
			cd42 BEFORE [0, +INF] cd43;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd7;
		}
		
		VALUE Channel_R1_L1()
		{
			cd0 <!> TM_CoordinationPortL1.portL1.Sending();
			cd1 <!> TM_CoordinationPortR1.portR1.Receiving();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd4 TM_ConveyorCross1.cconveyor1.Backward();
			
			cd5 <?> TM_NeighborL.neighborL.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd1;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			DURING [0, +INF] [0, +INF] cd5;
			DURING [0, +INF] [0, +INF] cd6;
			END-END [0, 0] cd0;
			
			cd1 BEFORE [0, +INF] cd0;
		}
		
		VALUE Channel_R1_F()
		{
			cd0 <!> TM_CoordinationPortR1.portR1.Receiving();
			cd1 <!> TM_CoordinationPortF.portF.Sending();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd4 <!> TM_CrossTransfer1.cross1.Down();
			
			cd5 TM_ConveyorCross1.cconveyor1.Backward();
			cd6 <!> TM_ConveyorMain.main.Backward();
			
			cd7 <?> TM_NeighborR.neighborR.Available();
			cd8 <?> TM_NeighborF.neighborF.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd7;
			
			cd3 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_R1_B()
		{
			cd0 <!> TM_CoordinationPortR1.portR1.Receiving();
			cd1 <!> TM_CoordinationPortB.portB.Sending();
			
			cd3 TM_CrossTransfer1.cross1.Up();
			cd41 <!> TM_CrossTransfer1.cross1.Down();
			cd42 TM_CrossTransfer2.cross2.Down();
			cd43 TM_CrossTransfer3.cross3.Down();
			
			cd5 TM_ConveyorCross1.cconveyor1.Backward();
			cd6 <!> TM_ConveyorMain.main.Forward();
			
			cd7 <?> TM_NeighborR.neighborR.Available();
			cd8 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd41;
			CONTAINS [0, +INF] [0, +INF] cd42;
			CONTAINS [0, +INF] [0, +INF] cd43;
			
			cd3 BEFORE [0, +INF] cd41;
			cd41 BEFORE [0, +INF] cd42;
			cd42 BEFORE [0, +INF] cd43;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd8;
		}
	}
}