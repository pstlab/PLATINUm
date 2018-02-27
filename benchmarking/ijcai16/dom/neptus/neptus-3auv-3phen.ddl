DOMAIN NEPTUS
{
	TEMPORAL_MODULE temporal_module = [0, 200], 500;
	PAR_TYPE EnumerationParameterType location = {base, location1, location2, location3, location4, location5};
	PAR_TYPE EnumerationParameterType phenomena = {phenomena1, phenomena2, phenomena3};
	PAR_TYPE EnumerationParameterType auvId = {a1, a2, a3};
	PAR_TYPE NumericParameterType file = [1, 100];
	
	COMP_TYPE SingletonStateVariable NavigationControllerType(At(location), MovingTo(location))
	{	
		VALUE At(?x) [1, +INF]
		MEETS {
			MovingTo(?y);
			?x != ?y;
		}
		
		VALUE MovingTo(?x) [5, 30]
		MEETS {
			At(?y);
			?y = ?x;
		}
	}
	
	COMP_TYPE SingletonStateVariable PayloadControllerType (Idle(), TurningOn(), TurningOff(), Analyzing(phenomena))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			TurningOn();
		}
		
		VALUE TurningOn() [5, 5]
		MEETS {
			Analyzing(?p);	
		}
		
		VALUE Analyzing(?p) [7, 23]
		MEETS {
			TurningOff();
		}
		
		VALUE TurningOff() [5, 5]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable AUVControllerType (Idle(), GoTo(location), Sample(location, phenomena, file), CollectData(file))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			GoTo(?l1);
			Sample(?l2, ?p2, ?f2);
			CollectData(?f4);
		}
		
		VALUE GoTo(?l) [1, 30]
		MEETS {
			Idle();
		}
		
		VALUE Sample(?l, ?p, ?f) [6, 18]
		MEETS {
			Idle();
		}
		
		VALUE CollectData(?f) [3, 11]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable MissionControllerType(Idle(), MakeSample(location, phenomena, file))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			MakeSample(?l1, ?p1, ?f1);
		}
		
		VALUE MakeSample(?l, ?p, ?f) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	// AUV1's components
	COMPONENT AUV1Controller {FLEXIBLE auv1(trex_internal)} : AUVControllerType;
	COMPONENT AUV1NavigationController {FLEXIBLE auv1_navigator(trex_internal)} : NavigationControllerType;
	COMPONENT AUV1SamplePayloadController {FLEXIBLE auv1_payload_sample(trex_internal)} : PayloadControllerType;
	
	// AUV2's components
	COMPONENT AUV2Controller {FLEXIBLE auv2(trex_internal)} : AUVControllerType;
	COMPONENT AUV2NavigationController {FLEXIBLE auv2_navigator(trex_internal)} : NavigationControllerType;
	COMPONENT AUV2SamplePayloadController {FLEXIBLE auv2_payload_sample(trex_internal)} : PayloadControllerType;
	
	// AUV3's components
	COMPONENT AUV3Controller {FLEXIBLE auv3(trex_internal)} : AUVControllerType;
	COMPONENT AUV3NavigationController {FLEXIBLE auv3_navigator(trex_internal)} : NavigationControllerType;
	COMPONENT AUV3SamplePayloadController {FLEXIBLE auv3_payload_sample(trex_internal)} : PayloadControllerType;
	
	// Mission's component
	COMPONENT MissionController {FLEXIBLE mission(trex_internal)} : MissionControllerType;
	
	// Mission constraints for AUV1
	SYNCHRONIZE MissionController.mission 
	{
		// MakeSample for AUV1
		VALUE MakeSample(?location, ?phenomena, ?data)
		{
			cd0 <!> AUV1Controller.auv1.Sample(?location0, ?phenomena0, ?data0);
			cd1 <!> AUV1Controller.auv1.CollectData(?data1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			BEFORE [0, +INF] cd1;
			
			?location0 = ?location;
			?phenomena0 = ?phenomena;
			?data0 = ?data;
			?data1 = ?data;
		}
		
		// MakeSample for AUV2
		VALUE MakeSample(?location, ?phenomena, ?data)
		{
			cd0 <!> AUV2Controller.auv2.Sample(?location0, ?phenomena0, ?data0);
			cd1 <!> AUV2Controller.auv2.CollectData(?data1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			BEFORE [0, +INF] cd1;
			
			?location0 = ?location;
			?phenomena0 = ?phenomena;
			?data0 = ?data;
			?data1 = ?data;
		}
		
		// MakeSample for AUV3
		VALUE MakeSample(?location, ?phenomena, ?data)
		{
			cd0 <!> AUV3Controller.auv3.Sample(?location0, ?phenomena0, ?data0);
			cd1 <!> AUV3Controller.auv3.CollectData(?data1);
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			BEFORE [0, +INF] cd1;
			
			?location0 = ?location;
			?phenomena0 = ?phenomena;
			?data0 = ?data;
			?data1 = ?data;
		}
	}
	
	// AUV1 sample implementation constraints
	SYNCHRONIZE AUV1Controller.auv1
	{
		VALUE Sample(?l, ?p, ?f)
		{
			cd0 AUV1NavigationController.auv1_navigator.At(?l0);
			cd1 <!> AUV1SamplePayloadController.auv1_payload_sample.Analyzing(?p1);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?p1 = ?p;
			?l0 = ?l;
		}
		
		VALUE CollectData(?f)
		{
			cd0 AUV1NavigationController.auv1_navigator.At(?l0);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?l0 = base;
		}
	}
	
	
	
	// AUV2 sample implementation constraints
	SYNCHRONIZE AUV2Controller.auv2
	{
		VALUE Sample(?l, ?p, ?f)
		{
			cd0 AUV2NavigationController.auv2_navigator.At(?l0);
			cd1 <!> AUV2SamplePayloadController.auv2_payload_sample.Analyzing(?p1);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?p1 = ?p;
			?l0 = ?l;
		}
		
		VALUE CollectData(?f)
		{
			cd0 AUV2NavigationController.auv2_navigator.At(?l0);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?l0 = base;
		}
	}
	
	// AUV3 sample implementation constraints
	SYNCHRONIZE AUV3Controller.auv3
	{
		VALUE Sample(?l, ?p, ?f)
		{
			cd0 AUV3NavigationController.auv3_navigator.At(?l0);
			cd1 <!> AUV3SamplePayloadController.auv3_payload_sample.Analyzing(?p1);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			
			?p1 = ?p;
			?l0 = ?l;
		}
		
		VALUE CollectData(?f)
		{
			cd0 AUV3NavigationController.auv3_navigator.At(?l0);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?l0 = base;
		}
	}
}