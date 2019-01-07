package it.istc.pst.platinum.testing.aij;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 
 * @author anacleto
 *
 */
public class AIJFourByThreeDomainGenerator 
{
	// domain name
	private String domainFolder;
	private int[] domainTasks;
	private int[] domainSharedTasks;
	private int[] domainUncertaintyVariability;
	private int domainHorizon;
	
	/**
	 * 
	 * @param domainFolder
	 */
	protected AIJFourByThreeDomainGenerator(
			String domainFolder,
			int[] domainTasks,
			int[] domainSharedTasks,
			int[] domainUcertaintyVariability,
			int domainHorizion) 
	{
		// set generator fields
		this.domainFolder = domainFolder;
		this.domainTasks = domainTasks;
		this.domainSharedTasks = domainSharedTasks;
		this.domainUncertaintyVariability = domainUcertaintyVariability;
		this.domainHorizon = domainHorizion;
	}
	
	/**
	 * 
	 */
	public void generate()
	{
		// iterate over domain parameters
		for (int task : this.domainTasks) 
		{
			for (int shared : this.domainSharedTasks) 
			{
				for (int uncertainty : this.domainUncertaintyVariability) 
				{
					try
					{
						// set domain name
						String domainName = "AIJ_EXP_T" + task  + "_S" + shared + "_U" + uncertainty;
						// prepare domain specification file
						String ddl = "DOMAIN " + domainName + " {\n\n"
								+ "\tTEMPORAL_MODULE tm = [0, " + this.domainHorizon + "], 100;\n\n";
						
						// model HRC processes 
						ddl += prepareProductionSV(); 
						
						// model the HRC assembly process
						ddl += prepareAssemblySV();
						
						// get number of task concerning the top cover
						int topTasks = task / 2;
						int bottomTasks = task - topTasks;
						// model human capabilities
						ddl += prepareHumanSV(topTasks, bottomTasks, uncertainty);
						// model robot capabilities
						ddl += prepareRobotSV(topTasks, bottomTasks);
						// model robot arm motions
						ddl += prepareRoboticArmSV(topTasks, bottomTasks);
						// model robot tool configuration
						ddl += prepareRobotToolConfigurationSV();
						// model tool 
						ddl += prepareToolSV();
						
						// model domain components
						ddl += prepareDomainComponents();

						// model production synchronization rules
						ddl += prepareProductionRules();
						// get shared operations for the remove top cover
						int sharedTopTasks = (shared * topTasks) / 100;
						// get shared operations for the remove bottom cover
						int sharedBottomTasks = (shared * bottomTasks) / 100;
						// model HRC coordination rules for the task "RemoveTopCover" and the task "RemoveBottomCover"
						ddl += prepareHRCRules(topTasks, sharedTopTasks, bottomTasks, sharedBottomTasks);
						// model robot task internal coordination
						ddl += prepareRobotRules(topTasks, bottomTasks);
						// model robot tool change rules
						ddl += prepareToolChangeRules();
						// close domain description
						ddl += "}\n";
						
						// prepare domain specification file
						File ddlFile = new File(this.domainFolder + "/" + domainName + ".ddl");
						try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ddlFile), "UTF-8"))) {
							// write file 
							writer.write(ddl);
						}
						
						// prepare problem specification file 
						String pdl = prepareProblemDescription(domainName);
						File pdlFile = new File(this.domainFolder + "/" + domainName + ".pdl");
						try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pdlFile), "UTF-8"))) {
							// write file
							writer.write(pdl);
						}
					}
					catch (IOException ex) {
						System.err.println(ex.getMessage());
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private String prepareProductionSV() 
	{
		// return SV description
		return "\tCOMP_TYPE SingletonStateVariable ProductionSV (Idle(), Assembly()) {\n\n"
				+ "\t\tVALUE Idle() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tAssembly();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE Assembly() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tIdle();\n"
				+ "\t\t}\n"
				+ "\t}\n\n";
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private String prepareAssemblySV() 
	{
		// return SV description
		return "\tCOMP_TYPE SingletonStateVariable AssemblySV (Holding(), PreparePiece(), RemoveTopCover(), RemovePart(), RemoveBottomCover()) {\n\n"
				+ "\t\tVALUE Holding() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tPreparePiece();\n"
				+ "\t\t\tRemoveTopCover();\n"
				+ "\t\t\tRemovePart();\n"
				+ "\t\t\tRemoveBottomCover();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE PreparePiece() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tHolding();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE RemoveTopCover() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tHolding();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE RemovePart() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tHolding();"
				+ "\t\t}\n\n"
				+ "\t\tVALUE RemoveBottomCover() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tHolding();\n"
				+ "\t\t}\n"
				+ "\t}\n\n";
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private String prepareHumanSV(int topTask, int bottomTask, int uncertainty) {
		// prepare list of human tasks
		String HTaskList = "Idle(), ";
		for (int i = 1; i <= topTask; i++) {
			HTaskList += "_UnscrewTopBolt" + i + "(), ";
		}
		for (int i = 1; i <= bottomTask; i++) {
			HTaskList += "_UnscrewBottomBolt" + i + "(), ";
		}
		HTaskList += "_SetWorkPiece(), _MountTool1(), _MountTool2(), _RemoveWaxPart()";
		
		// model human operator capabilities
		String ddl = "\tCOMP_TYPE SingletonStateVariable HumanSV (" + HTaskList + ") {\n\n"
				+ "\t\tVALUE _SetWorkPiece() [7, " + (7 + uncertainty) + "]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tIdle();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE _MountTool1() [11, " + (11 + uncertainty) + "]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tIdle();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE _MountTool2() [7, " + (7 + uncertainty) + "]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tIdle();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE _RemoveWaxPart() [18, " + (18 + uncertainty) + "]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tIdle();\n"
				+ "\t\t}\n\n";
		
		
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\tVALUE _UnscrewTopBolt" + i + "() [3, " + (3 + uncertainty) + "]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tIdle();\n"
					+ "\t\t}\n\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\tVALUE _UnscrewBottomBolt" + i + "() [6, " + (6 + uncertainty) + "]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tIdle();\n"
					+ "\t\t}\n\n";
		}
		
		ddl += "\t\tVALUE Idle() [1, +INF]\n"
				+ "\t\tMEETS {\n";
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\t\t_UnscrewTopBolt" + i + "();\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\t\t_UnscrewBottomBolt" + i + "();\n";
		}
		
		ddl += "\t\t\t_SetWorkPiece();\n"
				+ "\t\t\t_MountTool1();\n"
				+ "\t\t\t_MountTool2();\n"
				+ "\t\t\t_RemoveWaxPart();\n"
				+ "\t\t}\n";
		ddl += "\t}\n\n";
		// get description
		return ddl;
	}
	
	
	
	/**
	 */
	private String prepareRobotSV(int topTask, int bottomTask) {
		// prepare list of human tasks
		String RTaskList = "";
		for (int i = 1; i <= topTask; i++) {
			RTaskList += "UnscrewTopBolt" + i + "(), ";
		}
		for (int i = 1; i <= bottomTask; i++) {
			RTaskList += "UnscrewBottomBolt" + i + "(), ";
		}
		RTaskList += "Idle()";
		
		// model human operator capabilities
		String ddl = "\tCOMP_TYPE SingletonStateVariable RobotSV (" + RTaskList + ") {\n\n";
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\tVALUE UnscrewTopBolt" + i + "() [1, +INF]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tIdle();\n"
					+ "\t\t}\n\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\tVALUE UnscrewBottomBolt" + i + "() [1, +INF]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tIdle();\n"
					+ "\t\t}\n\n";
		}
		
		ddl += "\t\tVALUE Idle() [1, +INF]\n"
				+ "\t\tMEETS {\n";
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\t\tUnscrewTopBolt" + i + "();\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\t\tUnscrewBottomBolt" + i + "();\n";
		}
		
		ddl += "\t\t}\n";
		ddl += "\t}\n\n";
		// get description
		return ddl;
	}
	
	
	/**
	 * 
	 * @param topTask
	 * @param bottomTask
	 * @return
	 */
	private String prepareRoboticArmSV(int topTask, int bottomTask) {
		// prepare list of human tasks
		String ArmTaskList = "";
		for (int i = 1; i <= topTask; i++) {
			ArmTaskList += "SetOnTopBolt" + i + "(), ";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ArmTaskList += "SetOnBottomBolt" + i + "(), ";
		}
		ArmTaskList += "SetOnBase(), Moving()";
		
		// model human operator capabilities
		String ddl = "\tCOMP_TYPE SingletonStateVariable RoboticArmSV (" + ArmTaskList + ") {\n\n"
				+ "\t\tVALUE SetOnBase() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tMoving();\n"
				+ "\t\t}\n\n";
		
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\tVALUE SetOnTopBolt" + i + "() [1, +INF]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tMoving();\n"
					+ "\t\t}\n\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\tVALUE SetOnBottomBolt" + i + "() [1, +INF]\n"
					+ "\t\tMEETS {\n"
					+ "\t\t\tMoving();\n"
					+ "\t\t}\n\n";
		}
		
		ddl += "\t\tVALUE Moving() [5, 45]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tSetOnBase();\n";
		for (int i = 1; i <= topTask; i++) {
			ddl += "\t\t\tSetOnTopBolt" + i + "();\n";
		}
		for (int i = 1; i <= bottomTask; i++) {
			ddl += "\t\t\tSetOnBottomBolt" + i + "();\n";
		}
		
		ddl += "\t\t}\n";
		ddl += "\t}\n\n";
		// get description
		return ddl;
	}
	
	/**
	 * 
	 * @return
	 */
	private String prepareRobotToolConfigurationSV()
	{
		// return SV description
		return "\tCOMP_TYPE SingletonStateVariable RobotToolConfigurationSV (None(), Tool1Mounted(), Tool2Mounted()) {\n\n"
				+ "\t\tVALUE None() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tTool1Mounted();\n"
				+ "\t\t\tTool2Mounted();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE Tool1Mounted() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tNone();\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE Tool2Mounted() [1, +INF]\n"
				+ "\t\tMEETS {\n"
				+ "\t\t\tNone();\n"
				+ "\t\t}\n\n"
				+ "\t}\n\n";
	}
	
	/**
	 * 
	 * @return
	 */
	private String prepareToolSV()
	{
		// return SV description
				return "\tCOMP_TYPE SingletonStateVariable ToolSV (Idle(), UnscrewBolt()) {\n\n"
						+ "\t\tVALUE Idle() [1, +INF]\n"
						+ "\t\tMEETS {\n"
						+ "\t\t\tUnscrewBolt();\n"
						+ "\t\t}\n\n"
						+ "\t\tVALUE UnscrewBolt() [7, 7]\n"
						+ "\t\tMEETS {\n"
						+ "\t\t\tIdle();\n"
						+ "\t\t}\n\n"
						+ "\t}\n\n";
	}
	
	/**
	 * 
	 * @return
	 */
	private String prepareDomainComponents() {
		// return component declaration
		return "\tCOMPONENT Production {FLEXIBLE process(functional)} : ProductionSV;\n"
				+ "\tCOMPONENT Assembly {FLEXIBLE hrc(functional)} : AssemblySV;\n"
				+ "\tCOMPONENT Human {FLEXIBLE operator(primitive)} : HumanSV;\n"
				+ "\tCOMPONENT Robot {FLEXIBLE cobot(functional)} : RobotSV;\n"
				+ "\tCOMPONENT Arm {FLEXIBLE motions(primitive)} : RoboticArmSV;\n"
				+ "\tCOMPONENT RobotTool {FLEXIBLE configuration(primitive)} : RobotToolConfigurationSV;\n"
				+ "\tCOMPONENT Tool1 {FLEXIBLE t1(primitive)} : ToolSV;\n"
				+ "\tCOMPONENT Tool2 {FLEXIBLE t2(primitive)} : ToolSV;\n"
				+ "\n\n";
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private String prepareProductionRules() {
		// return production constraints
		return "\tSYNCHRONIZE Production.process {\n\n"
				+ "\t\tVALUE Assembly() {\n\n"
				+ "\t\t\ttask1 <!> Assembly.hrc.PreparePiece();\n"
				+ "\t\t\ttask2 <!> Assembly.hrc.RemoveTopCover();\n"
				+ "\t\t\ttask3 <!> Assembly.hrc.RemovePart();\n"
				+ "\t\t\ttask4 <!> Assembly.hrc.RemoveBottomCover();\n\n"
				+ "\t\t\ttask1 BEFORE [0, +INF] task2;\n"
				+ "\t\t\ttask2 BEFORE [0, +INF] task3;\n"
				+ "\t\t\ttask3 BEFORE [0, +INF] task4;\n\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task1;\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task2;\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task3;\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task4;\n\n"
				+ "\t\t}\n"
				+ "\t}\n\n";
	}
	
	
	/**
	 * 
	 * @param tasks
	 * @param shared
	 * @return
	 */
	private String prepareHRCRules(int topTasks, int topShared, int bottomTasks, int bottomShared) 
	{
		// prepare domain description
		String ddl = "\tSYNCHRONIZE Assembly.hrc {\n\n"
				+ "\t\tVALUE PreparePiece() {\n\n"
				+ "\t\t\ttask0 <!> Human.operator._SetWorkPiece();\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task0;\n\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE RemovePart() {\n\n"
				+ "\t\t\ttask0 <!> Human.operator._RemoveWaxPart();\n"
				+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task0;\n\n"
				+ "\t\t}\n\n";
		
		// take into account possible sharing possibility for the task "RemoveTopCover" 
		for (int choice = 1; choice <= topShared; choice++)
		{
			// domain description
			ddl += "\t\tVALUE RemoveTopCover() {\n\n";
			
			// get number of "human only" tasks
			int hOnly = topTasks - choice;
			for (int i = 1; i <= hOnly; i++) {
				ddl+= "\t\t\ttask" + i + " <!> Human.operator._UnscrewTopBolt" + i + "();\n"
						+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task" + i + ";\n\n";
			}
			
			for (int i = hOnly + 1; i <= topTasks; i++) {
				ddl += "\t\t\ttask" + i + " <!> Robot.cobot.UnscrewTopBolt" + i + "();\n"
						+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task" + i + ";\n\n";
			}
		
			ddl += "\t\t}\n\n";
			
		}
		
		
		// take into account possible sharing possibility for the task "RemoveBottomCover" 
		for (int choice = 1; choice <= bottomShared; choice++)
		{
			// domain description
			ddl += "\t\tVALUE RemoveBottomCover() {\n\n";
			
			// get number of "human only" tasks
			int hOnly = bottomTasks - choice;
			for (int i = 1; i <= hOnly; i++) {
				ddl+= "\t\t\ttask" + i + " <!> Human.operator._UnscrewBottomBolt" + i + "();\n"
						+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task" + i + ";\n\n";
			}
			
			for (int i = hOnly + 1; i <= bottomTasks; i++) {
				ddl += "\t\t\ttask" + i + " <!> Robot.cobot.UnscrewBottomBolt" + i + "();\n"
						+ "\t\t\tCONTAINS [0, +INF] [0, +INF] task" + i + ";\n\n";
			}
		
			ddl += "\t\t}\n\n";
			
		}
		
		// return task coordination rules
		ddl += "\t}\n\n";
		return ddl;
	}
	
	
	/**
	 * 
	 * @param topTasks
	 * @param bottomTasks
	 * @return
	 */
	private String prepareRobotRules(int topTasks, int bottomTasks)
	{
		// prepare domain description
		String ddl = "\tSYNCHRONIZE Robot.cobot {\n\n";
		
		for (int i = 1; i <= topTasks; i++)
		{
			ddl += "\t\tVALUE UnscrewTopBolt" + i + "() {\n\n"
					+ "\t\t\tp0 Arm.motions.SetOnTopBolt" + i + "();\n"
					+ "\t\t\tDURING [0, +INF] [0, +INF] p0;\n\n"
					+ "\t\t\tt0 RobotTool.configuration.Tool1Mounted();\n"
					+ "\t\t\tDURING [0, +INF] [0, +INF] t0;\n\n"
					+ "\t\t\to0 Tool1.t1.UnscrewBolt();\n"
					+ "\t\t\tCONTAINS [0, +INF] [0, +INF] o0;\n"
					+ "\t\t}\n\n";
		}
		
		for (int i = 1; i <= bottomTasks; i++)
		{
			ddl += "\t\tVALUE UnscrewBottomBolt" + i + "() {\n\n"
					+ "\t\t\tp0 Arm.motions.SetOnBottomBolt" + i + "();\n"
					+ "\t\t\tDURING [0, +INF] [0, +INF] p0;\n\n"
					+ "\t\t\tt0 RobotTool.configuration.Tool2Mounted();\n"
					+ "\t\t\tDURING [0, +INF] [0, +INF] t0;\n\n"
					+ "\t\t\to0 Tool2.t2.UnscrewBolt();\n"
					+ "\t\t\tCONTAINS [0, +INF] [0, +INF] o0;\n"
					+ "\t\t}\n\n";
		}
		
		// get domain description part
		ddl += "\t}\n\n";
		return ddl;
	}
	
	/**
	 * 
	 * @return
	 */
	private String prepareToolChangeRules()
	{
		// get domain description
		return "\tSYNCHRONIZE RobotTool.configuration {\n\n"
				+ "\t\tVALUE Tool1Mounted() {\n\n"
				+ "\t\t\th0 Human.operator._MountTool1();\n"
				+ "\t\t\tMET-BY h0;\n"
				+ "\t\t\tm0 Arm.motions.SetOnBase();\n"
				+ "\t\t\th0 DURING [0, +INF] [0, +INF] m0;\n"
				+ "\t\t}\n\n"
				+ "\t\tVALUE Tool2Mounted() {\n\n"
				+ "\t\t\th0 Human.operator._MountTool2();\n"
				+ "\t\t\tMET-BY h0;\n"
				+ "\t\t\tm0 Arm.motions.SetOnBase();\n"
				+ "\t\t\th0 DURING [0, +INF] [0, +INF] m0;\n"
				+ "\t\t}\n"
				+ "\t}\n\n";
	}
	
	
	/**
	 * 
	 * @param domain
	 * @return
	 */
	private String prepareProblemDescription(String domain)
	{
		// get problem specification
		return "PROBLEM " + domain + "_PLANNING (DOMAIN " + domain + ") {\n\n"
				+ "\tfact0 <fact> Production.process.Idle() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact1 <fact> Human.operator.Idle() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact2 <fact> Robot.cobot.Idle() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact3 <fact> Arm.motions.SetOnBase() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact4 <fact> RobotTool.configuration.None() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact5 <fact> Tool1.t1.Idle() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\tfact6 <fact> Tool2.t2.Idle() AT [0, 0] [0, +INF] [1, +INF];\n"
				+ "\n\n"
				+ "\tgoal0 <goal> Production.process.Assembly() AT [0, +INF] [0, +INF] [1, +INF];\n\n"
				+ "\n\n"	
				+ "}\n\n";
	}
}

