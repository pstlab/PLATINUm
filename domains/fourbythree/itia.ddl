DOMAIN FourByThree_Domain
{
	// one unit is half-second
	TEMPORAL_MODULE temporal_module = [0, 50], 300;

	PAR_TYPE EnumerationParameterType modality = {
		independent, simultaneous, supportive, synchronous		
	};

	PAR_TYPE EnumerationParameterType device = {
		T1, T2
	};

	PAR_TYPE EnumerationParameterType hov = {
		H1, H2, H3, H4
	};

	PAR_TYPE EnumerationParameterType robotTask = {
		R1, R2, R3
	};
	
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
		R1(hov), R2(hov), R3(hov)) 
	{
	
		VALUE Idle() [1, +INF] 
		MEETS {
			R1(?h1);
			R2(?h2);
			R3(?h3);
		}
		
		VALUE R1(?h) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R2(?h) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R3(?h) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmMotionType (
		Home(),
		_MovingTo(robotTask, hov),
		Set(),
		_MovingHome(robotTask, hov))
	{
		VALUE Home() [1, +INF]
		MEETS {
			_MovingTo(?task1, ?modality1);
		}
		
		VALUE _MovingTo(?task, ?hov) [3, 23]
		MEETS {
			Set();
		}
		
		VALUE Set() [1, +INF]
		MEETS {
			_MovingHome(?task1, ?hov1);
		}
		
		VALUE _MovingHome(?task, ?hov) [3, 23]
		MEETS {
			Home();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotHandType (
		Free(),
		Holding(device))
	{
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?device);
		}
		
		VALUE Holding(?device) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotToolType (
		Unset(), 
		Activate(), 
		Deactivate(), 
		Ready())
	{
		VALUE Unset() [1, +INF]
		MEETS {
			Activate();
		}
		
		VALUE Activate() [1, 3]
		MEETS {
			Ready();
		}
		
		VALUE Ready() [1, +INF]
		MEETS {
			Deactivate();
		}	
		
		VALUE Deactivate() [1, 3]
		MEETS {
			Unset();
		}
	}
	
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

	COMPONENT HRC {FLEXIBLE interaction(trex_internal_dispatch_asap)} : HRCType;	
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanType;
	COMPONENT RobotController {FLEXIBLE controller(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotArmController {FLEXIBLE motion(trex_internal_dispatch_asap)} : RobotArmMotionType;
	COMPONENT RobotHandController {FLEXIBLE hand(trex_internal_dispatch_asap)} : RobotHandType;
	COMPONENT Tool1 {FLEXIBLE t1(trex_internal_dispatch_asap)} : RobotToolType;
	COMPONENT Tool2 {FLEXIBLE t2(trex_internal_dispatch_asap)} : RobotToolType;
	
	SYNCHRONIZE HRC.interaction
	{
		VALUE HRC_ITIA()
		{
			r3 <!> RobotController.controller.R3(?r3h4);
			r1 <!> RobotController.controller.R1(?r1h3);
			r2 <!> RobotController.controller.R2(?r2h1);
			
			h3 <!> Human.operator._H3();
			h4 <!> Human.operator._H4();
			h2 <!> Human.operator._H2();
			h1 <!> Human.operator._H1();
			
			CONTAINS [0, +INF] [0, +INF] r1;
			CONTAINS [0, +INF] [0, +INF] r2;
			CONTAINS [0, +INF] [0, +INF] r3;
			CONTAINS [0, +INF] [0, +INF] h1;
			CONTAINS [0, +INF] [0, +INF] h2;
			CONTAINS [0, +INF] [0, +INF] h3;
			CONTAINS [0, +INF] [0, +INF] h4;
			
			// robot sequence
			r3 BEFORE [0, +INF] r1;
			r1 BEFORE [0, +INF] r2;
			
			//r3 BEFORE [0, +INF] h4;
			//r1 BEFORE [0, +INF] h2;

			// human sequence
			h3 BEFORE [0, +INF] h4;
			h4 BEFORE [0, +INF] h2;
			h2 BEFORE [0, +INF] h1;
			
			// human to robot coordination
			h2 MEETS r2;
			//r3 BEFORE [0, +INF] h4;

			?r3h4 = H4;
			?r1h3 = H3;			
			?r2h1 = H1;
		}
	}
	
	SYNCHRONIZE Human.operator
	{
		VALUE _H2()
		{
			cd0 RobotArmController.motion.Home();
			cd1 <?> RobotHandController.hand.Holding(?device1);
			cd2 <!> RobotHandController.hand.Holding(?device2);
			
			MET-BY cd1;
			MEETS cd2;
			DURING [0, +INF] [0, +INF] cd0;
			
			?device1 = T1;
			?device2 = T2;
		}
	}
	
	SYNCHRONIZE RobotController.controller
	{
		VALUE R1(?hov)
		{
			cd1 <?> RobotHandController.hand.Holding(?device1);
			
			cd2 <!> Tool1.t1.Activate();
			// move to target
			cd3 <!> RobotArmController.motion._MovingTo(?task3, ?hov3);
			cd4 <!> Tool1.t1.Deactivate();
			// move to home
			cd5 <!> RobotArmController.motion._MovingHome(?task5, ?hov5);
			
			DURING [0, +INF] [0, +INF] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			
			cd2 BEFORE [0, +INF] cd4;
			cd3 BEFORE [0, +INF] cd5;
			cd2 BEFORE [0, +INF] cd3;
			cd4 BEFORE [0, +INF] cd5;
			cd4 AFTER [0, +INF] cd3;
			
			?device1 = T1; 
			?task3 = R1;
			?hov3 = ?hov;
			?task5 = R1;
			?hov5 = ?hov;
		}
		
		VALUE R2(?hov)
		{
			cd1 <?> RobotHandController.hand.Holding(?device1);
			
			cd2 <!> Tool2.t2.Activate();
			// move to target
			cd3 <!> RobotArmController.motion._MovingTo(?task3, ?hov3);
			cd4 <!> Tool2.t2.Deactivate();
			// move to home
			cd5 <!> RobotArmController.motion._MovingHome(?task5, ?hov5);
			
			DURING [0, +INF] [0, +INF] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			
			cd2 BEFORE [0, +INF] cd4;
			cd3 BEFORE [0, +INF] cd5;
			cd2 BEFORE [0, +INF] cd3;
			cd4 BEFORE [0, +INF] cd5;
			cd4 AFTER [0, +INF] cd3;
			
			?device1 = T2; 
			?task3 = R2;
			?hov3 = ?hov;
			?task5 = R2;
			?hov5 = ?hov;
		}
		
		VALUE R3(?hov)
		{
			cd1 <?> RobotHandController.hand.Holding(?device1);
			
			cd2 <!> Tool1.t1.Activate();
			cd3 <!> RobotArmController.motion._MovingTo(?task3, ?hov3);
			cd4 <!> Tool1.t1.Deactivate();
			cd5 <!> RobotArmController.motion._MovingHome(?task5, ?hov5);
			
			DURING [0, +INF] [0, +INF] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			
			cd2 BEFORE [0, +INF] cd4;
			cd3 BEFORE [0, +INF] cd5;
			cd2 BEFORE [0, +INF] cd3;
			cd4 BEFORE [0, +INF] cd5;
			cd4 AFTER [0, +INF] cd3;
			
			?device1 = T1; 
			?task3 = R3;
			?hov3 = ?hov;
			?task5 = R3;
			?hov5 = ?hov;
		}
	}
}
