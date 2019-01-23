DOMAIN ITIA_v5
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
	
	COMP_TYPE SingletonStateVariable ProcessOperationType(
		Idle(), Task1(), Task2(), Task3(), Task4(), Task5(), Task6(), Task7(), Task8())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Task1();
			Task2(); 
			Task3();
			Task4();
			Task5(); 
			Task6(); 
			Task7();
			Task8();
		}
		
		VALUE Task1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task2() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task3() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task4() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task5() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task6() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task7() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task8() [1, +INF]
		MEETS {
			Idle();
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
			?tool != ?old;
		}
		
		VALUE Mounted(?tool) [1, +INF]
		MEETS {
			Mounting(?old, ?new);
			?old = ?tool;
			?new != ?tool;
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
			?to != ?position;
		}
		
		VALUE _MotionTask(?from, ?to, ?level) [1, 35]
		MEETS {
			At(?position, ?l);
			?from != ?position;
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
	COMPONENT Process {FLEXIBLE operations(trex_internal_dispatch_asap)} : ProcessOperationType;
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanType;
	COMPONENT RobotController {FLEXIBLE controller(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotArm {FLEXIBLE motion(trex_internal_dispatch_asap)} : ArmControllerType;
	COMPONENT ArmTool {FLEXIBLE status(trex_internal_dispatch_asap)} : ArmToolHandlerType;
	COMPONENT T1 {FLEXIBLE tool(trex_internal_dispatch_asap)} : ToolControllerType;
	COMPONENT T3 {FLEXIBLE tool3(trex_internal_dispatch_asap)} : PassiveToolType;
	
	SYNCHRONIZE HRC.process
	{
		VALUE HRC_ITIA()
		{
			cd0 Process.operations.Task1();
			cd1 Process.operations.Task2();
			cd2 Process.operations.Task3();
			cd3 Process.operations.Task4();
			cd4 Process.operations.Task5();
			cd5 Process.operations.Task6();
			cd6 Process.operations.Task7();
			cd7 Process.operations.Task8();
		}
	}
	
	SYNCHRONIZE Process.operations
	{
		VALUE Task1()
		{
			cd0 RobotController.controller.R1(?level);
		}
		
		VALUE Task2()
		{
			cd0 RobotController.controller.R7(?level);
		}
		
		VALUE Task3()
		{
			cd0 Human.operator._H2();
		}
		
		VALUE Task4()
		{
			cd0 Human.operator._H4();
			cd1 Human.operator._H3();
			
			cd0 BEFORE [0, +INF] cd1;
		}
		
		VALUE Task4()
		{
			cd0 RobotController.controller.R2(?level);
			cd1 Human.operator._H3();
			
			cd0 BEFORE [0, +INF] cd1;
		}
		
		VALUE Task5()
		{
			cd0 Human.operator._H9();
			cd1 Human.operator._H8();
			
			cd0 BEFORE [0, +INF] cd1;
		}
		
		VALUE Task5()
		{
			cd0 RobotController.controller.R6(?level);
		}
		
		VALUE Task6()
		{
			cd0 Human.operator._H5();
		}
		
		VALUE Task6()
		{
			cd0 RobotController.controller.R3(?level);
		}
		
		VALUE Task7()
		{
			cd0 Human.operator._H6();
		}
		
		VALUE Task7()
		{
			cd0 RobotController.controller.R4(?level);
		}
		
		VALUE Task8()
		{
			cd0 Human.operator._H7();
		}
		
		VALUE Task8()
		{
			cd0 RobotController.controller.R5(?level);
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

