DOMAIN FourByThree_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 1000], 1000;

	PAR_TYPE EnumerationParameterType location = {l_base_station, l1, l2, l3, l4, l5, l6, l7, l8, l9};
	PAR_TYPE NumericParameterType workpiece_id = [0, 100];
	
	COMP_TYPE SingletonStateVariable ALFAUseCaseType(
		Idle(), 
		Assembly(workpiece_id))
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
		Set(workpiece_id), 
		CoverRemoval(workpiece_id), 
		BaseRemoval(workpiece_id), 
		InternalCoverRemoval(workpiece_id),
		HeavyPartsRemoval(workpiece_id),
		InsertRemoval(workpiece_id),
		HalfMouldAssembly(workpiece_id),
		BaseCoverRemoval(workpiece_id),
		InsertMouldRemoval(workpiece_id),
		WaxPartRemoval(workpiece_id),
		MouldAssembly(workpiece_id),
		BaseTightening(workpiece_id),
		CoverPlacement(workpiece_id))
	{
		VALUE Set(?piece) [1, +INF]
		MEETS {
			CoverRemoval(?p);
			?p = ?piece;
		}
		
		VALUE CoverRemoval(?piece) [1, +INF]
		MEETS {
			BaseRemoval(?p);
			?p = ?piece;	
		}
		
		VALUE BaseRemoval(?piece) [1, +INF]
		MEETS {
			InternalCoverRemoval(?p);
			?p = ?piece;
		}
		
		VALUE InternalCoverRemoval(?piece) [1, +INF]
		MEETS {
			HeavyPartsRemoval(?p);
			?p = ?piece;
		}
		
		VALUE HeavyPartsRemoval(?piece) [1, +INF]
		MEETS {	
			InsertRemoval(?p);
			?p = ?piece;
		}
		
		VALUE InsertRemoval(?piece) [1, +INF]
		MEETS {
			HalfMouldAssembly(?p);
			?p = ?piece;
		}
		
		VALUE HalfMouldAssembly(?piece) [1, +INF]
		MEETS {
			BaseCoverRemoval(?p);
			?p = ?piece;
		}
		
		VALUE BaseCoverRemoval(?piece) [1, +INF]
		MEETS {
			InsertMouldRemoval(?p);
			?p = ?piece;
		}
		VALUE InsertMouldRemoval(?piece) [1, +INF]
		MEETS {
			WaxPartRemoval(?p);
			?p = ?piece;
		}
		
		VALUE WaxPartRemoval(?piece) [1, +INF]
		MEETS {
			MouldAssembly(?p);
			?p = ?piece;
		}
		
		VALUE MouldAssembly(?piece) [1, +INF]
		MEETS {
			BaseTightening(?p);
			?p = ?piece;
		}
		
		VALUE BaseTightening(?piece) [1, +INF]
		MEETS {
			CoverPlacement(?p);
			?p = ?piece;
		}
		
		VALUE CoverPlacement(?piece) [1, +INF]
		MEETS {
			Set(?p);
			?p != ?piece;
		}
	}
	
	COMP_TYPE SingletonStateVariable HumanOperatorFunctionType(
		Idle(), 
		Place(workpiece_id), 
		Screw(workpiece_id), 
		Unscrew(workpiece_id), 
		Rotate(workpiece_id), 
		RemovePart(workpiece_id)) 
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Place(?piece0);
			Screw(?piece1);
			Unscrew(?piece2); 
			Rotate(?piece3);
			RemovePart(?piece5);
		}
		
		VALUE Place(?piece) [3, 5]
		MEETS {
			Idle();
		}
		
		VALUE Screw(?piece) [8, 11]
		MEETS {
			Idle();
		}
		
		VALUE Unscrew(?piece) [8, 11]
		MEETS {
			Idle();
		}
		
		VALUE Rotate(?piece) [3, 7]
		MEETS {
			Idle();
		}
		
		VALUE RemovePart(?piece) [11, 18]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotFunctionType(
		Idle(), 
		Screw(workpiece_id, location), 
		Unscrew(workpiece_id, location)) 
	{
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
	
	COMP_TYPE SingletonStateVariable RobotArmMotionType (
		Idle(location), 
		Moving(location), 
		OnTarget(workpiece_id, location))
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
			?location1 != ?location;
		}
	
	}
	
	COMP_TYPE SingletonStateVariable RobotToolType (
		Unset(), 
		Setting(), 
		Unsetting(), 
		Ready(), 
		Operating(workpiece_id, location))
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
	
	// robot controller components
	COMPONENT RobotController {FLEXIBLE functions(trex_internal_dispatch_asap)} : RobotFunctionType;
	COMPONENT RobotArmController {FLEXIBLE motion(trex_internal_dispatch_asap)} : RobotArmMotionType;
	COMPONENT ScrewDriverController {FLEXIBLE driver(trex_internal_dispatch_asap)} : RobotToolType;
	
	// domain rules
	SYNCHRONIZE ALFA.use_case
	{
		VALUE Assembly(?piece) 
		{
			task0 <!> AssemblyProcess.tasks.Set(?piece0);
			task1 <!> AssemblyProcess.tasks.CoverPlacement(?piece1);
			
			CONTAINS [0, +INF] [0, +INF] task0;
			CONTAINS [0, +INF] [0, +INF] task1;
			
			task0 BEFORE [0, +INF] task1;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
		}
	}
	
	SYNCHRONIZE AssemblyProcess.tasks
	{
		VALUE Set(?piece) 
		{
			// load the piece to work on the work-cell
			cd0 <!> Human.operator.Place(?piece0);
			
			EQUALS cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE CoverRemoval(?piece) 
		{
			// un-screw the bolts in both covers
			cd0 <!> Human.operator.Unscrew(?piece0);
			// remove covers
			cd1 <!> Human.operator.RemovePart(?piece1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			cd0 BEFORE [0, +INF] cd1;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
		}
		
		VALUE BaseRemoval(?piece) 
		{
			// the human un-screw the lower two bolts in the first base
			cd0 <!> Human.operator.Unscrew(?piece0);
			
			// the human un-screw the lower two bolts in the second base
			cd2 <!> Human.operator.Unscrew(?piece2);
			
			// the human rotates the mould
			cd22 <!> Human.operator.Rotate(?piece22);
			
			// the robot un-screw the upper two bolts in the second base
			cd1 <!> RobotController.functions.Unscrew(?piece1, ?location1);
			
			// the robot un-screw the upper two bolts in the first base
			cd3 <!> RobotController.functions.Unscrew(?piece3, ?location3);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd22;
			CONTAINS [0, +INF] [0, +INF] cd3;
			
			
			cd0 BEFORE [0, +INF] cd22;
			cd22 BEFORE [0, +INF] cd2;
			cd22 BEFORE [0, +INF] cd3;
			cd1 BEFORE [0, +INF] cd22;

			?piece0 = ?piece;
			?piece1 = ?piece;
			?piece2 = ?piece;
			?piece22 = ?piece;
			?piece3 = ?piece;
			
			?location1 = l1;
			?location3 = l2;
		}
		
		VALUE InternalCoverRemoval(?piece)
		{
			// the human rotates the mould
			cd0 <!> Human.operator.Rotate(?piece0);
			// the human unscrews the bolts 
			cd1 <!> Human.operator.Unscrew(?piece1);
			// the human unscrews the lateral pats
			cd2 <!> Human.operator.RemovePart(?piece2);
			
			// the robot un-screw the six bolts on the top part
			cd31 <!> RobotController.functions.Unscrew(?piece31, ?location31);
			cd32 <!> RobotController.functions.Unscrew(?piece32, ?location32);
			cd33 <!> RobotController.functions.Unscrew(?piece33, ?location33);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			
			cd31 BEFORE [0, +INF] cd32;
			cd32 BEFORE [0, +INF] cd33;
			cd33 BEFORE [0, +INF] cd2;
			
			cd0 BEFORE [0, +INF] cd1;
			cd1 BEFORE [0, +INF] cd2;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			?piece2 = ?piece;
			?piece31 = ?piece;
			?piece32 = ?piece;
			?piece33 = ?piece;
			
			?location31 = l1;
			?location32 = l2;
			?location33 = l3;
		}
		
		VALUE HeavyPartsRemoval(?piece) 
		{
			cd0 <!> Human.operator.RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE InsertRemoval(?piece) 
		{
			cd0 <!> Human.operator.RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE HalfMouldAssembly(?piece) 
		{
			cd0 <!> Human.operator.RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE BaseCoverRemoval(?piece) 
		{
			// the human rotates the mould
			cd0 <!> Human.operator.Rotate(?piece0);
			// the human unscrews the bolts 
			cd1 <!> Human.operator.Unscrew(?piece1);
			// the human unscrews the lateral pats
			cd2 <!> Human.operator.RemovePart(?piece2);
			
			// the robot un-screw the six bolts on the top part
			cd31 <!> RobotController.functions.Unscrew(?piece31, ?location31);
			cd32 <!> RobotController.functions.Unscrew(?piece32, ?location32);
			cd33 <!> RobotController.functions.Unscrew(?piece33, ?location33);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			
			cd31 BEFORE [0, +INF] cd32;
			cd32 BEFORE [0, +INF] cd33;
			cd33 BEFORE [0, +INF] cd2;
			
			cd0 BEFORE [0, +INF] cd1;
			cd1 BEFORE [0, +INF] cd2;
			
			?piece0 = ?piece;
			?piece1 = ?piece;
			?piece2 = ?piece;
			?piece31 = ?piece;
			?piece32 = ?piece;
			?piece33 = ?piece;
			
			?location31 = l1;
			?location32 = l2;
			?location33 = l3;
		}
		
		VALUE InsertMouldRemoval(?piece) {
			cd0 <!> Human.operator.RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE WaxPartRemoval(?piece) 
		{
			cd0 <!> Human.operator.RemovePart(?piece0);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			?piece0 = ?piece;
		}
		
		VALUE BaseTightening(?piece)
		{
			// the human un-screw the lower two bolts in the first base
			cd0 <!> Human.operator.Screw(?piece0);
			
			// the human un-screw the lower two bolts in the second base
			cd2 <!> Human.operator.Screw(?piece2);
			
			// the human rotates the mould
			cd22 <!> Human.operator.Rotate(?piece22);
			
			// the robot un-screw the upper two bolts in the second base
			cd1 <!> RobotController.functions.Screw(?piece1, ?location1);
			
			// the robot un-screw the upper two bolts in the first base
			cd3 <!> RobotController.functions.Screw(?piece3, ?location3);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd22;
			CONTAINS [0, +INF] [0, +INF] cd3;
			
			
			cd0 BEFORE [0, +INF] cd22;
			cd22 BEFORE [0, +INF] cd2;
			cd22 BEFORE [0, +INF] cd3;
			cd1 BEFORE [0, +INF] cd22;

			?piece0 = ?piece;
			?piece1 = ?piece;
			?piece2 = ?piece;
			?piece22 = ?piece;
			?piece3 = ?piece;
			
			?location1 = l1;
			?location3 = l2;
		}
	}

	SYNCHRONIZE RobotController.functions {
		
		VALUE Unscrew(?piece, ?location) {
			
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
