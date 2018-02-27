DOMAIN SATELLITE_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 300], 500;
	
	COMP_TYPE SingletonStateVariable OperatingStatusType (Idle(), Science(), Maintenance())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Science();
			Maintenance();
		}
		
		VALUE Science() [36, 58]
		MEETS {
			Idle();
		}
		
		VALUE Maintenance() [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable CommunicationType (Idle(), Comm())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Comm();
		}
	
		VALUE Comm() [30, 50]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Planet(), Slewing())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
		}

		VALUE Slewing() [30, 30]
		MEETS {
			Earth();
			Planet();
		}
		
		VALUE Planet() [36, 58]
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
	
	COMPONENT Operative { FLEXIBLE op(trex_internal_dispatch_asap) } : OperatingStatusType;
	COMPONENT PointingMode { FLEXIBLE pm(trex_internal_dispatch_asap) } : PointingModeType;
	COMPONENT Communication { FLEXIBLE cm(trex_internal_dispatch_asap) } : CommunicationType;
	COMPONENT GroundStation { FLEXIBLE gv(uncontrollable) } : GroundStationVisibilityType;

	SYNCHRONIZE Operative.op
	{
		VALUE Science()
		{
			cd0 PointingMode.pm.Planet();
			cd1 Communication.cm.Comm();
			
			DURING [0, +INF] [0, +INF] cd0;
			BEFORE [0, +INF] cd1;
		}
	}
	
	SYNCHRONIZE Communication.cm
	{
		VALUE Comm()
		{
			cd0 PointingMode.pm.Earth();
			cd1 <?> GroundStation.gv.Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
	}
}