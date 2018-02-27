DOMAIN GOAC_Domain {

	TEMPORAL_MODULE temporal_module = [0, 20000], 300;

	COMP_TYPE SingletonStateVariable Navigator (At_a(), At_b(), At_c(), At_d(), At_e(), At_f(), At_g(), At_h(), At_i(), At_j(), GoingTo_a_b(), GoingTo_a_c(), GoingTo_a_e(), GoingTo_a_f(), GoingTo_a_j(), GoingTo_b_c(), GoingTo_b_g(), GoingTo_c_d(), GoingTo_c_h(), GoingTo_c_i(), GoingTo_d_e(), GoingTo_d_h(), GoingTo_d_i(), GoingTo_e_f(), GoingTo_e_j(), GoingTo_f_g(), GoingTo_f_h(), GoingTo_f_j(), GoingTo_g_h(), GoingTo_h_i(), GoingTo_i_j(), GoingTo_b_a(), GoingTo_c_a(), GoingTo_e_a(), GoingTo_f_a(), GoingTo_j_a(), GoingTo_c_b(), GoingTo_g_b(), GoingTo_d_c(), GoingTo_h_c(), GoingTo_i_c(), GoingTo_e_d(), GoingTo_h_d(), GoingTo_i_d(), GoingTo_f_e(), GoingTo_j_e(), GoingTo_g_f(), GoingTo_h_f(), GoingTo_j_f(), GoingTo_h_g(), GoingTo_i_h(), GoingTo_j_i()) {
	
		VALUE At_a() [1, +INF]
		MEETS {
			GoingTo_a_b();
			GoingTo_a_c();
			GoingTo_a_e();
			GoingTo_a_f();
			GoingTo_a_j();
		}

		VALUE At_b() [1, +INF]
		MEETS {
			GoingTo_b_a();
			GoingTo_b_c();
			GoingTo_b_g();
		}

		VALUE At_c() [1, +INF]
		MEETS {
			GoingTo_c_a();
			GoingTo_c_b();
			GoingTo_c_d();
			GoingTo_c_h();
			GoingTo_c_i();
		}

		VALUE At_d() [1, +INF]
		MEETS {
			GoingTo_g_b();
			GoingTo_g_f();
			GoingTo_d_e();
			GoingTo_d_h();
			GoingTo_d_i();
		}

		VALUE At_e() [1, +INF]
		MEETS {
			GoingTo_e_a();
			GoingTo_e_d();
			GoingTo_e_f();
			GoingTo_e_j();
		}

		VALUE At_f() [1, +INF]
		MEETS {
			GoingTo_f_a();
			GoingTo_f_e();
			GoingTo_f_g();
			GoingTo_f_h();
			GoingTo_f_j();
		}

		VALUE At_g() [1, +INF]
		MEETS {
			GoingTo_g_b();
			GoingTo_g_f();
			GoingTo_g_h();
		}

		VALUE At_h() [1, +INF]
		MEETS {
			GoingTo_h_i();
			GoingTo_h_c();
			GoingTo_h_d();
			GoingTo_h_f();
			GoingTo_h_g();
		}

		VALUE At_i() [1, +INF]
		MEETS {
			GoingTo_i_c();
			GoingTo_i_d();
			GoingTo_i_h();
			GoingTo_i_j();
		}

		VALUE At_j() [1, +INF]
		MEETS {
			GoingTo_j_a();
			GoingTo_j_e();
			GoingTo_j_f();
			GoingTo_j_i();
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

		VALUE GoingTo_a_e() [100, +INF]
		MEETS {
			At_e();
		}

		VALUE GoingTo_e_a() [100, +INF]
		MEETS {
			At_a();
		}

		VALUE GoingTo_a_f() [200, +INF]
		MEETS {
			At_f();
		}

		VALUE GoingTo_f_a() [200, +INF]
		MEETS {
			At_a();
		}

		VALUE GoingTo_a_j() [300, +INF]
		MEETS {
			At_j();
		}

		VALUE GoingTo_j_a() [300, +INF]
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

		VALUE GoingTo_b_g() [200, +INF]
		MEETS {
			At_g();
		}

		VALUE GoingTo_g_b() [200, +INF]
		MEETS {
			At_b();
		}

		VALUE GoingTo_c_d() [100, +INF]
		MEETS {
			At_d();
		}

		VALUE GoingTo_d_c() [100, +INF]
		MEETS {
			At_c();
		}

		VALUE GoingTo_c_h() [200, +INF]
		MEETS {
			At_h();
		}

		VALUE GoingTo_h_c() [200, +INF]
		MEETS {
			At_c();
		}

		VALUE GoingTo_c_i() [300, +INF]
		MEETS {
			At_i();
		}

		VALUE GoingTo_i_c() [300, +INF]
		MEETS {
			At_c();
		}

		VALUE GoingTo_d_e() [200, +INF]
		MEETS {
			At_e();
		}

		VALUE GoingTo_e_d() [200, +INF]
		MEETS {
			At_d();
		}

		VALUE GoingTo_d_h() [100, +INF]
		MEETS {
			At_h();
		}

		VALUE GoingTo_h_d() [100, +INF]
		MEETS {
			At_d();
		}

		VALUE GoingTo_d_i() [200, +INF]
		MEETS {
			At_i();
		}

		VALUE GoingTo_i_d() [200, +INF]
		MEETS {
			At_d();
		}

		VALUE GoingTo_e_f() [100, +INF]
		MEETS {
			At_f();
		}

		VALUE GoingTo_f_e() [100, +INF]
		MEETS {
			At_e();
		}

		VALUE GoingTo_e_j() [200, +INF]
		MEETS {
			At_j();
		}

		VALUE GoingTo_j_e() [200, +INF]
		MEETS {
			At_e();
		}

		VALUE GoingTo_f_g() [100, +INF]
		MEETS {
			At_g();
		}

		VALUE GoingTo_g_f() [100, +INF]
		MEETS {
			At_f();
		}

		VALUE GoingTo_f_h() [200, +INF]
		MEETS {
			At_h();
		}

		VALUE GoingTo_h_f() [200, +INF]
		MEETS {
			At_f();
		}

		VALUE GoingTo_f_j() [100, +INF]
		MEETS {
			At_j();
		}

		VALUE GoingTo_j_f() [100, +INF]
		MEETS {
			At_f();
		}

		VALUE GoingTo_g_h() [100, +INF]
		MEETS {
			At_h();
		}

		VALUE GoingTo_h_g() [100, +INF]
		MEETS {
			At_g();
		}

		VALUE GoingTo_h_i() [100, +INF]
		MEETS {
			At_i();
		}

		VALUE GoingTo_i_h() [100, +INF]
		MEETS {
			At_h();
		}

		VALUE GoingTo_i_j() [200, +INF]
		MEETS {
			At_j();
		}

		VALUE GoingTo_j_i() [200, +INF]
		MEETS {
			At_i();
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

	COMP_TYPE SingletonStateVariable Camera (Idle(), TakingPicture_a(), TakingPicture_b(), TakingPicture_c(), TakingPicture_d(), TakingPicture_e(), TakingPicture_f(), TakingPicture_g(), TakingPicture_h(), TakingPicture_i(), TakingPicture_j()) {

		VALUE Idle() [1, +INF]
		MEETS {
			TakingPicture_a();
			TakingPicture_b();
			TakingPicture_c();
			TakingPicture_d();
			TakingPicture_e();
			TakingPicture_f();
			TakingPicture_g();
			TakingPicture_h();
			TakingPicture_i();
			TakingPicture_j();
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

		VALUE TakingPicture_d() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_e() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_f() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_g() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_h() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_i() [50, 50]
		MEETS {
			Idle();
		}

		VALUE TakingPicture_j() [50, 50]
		MEETS {
			Idle();
		}
	}

	COMP_TYPE SingletonStateVariable Antenna (Idle(), Dumping_a(), Dumping_b(), Dumping_c(), Dumping_d(), Dumping_e(), Dumping_f(), Dumping_g(), Dumping_h(), Dumping_i(), Dumping_j()) {

		VALUE Idle() [1, +INF]
		MEETS {
			Dumping_a();
			Dumping_b();
			Dumping_c();
			Dumping_d();
			Dumping_e();
			Dumping_f();
			Dumping_g();
			Dumping_h();
			Dumping_i();
			Dumping_j();
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

		VALUE Dumping_d() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_e() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_f() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_g() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_h() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_i() [240, 240]
		MEETS {
			Idle();
		}

		VALUE Dumping_j() [240, 240]
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

		VALUE TakingPicture_d() {
			at Nav1.nav1_position.At_d();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_e() {
			at Nav1.nav1_position.At_e();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_f() {
			at Nav1.nav1_position.At_f();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_g() {
			at Nav1.nav1_position.At_g();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_h() {
			at Nav1.nav1_position.At_h();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_i() {
			at Nav1.nav1_position.At_i();
			DURING [0, +INF] [0, +INF] at;
		}

		VALUE TakingPicture_j() {
			at Nav1.nav1_position.At_j();
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

		VALUE Dumping_d() {
			tp Cam1.cam1_camera.TakingPicture_d();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_e() {
			tp Cam1.cam1_camera.TakingPicture_e();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_f() {
			tp Cam1.cam1_camera.TakingPicture_f();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_g() {
			tp Cam1.cam1_camera.TakingPicture_g();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_h() {
			tp Cam1.cam1_camera.TakingPicture_h();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_i() {
			tp Cam1.cam1_camera.TakingPicture_i();
			AFTER [0, +INF] tp;
		}

		VALUE Dumping_j() {
			tp Cam1.cam1_camera.TakingPicture_j();
			AFTER [0, +INF] tp;
		}
	}
}