DOMAIN SATELLITE_Domain
{
	TEMPORAL_MODULE temporal_module = [0, 700], 500;
	
	COMP_TYPE SingletonStateVariable SatelliteOperationModeType (Earth(), Slewing(), Science(), Comm())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slewing();
			Comm();
		}
		
		VALUE Slewing() [10, 10]
		MEETS {
			Earth();
			Science();
		}
		
		VALUE Science() [16, +INF]
		MEETS {
			Slewing();
		}
		
		VALUE Comm() [15, 45]
		MEETS {
			Slewing();
		}
	}
	
	COMP_TYPE SingletonStateVariable InstrumentType (Idle(), Warmup(), Turnoff(), Process())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Warmup();
		}
		
		VALUE Warmup() [3, 3]
		MEETS {
			Process();
		}
		
		VALUE Process() [10, 40]
		MEETS {
			Turnoff();
		}
		
		VALUE Turnoff() [3, 3]
		MEETS {
			Idle();
		}

	}
	
	COMP_TYPE SingletonStateVariable GroundStationVisibilityWindowType (Available(), NotAvailable())
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
	
	COMPONENT Satellite_Operation {FLEXIBLE op_trace(trex_internal_dispatch_asap)} : SatelliteOperationModeType;
	COMPONENT Satellite_Instrument_1 {FLEXIBLE inst1(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_2 {FLEXIBLE inst2(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_3 {FLEXIBLE inst3(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_4 {FLEXIBLE inst4(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_5 {FLEXIBLE inst5(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_6 {FLEXIBLE inst6(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_7 {FLEXIBLE inst7(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT Satellite_Instrument_8 {FLEXIBLE inst8(trex_internal_dispatch_asap)} : InstrumentType;
	COMPONENT GraoundStationVisibility {FLEXIBLE window(uncontrollable)} : GroundStationVisibilityWindowType;
	
	SYNCHRONIZE Satellite_Operation.op_trace
	{
		VALUE Science()
		{
			cd0 <!> Satellite_Instrument_1.inst1.Process();
			cd1 Satellite_Operation.op_trace.Comm();
			cd2 <?> GraoundStationVisibility.window.Available();
			cd3 <!> Satellite_Instrument_2.inst2.Process();
			cd4 <!> Satellite_Instrument_3.inst3.Process();
			cd5 <!> Satellite_Instrument_4.inst4.Process();
			cd6 <!> Satellite_Instrument_5.inst5.Process();
			cd7 <!> Satellite_Instrument_6.inst6.Process();
			cd8 <!> Satellite_Instrument_7.inst7.Process();
			cd9 <!> Satellite_Instrument_8.inst8.Process();
			cd10 <!> Satellite_Instrument_8.inst8.Idle();
			
			cd11 Satellite_Instrument_1.inst1.Idle();
			cd121 Satellite_Instrument_2.inst2.Idle();
			cd122 Satellite_Instrument_2.inst2.Idle();
			cd131 Satellite_Instrument_3.inst3.Idle();
			cd132 Satellite_Instrument_3.inst3.Idle();
			cd141 Satellite_Instrument_4.inst4.Idle();
			cd142 Satellite_Instrument_4.inst4.Idle();
			cd151 Satellite_Instrument_5.inst5.Idle();
			cd152 Satellite_Instrument_5.inst5.Idle();
			cd161 Satellite_Instrument_6.inst6.Idle();
			cd162 Satellite_Instrument_6.inst6.Idle();
			cd171 Satellite_Instrument_7.inst7.Idle();
			cd172 Satellite_Instrument_7.inst7.Idle();
			cd18 Satellite_Instrument_8.inst8.Idle();
			
			CONTAINS [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd3;
			CONTAINS [0, +INF] [0, +INF] cd4;
			CONTAINS [0, +INF] [0, +INF] cd5;
			CONTAINS [0, +INF] [0, +INF] cd6;
			CONTAINS [0, +INF] [0, +INF] cd7;
			CONTAINS [0, +INF] [0, +INF] cd8;
			CONTAINS [0, +INF] [0, +INF] cd9; 
			BEFORE [0, +INF] cd1;
			
			cd1 DURING [0, +INF] [0, +INF] cd2;
			cd0 BEFORE [3, +INF] cd3;
			cd3 BEFORE [3, +INF] cd4;
			cd4 BEFORE [3, +INF] cd5;
			cd5 BEFORE [3, +INF] cd6;
			cd6 BEFORE [3, +INF] cd7;
			cd7 BEFORE [3, +INF] cd8;
			cd8 BEFORE [3, +INF] cd9;
			cd9 BEFORE [3, +INF] cd10;
			
			cd0 DURING [0, +INF] [0, +INF] cd121;
			cd0 DURING [0, +INF] [0, +INF] cd131;
			cd0 DURING [0, +INF] [0, +INF] cd141;
			cd0 DURING [0, +INF] [0, +INF] cd151;
			cd0 DURING [0, +INF] [0, +INF] cd161;
			cd0 DURING [0, +INF] [0, +INF] cd171;
			cd0 DURING [0, +INF] [0, +INF] cd18;
			
			cd3 DURING [0, +INF] [0, +INF] cd11;
			cd3 DURING [0, +INF] [0, +INF] cd131;
			cd3 DURING [0, +INF] [0, +INF] cd141;
			cd3 DURING [0, +INF] [0, +INF] cd151;
			cd3 DURING [0, +INF] [0, +INF] cd161;
			cd3 DURING [0, +INF] [0, +INF] cd171;
			cd3 DURING [0, +INF] [0, +INF] cd18;
			
			cd4 DURING [0, +INF] [0, +INF] cd11;
			cd4 DURING [0, +INF] [0, +INF] cd122;
			cd4 DURING [0, +INF] [0, +INF] cd141;
			cd4 DURING [0, +INF] [0, +INF] cd151;
			cd4 DURING [0, +INF] [0, +INF] cd161;
			cd4 DURING [0, +INF] [0, +INF] cd171;
			cd4 DURING [0, +INF] [0, +INF] cd18;
			
			cd5 DURING [0, +INF] [0, +INF] cd11;
			cd5 DURING [0, +INF] [0, +INF] cd122;
			cd5 DURING [0, +INF] [0, +INF] cd132;
			cd5 DURING [0, +INF] [0, +INF] cd151;
			cd5 DURING [0, +INF] [0, +INF] cd161;
			cd5 DURING [0, +INF] [0, +INF] cd171;
			cd5 DURING [0, +INF] [0, +INF] cd18;
			
			cd6 DURING [0, +INF] [0, +INF] cd11;
			cd6 DURING [0, +INF] [0, +INF] cd122;
			cd6 DURING [0, +INF] [0, +INF] cd132;
			cd6 DURING [0, +INF] [0, +INF] cd142;
			cd6 DURING [0, +INF] [0, +INF] cd161;
			cd6 DURING [0, +INF] [0, +INF] cd171;
			cd6 DURING [0, +INF] [0, +INF] cd18;
			
			cd7 DURING [0, +INF] [0, +INF] cd11;
			cd7 DURING [0, +INF] [0, +INF] cd122;
			cd7 DURING [0, +INF] [0, +INF] cd132;
			cd7 DURING [0, +INF] [0, +INF] cd142;
			cd7 DURING [0, +INF] [0, +INF] cd152;
			cd7 DURING [0, +INF] [0, +INF] cd171;
			cd7 DURING [0, +INF] [0, +INF] cd18;
			
			cd8 DURING [0, +INF] [0, +INF] cd11;
			cd8 DURING [0, +INF] [0, +INF] cd122;
			cd8 DURING [0, +INF] [0, +INF] cd132;
			cd8 DURING [0, +INF] [0, +INF] cd142;
			cd8 DURING [0, +INF] [0, +INF] cd152;
			cd8 DURING [0, +INF] [0, +INF] cd162;
			cd8 DURING [0, +INF] [0, +INF] cd18;
			
			cd9 DURING [0, +INF] [0, +INF] cd11;
			cd9 DURING [0, +INF] [0, +INF] cd122;
			cd9 DURING [0, +INF] [0, +INF] cd132;
			cd9 DURING [0, +INF] [0, +INF] cd142;
			cd9 DURING [0, +INF] [0, +INF] cd152;
			cd9 DURING [0, +INF] [0, +INF] cd162;
			cd9 DURING [0, +INF] [0, +INF] cd172;
		}
	}
}