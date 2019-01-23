DOMAIN AIJ_EXP_T20_S100_U30 {

	TEMPORAL_MODULE tm = [0, 500], 100;

	COMP_TYPE SingletonStateVariable ProductionSV (Idle(), Assembly()) {

		VALUE Idle() [1, +INF]
		MEETS {
			Assembly();
		}

		VALUE Assembly() [1, +INF]
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable AssemblySV (Holding(), PreparePiece(), RemoveTopCover(), RemovePart(), RemoveBottomCover()) {

		VALUE Holding() [1, +INF]
		MEETS {
			PreparePiece();
			RemoveTopCover();
			RemovePart();
			RemoveBottomCover();
		}

		VALUE PreparePiece() [1, +INF]
		MEETS {
			Holding();
		}

		VALUE RemoveTopCover() [1, +INF]
		MEETS {
			Holding();
		}

		VALUE RemovePart() [1, +INF]
		MEETS {
			Holding();		}

		VALUE RemoveBottomCover() [1, +INF]
		MEETS {
			Holding();
		}
	}

	COMP_TYPE SingletonStateVariable HumanSV (Idle(), _UnscrewTopBolt1(), _UnscrewTopBolt2(), _UnscrewTopBolt3(), _UnscrewTopBolt4(), _UnscrewTopBolt5(), _UnscrewTopBolt6(), _UnscrewTopBolt7(), _UnscrewTopBolt8(), _UnscrewTopBolt9(), _UnscrewTopBolt10(), _UnscrewBottomBolt1(), _UnscrewBottomBolt2(), _UnscrewBottomBolt3(), _UnscrewBottomBolt4(), _UnscrewBottomBolt5(), _UnscrewBottomBolt6(), _UnscrewBottomBolt7(), _UnscrewBottomBolt8(), _UnscrewBottomBolt9(), _UnscrewBottomBolt10(), _SetWorkPiece(), _RemoveWaxPart()) {

		VALUE _SetWorkPiece() [1, 31]
		MEETS {
			Idle();
		}

		VALUE _RemoveWaxPart() [1, 31]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt1() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt2() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt3() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt4() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt5() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt6() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt7() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt8() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt9() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt10() [1, 38]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt1() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt2() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt3() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt4() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt5() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt6() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt7() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt8() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt9() [1, 46]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt10() [1, 46]
		MEETS {
			Idle();
		}

		VALUE Idle() [1, +INF]
		MEETS {
			_UnscrewTopBolt1();
			_UnscrewTopBolt2();
			_UnscrewTopBolt3();
			_UnscrewTopBolt4();
			_UnscrewTopBolt5();
			_UnscrewTopBolt6();
			_UnscrewTopBolt7();
			_UnscrewTopBolt8();
			_UnscrewTopBolt9();
			_UnscrewTopBolt10();
			_UnscrewBottomBolt1();
			_UnscrewBottomBolt2();
			_UnscrewBottomBolt3();
			_UnscrewBottomBolt4();
			_UnscrewBottomBolt5();
			_UnscrewBottomBolt6();
			_UnscrewBottomBolt7();
			_UnscrewBottomBolt8();
			_UnscrewBottomBolt9();
			_UnscrewBottomBolt10();
			_SetWorkPiece();
			_RemoveWaxPart();
		}
	}

	COMP_TYPE SingletonStateVariable RobotSV (UnscrewTopBolt1(), UnscrewTopBolt2(), UnscrewTopBolt3(), UnscrewTopBolt4(), UnscrewTopBolt5(), UnscrewTopBolt6(), UnscrewTopBolt7(), UnscrewTopBolt8(), UnscrewTopBolt9(), UnscrewTopBolt10(), UnscrewBottomBolt1(), UnscrewBottomBolt2(), UnscrewBottomBolt3(), UnscrewBottomBolt4(), UnscrewBottomBolt5(), UnscrewBottomBolt6(), UnscrewBottomBolt7(), UnscrewBottomBolt8(), UnscrewBottomBolt9(), UnscrewBottomBolt10(), Idle()) {

		VALUE UnscrewTopBolt1() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt2() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt3() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt4() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt5() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt6() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt7() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt8() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt9() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewTopBolt10() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt1() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt2() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt3() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt4() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt5() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt6() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt7() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt8() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt9() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE UnscrewBottomBolt10() [1, +INF]
		MEETS {
			Idle();
		}

		VALUE Idle() [1, +INF]
		MEETS {
			UnscrewTopBolt1();
			UnscrewTopBolt2();
			UnscrewTopBolt3();
			UnscrewTopBolt4();
			UnscrewTopBolt5();
			UnscrewTopBolt6();
			UnscrewTopBolt7();
			UnscrewTopBolt8();
			UnscrewTopBolt9();
			UnscrewTopBolt10();
			UnscrewBottomBolt1();
			UnscrewBottomBolt2();
			UnscrewBottomBolt3();
			UnscrewBottomBolt4();
			UnscrewBottomBolt5();
			UnscrewBottomBolt6();
			UnscrewBottomBolt7();
			UnscrewBottomBolt8();
			UnscrewBottomBolt9();
			UnscrewBottomBolt10();
		}
	}

	COMP_TYPE SingletonStateVariable RoboticArmSV (SetOnTopBolt1(), SetOnTopBolt2(), SetOnTopBolt3(), SetOnTopBolt4(), SetOnTopBolt5(), SetOnTopBolt6(), SetOnTopBolt7(), SetOnTopBolt8(), SetOnTopBolt9(), SetOnTopBolt10(), SetOnBottomBolt1(), SetOnBottomBolt2(), SetOnBottomBolt3(), SetOnBottomBolt4(), SetOnBottomBolt5(), SetOnBottomBolt6(), SetOnBottomBolt7(), SetOnBottomBolt8(), SetOnBottomBolt9(), SetOnBottomBolt10(), SetOnBase(), Moving()) {

		VALUE SetOnBase() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt1() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt2() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt3() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt4() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt5() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt6() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt7() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt8() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt9() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnTopBolt10() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt1() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt2() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt3() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt4() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt5() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt6() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt7() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt8() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt9() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE SetOnBottomBolt10() [1, +INF]
		MEETS {
			Moving();
		}

		VALUE Moving() [3, 5]
		MEETS {
			SetOnBase();
			SetOnTopBolt1();
			SetOnTopBolt2();
			SetOnTopBolt3();
			SetOnTopBolt4();
			SetOnTopBolt5();
			SetOnTopBolt6();
			SetOnTopBolt7();
			SetOnTopBolt8();
			SetOnTopBolt9();
			SetOnTopBolt10();
			SetOnBottomBolt1();
			SetOnBottomBolt2();
			SetOnBottomBolt3();
			SetOnBottomBolt4();
			SetOnBottomBolt5();
			SetOnBottomBolt6();
			SetOnBottomBolt7();
			SetOnBottomBolt8();
			SetOnBottomBolt9();
			SetOnBottomBolt10();
		}
	}

	COMP_TYPE SingletonStateVariable ToolSV (Idle(), rUnscrewBolt()) {

		VALUE Idle() [1, +INF]
		MEETS {
			rUnscrewBolt();
		}

		VALUE rUnscrewBolt() [3, 3]
		MEETS {
			Idle();
		}

	}

	COMPONENT Production {FLEXIBLE process(functional)} : ProductionSV;
	COMPONENT Assembly {FLEXIBLE hrc(functional)} : AssemblySV;
	COMPONENT Human {FLEXIBLE operator(primitive)} : HumanSV;
	COMPONENT Robot {FLEXIBLE cobot(functional)} : RobotSV;
	COMPONENT Arm {FLEXIBLE motions(primitive)} : RoboticArmSV;
	COMPONENT Tool {FLEXIBLE screwdriver(primitive)} : ToolSV;


	SYNCHRONIZE Production.process {

		VALUE Assembly() {

			task1 <!> Assembly.hrc.PreparePiece();
			task2 <!> Assembly.hrc.RemoveTopCover();
			task3 <!> Assembly.hrc.RemovePart();
			task4 <!> Assembly.hrc.RemoveBottomCover();

			task1 BEFORE [0, +INF] task2;
			task2 BEFORE [0, +INF] task3;
			task3 BEFORE [0, +INF] task4;

			CONTAINS [0, +INF] [0, +INF] task1;
			CONTAINS [0, +INF] [0, +INF] task2;
			CONTAINS [0, +INF] [0, +INF] task3;
			CONTAINS [0, +INF] [0, +INF] task4;

		}
	}

	SYNCHRONIZE Assembly.hrc {

		VALUE PreparePiece() {

			task0 <!> Human.operator._SetWorkPiece();
			CONTAINS [0, +INF] [0, +INF] task0;
		}

		VALUE RemovePart() {

			task0 <!> Human.operator._RemoveWaxPart();
			CONTAINS [0, +INF] [0, +INF] task0;
		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Human.operator._UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Human.operator._UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Human.operator._UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Human.operator._UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Robot.cobot.UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveTopCover() {

			task1 <!> Robot.cobot.UnscrewTopBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Robot.cobot.UnscrewTopBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewTopBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewTopBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewTopBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewTopBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewTopBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewTopBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewTopBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Human.operator._UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Human.operator._UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Human.operator._UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Human.operator._UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Human.operator._UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Human.operator._UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Human.operator._UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Human.operator._UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Human.operator._UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Human.operator._UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Robot.cobot.UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

		VALUE RemoveBottomCover() {

			task1 <!> Robot.cobot.UnscrewBottomBolt1();
			CONTAINS [0, +INF] [0, +INF] task1;

			task2 <!> Robot.cobot.UnscrewBottomBolt2();
			CONTAINS [0, +INF] [0, +INF] task2;

			task3 <!> Robot.cobot.UnscrewBottomBolt3();
			CONTAINS [0, +INF] [0, +INF] task3;

			task4 <!> Robot.cobot.UnscrewBottomBolt4();
			CONTAINS [0, +INF] [0, +INF] task4;

			task5 <!> Robot.cobot.UnscrewBottomBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

			task6 <!> Robot.cobot.UnscrewBottomBolt6();
			CONTAINS [0, +INF] [0, +INF] task6;

			task7 <!> Robot.cobot.UnscrewBottomBolt7();
			CONTAINS [0, +INF] [0, +INF] task7;

			task8 <!> Robot.cobot.UnscrewBottomBolt8();
			CONTAINS [0, +INF] [0, +INF] task8;

			task9 <!> Robot.cobot.UnscrewBottomBolt9();
			CONTAINS [0, +INF] [0, +INF] task9;

			task10 <!> Robot.cobot.UnscrewBottomBolt10();
			CONTAINS [0, +INF] [0, +INF] task10;

		}

	}

	SYNCHRONIZE Robot.cobot {

		VALUE UnscrewTopBolt1() {

			p0 Arm.motions.SetOnTopBolt1();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt2() {

			p0 Arm.motions.SetOnTopBolt2();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt3() {

			p0 Arm.motions.SetOnTopBolt3();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt4() {

			p0 Arm.motions.SetOnTopBolt4();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt5() {

			p0 Arm.motions.SetOnTopBolt5();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt6() {

			p0 Arm.motions.SetOnTopBolt6();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt7() {

			p0 Arm.motions.SetOnTopBolt7();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt8() {

			p0 Arm.motions.SetOnTopBolt8();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt9() {

			p0 Arm.motions.SetOnTopBolt9();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewTopBolt10() {

			p0 Arm.motions.SetOnTopBolt10();
			DURING [0, +INF] [0, +INF] p0;

			t0 <!> Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt1() {

			p0 Arm.motions.SetOnBottomBolt1();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt2() {

			p0 Arm.motions.SetOnBottomBolt2();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt3() {

			p0 Arm.motions.SetOnBottomBolt3();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt4() {

			p0 Arm.motions.SetOnBottomBolt4();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt5() {

			p0 Arm.motions.SetOnBottomBolt5();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt6() {

			p0 Arm.motions.SetOnBottomBolt6();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt7() {

			p0 Arm.motions.SetOnBottomBolt7();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt8() {

			p0 Arm.motions.SetOnBottomBolt8();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt9() {

			p0 Arm.motions.SetOnBottomBolt9();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

		VALUE UnscrewBottomBolt10() {

			p0 Arm.motions.SetOnBottomBolt10();
			DURING [0, +INF] [0, +INF] p0;

			t0 Tool.screwdriver.rUnscrewBolt();
			CONTAINS [0, +INF] [0, +INF] t0;
		}

	}

}
