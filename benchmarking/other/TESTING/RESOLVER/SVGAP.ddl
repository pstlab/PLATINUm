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
		
		VALUE Comm() [1, +INF]
		MEETS {
			Earth();
			Maintenance();
		}
		
		VALUE Maintenance() [1, +INF]
		MEETS {
			Earth();
		}
		
		VALUE Slewing() [1, +INF]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [1, +INF]
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
	COMPONENT GroundStation { FLEXIBLE gv(trex_internal_dispatch_asap) } : GroundStationVisibilityType;

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