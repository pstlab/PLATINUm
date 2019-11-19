DOMAIN RSA_H100_U30_W1_I3
{
	TEMPORAL_MODULE temporal_module = [0, 100], 100;
	
	COMP_TYPE SingletonStateVariable RSAOperations (Idle(), Scien(), _Comm(), Maint())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Scien();
			_Comm();
			Maint();
		}
		
		VALUE Scien() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE _Comm() [5, 35]
		MEETS {
			Idle();
		}
		
		VALUE Maint() [3, 5]
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable RSAOrbit(APO(), INT(), PERI())
	{
		VALUE APO() [30, 30]
		MEETS {
			INT();
		}
		
		VALUE INT() [40, 40]
		MEETS {
			APO();
			PERI();		
		}
		
		VALUE PERI() [30, 40]
		MEETS {
			INT();
		}
	}
	
	COMP_TYPE SingletonStateVariable RSAPointingMode (Earth(), Slew(), Planet())
	{
		VALUE Earth() [1, +INF]
		MEETS {
			Slew();
		}
		
		VALUE Slew() [5, 5]
		MEETS {
			Earth();
			Planet();
		}
		
		VALUE Planet() [1, +INF]
		MEETS {
			Slew();
		}
	}
	
	COMP_TYPE SingletonStateVariable RSAInstrument (Off(), WarmUp(), Process(), TurnOff())
	{
		VALUE Off() [1, +INF]
		MEETS {
			WarmUp();
		}
		
		VALUE WarmUp() [3, 3]
		MEETS {
			Process();
		}
		
		VALUE Process() [3, 8]
		MEETS {
			TurnOff();
		}
		
		VALUE TurnOff() [1, 1]
		MEETS {
			Off();
		}
	}
	
	COMP_TYPE SingletonStateVariable RSAInstrumentLock(None(), Inst1(), Inst2(), Inst3())
	{
		VALUE None() [1, +INF]
		MEETS {
			Inst1();
			Inst2();
			Inst3(); 
		}
		
		VALUE Inst1() [1, +INF]
		MEETS {
			None();
		}
		
		VALUE Inst2() [1, +INF]
		MEETS {
			None();
		}
		
		VALUE Inst3() [1, +INF]
		MEETS {
			None();
		}
	}
	
	
	COMP_TYPE SingletonStateVariable VisibilityWindow (_Visible(), NotVisible())
	{
		VALUE _Visible() [20, 30]
		MEETS {
			NotVisible();
		}
		
		VALUE NotVisible() [1, +INF]
		MEETS {
			_Visible();
		}
	}
	
	COMPONENT RSA {FLEXIBLE operations(primitive)} : RSAOperations;
	COMPONENT PointingMode {FLEXIBLE pointing(primitive)} : RSAPointingMode;
	COMPONENT Instrument1 {FLEXIBLE inst1(primitive)} : RSAInstrument;
	COMPONENT Instrument2 {FLEXIBLE inst2(primitive)} : RSAInstrument;
	COMPONENT Instrument3 {FLEXIBLE inst3(primitive)} : RSAInstrument;
	COMPONENT InstLock {FLEXIBLE locking(primitive)} : RSAInstrumentLock;
	COMPONENT Orbit {FLEXIBLE orbit(external)} : RSAOrbit;
	COMPONENT GroundStation {FLEXIBLE visibility(primitive)} : VisibilityWindow;
	
		
	SYNCHRONIZE RSA.operations 
	{
		VALUE Scien()
		{
			cd0 PointingMode.pointing.Planet();
			cd11 <!> Instrument1.inst1.Process();
			cd12 <!> Instrument2.inst2.Process();
			cd13 <!> Instrument3.inst3.Process();
			
			cd2 <!> RSA.operations._Comm();
			
			DURING [0, +INF] [0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd11;
			CONTAINS [0, +INF] [0, +INF] cd12;
			CONTAINS [0, +INF] [0, +INF] cd13;
			BEFORE [0, +INF] cd2;
		}
		
		VALUE _Comm()
		{
			cd0 PointingMode.pointing.Earth();
			cd1 <?> GroundStation.visibility._Visible();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF] [0, +INF] cd1;
		}
		
		
		VALUE Maint()
		{
			cd0 <?> Orbit.orbit.APO();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE Instrument1.inst1
	{
		VALUE Process() {
			
			cd0 InstLock.locking.Inst1();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE Instrument2.inst2
	{
		VALUE Process() {
			
			cd0 InstLock.locking.Inst2();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE Instrument3.inst3
	{
		VALUE Process() {
			
			cd0 InstLock.locking.Inst3();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	
	SYNCHRONIZE PointingMode.pointing
	{
		VALUE Planet()
		{
			cd0 <?> Orbit.orbit.PERI();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
		
		
		VALUE Earth()
		{
			cd0 <?> Orbit.orbit.INT();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
}



