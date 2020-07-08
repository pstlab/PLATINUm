DOMAIN DRUG_DELIVERY
{	
	TEMPORAL_MODULE temporal_module = [0, 480], 500;

	PAR_TYPE EnumerationParameterType room = {Magazine, Room0, Room1, Room2, Room3};
	PAR_TYPE EnumerationParameterType patient = {Patient0, Patient1, Patient2};	
	
	
	
	COMP_TYPE SingletonStateVariable MissionTimelineSV (
		Idle(), Deliver(room, patient)
	)
	{
		VALUE Idle() [1, +INF]
		MEETS 
		{
			Deliver(?room, ?patient);
		}
		
		VALUE Deliver(?room, ?patient) [1, +INF]
		MEETS
		{
			Idle();
		}
	}
		
	COMP_TYPE SingletonStateVariable RobotMoveToRoomSV (
		GoingTo(room), At(room)
	)
	{
		VALUE At(?x1) [1, +INF]
		MEETS 
		{
			GoingTo(?x2);
		}
		
		VALUE GoingTo(?x1) [1, 30]
		MEETS 
		{
			At(?x2);
			?x2 = ?x1;
		}
	}
	
	COMP_TYPE SingletonStateVariable RobotDeliveryToPatientSV (
		Idle(), MoveTo(room, patient), Stop(room, patient) 
	)
	{		
		VALUE MoveTo(?roomX, ?patientX) [1, 2]
		MEETS 
		{
			Stop(?roomY, ?patientY);
			
			?roomY = ?roomX;
			?patientY = ?patientX;
		}
		
		VALUE Stop(?roomX, ?patientX) [1, 3]
		MEETS 
		{
			Idle();
//			MoveTo(?room, ?patient);
		}
		
		VALUE Idle() [1, +INF] 
		MEETS 
		{
			MoveTo(?room, ?patient);
		}
	}

	COMPONENT RobotDeliveryToPatient {FLEXIBLE robot_delivery(primitive)} : RobotDeliveryToPatientSV;
	COMPONENT RobotMoveToRoom {FLEXIBLE robot_move_to_room(primitive)} : RobotMoveToRoomSV;	
	COMPONENT MissionTimeline {FLEXIBLE mission_timeline(functional)}: MissionTimelineSV;


	
	SYNCHRONIZE MissionTimeline.mission_timeline {
	
		VALUE  Deliver(?roomX, ?patientX) {
			
			cd0 RobotMoveToRoom.robot_move_to_room.At(?room0);
			cd1 RobotDeliveryToPatient.robot_delivery.Stop(?room1, ?patient1);
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
						

			?room0 = ?roomX;
			?room1 = ?roomX;
			?patient1 = ?patientX;
		}			
	}
	
	SYNCHRONIZE RobotDeliveryToPatient.robot_delivery
	{
		VALUE MoveTo(?r, ?p)
		{
			cd0 RobotMoveToRoom.robot_move_to_room.At(?r0);
			
			DURING [0, +INF] [0, +INF] cd0;
			
			?r0 = ?r;
		}
	}

}