DOMAIN box_Domain {

	TEMPORAL_MODULE temporal_module = [0, 1000], 500;

	COMP_TYPE SingletonStateVariable box_ChannelType (Idle(), ChannelFR(), ChannelFF(), ChannelRR(), ChannelLF()) {

		VALUE Idle() [1, 1000]
		MEETS {
			ChannelFR();
			ChannelFF();
			ChannelRR();
			ChannelLF();
		}

		VALUE ChannelFR() [20, 35]
		MEETS {
			Idle();
		}

		VALUE ChannelFF() [10, 20]
		MEETS {
			Idle();
		}

		VALUE ChannelRR() [20, 35]
		MEETS {
			Idle();
		}

		VALUE ChannelLF() [20, 35]
		MEETS {
			Idle();
		}

	}

	COMP_TYPE SingletonStateVariable box_Cross_Conveyor_1Type (Cross_Conveyor_1_Forward(), Cross_Conveyor_1_Still(), Cross_Conveyor_1_Backward()) {

		VALUE Cross_Conveyor_1_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Still();
		}

		VALUE Cross_Conveyor_1_Still() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Forward();
			Cross_Conveyor_1_Backward();
		}

		VALUE Cross_Conveyor_1_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Still();
		}

	}

	COMP_TYPE SingletonStateVariable box_Main_ConveyorType (Main_Conveyor_Backward(), Main_Conveyor_Still(), Main_Conveyor_Forward()) {

		VALUE Main_Conveyor_Backward() [1, 1000]
		MEETS {
			Main_Conveyor_Still();
		}

		VALUE Main_Conveyor_Still() [1, 1000]
		MEETS {
			Main_Conveyor_Backward();
			Main_Conveyor_Forward();
		}

		VALUE Main_Conveyor_Forward() [1, 1000]
		MEETS {
			Main_Conveyor_Still();
		}

	}

	COMP_TYPE SingletonStateVariable box_Cross_Transfer_2Type (Cross_2_Down(), Cross_2_Changing(), Cross_2_Up()) {

		VALUE Cross_2_Down() [1, 1000]
		MEETS {
			Cross_2_Changing();
		}

		VALUE Cross_2_Changing() [3, 3]
		MEETS {
			Cross_2_Down();
			Cross_2_Up();
		}

		VALUE Cross_2_Up() [1, 1000]
		MEETS {
			Cross_2_Changing();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_L1Type (Port_L1_Not_Available(), Port_L1_Available()) {

		VALUE Port_L1_Not_Available() [1, 1000]
		MEETS {
			Port_L1_Available();
		}

		VALUE Port_L1_Available() [1, 1000]
		MEETS {
			Port_L1_Not_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_B1Type (Port_B1_Available(), Port_B1_Not_Available()) {

		VALUE Port_B1_Available() [1, 1000]
		MEETS {
			Port_B1_Not_Available();
		}

		VALUE Port_B1_Not_Available() [1, 1000]
		MEETS {
			Port_B1_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_F1Type (Port_F1_Available(), Port_F1_Not_Available()) {

		VALUE Port_F1_Available() [1, 1000]
		MEETS {
			Port_F1_Not_Available();
		}

		VALUE Port_F1_Not_Available() [1, 1000]
		MEETS {
			Port_F1_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_L2Type (Port_L2_Available(), Port_L2_Not_Available()) {

		VALUE Port_L2_Available() [1, 1000]
		MEETS {
			Port_L2_Not_Available();
		}

		VALUE Port_L2_Not_Available() [1, 1000]
		MEETS {
			Port_L2_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_F2Type (Port_F2_Available(), Port_F2_Not_Available()) {

		VALUE Port_F2_Available() [1, 1000]
		MEETS {
			Port_F2_Not_Available();
		}

		VALUE Port_F2_Not_Available() [1, 1000]
		MEETS {
			Port_F2_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Cross_Transfer_1Type (Cross_1_Changing(), Cross_1_Up(), Cross_1_Down()) {

		VALUE Cross_1_Changing() [3, 3]
		MEETS {
			Cross_1_Up();
			Cross_1_Down();
		}

		VALUE Cross_1_Up() [1, 1000]
		MEETS {
			Cross_1_Changing();
		}

		VALUE Cross_1_Down() [1, 1000]
		MEETS {
			Cross_1_Changing();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_R1Type (Port_R1_Available(), Port_R1_Not_Available()) {

		VALUE Port_R1_Available() [1, 1000]
		MEETS {
			Port_R1_Not_Available();
		}

		VALUE Port_R1_Not_Available() [1, 1000]
		MEETS {
			Port_R1_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Cross_Conveyor_2Type (Cross_Conveyor_2_Backward(), Cross_Conveyor_2_Forward(), Cross_Conveyor_2_Still()) {

		VALUE Cross_Conveyor_2_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Still();
		}

		VALUE Cross_Conveyor_2_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Still();
		}

		VALUE Cross_Conveyor_2_Still() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Backward();
			Cross_Conveyor_2_Forward();
		}

	}

	COMP_TYPE SingletonStateVariable box_Port_R2Type (Port_R2_Available(), Port_R2_Not_Available()) {

		VALUE Port_R2_Available() [1, 1000]
		MEETS {
			Port_R2_Not_Available();
		}

		VALUE Port_R2_Not_Available() [1, 1000]
		MEETS {
			Port_R2_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Cross_Conveyor_3Type (Main_Conveyor_3_Forward(), Cross_Conveyor_3_Still(), Cross_Conveyor_3_Backward()) {

		VALUE Main_Conveyor_3_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_3_Still();
		}

		VALUE Cross_Conveyor_3_Still() [1, 1000]
		MEETS {
			Main_Conveyor_3_Forward();
			Cross_Conveyor_3_Backward();
		}

		VALUE Cross_Conveyor_3_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_3_Still();
		}

	}

	COMP_TYPE SingletonStateVariable box_Neighbor_RType (Neighbor_R_not_Available(), Neighbor_R_Available()) {

		VALUE Neighbor_R_not_Available() [1, 1000]
		MEETS {
			Neighbor_R_Available();
		}

		VALUE Neighbor_R_Available() [1, 1000]
		MEETS {
			Neighbor_R_not_Available();
		}

	}

	COMP_TYPE SingletonStateVariable box_Neighbor_FType (Neighbor_F_Available(), Neighbor_F_not_Available()) {

		VALUE Neighbor_F_Available() [1, 1000]
		MEETS {
			Neighbor_F_not_Available();
		}

		VALUE Neighbor_F_not_Available() [1, 1000]
		MEETS {
			Neighbor_F_Available();
		}

	}

	COMPONENT box_Channel { FLEXIBLE box_Channel_timeline(trex_internal_dispatch_asap) } : box_ChannelType;
	COMPONENT box_Cross_Conveyor_1 { FLEXIBLE box_Cross_Conveyor_1_timeline(trex_internal_dispatch_asap) } : box_Cross_Conveyor_1Type;
	COMPONENT box_Main_Conveyor { FLEXIBLE box_Main_Conveyor_timeline(trex_internal_dispatch_asap) } : box_Main_ConveyorType;
	COMPONENT box_Cross_Transfer_2 { FLEXIBLE box_Cross_Transfer_2_timeline(trex_internal_dispatch_asap) } : box_Cross_Transfer_2Type;
	COMPONENT box_Port_L1 { FLEXIBLE box_Port_L1_timeline(trex_internal_dispatch_asap) } : box_Port_L1Type;
	COMPONENT box_Port_B1 { FLEXIBLE box_Port_B1_timeline(trex_internal_dispatch_asap) } : box_Port_B1Type;
	COMPONENT box_Port_F1 { FLEXIBLE box_Port_F1_timeline(trex_internal_dispatch_asap) } : box_Port_F1Type;
	COMPONENT box_Port_L2 { FLEXIBLE box_Port_L2_timeline(trex_internal_dispatch_asap) } : box_Port_L2Type;
	COMPONENT box_Port_F2 { FLEXIBLE box_Port_F2_timeline(trex_internal_dispatch_asap) } : box_Port_F2Type;
	COMPONENT box_Cross_Transfer_1 { FLEXIBLE box_Cross_Transfer_1_timeline(trex_internal_dispatch_asap) } : box_Cross_Transfer_1Type;
	COMPONENT box_Port_R1 { FLEXIBLE box_Port_R1_timeline(trex_internal_dispatch_asap) } : box_Port_R1Type;
	COMPONENT box_Cross_Conveyor_2 { FLEXIBLE box_Cross_Conveyor_2_timeline(trex_internal_dispatch_asap) } : box_Cross_Conveyor_2Type;
	COMPONENT box_Port_R2 { FLEXIBLE box_Port_R2_timeline(trex_internal_dispatch_asap) } : box_Port_R2Type;
	COMPONENT box_Cross_Conveyor_3 { FLEXIBLE box_Cross_Conveyor_3_timeline(trex_internal_dispatch_asap) } : box_Cross_Conveyor_3Type;
	COMPONENT box_Neighbor_R { FLEXIBLE box_Neighbor_R_timeline(uncontrollable) } : box_Neighbor_RType;
	COMPONENT box_Neighbor_F { FLEXIBLE box_Neighbor_F_timeline(uncontrollable) } : box_Neighbor_FType;

	SYNCHRONIZE box_Channel.box_Channel_timeline {

		VALUE ChannelLF() {

			cd0 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Main_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Cross_Conveyor_2.box_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 box_Port_F1.box_Port_F1_timeline.Port_F1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> box_Neighbor_R.box_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6 box_Port_R2.box_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			DURING [0,+INF] [0,+INF]  cd7;

			cd8  <?> box_Neighbor_F.box_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE ChannelLF() {

			cd0 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Main_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Cross_Conveyor_1.box_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 box_Port_F2.box_Port_F2_timeline.Port_F2_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 box_Port_R1.box_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> box_Neighbor_F.box_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> box_Neighbor_R.box_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE ChannelRR() {

			cd0 box_Cross_Conveyor_2.box_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 box_Port_R2.box_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6 box_Port_R1.box_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> box_Neighbor_R.box_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE ChannelFF() {

			cd0 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Main_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Main_Conveyor.box_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Up();
			CONTAINS [0,+INF] [0,+INF]  cd4;

			cd5 box_Cross_Conveyor_1.box_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd5;

			cd6 box_Port_F2.box_Port_F2_timeline.Port_F2_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7 box_Port_F1.box_Port_F1_timeline.Port_F1_Available();
			DURING [0,+INF] [0,+INF]  cd7;

			cd8  <?> box_Neighbor_F.box_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE ChannelFR() {

			cd0 box_Cross_Conveyor_2.box_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Cross_Transfer_2.box_Cross_Transfer_2_timeline.Cross_2_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Port_F2.box_Port_F2_timeline.Port_F2_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> box_Neighbor_R.box_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> box_Neighbor_F.box_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

			cd8 box_Port_R2.box_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE ChannelFR() {

			cd0 box_Cross_Conveyor_1.box_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 box_Cross_Transfer_1.box_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 box_Cross_Conveyor_3.box_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4 box_Port_R1.box_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 box_Port_F2.box_Port_F2_timeline.Port_F2_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> box_Neighbor_R.box_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> box_Neighbor_F.box_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

	}

}

