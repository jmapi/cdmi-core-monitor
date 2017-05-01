package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_CallBasedModem extends CIM_Modem {
	private boolean FallbackEnabled;
	private UnsignedInteger16[] CompressionSupported;
	private String OtherCompressionDescription;
	private UnsignedInteger16 CompressionInfo;
	private String OtherCompressionInfoDescription;
	private UnsignedInteger16[] ModulationSupported;
	private String OtherModulationDescription;
	private UnsignedInteger16 ModulationScheme;
	private String OtherSchemeDescription;
	private UnsignedInteger16[] ErrorControlSupported;
	private String OtherErrorControlDescription;
	private UnsignedInteger16 ErrorControlInfo;
	private String OtherErrorControlInfoDescription;
	private Date TimeOfLastReset;
	private UnsignedInteger32 CallSpeed;
	private UnsignedInteger16 CallStatus;
	private UnsignedInteger32 CharsSent;
	private UnsignedInteger32 CharsReceived;
	private UnsignedInteger32 CharsLost;
	private UnsignedInteger32 BlocksSent;
	private UnsignedInteger32 BlocksResent;
	private UnsignedInteger32 BlocksReceived;
	private UnsignedInteger32 BlockErrors;
	private Date CallLength;
	private String NumberDialed;
	
	
	
	
}
