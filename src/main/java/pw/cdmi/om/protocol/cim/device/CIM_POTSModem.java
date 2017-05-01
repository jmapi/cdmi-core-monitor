package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.cim.UnsignedInteger8;

public class CIM_POTSModem extends CIM_CallBasedModem {
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] EnabledCapabilities;
	private UnsignedInteger32 MaxBaudRateToSerialPort;
	private UnsignedInteger32 MaxBaudRateToPhone;
	private boolean SupportsSynchronousConnect;
	private UnsignedInteger16 DialType;
	private UnsignedInteger32 InactivityTimeout;
	private UnsignedInteger16 SpeakerVolumeInfo;
	private String[] CountriesSupported;
	private String CountrySelected;
	private UnsignedInteger8 RingsBeforeAnswer;
	private UnsignedInteger16 MaxNumberOfPasswords;
	private String[] CurrentPasswords;
	private boolean SupportsCallback;
	private UnsignedInteger16 AnswerMode;
	private UnsignedInteger16 Equalization;
	private String[] BlackListedNumbers;
}
