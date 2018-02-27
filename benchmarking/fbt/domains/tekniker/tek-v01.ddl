DOMAIN FourByThree_TeknikerDomain
{
	// one unit is one second
	TEMPORAL_MODULE temporal_module = [0, 100], 300;
	
	PAR_TYPE EnumerationParameterType position = {
		P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14
	};
	
	PAR_TYPE EnumerationParameterType tool = {
		none, T1, T2, T3 
	};
	
	PAR_TYPE EnumerationParameterType level = {
		green, yellow, red
	};
	
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
	
	COMP_TYPE SingletonStateVariable RobotControllerType (
		Idle(), 
		R0(level), R1(level), R2(level), R3(level), R4(level)) 
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			R0(?level0);
			R1(?level1);
			R2(?level2);
			R3(?level3);
			R4(?level4);
		}
		
		VALUE R0(?level) [3, 11]
		MEETS {
			Idle();
		}
		
		VALUE R1(?level) [1, 5]
		MEETS {
			Idle();
		}
		
		VALUE R2(?level) [1, 8]
		MEETS {
			Idle();
		}
		
		VALUE R3(?level) [3, 22]
		MEETS {
			Idle();
		}
		
		VALUE R4(?level) [3, 12]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ArmControllerType (
		At(position, level), _MotionTask(position, position, level))
	{
		VALUE At(?position, ?level) [1, +INF]
		MEETS {
			_MotionTask(?from, ?to, ?l);
			?from = ?position;
		}
		
		VALUE _MotionTask(?from, ?to, ?level) [1, 35]
		MEETS {
			At(?position, ?l);
			?to = ?position;
			?l = ?level;
		}
	}
	
	COMP_TYPE SingletonStateVariable HumanType(Idle(), H1())
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			H1();
		}
		
		VALUE H1() [1, +INF] 
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ArmToolHandlerType(Idle(), Mounted(tool))
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			Mounted(?tool);
		}
		
		VALUE Mounted(?tool) [1, +INF] 
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable ToolControllerType(Idle(), Operating())
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			Operating();
		}
		
		VALUE Operating() [1, +INF] 
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable PassiveToolType(Idle(), Operating())
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			Operating();
		}
		
		VALUE Operating() [1, +INF] 
		MEETS {
			Idle();
		}
	}

	COMPONENT HRC {FLEXIBLE process(trex_internal_dispatch_asap)} : HRCType;
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanType;
	COMPONENT RobotController {FLEXIBLE controller(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotArm {FLEXIBLE motion(trex_internal_dispatch_asap)} : ArmControllerType;
	COMPONENT ArmTool {FLEXIBLE status(trex_internal_dispatch_asap)} : ArmToolHandlerType;
	COMPONENT T1 {FLEXIBLE tool(trex_internal_dispatch_asap)} : ToolControllerType;
	COMPONENT T3 {FLEXIBLE tool3(trex_internal_dispatch_asap)} : PassiveToolType;
	
	SYNCHRONIZE HRC.process
	{
		// process description
		VALUE HRC_ITIA()
		{
			r0 RobotController.controller.R0(?level0);
			r1 RobotController.controller.R1(?level1);
			r2 RobotController.controller.R2(?level2);
			r3 RobotController.controller.R3(?level3);
			r4 RobotController.controller.R4(?level4);
			
			CONTAINS [0, +INF] [0, +INF] r0;
			CONTAINS [0, +INF] [0, +INF] r1;
			CONTAINS [0, +INF] [0, +INF] r2;
			CONTAINS [0, +INF] [0, +INF] r3;
			CONTAINS [0, +INF] [0, +INF] r4;
			
			r1 BEFORE [0, +INF] r2;
			r1 BEFORE [0, +INF] r3;
			r3 BEFORE [0, +INF] r4;
			
			r2 BEFORE [0, +INF] r0;
			r4 BEFORE [0, +INF] r0;
			
			?level0 = green;
			?level1 = green;
			?level2 = green;
			?level3 = green;
			?level4 = green;
		}
	}
	
	SYNCHRONIZE RobotController.controller
	{
		VALUE R0(?level)
		{
			cd0 <?> RobotArm.motion.At(?initialPosition, ?level0);
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			MET-BY cd0;
			MEETS cd1;
			
			?finalPosition = P0;
			?level1 = ?level;
		}
		
		VALUE R1(?level)
		{
			cd0 <?> RobotArm.motion.At(?initialPosition, ?level0);
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			MET-BY cd0;
			MEETS cd1;
			
			?finalPosition = P1;
			?level1 = green;
		}
		
		VALUE R2(?level)
		{
			cd0 <?> RobotArm.motion.At(?initialPosition, ?level0);
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			MET-BY cd0;
			MEETS cd1;
			
			?finalPosition = P2;
			?level1 = green;
		}
		
		VALUE R3(?level)
		{
			cd0 <?> RobotArm.motion.At(?initialPosition, ?level0);
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			MET-BY cd0;
			MEETS cd1;
			
			?finalPosition = P3;
			?level1 = green;
		}
		
		VALUE R4(?level)
		{
			cd0 <?> RobotArm.motion.At(?initialPosition, ?level0);
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			MET-BY cd0;
			MEETS cd1;
			
			?finalPosition = P4;
			?level1 = green;
		}
	}
}
