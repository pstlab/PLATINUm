DOMAIN CIRP17_a3
{
	// one unit is one second
	TEMPORAL_MODULE temporal_module = [0, 300], 300;
	
	PAR_TYPE EnumerationParameterType position = {
		P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14
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
	
	COMP_TYPE SingletonStateVariable HumanType (
		Idle(),
		_H1(tool), _H2(), _H3(), _H4(), _H5(), _H6(), _H7(), _H8(), _H9())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_H1(?tool);
			_H2();
			_H3();
			_H4();
			_H5();
			_H6();
			_H7();
			_H8();
			_H9();
		}
		
		// 64
		VALUE _H1(?tool) [5, 70]
		MEETS {
			Idle();
		}
		
		// 22.2
		VALUE _H2() [6, 28]
		MEETS {
			Idle();			
		}

		// 29.7
		VALUE _H3() [8, 35]
		MEETS {
			Idle();
		}
		
		// 6.5
		VALUE _H4() [2, 10]
		MEETS {
			Idle();
		}
		
		// 13.8
		VALUE _H5() [4, 22]
		MEETS {
			Idle();
		}
		
		// 13.5
		VALUE _H6() [5, 18]
		MEETS {
			Idle();
		}
		
		// 17.5
		VALUE _H7() [4, 23]
		MEETS {
			Idle();
		}
		
		// 5.5
		VALUE _H8() [1, 8]
		MEETS {
			Idle();
		}
		
		// 18.5
		VALUE _H9() [3, 25]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotControllerType (
		Idle(), 
		R0(level), R1(level), R2(level), R3(level), R4(level), R5(level), R6(level), R7(level)) 
	{
		VALUE Idle() [1, +INF] 
		MEETS {
			R0(?level);
			R1(?level);
			R2(?level);
			R3(?level);
			R4(?level);
			R5(?level);
			R6(?level);
			R7(?level);
		}
		
		VALUE R0(?level) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R1(?level) [2, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R2(?level) [6, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R3(?level) [2, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R4(?level) [5, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R5(?level) [3, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R6(?level) [6, +INF]
		MEETS {
			Idle();
		}
		
		VALUE R7(?level) [6, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ArmToolHandlerType (
		Mounted(tool), Mounting(tool, tool))
	{
		VALUE Mounting(?old, ?new) [1, 70]
		MEETS {
			Mounted(?tool);
			?tool = ?new;
		}
		
		VALUE Mounted(?tool) [1, +INF]
		MEETS {
			Mounting(?old, ?new);
			?old = ?tool;
		}
	}
	
	COMP_TYPE SingletonStateVariable ToolControllerType (
		Idle(), _Activate(), Operating(), _Deactivate())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_Activate();
		}
	
		VALUE _Activate() [1, 5]
		MEETS {
			Operating();
		}
		
		VALUE Operating() [1, +INF]
		MEETS {
			_Deactivate();
		}
		
		VALUE _Deactivate() [1, 5]
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
	
	COMP_TYPE SingletonStateVariable PassiveToolType (
		Idle(), Analyze())
	{
		VALUE  Idle() [1, +INF]
		MEETS {
			Analyze();
		}
		
		VALUE Analyze() [3, 3]
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
			r7 RobotController.controller.R7(?levelR7);
			r3 RobotController.controller.R3(?levelR3);
			r5 RobotController.controller.R5(?levelR5);
			r4 RobotController.controller.R4(?levelR4);
			r1 RobotController.controller.R1(?levelR1);
			r0 RobotController.controller.R0(?levelR0);
			
			r7 BEFORE [0, +INF] r3;
			r3 BEFORE [0, +INF] r5;
			r5 BEFORE [0, +INF] r4;
			r4 BEFORE [0, +INF] r1;
			r1 BEFORE [0, +INF] r0;
			
			h2 Human.operator._H2();
			h4 Human.operator._H4();
			h3 Human.operator._H3();
			h9 Human.operator._H9();
			h8 Human.operator._H8();
			
			h2 BEFORE [0, +INF] h4;
			h4 BEFORE [0, +INF] h3;
			h3 BEFORE [0, +INF] h9;
			h9 BEFORE [0, +INF] h8;
			
			CONTAINS [0, +INF] [0, +INF] r7;
			CONTAINS [0, +INF] [0, +INF] r3;
			CONTAINS [0, +INF] [0, +INF] r5;
			CONTAINS [0, +INF] [0, +INF] r4;
			CONTAINS [0, +INF] [0, +INF] r1;
			CONTAINS [0, +INF] [0, +INF] r0;
			
			CONTAINS [0, +INF] [0, +INF] h2;
			CONTAINS [0, +INF] [0, +INF] h4;
			CONTAINS [0, +INF] [0, +INF] h3;
			CONTAINS [0, +INF] [0, +INF] h9;
			CONTAINS [0, +INF] [0, +INF] h8;
		}
	}
	
	SYNCHRONIZE RobotController.controller
	{
		VALUE R0(?level)
		{
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			
			ENDS-DURING [0, +INF] [0, +INF] cd1;
			
			?finalPosition = P4;
			?level = green;
			?level1 = ?level;
		}
	
		VALUE R1(?level)
		{
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			cd2 ArmTool.status.Mounted(?tool);
			cd3 T3.tool3.Analyze();
			
			DURING [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd3 DURING [0, +INF] [0, +INF] cd1;
			
			?finalPosition = P3;
			?level = yellow;
			?tool = T3;
			?level1 = ?level;
		}
		
		VALUE R2(?level)
		{
			cd0 ArmTool.status.Mounted(?tool);
			 
			cd1 RobotArm.motion.At(?initialPosition, ?level1);
			cd2 T1.tool._Activate();
			cd3 RobotArm.motion.At(?finalPosition, ?level3);
			cd4 T1.tool._Deactivate();
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd4;
			
			cd2 DURING [0, +INF] [0, +INF] cd1;
			cd4 DURING [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd4;
			
			?initialPosition = P1;
			?finalPosition = P2;
			?tool = T1;
			?level = yellow;
			?level1 = ?level;
			?level3 = ?level;
		}
		
		VALUE R3(?level)
		{
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			cd2 ArmTool.status.Mounted(?tool);
			cd3 T3.tool3.Analyze();
			
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd3 DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			?finalPosition = P6;
			?level = green;
			?tool = T3;
			?level1 = ?level;
		}
		
		VALUE R4(?level)
		{
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			cd2 ArmTool.status.Mounted(?tool);
			cd3 T3.tool3.Analyze();
			
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd3 DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			?finalPosition = P7;
			?level = green;
			?tool = T3;
			?level1 = ?level;
		}
		
		VALUE R5(?level)
		{
			cd1 RobotArm.motion.At(?finalPosition, ?level1);
			cd2 ArmTool.status.Mounted(?tool);
			cd3 T3.tool3.Analyze();
			
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd3 DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			?finalPosition = P8;
			?level = yellow;
			?tool = T3;
			?level1 = ?level;
		}
		
		VALUE R6(?level)
		{
			cd0 ArmTool.status.Mounted(?tool);
			
			cd1 RobotArm.motion.At(?initialPosition, ?level1);
			cd2 T1.tool._Activate();
			cd3 RobotArm.motion.At(?finalPosition, ?level3);
			cd4 T1.tool._Deactivate();
			
			cd5 Human.operator._H9();
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			
			cd5 DURING [0, +INF] [0, +INF] cd1;
			cd2 DURING [0, +INF] [0, +INF] cd1;
			cd4 DURING [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd5;
			cd5 BEFORE [0, +INF] cd4;
			
			?initialPosition = P10;
			?finalPosition = P9;
			?tool = T1;
			?level = red;
			?level1 = ?level;
			?level3 = ?level;
		}
		
		VALUE R7(?level)
		{
			cd0 ArmTool.status.Mounted(?tool);
			 
			cd1 RobotArm.motion.At(?initialPosition, ?level1);
			cd2 T1.tool._Activate();
			cd3 RobotArm.motion.At(?finalPosition, ?level3);
			cd4 T1.tool._Deactivate();
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd4;
			
			cd2 DURING [0, +INF] [0, +INF] cd1;
			cd4 DURING [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd4;
			
			?initialPosition = P13;
			?finalPosition = P14;
			?tool = T1;
			?level = yellow;
			?level1 = ?level;
			?level3 = ?level;
		}
	}
	
	SYNCHRONIZE Human.operator
	{
		VALUE _H1(?tool)
		{
			cd2 RobotArm.motion.At(?position, ?level);
			
			DURING [0, +INF] [0, +INF] cd2;
			
			?position = P4;
			?level = red;
		}		
	}
	
	SYNCHRONIZE ArmTool.status
	{
		VALUE Mounting(?oldTool, ?newTool)
		{
			cd0 <!> Human.operator._H1(?tool);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?tool = ?newTool;
		}
	}
}
