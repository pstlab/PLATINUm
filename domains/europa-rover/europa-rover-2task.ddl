DOMAIN Rover_EUROPA_2task {
	
	TEMPORAL_MODULE tm = [0, 50], 100;
	PAR_TYPE NumericParameterType location = [0, 100];	// location 0 is the lander
	PAR_TYPE NumericParameterType file = [0,100];
	
	COMP_TYPE SingletonStateVariable NavigationControllerType (
		At(location), 
		GoingTo(location)) {
		
			VALUE At(?l) [1, +INF]
			MEETS {
				GoingTo(?l1);
				?l1 != ?l;
			}
	
			VALUE GoingTo(?l) [5, 11]
			MEETS {
				At(?l1);
				?l1 = ?l;
			}
	}
	
	COMP_TYPE SingletonStateVariable InstrumentPositionType (
		Unstowed(), 
		Stowed(), 
		Unstowing(), 
		Stowing()) {
		
		VALUE Unstowed() [1, +INF]
		MEETS {
			Stowing();
		}
		
		VALUE Stowed() [1, +INF]
		MEETS {
			Unstowing();
		}
		
		VALUE Unstowing() [3, 3]
		MEETS {
			Unstowed();
		}
		
		VALUE Stowing() [3, 3]
		MEETS {
			Stowed();
		}
	}
	
	COMP_TYPE SingletonStateVariable InstrumentOperationType (
		Idle(),
		Placing(location),
		Placed(location),
		Sampling(location)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			Placing(?location);
		}
		
		VALUE Placing(?location) [3, 5]
		MEETS {
			Placed(?location1);
			?location1 = ?location;
		}
		
		VALUE Placed(?location) [1, +INF]
		MEETS {
			Sampling(?location1);
			Placing(?location2);
			Idle();
			?location1 = ?location;
			?location2 != ?location;
		}
		
		VALUE Sampling(?location) [5, 7]
		MEETS {
			Placed(?location0);
			?location0 = ?location;
		}
	}
	
	COMP_TYPE SingletonStateVariable RoverControllerType(
		Idle(), 
		TakeSample(location, file)) {
		
		VALUE Idle() [1, +INF]
		MEETS {
			TakeSample(?location, ?file);
		}
		
		VALUE TakeSample(?location, ?file) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMPONENT NavigationController {FLEXIBLE nav(trex_internal)} : NavigationControllerType;
	COMPONENT InstrumentPosition {FLEXIBLE inst_position(trex_internal)} : InstrumentPositionType;
	COMPONENT InstrumentOperation {FLEXIBLE inst_operation(trex_internal)} : InstrumentOperationType;
	COMPONENT RoverController {FLEXIBLE rover(trex_internal)} : RoverControllerType;
	
	SYNCHRONIZE NavigationController.nav {
	
		VALUE GoingTo(?location) {
			
			// check the instrument position while moving
			cd0 InstrumentPosition.inst_position.Stowed();
			cd1 InstrumentOperation.inst_operation.Idle();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
	}
	
	SYNCHRONIZE RoverController.rover {
	
		VALUE TakeSample(?location, ?file) {
		
			cd0 NavigationController.nav.At(?location0);
			cd1 InstrumentPosition.inst_position.Unstowed();
			cd2 InstrumentOperation.inst_operation.Sampling(?location2);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			CONTAINS [0, +INF] [0, +INF] cd2;
			
			cd2 DURING [0, +INF] [0, +INF] cd1;
			
			?location0 = ?location;
			?location2 = ?location;
		}
	}
}