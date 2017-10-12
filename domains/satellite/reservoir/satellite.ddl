DOMAIN RESERVOIR_SATELLITE
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE ConsumableResource BatteryType (0, 10) 
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Science(), _Comm(), _Recharging())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			_Comm();
		}
		
		VALUE _Recharging() [3, 7]
		MEETS {
			Slewing();
		}
		
		VALUE Slewing() [3, 3]
		MEETS {
			Earth();
			Science();
			_Recharging();
		}
		
		VALUE Science() [8, 11]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [10, 18]
		MEETS {
			Earth();
		}
	}
	
	COMP_TYPE SingletonStateVariable VisibilityWindowType (Visible(), NotVisible())
	{
		VALUE Visible() [1, +INF]
		MEETS {
			NotVisible();
		}
		
		VALUE NotVisible() [1, +INF]
		MEETS {
			Visible();
		}
	}
	
	COMPONENT PointingMode {FLEXIBLE pm(primitive)} : PointingModeType;
	COMPONENT GroundStationWindow {FLEXIBLE channel(uncontrollable)} : VisibilityWindowType;
	COMPONENT SunWindow {FLEXIBLE sun(uncontrollable)} : VisibilityWindowType; 
	COMPONENT SatelliteBattery {FLEXIBLE profile(primitive)} : BatteryType;
		
	SYNCHRONIZE PointingMode.pm
	{
		VALUE Science()
		{
			cd0 <!> SatelliteBattery.profile.CONSUMPTION(?amount);
			cd1 <!> PointingMode.pm._Comm();
			
			BEFORE [0, +INF] cd1;
			
			EQUALS cd0;
			
			?amount = 3;
		}
		
		VALUE _Comm()
		{
			cd0 <?> GroundStationWindow.channel.Visible();
			cd1 <!> SatelliteBattery.profile.CONSUMPTION(?amount);
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			
			?amount = 4;
		}
	}
	
	SYNCHRONIZE SatelliteBattery.profile
	{
		VALUE PRODUCTION(?amount)
		{
			cd0 <?> SunWindow.sun.Visible();
			cd1 <!> PointingMode.pm._Recharging(); 
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
		}
	}
}