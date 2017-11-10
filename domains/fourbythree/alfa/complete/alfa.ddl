DOMAIN ALFA_COMPLETE
{
	TEMPORAL_MODULE temporal_module = [0, 150], 100;

	// State Variables

	COMP_TYPE SingletonStateVariable FourByThreeType (Idle(), Assembly())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Assembly();
		}
		
		VALUE Assembly() [1, +INF]
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable ALFAProcessType (
		Idle(), Task_1(), Task_2()) 
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Task_1();
			Task_2();
		}
	
		VALUE Task_1() [1, +INF]
		MEETS {
			Idle();
		}
		
		VALUE Task_2() [1, +INF]
		MEETS {
			Idle();
		} 
	}
	

	COMP_TYPE SingletonStateVariable HumanActorType(
		Idle(), 
		Task_1_2a(), Task_1_2b(), Task_1_3(), Task_1_4(),  Task_1_6a(), Task_1_6b(), Task_1_7(),
		Task_2_1(), Task_2_3a(), Task_2_3b(), Task_2_4a(), Task_2_4b(), Task_2_5(), Task_2_6())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Task_1_2a(); 
			Task_1_2b(); 
			Task_1_3(); 
			Task_1_4(); 
			Task_1_6a(); 
			Task_1_6b();
			Task_1_7();
			Task_2_1();
			Task_2_3a(); 
			Task_2_3b(); 
			Task_2_4a(); 
			Task_2_4b(); 
			Task_2_5(); 
			Task_2_6();
		}
	
		VALUE Task_1_2a() [8, 10]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_2b() [10, 22]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_3() [12, 17]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_4() [8, 16]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_6a() [9, 21]
		MEETS {
			Idle();
		}
		
		VALUE Task_1_6b() [13, 17]
		MEETS {
			Idle();
		}
	
		VALUE Task_1_7() [10, 10]
		MEETS {
			Idle();
		}
	
		VALUE Task_2_1() [11, 24]
		MEETS {
			Idle();
		}
		
		VALUE Task_2_3a() [16, 20]
		MEETS {
			Idle();
		} 
		
		VALUE Task_2_3b() [8, 13]
		MEETS {
			Idle();
		} 
		
		VALUE Task_2_4a() [12, 14]
		MEETS {
			Idle();
		}
		 
		VALUE Task_2_4b() [11, 15]
		MEETS {
			Idle();
		} 
		
		VALUE Task_2_5() [7, 10]
		MEETS {
			Idle();
		} 
	
		VALUE Task_2_6() [10, 10]
		MEETS {
			Idle();
		}
	}
	

	COMP_TYPE SingletonStateVariable RobotActorType(
		Idle(), 
		Task_1_1(), Task_1_2a(), Task_1_2b(), Task_1_5(), Task_1_6a(), Task_1_6b(),
		Task_2_2(), Task_2_3a(), Task_2_4a())
	{
		VALUE Idle() [1, +INF]
		MEETS {
			Task_1_1(); 
			Task_1_2a(); 
			Task_1_2b(); 
			Task_1_5(); 
			Task_1_6a(); 
			Task_1_6b();
			Task_2_2(); 
			Task_2_3a(); 
			Task_2_4a();
		}
	
		VALUE Task_1_1() [3, 6]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_2a() [4, 8]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_2b() [5, 7]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_5() [6, 9]
		MEETS {
			Idle();
		} 
		VALUE Task_1_6a() [5, 5]
		MEETS {
			Idle();
		} 
		
		VALUE Task_1_6b() [3, 8]
		MEETS {
			Idle();
		}
		
		VALUE Task_2_2() [4, 7]
		MEETS {
			Idle();
		} 
		
		VALUE Task_2_3a() [6, 6]
		MEETS {
			Idle();
		} 

		VALUE Task_2_4a() [8, 11]
		MEETS {
			Idle();
		}
	}
	
	
	// Components
	
	COMPONENT FourByThree {FLEXIBLE case_study(primitive)} : FourByThreeType;
	COMPONENT ALFA {FLEXIBLE process(primitive) } : ALFAProcessType;
	COMPONENT Human {FLEXIBLE worker(primitive)} : HumanActorType;
	COMPONENT Robot {FLEXIBLE actor(primitive)} : RobotActorType;
	
	// Synchronizations
	
	SYNCHRONIZE FourByThree.case_study
	{
		VALUE Assembly()
		{
			t1 <!> ALFA.process.Task_1();
			t2 <!> ALFA.process.Task_2();
			
			t1 BEFORE [0, +INF] t2;
			CONTAINS [0, +INF] [0, +INF] t1;
			CONTAINS [0, +INF] [0, +INF] t2;
		}	
	}
		
	SYNCHRONIZE ALFA.process
	{
		// [Task1] All R
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Robot.actor.Task_1_2a();		// R
			t12b <!> Robot.actor.Task_1_2b();		// R
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Robot.actor.Task_1_6a();		// R
			t16b <!> Robot.actor.Task_1_6b();		// R
			t17 <!> Human.worker.Task_1_7();		// H
			
			t12a AFTER [0, +INF] t11;
			t12b AFTER [0, +INF] t11;
			
			t13 AFTER [0, +INF] t12a;
			t13 AFTER [0, +INF] t12b;			
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t17 AFTER [0, +INF] t16a;
			t17 AFTER [0, +INF] t16b;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] All H
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Human.worker.Task_1_2a();		// H
			t12b <!> Human.worker.Task_1_2b();		// H
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Human.worker.Task_1_6a();		// H
			t16b <!> Human.worker.Task_1_6b();		// H
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 1R:1H - 1H:1R
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Robot.actor.Task_1_2a();		// R
			t12b <!> Human.worker.Task_1_2b();		// H
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Human.worker.Task_1_6a();		// H
			t16b <!> Robot.actor.Task_1_6b();		// R
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 1R:1H - 1R:1H
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Robot.actor.Task_1_2a();		// R
			t12b <!> Human.worker.Task_1_2b();		// H
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Robot.actor.Task_1_6a();		// R
			t16b <!> Human.worker.Task_1_6b();		// H
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 1H:1R - 1R:1H
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Human.worker.Task_1_2a();		// H
			t12b <!> Robot.actor.Task_1_2b();		// R
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Robot.actor.Task_1_6a();		// R
			t16b <!> Human.worker.Task_1_6b();		// H
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 1H:1R - 1H:1R
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Human.worker.Task_1_2a();		// H
			t12b <!> Robot.actor.Task_1_2b();		// R
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Human.worker.Task_1_6a();		// H
			t16b <!> Robot.actor.Task_1_6b();		// R
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 2R - 2H
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Robot.actor.Task_1_2a();		// R
			t12b <!> Robot.actor.Task_1_2b();		// R
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Human.worker.Task_1_6a();		// H
			t16b <!> Human.worker.Task_1_6b();		// H
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
		
		// [Task1] 2H : 2R
		VALUE Task_1()
		{
			t11 <!> Robot.actor.Task_1_1();			// R
			t12a <!> Human.worker.Task_1_2a();		// H
			t12b <!> Human.worker.Task_1_2b();		// H
			t13 <!> Human.worker.Task_1_3();		// H
			t14 <!> Human.worker.Task_1_4();		// H
			t15 <!> Robot.actor.Task_1_5();			// R
			t16a <!> Robot.actor.Task_1_6a();		// R
			t16b <!> Robot.actor.Task_1_6b();		// R
			t17 <!> Human.worker.Task_1_7();		// H
			
			t11 BEFORE [0, +INF] t12a;
			t11 BEFORE [0, +INF] t12b;
			t11 BEFORE [0, +INF] t13;
			t11 BEFORE [0, +INF] t14;
			
			t12a BEFORE [0, +INF] t13;
			t12b BEFORE [0, +INF] t13;
			
			t13 BEFORE [0, +INF] t14;
			
			t14 BEFORE [0, +INF] t15;
			t15 BEFORE [0, +INF] t16a;
			t15 BEFORE [0, +INF] t16b;
			
			t16a BEFORE [0, +INF] t17;
			t16b BEFORE [0, +INF] t17;
			
			
			CONTAINS [0, +INF] [0, +INF] t11;
			CONTAINS [0, +INF] [0, +INF] t12a;
			CONTAINS [0, +INF] [0, +INF] t12b;
			CONTAINS [0, +INF] [0, +INF] t13;
			CONTAINS [0, +INF] [0, +INF] t14;
			CONTAINS [0, +INF] [0, +INF] t15;
			CONTAINS [0, +INF] [0, +INF] t16a;
			CONTAINS [0, +INF] [0, +INF] t16b;
			CONTAINS [0, +INF] [0, +INF] t17;
		}
	
		// [Task2] All R
		VALUE Task_2()
		{
			t21 <!> Human.worker.Task_2_1();		// H
			t22 <!> Robot.actor.Task_2_2();			// R
			t23a <!> Robot.actor.Task_2_3a();		// R
			t23b <!> Human.worker.Task_2_3b();		// H
			t24a <!> Robot.actor.Task_2_4a();		// R
			t24b <!> Human.worker.Task_2_4b();		// H
			t25 <!> Human.worker.Task_2_5();		// H
			t26 <!> Human.worker.Task_2_6();		// H
			
			t21 BEFORE [0, +INF] t22;
			
			t22 BEFORE [0, +INF] t23a;
			t22 BEFORE [0, +INF] t23b;
			t22 BEFORE [0, +INF] t24a;
			t22 BEFORE [0, +INF] t24b;
			
			t23a BEFORE [0, +INF] t25;
			t23b BEFORE [0, +INF] t25;
			t24a BEFORE [0, +INF] t25;
			t24b BEFORE [0, +INF] t25;
			
			t25 BEFORE [0, +INF] t26;
			
			CONTAINS [0, +INF] [0, +INF] t21;
			CONTAINS [0, +INF] [0, +INF] t22;
			CONTAINS [0, +INF] [0, +INF] t23a;
			CONTAINS [0, +INF] [0, +INF] t23b;
			CONTAINS [0, +INF] [0, +INF] t24a;
			CONTAINS [0, +INF] [0, +INF] t24b;
			CONTAINS [0, +INF] [0, +INF] t25;
			CONTAINS [0, +INF] [0, +INF] t26;
		}
		
		// [Task2] All H
		VALUE Task_2()
		{
			t21 <!> Human.worker.Task_2_1();		// H
			t22 <!> Robot.actor.Task_2_2();			// R
			t23a <!> Human.worker.Task_2_3a();		// H
			t23b <!> Human.worker.Task_2_3b();		// H
			t24a <!> Human.worker.Task_2_4a();		// H
			t24b <!> Human.worker.Task_2_4b();		// H
			t25 <!> Human.worker.Task_2_5();		// H
			t26 <!> Human.worker.Task_2_6();		// H
			
			t21 BEFORE [0, +INF] t22;
			
			t22 BEFORE [0, +INF] t23a;
			t22 BEFORE [0, +INF] t23b;
			t22 BEFORE [0, +INF] t24a;
			t22 BEFORE [0, +INF] t24b;
			
			t23a BEFORE [0, +INF] t25;
			t23b BEFORE [0, +INF] t25;
			t24a BEFORE [0, +INF] t25;
			t24b BEFORE [0, +INF] t25;
			
			t25 BEFORE [0, +INF] t26;
			
			CONTAINS [0, +INF] [0, +INF] t21;
			CONTAINS [0, +INF] [0, +INF] t22;
			CONTAINS [0, +INF] [0, +INF] t23a;
			CONTAINS [0, +INF] [0, +INF] t23b;
			CONTAINS [0, +INF] [0, +INF] t24a;
			CONTAINS [0, +INF] [0, +INF] t24b;
			CONTAINS [0, +INF] [0, +INF] t25;
			CONTAINS [0, +INF] [0, +INF] t26;
		}
		
		// [Task2] All 1:R - 1:H
		VALUE Task_2()
		{
			t21 <!> Human.worker.Task_2_1();		// H
			t22 <!> Robot.actor.Task_2_2();			// R
			t23a <!> Robot.actor.Task_2_3a();		// R
			t23b <!> Human.worker.Task_2_3b();		// H
			t24a <!> Human.worker.Task_2_4a();		// R
			t24b <!> Human.worker.Task_2_4b();		// H
			t25 <!> Human.worker.Task_2_5();		// H
			t26 <!> Human.worker.Task_2_6();		// H
			
			t21 BEFORE [0, +INF] t22;
			
			t22 BEFORE [0, +INF] t23a;
			t22 BEFORE [0, +INF] t23b;
			t22 BEFORE [0, +INF] t24a;
			t22 BEFORE [0, +INF] t24b;
			
			t23a BEFORE [0, +INF] t25;
			t23b BEFORE [0, +INF] t25;
			t24a BEFORE [0, +INF] t25;
			t24b BEFORE [0, +INF] t25;
			
			t25 BEFORE [0, +INF] t26;
			
			CONTAINS [0, +INF] [0, +INF] t21;
			CONTAINS [0, +INF] [0, +INF] t22;
			CONTAINS [0, +INF] [0, +INF] t23a;
			CONTAINS [0, +INF] [0, +INF] t23b;
			CONTAINS [0, +INF] [0, +INF] t24a;
			CONTAINS [0, +INF] [0, +INF] t24b;
			CONTAINS [0, +INF] [0, +INF] t25;
			CONTAINS [0, +INF] [0, +INF] t26;
		}
		
		// [Task2] All 1:H - 1:R
		VALUE Task_2()
		{
			t21 <!> Human.worker.Task_2_1();		// H
			t22 <!> Robot.actor.Task_2_2();			// R
			t23a <!> Human.worker.Task_2_3a();		// R
			t23b <!> Human.worker.Task_2_3b();		// H
			t24a <!> Robot.actor.Task_2_4a();		// R
			t24b <!> Human.worker.Task_2_4b();		// H
			t25 <!> Human.worker.Task_2_5();		// H
			t26 <!> Human.worker.Task_2_6();		// H
			
			t21 BEFORE [0, +INF] t22;
			
			t22 BEFORE [0, +INF] t23a;
			t22 BEFORE [0, +INF] t23b;
			t22 BEFORE [0, +INF] t24a;
			t22 BEFORE [0, +INF] t24b;
			
			t23a BEFORE [0, +INF] t25;
			t23b BEFORE [0, +INF] t25;
			t24a BEFORE [0, +INF] t25;
			t24b BEFORE [0, +INF] t25;
			
			t25 BEFORE [0, +INF] t26;
			
			CONTAINS [0, +INF] [0, +INF] t21;
			CONTAINS [0, +INF] [0, +INF] t22;
			CONTAINS [0, +INF] [0, +INF] t23a;
			CONTAINS [0, +INF] [0, +INF] t23b;
			CONTAINS [0, +INF] [0, +INF] t24a;
			CONTAINS [0, +INF] [0, +INF] t24b;
			CONTAINS [0, +INF] [0, +INF] t25;
			CONTAINS [0, +INF] [0, +INF] t26;
		}
		
	}
SYNCHRONIZE Human.worker {
	}
}
