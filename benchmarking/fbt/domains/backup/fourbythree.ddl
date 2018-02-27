DOMAIN FourByThree_Domain
{
	// one time unit is half second
	TEMPORAL_MODULE temporal_module = [0, 20], 100;

	PAR_TYPE NumericParameterType workpiece_id = [0, 100];
	
	PAR_TYPE EnumerationParameterType location = {
		home, block1, block2, block3, block4, block5, block6, block7, block8, block9
	};
	
	PAR_TYPE EnumerationParameterType modality = {
		independent, simultaneous, supportive, synchronous		
	};
	
	COMP_TYPE SingletonStateVariable ALFAUseCaseType (Idle(), Assembly(workpiece_id))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Assembly(?piece);
		}
		
		VALUE Assembly(?piece) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable AssemblyProcessType (
		None(),
		SetTheWorkPiece(workpiece_id), 
		RemoveTopCover(workpiece_id),
		Finalize(workpiece_id))
	{
		VALUE None() [1, +INF]
		MEETS {
			SetTheWorkPiece(?p);
		}
	
		VALUE SetTheWorkPiece(?piece) [1, +INF]
		MEETS {
			RemoveTopCover(?p);
			?p = ?piece;
		}
		 
		VALUE RemoveTopCover(?piece) [1, +INF]
		MEETS {
			Finalize(?p);
			?p = ?piece;
		}
		
		VALUE Finalize(?piece) [1, +INF]
		MEETS {
			None();
		}
	}
	
	COMP_TYPE SingletonStateVariable HumanOperatorFunctionType(
		Idle(), 
		_Place(workpiece_id), 
		_Screw(workpiece_id), 
		_Unscrew(workpiece_id), 
		_Rotate(workpiece_id), 
		_RemovePart(workpiece_id), 
		_RebuildPart(workpiece_id)) 
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_Place(?piece0);
			_Screw(?piece1);
			_Unscrew(?piece2); 
			_Rotate(?piece3);
			_RemovePart(?piece5);
			_RebuildPart(?piece6);
		}
		
		VALUE _Place(?piece) [3, 5]
		MEETS {
			Idle();
		}
		
		VALUE _Screw(?piece) [8, 11]
		MEETS {
			Idle();
		}
		
		VALUE _Unscrew(?piece) [8, 11]
		MEETS {
			Idle();
		}
		
		VALUE _Rotate(?piece) [3, 7]
		MEETS {
			Idle();
		}
		
		VALUE _RemovePart(?piece) [11, 18]
		MEETS {
			Idle();
		}
		
		VALUE _RebuildPart(?piece) [15, 23]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotFunctionType(
		Idle(), 
		Screw(workpiece_id, location, modality), 
		Unscrew(workpiece_id, location, modality)) 
	{
	
		VALUE Idle() [1, +INF] 
		MEETS {
			Screw(?p1, ?l1, ?m1);
			Unscrew(?p2, ?l2, ?m2);
		}
		
		VALUE Screw(?piece, ?location, ?modality) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Unscrew(?piece, ?location, ?modality) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmMotionType (
		SetOn(location, modality), 
		_MovingTo(location, modality))
	{
		VALUE SetOn(?location, ?modality) [1, +INF]
		MEETS {
			_MovingTo(?location1, ?modality1);
			?location1 != ?location;
		}
		
		VALUE _MovingTo(?location, ?modality) [4, 6]
		MEETS {
			SetOn(?location1, ?modality1);
			?location1 = ?location;
			?modality1 = ?modality;
		}	
	}
	
	COMP_TYPE SingletonStateVariable RobotToolType (
		Idle(), 
		Activate(),
		Deactivate(), 
		Operating())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Activate();
		}
		
		VALUE Activate() [1, 3]
		MEETS {
			Operating();
		}				
		
		VALUE Operating() [3, 7]
		MEETS {
			Deactivate();
		}
		
		VALUE Deactivate() [1, 3]
		MEETS {
			Idle();
		}
	}
	
	COMPONENT ALFA {FLEXIBLE use_case(trex_internal_dispatch_asap) } : ALFAUseCaseType;
	COMPONENT AssemblyProcess {FLEXIBLE tasks(trex_internal_dispatch_asap)} : AssemblyProcessType;
	
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanOperatorFunctionType;
	
	COMPONENT RobotController {FLEXIBLE functions(trex_internal_dispatch_asap)} : RobotFunctionType;
	COMPONENT RobotArmController {FLEXIBLE motion(trex_internal_dispatch_asap)} : RobotArmMotionType;
	COMPONENT ScrewDriverController {FLEXIBLE driver(trex_internal_dispatch_asap)} : RobotToolType;
	
	// domain rules
	SYNCHRONIZE ALFA.use_case
	{
		VALUE Assembly(?piece) 
		{
			task0 <!> AssemblyProcess.tasks.SetTheWorkPiece(?piece0);
			task7 <!> AssemblyProcess.tasks.Finalize(?piece7);			
		
			task0 BEFORE [1, +INF] task7;
			CONTAINS [0, +INF] [0, +INF] task0;
			CONTAINS [0, +INF] [0, +INF] task7;
			
			?piece0 = ?piece;
			?piece7 = ?piece;
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
			cd0 <!> Human.operator._Unscrew(?piece0);
			cd1 <!> RobotController.functions.Unscrew(?piece1, ?location1, ?modality1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			?location1 = block3;
			?modality1 = simultaneous;
		}
	}
	
	SYNCHRONIZE RobotController.functions
	{
		VALUE Unscrew(?piece, ?location, ?modality) 
		{
			cd00 <?> RobotArmController.motion.SetOn(?home, ?homeModality);
			
			cd0 <!> RobotArmController.motion.SetOn(?location0, ?modality0);
			cd1 <!> ScrewDriverController.driver.Activate();
			cd2 <!> ScrewDriverController.driver.Deactivate();
			
			cd3 <!> RobotArmController.motion.SetOn(?final, ?homeModality);
			
			MET-BY cd00;
			MEETS cd3;
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			
			cd1 BEFORE [0, +INF] cd2;
			cd1 DURING [0, +INF] [0, +INF] cd0;
			cd2 DURING [0, +INF] [0, +INF] cd0;
			
			?home = home;
			?location0 = ?location;
			?modality0 = ?modality;
			?final = home;
			?homeModality = independent;
		}
	}
}
