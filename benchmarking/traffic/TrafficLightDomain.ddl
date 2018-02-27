/*
 * This is an example domain 
 */

DOMAIN TrafficLight
{
	TEMPORAL_MODULE temporal_module = [0, 200], 300;

	COMP_TYPE SingletonStateVariable TrafficLightType (
		Red(), Yellow(), Green())
	{
		VALUE Red() [30, 30]
		MEETS {
			Green();
		}
		
		VALUE Green() [20, 20]
		MEETS {
			Yellow();
		}
		
		VALUE Yellow() [10, 10]
		MEETS {
			Red();
		}
	}
	
	// Components
	
	COMPONENT TL1 {FLEXIBLE timeline1(primitive) } : TrafficLightType;
	COMPONENT TL2 {FLEXIBLE timeline2(primitive)} : TrafficLightType;
	
	// Synchronizations
	
	SYNCHRONIZE TL1.timeline1
	{
		VALUE Green() 
		{
			otherRed TL2.timeline2.Red();
			STARTS otherRed;
		}
	
	}
	
	SYNCHRONIZE TL2.timeline2
	{
		VALUE Green()
		{
			otherRed TL1.timeline1.Red();
			STARTS otherRed;
		}
	}
}
