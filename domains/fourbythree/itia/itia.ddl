DOMAIN HRC_ITIA_TEST {


TEMPORAL_MODULE temporal_module = [0, 300], 100;



PAR_TYPE EnumerationParameterType configuration = {  Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q20, Q11, Q22, Q10, Q21, Q13, Q12, Q15, Q14, Q17, Q16, Q19, Q18 };


COMP_TYPE SingletonStateVariable ProcessType(Idle(), HRC()) {
	
	VALUE Idle() [1, +INF] 
	MEETS {
		HRC();
	}
	
	VALUE HRC() [1, +INF]
	MEETS {
		Idle();
	}
}


COMP_TYPE SingletonStateVariable HumanType (Idle(), _H10(), _H12(), _H1(), _H11(), _H2(), _H3(), _H13(), _H4(), _H5(), _H6(), _H7(), _H8(), _H9()) {
	VALUE Idle() [1, +INF]
	MEETS {
		_H10();
		_H12();
		_H1();
		_H11();
		_H2();
		_H3();
		_H13();
		_H4();
		_H5();
		_H6();
		_H7();
		_H8();
		_H9();
	}

	VALUE _H10() [1, 15]
	MEETS {
		Idle();
	}
	VALUE _H12() [1, 16]
	MEETS {
		Idle();
	}
	VALUE _H1() [6, 26]
	MEETS {
		Idle();
	}
	VALUE _H11() [1, 17]
	MEETS {
		Idle();
	}
	VALUE _H2() [23, 43]
	MEETS {
		Idle();
	}
	VALUE _H3() [13, 33]
	MEETS {
		Idle();
	}
	VALUE _H13() [1, 15]
	MEETS {
		Idle();
	}
	VALUE _H4() [17, 37]
	MEETS {
		Idle();
	}
	VALUE _H5() [1, 17]
	MEETS {
		Idle();
	}
	VALUE _H6() [1, 16]
	MEETS {
		Idle();
	}
	VALUE _H7() [1, 16]
	MEETS {
		Idle();
	}
	VALUE _H8() [1, 17]
	MEETS {
		Idle();
	}
	VALUE _H9() [1, 16]
	MEETS {
		Idle();
	}

}



COMP_TYPE SingletonStateVariable RobotControllerType (Idle(), RQ18(), RQ16(), RQ17(), RQ14(), RQ15(), RQ12(), RQ13(), RQ2(), RQ1(), RQ4(), RQ3(), RQ5(), RQ10(), RQ11()) {
	VALUE Idle() [1, +INF]
	MEETS {
		RQ18();
		RQ16();
		RQ17();
		RQ14();
		RQ15();
		RQ12();
		RQ13();
		RQ2();
		RQ1();
		RQ4();
		RQ3();
		RQ5();
		RQ10();
		RQ11();
	}

	VALUE RQ18() [1, 23]
	MEETS {
		Idle();
	}
	VALUE RQ16() [1, 22]
	MEETS {
		Idle();
	}
	VALUE RQ17() [1, 16]
	MEETS {
		Idle();
	}
	VALUE RQ14() [1, 18]
	MEETS {
		Idle();
	}
	VALUE RQ15() [1, 22]
	MEETS {
		Idle();
	}
	VALUE RQ12() [1, 19]
	MEETS {
		Idle();
	}
	VALUE RQ13() [1, 32]
	MEETS {
		Idle();
	}
	VALUE RQ2() [1, 25]
	MEETS {
		Idle();
	}
	VALUE RQ1() [1, 18]
	MEETS {
		Idle();
	}
	VALUE RQ4() [1, 16]
	MEETS {
		Idle();
	}
	VALUE RQ3() [1, 19]
	MEETS {
		Idle();
	}
	VALUE RQ5() [1, 22]
	MEETS {
		Idle();
	}
	VALUE RQ10() [1, 23]
	MEETS {
		Idle();
	}
	VALUE RQ11() [1, 23]
	MEETS {
		Idle();
	}

}



COMP_TYPE SingletonStateVariable RobotMotionType (At(configuration), MotionTask(configuration, configuration)) {

	VALUE At(?config) [1, +INF]
	MEETS {
		MotionTask(?c1, ?c2);
		?config = ?c1;
	}

	VALUE MotionTask(?c1, ?c2) [1, +INF]
	MEETS {
		At(?config);
		?config = ?c2;
	}

}

COMP_TYPE SingletonStateVariable RobotToolType (Idle(), Activate(), Operating(), Deactivate()) {

	VALUE Idle() [1, +INF]
	MEETS {
		Activate();
	}

	VALUE Activate() [1, 3]
	MEETS {
		Operating();
	}

	VALUE Operating() [3, 3]
	MEETS {
		Deactivate();
	}

	VALUE Deactivate() [1, 3]
	MEETS {
		Idle();
	}

}

COMPONENT CollaborativeProcess {FLEXIBLE process(primitive)} : ProcessType;
COMPONENT RobotController {FLEXIBLE controller(primitive)} : RobotControllerType;
COMPONENT Human {FLEXIBLE operator(primitive)} : HumanType;
COMPONENT RobotMotion {FLEXIBLE motion(primitive)} : RobotMotionType;
COMPONENT T1 {FLEXIBLE toolT1(primitive)} : RobotToolType;
COMPONENT T2 {FLEXIBLE toolT2(primitive)} : RobotToolType;
COMPONENT T3 {FLEXIBLE toolT3(primitive)} : RobotToolType;

SYNCHRONIZE CollaborativeProcess.process {
	
	// RQ10, RQ11, RQ12
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 RobotController.controller.RQ10(); 
		
		// {H6, Q11}
		c1 RobotController.controller.RQ11(); 
		
		// {H7, Q12} 
		c2 RobotController.controller.RQ12();
//		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	// RQ10, RQ11, H7
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 RobotController.controller.RQ10(); 
		
		// {H6, Q11}
		c1 RobotController.controller.RQ11(); 
		
		// {H7, Q12} 
		c2 Human.operator._H7();
		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	// H5, RQ11, RQ12 
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 Human.operator._H5(); 
		
		// {H6, Q11}
		c1 RobotController.controller.RQ11(); 
		
		// {H7, Q12} 
		c2 RobotController.controller.RQ12();
//		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	// H5, RQ11, H7 
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 Human.operator._H5(); 
		
		// {H6, Q11}
		c1 RobotController.controller.RQ11(); 
		
		// {H7, Q12} 
		c2 Human.operator._H7();
//		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	
	
	// RQ10, H6, H7
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 RobotController.controller.RQ10(); 
		
		// {H6, Q11}
		c1 Human.operator._H6(); 
		
		// {H7, Q12} 
		c2 Human.operator._H7();
		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	// H5, H6, H7
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 Human.operator._H5(); 
		
		// {H6, Q11}
		c1 Human.operator._H6();
		
		// {H7, Q12} 
		c2 Human.operator._H7();
		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
	
	// H5, H6, RQ12
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 Human.operator._H5(); 
		
		// {H6, Q11}
		c1 Human.operator._H6();
		
		// {H7, Q12} 
		c2 RobotController.controller.RQ12();
		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}

	// RQ10, H6, RQ12
	VALUE HRC() {
		
		// robot dedicated tasks
		r0 RobotController.controller.RQ2();
		r1 RobotController.controller.RQ3();
		r2 RobotController.controller.RQ4();
		r3 RobotController.controller.RQ5();
		
		// human dedicated tasks
		h0 Human.operator._H1();
		h1 Human.operator._H2();
		h2 Human.operator._H3();
		h3 Human.operator._H4();
		
		// collaborative tasks 
		// {H5, Q10}
		c0 RobotController.controller.RQ10(); 
		
		// {H6, Q11}
		c1 Human.operator._H6(); 
		
		// {H7, Q12} 
		c2 RobotController.controller.RQ12();
		
//		// {H8, Q13} 
//		c3 RobotController.controller.RQ13();
//		
//		// {H9, Q14} 
//		c4 RobotController.controller.RQ14();
//		
//		// {H10, Q15} 
//		c5 RobotController.controller.RQ15();
//		
//		// {H11, Q16} 
//		c6 RobotController.controller.RQ16();
//		
//		// {H12, Q17} 
//		c7 RobotController.controller.RQ17();
//		
//		// {H13, Q18}
//		c8 RobotController.controller.RQ18();
		
		CONTAINS [0, +INF] [0, +INF] r0;
		CONTAINS [0, +INF] [0, +INF] r1;
		CONTAINS [0, +INF] [0, +INF] r2;
		CONTAINS [0, +INF] [0, +INF] r3;
		CONTAINS [0, +INF] [0, +INF] h0;
		CONTAINS [0, +INF] [0, +INF] h1;
		CONTAINS [0, +INF] [0, +INF] h2;
		CONTAINS [0, +INF] [0, +INF] h3;
		
		CONTAINS [0, +INF] [0, +INF] c0;
		CONTAINS [0, +INF] [0, +INF] c1;
		CONTAINS [0, +INF] [0, +INF] c2;
//		CONTAINS [0, +INF] [0, +INF] c3;
//		CONTAINS [0, +INF] [0, +INF] c4;
//		CONTAINS [0, +INF] [0, +INF] c5;
//		CONTAINS [0, +INF] [0, +INF] c6;
//		CONTAINS [0, +INF] [0, +INF] c7;
//		CONTAINS [0, +INF] [0, +INF] c8;
	}
}


