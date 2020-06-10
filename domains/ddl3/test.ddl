DOMAIN DDL3 {

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
	
	COMP_TYPE SingletonStateVariable WindowType (Available(), NotAvailable())
	{
		VALUE Available() [1, +INF]
		MEETS {
			NotAvailable();
		}
		
		VALUE NotAvailable() [1, +INF]
		MEETS {
			Available();
		}
	}
	
	COMP_TYPE SingletonStateVariable RoverControllerType (Idle(), Recharging(), TakeSample(location))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			TakeSample(?location);
			Recharging();
		}
		
		VALUE TakeSample(?location) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Recharging() [5, 5]
		MEETS {
			Idle();
		}
	}
	
	COMPONENT BaseController {FLEXIBLE position(trex_internal_dispatch_asap)} : BaseControllerType;
	COMPONENT Energy {FLEXIBLE profile(trex_internal_dispatch_asap)} : MotionEnergyType;
	COMPONENT Battery {FLEXIBLE level(trex_internal_dispatch_asap)} : BatteryType;
	COMPONENT Window {FLEXIBLE channel(uncontrollable)} : WindowType;
	COMPONENT RoverController {FLEXIBLE mission(functional)} : RoverControllerType;
	
	SYNCHRONIZE Battery.level
	{
		VALUE PRODUCTION(?amount)
		{
			cd0 RoverController.mission.Recharging();
			
			EQUALS cd0;
			
			?amount = 10;
		} 
	}
	
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
	
	SYNCHRONIZE RoverController.mission
	{
		VALUE Recharging()
		{
			cd0 BaseController.position.At(?location);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?location = location3;
		}
	}
}