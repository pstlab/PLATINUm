DOMAIN BATTERY_SATELLITE_SIMPLE
{
	TEMPORAL_MODULE temporal_module = [0, 500], 100;
	
	COMP_TYPE SingletonStateVariable SatelliteType (Idle(), Science(), _Communicate())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Science();
			_Communicate();
		}
		
		VALUE Science() [3, 7]
		MEETS {
			Idle();
		}
		
		VALUE _Communicate() [5, 8]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable PointingModeType (Earth(), Slewing(), Sun(), Target())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
		}
		
		VALUE Slewing() [1, +INF]
		MEETS {
			Earth();
			Target();
			Sun();
		}
		
		VALUE Sun() [1, +INF]
		MEETS {
			Slewing();
		}
		
		VALUE Target() [1, +INF]
		MEETS {
			Slewing();
		}
	}
	
	COMP_TYPE SingletonStateVariable VisibilityWindowType (Visible(), NotVisible())
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
	
	COMPONENT Satellite {FLEXIBLE operations(functional)} : SatelliteType;
	COMPONENT PointingMode {FLEXIBLE orientation(primitive)} : PointingModeType;
	COMPONENT GroundStationWindow {FLEXIBLE channel(uncontrollable)} : VisibilityWindowType;
	COMPONENT SunWindow {FLEXIBLE visibility(uncontrollable)} : VisibilityWindowType; 
		
	SYNCHRONIZE Satellite.operations 
	{
		VALUE Science()
		{
			cd2 <!> PointingMode.orientation.Target();
			cd1 <!> Satellite.operations._Communicate();
			
			BEFORE [0, +INF] cd1;
			DURING [0, +INF] [0, +INF] cd2;
		}
		
		VALUE _Communicate()
		{
			cd0 PointingMode.orientation.Earth();
			cd3 <?> GroundStationWindow.channel.Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd3;
		}
	}
}