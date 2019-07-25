DOMAIN ACTA_SATELLITE
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Science(), Comm(), Maintenance())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			Comm();
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
		
		VALUE Comm() [15, 23]
		MEETS {
			Earth();
			Maintenance();
		}
		
		VALUE Maintenance() [5, 7]
		MEETS {
			Earth();
		} 
	}
	

//	COMP_TYPE SingletonStateVariable GroundStationVisibilityType (Visible(), NotVisible())
//	{
//		VALUE Visible() [1, +INF]
//		MEETS {
//			NotVisible();
//		}
//		
//		VALUE NotVisible() [1, +INF]
//		MEETS {
//			Visible();
//		}
//	}
	
	COMPONENT PointingMode {FLEXIBLE pm(trex_internal_dispatch_asap)} : PointingModeType;
//	COMPONENT Window {FLEXIBLE channel(uncontrollable)} : GroundStationVisibilityType;
		
	SYNCHRONIZE PointingMode.pm
	{
		VALUE Science()
		{
			cd0 PointingMode.pm.Comm();
			
			BEFORE [0, +INF] cd0; 
		}
		
//		VALUE _Comm()
//		{
//			cd0 <?> Window.channel.Visible();
//			
//			DURING [0, +INF] [0, +INF] cd0;
//		}

	}
}
