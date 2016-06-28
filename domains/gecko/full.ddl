DOMAIN full_Domain {

	TEMPORAL_MODULE temporal_module = [0, 1000], 500;

	COMP_TYPE SingletonStateVariable full_ChannelType (Channel_F_R2(), Channel_L2_F(), Channel_L1_B(), Channel_F_R1(), Channel_F_R3(), Idle(), Channel_F_B(), Channel_L3_L3(), Channel_L2_B(), Channel_L3_L2(), Channel_L1_F(), Channel_L3_L1(), Channel_F_F(), Channel_L3_R1(), Channel_L3_R2(), Channel_L3_R3(), Channel_R3_L3(), Channel_R3_L2(), Channel_R1_L1(), Channel_R1_L2(), Channel_R3_L1(), Channel_R1_L3(), Channel_R2_R2(), Channel_R2_R1(), Channel_R2_B(), Channel_L1_R2(), Channel_L1_R3(), Channel_R2_R3(), Channel_L1_R1(), Channel_B_F(), Channel_L2_L3(), Channel_L2_L2(), Channel_L2_L1(), Channel_R1_B(), Channel_B_B(), Channel_L1_L2(), Channel_L1_L3(), Channel_L1_L1(), Channel_R2_F(), Channel_B_R3(), Channel_B_R1(), Channel_B_R2(), Channel_R3_F(), Channel_R3_B(), Channel_F_L3(), Channel_F_L2(), Channel_F_L1(), Channel_B_L3(), Channel_R1_F(), Channel_L3_B(), Channel_B_L1(), Channel_B_L2(), Channel_R2_L1(), Channel_R2_L2(), Channel_R2_L3(), Channel_R1_R1(), Channel_R1_R3(), Channel_L3_F(), Channel_R1_R2(), Channel_L2_R3(), Channel_L2_R2(), Channel_L2_R1(), Channel_R3_R3(), Channel_R3_R2(), Channel_R3_R1()) {

		VALUE Channel_F_R2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_F() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_B() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_F_R1() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_F_R3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Idle() [1, 1000]
		MEETS {
			Channel_F_R2();
			Channel_L2_F();
			Channel_L1_B();
			Channel_F_R1();
			Channel_F_R3();
			Channel_F_B();
			Channel_L3_L3();
			Channel_L2_B();
			Channel_L3_L2();
			Channel_L1_F();
			Channel_L3_L1();
			Channel_F_F();
			Channel_L3_R1();
			Channel_L3_R2();
			Channel_L3_R3();
			Channel_R3_L3();
			Channel_R3_L2();
			Channel_R1_L1();
			Channel_R1_L2();
			Channel_R3_L1();
			Channel_R1_L3();
			Channel_R2_R2();
			Channel_R2_R1();
			Channel_R2_B();
			Channel_L1_R2();
			Channel_L1_R3();
			Channel_R2_R3();
			Channel_L1_R1();
			Channel_B_F();
			Channel_L2_L3();
			Channel_L2_L2();
			Channel_L2_L1();
			Channel_B_B();
			Channel_R1_B();
			Channel_L1_L2();
			Channel_L1_L3();
			Channel_L1_L1();
			Channel_R2_F();
			Channel_B_R3();
			Channel_B_R1();
			Channel_B_R2();
			Channel_R3_F();
			Channel_R3_B();
			Channel_F_L3();
			Channel_F_L2();
			Channel_F_L1();
			Channel_B_L3();
			Channel_R1_F();
			Channel_L3_B();
			Channel_B_L1();
			Channel_B_L2();
			Channel_R2_L1();
			Channel_R2_L2();
			Channel_R2_L3();
			Channel_R1_R1();
			Channel_R1_R3();
			Channel_L3_F();
			Channel_R1_R2();
			Channel_L2_R3();
			Channel_L2_R2();
			Channel_L2_R1();
			Channel_R3_R3();
			Channel_R3_R2();
			Channel_R3_R1();
		}

		VALUE Channel_F_B() [30, 60]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_L3() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_B() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_F() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_L1() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_F_F() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_R1() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_R2() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_R3() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_L3() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_L1() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_L1() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_L3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_R2() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_R1() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_B() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_R2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_R3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_R3() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_R1() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_B_F() [30, 60]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_L3() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_L2() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_L1() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_B() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_B() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_L3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_L1_L1() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_F() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_B_R3() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_R1() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_R2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_F() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_B() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_F_L3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_F_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_F_L1() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_L3() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_F() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_B() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_L1() [10, 25]
		MEETS {
			Idle();
		}

		VALUE Channel_B_L2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_L1() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_L2() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R2_L3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_R1() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_R3() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_L3_F() [40, 55]
		MEETS {
			Idle();
		}

		VALUE Channel_R1_R2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_R3() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_R2() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_L2_R1() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_R3() [10, 20]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_R2() [20, 35]
		MEETS {
			Idle();
		}

		VALUE Channel_R3_R1() [40, 55]
		MEETS {
			Idle();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Conveyor_2Type (Cross_Conveyor_2_Still(), Cross_Conveyor_2_Backward(), Cross_Conveyor_2_Forward()) {

		VALUE Cross_Conveyor_2_Still() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Backward();
			Cross_Conveyor_2_Forward();
		}

		VALUE Cross_Conveyor_2_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Still();
		}

		VALUE Cross_Conveyor_2_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_2_Still();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_R1Type (Port_R1_Available(), Port_R1_Not_Available()) {

		VALUE Port_R1_Available() [1, 1000]
		MEETS {
			Port_R1_Not_Available();
		}

		VALUE Port_R1_Not_Available() [1, 1000]
		MEETS {
			Port_R1_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_L1Type (Port_L1_Available(), Port_L1_Not_Available()) {

		VALUE Port_L1_Available() [1, 1000]
		MEETS {
			Port_L1_Not_Available();
		}

		VALUE Port_L1_Not_Available() [1, 1000]
		MEETS {
			Port_L1_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Transfer_2Type (Cross_2_Changing(), Cross_2_Up(), Cross_2_Down()) {

		VALUE Cross_2_Changing() [3, 3]
		MEETS {
			Cross_2_Up();
			Cross_2_Down();
		}

		VALUE Cross_2_Up() [1, 1000]
		MEETS {
			Cross_2_Changing();
		}

		VALUE Cross_2_Down() [1, 1000]
		MEETS {
			Cross_2_Changing();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Conveyor_1Type (Cross_Conveyor_1_Still(), Cross_Conveyor_1_Forward(), Cross_Conveyor_1_Backward()) {

		VALUE Cross_Conveyor_1_Still() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Forward();
			Cross_Conveyor_1_Backward();
		}

		VALUE Cross_Conveyor_1_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Still();
		}

		VALUE Cross_Conveyor_1_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_1_Still();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Transfer_1Type (Cross_1_Up(), Cross_1_Down(), Cross_1_Changing()) {

		VALUE Cross_1_Up() [1, 1000]
		MEETS {
			Cross_1_Changing();
		}

		VALUE Cross_1_Down() [1, 1000]
		MEETS {
			Cross_1_Changing();
		}

		VALUE Cross_1_Changing() [3, 3]
		MEETS {
			Cross_1_Up();
			Cross_1_Down();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_BType (Port_B_Not_Available(), Port_B_Available()) {

		VALUE Port_B_Not_Available() [1, 1000]
		MEETS {
			Port_B_Available();
		}

		VALUE Port_B_Available() [1, 1000]
		MEETS {
			Port_B_Not_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_FType (Port_F_Not_Available(), Port_F_Available()) {

		VALUE Port_F_Not_Available() [1, 1000]
		MEETS {
			Port_F_Available();
		}

		VALUE Port_F_Available() [1, 1000]
		MEETS {
			Port_F_Not_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Main_ConveyorType (Main_Conveyor_Forward(), Main_Conveyor_Backward(), Main_Conveyor_Still()) {

		VALUE Main_Conveyor_Forward() [1, 1000]
		MEETS {
			Main_Conveyor_Still();
		}

		VALUE Main_Conveyor_Backward() [1, 1000]
		MEETS {
			Main_Conveyor_Still();
		}

		VALUE Main_Conveyor_Still() [1, 1000]
		MEETS {
			Main_Conveyor_Forward();
			Main_Conveyor_Backward();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_R3Type (Port_R3_Available(), Port_R3_Not_Available()) {

		VALUE Port_R3_Available() [1, 1000]
		MEETS {
			Port_R3_Not_Available();
		}

		VALUE Port_R3_Not_Available() [1, 1000]
		MEETS {
			Port_R3_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Conveyor_3Type (Cross_Conveyor_3_Forward(), Cross_Conveyor_3_Still(), Cross_Conveyor_3_Backward()) {

		VALUE Cross_Conveyor_3_Forward() [1, 1000]
		MEETS {
			Cross_Conveyor_3_Still();
		}

		VALUE Cross_Conveyor_3_Still() [1, 1000]
		MEETS {
			Cross_Conveyor_3_Forward();
			Cross_Conveyor_3_Backward();
		}

		VALUE Cross_Conveyor_3_Backward() [1, 1000]
		MEETS {
			Cross_Conveyor_3_Still();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_R2Type (Port_R2_Available(), Port_R2_Not_Available()) {

		VALUE Port_R2_Available() [1, 1000]
		MEETS {
			Port_R2_Not_Available();
		}

		VALUE Port_R2_Not_Available() [1, 1000]
		MEETS {
			Port_R2_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_L2Type (Port_L2_Available(), Port_L2_Not_Available()) {

		VALUE Port_L2_Available() [1, 1000]
		MEETS {
			Port_L2_Not_Available();
		}

		VALUE Port_L2_Not_Available() [1, 1000]
		MEETS {
			Port_L2_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Port_L3Type (Port_L3_Available(), Port_L3_Not_Available()) {

		VALUE Port_L3_Available() [1, 1000]
		MEETS {
			Port_L3_Not_Available();
		}

		VALUE Port_L3_Not_Available() [1, 1000]
		MEETS {
			Port_L3_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Cross_Transfer_3Type (Cross_3_Changing(), Cross_3_Down(), Cross_3_Up()) {

		VALUE Cross_3_Changing() [3, 3]
		MEETS {
			Cross_3_Down();
			Cross_3_Up();
		}

		VALUE Cross_3_Down() [1, 1000]
		MEETS {
			Cross_3_Changing();
		}

		VALUE Cross_3_Up() [1, 1000]
		MEETS {
			Cross_3_Changing();
		}

	}

	COMP_TYPE SingletonStateVariable full_Neighbor_FType (Neighbor_F_not_Available(), Neighbor_F_Available()) {

		VALUE Neighbor_F_not_Available() [1, 1000]
		MEETS {
			Neighbor_F_Available();
		}

		VALUE Neighbor_F_Available() [1, 1000]
		MEETS {
			Neighbor_F_not_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Neighbor_RType (Neighbor_R_Available(), Neighbor_R_not_Available()) {

		VALUE Neighbor_R_Available() [1, 1000]
		MEETS {
			Neighbor_R_not_Available();
		}

		VALUE Neighbor_R_not_Available() [1, 1000]
		MEETS {
			Neighbor_R_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Neighbor_BType (Neighbor_B_Available(), Neighbor_B_not_Available()) {

		VALUE Neighbor_B_Available() [1, 1000]
		MEETS {
			Neighbor_B_not_Available();
		}

		VALUE Neighbor_B_not_Available() [1, 1000]
		MEETS {
			Neighbor_B_Available();
		}

	}

	COMP_TYPE SingletonStateVariable full_Neighbor_LType (Neighbor_L_Available(), Neighbor_L_not_Available()) {

		VALUE Neighbor_L_Available() [1, 1000]
		MEETS {
			Neighbor_L_not_Available();
		}

		VALUE Neighbor_L_not_Available() [1, 1000]
		MEETS {
			Neighbor_L_Available();
		}

	}

	COMPONENT full_Channel { FLEXIBLE full_Channel_timeline(trex_internal_dispatch_asap) } : full_ChannelType;
	COMPONENT full_Cross_Conveyor_2 { FLEXIBLE full_Cross_Conveyor_2_timeline(trex_internal_dispatch_asap) } : full_Cross_Conveyor_2Type;
	COMPONENT full_Port_R1 { FLEXIBLE full_Port_R1_timeline(trex_internal_dispatch_asap) } : full_Port_R1Type;
	COMPONENT full_Port_L1 { FLEXIBLE full_Port_L1_timeline(trex_internal_dispatch_asap) } : full_Port_L1Type;
	COMPONENT full_Cross_Transfer_2 { FLEXIBLE full_Cross_Transfer_2_timeline(trex_internal_dispatch_asap) } : full_Cross_Transfer_2Type;
	COMPONENT full_Cross_Conveyor_1 { FLEXIBLE full_Cross_Conveyor_1_timeline(trex_internal_dispatch_asap) } : full_Cross_Conveyor_1Type;
	COMPONENT full_Cross_Transfer_1 { FLEXIBLE full_Cross_Transfer_1_timeline(trex_internal_dispatch_asap) } : full_Cross_Transfer_1Type;
	COMPONENT full_Port_B { FLEXIBLE full_Port_B_timeline(trex_internal_dispatch_asap) } : full_Port_BType;
	COMPONENT full_Port_F { FLEXIBLE full_Port_F_timeline(trex_internal_dispatch_asap) } : full_Port_FType;
	COMPONENT full_Main_Conveyor { FLEXIBLE full_Main_Conveyor_timeline(trex_internal_dispatch_asap) } : full_Main_ConveyorType;
	COMPONENT full_Port_R3 { FLEXIBLE full_Port_R3_timeline(trex_internal_dispatch_asap) } : full_Port_R3Type;
	COMPONENT full_Cross_Conveyor_3 { FLEXIBLE full_Cross_Conveyor_3_timeline(trex_internal_dispatch_asap) } : full_Cross_Conveyor_3Type;
	COMPONENT full_Port_R2 { FLEXIBLE full_Port_R2_timeline(trex_internal_dispatch_asap) } : full_Port_R2Type;
	COMPONENT full_Port_L2 { FLEXIBLE full_Port_L2_timeline(trex_internal_dispatch_asap) } : full_Port_L2Type;
	COMPONENT full_Port_L3 { FLEXIBLE full_Port_L3_timeline(trex_internal_dispatch_asap) } : full_Port_L3Type;
	COMPONENT full_Cross_Transfer_3 { FLEXIBLE full_Cross_Transfer_3_timeline(trex_internal_dispatch_asap) } : full_Cross_Transfer_3Type;
	COMPONENT full_Neighbor_F { FLEXIBLE full_Neighbor_F_timeline(uncontrollable) } : full_Neighbor_FType;
	COMPONENT full_Neighbor_R { FLEXIBLE full_Neighbor_R_timeline(uncontrollable) } : full_Neighbor_RType;
	COMPONENT full_Neighbor_B { FLEXIBLE full_Neighbor_B_timeline(uncontrollable) } : full_Neighbor_BType;
	COMPONENT full_Neighbor_L { FLEXIBLE full_Neighbor_L_timeline(uncontrollable) } : full_Neighbor_LType;

	SYNCHRONIZE full_Channel.full_Channel_timeline {

		VALUE Channel_R1_R2() {

			cd0 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_B_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_B.full_Port_B_timeline.Port_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_L2_R2() {

			cd0 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_R3_L2() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L2_L2() {

			cd0 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_L2_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_R1_R3() {

			cd0 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd8;

			cd9  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L3_L2() {

			cd0 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_L2_F() {

			cd0 full_Port_F.full_Port_F_timeline.Port_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_B_R2() {

			cd0  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_B.full_Port_B_timeline.Port_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R3_F() {

			cd0  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_F.full_Port_F_timeline.Port_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_L3_R2() {

			cd0  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_B_R3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_B.full_Port_B_timeline.Port_B_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_F_B() {

			cd0 full_Port_B.full_Port_B_timeline.Port_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_F.full_Port_F_timeline.Port_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_R3_R1() {

			cd0 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L1_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R2_B() {

			cd0 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_B.full_Port_B_timeline.Port_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R1_F() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_F.full_Port_F_timeline.Port_F_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_R3_R2() {

			cd0 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_F_R2() {

			cd0  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_F.full_Port_F_timeline.Port_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R1_B() {

			cd0  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_B.full_Port_B_timeline.Port_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_L3_L1() {

			cd0 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd8;

			cd9  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_F_R3() {

			cd0 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_F.full_Port_F_timeline.Port_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_B_B() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_B.full_Port_B_timeline.Port_B_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_R3_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_L1_F() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_F.full_Port_F_timeline.Port_F_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_R2_L3() {

			cd0 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_F_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_F.full_Port_F_timeline.Port_F_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_L3_B() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_B.full_Port_B_timeline.Port_B_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_L3_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_R3_L1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_F_L1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_F.full_Port_F_timeline.Port_F_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_L2_R3() {

			cd0 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R2_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_L3_R3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_L2_B() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_B.full_Port_B_timeline.Port_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R3_B() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_B.full_Port_B_timeline.Port_B_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_R2_L1() {

			cd0 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R3_R3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_L2_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L3_R1() {

			cd0 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_F_L2() {

			cd0  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_F.full_Port_F_timeline.Port_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L1_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_L2_L1() {

			cd0 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_L3_F() {

			cd0 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_F.full_Port_F_timeline.Port_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_B_L2() {

			cd0 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_B.full_Port_B_timeline.Port_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_F_L3() {

			cd0  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_F.full_Port_F_timeline.Port_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_L1_R2() {

			cd0 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_B_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_B.full_Port_B_timeline.Port_B_Available();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_L1_B() {

			cd0  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_B.full_Port_B_timeline.Port_B_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_F_F() {

			cd0 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_F.full_Port_F_timeline.Port_F_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_R1_L3() {

			cd0 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_L3.full_Port_L3_timeline.Port_L3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_L1_L1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_R2_F() {

			cd0 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_F.full_Port_F_timeline.Port_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_L1_L2() {

			cd0 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_R1_L1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_L1_R3() {

			cd0 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

		VALUE Channel_R2_R2() {

			cd0 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_B_F() {

			cd0 full_Port_F.full_Port_F_timeline.Port_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_F.full_Neighbor_F_timeline.Neighbor_F_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_B.full_Port_B_timeline.Port_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd2;

			cd3  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			DURING [0,+INF] [0,+INF]  cd7;

		}

		VALUE Channel_R1_L2() {

			cd0 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

		}

		VALUE Channel_R1_R1() {

			cd0 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_R1.full_Port_R1_timeline.Port_R1_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

		}

		VALUE Channel_R2_R3() {

			cd0 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Port_R3.full_Port_R3_timeline.Port_R3_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Cross_Conveyor_3.full_Cross_Conveyor_3_timeline.Cross_Conveyor_3_Forward();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Backward();
			STARTS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			CONTAINS [0,+INF] [0,+INF]  cd6;

			cd7 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Backward();
			CONTAINS [0,+INF] [0,+INF]  cd7;

			cd8  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd8;

		}

		VALUE Channel_R2_L2() {

			cd0 full_Cross_Conveyor_2.full_Cross_Conveyor_2_timeline.Cross_Conveyor_2_Forward();
			DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Up();
			DURING [0,+INF] [0,+INF]  cd1;

			cd2 full_Port_R2.full_Port_R2_timeline.Port_R2_Available();
			DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L2.full_Port_L2_timeline.Port_L2_Available();
			DURING [0,+INF] [0,+INF]  cd3;

			cd4  <?> full_Neighbor_R.full_Neighbor_R_timeline.Neighbor_R_Available();
			DURING [0,+INF] [0,+INF]  cd4;

			cd5  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			DURING [0,+INF] [0,+INF]  cd5;

		}

		VALUE Channel_B_L1() {

			cd0 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Up();
			ENDS-DURING [0,+INF] [0,+INF]  cd0;

			cd1 full_Cross_Conveyor_1.full_Cross_Conveyor_1_timeline.Cross_Conveyor_1_Backward();
			ENDS-DURING [0,+INF] [0,+INF]  cd1;

			cd2  <?> full_Neighbor_L.full_Neighbor_L_timeline.Neighbor_L_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd2;

			cd3 full_Port_L1.full_Port_L1_timeline.Port_L1_Available();
			ENDS-DURING [0,+INF] [0,+INF]  cd3;

			cd4 full_Cross_Transfer_3.full_Cross_Transfer_3_timeline.Cross_3_Down();
			STARTS-DURING [0,+INF] [0,+INF]  cd4;

			cd5 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			STARTS-DURING [0,+INF] [0,+INF]  cd5;

			cd6  <?> full_Neighbor_B.full_Neighbor_B_timeline.Neighbor_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd6;

			cd7 full_Port_B.full_Port_B_timeline.Port_B_Available();
			STARTS-DURING [0,+INF] [0,+INF]  cd7;

			cd8 full_Main_Conveyor.full_Main_Conveyor_timeline.Main_Conveyor_Forward();
			CONTAINS [0,+INF] [0,+INF]  cd8;

			cd9 full_Cross_Transfer_1.full_Cross_Transfer_1_timeline.Cross_1_Down();
			CONTAINS [0,+INF] [0,+INF]  cd9;

			cd10 full_Cross_Transfer_2.full_Cross_Transfer_2_timeline.Cross_2_Down();
			DURING [0,+INF] [0,+INF]  cd10;

		}

	}

}

