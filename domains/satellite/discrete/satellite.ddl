DOMAIN SATELLITE_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 200], 100;
	
	COMP_TYPE RenewableResource MemoryType (10) 
	
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
			cd0 <!> PointingMode.pm._Comm();
			cd1 <!> Memory.profile.REQUIREMENT(?amount);
			
			BEFORE [0, +INF] cd0; 
			STARTS-DURING [0, 0] [0, +INF] cd1;
			cd0 ENDS-DURING [0, +INF] [0, 0] cd1;
			
			?amount = 10;
		}
		
		VALUE _Comm()
		{
			cd0 <?> Window.channel.Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
}