DOMAIN ROMAN19
{
	TEMPORAL_MODULE temporal_module=[0,150],150;
	
	COMP_TYPE SingletonStateVariable HumanType (Idle(), _Trigger())
	{
		VALUE Idle() [0, +INF]
		MEETS {
			_Trigger();
		}		
		
		VALUE _Trigger() [3, 5]
		MEETS {
			Idle();
		}
		
	}
	
	
	COMP_TYPE SingletonStateVariable MovementType (At(), _Move())
	{
		VALUE At() [4, +INF]
		MEETS {
			_Move();
		}
		VALUE _Move() [4,30]
		MEETS {
			At();
		}
	}
	
	
	COMP_TYPE SingletonStateVariable PhotoActionType (_Photo(), Filter(), Idle())
	{
		VALUE _Photo() [4, 7]
		MEETS {
			Filter();
		}
		
		
		VALUE Filter() [3, 7]
		MEETS {
			Idle();
		}
		
		
		VALUE Idle() [3, +INF]
		MEETS {
			_Photo();
		}
	}
	
	
	COMPONENT Human {FLEXIBLE h0(primitive)} : HumanType;
	COMPONENT Movement {FLEXIBLE Mo7(primitive)}: MovementType;
	COMPONENT PhotoAction {FLEXIBLE Ph11(primitive)}: PhotoActionType;
	
	SYNCHRONIZE Human.h0
	{
		VALUE _Trigger() {
			
			cd0 <!> PhotoAction.Ph11.Filter();
			
			MEETS cd0;
		}
	}
	
	SYNCHRONIZE PhotoAction.Ph11
	{
		VALUE Filter() {
			
			cd0 <!> Movement.Mo7.At();
			
			DURING [0, +INF] [0, +INF] cd0;
		}
	}
	
	SYNCHRONIZE Movement.Mo7 
	{
		VALUE _Move() {
			
			cd0 PhotoAction.Ph11.Idle();
			
			DURING [0, +INF] [0, +INF] cd0;
			
		} 
	}
}
