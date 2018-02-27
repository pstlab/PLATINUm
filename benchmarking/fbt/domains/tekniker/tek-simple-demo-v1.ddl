DOMAIN FourByThree_TeknikerDomain
{
	// one unit is one second
	TEMPORAL_MODULE temporal_module = [0, 300], 300;
	
	PAR_TYPE EnumerationParameterType position = {
		P0, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14
	};
	
	PAR_TYPE EnumerationParameterType area = {
		top, lateral
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
		Unscrew(area, position)) 
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			Unscrew(?area, ?level);
		}
		
		VALUE Unscrew(?area, ?level) [1, +INF]
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
	
	COMP_TYPE SingletonStateVariable HumanType(
		Idle(), Unscrew(area, position)
	)
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			Unscrew(?area, ?position);
		}
		
		VALUE Unscrew(?area, ?position) [1, +INF]
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
			h1 Human.operator.Unscrew(?areaH1, ?positionH1);
			h2 Human.operator.Unscrew(?areaH2, ?positionH2);
			h3 Human.operator.Unscrew(?areaH3, ?positionH3);
			h4 Human.operator.Unscrew(?areaH4, ?positionH4);
			h5 Human.operator.Unscrew(?areaH5, ?positionH5);
			
			r1 RobotController.controller.Unscrew(?areaR1, ?positionR1);
			r2 RobotController.controller.Unscrew(?areaR2, ?positionR2);
			r3 RobotController.controller.Unscrew(?areaR3, ?positionR3);
			r4 RobotController.controller.Unscrew(?areaR4, ?positionR4);
			r5 RobotController.controller.Unscrew(?areaR5, ?positionR5);
			r6 RobotController.controller.Unscrew(?areaR6, ?positionR6);
			r7 RobotController.controller.Unscrew(?areaR7, ?positionR7);
			r8 RobotController.controller.Unscrew(?areaR8, ?positionR8);
			
			CONTAINS [0, +INF] [0, +INF] h1;
			CONTAINS [0, +INF] [0, +INF] h2;
			CONTAINS [0, +INF] [0, +INF] h3;
			CONTAINS [0, +INF] [0, +INF] h4;
			CONTAINS [0, +INF] [0, +INF] h5;

			CONTAINS [0, +INF] [0, +INF] r1;
			CONTAINS [0, +INF] [0, +INF] r2;
			CONTAINS [0, +INF] [0, +INF] r3;
			CONTAINS [0, +INF] [0, +INF] r4;
			CONTAINS [0, +INF] [0, +INF] r5;
			CONTAINS [0, +INF] [0, +INF] r6;
			CONTAINS [0, +INF] [0, +INF] r7;
			CONTAINS [0, +INF] [0, +INF] r8;
			
			// human (lateral) bolts to unscrew
			?positionH1 = P11;
			?positionH2 = P13;
			?positionH3 = P12;
			?positionH4 = P10;
			?positionH5 = P14;
			
			?areaH1 = lateral;
			?areaH2 = lateral;
			?areaH3 = lateral;
			?areaH4 = lateral;
			?areaH5 = lateral;
			
			// robot (top) bolts to unscrew
			?positionR1 = P3;
			?positionR2 = P4;
			?positionR3 = P5;
			?positionR4 = P6;
			?positionR5 = P7;
			?positionR6 = P8;
			?positionR7 = P1;
			?positionR8 = P2;
			
			?areaR1 = top;
			?areaR2 = top;
			?areaR3 = top;
			?areaR4 = top;
			?areaR5 = top;
			?areaR6 = top;
			?areaR7 = top;
			?areaR8 = top;
		}
	}
}
