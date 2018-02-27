DOMAIN Rover {
	
	TEMPORAL_MODULE tm = [0, 100], 500;
	PAR_TYPE EnumerationParameterType location = {lander, rock1, rock2, rock3};
	PAR_TYPE NumericParameterType file = [0,100];
	
	COMP_TYPE SingletonStateVariable NavigationControllerType (At(location), GoingTo(location)) {
		
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
	
	COMP_TYPE SingletonStateVariable InstrumentControllerType (Unstowed(), Stowed(), Unstowing(), Stowing(), Placing(location), Sampling(location)) {
		
		VALUE Unstowed() [1, +INF]
		MEETS {
			Stowing();
			Placing(?l);
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
		
		VALUE Placing(?l) [3, 7]
		MEETS {
			Sampling(?l1);
			?l1 = ?l;
		}
		
		VALUE Sampling(?l) [5, 10]
		MEETS {
			Unstowed();
		}
	}
	
	COMP_TYPE SingletonStateVariable RoverControllerType(Idle(), GoTo(location), TakeSample(location, file)) {
		
		VALUE Idle() [1, +INF]
		MEETS {
			GoTo(?l1);
			TakeSample(?l2, ?f2);
		}
		
		VALUE TakeSample(?l, ?f) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable CommunicationControllerType(Idle(), Communicating(file)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			Communicating(?f);
		}
		
		VALUE Communicating(?f) [7, 23]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable VisibilityWindowType(Visible(), NotVisible()) {
		
		VALUE Visible() [1, +INF]
		MEETS {
			NotVisible();
		}
	
		VALUE NotVisible() [1, +INF]
		MEETS {
			Visible();
		}
	}
	
	
	COMPONENT NavigationController {FLEXIBLE nav(trex_internal)} : NavigationControllerType;
	COMPONENT InstrumentController {FLEXIBLE inst(trex_internal)} : InstrumentControllerType;
	COMPONENT CommunicationController {FLEXIBLE comm(trex_internal)} : CommunicationControllerType;
	COMPONENT RoverController {FLEXIBLE rover(trex_internal)} : RoverControllerType;
	COMPONENT VisibilityWindow {FLEXIBLE window(trex_external)} : VisibilityWindowType;
	
	SYNCHRONIZE NavigationController.nav {
	
		VALUE GoingTo(?location) {
			
			// check the instrument position while moving
			cd0 InstrumentController.inst.Stowed();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE CommunicationController.comm {
		
		VALUE Communicating(?file) {
			
			cd0 NavigationController.nav.At(?l);
			cd1 <?> VisibilityWindow.window.Visible();
		
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
	}
	
	SYNCHRONIZE RoverController.rover {
	
		VALUE TakeSample(?location, ?file) {
		
			cd0 NavigationController.nav.At(?l0);
			cd1 <!> InstrumentController.inst.Sampling(?l1);
			cd2 <!> CommunicationController.comm.Communicating(?f);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			BEFORE [0, +INF] cd2;
			
			?l0 = ?location;
			?l1 = ?location;
			?f = ?file;
		}
	}
}