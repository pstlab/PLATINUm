DOMAIN GECKO_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 1000], 500;
	PAR_TYPE NumericParameterType partId = [0, 100];
	PAR_TYPE NumericParameterType modulePortId = [1, 8];
	PAR_TYPE EnumerationParameterType transportDirection = {N, S, W, E};
	PAR_TYPE EnumerationParameterType movingDirection = {FORWARD, BACKWARD};
	
	COMP_TYPE SingletonStateVariable TransportationModuleActivityType (Idle(), TransportingPallet(partId, modulePortId, modulePortId))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			TransportingPallet(?pid1, ?from1, ?to1);
		}
		
		VALUE TransportingPallet(?pid, ?from, ?to) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable TransportationUnitStatusType (Idle(), Sending(partId, transportDirection), Receiving(partId, transportDirection), Busy(partId))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Receiving(?pid, ?in);
		}
		
		VALUE Receiving(?pid, ?in) [5, 5]
		MEETS {
			Busy(?pid1);
			?pid1 = ?pid;
		}
		
		VALUE Busy(?pid) [1, +INF]
		MEETS {
			Sending(?pid1, ?out1);
			?pid1 = ?pid;
		}
		
		VALUE Sending(?pid, ?out) [5, 5]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable NeighborPortStatusType (Available(), NotAvailable())
	{
		VALUE Available() [1, +INF]
		MEETS {
			NotAvailable();
		}
		
		VALUE NotAvailable() [1, +INF]
		MEETS {
			Available();
		}
	}
	
	COMP_TYPE SingletonStateVariable TransportationUnitCrossPositionType (Down(), MovingUp(), MovingDown(), Up())
	{
		VALUE Down() [1, +INF]
		MEETS {
			MovingUp();
		}
		
		VALUE MovingUp() [1, 1]
		MEETS {
			Up();
		}
		
		VALUE Up() [1, +INF]
		MEETS {
			MovingDown();
		}
		
		VALUE MovingDown() [1, 1]
		MEETS {
			Down();
		}
	}
	
	COMP_TYPE SingletonStateVariable TransportationModuleEngineType (Off(), On(movingDirection))
	{
		VALUE Off() [1, +INF]
		MEETS {
			On(?mov);
		}
		
		VALUE On(?mov) [1, +INF]
		MEETS {
			Off();
		}
	}
	
	COMPONENT TM_ModuleActivity1 { FLEXIBLE module_activity1(trex_internal_dispatch_asap) } : TransportationModuleActivityType;
	
	COMPONENT TM_Engine { FLEXIBLE engine_main(trex_internal_dispatch_asap) } : TransportationModuleEngineType;
	
	COMPONENT TM_Unit1_Status { FLEXIBLE unit1_status(trex_internal_dispatch_asap) } : TransportationUnitStatusType;
	COMPONENT TM_Unit2_Status { FLEXIBLE unit2_status(trex_internal_dispatch_asap) } : TransportationUnitStatusType;
	COMPONENT TM_Unit3_Status { FLEXIBLE unit3_status(trex_internal_dispatch_asap) } : TransportationUnitStatusType;
	
	COMPONENT TM_Unit1_Cross { FLEXIBLE unit1_cross(trex_internal_dispatch_asap) } : TransportationUnitCrossPositionType;
	COMPONENT TM_Unit2_Cross { FLEXIBLE unit2_cross(trex_internal_dispatch_asap) } : TransportationUnitCrossPositionType;
	COMPONENT TM_Unit3_Cross { FLEXIBLE unit3_cross(trex_internal_dispatch_asap) } : TransportationUnitCrossPositionType;
	
	COMPONENT TM_Unit1_Engine { FLEXIBLE unit1_engine(trex_internal_dispatch_asap) } : TransportationModuleEngineType;
	COMPONENT TM_Unit2_Engine { FLEXIBLE unit2_engine(trex_internal_dispatch_asap) } : TransportationModuleEngineType;
	COMPONENT TM_Unit3_Engine { FLEXIBLE unit3_engine(trex_internal_dispatch_asap) } : TransportationModuleEngineType;
	
	COMPONENT TM_NeighborPort1 { FLEXIBLE neighbor_port1(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort2 { FLEXIBLE neighbor_port2(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort3 { FLEXIBLE neighbor_port3(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort4 { FLEXIBLE neighbor_port4(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort5 { FLEXIBLE neighbor_port5(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort6 { FLEXIBLE neighbor_port6(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort7 { FLEXIBLE neighbor_port7(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	COMPONENT TM_NeighborPort8 { FLEXIBLE neighbor_port8(trex_internal_dispatch_asap)} : NeighborPortStatusType;
	
	SYNCHRONIZE TM_ModuleActivity1.module_activity1
	{
		// TRANSPORT <FROM=1, TO=8>
		VALUE TransportingPallet(?pid, ?from, ?to)
		{
			cd0 <!> TM_Unit1_Status.unit1_status.Receiving(?pid0, ?in0);
			cd1 <!> TM_Unit1_Status.unit1_status.Sending(?pid1, ?out1);
			
			cd2 <!> TM_Unit2_Status.unit2_status.Receiving(?pid2, ?in2);
			cd3 <!> TM_Unit2_Status.unit2_status.Sending(?pid3, ?out3);
		
			cd4 <!> TM_Unit3_Status.unit3_status.Receiving(?pid4, ?in4);
			cd5 <!> TM_Unit3_Status.unit3_status.Sending(?pid5, ?out5);
			
			cd6 TM_Engine.engine_main.On(?mov6);
			cd7 TM_Engine.engine_main.On(?mov7);
			cd8 TM_Engine.engine_main.On(?mov8);
			cd9 TM_Engine.engine_main.On(?mov9);
			
			cd10 TM_Unit1_Cross.unit1_cross.Down();
			cd11 TM_Unit2_Cross.unit2_cross.Down();
			cd12 TM_Unit3_Cross.unit3_cross.Down();
			
			cd13 <?> TM_NeighborPort1.neighbor_port1.Available();
			cd14 <?> TM_NeighborPort8.neighbor_port8.Available();
			
			cd0 DURING [0, +INF] [0, +INF] cd13;
			cd5 DURING [0, +INF]Ê[0, +INF] cd14;
			
			cd0 DURING [0, +INF]Ê[0, +INF] cd10;
			cd1 DURING [0, +INF]Ê[0, +INF] cd10;
			
			cd2 DURING [0, +INF]Ê[0, +INF] cd11;
			cd3 DURING [0, +INF]Ê[0, +INF] cd11;
			
			cd4 DURING [0, +INF]Ê[0, +INF] cd12;
			cd5 DURING [0, +INF]Ê[0, +INF] cd12;
			
			START-START [0, 0] cd0;
			CONTAINS [0, +INF]Ê[0, +INF] cd1;
			CONTAINS [0, +INF]Ê[0, +INF] cd3;
			END-END [0, 0] cd5; 
			
			cd0 BEFORE [0, +INF] cd1;
			cd2 BEFORE [0, +INF] cd3;
			cd4 BEFORE [0, +INF] cd5;
			
			cd1 EQUALS cd2;
			cd3 EQUALS cd4;
			
			cd0 DURING [0, +INF] [0, +INF] cd6;
			cd1 DURING [0, +INF]Ê[0, +INF]Êcd7;
			cd2 DURING [0, +INF]Ê[0, +INF]Êcd7;
			cd3 DURING [0, +INF]Ê[0, +INF] cd8;
			cd4 DURING [0, +INF]Ê[0, +INF] cd8;
			cd5 DURING [0, +INF]Ê[0, +INF]Êcd9;
			
			?pid0 = ?pid;
			?pid1 = ?pid;
			?pid2 = ?pid;
			?pid3 = ?pid;
			?pid4 = ?pid;
			?pid5 = ?pid;
			
			?in0 = N; ?out1 = S;
			?in2 = N; ?out3 = S;
			?in4 = N; ?out5 = S;
			
			?mov6 = BACKWARD;
			?mov7 = BACKWARD;
			?mov8 = BACKWARD;
			?mov9 = BACKWARD;
			
			?from = 1;
			?to = 8;
		}
	}
}