package pw.cdmi.om.protocol.snmp.mib;

/************************************************************
 * "进程状态"和"进程信息"的综合数据
 * 即"进程CPU和内存"的类定义
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public class MibSWRunCpuAndMemEntry {
	private String index;

	private int hrSWRunIndex;

	private String hrSWRunName;

	private String hrSWRunID;

	private String hrSWRunPath;

	private String hrSWRunParameters;

	private int hrSWRunType;

	private int hrSWRunStatus;

	private int hrSWRunPerfCPU;

	private int hrSWRunPerfMem;

	public MibSWRunCpuAndMemEntry(String index, int hrSWRunIndex, String hrSWRunName, String hrSWRunID,
		String hrSWRunPath, String hrSWRunParameters, int hrSWRunType, int hrSWRunStatus, int hrSWRunPerfCPU,
		int hrSWRunPerfMem) {
		this.index = index;
		this.hrSWRunIndex = hrSWRunIndex;
		this.hrSWRunName = hrSWRunName;
		this.hrSWRunID = hrSWRunID;
		this.hrSWRunPath = hrSWRunPath;
		this.hrSWRunParameters = hrSWRunParameters;
		this.hrSWRunType = hrSWRunType;
		this.hrSWRunStatus = hrSWRunStatus;
		this.hrSWRunPerfCPU = hrSWRunPerfCPU;
		this.hrSWRunPerfMem = hrSWRunPerfMem;
	}
}
