DOMAIN Dummy {

	TEMPORAL_MODULE temporal_module = [0, 20], 500;

	PAR_TYPE EnumerationParameterType location = {location1, location2, location3};

	COMP_TYPE SingletonStateVariable DummyControllerType (Go1(), Go2()) {
	
		VALUE Go1() [1, +INF]
		MEETS {
			Go2();
		}
		
		VALUE Go2() [1, +INF]
		MEETS {
			Go1();
		}
	}
	
	COMP_TYPE SingletonStateVariable DummyRobotType (At(location), _MovingTo(location)) {
	
		VALUE At(?location) [1, +INF]
		MEETS {
			_MovingTo(?destination);
			?location != ?destination;
		}
		
		VALUE _MovingTo(?destination) [3, 5]
		MEETS {
			At(?location);
			?location = ?destination;
		}
	}
	
	COMP_TYPE SingletonStateVariable ObjectPosition (On(location), OnBoard(), Undefined()) {
	
		VALUE Undefined() [1, +INF]
		MEETS {
			On(?location);
		}
		
		VALUE OnBoard() [1, +INF]
		MEETS {
			On(?location);
		}
		
		VALUE On(?location) [1, +INF]
		MEETS {
			Undefined();
			OnBoard();
		}
	}
	
	COMPONENT DummyController {FLEXIBLE controller(trex_internal_dispatch_asap)} : DummyControllerType;
	COMPONENT Dummy {FLEXIBLE robot(trex_internal_dispatch_asap)} : DummyRobotType;
	COMPONENT Mug1 {FLEXIBLE position(trex_internal_dispatch_asap)} : ObjectPosition;
	
	
	SYNCHRONIZE DummyController.controller {
	
		VALUE Go1() {
		
			cd0 Dummy.robot.At(?location);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?location = location3;
		}
		
		VALUE Go2() {
			
			cd0 Dummy.robot.At(?location);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?location = location2;
		}
	
	}
}