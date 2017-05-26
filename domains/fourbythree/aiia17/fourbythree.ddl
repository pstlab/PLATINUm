DOMAIN FourByThree_AIIA17
{
	// one unit is half-second
	TEMPORAL_MODULE temporal_module = [0, 200], 300;

	PAR_TYPE NumericParameterType workpiece_id = [0, 100];
	PAR_TYPE EnumerationParameterType location = {
		home, block1, block2, block3, block4, block5, 
		block6, block7, block8, block9, block10, block11, block12
	};
	PAR_TYPE EnumerationParameterType modality = {
		independent, simultaneous, supportive, synchronous		
	};
	
	COMP_TYPE SingletonStateVariable ALFAUseCaseType (
		Idle(), 
		Assembly(workpiece_id), 
		RemoveTopCover(workpiece_id))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Assembly(?piece0);
			RemoveTopCover(?piece1);
		}
		
		VALUE Assembly(?piece) [2, +INF]
		MEETS {
			Idle();
		}
		
		VALUE RemoveTopCover(?piece) [2, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable AssemblyDiscretizedProcessType (
		None(),
		SetTheWorkPiece(workpiece_id), 
		RemoveTopCover(workpiece_id), 
		TurnPiece(workpiece_id), 
		RemoveBottomCover(workpiece_id),
		RemoveTheInsert(workpiece_id),
		RebuildThePiece(workpiece_id), 
		CoverPlacement(workpiece_id))
	{
		VALUE None() [2, +INF]
		MEETS {
			SetTheWorkPiece(?p0); 
			RemoveTopCover(?p1); 
			TurnPiece(?p2); 
			RemoveBottomCover(?p3);
			RemoveTheInsert(?p4);
			RebuildThePiece(?p5); 
			CoverPlacement(?p6);
		}
	
		VALUE SetTheWorkPiece(?piece) [2, +INF]
		MEETS {
			None();
		}
		 
		VALUE RemoveTopCover(?piece) [2, +INF]
		MEETS {
			None();
		}
		
		VALUE TurnPiece(?piece) [2, +INF]
		MEETS {
			None();
		} 
		
		VALUE RemoveBottomCover(?piece) [2, +INF]
		MEETS {
			None();
		}
		
		VALUE RemoveTheInsert(?piece) [2, +INF]
		MEETS {
			None();
		} 
		
		VALUE RebuildThePiece(?piece) [2, +INF]
		MEETS {
			None();
		} 
		
		VALUE CoverPlacement(?piece) [2, +INF]
		MEETS {
			None();
		}
	}
	
	
	COMP_TYPE SingletonStateVariable AssemblyProcessType (
		None(),
		SetTheWorkPiece(workpiece_id), 
		RemoveTopCover(workpiece_id), 
		TurnPiece(workpiece_id), 
		RemoveBottomCover(workpiece_id),
		RemoveTheInsert(workpiece_id),
		RebuildThePiece(workpiece_id), 
		CoverPlacement(workpiece_id))
	{
		VALUE None() [2, +INF]
		MEETS {
			SetTheWorkPiece(?p);
		}
	
		VALUE SetTheWorkPiece(?piece) [2, +INF]
		MEETS {
			RemoveTopCover(?p);
			?p = ?piece;
		}
		 
		VALUE RemoveTopCover(?piece) [2, +INF]
		MEETS {
			TurnPiece(?p);
			?p = ?piece;
		}
		
		VALUE TurnPiece(?piece) [2, +INF]
		MEETS {
			RemoveBottomCover(?p);
			?p = ?piece;
		} 
		
		VALUE RemoveBottomCover(?piece) [2, +INF]
		MEETS {
			RemoveTheInsert(?p);
			?p = ?piece;
		}
		
		VALUE RemoveTheInsert(?piece) [2, +INF]
		MEETS {
			RebuildThePiece(?p);
			?p = ?piece;
		} 
		
		VALUE RebuildThePiece(?piece) [2, +INF]
		MEETS {
			CoverPlacement(?p);
			?p = ?piece;
		} 
		
		VALUE CoverPlacement(?piece) [2, +INF]
		MEETS {
			None();
		}
	}
	
	COMP_TYPE SingletonStateVariable HumanOperatorFunctionType(
		Idle(), 
		_Place(workpiece_id), 
		_Screw(workpiece_id, location), 
		_Unscrew(workpiece_id, location), 
		_Rotate(workpiece_id), 
		_RemovePart(workpiece_id), 
		_RebuildPart(workpiece_id)) 
	{
		VALUE Idle() [2, +INF]
		MEETS {
			_Place(?piece0);
			_Screw(?piece1, ?location1);
			_Unscrew(?piece2, ?location2); 
			_Rotate(?piece3);
			_RemovePart(?piece5);
			_RebuildPart(?piece6);
		}
		
		VALUE _Place(?piece) [6, 10]
		MEETS {
			Idle();
		}
		
		VALUE _Screw(?piece, ?location) [16, 22]
		MEETS {
			Idle();
		}
		
		VALUE _Unscrew(?piece, ?location) [16, 22]
		MEETS {
			Idle();
		}
		
		VALUE _Rotate(?piece) [6, 14]
		MEETS {
			Idle();
		}
		
		VALUE _RemovePart(?piece) [22, 36]
		MEETS {
			Idle();
		}
		
		VALUE _RebuildPart(?piece) [30, 46]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotFunctionType(
		Idle(), 
		Screw(workpiece_id, location, modality), 
		Unscrew(workpiece_id, location, modality)) 
	{
	
		VALUE Idle() [2, +INF] 
		MEETS {
			Screw(?p1, ?l1, ?m1);
			Unscrew(?p2, ?l2, ?m2);
		}
		
		VALUE Screw(?piece, ?location, ?modality) [2, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Unscrew(?piece, ?location, ?modality) [2, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmMotionType (
		SetOn(location, modality),
		_MovingTo(location, modality))
	{
		VALUE _MovingTo(?location, ?modality) [4, 8]
		MEETS {
			SetOn(?location1, ?modality1);
			?location1 = ?location;
			?modality1 = ?modality;
		}	
		
		VALUE SetOn(?location, ?modality) [2, +INF]
		MEETS {
			_MovingTo(?location1, ?modality1);
			?location1 != ?location;
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotToolType (
		Unset(), 
		Activate(), 
		Deactivate(), 
		Operating())
	{
		VALUE Unset() [2, +INF]
		MEETS {
			Activate();
		}
		
		VALUE Activate() [1, 3]
		MEETS {
			Operating();
		}				
		
		VALUE Deactivate() [1, 3]
		MEETS {
			Unset();
		}
		
		VALUE Operating() [10, 10]
		MEETS {
			Deactivate();
		}
				
	}
	
	// factory work-flow components
	COMPONENT ALFA {FLEXIBLE use_case(trex_internal_dispatch_asap) } : ALFAUseCaseType;
	COMPONENT AssemblyProcess {FLEXIBLE tasks(trex_internal_dispatch_asap)} : AssemblyProcessType;
	COMPONENT AssemblyDiscreteProcess {FLEXIBLE discrete_tasks(trex_internal_dispatch_asap)} : AssemblyDiscretizedProcessType;
	
	// human operator component
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanOperatorFunctionType;

	// robot controller components
	COMPONENT RobotController {FLEXIBLE functions(trex_internal_dispatch_asap)} : RobotFunctionType;
	COMPONENT RobotArmController {FLEXIBLE motion(trex_internal_dispatch_asap)} : RobotArmMotionType;
	COMPONENT ScrewDriverController {FLEXIBLE driver(trex_internal_dispatch_asap)} : RobotToolType;
	
	// domain rules
	SYNCHRONIZE ALFA.use_case
	{
		VALUE Assembly(?piece) 
		{
			task0 <!> AssemblyProcess.tasks.SetTheWorkPiece(?piece0);
			task7 <!> AssemblyProcess.tasks.CoverPlacement(?piece7);	
		
			STARTS-DURING [0, +INF] [0, +INF] task0;
			ENDS-DURING [0, +INF] [0, +INF] task7;
			
			?piece0 = ?piece;
			?piece7 = ?piece;
		}
		
		VALUE RemoveTopCover(?piece)
		{
			task0 <!> AssemblyDiscreteProcess.discrete_tasks.RemoveTopCover(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] task0;
			
			?piece0 = ?piece;
		}
	}
	
	SYNCHRONIZE AssemblyDiscreteProcess.discrete_tasks 
	{
		VALUE RemoveTopCover(?piece)
		{
			cd0 <!> Human.operator._Unscrew(?piece0, ?location0);
			cd1 <!> Human.operator._Unscrew(?piece1, ?location1);
			cd2 <!> Human.operator._Unscrew(?piece2, ?location2);
			cd3 <!> Human.operator._Unscrew(?piece3, ?location3);
			
			cd0 BEFORE [0, +INF] cd1;
			cd1 BEFORE [0, +INF] cd2;
			cd2 BEFORE [0, +INF] cd3;
			
			cd4 <!> RobotController.functions.Unscrew(?piece4, ?location4, ?modality4);
			cd5 <!> RobotController.functions.Unscrew(?piece5, ?location5, ?modality5);
			cd6 <!> RobotController.functions.Unscrew(?piece6, ?location6, ?modality6);
			cd7 <!> RobotController.functions.Unscrew(?piece7, ?location7, ?modality7);
			
			cd4 BEFORE [0, +INF] cd5;
			cd5 BEFORE [0, +INF] cd6;
			cd6 BEFORE [0, +INF] cd7;
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			CONTAINS [0, +INF] [0, +INF] cd6;
			CONTAINS [0, +INF] [0, +INF] cd7;
			
			?piece0 = ?piece;
			?location0 = block7;
			
			?piece1 = ?piece;
			?location1 = block8;
			
			?piece2 = ?piece;
			?location2 = block11;
			
			?piece3 = ?piece;
			?location3 = block12;

			?piece4 = ?piece;			
			?location4 = block1;
			?modality4 = simultaneous;
			
			?piece5 = ?piece;			
			?location5 = block2;
			?modality5 = simultaneous;
			
			?piece6 = ?piece;			
			?location6 = block3;
			?modality6 = simultaneous;
			
			?piece7 = ?piece;			
			?location7 = block4;
			?modality7 = simultaneous;
		} 
	}
	
	SYNCHRONIZE AssemblyProcess.tasks
	{
		VALUE SetTheWorkPiece(?piece) 
		{
			cd0 <!> Human.operator._Place(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE RemoveTopCover(?piece)
		{
			cd0 <!> Human.operator._Unscrew(?piece0, ?location0);
			cd1 <!> RobotController.functions.Unscrew(?piece1, ?location1, ?modality1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?location0 = block12;
			
			?piece1 = ?piece;
			?location1 = block1;
			?modality1 = simultaneous;
		}
		
		VALUE TurnPiece(?piece)
		{
			cd0 <!> Human.operator._Rotate(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE RemoveBottomCover(?piece)
		{
			cd0 <!> Human.operator._Unscrew(?piece0, ?location0);
			cd1 <!> RobotController.functions.Unscrew(?piece1, ?location1, ?modality1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?location0 = block8;
			
			?piece1 = ?piece;
			?location1 = block5;
			?modality1 = simultaneous;
		}
		
		VALUE RemoveTheInsert(?piece)
		{	
			cd0 <!> Human.operator._RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE RebuildThePiece(?piece)
		{
			cd0 <!> Human.operator._RebuildPart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE CoverPlacement(?piece) 
		{
			cd0 <!> Human.operator._Screw(?piece0, ?location0);
			cd1 <!> RobotController.functions.Screw(?piece1, ?location1, ?modality1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?location0 = block4;

			?piece1 = ?piece;
			?location1 = block9;
			?modality1 = simultaneous;
		}
	}
	
	SYNCHRONIZE RobotController.functions
	{
		VALUE Unscrew(?piece, ?location, ?modality) 
		{
			cd0 <!> RobotArmController.motion.SetOn(?location0, ?modality0);
			cd1 <!> RobotArmController.motion.SetOn(?location1, ?modality1);
			cd2 <!> ScrewDriverController.driver.Activate();
			cd3 <!> ScrewDriverController.driver.Deactivate();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			ENDS-DURING [0, +INF] [0, +INF] cd1;
			cd0 CONTAINS [0, +INF] [0, +INF] cd2;
			cd0 CONTAINS [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd3;
			
			?location0 = ?location;
			?modality0 = ?modality;
			
			?location1 = home;
			?modality1 = ?modality;
		}
		
		VALUE Screw(?piece, ?location, ?modality) 
		{
			cd0 <!> RobotArmController.motion.SetOn(?location0, ?modality0);
			cd1 <!> RobotArmController.motion.SetOn(?location1, ?modality1);
			cd2 <!> ScrewDriverController.driver.Activate();
			cd3 <!> ScrewDriverController.driver.Deactivate();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			ENDS-DURING [0, +INF] [0, +INF] cd1;
			cd0 CONTAINS [0, +INF] [0, +INF] cd2;
			cd0 CONTAINS [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd3;
			
			?location0 = ?location;
			?modality0 = ?modality;
			
			?location1 = home;
			?modality1 = ?modality;
		}
	}
}
