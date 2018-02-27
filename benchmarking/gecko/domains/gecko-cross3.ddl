DOMAIN GeckoDomain
{
	TEMPORAL_MODULE temporal_module = [0, 30], 500;
	
	COMP_TYPE SingletonStateVariable ConveyorController(Still(), MovingForward(), MovingBackward()) {
		
		VALUE Still() [1, +INF] 
		MEETS {
			MovingForward();
			MovingBackward();
		}
		
		VALUE MovingForward() [1, +INF]
		MEETS {
			Still();
		}
		
		VALUE MovingBackward() [1, +INF]
		MEETS {
			Still();
		}
	}
	
	COMP_TYPE SingletonStateVariable CrossTransferController(Down(), Up(), Moving()) {
	
		VALUE Moving() [3, 3]
		MEETS {
			Up();
			Down();
		}
		
		VALUE Up() [1, +INF]
		MEETS {
			Moving();
		}
		
		VALUE Down() [1, +INF]
		MEETS {
			Moving();
		}
	}
	
	COMP_TYPE SingletonStateVariable TMController(Idle(), ChannelR1L1(), ChannelR2L2(), ChannelR3L3()) {
	
		VALUE Idle() [1, +INF]
		MEETS {
			ChannelR1L1();
			ChannelR2L2();
			ChannelR3L3();
		}
		
		VALUE ChannelR1L1() [1, 10]
		MEETS {
			Idle();
		}
		
		VALUE ChannelR2L2() [1, 10]
		MEETS {
			Idle();
		}
		
		VALUE ChannelR3L3() [1, 10]
		MEETS {
			Idle();
		}
	}
	
	COMPONENT TM {FLEXIBLE module(trex_internal_dispatch_asap)} : TMController;
	COMPONENT TMConveyor {FLEXIBLE conveyor(trex_internal_dispatch_asap)} : ConveyorController;
	
	COMPONENT Cross1 {FLEXIBLE cross1(trex_internal_dispatch_asap)} : CrossTransferController;
	COMPONENT Conveyor1 {FLEXIBLE conveyor1(trex_internal_dispatch_asap)} : ConveyorController;
	
	COMPONENT Cross2 {FLEXIBLE cross2(trex_internal_dispatch_asap)} : CrossTransferController;
	COMPONENT Conveyor2 {FLEXIBLE conveyor2(trex_internal_dispatch_asap)} : ConveyorController;
	
	COMPONENT Cross3 {FLEXIBLE cross3(trex_internal_dispatch_asap)} : CrossTransferController;
	COMPONENT Conveyor3 {FLEXIBLE conveyor3(trex_internal_dispatch_asap)} : ConveyorController;
	
	
	SYNCHRONIZE TM.module {
	
		VALUE ChannelR1L1() {
		
			cd0 Cross1.cross1.Up();
			cd1 Conveyor1.conveyor1.MovingForward();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;	
		}
		
		VALUE ChannelR2L2() {
		
			cd0 Cross2.cross2.Up();
			cd1 Conveyor2.conveyor2.MovingForward();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;	
		}
		
		VALUE ChannelR3L3() {
		
			cd0 Cross3.cross3.Up();
			cd1 Conveyor3.conveyor3.MovingForward();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;	
		}
	}
}