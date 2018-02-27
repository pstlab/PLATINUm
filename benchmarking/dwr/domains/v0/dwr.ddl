// Dock Worker Robot domain from Automated Planning book
DOMAIN DWR {
	
	TEMPORAL_MODULE temporal_module = [0, 10], 300;

	PAR_TYPE EnumerationParameterType object = { 
		container1, container2, crane1, pile1, pile2, robot, nothing 
	};
	
	COMP_TYPE SingletonStateVariable CraneControllerType (Idle(), UnStack(object)) {
		
		VALUE Idle() [1, +INF]
		MEETS {
			UnStack(?obj);
		}
		
		VALUE UnStack(?obj) [2, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable CraneType (Free(), Holding(object)) {
	
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?container);
		}
		
		VALUE Holding(?container) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	
	COMP_TYPE SingletonStateVariable PileType (Unset(), Top(object)) {
	
		VALUE Unset() [1, +INF]
		MEETS {
			Top(?obj);
		}
	
		VALUE Top(?object) [1, +INF] 
		MEETS {
			Unset();
		}
	}
	
	COMP_TYPE SingletonStateVariable ContainerType (Unset(), On(object)) {
	
		VALUE Unset() [1, +INF]
		MEETS {
			On(?obj);
		}
	
		VALUE On(?object) [1, +INF]
		MEETS {
			Unset();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotControllerType (Idle(), Load(object)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			Load(?obj);
		}
		
		VALUE Load(?object) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotStatusType (Free(), Holding(object)) {
	
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?obj);
		}
		
		VALUE Holding(?obj) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	COMPONENT Container1 {FLEXIBLE c1_status(trex_internal_dispatch_asap)} : ContainerType;
	COMPONENT Container2 {FLEXIBLE c2_status(trex_internal_dispatch_asap)} : ContainerType;
	
	COMPONENT Pile1 {FLEXIBLE p1_status(trex_internal_dispatch_asap)} : PileType;
	COMPONENT Pile2 {FLEXIBLE p2_status(trex_internal_dispatch_asap)} : PileType;
	
	COMPONENT Crane1 {FLEXIBLE crane1_status(trex_internal_dispatch_asap)} : CraneType;
	
	COMPONENT CraneController {FLEXIBLE crane(trex_internal_dispatch_asap)} : CraneControllerType;
	COMPONENT RobotController {FLEXIBLE robot(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotStatus {FLEXIBLE status(trex_internal_dispatch_asap)} : RobotStatusType;
	
	SYNCHRONIZE RobotController.robot {
	
		VALUE Load(?object) {
		
			// precondition
			cd1 RobotStatus.status.Free();
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			
			// subtask(s)
			cd0 CraneController.crane.UnStack(?object);
			CONTAINS [0, +INF] [0, +INF] cd0;

			// effects
			cd2 RobotStatus.status.Holding(?object);
			cd4 Crane1.crane1_status.Free();
			ENDS-DURING [0, +INF] [0, +INF] cd2;
			ENDS-DURING [0, +INF] [0, +INF] cd4;
			cd2 STARTS cd4;
			
			?object = container1;
		}
	}

	SYNCHRONIZE CraneController.crane {
	
		// caso base dove il container da prendere è al top della pila
		VALUE UnStack(?object) 
		{
			// preconditions
			cd0 <?> Crane1.crane1_status.Free();
			cd1 <?> Container1.c1_status.On(?container1on);
			cd2 <?> Pile1.p1_status.Top(?p1current);

			STARTS-DURING [0, +INF] [0, +INF] cd0; 
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			STARTS-DURING [0, +INF] [0, +INF] cd2;
			
			// effects
			cd3 <!> Crane1.crane1_status.Holding(?holding);
			cd4 <!> Container1.c1_status.Unset();
			cd5 <!> Pile1.p1_status.Top(?p1next);
			
			ENDS-DURING [0, +INF] [0, +INF] cd3;
			ENDS-DURING [0, +INF] [0, +INF] cd4;
			ENDS-DURING [0, +INF] [0, +INF] cd5;
			
			?object = container1;
			?p1current = container1;
			?holding = container1;
			?p1next = ?container1on;
		}
		
		// chiamata ricorsiva - svuota la pila fino a far "emergere" il container desiderato
		VALUE UnStack(?object) 
		{
			// preconditions
			cd0 <?> Crane1.crane1_status.Free();
			cd1 <?> Container2.c2_status.On(?container2on);
			cd2 <?> Pile1.p1_status.Top(?p1current);
			cd3 <?> Pile2.p2_status.Top(?p2current);
			
			STARTS-DURING [0, +INF] [0, +INF] cd0; 
			STARTS-DURING [0, +INF] [0, +INF] cd1;
			STARTS-DURING [0, +INF] [0, +INF] cd2;
			STARTS-DURING [0, +INF] [0, +INF] cd3;
			
			// effect
			cd4 <!> Crane1.crane1_status.Holding(?holding);
			cd5 <!> Pile1.p1_status.Top(?p1next);
			cd6 <!> Pile2.p2_status.Top(?p2next);
			cd7 <!> Container2.c2_status.On(?container2onnext);
			cd8 <!> Crane1.crane1_status.Free();
			
			cd0 MEETS cd4;
			cd4 MEETS cd8;
			ENDS-DURING [0, +INF] [0, +INF] cd5;
			ENDS-DURING [0, +INF] [0, +INF] cd6;
			ENDS-DURING [0, +INF] [0, +INF] cd7;
			ENDS-DURING [0, +INF] [0, +INF] cd8;
			
			// recursive call
			cd9 <!> CraneController.crane.UnStack(?object);
			BEFORE [1, +INF] cd9;
			
			?object = container1;
			?p1current = container2;
			
			?holding = container2;
			?p2next = container2;
			?p1next = ?container2on;
			?container2onnext = ?p2current;
		}
	}
}