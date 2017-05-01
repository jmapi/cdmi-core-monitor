package pw.cdmi.om.protocol.cim.system;

import java.util.Date;

import javax.cim.UnsignedInteger64;

import pw.cdmi.om.protocol.cim.core.CIM_LogicalElement;

public class CIM_LogicalFile extends CIM_LogicalElement {
	private String CSCreationClassName;
	private String CSName;
	private String FSCreationClassName;
	private String FSName;
	private String CreationClassName;
	private String Name;
	private UnsignedInteger64 FileSize;
	private Date CreationDate;
	private Date LastModified;
	private Date LastAccessed;
	private boolean Readable;
	private boolean Writeable;
	private boolean Executable;
	private String CompressionMethod;
	private String EncryptionMethod;
	private UnsignedInteger64 InUseCount;
}
