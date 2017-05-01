package pw.cdmi.om.protocol.cim.core;

import java.util.Date;

import javax.cim.UnsignedInteger32;

public class CIM_Setting extends CIM_ManagedElement {
	private String SettingID;

	public UnsignedInteger32 VerifyOKToApplyToMSE(CIM_ManagedSystemElement MSE, Date TimeToApply, Date MustBeCompletedBy) {
		return null;
	}
	
	public UnsignedInteger32 ApplyToMSE(CIM_ManagedSystemElement MSE, Date TimeToApply, Date MustBeCompletedBy) {
		return null;
	}
	
	public UnsignedInteger32 VerifyOKToApplyToCollection(CIM_CollectionOfMSEs Collection, Date TimeToApply, Date MustBeCompletedBy,String[] CanNotApply) {
		return null;
	}
	
	public UnsignedInteger32 ApplyToCollection(CIM_CollectionOfMSEs Collection, Date TimeToApply, boolean ContinueOnError, Date MustBeCompletedBy,String[] CanNotApply) {
		return null;
	}
	
	public UnsignedInteger32 VerifyOKToApplyIncrementalChangeToMSE(CIM_ManagedSystemElement MSE, Date TimeToApply, Date MustBeCompletedBy,String[] PropertiesToApply) {
		return null;
	}
	
	public UnsignedInteger32 ApplyIncrementalChangeToMSE(CIM_ManagedSystemElement MSE, Date TimeToApply, Date MustBeCompletedBy,String[] PropertiesToApply) {
		return null;
	}
	
	public UnsignedInteger32 VerifyOKToApplyIncrementalChangeToCollection(CIM_CollectionOfMSEs Collection, Date TimeToApply, Date MustBeCompletedBy,String[] PropertiesToApply,String[] CanNotApply) {
		return null;
	}
	
	public UnsignedInteger32 ApplyIncrementalChangeToCollection(CIM_CollectionOfMSEs Collection, Date TimeToApply, boolean ContinueOnError, Date MustBeCompletedBy,String[] PropertiesToApply,String[] CanNotApply) {
		return null;
	}
}
