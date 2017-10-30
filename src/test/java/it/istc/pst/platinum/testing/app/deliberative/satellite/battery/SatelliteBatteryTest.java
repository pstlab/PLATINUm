package it.istc.pst.platinum.testing.app.deliberative.satellite.battery;

/**
 * 
 * @author anacleto
 *
 */
public abstract class SatelliteBatteryTest 
{
	protected static final String DOMAIN_DIRECTORY = "domains/satellite/battery/domains";
	protected static final String PROBLEM_DIRECTORY = "domains/satellite/battery/problems";
	protected static final String PLAN_FOLDER = "domains/satellite/battery/plans";
	protected static final String DATA_FOLDER = "domains/satellite/battery/data";
	protected static final String DDL_PATTERN = "$v/satellite.ddl";
	protected static final String PDL_PATTERN = "satellite_$v_$c_$s_$g.pdl";
	protected static final String[] DOMAIN_VERSIONS = new String[] {
			"complete",
			"reservoir",
			"discrete",
			"simple"
	};
	
}
