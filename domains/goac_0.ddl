DOMAIN GOAC_Domain {

	TEMPORAL_MODULE temporal_module = [0, 20000], 300;

	COMP_TYPE SingletonStateVariable Navigator (At_a(), At_b(), At_c(), GoingTo_a_b(), GoingTo_a_c(), GoingTo_b_c(), GoingTo_b_a(), GoingTo_c_a(), GoingTo_c_b()) {
	
		VALUE At_a() [1, +INF]
		MEETS {
			GoingTo_a_b();
			GoingTo_a_c();
		}

		VALUE At_b() [1, +INF]
		MEETS {
			GoingTo_b_a();
			GoingTo_b_c();
		}

		VALUE At_c() [1, +INF]
		MEETS {
			GoingTo_c_a();
			GoingTo_c_b();
		}

		VALUE GoingTo_a_b() [100, +INF]
		MEETS {
			At_b();
		}

		VALUE GoingTo_b_a() [100, +INF]
		MEETS {
			At_a();
		}

		VALUE GoingTo_a_c() [200, +INF]
		MEETS {
			At_c();
		}

		VALUE GoingTo_c_a() [200, +INF]
		MEETS {
			At_a();
		}

		VALUE GoingTo_b_c() [100, +INF]
		MEETS {
			At_c();
		}

		VALUE GoingTo_c_b() [100, +INF]
		MEETS {
			At_b();
		}
	}

	COMP_TYPE SingletonStateVariable Visibility (Visible(), NotVisible()) {

		VALUE Visible() [1, +INF]
		MEETS {
			NotVisible();
		}

		VALUE NotVisible() [1, +INF]
		MEETS {
			Visible();
		}
	}

	COMP_TYPE SingletonStateVariable Camera (Idle(), TakingPicture_a(), TakingPicture_b(), TakingPicture_c()) {

		VALUE Idle() [1, +INF]
		MEETS {
			TakingPicture_a();
			TakingPicture_b();
			TakingPicture_c();
		}

		VALUE TakingPicture_a() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_b() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_c() [50, 50]
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable Antenna (Idle(), Dumping_a(), Dumping_b(), Dumping_c()) {

		VALUE Idle() [1, +INF]
		MEETS {
			Dumping_a();
			Dumping_b();
			Dumping_c();
		}

		VALUE Dumping_a() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_b() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_c() [240, 240]
		MEETS {
			Idle();
		}
	}

	// ENVIRONMENT'S COMPONENTS
	COMPONENT Nav1 {FLEXIBLE nav1_position(trex_internal_dispatch_asap)} : Navigator;
	COMPONENT Vis1 {FLEXIBLE vis1_visibility(trex_internal_dispatch_asap)} : Visibility;
	COMPONENT Cam1 {FLEXIBLE cam1_camera(trex_internal_dispatch_asap)} : Camera;
	COMPONENT Ant1 {FLEXIBLE ant1_antenna(trex_internal_dispatch_asap)} : Antenna;

	SYNCHRONIZE Cam1.cam1_camera {

		VALUE TakingPicture_a() {
			at Nav1.nav1_position.At_a();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_b() {
			at Nav1.nav1_position.At_b();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_c() {
			at Nav1.nav1_position.At_c();
			DURING [0, +INF] [0, +INF] at;
		}
	}

	SYNCHRONIZE Ant1.ant1_antenna {

		VALUE Dumping_a() {
			tp Cam1.cam1_camera.TakingPicture_a();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_b() {
			tp Cam1.cam1_camera.TakingPicture_b();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_c() {
			tp Cam1.cam1_camera.TakingPicture_c();
			AFTER [0, +INF] tp;
		}
	}
}