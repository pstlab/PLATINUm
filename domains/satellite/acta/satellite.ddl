DOMAIN ACTA_SATELLITE
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), rSlewing(), rScience(), _Comm(), _Maintenance())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			rSlewing();
			_Comm();
			_Maintenance();
		}
		
		VALUE rSlewing() [3, 7]
		MEETS {
			Earth();
			rScience();
		}
		
		VALUE rScience() [10, 20]
		MEETS {
			rSlewing();
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
		VALUE rScience()
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
