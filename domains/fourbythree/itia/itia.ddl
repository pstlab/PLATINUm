DOMAIN FBT_HRC_ITIA {


TEMPORAL_MODULE temporal_module = [0, 100], 100;



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

	VALUE RQ18() [1, 1]
	MEETS {
		Idle();
	}
	VALUE RQ16() [1, 12]
	MEETS {
		Idle();
	}
	VALUE RQ17() [1, 11]
	MEETS {
		Idle();
	}
	VALUE RQ14() [1, 10]
	MEETS {
		Idle();
	}
	VALUE RQ15() [1, 9]
	MEETS {
		Idle();
	}
	VALUE RQ12() [1, 10]
	MEETS {
		Idle();
	}
	VALUE RQ13() [1, 12]
	MEETS {
		Idle();
	}
	VALUE RQ2() [1, 11]
	MEETS {
		Idle();
	}
	VALUE RQ1() [1, 11]
	MEETS {
		Idle();
	}
	VALUE RQ4() [1, 10]
	MEETS {
		Idle();
	}
	VALUE RQ3() [1, 11]
	MEETS {
		Idle();
	}
	VALUE RQ5() [1, 9]
	MEETS {
		Idle();
	}
	VALUE RQ10() [1, 1]
	MEETS {
		Idle();
	}
	VALUE RQ11() [1, 1]
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

	VALUE Activate() [1, 5]
	MEETS {
		Operating();
	}

	VALUE Operating() [5, 5]
	MEETS {
		Deactivate();
	}

	VALUE Deactivate() [1, 5]
	MEETS {
		Idle();
	}

}

COMPONENT CollaborativeProcess {FLEXIBLE process(functional)} : ProcessType;
COMPONENT RobotController {FLEXIBLE controller(functional)} : RobotControllerType;
COMPONENT Human {FLEXIBLE operator(primitive)} : HumanType;
COMPONENT RobotMotion {FLEXIBLE motion(primitive)} : RobotMotionType;
COMPONENT T1 {FLEXIBLE toolT1(primitive)} : RobotToolType;
COMPONENT T2 {FLEXIBLE toolT2(primitive)} : RobotToolType;
COMPONENT T3 {FLEXIBLE toolT3(primitive)} : RobotToolType;

SYNCHRONIZE CollaborativeProcess.process {
	
	VALUE HRC() {
		
		cd0 Human.operator._H4();
		cd1 Human.operator._H7();
		cd2 RobotController.controller.RQ16();
		cd3 RobotController.controller.RQ14();
		
		CONTAINS [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
	}
}


SYNCHRONIZE RobotController.controller {

	VALUE RQ18() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q18;
	}

	VALUE RQ16() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q16;
	}

	VALUE RQ17() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q17;
	}

	VALUE RQ14() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q14;
	}

	VALUE RQ15() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q15;
	}

	VALUE RQ12() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q12;
	}

	VALUE RQ13() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q13;
	}

	VALUE RQ2() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q2;
	}

	VALUE RQ1() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q1;
	}

	VALUE RQ4() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q4;
	}

	VALUE RQ3() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q3;
	}

	VALUE RQ5() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q5;
	}

	VALUE RQ10() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q10;
	}

	VALUE RQ11() {

		cd0 RobotMotion.motion.At(?pos);

		cd1 <!> T3.toolT3.Activate();

		cd2 <!> T3.toolT3.Operating();

		cd3 <!> T3.toolT3.Deactivate();

		DURING [0, +INF] [0, +INF] cd0;
		CONTAINS [0, +INF] [0, +INF] cd1;
		CONTAINS [0, +INF] [0, +INF] cd2;
		CONTAINS [0, +INF] [0, +INF] cd3;
		cd1 MEETS cd2;
		cd2 MEETS cd3;
		?pos = Q11;
	}


}


}


