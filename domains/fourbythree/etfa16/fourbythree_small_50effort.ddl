DOMAIN FourByThree_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 40], 200;

	PAR_TYPE EnumerationParameterType location = {l_base_station, l1, l2, l3, l4, l5, l6, l7, l8, l9};
	PAR_TYPE NumericParameterType workpiece_id = [0, 100];
	
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
	
	COMP_TYPE SingletonStateVariable AssemblyProcessType (None(),
		SetTheWorkPiece(workpiece_id), 
		RemoveCover(workpiece_id), 
		RemoveInsert(workpiece_id), 
		CoverPlacement(workpiece_id))
	{
		VALUE None() [1, +INF]
		MEETS {
			SetTheWorkPiece(?p);
		}
	
		VALUE SetTheWorkPiece(?piece) [1, +INF]
		MEETS {
			RemoveCover(?p);
			?p = ?piece;
		}
		 
		VALUE RemoveCover(?piece) [1, +INF]
		MEETS {
			RemoveInsert(?p);
			?p = ?piece;
		}
		
		VALUE RemoveInsert(?piece) [1, +INF]
		MEETS {
			CoverPlacement(?p);
			?p = ?piece;
		}
		
		VALUE CoverPlacement(?piece) [1, +INF]
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
	
	COMP_TYPE SingletonStateVariable CollaborationModalityType (
		None(),
		Independent(), Simultaneous(), Supportive(), Synchronous()) 
	{
		VALUE None() [1, +INF]
		MEETS {
			Independent();
			Simultaneous(); 
			Supportive();
			Synchronous();
		}
	
		VALUE Independent() [1, +INF]
		MEETS {
			Simultaneous(); 
			Supportive();
			Synchronous();
			None();
		}
		
		VALUE Simultaneous() [1, +INF]
		MEETS {
			Independent();
			Supportive();
			Synchronous();
			None();
		} 
		
		VALUE Supportive() [1, +INF]
		MEETS {
			Independent();
			Simultaneous();
			Synchronous();
			None();
		} 
		
		VALUE Synchronous() [1, +INF]
		MEETS {
			Independent();
			Simultaneous();
			Supportive();
			None();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotFunctionType(Idle(), Screw(workpiece_id, location), Unscrew(workpiece_id, location)) {
	
		VALUE Idle() [1, +INF] 
		MEETS {
			Screw(?p1, ?l1);
			Unscrew(?p2, ?l2);
		}
		
		VALUE Screw(?piece, ?location) [5, 10]
		MEETS {
			Idle();
		}
		
		VALUE Unscrew(?piece, ?location) [5, 10]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmMotionType (Idle(location), Moving(location), OnTarget(workpiece_id, location))
	{
		VALUE Idle(?location) [1, +INF]
		MEETS {
			Moving(?location1);
			?location1 != ?location;
		}
		
		VALUE Moving(?location) [3, 11]
		MEETS {
			OnTarget(?workpiece_id, ?location1);
			?location1 = ?location;
		}	
		
		VALUE OnTarget(?workpiece_id, ?location) [1, +INF]
		MEETS {
			Moving(?location1);
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotToolType (Unset(), Setting(), Unsetting(), Ready(), Operating(workpiece_id, location))
	{
		VALUE Unset() [1, +INF]
		MEETS {
			Setting();
		}
		
		VALUE Setting() [3, 3]
		MEETS {
			Ready();
		}				
		
		VALUE Ready() [1, +INF]
		MEETS {
			Unsetting();
			Operating(?piece, ?location);
		}
		
		VALUE Unsetting() [3, 3]
		MEETS {
			Unset();
		}
		
		VALUE Operating(?piece, ?location) [3, 7]
		MEETS {
			Ready();
		}
				
	}
	
	// factory work-flow components
	COMPONENT ALFA {FLEXIBLE use_case(trex_internal_dispatch_asap) } : ALFAUseCaseType;
	COMPONENT AssemblyProcess {FLEXIBLE tasks(trex_internal_dispatch_asap)} : AssemblyProcessType;
	
	// human operator component
	COMPONENT Human {FLEXIBLE operator(trex_internal_dispatch_asap)} : HumanOperatorFunctionType;
	
	// collaboration modality
	COMPONENT CollaborationType {FLEXIBLE modality(trex_internal_dispatch_asap)} : CollaborationModalityType;
	
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
		
		VALUE RemoveCover(?piece)
		{
			cd0 <!> Human.operator._Unscrew(?piece0);
			cd1 <!> RobotController.functions.Unscrew(?piece1, ?location1);
			cd2 CollaborationType.modality.Simultaneous();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			cd1 DURING [0, +INF] [0, +INF] cd2;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			
			?location1 = l1;
		}
		
		VALUE RemoveInsert(?piece)
		{	
			cd0 <!> Human.operator._RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE CoverPlacement(?piece) 
		{
			cd0 <!> Human.operator._Screw(?piece0);
			cd1 <!> RobotController.functions.Screw(?piece1, ?location1);
			cd2 CollaborationType.modality.Simultaneous();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			cd1 DURING [0, +INF] [0, +INF] cd2;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			
			?location1 = l3;
		}
	}
	
	SYNCHRONIZE RobotController.functions
	{
		VALUE Unscrew(?piece, ?location) 
		{
			cd0 RobotArmController.motion.OnTarget(?piece0, ?location0);
			cd1 ScrewDriverController.driver.Operating(?piece1, ?location1);
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			
			?location0 = ?location;
			?location1 = ?location;
		}
		
		VALUE Screw(?piece, ?location) 
		{
			cd0 RobotArmController.motion.OnTarget(?piece0, ?location0);
			cd1 ScrewDriverController.driver.Operating(?piece1, ?location1);
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			
			?location0 = ?location;
			?location1 = ?location;
		}
	}
}
