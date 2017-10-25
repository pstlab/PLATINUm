DOMAIN BATTERY_RESERVOIR_SATELLITE_V1
{
	TEMPORAL_MODULE temporal_module = [0, 1000], 100;
	
	COMP_TYPE ConsumableResource BatteryType (0, 10) 
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Science(), _Comm(), _Recharging())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			_Comm();
		}
		
		VALUE Slewing() [3, 7]
		MEETS {
			Earth();
			Science();
			_Recharging();
		}
		
		VALUE Science() [8, 12]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [11, 18]
		MEETS {
			Earth();
		}
	
		VALUE _Recharging() [7, 23]
		MEETS {
			Slewing();
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
	
		VALUE _Recharging() 
		{
			cd0 <?> SunWindow.sun.Visible();
			
			cd0 DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE SatelliteBattery.profile
	{
		VALUE PRODUCTION(?amount)
		{
			
			cd1 PointingMode.pm._Recharging();
			
			EQUALS cd1;
		}
	}
}