DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 500], 500;
	
	COMP_TYPE SingletonStateVariable ChannelFunctionalityType (
		Idle(), 
		Channel_F_B(), Channel_F_R(), Channel_F_L(), 
		Channel_B_F(), Channel_B_R(), Channel_B_L(), 
		Channel_L_R(), Channel_L_F(), Channel_L_B(),
		Channel_R_L(), Channel_R_F(), Channel_R_B())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Channel_F_B();
			Channel_F_L();
			Channel_F_R();
			
			Channel_B_F();
			Channel_B_L();
			Channel_B_R();
			
			Channel_L_R();
			Channel_L_F();
			Channel_L_R();
			
			Channel_R_L();
			Channel_R_F();
			Channel_R_B();
		}
		
		VALUE Channel_F_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_L() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_F_R() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_R() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_B_L() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_L() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_R_B() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_R() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_F() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Channel_L_B() [1, +INF]
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
		
		VALUE Sending() [5, 35]
		MEETS {
			Idle();
		}
		
		VALUE Receiving() [5, 35]
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
	
	COMPONENT TM_CrossTransfer {FLEXIBLE cross(trex_internal_dispatch_asap)} : CrossTransferEngineType;
	
	COMPONENT TM_ConveyorMain {FLEXIBLE main(trex_internal_dispatch_asap)} : ConveyorEngineType;
	COMPONENT TM_ConveyorCross {FLEXIBLE cconveyor(trex_internal_dispatch_asap)} : ConveyorEngineType;
	
	COMPONENT TM_CoordinationPortF {FLEXIBLE portF(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortB {FLEXIBLE portB(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortL {FLEXIBLE portL(trex_internal_dispatch_asap)} : CoordinationPortType;
	COMPONENT TM_CoordinationPortR {FLEXIBLE portR(trex_internal_dispatch_asap)} : CoordinationPortType;
	
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
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CoordinationPortB.portB.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			END-END [0, 0] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd3 DURING [0, +INF] [0, +INF] cd6;
		}
		
		VALUE Channel_F_R()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CrossTransfer.cross.Up();
			cd7 <!> TM_ConveyorCross.cconveyor.Forward();
			cd4 <!> TM_CoordinationPortR.portR.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd3;
			
			cd2 BEFORE [0, +INF] cd3;
		}
		
		VALUE Channel_F_L()
		{
			cd0 <!> TM_CoordinationPortF.portF.Receiving();
			cd1 TM_ConveyorMain.main.Backward();
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CrossTransfer.cross.Up();
			cd7 <!> TM_ConveyorCross.cconveyor.Backward();
			cd4 <!> TM_CoordinationPortL.portL.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborL.neighborL.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd4 DURING [0, +INF] [0, +INF] cd6;
			cd4 DURING [0, +INF] [0, +INF] cd7;
			cd4 DURING [0, +INF] [0, +INF] cd3;
			
			cd2 BEFORE [0, +INF] cd3;
		}
		
		VALUE Channel_B_F()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CoordinationPortF.portF.Sending();
			
			cd5 <?> TM_NeighborF.neighborF.Available();
			cd6 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			END-END [0, 0] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd6;
			cd3 DURING [0, +INF] [0, +INF] cd5;
		}
		
		VALUE Channel_B_R()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CrossTransfer.cross.Up();
			cd4 <!> TM_ConveyorCross.cconveyor.Forward();
			cd5 <!> TM_CoordinationPortR.portR.Sending();
			
			cd6 <?> TM_NeighborR.neighborR.Available();
			cd7 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd5;
			
			cd2 BEFORE [0, +INF] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd7;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd5 DURING [0, +INF] [0, +INF] cd6;
			cd5 DURING [0, +INF] [0, +INF] cd4;
			cd5 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_B_L()
		{
			cd0 <!> TM_CoordinationPortB.portB.Receiving();
			cd1 TM_ConveyorMain.main.Forward();
			cd2 TM_CrossTransfer.cross.Down();
			cd3 <!> TM_CrossTransfer.cross.Up();
			cd4 <!> TM_ConveyorCross.cconveyor.Backward();
			cd5 <!> TM_CoordinationPortL.portL.Sending();
			
			cd6 <?> TM_NeighborL.neighborL.Available();
			cd7 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd5;
			
			cd2 BEFORE [0, +INF] cd3;
			
			cd0 DURING [0, +INF] [0, +INF] cd7;
			cd0 DURING [0, +INF] [0, +INF] cd1;
			
			cd5 DURING [0, +INF] [0, +INF] cd6;
			cd5 DURING [0, +INF] [0, +INF] cd4;
			cd5 DURING [0, +INF] [0, +INF] cd3;
		}
		
		VALUE Channel_L_R()
		{
			cd0 <!> TM_CoordinationPortL.portL.Receiving();
			cd1 <!> TM_CoordinationPortR.portR.Sending();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 TM_ConveyorCross.cconveyor.Forward();
			
			cd5 <?> TM_NeighborL.neighborL.Available();
			cd6 <?> TM_NeighborR.neighborR.Available();
			
			START-START [0, 0] cd0;
			DURING [0, +INF] [0, +INF] cd3;
			DURING [0, +INF] [0, +INF] cd4;
			DURING [0, +INF] [0, +INF] cd5;
			DURING [0, +INF] [0, +INF] cd6; 
			END-END [0, 0] cd1;
		}
		
		VALUE Channel_L_F()
		{
			cd0 <!> TM_CoordinationPortL.portL.Receiving();
			cd1 <!> TM_CoordinationPortF.portF.Sending();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 <!> TM_CrossTransfer.cross.Down();
			
			cd5 TM_ConveyorCross.cconveyor.Forward();
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
		
		VALUE Channel_L_B()
		{
			cd0 <!> TM_CoordinationPortL.portL.Receiving();
			cd1 <!> TM_CoordinationPortB.portB.Sending();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 <!> TM_CrossTransfer.cross.Down();
			
			cd5 TM_ConveyorCross.cconveyor.Forward();
			cd6 <!> TM_ConveyorMain.main.Forward();
			
			cd7 <?> TM_NeighborL.neighborL.Available();
			cd8 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd7;
			
			cd3 BEFORE [0, +INF] cd4;
		}
		
		VALUE Channel_R_L()
		{
			cd0 <!> TM_CoordinationPortL.portL.Sending();
			cd1 <!> TM_CoordinationPortR.portR.Receiving();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 TM_ConveyorCross.cconveyor.Backward();
			
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
		
		VALUE Channel_R_F()
		{
			cd0 <!> TM_CoordinationPortR.portR.Receiving();
			cd1 <!> TM_CoordinationPortF.portF.Sending();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 <!> TM_CrossTransfer.cross.Down();
			
			cd5 TM_ConveyorCross.cconveyor.Backward();
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
		
		VALUE Channel_R_B()
		{
			cd0 <!> TM_CoordinationPortR.portR.Receiving();
			cd1 <!> TM_CoordinationPortB.portB.Sending();
			
			cd3 TM_CrossTransfer.cross.Up();
			cd4 <!> TM_CrossTransfer.cross.Down();
			
			cd5 TM_ConveyorCross.cconveyor.Backward();
			cd6 <!> TM_ConveyorMain.main.Forward();
			
			cd7 <?> TM_NeighborR.neighborR.Available();
			cd8 <?> TM_NeighborB.neighborB.Available();
			
			START-START [0, 0] cd0;
			END-END [0, 0] cd1;
			
			cd0 DURING [0, +INF] [0, +INF] cd3;
			cd0 DURING [0, +INF] [0, +INF] cd5;
			cd0 DURING [0, +INF] [0, +INF] cd7;
			
			cd1 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF] [0, +INF] cd8;
			
			cd3 BEFORE [0, +INF] cd4;
		}
	}
}