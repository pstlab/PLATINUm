DOMAIN SATELLITE
{
	TEMPORAL_MODULE temporal_module = [0, 250], 500;
	
	COMP_TYPE RenewableResource EnergyConsumptionTraceType(10)
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Science(), _Comm(), Maintenance())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			_Comm();
			Maintenance();
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
		
		VALUE _Comm() [30, 50]
		MEETS {
			Earth();
			Maintenance();
		}
	}
	
	COMP_TYPE SingletonStateVariable GroundStationVisibilityType (Visible(), NotVisible())
	{
		VALUE Visible() [60, 100]
		MEETS {
			NotVisible();
		}
		
		VALUE NotVisible() [1, 100]
		MEETS {
			Visible();
		}
	}
	
	COMPONENT PointingMode {FLEXIBLE pm(trex_internal_dispatch_asap)} : PointingModeType;
	COMPONENT GroundStationVisibility {FLEXIBLE gv(uncontrollable)} : GroundStationVisibilityType;
	
	COMPONENT EnergyTrace {ESTA_LIGHT_MAX_CONSUMPTION energy(trex_external)} : EnergyConsumptionTraceType;
	
	SYNCHRONIZE PointingMode.pm {
	
		VALUE Science() {
		
			cd0 <!> EnergyTrace.energy.REQUIREMENT(?amount);
			
			cd1 <!> PointingMode.pm._Comm();
			
			BEFORE [0, +INF] cd1;
			EQUALS cd0;
			
			?amount = 3;
		}
	
		VALUE _Comm() {
		
			cd0 <?> GroundStationVisibility.gv.Visible();
			
			cd1 <!> EnergyTrace.energy.REQUIREMENT(?amount);
			
			DURING [0, +INF] [0, +INF] cd0;
			EQUALS cd1;
			
			?amount = 6;
		}
		
		VALUE Slewing() {
		
			cd0 <!> EnergyTrace.energy.REQUIREMENT(?amount);
			
			EQUALS cd0;
			
			?amount = 1;
		}
	}
}