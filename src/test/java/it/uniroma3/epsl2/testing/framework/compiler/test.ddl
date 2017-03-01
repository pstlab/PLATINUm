DOMAIN Test {

	TEMPORAL_MODULE temporal_module = [0, 100], 500;	
	
	PAR_TYPE EnumerationParameterType location = {
		location1, location2, location3
	};

	COMP_TYPE RenewableResource MotionEnergyType (10)
	
	COMP_TYPE ConsumableResource BatteryType (5, 35)

	COMP_TYPE SingletonStateVariable BaseControllerType (At(location), _MoveTo(location)) 
	{
		VALUE At(?location) [1, +INF]
		MEETS {
			_MoveTo(?destination);
			?destination != ?location;
		}
		
		VALUE _MoveTo(?location) [7, 32]
		MEETS {
			At(?destination);
			?destination = ?location;
		}
	}
	
	COMPONENT BaseController {FLEXIBLE position(trex_internal_dispatch_asap)} : BaseControllerType;
	COMPONENT Energy {FLEXIBLE profile(trex_internal_dispatch_asap)} : MotionEnergyType;
	COMPONENT Battery {FLEXIBLE level(trex_internal_dispatch_asap)} : BatteryType;
	
	SYNCHRONIZE BaseController.position
	{
		VALUE _MoveTo(?location)
		{
			cd0 Energy.profile.REQUIREMENT(?amount);
			cd1 Battery.level.CONSUMPTION(?cons);
			
			EQUALS cd0;
			
			?amount = 7;
			?cons = 3;
		}
	}
}