package pw.cdmi.om.protocol.snmp.storage;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.om.protocol.snmp.SNMPClient;
import pw.cdmi.om.protocol.snmp.SNMPTarget;
import pw.cdmi.om.protocol.snmp.mib.MibOID;
import pw.cdmi.om.protocol.snmp.mib.MibUSPV;

public class StorageHealthStatus {
	private Map<String, Object> result;

	public StorageHealthStatus(SNMPTarget target) throws IOException {
		MibOID[] tabOIDs = { MibUSPV.raidExMibDKCHWTable, MibUSPV.raidExMibDKUHWTable };
		SNMPClient snmpClient = null;
		try {
			snmpClient = new SNMPClient(target);
			result = snmpClient.getMibField(tabOIDs);
		} catch (RuntimeException e) {
			throw new IOException(e);
		} finally {
			snmpClient.close();
		}
	}

	/**
	 * 获取DiskController的健康状态
	 * 
	 * @return
	 */
	public DiskControllerHealth getDiskControllerHealth() {
		DiskControllerHealth health = new DiskControllerHealth();
		LinkedHashMap<String, String> linkmap = (LinkedHashMap<String, String>) result.get(MibUSPV.raidExMibDKCHWTable
			.getOID().toString());
		
		Iterator<String> list = linkmap.values().iterator();
		
		health.setDkcRaidListIndexSerialNumber(Integer.parseInt(list.next()));
		health.setDkcHWProcessor(Integer.parseInt(list.next()));
		health.setDkcHWCSW(Integer.parseInt(list.next()));
		health.setDkcHWCache(Integer.parseInt(list.next()));
		health.setDkcHWSM(Integer.parseInt(list.next()));
		health.setDkcHWPS(Integer.parseInt(list.next()));
		health.setDkcHWBattery(Integer.parseInt(list.next()));
		health.setDkcHWFan(Integer.parseInt(list.next()));
		health.setDkcHWEnvironment(Integer.parseInt(list.next()));
		return health;
	}
	
	/**
	 * 获取DiskController的健康状态
	 * 
	 * @return
	 */
	public DiskUnitHealth getDiskUnitHealth() {
		DiskUnitHealth health = new DiskUnitHealth();
		LinkedHashMap<String, String> linkmap = (LinkedHashMap<String, String>) result.get(MibUSPV.raidExMibDKUHWTable
			.getOID().toString());
		Iterator<String> list = linkmap.values().iterator();
		
		health.setDkuRaidListIndexSerialNumber(Integer.parseInt(list.next()));
		health.setDkuHWPS(Integer.parseInt(list.next()));
		health.setDkuHWFan(Integer.parseInt(list.next()));
		health.setDkuHWEnvironment(Integer.parseInt(list.next()));
		health.setDkuHWDrive(Integer.parseInt(list.next()));
		return health;
	}
	
	
	/**
	 * 生成json对象
	 * 
	 * @return
	 */
	public String toJSONString() {
		
		DiskControllerHealth diskControllerHealth = getDiskControllerHealth();
		
		JSONObject diskControllerHealthJsonName = new JSONObject();
		JSONObject diskControllerHealthJsonObject = new JSONObject();
		JSONArray diskControllerHealthJsonArray = new JSONArray();

		diskControllerHealthJsonObject.put("dkcRaidListIndexSerialNumber", diskControllerHealth.getDkcRaidListIndexSerialNumber());
		diskControllerHealthJsonObject.put("Status of processor", FailureStatus.fromValue(diskControllerHealth.getDkcHWProcessor()));
		diskControllerHealthJsonObject.put("Status of internal star", FailureStatus.fromValue(diskControllerHealth.getDkcHWCSW()));
		diskControllerHealthJsonObject.put("Status of cache", FailureStatus.fromValue(diskControllerHealth.getDkcHWCache()));
		diskControllerHealthJsonObject.put("Status of shared memory", FailureStatus.fromValue(diskControllerHealth.getDkcHWSM()));
		diskControllerHealthJsonObject.put("Status of power supply", FailureStatus.fromValue(diskControllerHealth.getDkcHWPS()));
		diskControllerHealthJsonObject.put("Status of battery", FailureStatus.fromValue(diskControllerHealth.getDkcHWBattery()));
		diskControllerHealthJsonObject.put("Status of fan", FailureStatus.fromValue(diskControllerHealth.getDkcHWFan()));
		diskControllerHealthJsonObject.put("Others", FailureStatus.fromValue(diskControllerHealth.getDkcHWEnvironment()));
		diskControllerHealthJsonArray.add(diskControllerHealthJsonObject);
		diskControllerHealthJsonName.put("name", "diskControllerHealth");
		diskControllerHealthJsonName.put("content", diskControllerHealthJsonArray);
		
		DiskUnitHealth diskUnitHealth = getDiskUnitHealth();
		
		JSONObject diskUnitHealthJsonName = new JSONObject();
		JSONObject diskUnitHealthJsonObject = new JSONObject();
		JSONArray diskUnitHealthJsonArray = new JSONArray();

		
		diskUnitHealthJsonObject.put("dkuRaidListIndexSerialNumber", diskUnitHealth.getDkuRaidListIndexSerialNumber());
		diskUnitHealthJsonObject.put("Status of power supply", FailureStatus.fromValue(diskUnitHealth.getDkuHWPS()));
		diskUnitHealthJsonObject.put("Status of fan", FailureStatus.fromValue(diskUnitHealth.getDkuHWFan()));
		diskUnitHealthJsonObject.put("Status of environment monitor", FailureStatus.fromValue(diskUnitHealth.getDkuHWEnvironment()));
		diskUnitHealthJsonObject.put("Status of drive", FailureStatus.fromValue(diskUnitHealth.getDkuHWDrive()));
		diskUnitHealthJsonArray.add(diskUnitHealthJsonObject);
		diskUnitHealthJsonName.put("name", "diskUnitHealth");
		diskUnitHealthJsonName.put("content", diskUnitHealthJsonArray);

		JSONArray workstationStatusJsonArray = new JSONArray();
		workstationStatusJsonArray.add(diskControllerHealthJsonName);
		workstationStatusJsonArray.add(diskUnitHealthJsonName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("WorkstationStatus", workstationStatusJsonArray);
		return jsonObject.toString();

	}
}
