DOMAIN GOAC_Domain {

	TEMPORAL_MODULE temporal_module = [0, 650], 500;

	PAR_TYPE NumericParameterType coordinate = [-1000, +1000];
	PAR_TYPE NumericParameterType angle = [-360, 360];
	PAR_TYPE NumericParameterType file_id = [0, 100];

	COMP_TYPE SingletonStateVariable RobotBaseType (GoingTo(coordinate, coordinate), At(coordinate, coordinate) )
	{
		VALUE At(?x1, ?y1) [1, +INF]

		MEETS {
			GoingTo(?x2, ?y2);
		}

		VALUE GoingTo(?x1, ?y1) [10, 30]

		MEETS {
			At(?x2, ?y2);
			?x1 = ?x2;
			?y1 = ?y2;
		}
		
	}

	COMP_TYPE SingletonStateVariable PlatineType (MovingTo(angle, angle), PointingAt(angle, angle) )
	{
		VALUE MovingTo(?pan1, ?tilt1)  [10, 30]

		MEETS {
			PointingAt(?pan2, ?tilt2);
			?pan1 = ?pan2;
			?tilt1 = ?tilt2;
		}

		VALUE PointingAt(?pan1, ?tilt1) [1, +INF]

		MEETS {
			MovingTo(?pan2, ?tilt2);
		}
	}

	COMP_TYPE SingletonStateVariable CameraType (CamIdle() , TakingPicture(file_id, coordinate, coordinate, angle, angle) )
	{
		VALUE CamIdle() [1, +INF]

		MEETS {
			TakingPicture(?file_id, ?x, ?y, ?pan, ?tilt);
		}

		VALUE TakingPicture(?file_id, ?x, ?y, ?pan, ?tilt) [10, 10]

		MEETS {
			CamIdle();
		}
	}

	COMP_TYPE SingletonStateVariable CommunicationType (CommIdle(), Communicating(file_id))
	{
		VALUE CommIdle() [1, +INF]

		MEETS {
			Communicating(?file_id);
		}

		VALUE Communicating(?file_id) [10, 30]

		MEETS {
			CommIdle();
		}
	}


	COMP_TYPE SingletonStateVariable CommunicationVWType (None(), Visible())
	{
		VALUE None()  [1, +INF]

		MEETS {
			Visible();
		}

		VALUE Visible()  [1, +INF]

		MEETS {
			None();
		}
	}

	COMP_TYPE SingletonStateVariable MissionTimelineType (Idle() , TakingPicture(file_id, coordinate, coordinate, angle, angle) , Communicating(file_id)  , At(coordinate, coordinate) )
	{
		VALUE Idle() [1, +INF] 
		MEETS 
		{
			TakingPicture(?file_id1, ?x1, ?y1, ?pan1, ?tilt1);
			Communicating(?file_id2);
			At(?x2, ?y2);
		}
	
		
		VALUE TakingPicture(?file_id, ?x, ?y, ?pan, ?tilt) [1, +INF]
		MEETS 
		{
			Idle();
		}

		VALUE Communicating(?file_id) [1, +INF]

		MEETS {
			Idle();
		}

		VALUE At(?x, ?y) [1, +INF]

		MEETS {
			Idle();
		}
	}

	
	COMPONENT RobotBase {FLEXIBLE robot_base(trex_external)} : RobotBaseType;
	COMPONENT Platine {FLEXIBLE platine(trex_external)} : PlatineType;
	COMPONENT Camera {FLEXIBLE camera(trex_external)} : CameraType;
	COMPONENT Communication {FLEXIBLE communication(trex_external)} : CommunicationType;
	COMPONENT MissionTimeline {FLEXIBLE mission_timeline(trex_internal,dispatch_asap)} : MissionTimelineType;
	COMPONENT CommunicationVW {FLEXIBLE communication_windows(uncontrollable)} : CommunicationVWType;

	SYNCHRONIZE MissionTimeline.mission_timeline {
		VALUE TakingPicture(?file_id1, ?x1, ?y1, ?pan1, ?tilt1) {
			cd1 <!> Camera.camera.TakingPicture(?file_id2, ?x2, ?y2, ?pan2, ?tilt2);
			cd5 <!> Communication.communication.Communicating(?file_id3);
			cd2 <!> MissionTimeline.mission_timeline.Idle();
			CONTAINS [0,+INF] [0,+INF] cd1;
			CONTAINS [0,+INF] [0,0] cd5;
			MEETS cd2;
			cd1 BEFORE [0, +INF] cd5;
			?x1 = ?x2;
			?y1 = ?y2;
			?pan1 = ?pan2;
			?tilt1 = ?tilt2;
			?file_id1 = ?file_id2;
			?file_id1 = ?file_id3;
		}
		
		
		VALUE At(?x3, ?y3) {
			cd1 RobotBase.robot_base.At(?x4, ?y4);
			EQUALS cd1;
			?x3 = ?x4;
			?y3 = ?y4;
		}
		
	}

	SYNCHRONIZE RobotBase.robot_base {
		VALUE GoingTo(?x1, ?y1) {
			cd2 Platine.platine.PointingAt(?pan1 = 0, ?tilt1 = 0);
			DURING [0, +INF] [0, +INF] cd2;
		}
	}

	
	SYNCHRONIZE Camera.camera {
		VALUE TakingPicture(?file_id1, ?x1, ?y1, ?pan1, ?tilt1) {
			cd3 <!> Platine.platine.PointingAt(?pan2, ?tilt2);
			cd2 <!> RobotBase.robot_base.At(?x2, ?y2);
			DURING [0, +INF] [0, +INF] cd2;
			DURING [0, +INF] [0, +INF] cd3;
			?x1 = ?x2;
			?y1 = ?y2;
			?pan1 = ?pan2;
			?tilt1 = ?tilt2;
		}
		
	}
	
	SYNCHRONIZE Communication.communication {
		VALUE Communicating(?file_id1) {
			cd4 RobotBase.robot_base.At(?x2, ?y2);
			cd1 <?> CommunicationVW.communication_windows.Visible();
			DURING [0, +INF] [0, +INF] cd4;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
	}
}

