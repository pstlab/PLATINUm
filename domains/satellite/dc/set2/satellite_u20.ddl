DOMAIN DC_SATELLITE_s2_u20
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE SingletonStateVariable PointingModeType (
		Earth(), Slewing(), Science(), _Comm(), _Maintenance()
	)
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			_Comm();
			_Maintenance();
		}
		
		VALUE Slewing() [3, 3]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [3, 3]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [1, 23]
		MEETS {
			Earth();
			_Maintenance();
		}
		
		VALUE _Maintenance() [1, 24]
		MEETS {
			Earth();
		} 
	}
	

	COMP_TYPE SingletonStateVariable GroundStationVisibilityType (
		_Visible(), NotVisible()
	)
	{
		VALUE _Visible() [20, 80]
		MEETS {
			NotVisible();
		}
		
		VALUE NotVisible() [1, +INF]
		MEETS {
			_Visible();
		}
	}
	
	COMPONENT PointingMode {FLEXIBLE pm(primitive)} : PointingModeType;
	COMPONENT Window {FLEXIBLE channel(primitive)} : GroundStationVisibilityType;
		
	SYNCHRONIZE PointingMode.pm
	{
		VALUE Science()
		{
			cd0 PointingMode.pm._Comm();
			
			BEFORE [0, +INF] cd0; 
		}
		
		VALUE _Comm()
		{
			cd0 <?> Window.channel._Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
		}

	}
}
