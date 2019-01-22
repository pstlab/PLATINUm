DOMAIN ICO_v2 {

	TEMPORAL_MODULE temporal_module = [0, 100], 300;

	PAR_TYPE EnumerationParameterType location = {
		floor, 
		manipulationAreaWestTable1, manipulationAreaEastTable1, manipulationAreaNorthTable1, manipulationAreaSouthTable1,
		manipulationAreaWstTable2, manipulationAreaEastTable2, manipulationAreaNorthTable2, manipulationAreaSouthTable2,
		manipulationAreaEastCounter1
	};
	
	PAR_TYPE EnumerationParameterType area = {
		placingAreaLeft, placingAreaRight
	};

	PAR_TYPE EnumerationParameterType guest = {
		guest1
	};
	
	PAR_TYPE EnumerationParameterType object = {
		mug1, mug2
	};

	PAR_TYPE EnumerationParameterType arm_posture = {
		tucked, unTucked, carry, side, unNamed
	};

	PAR_TYPE EnumerationParameterType torso_posture = {
		up, down, middle
	};


	COMP_TYPE SingletonStateVariable GuestPosition (Undefined(), At(location)) {
	
		VALUE Undefined() [1, +INF]
		MEETS {
			At(?location);
		}
		
		VALUE At(?location) [1, +INF]
		MEETS {
			Undefined();
		}
	}
	
	COMP_TYPE SingletonStateVariable EnvironmentObjectPosition (Undefined(), On(location, area)) {
	
		VALUE Undefined() [1, +INF]
		MEETS {
			On(?location, ?area);
		}
		
		VALUE On(?location, ?area) [1, +INF]
		MEETS {
			Undefined();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotBaseType (At(location), _MovingTo(location)) {
	
		VALUE At(?location) [1, +INF]
		MEETS {
			_MovingTo(?destination);
			?location != ?destination;
		}
		
		VALUE _MovingTo(?destination) [11, 23]
		MEETS {
			At(?location);
			?location = ?destination;
		}
	} 
	
	COMP_TYPE SingletonStateVariable RobotTorsoType (Posture(torso_posture), Assuming(torso_posture)) {
	
		VALUE Posture(?posture) [1, +INF]
		MEETS {
			Assuming(?newPosture);
			?newPosture != ?posture;
		}
	
		VALUE Assuming(?newPosture) [2, 5]
		MEETS {
			Posture(?posture);
			?posture = ?newPosture;
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmType (Posture(arm_posture), Assuming(arm_posture)) {
	
		VALUE Posture(?posture) [1, +INF]
		MEETS {
			Assuming(?newPosture);
			?newPosture != ?posture;
		}
		
		VALUE Assuming(?newPosture) [5, 8]
		MEETS {
			Posture(?posture);
			?posture = ?newPosture;
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotHandType (Free(), Holding(object)) {
	
		VALUE Free() [1, +INF]
		MEETS {
			Holding(?object);
		}
		
		VALUE Holding(?object) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	
	COMP_TYPE SingletonStateVariable RobotCameraType (Idle(), _Observing(area)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			_Observing(?area);
		}
		
		VALUE _Observing(?area) [15, 23]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotArmControllerType (
		Idle(), 
		PlaceObject(object, location, area),
		PickUpObject(object, location, area)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			PlaceObject(?obj1, ?loc1, ?plArea1);
			PickUpObject(?obj2, ?loc2, ?plArea2);
		}
		
		VALUE PlaceObject(?obj, ?location, ?plArea) [5, 7]
		MEETS {
			Idle();
		}
		
		VALUE PickUpObject(?obj, ?location, ?plArea) [5, 7]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotControllerType (
		Idle(),
		GetObject(object, location),
		PutObject(object, location)) {
		
		VALUE Idle() [1, +INF]
		MEETS {
			GetObject(?obj1, ?loc1);
			PutObject(?obj2, ?loc2);
		}
		
		VALUE GetObject(?object, ?location) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE PutObject(?object, ?location) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable ICOMissionType (Idle(), ServeCoffeeTo(guest)) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			ServeCoffeeTo(?guest);
		} 
		
		VALUE ServeCoffeeTo(?guest) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	// ENVIRONMENT'S COMPONENTS
	COMPONENT Guest1 {FLEXIBLE guest1_position(trex_internal_dispatch_asap)} : GuestPosition;
	COMPONENT Mug1 {FLEXIBLE mug1_position(trex_internal_dispatch_asap)} : EnvironmentObjectPosition;
	COMPONENT Mug2 {FLEXIBLE mug2_position(trex_internal_dispatch_asap)} : EnvironmentObjectPosition;
	
	// ROBOT'S ARM COMPONENTS
	COMPONENT LeftArmController {FLEXIBLE left_arm(trex_internal_dispatch_asap)} : RobotArmControllerType;
	COMPONENT LeftArm {FLEXIBLE left_arm_posture(trex_internal_dispatch_asap)} : RobotArmType;
	
	COMPONENT LeftHand {FLEXIBLE left_hand(trex_internal_dispatch_asap)} : RobotHandType;
	
	// ROBOT'S COMPONENTS
	COMPONENT RobotBase {FLEXIBLE base(trex_internal_dispatch_asap)} : RobotBaseType;
	COMPONENT RobotCamera {FLEXIBLE camera(trex_internal_dispatch_asap)} : RobotCameraType;
	COMPONENT RobotController {FLEXIBLE robot(trex_internal_dispatch_asap)} : RobotControllerType;
	COMPONENT RobotTorso {FLEXIBLE robot_torso_posture(trex_internal_dispatch_asap)} : RobotTorsoType;
	
	// ICO MISSION
	COMPONENT Mission {FLEXIBLE ico(trex_internal_dispatch_asap)} : ICOMissionType; 
	

	SYNCHRONIZE Mission.ico {
	
		VALUE ServeCoffeeTo(?guest) {

			// check guest position
			cd0 <?> Guest1.guest1_position.At(?guestLocation);		
			
			// check a mug
			cd1 <?> Mug1.mug1_position.On(?mugInitialLocation, ?mugPlacingArea);

			// get the mug
			cd2 <!> RobotController.robot.GetObject(?getMug, ?getMugLocation);
			
			// place the mug
			cd3 <!> RobotController.robot.PutObject(?putMug, ?putMugLocation);
			
			cd4 <!> Mug1.mug1_position.On(?mugFinalLocation, ?mugFinalPlacingArea);
			
			// set temporal constraints on "conditions"
			DURING [0, +INF] [0, +INF] cd0;
			MET-BY cd1;
			MEETS cd4;
			
			// set temporal constraints
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd3;
			
			
			// check guest
			?guest = guest1;		
			// check mug
			?getMug = mug1;
			?mugPlacingArea = placingAreaLeft;
			?getMugLocation = ?mugInitialLocation;
			
			// check guest location
			?guestLocation = manipulationAreaSouthTable1;
			?mugFinalLocation = manipulationAreaEastTable1;
			?mugFinalPlacingArea = placingAreaLeft;
			
			?putMug = ?getMug;
			?putMugLocation = ?mugFinalLocation;
		}
		
		VALUE ServeCoffeeTo(?guest) {

			// check guest position
			cd0 <?> Guest1.guest1_position.At(?guestLocation);		
			
			// check a mug
			cd1 <?> Mug2.mug2_position.On(?mugInitialLocation, ?mugPlacingArea);

			// get the mug
			cd2 <!> RobotController.robot.GetObject(?getMug, ?getMugLocation);
			
			// place the mug
			cd3 <!> RobotController.robot.PutObject(?putMug, ?putMugLocation);
			
			cd4 <!> Mug2.mug2_position.On(?mugFinalLocation, ?mugFinalPlacingArea);
			
			// set temporal constraints on "conditions"
			DURING [0, +INF] [0, +INF] cd0;
			MET-BY cd1;
			MEETS cd4;
			
			// set temporal constraints
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd3;
			cd2 BEFORE [0, +INF] cd3;
			
			
			// check guest
			?guest = guest1;		
			// check mug
			?getMug = mug2;
			?mugPlacingArea = placingAreaLeft;
			?getMugLocation = ?mugInitialLocation;
			
			// check guest location
			?guestLocation = manipulationAreaSouthTable1;
			?mugFinalLocation = manipulationAreaEastTable1;
			?mugFinalPlacingArea = placingAreaLeft;
			
			?putMug = ?getMug;
			?putMugLocation = ?mugFinalLocation;
		}
	}
	
	SYNCHRONIZE RobotController.robot {
	
		VALUE GetObject(?object, ?location) {
		
			cd1 RobotBase.base.At(?robotLocation);
			
			cd2 <!> RobotCamera.camera._Observing(?placingArea);
		
			cd4 <!> LeftArmController.left_arm.PickUpObject(?pickObject, ?pickLocation, ?pickFromArea);
			
			DURING [0, +INF] [0, +INF] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd4;
			 
			cd2 MEETS cd4;
			
			?robotLocation = ?location;
			?pickLocation = ?location;
			?pickObject = ?object;
			?placingArea = placingAreaLeft;
			?pickFromArea = ?placingArea;
		}
		
		VALUE PutObject(?object, ?location) {
		
			cd1 RobotBase.base.At(?robotLocation);
			
			cd2 <!> RobotCamera.camera._Observing(?placingArea);
			
			cd4 <!> LeftArmController.left_arm.PlaceObject(?placeObject, ?placeLocation, ?placeArea);
			
			DURING [0, +INF] [0, +INF] cd1;
			
			CONTAINS [0, +INF] [0, +INF] cd2;
			CONTAINS [0, +INF] [0, +INF] cd4;
			
			cd2 MEETS cd4;
			
			?robotLocation = ?location;
			?placeLocation = ?location;
			?placeObject = ?object;
			?placingArea = placingAreaLeft;
			?placeArea = ?placingArea;
		}
	}
	
	SYNCHRONIZE LeftArmController.left_arm {
	
		VALUE PickUpObject(?object, ?location, ?area) {
		
			cd0 LeftArm.left_arm_posture.Posture(?armPosture);
			
			cd1 RobotTorso.robot_torso_posture.Posture(?torsoPosture);
		
			// precondition		
			cd2 <?> LeftHand.left_hand.Free();
			
			// effect
			cd3 <!> LeftHand.left_hand.Holding(?holdingObject);
			
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			STARTS-DURING [1, +INF] [1, +INF] cd2;
			ENDS-DURING [1, +INF] [1, +INF] cd3;
			
			?holdingObject = ?object;
			
			?armPosture = side;
			?torsoPosture = up;
		}
		
		VALUE PlaceObject(?object, ?location, ?area) {
		
			// precondition
			cd1 <?> LeftHand.left_hand.Holding(?holdingObject);
			
			cd0 LeftArm.left_arm_posture.Posture(?armPosture);
			
			cd2 RobotTorso.robot_torso_posture.Posture(?torsoPosture);
			
			// effect
			cd3 <!> LeftHand.left_hand.Free();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd2;
			STARTS-DURING [1, +INF] [1, +INF] cd1;
			ENDS-DURING [1, +INF] [1, +INF] cd3;
			
			?holdingObject = ?object;
			?armPosture = side;
			?torsoPosture = up;
		}
	}
	
	SYNCHRONIZE RobotBase.base {
	
		// moving while holding objects
		VALUE _MovingTo(?destination) {
		
			cd0 <?> LeftHand.left_hand.Holding(?object);
			
			cd1 LeftArm.left_arm_posture.Posture(?armPosture);
			
			cd2 RobotTorso.robot_torso_posture.Posture(?torsoPosture);
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			?armPosture = side;
			?torsoPosture = up;
		}
		
		// moving with free arms
		VALUE _MovingTo(?destination) {
		
			cd0 <?> LeftHand.left_hand.Free();
			
			cd1 LeftArm.left_arm_posture.Posture(?armPosture);
			
			cd2 RobotTorso.robot_torso_posture.Posture(?torsoPosture);
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
			
			?armPosture = tucked;
			?torsoPosture = down;
		}
	}
}