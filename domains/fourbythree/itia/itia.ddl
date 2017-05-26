DOMAIN FourByThree_ITIA
{
	// one unit is half-second
	TEMPORAL_MODULE temporal_module = [0, 50], 300;
	
	COMP_TYPE SingletonStateVariable HRCType (
		None(),
		HRC_ITIA()) 
	{
		VALUE None() [1, +INF]
		MEETS {
			HRC_ITIA();
		}
		
		VALUE HRC_ITIA() [1, +INF]
		MEETS {
			None();
		}
	}
	
	COMP_TYPE SingletonStateVariable HumanType (
		Idle(),
		_H1(), _H2(), _H3(), _H4())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_H1();
			_H2();
			_H3();
			_H4();
		}
		
		VALUE _H1() [4, 8]
		MEETS {
			Idle();			
		}
		
		VALUE _H2() [13, 18]
		MEETS {
			Idle();
		}
		
		VALUE _H3() [3, 7]
		MEETS {
			Idle();
		}
		
		VALUE _H4() [3, 7]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotControllerType (
		Idle(), 
		R1(), R2(), R3(), R4()) 
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			R1();
			R2();
			R3();
			R4();
		}
		
		VALUE R1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R3() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R4() [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable HRInteractionType (
		None(),
		Interaction-H1(), Interaction-H2(), Interaction-H3(), Interaction-H4())
	{
		VALUE None() [1, +INF]
		MEETS {
			Interaction-H1(); 
			Interaction-H2(); 
			Interaction-H3(); 
			Interaction-H4();
		}
		
		VALUE Interaction-H1() [1, +INF]
		MEETS {
			None();
		}
		
		VALUE Interaction-H2() [1, +INF]
		MEETS {
			None();
		}
		
		VALUE Interaction-H3() [1, +INF]
		MEETS {
			None();
		}
		
		VALUE Interaction-H4() [1, +INF]
		MEETS {
			None();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotToolHandlerType (
		Changing(),
		Mounted-T1(), Mounted-T2())
	{
		VALUE Mounted-T1() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Mounted-T2() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [1, 5]
		MEETS {
			Mounted-T1();
			Mounted-T2();
		}
	}
	
	COMPONENT HRC {FLEXIBLE process(trex_internal_dispatch_asap)} : HRCType;	
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanType;
	COMPONENT RobotController {FLEXIBLE controller(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT HRInteraction {FLEXIBLE monitoring(trex_internal_dispatch_asap)} : HRInteractionType;
	COMPONENT RobotToolHandler {FLEXIBLE status(trex_internal_dispatch_asap)} : RobotToolHandlerType;
	
	SYNCHRONIZE HRC.process
	{
		// process description
		VALUE HRC_ITIA()
		{
			h1 <!> Human.operator._H1();
			h3 <!> Human.operator._H3();
			h4 <!> Human.operator._H4();
			
			r3 <!> RobotController.controller.R3();
			r2 <!> RobotController.controller.R2();
			r1 <!> RobotController.controller.R1();
				
			CONTAINS [0, +INF] [0, +INF] r1;
			CONTAINS [0, +INF] [0, +INF] r2;
			CONTAINS [0, +INF] [0, +INF] r3;
			CONTAINS [0, +INF] [0, +INF] h1;
			CONTAINS [0, +INF] [0, +INF] h3;
			CONTAINS [0, +INF] [0, +INF] h4;
			
			h3 BEFORE [0, +INF] h4;
			h4 BEFORE [0, +INF] h1;
			
			r3 BEFORE [0, +INF] r1;
			r1 BEFORE [0, +INF] r2;
			
			// process' constraints inferred from collaboration types
			r3 BEFORE [0, +INF] h1;
		}
	}
	
	SYNCHRONIZE Human.operator
	{
		VALUE _H1()
		{
			cd0 HRInteraction.monitoring.Interaction-H1();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
		
		VALUE _H2()
		{
			cd0 HRInteraction.monitoring.Interaction-H2();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
		
		VALUE _H3()
		{
			cd0 HRInteraction.monitoring.Interaction-H3();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
		
		VALUE _H4()
		{
			cd0 HRInteraction.monitoring.Interaction-H4();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	
	SYNCHRONIZE RobotController.controller
	{
		VALUE R1()
		{
			cd0 HRInteraction.monitoring.Interaction-H1();
			cd1 <?> RobotToolHandler.status.Mounted-T1();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R1()
		{
			cd0 HRInteraction.monitoring.Interaction-H3();
			cd1 <?> RobotToolHandler.status.Mounted-T1();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R1()
		{
			cd0 HRInteraction.monitoring.Interaction-H4();
			cd1 <?> RobotToolHandler.status.Mounted-T1();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R2()
		{
			cd0 HRInteraction.monitoring.Interaction-H3();
			cd1 <?> RobotToolHandler.status.Mounted-T2();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R2()
		{
			cd0 HRInteraction.monitoring.Interaction-H3();
			cd1 <!> RobotToolHandler.status.Mounted-T2();
			cd2 <!> RobotController.controller.R4();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			AFTER [0, +INF] cd2;
		}
		
		VALUE R2()
		{
			cd0 HRInteraction.monitoring.Interaction-H4();
			cd1 <?> RobotToolHandler.status.Mounted-T2();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R2()
		{
			cd0 HRInteraction.monitoring.Interaction-H4();
			cd1 <!> RobotToolHandler.status.Mounted-T2();
			cd2 <!> RobotController.controller.R4();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			AFTER [0, +INF] cd2;
		}
		
		VALUE R3()
		{
			cd0 HRInteraction.monitoring.Interaction-H3();
			cd1 <?> RobotToolHandler.status.Mounted-T1();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		VALUE R3()
		{
			cd0 HRInteraction.monitoring.Interaction-H4();
			cd1 <?> RobotToolHandler.status.Mounted-T1();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		
		VALUE R4()
		{
			cd0 HRInteraction.monitoring.Interaction-H2();
			cd1 <!> Human.operator._H2();
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
		}
	}
}
