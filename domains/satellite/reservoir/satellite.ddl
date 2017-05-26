DOMAIN SATELLITE_Reservoir
{
	TEMPORAL_MODULE temporal_module = [0, 200], 100;
	
	COMP_TYPE ConsumableResource MemoryType (0, 10) 
	
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
		
		VALUE Science() [11, 18]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [15, 23]
		MEETS {
			Earth();
			Maintenance();
		}
		
		VALUE Maintenance() [5, 7]
		MEETS {
			Earth();
		} 
	}
	
	COMP_TYPE SingletonStateVariable GroundStationVisibilityType (Visible(), NotVisible())
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
	COMPONENT Window {FLEXIBLE channel(uncontrollable)} : GroundStationVisibilityType;
	COMPONENT Memory {FLEXIBLE profile(primitive)} : MemoryType;
		
	SYNCHRONIZE PointingMode.pm
	{
		VALUE Science()
		{
			cd0 <!> Memory.profile.CONSUMPTION(?amount);
			
			EQUALS cd0;
			
			?amount = 10;
		}
		
		VALUE _Comm()
		{
			cd0 <?> Window.channel.Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE Memory.profile
	{
		VALUE PRODUCTION(?amount)
		{
			cd0 <!> PointingMode.pm._Comm();
			
			EQUALS cd0;
			
			?amount = 10;
		}
	}
}