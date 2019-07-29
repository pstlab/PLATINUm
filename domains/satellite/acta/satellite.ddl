DOMAIN ACTA_SATELLITE
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
		
		VALUE Slewing() [3, 7]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [10, 20]
		MEETS {
			Slewing();
		}
		
		VALUE _Comm() [15, 25]
		MEETS {
			Earth();
			_Maintenance();
		}
		
		VALUE _Maintenance() [1, 12]
		MEETS {
			Earth();
		} 
	}
	

	COMP_TYPE SingletonStateVariable GroundStationVisibilityType (_Visible(), _NotVisible())
	{
		VALUE _Visible() [1, +INF]
		MEETS {
			_NotVisible();
		}
		
		VALUE _NotVisible() [1, +INF]
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
