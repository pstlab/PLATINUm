DOMAIN SIR
{
	TEMPORAL_MODULE temporal_module = [0, 100], 500;
	
	PAR_TYPE EnumerationParameterType exercises = {
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
	
	COMP_TYPE RenewableResource DailyExercisesType(3)
	
	COMP_TYPE SingletonStateVariable StimulationType(Idle(), DoCognitiveStimulation(exercises))
	{	
		VALUE Idle() [1, +INF]
		MEETS {
			DoCognitiveStimulation(?s);
		} 
		
		VALUE DoCognitiveStimulation(?s) [1, +INF] 
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
	
	COMP_TYPE SingletonStateVariable AssistantType (Idle(), _DoStimulationAction(exercises, explanations, sound_levels, speech_rates, text_sizes))
	{
		VALUE Idle() [1, +INF]
		MEETS {
			_DoStimulationAction(?s, ?e, ?s, ?r, ?t);
		}
		
		VALUE _DoStimulationAction(?s, ?e, ?s, ?r, ?t) [3, 7]
		MEETS {
			Idle();
		}
	} 
	
	COMPONENT Stimulation {FLEXIBLE process(functional)} : StimulationType;
	COMPONENT Patient {FLEXIBLE windows(external)} : InteractionWindowType;
	COMPONENT Robot {FLEXIBLE tasks(primitive)} : AssistantType;
	COMPONENT PatientLoad {FLEXIBLE capacity(primitive)} : DailyExercisesType;
	
	SYNCHRONIZE Stimulation.process
	{
		VALUE DoCognitiveStimulation(?s)
		{
			d0 <?> Patient.windows._Available(?e0, ?sl0, ?r0, ?t0);
			d1 <!> Robot.tasks._DoStimulationAction(?s1, ?e1, ?sl1, ?r1, ?t1);
			d3 <!> PatientLoad.capacity.REQUIREMENT(?session);
			
			EQUALS d1;
			d3 EQUALS d0;
			d1 DURING [0, +INF] [0, +INF] d0;
			
			?session = 1;
			
			// bind parameters
			?s1 = ?s;
			?e1 = ?e0;
			?sl1 = ?sl0;
			?r1 = ?r0;
			?t1 = ?t0;
		}
	}
	
}