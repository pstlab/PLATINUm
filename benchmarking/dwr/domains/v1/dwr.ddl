// Dock Worker Robot domain from Automated Planning book
DOMAIN DWR {
	
	TEMPORAL_MODULE temporal_module = [0, 50], 300;
	
	PAR_TYPE EnumerationParameterType location = {
		dock1, 
		dock2
	};
	
	PAR_TYPE EnumerationParameterType container = {
		container1, 
		container2, 
		nothing
	};
	
	PAR_TYPE EnumerationParameterType pile = {
		pile11, pile21, 
		pile12, pile22, 
		nothing
	};

	COMP_TYPE SingletonStateVariable CraneControllerType (Idle(), UnStack(container)) {
		
		VALUE Idle() [1, +INF]
		MEETS {
			UnStack(?container);
		}
		
		VALUE UnStack(?container) [2, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable CraneStatusType (Free(), Holding(container)) {
	
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?container);
		}
		
		VALUE Holding(?container) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	// The Unset() value is needed for variable transition, otherwise the planner is not able to build the timeline
	COMP_TYPE SingletonStateVariable PileType (Unset(), Top(container)) {
	
		VALUE Unset() [1, +INF]
		MEETS {
			Top(?container);
		}
	
		VALUE Top(?container) [1, +INF] 
		MEETS {
			Unset();
		}
	}
	
	// The Unset() value is needed for variable transition, otherwise the planner is not able to build the timeline
	COMP_TYPE SingletonStateVariable ContainerType (Unset(), On(container, pile)) {
	
		VALUE Unset() [1, +INF]
		MEETS {
			On(?container, ?pile);
		}
	
		VALUE On(?container, ?pile) [1, +INF]
		MEETS {
			Unset();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotStatusType (Free(), Holding(container)) {
	
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?container);
		}
		
		VALUE Holding(?container) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotDrivingType (At(location), DrivingTo(location)) {
	
		VALUE At(?location) [1, +INF]
		MEETS {
			DrivingTo(?destination);
			?destination != ?location;
		}
		
		VALUE DrivingTo(?destination) [3, 5]
		MEETS {
			At(?location);
			?location = ?destination;
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotControllerType (Idle(), Load(container)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			Load(?container);
		}
		
		VALUE Load(?container) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	
	// domain's containers
	COMPONENT Container1 {FLEXIBLE c1_status(trex_internal_dispatch_asap)} : ContainerType;
	COMPONENT Container2 {FLEXIBLE c2_status(trex_internal_dispatch_asap)} : ContainerType;

	// piles of dock1
	COMPONENT Pile11 {FLEXIBLE p11_status(trex_internal_dispatch_asap)} : PileType;
	COMPONENT Pile21 {FLEXIBLE p21_status(trex_internal_dispatch_asap)} : PileType;
	
	// crane of dock1
	COMPONENT Crane1Controller {FLEXIBLE crane1(trex_internal_dispatch_asap)} : CraneControllerType;
	COMPONENT Crane1 {FLEXIBLE crane1_status(trex_internal_dispatch_asap)} : CraneStatusType;
	
	// piles of dock2
	//COMPONENT Pile12 {FLEXIBLE p12_status(trex_internal_dispatch_asap)} : PileType;
	//COMPONENT Pile22 {FLEXIBLE p22_status(trex_internal_dispatch_asap)} : PileType;

	// crane of dock2
	//COMPONENT Crane2Controller {FLEXIBLE crane2(trex_internal_dispatch_asap)} : CraneControllerType;
	//COMPONENT Crane2 {FLEXIBLE crane2_status(trex_internal_dispatch_asap)} : CraneStatusType;
	
	// robot 
	COMPONENT RobotController {FLEXIBLE robot(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotStatus {FLEXIBLE status(trex_internal_dispatch_asap)} : RobotStatusType;

	SYNCHRONIZE RobotController.robot {
	
		VALUE Load(?object) {
		
			// precondition
			cd1 RobotStatus.status.Free();
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			
			// subtask(s)
			cd0 Crane1Controller.crane1.UnStack(?object);
			CONTAINS [0, +INF] [0, +INF] cd0;
			
			// check container's position
			cd3 Container1.c1_status.On(?c_position, ?c_dock);
			STARTS-DURING [0, +INF] [0, +INF] cd3;
			
			// effects
			cd2 RobotStatus.status.Holding(?object);
			cd4 Crane1.crane1_status.Free();
			ENDS-DURING [0, +INF] [0, +INF] cd2;
			ENDS-DURING [0, +INF] [0, +INF] cd4;
			cd2 STARTS cd4;

			// hypotheses
			?object = container1;
			?c_dock = pile11;
		}
	}

	SYNCHRONIZE Crane1Controller.crane1 {
	
		// the container to unstack is the top of the pile
		VALUE UnStack(?object) 
		{
			// preconditions
			cd0 <?> Crane1.crane1_status.Free();
			cd1 <?> Container1.c1_status.On(?container1on, ?container1pile);
			cd2 <?> Pile11.p11_status.Top(?p11current);

			STARTS-DURING [0, +INF] [0, +INF] cd0; 
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			STARTS-DURING [0, +INF] [0, +INF] cd2;
			
			// effects
			cd3 <!> Crane1.crane1_status.Holding(?holding);
			cd4 <!> Container1.c1_status.Unset();
			cd5 <!> Pile11.p11_status.Top(?p11next);
			
			ENDS-DURING [0, +INF] [0, +INF] cd3;
			ENDS-DURING [0, +INF] [0, +INF] cd4;
			ENDS-DURING [0, +INF] [0, +INF] cd5;
			
			?object = container1;
			?p11current = container1;
			?holding = container1;
			?p11next = ?container1on;
			?container1pile = pile11;
		}
		
		// recursive call to let the container to unstack emerging to the top of the pile
		VALUE UnStack(?object) 
		{
			// preconditions
			cd0 <?> Crane1.crane1_status.Free();
			cd1 <?> Container2.c2_status.On(?container2on, ?container2pile);
			cd2 <?> Pile11.p11_status.Top(?p11current);
			//cd3 <?> Pile21.p21_status.Top(?p21current);
			
			STARTS-DURING [0, +INF] [0, +INF] cd0; 
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			STARTS-DURING [0, +INF] [0, +INF] cd2;
			//STARTS-DURING [0, +INF] [0, +INF] cd3;
			
			// effect
			cd4 <!> Crane1.crane1_status.Holding(?holding);
			cd5 <!> Pile11.p11_status.Top(?p11next);
			//cd6 <!> Pile21.p21_status.Top(?p21next);
			cd7 <!> Container2.c2_status.On(?container2onnext, ?container2pilenext);
			cd8 <!> Crane1.crane1_status.Free();
			
			cd0 MEETS cd4;
			cd4 MEETS cd8;
			ENDS-DURING [0, +INF] [0, +INF] cd5;
			//ENDS-DURING [0, +INF] [0, +INF] cd6;
			ENDS-DURING [0, +INF] [0, +INF] cd7;
			ENDS-DURING [0, +INF] [0, +INF] cd8;
			
			// recursive call
			cd9 <!> CraneController.crane.UnStack(?object);
			BEFORE [1, +INF] cd9;
			
			?object = container1;
			?p11current = container2;
			
			?holding = container2;
			//?p21next = container2;
			?p11next = ?container2on;
			
			//?container2onnext = ?p21current;
			?container2pile = pile11;
			?container2pilenext = pile21;
		}
	}	
	
}