package pw.cdmi.om.protocol.cim.physical;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_PhysicalElement;

public class CIM_PhysicalConnector extends CIM_PhysicalElement {
	private String ConnectorPinout;
	private UnsignedInteger16[] ConnectorType; 
	private String OtherTypeDescription;
	private UnsignedInteger16 ConnectorGender;
	private UnsignedInteger16[] ConnectorElectricalCharacteristics;
	private String[] OtherElectricalCharacteristics;
	private UnsignedInteger32 NumPhysicalPins;
	private UnsignedInteger16 ConnectorLayout;
	private String ConnectorDescription;
	
	public String getConnectorPinout() {
		return ConnectorPinout;
	}
	public UnsignedInteger16[] getConnectorType() {
		return ConnectorType;
	}
	public String getOtherTypeDescription() {
		return OtherTypeDescription;
	}
	public UnsignedInteger16 getConnectorGender() {
		return ConnectorGender;
	}
	public UnsignedInteger16[] getConnectorElectricalCharacteristics() {
		return ConnectorElectricalCharacteristics;
	}
	public String[] getOtherElectricalCharacteristics() {
		return OtherElectricalCharacteristics;
	}
	public UnsignedInteger32 getNumPhysicalPins() {
		return NumPhysicalPins;
	}
	public UnsignedInteger16 getConnectorLayout() {
		return ConnectorLayout;
	}
	public String getConnectorDescription() {
		return ConnectorDescription;
	}
}
