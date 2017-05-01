package pw.cdmi.om.protocol.cim.device;

import java.util.Date;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalDevice;

public class CIM_Printer extends CIM_LogicalDevice{
	private String ElementName;
	private UnsignedInteger16 PrinterStatus;
	private UnsignedInteger16 DetectedErrorState;
	private String[] ErrorInformation;
	private UnsignedInteger16[] PaperSizesSupported;
	private String[] PaperTypesAvailable;
	private String DefaultPaperType;
	private String CurrentPaperType;
	private UnsignedInteger16 LanguagesSupported;
	private String[] MimeTypesSupported;
	private UnsignedInteger16 CurrentLanguage;
	private String CurrentMimeType;
	private UnsignedInteger16 DefaultLanguage;
	private String DefaultMimeType;
	private UnsignedInteger32 JobCountSinceLastReset;
	private Date TimeOfLastReset;
	private UnsignedInteger16[] Capabilities;
	private String[] CapabilityDescriptions;
	private UnsignedInteger16[] DefaultCapabilities;
	private UnsignedInteger16[] CurrentCapabilities;
	private UnsignedInteger32 MaxCopies;
	private UnsignedInteger32 DefaultCopies;
	private UnsignedInteger32 MaxNumberUp;
	private UnsignedInteger32 DefaultNumberUp;
	private UnsignedInteger32 HorizontalResolution;
	private UnsignedInteger32 VerticalResolution;
	private String[] CharSetsSupported;
	private String CurrentCharSet;
	private String[] NaturalLanguagesSupported;
	private String CurrentNaturalLanguage;
	private UnsignedInteger32 MaxSizeSupported;
	private String[] AvailableJobSheets;
	private UnsignedInteger16 MarkingTechnology;
	private String ConsoleNaturalLanguage;
	private String[] ConsoleDisplayBufferText;
}
