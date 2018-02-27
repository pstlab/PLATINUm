DOMAIN RACE_ServeCoffee
{
	TEMPORAL_MODULE temporal_module = [0, 10000], 100;
	PAR_TYPE EnumerationParameterType locations = {counter, table1, table2, base, floor};
	PAR_TYPE EnumerationParameterType objects = {mug1, mug2};
	PAR_TYPE EnumerationParameterType guests = {guest1, guest2};
	
	COMP_TYPE SingletonStateVariable TrixiControllerType (Idle(), ServeCoffee(locations, objects))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			ServeCoffee(?locations, ?object);		
		}
		
		VALUE ServeCoffee(?locations, ?object) [1, +INF]
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable TrixiBaseControllerType (At(locations), DrivingTo(locations), MovingInBlind(locations), AtMA(locations), MovingOutBlind(locations))
	{
		VALUE At(?loc) [1, +INF]
		MEETS {
			DrivingTo(?loc1);
			?loc1 != ?loc;
			
			MovingInBlind(?loc2);
			?loc2 = ?loc;
		}
		
		VALUE DrivingTo(?loc) [5, 10]
		MEETS {
			At(?loc1);
			?loc1 = ?loc;
		}
	
		VALUE AtMA(?loc) [1, +INF]
		MEETS {
			MovingOutBlind(?loc1);
			?loc1 = ?loc;
		}
		
		VALUE MovingInBlind(?loc) [1, 1]
		MEETS {
			AtMA(?loc1);
			?loc1 = ?loc;
		}
		
		VALUE MovingOutBlind(?loc) [1, 1]
		MEETS {
			At(?loc1);
			?loc1 = ?loc;
		}
	}
	
	COMP_TYPE SingletonStateVariable TrixiArmControllerType (Idle(), GraspObject(objects), Holding(objects), PutObject(objects))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			GraspObject(?obj);
		}
		
		VALUE GraspObject(?obj) [1, +INF]
		MEETS {
			Holding(?obj1);
			?obj1 = ?obj;
		}
		
		VALUE PutObject(?obj) [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Holding(?obj) [1, +INF]
		MEETS {
			PutObject(?obj1);
			?obj1 = ?obj;
		}
	}
	
	COMP_TYPE SingletonStateVariable TrixiArmConfigurationControllerType (DrivingPose(), Changing(), ManipulationPose())
	{
		VALUE DrivingPose() [1, +INF]
		MEETS {
			Changing();
		}
		
		VALUE Changing() [3, 3]
		MEETS {
			DrivingPose();
			ManipulationPose();
		}
		
		VALUE ManipulationPose() [1, +INF]
		MEETS {
			Changing();
		} 
	}
	
	COMP_TYPE SingletonStateVariable PlacingAreaStatusType (Free(), ObjectOn(objects))
	{
		VALUE Free() [1, +INF]
		MEETS {
			ObjectOn(?obj);
		}
		
		VALUE ObjectOn(?obj) [1, +INF]
		MEETS {
			Free();
		}
	}
	
	COMP_TYPE SingletonStateVariable TableServiceAreaType (Free(), WaitingForCoffee(guests), Served(guests))
	{	
		VALUE Free() [1, +INF]
		MEETS {
			WaitingForCoffee(?guest);
		}
		
		VALUE WaitingForCoffee(?guest) [1, +INF]
		MEETS {
			Served(?guest1);
			?guest1 = ?guest;
		}
		
		VALUE Served(?guest)Ê[1, +INF]
		MEETS {
			Free();
			
			WaitingForCoffee(?gues1);
			?guest1 = ?guest;
		}
	}	
	
	COMPONENT Table1ServiceArea { FLEXIBLE table1_sa(trex_internal_dispatch_asap) } : TableServiceAreaType;
	
	COMPONENT Trixi { FLEXIBLE mission(trex_internal_dispatch_asap) } : TrixiControllerType;
	COMPONENT TrixiBase { FLEXIBLE position(trex_internal_dispatch_asap) } : TrixiBaseControllerType;
	COMPONENT TrixiArmController { FLEXIBLE status(trex_internal_dispatch_asap) }Ê: TrixiArmControllerType;
	COMPONENT TrixiArmConfiguration { FLEXIBLE configuration(trex_internal_dispatch_asap) } : TrixiArmConfigurationControllerType;

	COMPONENT CounterPlacingArea { FLEXIBLE counter_pa(trex_internal_dispatch_asap) } : PlacingAreaStatusType;
	COMPONENT Table1PlacingArea { FLEXIBLE table1_pa(trex_internal_dispatch_asap) } : PlacingAreaStatusType;
	
	
	SYNCHRONIZE Table1ServiceArea.table1_sa
	{
		VALUE WaitingForCoffee(?guest)
		{
			cd0 <!> Trixi.mission.ServeCoffee(?loc, ?obj);
			cd1 <!> Table1ServiceArea.table1_sa.Served(?guest1);
			
			EQUALSÊcd0;
			MEETS cd1;
			
			?guest1 = ?guest;
			?loc = table1; 
		}
	}


	SYNCHRONIZE Trixi.mission
	{
		VALUE ServeCoffee(?loc, ?obj)
		{
			cd0 TrixiBase.position.At(?loc0);
			cd1 <!> TrixiBase.position.At(?loc1);
			
			cd2 <!> TrixiArmController.status.GraspObject(?obj2);
			cd3 <!> TrixiArmController.status.PutObject(?obj3);
			
			CONTAINS [0, +INF]Ê[0, +INF] cd0;
			CONTAINS [0, +INF] [0, +INF] cd1;
			cd0 BEFORE [0, +INF]Êcd1;
			
			cd2 STARTS-DURING [0, +INF]Ê[0, +INF]Êcd0;
			cd3 STARTS-DURING [0, +INF]Ê[0, +INF]Êcd1;
			
			?obj2 = ?obj;
			?obj3 = ?obj;
			
			?loc0 = counter;
			?loc1 = ?loc;
		}
	}
	
	SYNCHRONIZE TrixiArmController.status
	{
		VALUE GraspObject(?obj)
		{
			cd0 <?> CounterPlacingArea.counter_pa.ObjectOn(?obj0);
			cd1 TrixiArmConfiguration.configuration.ManipulationPose();
			cd2 <!> CounterPlacingArea.counter_pa.Free();
			
			DURING [0, +INF] [0, +INF] cd0;
			DURING [0, +INF]Ê[0, +INF]Êcd1;
			MEETS cd2;
			
			?obj = ?obj0;
		}
		
		VALUE PutObject(?obj)
		{
			cd0 <?> Table1PlacingArea.table1_pa.Free();
			cd1 TrixiArmConfiguration.configuration.ManipulationPose();
			cd2 <!> Table1PlacingArea.table1_pa.ObjectOn(?obj1);
			
			DURING [0, +INF]Ê[0, +INF]Êcd0;
			DURING [0, +INF]Ê[0, +INF]Êcd1;
			MEETS cd2;
			
			?obj = ?obj1;
		}
	}
	
	SYNCHRONIZE TrixiBase.position
	{
		VALUE DrivingTo(?loc)
		{
			cd0 TrixiArmConfiguration.configuration.DrivingPose();
			
			DURINGÊ[0, +INF]Ê[0, +INF] cd0;Ê
		}
	}
}



