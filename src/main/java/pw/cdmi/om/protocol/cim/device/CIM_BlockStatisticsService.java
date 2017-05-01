package pw.cdmi.om.protocol.cim.device;

import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;

import pw.cdmi.om.protocol.cim.core.CIM_ConcreteJob;
import pw.cdmi.om.protocol.cim.core.CIM_StatisticsCollection;


public class CIM_BlockStatisticsService extends CIM_StatisticsService {
	private UnsignedInteger32 GetStatisticsCollection(CIM_ConcreteJob job, UnsignedInteger16[] ElementTypes,CIM_BlockStatisticsManifestCollection ManifestCollection,UnsignedInteger16 StatisticsFormat,String[] Statistics ){
		return null;
	}
	private UnsignedInteger32 CreateManifestCollection(CIM_StatisticsCollection Statistics, String ElementName,CIM_BlockStatisticsManifestCollection ManifestCollection){
		return null;
	}
	
	private UnsignedInteger32 AddOrModifyManifest(CIM_BlockStatisticsManifestCollection ManifestCollection, UnsignedInteger16 ElementType,String ElementName,String[] StatisticsList,CIM_BlockStatisticsManifest Manifest){
		return null;
	}
	
	private UnsignedInteger32 RemoveManifests(CIM_BlockStatisticsManifestCollection ManifestCollection, CIM_BlockStatisticsManifest[] Manifests){
		return null;
	}
}