SYNCHRONIZE RobotController.controller {

	VALUE RQ18() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q18;
	}

	VALUE RQ16() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q16;
	}

	VALUE RQ17() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q17;
	}

	VALUE RQ14() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q14;
	}

	VALUE RQ15() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q15;
	}

	VALUE RQ12() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q12;
	}

	VALUE RQ13() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q13;
	}

	VALUE RQ2() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q2;
	}

	VALUE RQ1() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q1;
	}

	VALUE RQ4() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q4;
	}

	VALUE RQ3() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q3;
	}

	VALUE RQ5() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q5;
	}

	VALUE RQ10() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q10;
	}

	VALUE RQ11() {

		cd0 <?> RobotMotion.motion.At(?pos0);

		cd1 <!> RobotMotion.motion.At(?pos1);

		cd2 <!> T3.toolT3.Activate();

		cd3 <!> T3.toolT3.Operating();

		cd4 <!> T3.toolT3.Deactivate();

		STARTS-DURING [0, +INF] [0, +INF] cd0;
		ENDS-DURING [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		CONTAINS [0, +INF] [0, +INF] cd4;
		cd2 MEETS cd3;
		cd3 MEETS cd4;
cd2 DURING [0, +INF] [0, +INF] cd1;
cd3 DURING [0, +INF] [0, +INF] cd1;
cd4 DURING [0, +INF] [0, +INF] cd1;
		?pos1 = Q11;
	}


}


}


