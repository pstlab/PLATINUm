DOMAIN FourByThree_TeknikerDomain
{
	// one unit is one second
	TEMPORAL_MODULE temporal_module = [0, 300], 300;
	
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
		R0(level)) 
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			R0(?level);
		}
		
		VALUE R0(?level) [1, +INF]
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
			r0 RobotController.controller.R0(?lev0);
			
			CONTAINS [0, +INF] [0, +INF] r0;
			
			?lev0 = green;
		}
	}
	
	SYNCHRONIZE RobotController.controller
	{
		VALUE R0(?level)
		{
			cd1 <!> RobotArm.motion.At(?finalPosition, ?level1);
			
			ENDS-DURING [0, +INF] [0, +INF] cd1;
			
			?finalPosition = P3;
			?level1 = ?level;
		}
	}
}
