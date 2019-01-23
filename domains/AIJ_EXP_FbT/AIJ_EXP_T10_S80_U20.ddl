DOMAIN AIJ_EXP_T10_S80_U20 {

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

	COMP_TYPE SingletonStateVariable HumanSV (Idle(), _UnscrewTopBolt1(), _UnscrewTopBolt2(), _UnscrewTopBolt3(), _UnscrewTopBolt4(), _UnscrewTopBolt5(), _UnscrewBottomBolt1(), _UnscrewBottomBolt2(), _UnscrewBottomBolt3(), _UnscrewBottomBolt4(), _UnscrewBottomBolt5(), _SetWorkPiece(), _RemoveWaxPart()) {

		VALUE _SetWorkPiece() [1, 21]
		MEETS {
			Idle();
		}

		VALUE _RemoveWaxPart() [1, 21]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt1() [1, 28]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt2() [1, 28]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt3() [1, 28]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt4() [1, 28]
		MEETS {
			Idle();
		}

		VALUE _UnscrewTopBolt5() [1, 28]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt1() [1, 36]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt2() [1, 36]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt3() [1, 36]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt4() [1, 36]
		MEETS {
			Idle();
		}

		VALUE _UnscrewBottomBolt5() [1, 36]
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
			_UnscrewBottomBolt1();
			_UnscrewBottomBolt2();
			_UnscrewBottomBolt3();
			_UnscrewBottomBolt4();
			_UnscrewBottomBolt5();
			_SetWorkPiece();
			_RemoveWaxPart();
		}
	}

	COMP_TYPE SingletonStateVariable RobotSV (UnscrewTopBolt1(), UnscrewTopBolt2(), UnscrewTopBolt3(), UnscrewTopBolt4(), UnscrewTopBolt5(), UnscrewBottomBolt1(), UnscrewBottomBolt2(), UnscrewBottomBolt3(), UnscrewBottomBolt4(), UnscrewBottomBolt5(), Idle()) {

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

		VALUE Idle() [1, +INF]
		MEETS {
			UnscrewTopBolt1();
			UnscrewTopBolt2();
			UnscrewTopBolt3();
			UnscrewTopBolt4();
			UnscrewTopBolt5();
			UnscrewBottomBolt1();
			UnscrewBottomBolt2();
			UnscrewBottomBolt3();
			UnscrewBottomBolt4();
			UnscrewBottomBolt5();
		}
	}

	COMP_TYPE SingletonStateVariable RoboticArmSV (SetOnTopBolt1(), SetOnTopBolt2(), SetOnTopBolt3(), SetOnTopBolt4(), SetOnTopBolt5(), SetOnBottomBolt1(), SetOnBottomBolt2(), SetOnBottomBolt3(), SetOnBottomBolt4(), SetOnBottomBolt5(), SetOnBase(), Moving()) {

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

		VALUE Moving() [3, 5]
		MEETS {
			SetOnBase();
			SetOnTopBolt1();
			SetOnTopBolt2();
			SetOnTopBolt3();
			SetOnTopBolt4();
			SetOnTopBolt5();
			SetOnBottomBolt1();
			SetOnBottomBolt2();
			SetOnBottomBolt3();
			SetOnBottomBolt4();
			SetOnBottomBolt5();
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

			task5 <!> Robot.cobot.UnscrewTopBolt5();
			CONTAINS [0, +INF] [0, +INF] task5;

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

	}

}
