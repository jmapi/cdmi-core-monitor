package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger32;

public class CIM_StatisticalData extends CIM_ManagedElement {
	private String InstanceID;
	private String ElementName;
	private Date StartStatisticTime;
	private Date StatisticTime;
	private Date SampleInterval = new Date("00000000000000.000000:000");
	
	public UnsignedInteger32 ResetSelectedStats(String[] SelectedStatistics){
		return null;
	}
}
