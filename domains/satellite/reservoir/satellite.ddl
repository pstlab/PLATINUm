DOMAIN RESERVOIR_SATELLITE
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE ConsumableResource BatteryType (0, 10) 
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Science(), _Comm(), Maintenance())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			_Comm();
			Maintenance();
		}
		
		VALUE Slewing() [3, 3]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [8, 11]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [10, 18]
		MEETS {
			Earth();
			Maintenance();
		}
		
		VALUE Maintenance() [5, 7]
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
			
			?amount = 4;
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
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
}