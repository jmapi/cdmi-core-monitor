package pw.cdmi.om.protocol.cim.core;

import javax.cim.UnsignedInteger16;

public class CIM_ActiveConnection  extends CIM_SAPSAPDependency{
 private CIM_ServiceAccessPoint Antecedent;
 private CIM_ServiceAccessPoint Dependent;
 private UnsignedInteger16 TrafficType;//ValueMap
 private boolean IsUnidirectional;//
}
