package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

public class CIM_AGPVideoController extends CIM_VideoController {
	private UnsignedInteger16[] Capabilities;
	private UnsignedInteger32 NonlocalVideoMemorySize;
	private UnsignedInteger32 LocalBusWidth;
	private UnsignedInteger16 UsageModel;
	private UnsignedInteger16 DataTransferRate;
	private UnsignedInteger16 AddressingMode;
	private UnsignedInteger32 MaximumAGPCommandQueuePath;
	private UnsignedInteger32 MaxNumberOfPipelinedAGPTransactions;
	private UnsignedInteger32 GraphicsApertureSize;
	private String AGPSpecificationVersionConformance;
}
