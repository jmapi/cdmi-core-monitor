package pw.cdmi.om.protocol.cim.system;

import java.util.Date;

import javax.cim.UnsignedInteger16;

import pw.cdmi.om.protocol.cim.core.CIM_SettingData;

public class CIM_VirtualSystemSettingData extends CIM_SettingData {
	private String VirtualSystemIdentifier;
	private String VirtualSystemType;
	private String[] Notes;
	private Date CreationTime;
	private String ConfigurationID;
	private String ConfigurationDataRoot;
	private String ConfigurationFile;
	private String SnapshotDataRoot;
	private String SuspendDataRoot;
	private String SwapFileDataRoot;
	private String LogDataRoot;
	private UnsignedInteger16 AutomaticStartupAction;
	private Date AutomaticStartupActionDelay;
	private UnsignedInteger16 AutomaticStartupActionSequenceNumber;
	private UnsignedInteger16 AutomaticShutdownAction;
	private UnsignedInteger16 AutomaticRecoveryAction;
	private String RecoveryFile;
}
