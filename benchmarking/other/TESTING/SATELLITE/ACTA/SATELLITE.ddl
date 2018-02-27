DOMAIN SATELLITE_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 300], 500;
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Comm(), Maintenance(), Slewing(), Science())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Comm();
			Maintenance();
			Slewing();
		}
		
		VALUE Comm() [30, 50]
		MEETS {
			Earth();
			Maintenance();
		}
		
		VALUE Maintenance() [90, 90]
		MEETS {
			Earth();
		}
		
		VALUE Slewing() [30, 30]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [36, 58]
		MEETS {
			Slewing();
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
	
	
	COMPONENT PointingMode { FLEXIBLE pm(trex_internal_dispatch_asap) } : PointingModeType;
	COMPONENT GroundStation { FLEXIBLE gv(uncontrollable) } : GroundStationVisibilityType;

	SYNCHRONIZE PointingMode.pm
	{
		VALUE Science()
		{
			cd0 <?> GroundStation.gv.Visible();
			cd1 PointingMode.pm.Comm();
			
			cd1 DURING [0, +INF] [0, +INF] cd0;
			BEFORE [0, +INF] cd1;
		}
	}
}