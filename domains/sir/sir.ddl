DOMAIN SIR
{
	TEMPORAL_MODULE temporal_module = [0, 100], 500;
	
	PAR_TYPE EnumerationParameterType contents = {
		ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10
	};
	
	PAR_TYPE EnumerationParameterType explanations = {
		yes, no
	};
	
	PAR_TYPE EnumerationParameterType sound_levels = {
		none, low, medium, high 
	};
	
	PAR_TYPE EnumerationParameterType speech_rates = {
		slow, regular
	};
	
	PAR_TYPE EnumerationParameterType text_sizes = {
		normal, large
	};
	
	
	
//	COMP_TYPE RenewableResource DailyExercisesType(3)
	
	
	
	
	COMP_TYPE SingletonStateVariable StimulationType(Idle(), DoCognitiveStimulation())
	{	
		VALUE Idle() [1, +INF]
		MEETS {
			DoCognitiveStimulation();
		} 
		
		VALUE DoCognitiveStimulation() [1, +INF] 
		MEETS {
			Idle();
		}
	}
	
	COMP_TYPE SingletonStateVariable InteractionWindowType (_None(), _Available(explanations, sound_levels, speech_rates, text_sizes))
	{
		VALUE _None() [1, +INF]
		MEETS {
			_Available(?e, ?s, ?r, ?t);
		}
		
		VALUE _Available(?e, ?s, ?r, ?t) [1, +INF]
		MEETS {
			_None();
		}
	}
	
	COMP_TYPE SingletonStateVariable AssistantType (Idle(), _DoStimulationAction(contents, explanations, sound_levels, speech_rates, text_sizes))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_DoStimulationAction(?s, ?e, ?s, ?r, ?t);
		}
		
		VALUE _DoStimulationAction(?s, ?e, ?s, ?r, ?t) [5, 8]
		MEETS {
			Idle();
		}
	} 
	
	COMPONENT Stimulation {FLEXIBLE process(functional)} : StimulationType;
	COMPONENT Patient {FLEXIBLE windows(external)} : InteractionWindowType;
	COMPONENT Robot {FLEXIBLE tasks(primitive)} : AssistantType;
//	COMPONENT PatientLoad {FLEXIBLE capacity(primitive)} : DailyExercisesType;
	
	SYNCHRONIZE Stimulation.process
	{
		VALUE DoCognitiveStimulation()
		{
			d1 <!> Robot.tasks._DoStimulationAction(?c1, ?e1, ?s1, ?r1, ?t1);
			d2 <!> Robot.tasks._DoStimulationAction(?c2, ?e2, ?s2, ?r2, ?t2);
			d3 <!> Robot.tasks._DoStimulationAction(?c3, ?e3, ?s3, ?r3, ?t3);
			d4 <!> Robot.tasks._DoStimulationAction(?c4, ?e4, ?s4, ?r4, ?t4);
			d5 <!> Robot.tasks._DoStimulationAction(?c5, ?e5, ?s5, ?r5, ?t5);
			d6 <!> Robot.tasks._DoStimulationAction(?c6, ?e6, ?s6, ?r6, ?t6);
//			d7 <!> Robot.tasks._DoStimulationAction(?c7, ?e7, ?s7, ?r7, ?t7);
//			d8 <!> Robot.tasks._DoStimulationAction(?c8, ?e8, ?s8, ?r8, ?t8);			

			CONTAINS [0, +INF] [0, +INF] d1;
			CONTAINS [0, +INF] [0, +INF] d2;
			CONTAINS [0, +INF] [0, +INF] d3;
			CONTAINS [0, +INF] [0, +INF] d4;
			CONTAINS [0, +INF] [0, +INF] d5;
			CONTAINS [0, +INF] [0, +INF] d6;
//			CONTAINS [0, +INF] [0, +INF] d7;
//			CONTAINS [0, +INF] [0, +INF] d8;
	
			// set stimulation content
			?c1 = ex1;
			?c2 = ex3;
			?c3 = ex8;
			?c4 = ex2;
			?c5 = ex4;
			?c6 = ex6;
//			?c7 = ex7;
//			?c8 = ex5;
		}
	}
	
	SYNCHRONIZE Robot.tasks
	{
		VALUE _DoStimulationAction(?c, ?e, ?s, ?r, ?t)
		{
			d0 <?> Patient.windows._Available(?e0, ?sl0, ?r0, ?t0);
//			d1 <!> PatientLoad.capacity.REQUIREMENT(?session); 
			
			DURING [0, +INF] [0, +INF] d0;
//			d1 EQUALS d0;
			
			// bind parameters
			?e = ?e0;
			?s = ?sl0;
			?r = ?r0;
			?t = ?t0;
			
			// require amount
//			?session = 1;
		}
	}
}